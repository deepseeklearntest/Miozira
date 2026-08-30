package com.miozira.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.miozira.data.db.entity.SessionEntity

@Dao
interface RecoveryDao {
    @Query("SELECT * FROM session WHERE ended_at_ms IS NULL")
    suspend fun activeSessions(): List<SessionEntity>

    @Query("SELECT * FROM session WHERE session_id = :sessionId")
    suspend fun findSession(sessionId: String): SessionEntity?

    @Query(
        "UPDATE interaction SET completed_at_ms = :recoveredAtMs, completion_reason = 'PROCESS_INTERRUPTION' " +
            "WHERE session_id = :sessionId AND completed_at_ms IS NULL",
    )
    suspend fun closeIncompleteInteractions(sessionId: String, recoveredAtMs: Long): Int

    @Query(
        "UPDATE session SET ended_at_ms = :recoveredAtMs, completion_reason = 'APP_EXITED' " +
            "WHERE session_id = :sessionId AND ended_at_ms IS NULL",
    )
    suspend fun closeSession(sessionId: String, recoveredAtMs: Long): Int

    @Transaction
    suspend fun closeAllInterruptedSessions(recoveredAtMs: Long): RecoveryMutationResult {
        val sessions = activeSessions()
        var closedInteractions = 0
        sessions.forEach { session ->
            closedInteractions += closeIncompleteInteractions(session.sessionId, recoveredAtMs)
            closeSession(session.sessionId, recoveredAtMs)
        }
        return RecoveryMutationResult(sessions.size, closedInteractions)
    }
}

data class RecoveryMutationResult(
    val closedSessionCount: Int,
    val closedInteractionCount: Int,
)
