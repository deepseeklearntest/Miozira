package com.miozira.data.repository

import com.miozira.core.model.InteractionId
import com.miozira.core.model.PairId
import com.miozira.core.model.SessionId
import com.miozira.core.result.AppError
import com.miozira.core.result.AppResult
import com.miozira.data.db.dao.SessionDao
import com.miozira.data.db.entity.InteractionEntity
import com.miozira.data.db.entity.SessionEntity

class RoomSessionRepository(
    private val sessionDao: SessionDao,
) {
    suspend fun createSession(
        sessionId: SessionId,
        startedAtMs: Long,
        contentVersionId: String,
        adaptiveConfigVersion: String,
        startingLanguageId: String? = null,
    ): AppResult<Unit> = writeSafely {
        sessionDao.insertSessionIgnore(
            SessionEntity(
                sessionId = sessionId.value,
                startedAtMs = startedAtMs,
                endedAtMs = null,
                startingLanguageId = startingLanguageId,
                completionReason = null,
                contentVersionId = contentVersionId,
                adaptiveConfigVersion = adaptiveConfigVersion,
                generatedSuggestionId = null,
            ),
        )
    }

    suspend fun createInteraction(
        interactionId: InteractionId,
        sessionId: SessionId,
        pairId: PairId,
        sequenceIndex: Int,
        selectionReason: String,
        startedAtMs: Long,
    ): AppResult<Unit> = writeSafely {
        sessionDao.insertInteractionIgnore(
            InteractionEntity(
                interactionId = interactionId.value,
                sessionId = sessionId.value,
                pairId = pairId.value,
                sequenceIndex = sequenceIndex,
                selectionReason = selectionReason,
                startedAtMs = startedAtMs,
                completedAtMs = null,
                completionReason = null,
                exposureCommittedAtMs = null,
                attemptRecordedAtMs = null,
            ),
        )
    }

    private suspend fun writeSafely(block: suspend () -> Unit): AppResult<Unit> = try {
        block()
        AppResult.Success(Unit)
    } catch (error: Exception) {
        AppResult.Failure(AppError.Unexpected("Could not save local session data"))
    }
}
