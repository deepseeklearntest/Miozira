package com.miozira.data.repository

import com.miozira.core.model.InteractionId
import com.miozira.core.model.PairId
import com.miozira.core.model.SessionId
import com.miozira.core.result.AppError
import com.miozira.core.result.AppResult
import com.miozira.data.db.dao.SessionDao
import com.miozira.data.db.entity.InteractionEntity
import com.miozira.data.db.entity.SessionEntity
import com.miozira.domain.session.SessionEndReason
import com.miozira.domain.session.SessionHistory

class RoomSessionRepository(
    private val sessionDao: SessionDao,
) : SessionHistory {
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

    /** Idempotent: only a session that has not ended yet is changed. */
    suspend fun endSession(sessionId: SessionId, reason: SessionEndReason, endedAtMs: Long): AppResult<Unit> = writeSafely {
        sessionDao.endSession(sessionId.value, reason.name, endedAtMs)
    }

    override suspend fun completedSessionCount(): AppResult<Int> = readSafely { sessionDao.completedSessionCount() }

    override suspend fun lastCompletedAtMs(): AppResult<Long?> = readSafely { sessionDao.lastCompletedAtMs() }

    private suspend fun <T> readSafely(block: suspend () -> T): AppResult<T> = try {
        AppResult.Success(block())
    } catch (error: Exception) {
        AppResult.Failure(AppError.Unexpected("Could not read local session data"))
    }

    private suspend fun writeSafely(block: suspend () -> Unit): AppResult<Unit> = try {
        block()
        AppResult.Success(Unit)
    } catch (error: Exception) {
        AppResult.Failure(AppError.Unexpected("Could not save local session data"))
    }
}
