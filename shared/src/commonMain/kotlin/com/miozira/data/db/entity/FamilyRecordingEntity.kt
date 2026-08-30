package com.miozira.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "family_recording",
    foreignKeys = [ForeignKey(ConceptLanguagePairEntity::class, ["pair_id"], ["pair_id"])],
    indices = [
        Index(value = ["pair_id"], unique = true),
        Index(value = ["logical_file_id"], unique = true),
    ],
)
data class FamilyRecordingEntity(
    @PrimaryKey
    @ColumnInfo(name = "recording_id")
    val recordingId: String,
    @ColumnInfo(name = "pair_id")
    val pairId: String,
    @ColumnInfo(name = "logical_file_id")
    val logicalFileId: String,
    @ColumnInfo(name = "created_at_ms")
    val createdAtMs: Long,
    @ColumnInfo(name = "updated_at_ms")
    val updatedAtMs: Long,
    @ColumnInfo(name = "duration_ms")
    val durationMs: Long?,
    val format: String?,
    val status: String,
)
