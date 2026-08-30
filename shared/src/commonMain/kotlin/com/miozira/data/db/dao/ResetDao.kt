package com.miozira.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface ResetDao {
    @Query("SELECT COUNT(*) FROM session")
    suspend fun sessionCount(): Int

    @Query("SELECT COUNT(*) FROM real_world_observation")
    suspend fun observationCount(): Int

    @Query("DELETE FROM interaction")
    suspend fun deleteInteractions()

    @Query("DELETE FROM session")
    suspend fun deleteSessions()

    @Query("DELETE FROM real_world_observation")
    suspend fun deleteObservations()

    @Query("DELETE FROM family_recording")
    suspend fun deleteFamilyRecordingMetadata(): Int

    @Query(
        "UPDATE pair_learning_state SET " +
            "learning_state = 'NEW', next_due_at_ms = NULL, rest_until_ms = NULL, " +
            "valid_exposure_count = 0, session_exposure_count = 0, attempt_detected_count = 0, " +
            "replay_count_total = 0, real_world_recognition_count = 0, real_world_use_count = 0, " +
            "last_exposed_at_ms = NULL, last_attempt_at_ms = NULL, last_real_world_event_at_ms = NULL, " +
            "low_interaction_encounter_count = 0, rest_count = 0, updated_at_ms = :updatedAtMs, state_version = 1",
    )
    suspend fun resetPairLearningStates(updatedAtMs: Long)

    @Transaction
    suspend fun clearStructuredLearningData(updatedAtMs: Long): ResetDeletion {
        val sessions = sessionCount()
        val observations = observationCount()
        deleteInteractions()
        deleteSessions()
        deleteObservations()
        resetPairLearningStates(updatedAtMs)
        return ResetDeletion(sessions, observations)
    }
}

data class ResetDeletion(
    val sessionsDeleted: Int,
    val observationsDeleted: Int,
)
