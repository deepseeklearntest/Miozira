package com.miozira.domain.adaptive

import com.miozira.content.ConceptLanguagePair
import com.miozira.content.LanguageId
import com.miozira.core.model.PairId
import com.miozira.core.result.AppError
import com.miozira.core.result.AppResult
import com.miozira.domain.session.PairPlanner
import com.miozira.domain.session.PairRequest
import com.miozira.domain.session.PairSelection

data class ScheduledPairCandidate(
    val pair: ConceptLanguagePair,
    val state: PairLearningState,
)

data class AdaptiveSelectionContext(
    val candidates: List<ScheduledPairCandidate>,
    val recentPairIds: List<PairId>,
    val newPairsSelected: Int,
    val activeLanguageBlock: LanguageId?,
    val nowMs: Long,
    val needsEasyOpener: Boolean = false,
    val recoveringFromLowInteraction: Boolean = false,
)

data class SelectionDecision(
    val pairId: PairId,
    val reason: String,
)

class AdaptiveEngine(
    private val policy: AdaptivePolicy = AdaptivePolicy(),
) {
    fun selectNext(context: AdaptiveSelectionContext): AppResult<SelectionDecision> {
        val recent = context.recentPairIds.takeLast(2).toSet()
        val base = context.candidates.filter { candidate ->
            candidate.state.state != LearningState.RESTING &&
                candidate.state.restUntilMs?.let { it > context.nowMs } != true &&
                candidate.pair.pairId !in recent &&
                !(candidate.state.state == LearningState.NEW && context.newPairsSelected >= policy.maxNewPairsPerBlock)
        }
        if (base.isEmpty()) {
            val fallback = context.candidates.firstOrNull {
                it.state.state != LearningState.RESTING && it.state.restUntilMs?.let { until -> until > context.nowMs } != true
            } ?: return AppResult.Failure(AppError.Unavailable("No eligible pair remains"))
            return AppResult.Success(SelectionDecision(fallback.pair.pairId, "INSUFFICIENT_ALTERNATIVES"))
        }

        val ranked = base.sortedWith(compareByDescending<ScheduledPairCandidate> { score(it, context) }
            .thenBy { it.pair.pairId.value })
        val selected = ranked.first()
        return AppResult.Success(SelectionDecision(selected.pair.pairId, reason(selected, context)))
    }

    private fun score(candidate: ScheduledPairCandidate, context: AdaptiveSelectionContext): Int {
        val state = candidate.state
        var score = when {
            context.needsEasyOpener && state.state == LearningState.FAMILIAR -> 100
            context.recoveringFromLowInteraction && state.state == LearningState.FAMILIAR -> 90
            state.state == LearningState.EMERGING && isDue(state, context.nowMs) -> 80
            state.state == LearningState.FAMILIAR && isDue(state, context.nowMs) -> 70
            state.state == LearningState.NEW -> 50
            state.state == LearningState.FAMILIAR -> 30
            else -> 20
        }
        if (context.activeLanguageBlock != null && candidate.pair.languageId == context.activeLanguageBlock) score += 5
        if (candidate.pair.pairId in context.recentPairIds) score -= 30
        return score
    }

    private fun reason(candidate: ScheduledPairCandidate, context: AdaptiveSelectionContext): String = when {
        context.needsEasyOpener && candidate.state.state == LearningState.FAMILIAR -> "EASY_OPENER"
        context.recoveringFromLowInteraction && candidate.state.state == LearningState.FAMILIAR -> "RECOVERY_ITEM"
        candidate.state.state == LearningState.EMERGING && isDue(candidate.state, context.nowMs) -> "DUE_EMERGING"
        candidate.state.state == LearningState.FAMILIAR && isDue(candidate.state, context.nowMs) -> "DUE_FAMILIAR"
        candidate.state.state == LearningState.NEW -> "NEW_INTRODUCTION"
        else -> "LANGUAGE_BALANCE"
    }

    private fun isDue(state: PairLearningState, nowMs: Long): Boolean =
        state.nextDueAtMs == null || state.nextDueAtMs <= nowMs
}

class AdaptivePairPlanner(
    private val states: suspend () -> List<ScheduledPairCandidate>,
    private val nowMs: () -> Long,
    private val engine: AdaptiveEngine = AdaptiveEngine(),
) : PairPlanner {
    override suspend fun selectNext(request: PairRequest): PairSelection? {
        val available = request.available.filter { request.language == null || it.languageId == request.language }
        val candidatesById = states().associateBy { it.pair.pairId }
        val candidates = available.mapNotNull { candidatesById[it.pairId] }
        val decision = engine.selectNext(
            AdaptiveSelectionContext(
                candidates = candidates,
                recentPairIds = request.recentPairIds,
                newPairsSelected = request.newPairsInBlock,
                activeLanguageBlock = request.language,
                nowMs = nowMs(),
                needsEasyOpener = request.easy,
            ),
        ) as? AppResult.Success ?: return null
        val pair = available.firstOrNull { it.pairId == decision.value.pairId } ?: return null
        return PairSelection(pair, decision.value.reason)
    }
}
