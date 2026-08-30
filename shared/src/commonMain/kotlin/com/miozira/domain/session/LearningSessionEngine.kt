package com.miozira.domain.session

import com.miozira.content.ConceptLanguagePair
import com.miozira.content.LanguageId
import com.miozira.core.model.InteractionId
import com.miozira.core.model.PairId
import com.miozira.core.model.SessionId
import com.miozira.core.result.AppError
import com.miozira.core.result.AppResult
import com.miozira.core.time.Clock

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

interface SessionPersistence {
    suspend fun createSession(snapshot: SessionSnapshot): AppResult<Unit>
    suspend fun createInteraction(plan: InteractionPlan): AppResult<Unit>
    suspend fun commitExposure(interactionId: InteractionId, committedAtMs: Long): AppResult<Unit>
    suspend fun recordAttempt(interactionId: InteractionId, state: com.miozira.core.model.AttemptState, recordedAtMs: Long): AppResult<Unit>
    suspend fun completeInteraction(interactionId: InteractionId, reason: com.miozira.core.model.InteractionCompletionReason, completedAtMs: Long): AppResult<Unit>
}

data class PairSelection(
    val pair: ConceptLanguagePair,
    val reason: String,
)

fun interface FixturePairSelector {
    fun selectNext(available: List<ConceptLanguagePair>, usedPairIds: Set<PairId>): ConceptLanguagePair?
}

fun interface PairPlanner {
    suspend fun selectNext(
        available: List<ConceptLanguagePair>,
        usedPairIds: Set<PairId>,
        interactionCount: Int,
    ): PairSelection?
}

class LearningSessionEngine(
    private val pairs: List<ConceptLanguagePair>,
    private val persistence: SessionPersistence,
    private val clock: Clock,
    private val selector: FixturePairSelector = FixturePairSelector { available, used -> available.firstOrNull { it.pairId !in used } },
    private val idGenerator: (String) -> String = { prefix -> "$prefix-${clock.nowMs()}" },
    private val planner: PairPlanner? = null,
) {
    private var session: SessionSnapshot? = null
    private val usedPairIds = linkedSetOf<PairId>()

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

    suspend fun beginInteraction(allowSpeakingInvite: Boolean = false): AppResult<InteractionPlan> {
        val current = session ?: return AppResult.Failure(AppError.InvalidState("Session has not started"))
        val selection = planner?.selectNext(pairs, usedPairIds, current.interactionCount)
            ?: selector.selectNext(pairs, usedPairIds)?.let { PairSelection(it, if (current.interactionCount == 0) "EASY_OPENER" else "FIXTURE_ORDER") }
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
                usedPairIds += selection.pair.pairId
                session = current.copy(interactionCount = current.interactionCount + 1)
                AppResult.Success(plan)
            }
        }
    }
}
