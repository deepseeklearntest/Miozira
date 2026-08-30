package com.miozira.data.db

import com.miozira.content.ConceptId
import com.miozira.content.PrototypeContentRepository
import com.miozira.core.result.AppError
import com.miozira.core.result.AppResult
import com.miozira.data.db.dao.ContentDao
import com.miozira.data.db.entity.ConceptEntity
import com.miozira.data.db.entity.ConceptLanguagePairEntity
import com.miozira.data.db.entity.ContentVersionEntity
import com.miozira.data.db.entity.LanguageEntity
import com.miozira.data.db.entity.PairLearningStateEntity

data class ContentSeed(
    val contentVersion: ContentVersionEntity,
    val languages: List<LanguageEntity>,
    val concepts: List<ConceptEntity>,
    val pairs: List<ConceptLanguagePairEntity>,
    val pairStates: List<PairLearningStateEntity>,
)

class ContentSeedService(
    private val contentDao: ContentDao,
    private val nowMs: () -> Long,
) {
    suspend fun seedContent(version: String): AppResult<Unit> {
        if (version != PrototypeContentRepository.contentVersion) {
            return AppResult.Failure(
                AppError.InvalidState("Unknown bundled content version: $version"),
            )
        }

        return try {
            contentDao.seedContent(createSeed(version, nowMs()))
            AppResult.Success(Unit)
        } catch (error: Exception) {
            AppResult.Failure(AppError.Unexpected("Could not seed bundled content"))
        }
    }

    private fun createSeed(version: String, seededAtMs: Long): ContentSeed {
        val pairs = PrototypeContentRepository.allPairs()
        val concepts = pairs
            .distinctBy { it.conceptId }
            .map { pair ->
                ConceptEntity(
                    conceptId = pair.conceptId.value,
                    category = categories.getValue(pair.conceptId),
                    primaryVisualAssetId = pair.visual.logicalId,
                    movementEligible = false,
                    contentVersionId = version,
                    isEnabled = true,
                )
            }
        val languages = listOf(
            LanguageEntity("en", "en", "English", true),
            LanguageEntity("ta", "ta", "Tamil", true),
        )
        val pairEntities = pairs.map { pair ->
            ConceptLanguagePairEntity(
                pairId = pair.pairId.value,
                conceptId = pair.conceptId.value,
                languageId = pair.languageId.value,
                spokenForm = pair.spokenForm,
                canonicalAudioAssetId = pair.audio.logicalId,
                reviewStatus = pair.audio.reviewStatus.name,
                contentVersionId = version,
                isEnabled = true,
            )
        }

        return ContentSeed(
            contentVersion = ContentVersionEntity(
                versionId = version,
                createdAtMs = seededAtMs,
                description = "Prototype 0.1 bundled content",
            ),
            languages = languages,
            concepts = concepts,
            pairs = pairEntities,
            pairStates = pairEntities.map { pair ->
                PairLearningStateEntity(
                    pairId = pair.pairId,
                    learningState = "NEW",
                    nextDueAtMs = null,
                    restUntilMs = null,
                    lastExposedAtMs = null,
                    lastAttemptAtMs = null,
                    lastRealWorldEventAtMs = null,
                    updatedAtMs = seededAtMs,
                )
            },
        )
    }

    private companion object {
        val categories = mapOf(
            ConceptId("apple") to "food",
            ConceptId("ball") to "object/toy",
            ConceptId("cup") to "object/toy",
            ConceptId("hand") to "body",
            ConceptId("nose") to "body",
            ConceptId("cat") to "animal",
            ConceptId("car") to "object/toy",
            ConceptId("water") to "food",
        )
    }
}
