package com.miozira.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "pair_learning_state",
    foreignKeys = [ForeignKey(ConceptLanguagePairEntity::class, ["pair_id"], ["pair_id"])],
    indices = [Index(value = ["pair_id"])],
)
data class PairLearningStateEntity(
    @PrimaryKey
    @ColumnInfo(name = "pair_id")
    val pairId: String,
    @ColumnInfo(name = "learning_state")
    val learningState: String,
    @ColumnInfo(name = "next_due_at_ms")
    val nextDueAtMs: Long?,
    @ColumnInfo(name = "rest_until_ms")
    val restUntilMs: Long?,
    @ColumnInfo(name = "valid_exposure_count", defaultValue = "0")
    val validExposureCount: Int = 0,
    @ColumnInfo(name = "session_exposure_count", defaultValue = "0")
    val sessionExposureCount: Int = 0,
    @ColumnInfo(name = "attempt_detected_count", defaultValue = "0")
    val attemptDetectedCount: Int = 0,
    @ColumnInfo(name = "replay_count_total", defaultValue = "0")
    val replayCountTotal: Int = 0,
    @ColumnInfo(name = "real_world_recognition_count", defaultValue = "0")
    val realWorldRecognitionCount: Int = 0,
    @ColumnInfo(name = "real_world_use_count", defaultValue = "0")
    val realWorldUseCount: Int = 0,
    @ColumnInfo(name = "last_exposed_at_ms")
    val lastExposedAtMs: Long?,
    @ColumnInfo(name = "last_attempt_at_ms")
    val lastAttemptAtMs: Long?,
    @ColumnInfo(name = "last_real_world_event_at_ms")
    val lastRealWorldEventAtMs: Long?,
    @ColumnInfo(name = "low_interaction_encounter_count", defaultValue = "0")
    val lowInteractionEncounterCount: Int = 0,
    @ColumnInfo(name = "rest_count", defaultValue = "0")
    val restCount: Int = 0,
    @ColumnInfo(name = "updated_at_ms")
    val updatedAtMs: Long,
    @ColumnInfo(name = "state_version", defaultValue = "1")
    val stateVersion: Int = 1,
)
