package com.miozira.domain.session

import com.miozira.content.LanguageId
import com.miozira.content.PrototypeContentRepository
import com.miozira.core.model.AttemptState
import com.miozira.core.model.InteractionCompletionReason
import com.miozira.core.model.InteractionId
import com.miozira.core.model.SessionId
import com.miozira.core.result.AppError
import com.miozira.core.result.AppResult
import com.miozira.core.time.Clock
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class LearningSessionEngineTest {
    private val en = LanguageId("en")
    private val ta = LanguageId("ta")
    private var nextId = 0

    private fun engine(persistence: RecordingPersistence) = LearningSessionEngine(
        pairs = PrototypeContentRepository.allPairs(),
        persistence = persistence,
        clock = Clock { 100L },
        idGenerator = { prefix -> "$prefix-${nextId++}" },
    )

    private fun word(language: LanguageId, part: SessionPart = SessionPart.FIRST_LANGUAGE) =
        SessionStep.Word(part, language, easy = false)

    @Test
    fun wordsComeFromTheRequestedLanguage() = runBlocking {
        val engine = engine(RecordingPersistence())
        engine.startSession()
        repeat(3) {
            val plan = assertIs<AppResult.Success<InteractionPlan>>(engine.beginInteraction(word(ta))).value
            assertEquals(ta, plan.pair.languageId)
        }
    }

    @Test
    fun aPairIsNotRepeatedWithinTwoTurns_butMayReturnLater() = runBlocking {
        val engine = engine(RecordingPersistence())
        engine.startSession()
        val chosen = (1..6).map {
            assertIs<AppResult.Success<InteractionPlan>>(engine.beginInteraction(word(en))).value.pair.pairId
        }
        chosen.forEachIndexed { i, id ->
            if (i >= 1) assertNotEquals(chosen[i - 1], id)
            if (i >= 2) assertNotEquals(chosen[i - 2], id)
        }
        assertTrue(chosen.toSet().size < chosen.size, "a pair should be able to come back later: $chosen")
    }

    @Test
    fun engineNeverRunsDry() = runBlocking {
        val engine = engine(RecordingPersistence())
        engine.startSession()
        repeat(40) { assertIs<AppResult.Success<InteractionPlan>>(engine.beginInteraction(word(en))) }
    }

    @Test
    fun endSession_isRecordedOnce_andANewSessionCanStart() = runBlocking {
        val persistence = RecordingPersistence()
        val engine = engine(persistence)
        val first = assertIs<AppResult.Success<SessionSnapshot>>(engine.startSession()).value
        engine.beginInteraction(word(en))

        assertIs<AppResult.Success<Unit>>(engine.endSession(SessionEndReason.COMPLETED))
        assertEquals(listOf(first.sessionId to SessionEndReason.COMPLETED), persistence.ended)
        assertIs<AppResult.Success<SessionSnapshot>>(engine.startSession())
        Unit
    }

    @Test
    fun endSession_withoutAnActiveSession_isInvalidState() = runBlocking {
        val persistence = RecordingPersistence()
        val result = engine(persistence).endSession(SessionEndReason.COMPLETED)
        assertIs<AppError.InvalidState>(assertIs<AppResult.Failure>(result).error)
        assertTrue(persistence.ended.isEmpty())
    }

    private class RecordingPersistence : SessionPersistence {
        val interactions = mutableListOf<InteractionPlan>()
        val ended = mutableListOf<Pair<SessionId, SessionEndReason>>()
        override suspend fun createSession(snapshot: SessionSnapshot) = AppResult.Success(Unit)
        override suspend fun createInteraction(plan: InteractionPlan): AppResult<Unit> {
            interactions += plan
            return AppResult.Success(Unit)
        }
        override suspend fun commitExposure(interactionId: InteractionId, committedAtMs: Long) = AppResult.Success(Unit)
        override suspend fun recordAttempt(interactionId: InteractionId, state: AttemptState, recordedAtMs: Long) = AppResult.Success(Unit)
        override suspend fun completeInteraction(interactionId: InteractionId, reason: InteractionCompletionReason, completedAtMs: Long) = AppResult.Success(Unit)
        override suspend fun endSession(sessionId: SessionId, reason: SessionEndReason, endedAtMs: Long): AppResult<Unit> {
            ended += sessionId to reason
            return AppResult.Success(Unit)
        }
    }
}
