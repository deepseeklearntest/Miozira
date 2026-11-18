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

    private val languageOf = mapOf("apple:en" to en, "ball:en" to en, "cup:ta" to ta)

    @Test fun aWordTriedOnceAndSilentOnce_countsAsTried() {
        val s = summarize(listOf("apple:en" to "ATTEMPT_DETECTED", "apple:en" to "NO_ATTEMPT_DETECTED"), languageOf)!!
        assertEquals(listOf(p("apple:en")), s.tried)
        assertEquals(emptyList(), s.listened)
        assertEquals(emptyList(), s.heardUnmeasured)
        assertEquals(listOf(en), s.languages)
    }

    @Test fun aSilentOnlyWord_isListened() {
        val s = summarize(listOf("ball:en" to "NO_ATTEMPT_DETECTED", "ball:en" to "NOT_MEASURED"), languageOf)!!
        assertEquals(emptyList(), s.tried)
        assertEquals(listOf(p("ball:en")), s.listened)
        assertEquals(emptyList(), s.heardUnmeasured)
    }

    @Test fun aMicrophoneUnavailableWord_isHeardUnmeasured() {
        val s = summarize(listOf("apple:en" to "NO_ATTEMPT_DETECTED", "cup:ta" to "MICROPHONE_UNAVAILABLE"), languageOf)!!
        assertEquals(emptyList(), s.tried)
        assertEquals(listOf(p("apple:en")), s.listened)
        assertEquals(listOf(p("cup:ta")), s.heardUnmeasured)
        assertEquals(listOf(en, ta), s.languages)
    }

    @Test fun emptyInput_givesNull() {
        assertEquals(null, summarize(emptyList(), languageOf))
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
