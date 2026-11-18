package com.miozira.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.miozira.data.db.entity.InteractionEntity
import com.miozira.data.db.entity.SessionEntity

@Dao
interface SessionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSessionIgnore(session: SessionEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertInteractionIgnore(interaction: InteractionEntity)

    @Query("UPDATE session SET ended_at_ms = :endedAtMs, completion_reason = :reason WHERE session_id = :sessionId AND ended_at_ms IS NULL")
    suspend fun endSession(sessionId: String, reason: String, endedAtMs: Long)

    @Query("SELECT COUNT(*) FROM session WHERE completion_reason = 'COMPLETED'")
    suspend fun completedSessionCount(): Int

    @Query("SELECT MAX(ended_at_ms) FROM session WHERE completion_reason = 'COMPLETED'")
    suspend fun lastCompletedAtMs(): Long?
}
