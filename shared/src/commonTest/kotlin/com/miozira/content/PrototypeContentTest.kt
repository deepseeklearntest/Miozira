package com.miozira.content

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PrototypeContentTest {
    @Test
    fun prototypeContent_hasExactlyEightConcepts_andSixteenDistinctPairs() {
        val concepts = setOf("apple", "ball", "cup", "hand", "nose", "cat", "car", "water")
        val languages = setOf("en", "ta")
        val pairs = concepts.flatMap { concept ->
            languages.map { language ->
                PrototypeContentRepository.resolve(ConceptId(concept), LanguageId(language))
            }
        }

        assertEquals(16, pairs.size)
        assertEquals(16, pairs.map { it.pairId }.toSet().size)
        assertEquals(concepts, pairs.map { it.conceptId.value }.toSet())
        assertEquals(languages, pairs.map { it.languageId.value }.toSet())
        assertTrue(pairs.all { it.visual.resourcePath.startsWith("drawable/concept_") })
        assertTrue(pairs.all { it.audio.assetPath.startsWith("files/audio/word_") })
    }

    @Test
    fun tamilCupAndWater_useTheSpecifiedHouseholdForms() {
        assertEquals(
            "கப்",
            PrototypeContentRepository.resolve(ConceptId("cup"), LanguageId("ta")).spokenForm,
        )
        assertEquals(
            "தண்ணீர்",
            PrototypeContentRepository.resolve(ConceptId("water"), LanguageId("ta")).spokenForm,
        )
    }
}
