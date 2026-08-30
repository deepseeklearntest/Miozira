package com.miozira.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "interaction",
    foreignKeys = [
        ForeignKey(
            entity = SessionEntity::class,
            parentColumns = ["session_id"],
            childColumns = ["session_id"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(ConceptLanguagePairEntity::class, ["pair_id"], ["pair_id"]),
    ],
    indices = [
        Index(value = ["session_id", "sequence_index"], unique = true),
        Index(value = ["pair_id"]),
        Index(value = ["pair_id", "exposure_committed"]),
        Index(value = ["completed_at_ms"]),
    ],
)
data class InteractionEntity(
    @PrimaryKey
    @ColumnInfo(name = "interaction_id")
    val interactionId: String,
    @ColumnInfo(name = "session_id")
    val sessionId: String,
    @ColumnInfo(name = "pair_id")
    val pairId: String,
    @ColumnInfo(name = "sequence_index")
    val sequenceIndex: Int,
    @ColumnInfo(name = "selection_reason")
    val selectionReason: String,
    @ColumnInfo(name = "started_at_ms")
    val startedAtMs: Long,
    @ColumnInfo(name = "completed_at_ms")
    val completedAtMs: Long?,
    @ColumnInfo(name = "completion_reason")
    val completionReason: String?,
    @ColumnInfo(name = "exposure_committed", defaultValue = "0")
    val exposureCommitted: Boolean = false,
    @ColumnInfo(name = "exposure_committed_at_ms")
    val exposureCommittedAtMs: Long?,
    @ColumnInfo(name = "replay_count", defaultValue = "0")
    val replayCount: Int = 0,
    @ColumnInfo(name = "attempt_state", defaultValue = "'NOT_MEASURED'")
    val attemptState: String = "NOT_MEASURED",
    @ColumnInfo(name = "attempt_recorded_at_ms")
    val attemptRecordedAtMs: Long?,
    @ColumnInfo(name = "planned", defaultValue = "1")
    val planned: Boolean = true,
)
