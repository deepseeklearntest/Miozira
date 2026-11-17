package com.miozira.content

import com.miozira.content.ContentReviewStatus.APPROVED
import com.miozira.content.ContentReviewStatus.DRAFT
import com.miozira.content.ContentReviewStatus.NATIVE_REVIEWED
import com.miozira.core.model.PairId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class LanguageReadinessTest {
    @Test fun bundledContent_hasEnglishAndTamilReady() =
        assertEquals(listOf(LanguageId("en"), LanguageId("ta")), PrototypeContentRepository.readyLanguages())

    @Test fun japaneseInDraft_isNotReady() =
        assertFalse(LanguageId("ja") in readyLanguages(eightConcepts("en", DRAFT) + eightConcepts("ja", DRAFT)))

    @Test fun japaneseNativeReviewed_isReady_andOrderIsKept() =
        assertEquals(
            listOf(LanguageId("en"), LanguageId("ja")),
            readyLanguages(eightConcepts("ja", NATIVE_REVIEWED) + eightConcepts("en", DRAFT)),
        )

    @Test fun oneUncheckedWord_keepsTheWholeLanguageOut() =
        assertFalse(
            LanguageId("ar") in readyLanguages(
                eightConcepts("ar", NATIVE_REVIEWED).drop(1) + fakePair("apple", "ar", DRAFT),
            ),
        )

    @Test fun sevenOfEightWords_isNotReady() =
        assertFalse(LanguageId("es") in readyLanguages(eightConcepts("es", APPROVED).drop(1)))

    private val concepts = listOf("apple", "ball", "cup", "hand", "nose", "cat", "car", "water")

    private fun eightConcepts(language: String, status: ContentReviewStatus) =
        concepts.map { fakePair(it, language, status) }

    private fun fakePair(concept: String, language: String, status: ContentReviewStatus) =
        ConceptLanguagePair(
            pairId = PairId("$concept:$language"),
            conceptId = ConceptId(concept),
            languageId = LanguageId(language),
            spokenForm = concept,
            visual = VisualAssetRef("visual.$concept", "drawable/$concept.png"),
            audio = CanonicalAudioAssetRef("audio.$concept.$language", "files/audio/$concept.$language.m4a", status),
        )
}
