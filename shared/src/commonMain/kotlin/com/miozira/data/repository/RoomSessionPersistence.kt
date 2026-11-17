package com.miozira.data.repository

import com.miozira.core.model.AttemptState
import com.miozira.core.model.InteractionCompletionReason
import com.miozira.core.model.InteractionId
import com.miozira.core.model.SessionId
import com.miozira.core.result.AppResult
import com.miozira.core.time.Clock
import com.miozira.domain.session.InteractionPlan
import com.miozira.domain.session.SessionEndReason
import com.miozira.domain.session.SessionPersistence
import com.miozira.domain.session.SessionSnapshot

class RoomSessionPersistence(
    private val sessions: RoomSessionRepository,
    private val learning: RoomLearningRepository,
    private val clock: Clock,
) : SessionPersistence {
    override suspend fun createSession(snapshot: SessionSnapshot): AppResult<Unit> = sessions.createSession(
        sessionId = snapshot.sessionId,
        startedAtMs = snapshot.startedAtMs,
        contentVersionId = "prototype-0.1-v1",
        adaptiveConfigVersion = "prototype-0.1-v1",
        startingLanguageId = snapshot.startingLanguage?.value,
    )

    override suspend fun createInteraction(plan: InteractionPlan): AppResult<Unit> = sessions.createInteraction(
        interactionId = plan.interactionId,
        sessionId = plan.sessionId,
        pairId = plan.pair.pairId,
        sequenceIndex = plan.sequenceIndex,
        selectionReason = plan.selectionReason,
        startedAtMs = clock.nowMs(),
    )

    override suspend fun commitExposure(interactionId: InteractionId, committedAtMs: Long): AppResult<Unit> =
        learning.commitExposure(interactionId, committedAtMs)

    override suspend fun recordAttempt(interactionId: InteractionId, state: AttemptState, recordedAtMs: Long): AppResult<Unit> =
        learning.recordAttempt(interactionId, state, recordedAtMs)

    override suspend fun completeInteraction(
        interactionId: InteractionId,
        reason: InteractionCompletionReason,
        completedAtMs: Long,
    ): AppResult<Unit> = learning.completeInteraction(interactionId, reason, completedAtMs)

    override suspend fun endSession(sessionId: SessionId, reason: SessionEndReason, endedAtMs: Long): AppResult<Unit> =
        sessions.endSession(sessionId, reason, endedAtMs)
}
