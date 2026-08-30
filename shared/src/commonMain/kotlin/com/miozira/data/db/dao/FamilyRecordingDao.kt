package com.miozira.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.miozira.data.db.entity.FamilyRecordingEntity

@Dao
interface FamilyRecordingDao {
    @Query("SELECT * FROM family_recording WHERE pair_id = :pairId LIMIT 1")
    suspend fun find(pairId: String): FamilyRecordingEntity?

    @Query("SELECT * FROM family_recording ORDER BY updated_at_ms DESC")
    suspend fun all(): List<FamilyRecordingEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun replace(entity: FamilyRecordingEntity)

    @Query("DELETE FROM family_recording WHERE pair_id = :pairId")
    suspend fun delete(pairId: String): Int
}
