package com.miozira.domain.session

import com.miozira.content.ConceptLanguagePair
import com.miozira.content.LanguageId
import com.miozira.core.model.InteractionId
import com.miozira.core.model.PairId
import com.miozira.core.model.SessionId
import com.miozira.core.result.AppError
import com.miozira.core.result.AppResult
import com.miozira.core.time.Clock
import com.miozira.domain.adaptive.AdaptivePolicy

data class SessionSnapshot(
    val sessionId: SessionId,
    val startedAtMs: Long,
    val startingLanguage: LanguageId?,
    val interactionCount: Int,
)

data class InteractionPlan(
    val interactionId: InteractionId,
    val sessionId: SessionId,
    val pair: ConceptLanguagePair,
    val sequenceIndex: Int,
    val selectionReason: String,
    val allowSpeakingInvite: Boolean,
)

enum class SessionEndReason { COMPLETED, IDLE, CONTENT_ERROR }

interface SessionPersistence {
    suspend fun createSession(snapshot: SessionSnapshot): AppResult<Unit>
    suspend fun createInteraction(plan: InteractionPlan): AppResult<Unit>
    suspend fun commitExposure(interactionId: InteractionId, committedAtMs: Long): AppResult<Unit>
    suspend fun recordAttempt(interactionId: InteractionId, state: com.miozira.core.model.AttemptState, recordedAtMs: Long): AppResult<Unit>
    suspend fun completeInteraction(interactionId: InteractionId, reason: com.miozira.core.model.InteractionCompletionReason, completedAtMs: Long): AppResult<Unit>
    suspend fun endSession(sessionId: SessionId, reason: SessionEndReason, endedAtMs: Long): AppResult<Unit>
}

interface SessionHistory {
    suspend fun completedSessionCount(): AppResult<Int>
    suspend fun lastCompletedAtMs(): AppResult<Long?>
}

data class PairSelection(
    val pair: ConceptLanguagePair,
    val reason: String,
)

data class PairRequest(
    val available: List<ConceptLanguagePair>,
    val recentPairIds: List<PairId>,
    val language: LanguageId?,
    val easy: Boolean,
    val newPairsInBlock: Int,
)

fun interface PairPlanner {
    suspend fun selectNext(request: PairRequest): PairSelection?
}

class LearningSessionEngine(
    private val pairs: List<ConceptLanguagePair>,
    private val persistence: SessionPersistence,
    private val clock: Clock,
    private val idGenerator: (String) -> String = { prefix -> "$prefix-${clock.nowMs()}" },
    private val planner: PairPlanner? = null,
    private val recentPairWindow: Int = AdaptivePolicy().recentPairWindow,
) {
    private var session: SessionSnapshot? = null
    private val history = mutableListOf<PairId>()
    private var currentPart: SessionPart? = null
    private var newPairsInBlock = 0

    suspend fun startSession(preferredStartingLanguage: LanguageId? = null): AppResult<SessionSnapshot> {
        if (session != null) return AppResult.Failure(AppError.InvalidState("Session already active"))
        val snapshot = SessionSnapshot(
            sessionId = SessionId(idGenerator("session")),
            startedAtMs = clock.nowMs(),
            startingLanguage = preferredStartingLanguage,
            interactionCount = 0,
        )
        return when (val result = persistence.createSession(snapshot)) {
            is AppResult.Failure -> result
            is AppResult.Success -> {
                session = snapshot
                AppResult.Success(snapshot)
            }
        }
    }

    suspend fun beginInteraction(step: SessionStep.Word, allowSpeakingInvite: Boolean = true): AppResult<InteractionPlan> {
        val current = session ?: return AppResult.Failure(AppError.InvalidState("Session has not started"))
        if (step.part != currentPart) {
            currentPart = step.part
            newPairsInBlock = 0
        }
        val request = PairRequest(
            available = pairs,
            recentPairIds = history.takeLast(recentPairWindow),
            language = step.language,
            easy = step.easy,
            newPairsInBlock = newPairsInBlock,
        )
        val selection = planner?.selectNext(request)
            ?: pairs.firstOrNull { it.languageId == step.language && it.pairId !in request.recentPairIds }
                ?.let { PairSelection(it, if (step.easy) "EASY_OPENER" else "FIXTURE_ORDER") }
            ?: return AppResult.Failure(AppError.Unavailable("No eligible pair remains"))
        val plan = InteractionPlan(
            interactionId = InteractionId(idGenerator("interaction")),
            sessionId = current.sessionId,
            pair = selection.pair,
            sequenceIndex = current.interactionCount,
            selectionReason = selection.reason,
            allowSpeakingInvite = allowSpeakingInvite,
        )
        return when (val result = persistence.createInteraction(plan)) {
            is AppResult.Failure -> result
            is AppResult.Success -> {
                history += selection.pair.pairId
                if (selection.reason == NEW_INTRODUCTION) newPairsInBlock++
                session = current.copy(interactionCount = current.interactionCount + 1)
                AppResult.Success(plan)
            }
        }
    }

    suspend fun endSession(reason: SessionEndReason): AppResult<Unit> {
        val current = session ?: return AppResult.Failure(AppError.InvalidState("Session has not started"))
        return when (val result = persistence.endSession(current.sessionId, reason, clock.nowMs())) {
            is AppResult.Failure -> result
            is AppResult.Success -> {
                session = null
                history.clear()
                currentPart = null
                newPairsInBlock = 0
                result
            }
        }
    }

    private companion object {
        const val NEW_INTRODUCTION = "NEW_INTRODUCTION"
    }
}
