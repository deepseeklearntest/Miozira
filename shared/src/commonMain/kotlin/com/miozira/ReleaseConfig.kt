package com.miozira

import com.miozira.content.PrototypeContentRepository
import com.miozira.domain.adaptive.AdaptivePolicy
import com.miozira.services.microphone.SpeechAttemptConfig

/** Identifiers recorded with a family-test build so configuration cannot drift silently. */
data class PrototypeReleaseConfig(
    val appVersion: String,
    val contentVersion: String,
    val schemaVersion: Int,
    val adaptiveConfigVersion: String,
    val microphoneConfigVersion: String,
    val conceptCount: Int,
    val languageCount: Int,
    val pairCount: Int,
)

fun prototypeReleaseConfig(appVersion: String = "0.1.0"): PrototypeReleaseConfig {
    val pairs = PrototypeContentRepository.allPairs()
    return PrototypeReleaseConfig(
        appVersion = appVersion,
        contentVersion = PrototypeContentRepository.contentVersion,
        schemaVersion = 1,
        adaptiveConfigVersion = AdaptivePolicy().version,
        microphoneConfigVersion = SpeechAttemptConfig().version,
        conceptCount = pairs.map { it.conceptId.value }.distinct().size,
        languageCount = pairs.map { it.languageId.value }.distinct().size,
        pairCount = pairs.size,
    )
}
