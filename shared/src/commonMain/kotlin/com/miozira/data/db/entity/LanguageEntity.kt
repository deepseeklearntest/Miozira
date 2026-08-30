package com.miozira.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "language",
    indices = [Index(value = ["language_code"], unique = true)],
)
data class LanguageEntity(
    @PrimaryKey
    @ColumnInfo(name = "language_id")
    val languageId: String,
    @ColumnInfo(name = "language_code")
    val languageCode: String,
    @ColumnInfo(name = "display_name")
    val displayName: String,
    @ColumnInfo(name = "is_enabled")
    val isEnabled: Boolean,
)
