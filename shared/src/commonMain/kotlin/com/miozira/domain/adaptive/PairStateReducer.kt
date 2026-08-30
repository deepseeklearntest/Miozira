package com.miozira.domain.adaptive

import com.miozira.core.model.PairId

enum class LearningState { NEW, EMERGING, FAMILIAR, RESTING }

data class PairLearningEvidence(
    val pairId: PairId,
    val validExposureCount: Int,
    val distinctSessionCount: Int,
    val attemptDetectedCount: Int = 0,
    val replayCount: Int = 0,
    val recognitionCount: Int = 0,
    val useCount: Int = 0,
    val lowInteractionEncounterCount: Int = 0,
    val restCount: Int = 0,
    val lastExposedAtMs: Long? = null,
    val restUntilMs: Long? = null,
    val lastRealWorldEventAtMs: Long? = null,
)

data class PairLearningState(
    val pairId: PairId,
    val state: LearningState,
    val nextDueAtMs: Long?,
    val restUntilMs: Long?,
    val validExposureCount: Int,
    val attemptDetectedCount: Int,
    val replayCount: Int,
    val recognitionCount: Int,
    val useCount: Int,
)

fun recalculate(
    pairId: PairId,
    evidence: PairLearningEvidence,
    nowMs: Long,
    policy: AdaptivePolicy = AdaptivePolicy(),
): PairLearningState {
    require(pairId == evidence.pairId)
    val hasPositiveSignal = evidence.attemptDetectedCount > 0 ||
        evidence.replayCount > 0 || evidence.recognitionCount > 0 || evidence.useCount > 0
    val familiar = evidence.useCount > 0 && evidence.validExposureCount > 0 ||
        evidence.validExposureCount >= 3 && evidence.distinctSessionCount >= 2 && hasPositiveSignal
    val restActive = evidence.restUntilMs != null && evidence.restUntilMs > nowMs
    val repeatedLowInteraction = evidence.lowInteractionEncounterCount >= 3 && evidence.distinctSessionCount >= 2 &&
        evidence.recognitionCount == 0 && evidence.useCount == 0
    val shouldRest = restActive || repeatedLowInteraction
    val restUntil = when {
        restActive -> evidence.restUntilMs
        shouldRest -> nowMs + if (evidence.restCount > 0) policy.extendedRestMs else policy.defaultRestMs
        else -> null
    }
    val state = when {
        shouldRest -> LearningState.RESTING
        familiar -> LearningState.FAMILIAR
        evidence.validExposureCount > 0 -> LearningState.EMERGING
        else -> LearningState.NEW
    }
    val interval = when {
        evidence.useCount > 0 && evidence.validExposureCount > 0 -> policy.familiarWithUseIntervalMs
        state == LearningState.FAMILIAR -> policy.familiarIntervalMs
        state == LearningState.EMERGING -> policy.emergingIntervalMs
        else -> null
    }
    val nextDue = when {
        state == LearningState.RESTING -> null
        evidence.lastExposedAtMs == null || interval == null -> null
        else -> evidence.lastExposedAtMs + interval
    }
    return PairLearningState(
        pairId = pairId,
        state = state,
        nextDueAtMs = nextDue,
        restUntilMs = restUntil,
        validExposureCount = evidence.validExposureCount,
        attemptDetectedCount = evidence.attemptDetectedCount,
        replayCount = evidence.replayCount,
        recognitionCount = evidence.recognitionCount,
        useCount = evidence.useCount,
    )
}
