package com.miozira.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "concept_language_pair",
    foreignKeys = [
        ForeignKey(ConceptEntity::class, ["concept_id"], ["concept_id"]),
        ForeignKey(LanguageEntity::class, ["language_id"], ["language_id"]),
        ForeignKey(ContentVersionEntity::class, ["version_id"], ["content_version_id"]),
    ],
    indices = [
        Index(value = ["concept_id", "language_id"], unique = true),
        Index(value = ["language_id"]),
        Index(value = ["content_version_id"]),
    ],
)
data class ConceptLanguagePairEntity(
    @PrimaryKey
    @ColumnInfo(name = "pair_id")
    val pairId: String,
    @ColumnInfo(name = "concept_id")
    val conceptId: String,
    @ColumnInfo(name = "language_id")
    val languageId: String,
    @ColumnInfo(name = "spoken_form")
    val spokenForm: String,
    @ColumnInfo(name = "canonical_audio_asset_id")
    val canonicalAudioAssetId: String,
    @ColumnInfo(name = "review_status")
    val reviewStatus: String,
    @ColumnInfo(name = "content_version_id")
    val contentVersionId: String,
    @ColumnInfo(name = "is_enabled")
    val isEnabled: Boolean,
)
