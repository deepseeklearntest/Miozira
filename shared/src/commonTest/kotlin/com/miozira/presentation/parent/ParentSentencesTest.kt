package com.miozira.presentation.parent

import com.miozira.content.LanguageId
import com.miozira.content.PrototypeContentRepository
import com.miozira.core.model.PairId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ParentSentencesTest {
    private val names = PrototypeContentRepository.languageNames
    private val en = LanguageId("en")
    private val ta = LanguageId("ta")
    private fun p(id: String) = PairId(id)

    private val full = SessionSummary(
        languages = listOf(en, ta),
        tried = listOf(p("apple:en"), p("ball:en"), p("water:ta")),
        listened = listOf(p("nose:en"), p("cat:en")),
        heardUnmeasured = listOf(p("cup:ta")),
    )

    private val cases = listOf(
        null,
        full,
        SessionSummary(listOf(en), emptyList(), emptyList(), listOf(p("cup:en"))),
        SessionSummary(listOf(en), listOf(p("apple:en")), emptyList(), emptyList()),
        SessionSummary(emptyList(), emptyList(), emptyList(), emptyList()),
    )

    @Test fun noSession_givesTheEmptySentence() {
        assertEquals(listOf("Miozira will describe his sessions here."), describeSession(null, names))
    }

    @Test fun englishWordsAreBare_otherLanguagesAreNamed() {
        assertEquals("apple", wordLabel(p("apple:en"), names))
        assertEquals("water (Tamil)", wordLabel(p("water:ta"), names))
    }

    @Test fun triedAndListened_areSeparateLines() {
        assertEquals(
            listOf(
                "Most recent session: English and Tamil",
                "Tried saying: apple, ball, water (Tamil)",
                "Listened without speaking: nose, cat",
                "Heard: cup (Tamil)",
            ),
            describeSession(full, names),
        )
    }

    @Test fun unmeasuredWords_areDescribedAsHeard() {
        val lines = describeSession(SessionSummary(listOf(en), emptyList(), emptyList(), listOf(p("cup:en"))), names)
        assertEquals(listOf("Most recent session: English", "Heard: cup"), lines)
        assertTrue(lines.none { it.startsWith("Listened without speaking") })
    }

    @Test fun aWordTriedOnceAndSilentOnce_countsAsTried() {
        // The data source groups by pair: any ATTEMPT_DETECTED puts the pair in tried only.
        val lines = describeSession(SessionSummary(listOf(en), listOf(p("apple:en")), emptyList(), emptyList()), names)
        assertEquals(listOf("Most recent session: English", "Tried saying: apple"), lines)
    }

    @Test fun emptyGroups_produceNoLine() {
        val lines = describeSession(SessionSummary(listOf(ta), emptyList(), listOf(p("cat:ta")), emptyList()), names)
        assertEquals(listOf("Most recent session: Tamil", "Listened without speaking: cat (Tamil)"), lines)
    }

    @Test fun noParentSentenceContainsADigit() {
        cases.flatMap { describeSession(it, names) }.forEach { line ->
            assertTrue(line.none { it.isDigit() }, line)
        }
    }

    @Test fun noParentSentenceUsesAForbiddenWord() {
        val forbidden = listOf("missed", "failed", "wrong", "learned", "mastered")
        cases.flatMap { describeSession(it, names) }.forEach { line ->
            forbidden.forEach { assertFalse(line.contains(it, ignoreCase = true), line) }
        }
    }
}
