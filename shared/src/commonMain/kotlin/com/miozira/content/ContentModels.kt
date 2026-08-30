package com.miozira.content

import com.miozira.core.model.PairId

@JvmInline
value class ConceptId(val value: String)

@JvmInline
value class LanguageId(val value: String)

data class VisualAssetRef(
    val logicalId: String,
    val resourcePath: String,
)

data class CanonicalAudioAssetRef(
    val logicalId: String,
    val assetPath: String,
    val reviewStatus: ContentReviewStatus,
)

enum class ContentReviewStatus {
    DRAFT,
    FAMILY_REVIEWED,
    NATIVE_REVIEWED,
    APPROVED,
}

data class ConceptLanguagePair(
    val pairId: PairId,
    val conceptId: ConceptId,
    val languageId: LanguageId,
    val spokenForm: String,
    val visual: VisualAssetRef,
    val audio: CanonicalAudioAssetRef,
)
