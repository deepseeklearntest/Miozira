package com.miozira.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "concept",
    foreignKeys = [
        ForeignKey(
            entity = ContentVersionEntity::class,
            parentColumns = ["version_id"],
            childColumns = ["content_version_id"],
        ),
    ],
    indices = [Index(value = ["content_version_id"])],
)
data class ConceptEntity(
    @PrimaryKey
    @ColumnInfo(name = "concept_id")
    val conceptId: String,
    val category: String,
    @ColumnInfo(name = "primary_visual_asset_id")
    val primaryVisualAssetId: String,
    @ColumnInfo(name = "movement_eligible")
    val movementEligible: Boolean,
    @ColumnInfo(name = "content_version_id")
    val contentVersionId: String,
    @ColumnInfo(name = "is_enabled")
    val isEnabled: Boolean,
)
