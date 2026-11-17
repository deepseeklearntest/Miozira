package com.miozira.domain.adaptive

import com.miozira.content.ConceptLanguagePair
import com.miozira.content.ConceptId
import com.miozira.content.LanguageId
import com.miozira.content.VisualAssetRef
import com.miozira.content.CanonicalAudioAssetRef
import com.miozira.content.ContentReviewStatus
import com.miozira.core.model.PairId
import com.miozira.core.result.AppResult
import com.miozira.domain.session.PairRequest
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class AdaptiveSelectionTest {
    private val engine = AdaptiveEngine()
    private val now = 1_000L

    @Test
    fun dueEmerging_beatsNew_andreturnsReason() {
        val result = engine.selectNext(context(
            candidate("new", LearningState.NEW),
            candidate("emerging", LearningState.EMERGING, dueAt = now),
        ))
        val decision = assertIs<AppResult.Success<SelectionDecision>>(result).value
        assertEquals("emerging:en", decision.pairId.value)
        assertEquals("DUE_EMERGING", decision.reason)
    }

    @Test
    fun restIsExcluded_andnewCapPreventsNoveltyDump() {
        val result = engine.selectNext(context(
            candidate("resting", LearningState.RESTING, restUntil = now + 100),
            candidate("new", LearningState.NEW),
        ).copy(newPairsSelected = 3))
        val decision = assertIs<AppResult.Success<SelectionDecision>>(result).value
        assertEquals("INSUFFICIENT_ALTERNATIVES", decision.reason)
    }

    @Test
    fun aThirdNewPairInABlock_isStillAllowed() {
        val result = engine.selectNext(context(
            candidate("new", LearningState.NEW),
        ).copy(newPairsSelected = 2))
        val decision = assertIs<AppResult.Success<SelectionDecision>>(result).value
        assertEquals("NEW_INTRODUCTION", decision.reason)
    }

    @Test
    fun candidatesOutsideTheRequestedLanguage_areNeverChosen() = runBlocking {
        val tamil = candidate("ball", LearningState.EMERGING, dueAt = now, language = "ta")
        val english = candidate("apple", LearningState.NEW)
        val planner = AdaptivePairPlanner({ listOf(tamil, english) }, nowMs = { now })
        val selection = planner.selectNext(
            PairRequest(
                available = listOf(tamil.pair, english.pair),
                recentPairIds = emptyList(),
                language = LanguageId("en"),
                easy = false,
                newPairsInBlock = 0,
            ),
        )
        assertEquals("apple:en", selection?.pair?.pairId?.value)
    }

    @Test
    fun samePairIsNotRepeatedWhenAlternativeExists() {
        val result = engine.selectNext(context(
            candidate("apple", LearningState.EMERGING, dueAt = now),
            candidate("ball", LearningState.EMERGING, dueAt = now),
        ).copy(recentPairIds = listOf(PairId("apple:en"))))
        val decision = assertIs<AppResult.Success<SelectionDecision>>(result).value
        assertEquals("ball:en", decision.pairId.value)
    }

    @Test
    fun familiarOpenerBeatsDueNewWhenHistoryExists() {
        val result = engine.selectNext(context(
            candidate("new", LearningState.NEW),
            candidate("familiar", LearningState.FAMILIAR, dueAt = now),
        ).copy(needsEasyOpener = true))
        val decision = assertIs<AppResult.Success<SelectionDecision>>(result).value
        assertEquals("familiar:en", decision.pairId.value)
        assertEquals("EASY_OPENER", decision.reason)
    }

    private fun context(vararg candidates: ScheduledPairCandidate) = AdaptiveSelectionContext(
        candidates = candidates.toList(),
        recentPairIds = emptyList(),
        newPairsSelected = 0,
        activeLanguageBlock = LanguageId("en"),
        nowMs = now,
    )

    private fun candidate(concept: String, state: LearningState, dueAt: Long? = null, restUntil: Long? = null, language: String = "en") =
        ScheduledPairCandidate(
            pair = pair(concept, language),
            state = PairLearningState(PairId("$concept:$language"), state, dueAt, restUntil, 0, 0, 0, 0, 0),
        )

    private fun pair(concept: String, language: String = "en") = ConceptLanguagePair(
        PairId("$concept:$language"), ConceptId(concept), LanguageId(language), concept,
        VisualAssetRef("visual.$concept", "drawable/$concept.png"),
        CanonicalAudioAssetRef("audio.$concept", "audio/$concept.m4a", ContentReviewStatus.APPROVED),
    )
}
