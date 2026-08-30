package com.miozira.domain.session

import com.miozira.content.PrototypeContentRepository
import com.miozira.core.model.AttemptState
import com.miozira.core.model.InteractionCompletionReason
import com.miozira.core.model.InteractionId
import com.miozira.core.result.AppResult
import com.miozira.core.time.Clock
import com.miozira.domain.adaptive.AdaptivePairPlanner
import com.miozira.domain.adaptive.LearningState
import com.miozira.domain.adaptive.PairLearningState
import com.miozira.domain.adaptive.ScheduledPairCandidate
import com.miozira.services.audio.AudioService
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

class AdaptiveSessionIntegrationTest {
    @Test
    fun engine_persistsAdaptiveSelectionReason() = runBlocking {
        val persistence = RecordingPersistence()
        val pairs = PrototypeContentRepository.allPairs()
        val candidates = pairs.mapIndexed { index, pair ->
            ScheduledPairCandidate(
                pair,
                PairLearningState(pair.pairId, if (index == 0) LearningState.FAMILIAR else LearningState.NEW, if (index == 0) 0L else null, null, 3, 1, 0, 0, 0),
            )
        }
        val engine = LearningSessionEngine(
            pairs = pairs,
            persistence = persistence,
            clock = Clock { 100L },
            planner = AdaptivePairPlanner({ candidates }, nowMs = { 100L }),
            idGenerator = { "$it-1" },
        )
        engine.startSession()
        engine.beginInteraction()
        assertEquals("EASY_OPENER", persistence.interactions.single().selectionReason)
        assertEquals("apple:en", persistence.interactions.single().pair.pairId.value)
    }

    private class RecordingPersistence : SessionPersistence {
        val interactions = mutableListOf<InteractionPlan>()
        override suspend fun createSession(snapshot: SessionSnapshot) = AppResult.Success(Unit)
        override suspend fun createInteraction(plan: InteractionPlan): AppResult<Unit> { interactions += plan; return AppResult.Success(Unit) }
        override suspend fun commitExposure(interactionId: InteractionId, committedAtMs: Long) = AppResult.Success(Unit)
        override suspend fun recordAttempt(interactionId: InteractionId, state: AttemptState, recordedAtMs: Long) = AppResult.Success(Unit)
        override suspend fun completeInteraction(interactionId: InteractionId, reason: InteractionCompletionReason, completedAtMs: Long) = AppResult.Success(Unit)
    }
}
