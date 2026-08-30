package com.miozira.content

import com.miozira.core.model.PairId

object PrototypeContentRepository {
    const val contentVersion = "prototype-0.1-v1"

    private val visuals = mapOf(
        "apple" to visual("apple", "png"),
        "ball" to visual("ball", "png"),
        "cup" to visual("cup", "jpg"),
        "hand" to visual("hand", "jpg"),
        "nose" to visual("nose", "png"),
        "cat" to visual("cat", "png"),
        "car" to visual("car", "png"),
        "water" to visual("water", "jpg"),
    )

    private val pairs = listOf(
        pair("apple", "en", "apple"),
        pair("apple", "ta", "ஆப்பிள்"),
        pair("ball", "en", "ball"),
        pair("ball", "ta", "பந்து"),
        pair("cup", "en", "cup"),
        pair("cup", "ta", "கப்"),
        pair("hand", "en", "hand"),
        pair("hand", "ta", "கை"),
        pair("nose", "en", "nose"),
        pair("nose", "ta", "மூக்கு"),
        pair("cat", "en", "cat"),
        pair("cat", "ta", "பூனை"),
        pair("car", "en", "car"),
        pair("car", "ta", "கார்"),
        pair("water", "en", "water"),
        pair("water", "ta", "தண்ணீர்"),
    )

    fun resolve(conceptId: ConceptId, languageId: LanguageId): ConceptLanguagePair =
        pairs.firstOrNull { it.conceptId == conceptId && it.languageId == languageId }
            ?: error("No content pair for ${conceptId.value}:${languageId.value}")

    fun allPairs(): List<ConceptLanguagePair> = pairs

    private fun visual(concept: String, extension: String) = VisualAssetRef(
        logicalId = "visual.concept.$concept.draft",
        resourcePath = "drawable/concept_${concept}_draft.$extension",
    )

    private fun pair(
        concept: String,
        language: String,
        spokenForm: String,
    ) = ConceptLanguagePair(
        pairId = PairId("$concept:$language"),
        conceptId = ConceptId(concept),
        languageId = LanguageId(language),
        spokenForm = spokenForm,
        visual = visuals.getValue(concept),
        audio = CanonicalAudioAssetRef(
            logicalId = "audio.word.$concept.$language.draft",
            assetPath = "files/audio/word_${concept}_${language}_draft.m4a",
            reviewStatus = ContentReviewStatus.DRAFT,
        ),
    )
}
