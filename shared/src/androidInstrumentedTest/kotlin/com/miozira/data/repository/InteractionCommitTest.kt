package com.miozira.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.miozira.content.PrototypeContentRepository
import com.miozira.core.model.AttemptState
import com.miozira.core.model.InteractionCompletionReason
import com.miozira.core.model.InteractionId
import com.miozira.core.model.PairId
import com.miozira.core.model.SessionId
import com.miozira.core.result.AppResult.Success
import com.miozira.data.db.ContentSeedService
import com.miozira.data.db.DatabaseFactory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InteractionCommitTest {
    @Test
    fun duplicateCallbacks_commitOneExposure_andKeepOneAttemptResult() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val databaseName = "interaction-commit-test.db"
        context.deleteDatabase(databaseName)
        val database = DatabaseFactory.create(context, databaseName)

        try {
            ContentSeedService(database.contentDao()) { 1_725_000_000_000L }
                .seedContent(PrototypeContentRepository.contentVersion)
            val sessions = RoomSessionRepository(database.sessionDao())
            val learning = RoomLearningRepository(database.interactionDao())
            val sessionId = SessionId("session-1")
            val interactionId = InteractionId("interaction-1")
            val pairId = PairId("apple:en")

            assertIs<Success<Unit>>(
                sessions.createSession(
                    sessionId = sessionId,
                    startedAtMs = 1_725_000_000_100L,
                    contentVersionId = PrototypeContentRepository.contentVersion,
                    adaptiveConfigVersion = "prototype-0.1-v1",
                ),
            )
            assertIs<Success<Unit>>(
                sessions.createInteraction(
                    interactionId = interactionId,
                    sessionId = sessionId,
                    pairId = pairId,
                    sequenceIndex = 0,
                    selectionReason = "NEW_INTRODUCTION",
                    startedAtMs = 1_725_000_000_200L,
                ),
            )

            learning.commitExposure(interactionId, 1_725_000_000_300L)
            learning.commitExposure(interactionId, 1_725_000_000_301L)
            learning.incrementReplay(interactionId)
            learning.incrementReplay(interactionId)
            learning.recordAttempt(
                interactionId,
                AttemptState.NO_ATTEMPT_DETECTED,
                1_725_000_000_400L,
            )
            learning.recordAttempt(
                interactionId,
                AttemptState.ATTEMPT_DETECTED,
                1_725_000_000_401L,
            )
            learning.completeInteraction(
                interactionId,
                InteractionCompletionReason.NORMAL,
                1_725_000_000_500L,
            )
            learning.completeInteraction(
                interactionId,
                InteractionCompletionReason.APP_BACKGROUND,
                1_725_000_000_501L,
            )

            val interaction = assertIs<Success<InteractionEvidenceSummary>>(
                learning.getInteractionEvidence(interactionId),
            ).value
            val pair = assertIs<Success<PairEvidenceSummary>>(
                learning.getPairEvidence(pairId),
            ).value

            assertEquals(true, interaction.exposureCommitted)
            assertEquals(2, interaction.replayCount)
            assertEquals(AttemptState.NO_ATTEMPT_DETECTED, interaction.attemptState)
            assertEquals(InteractionCompletionReason.NORMAL, interaction.completionReason)
            assertEquals(1, pair.validExposureCount)
            assertEquals(2, pair.replayCountTotal)
            assertEquals(0, pair.attemptDetectedCount)
        } finally {
            database.close()
            context.deleteDatabase(databaseName)
        }
        Unit
    }

    @Test
    fun interruptedAndConcurrentCallbacks_preserveOnlyActualExposure() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val databaseName = "interaction-interruption-test.db"
        context.deleteDatabase(databaseName)
        val database = DatabaseFactory.create(context, databaseName)

        try {
            ContentSeedService(database.contentDao()) { 1_725_000_000_000L }
                .seedContent(PrototypeContentRepository.contentVersion)
            val sessions = RoomSessionRepository(database.sessionDao())
            val learning = RoomLearningRepository(database.interactionDao())
            val sessionId = SessionId("session-2")
            val pairId = PairId("ball:en")
            sessions.createSession(
                sessionId,
                1_725_000_001_000L,
                PrototypeContentRepository.contentVersion,
                "prototype-0.1-v1",
            )
            val unseen = InteractionId("interaction-unseen")
            val exposed = InteractionId("interaction-exposed")
            val concurrent = InteractionId("interaction-concurrent")
            listOf(unseen, exposed, concurrent).forEachIndexed { index, interactionId ->
                sessions.createInteraction(
                    interactionId,
                    sessionId,
                    pairId,
                    index,
                    "NEW_INTRODUCTION",
                    1_725_000_001_100L + index,
                )
            }

            learning.commitExposure(exposed, 1_725_000_001_200L)
            (1L..8L).map { offset ->
                async(Dispatchers.Default) {
                    learning.commitExposure(concurrent, 1_725_000_001_300L + offset)
                }
            }.awaitAll()

            val unseenEvidence = assertIs<Success<InteractionEvidenceSummary>>(
                learning.getInteractionEvidence(unseen),
            ).value
            val exposedEvidence = assertIs<Success<InteractionEvidenceSummary>>(
                learning.getInteractionEvidence(exposed),
            ).value
            val pair = assertIs<Success<PairEvidenceSummary>>(
                learning.getPairEvidence(pairId),
            ).value

            assertEquals(false, unseenEvidence.exposureCommitted)
            assertEquals(true, exposedEvidence.exposureCommitted)
            assertEquals(null, exposedEvidence.completionReason)
            assertEquals(2, pair.validExposureCount)
        } finally {
            database.close()
            context.deleteDatabase(databaseName)
        }
        Unit
    }
}
