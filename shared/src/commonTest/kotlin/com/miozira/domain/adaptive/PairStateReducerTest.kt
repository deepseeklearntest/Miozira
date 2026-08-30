package com.miozira.domain.adaptive

import com.miozira.core.model.PairId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PairStateReducerTest {
    private val pair = PairId("apple:en")
    private val policy = AdaptivePolicy()

    @Test
    fun firstExposure_becomesEmerging_andDueTomorrow() {
        val state = recalculate(pair, PairLearningEvidence(pair, 1, 1, lastExposedAtMs = 1_000L), 1_000L, policy)
        assertEquals(LearningState.EMERGING, state.state)
        assertEquals(1_000L + policy.emergingIntervalMs, state.nextDueAtMs)
    }

    @Test
    fun threePositiveExposuresAcrossTwoSessions_becomesFamiliar_andDueInThreeDays() {
        val state = recalculate(
            pair,
            PairLearningEvidence(pair, 3, 2, attemptDetectedCount = 1, lastExposedAtMs = 1_000L),
            1_000L,
            policy,
        )
        assertEquals(LearningState.FAMILIAR, state.state)
        assertEquals(1_000L + policy.familiarIntervalMs, state.nextDueAtMs)
    }

    @Test
    fun realWorldUseWithOneExposure_shortcutsToFamiliar_andDueInFiveDays() {
        val state = recalculate(
            pair,
            PairLearningEvidence(pair, 1, 1, useCount = 1, lastExposedAtMs = 1_000L),
            1_000L,
            policy,
        )
        assertEquals(LearningState.FAMILIAR, state.state)
        assertEquals(1_000L + policy.familiarWithUseIntervalMs, state.nextDueAtMs)
    }

    @Test
    fun repeatedLowInteractionAcrossSessions_entersRest_andisExcludedFromDueSelection() {
        val state = recalculate(
            pair,
            PairLearningEvidence(pair, 2, 2, lowInteractionEncounterCount = 3, lastExposedAtMs = 1_000L),
            2_000L,
            policy,
        )
        assertEquals(LearningState.RESTING, state.state)
        assertNull(state.nextDueAtMs)
        assertEquals(2_000L + policy.defaultRestMs, state.restUntilMs)
    }

    @Test
    fun expiredRest_doesNotEraseHistory_andreturnsToEmerging() {
        val state = recalculate(
            pair,
            PairLearningEvidence(pair, 2, 2, restCount = 1, restUntilMs = 1_000L, lastExposedAtMs = 500L),
            2_000L,
            policy,
        )
        assertEquals(LearningState.EMERGING, state.state)
        assertEquals(2, state.validExposureCount)
    }

    @Test
    fun languagePairs_areIndependentInputs() {
        val tamil = PairId("apple:ta")
        val englishState = recalculate(pair, PairLearningEvidence(pair, 3, 2, attemptDetectedCount = 1), 0L, policy)
        val tamilState = recalculate(tamil, PairLearningEvidence(tamil, 0, 0), 0L, policy)
        assertEquals(LearningState.FAMILIAR, englishState.state)
        assertEquals(LearningState.NEW, tamilState.state)
    }
}
