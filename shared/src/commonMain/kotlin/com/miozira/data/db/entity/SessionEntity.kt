package com.miozira.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "session",
    foreignKeys = [
        ForeignKey(LanguageEntity::class, ["language_id"], ["starting_language_id"]),
        ForeignKey(ContentVersionEntity::class, ["version_id"], ["content_version_id"]),
    ],
    indices = [
        Index(value = ["started_at_ms"]),
        Index(value = ["starting_language_id"]),
        Index(value = ["content_version_id"]),
    ],
)
data class SessionEntity(
    @PrimaryKey
    @ColumnInfo(name = "session_id")
    val sessionId: String,
    @ColumnInfo(name = "started_at_ms")
    val startedAtMs: Long,
    @ColumnInfo(name = "ended_at_ms")
    val endedAtMs: Long?,
    @ColumnInfo(name = "starting_language_id")
    val startingLanguageId: String?,
    @ColumnInfo(name = "completion_reason")
    val completionReason: String?,
    @ColumnInfo(name = "content_version_id")
    val contentVersionId: String,
    @ColumnInfo(name = "adaptive_config_version")
    val adaptiveConfigVersion: String,
    @ColumnInfo(name = "meaningful_activity", defaultValue = "0")
    val meaningfulActivity: Boolean = false,
    @ColumnInfo(name = "generated_suggestion_id")
    val generatedSuggestionId: String?,
)
