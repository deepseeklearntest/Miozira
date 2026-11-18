package com.miozira.presentation.parent

import com.miozira.content.LanguageId
import com.miozira.content.PrototypeContentRepository
import com.miozira.content.dailyMoments
import com.miozira.core.model.PairId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class WordsForTodayTest {
    private val names = PrototypeContentRepository.languageNames
    private val en = LanguageId("en")
    private val ta = LanguageId("ta")
    private fun p(id: String) = PairId(id)
    private fun summary(
        tried: List<String> = emptyList(),
        listened: List<String> = emptyList(),
        heard: List<String> = emptyList(),
    ) = SessionSummary(listOf(en, ta), tried.map(::p), listened.map(::p), heard.map(::p))

    @Test fun noSession_givesNoWords() {
        assertEquals(emptyList(), wordsForToday(null, names))
    }

    @Test fun atMostFourLines() {
        val s = summary(
            tried = listOf("apple:en", "ball:en", "cup:en"),
            listened = listOf("nose:en", "cat:en"),
            heard = listOf("car:en"),
        )
        assertEquals(4, wordsForToday(s, names).size)
        assertEquals(2, wordsForToday(s, names, max = 2).size)
    }

    @Test fun triedWordsComeFirst() {
        val s = summary(tried = listOf("water:ta"), listened = listOf("apple:en"), heard = listOf("cup:en"))
        assertEquals(
            listOf(
                "At breakfast, when you pour: water (Tamil)",
                "At snack time, handing a slice: apple",
                "At dinner, passing it: cup",
            ),
            wordsForToday(s, names),
        )
    }

    @Test fun oneLinePerConcept_evenWhenHeardInBothLanguages() {
        val s = summary(tried = listOf("apple:en"), listened = listOf("apple:ta"))
        assertEquals(listOf("At snack time, handing a slice: apple"), wordsForToday(s, names))
    }

    @Test fun aLineJoinsTheMomentAndTheWord() {
        assertEquals(
            listOf("At breakfast, when you pour: water (Tamil)"),
            wordsForToday(summary(tried = listOf("water:ta")), names),
        )
    }

    @Test fun everyConceptInTheBundledContentHasAMoment() {
        val concepts = PrototypeContentRepository.allPairs().map { it.conceptId }.toSet()
        assertTrue(dailyMoments.keys.containsAll(concepts))
    }

    @Test fun noLineContainsADigitOrForbiddenWord() {
        val all = PrototypeContentRepository.allPairs().map { it.pairId.value }
        val forbidden = listOf("missed", "failed", "wrong", "learned", "mastered")
        all.chunked(4).forEach { chunk ->
            wordsForToday(summary(tried = chunk), names).forEach { line ->
                assertFalse(line.any { it.isDigit() }, line)
                assertTrue(forbidden.none { it in line.lowercase() }, line)
            }
        }
    }
}
