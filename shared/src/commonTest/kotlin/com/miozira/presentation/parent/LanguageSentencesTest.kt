package com.miozira.presentation.parent

import com.miozira.content.LanguageId
import com.miozira.content.PrototypeContentRepository
import com.miozira.core.model.PairId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LanguageSentencesTest {
    private val names = PrototypeContentRepository.languageNames
    private val ta = LanguageId("ta")
    private val concepts = listOf("apple", "ball", "cup", "hand", "nose", "cat", "car", "water")

    private fun ev(vararg states: Pair<String, Pair<Boolean, Boolean>>) =
        states.map { PairEvidence(PairId("${it.first}:ta"), it.second.first, it.second.second) }

    private val heardTried = true to true
    private val heardOnly = true to false
    private val unheard = false to false

    @Test fun languageNeverHeard_isNotMetYet() {
        assertEquals("Tamil — not met yet.", describeLanguage(ta, ev("apple" to unheard), names))
        assertEquals("Tamil — not met yet.", describeLanguage(ta, emptyList(), names))
    }

    @Test fun heardButNeverTried() {
        val line = describeLanguage(ta, ev("apple" to heardOnly), names)
        assertTrue(line.startsWith("Tamil — he has not tried saying his Tamil words yet."))
    }

    @Test fun twoThirdsTried_isMost() {
        val e = ev(*concepts.take(4).map { it to heardTried }.toTypedArray(), "nose" to heardOnly, "cat" to heardOnly)
        assertTrue(describeLanguage(ta, e, names).startsWith("Tamil — he tries most of his Tamil words."))
    }

    @Test fun fewerTried_isSome() {
        val e = ev("apple" to heardTried, "ball" to heardTried, "cup" to heardOnly, "hand" to heardOnly, "nose" to heardOnly)
        assertTrue(describeLanguage(ta, e, names).startsWith("Tamil — he tries some of his Tamil words."))
    }

    @Test fun untriedWordsAreListed_withoutTheLanguageName() {
        val e = ev("apple" to heardTried, "nose" to heardOnly, "cat" to heardOnly, "car" to unheard)
        assertEquals(
            "Tamil — he tries some of his Tamil words. Still just listening: nose, cat.",
            describeLanguage(ta, e, names),
        )
        assertFalse(describeLanguage(ta, ev("apple" to heardTried), names).contains("Still just"))
    }

    @Test fun noLineContainsADigit_orAForbiddenWord() {
        val forbidden = listOf("missed", "failed", "wrong", "learned", "mastered")
        val cases = listOf(
            emptyList(),
            ev("apple" to heardOnly),
            ev("apple" to heardTried, "ball" to heardOnly),
            ev(*concepts.map { it to heardTried }.toTypedArray()),
        )
        for (c in cases) {
            val line = describeLanguage(ta, c, names)
            assertTrue(line.none { it.isDigit() }, line)
            assertTrue(forbidden.none { line.lowercase().contains(it) }, line)
        }
    }

    @Test fun languageOrderFollowsContent() {
        assertEquals(listOf(LanguageId("en"), ta), languageOrder)
    }
}
