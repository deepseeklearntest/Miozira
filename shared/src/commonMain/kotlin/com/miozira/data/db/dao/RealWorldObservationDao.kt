package com.miozira.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.miozira.data.db.entity.RealWorldObservationEntity

@Dao
interface RealWorldObservationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIgnore(observation: RealWorldObservationEntity): Long

    @Query(
        "UPDATE pair_learning_state SET " +
            "real_world_recognition_count = real_world_recognition_count + CASE WHEN :type = 'RECOGNIZED_OUTSIDE_APP' THEN 1 ELSE 0 END, " +
            "real_world_use_count = real_world_use_count + CASE WHEN :type = 'USED_OUTSIDE_APP' THEN 1 ELSE 0 END, " +
            "last_real_world_event_at_ms = :recordedAtMs, updated_at_ms = :recordedAtMs " +
            "WHERE pair_id = :pairId",
    )
    suspend fun updatePairEvidence(pairId: String, type: String, recordedAtMs: Long): Int

    @Query(
        "SELECT * FROM real_world_observation WHERE pair_id = :pairId AND observation_type = :type LIMIT 1",
    )
    suspend fun find(pairId: String, type: String): RealWorldObservationEntity?
}
