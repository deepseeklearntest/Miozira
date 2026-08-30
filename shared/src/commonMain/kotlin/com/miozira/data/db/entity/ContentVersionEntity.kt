package com.miozira.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "content_version")
data class ContentVersionEntity(
    @PrimaryKey
    @ColumnInfo(name = "version_id")
    val versionId: String,
    @ColumnInfo(name = "created_at_ms")
    val createdAtMs: Long,
    val description: String?,
)
