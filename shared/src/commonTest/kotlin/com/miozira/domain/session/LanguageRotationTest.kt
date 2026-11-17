package com.miozira.domain.session

import com.miozira.content.LanguageId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

private val five = listOf("en", "ta", "ja", "ar", "es").map(::LanguageId)
private fun ids(vararg v: String) = v.map(::LanguageId)

class LanguageRotationTest {
    @Test fun fiveLanguages_followTheStream() {
        val got = (0..4).map { LanguageRotation.languagesFor(it, five)!! }
        assertEquals(listOf("en" to "ta", "ja" to "ar", "es" to "en", "ta" to "ja", "ar" to "es"),
            got.map { it.first.value to it.second.value })
    }

    @Test fun anyFiveSessionsInARow_useEachLanguageTwice() {
        for (start in 0..30) {
            val used = (start until start + 5).flatMap { LanguageRotation.languagesFor(it, five)!!.toList() }
            five.forEach { lang -> assertEquals(2, used.count { it == lang }) }
        }
    }

    @Test fun neverTheSameLanguageTwiceInOneSession_whenTwoOrMoreAreReady() {
        for (n in 2..5) for (k in 0..40) LanguageRotation.languagesFor(k, five.take(n))!!.let { assertNotEquals(it.first, it.second) }
    }

    @Test fun fourLanguages_doNotAlwaysKeepTheSamePartner() {
        val partnersOfFirst = (0..7).map { LanguageRotation.languagesFor(it, five.take(4))!! }
            .filter { five[0] in it.toList() }.map { (it.toList() - five[0]).single() }.toSet()
        assertTrue(partnersOfFirst.size >= 2)
    }

    @Test fun twoLanguages_areAlwaysEnglishThenTamil() =
        assertEquals(LanguageId("en") to LanguageId("ta"), LanguageRotation.languagesFor(7, ids("en", "ta")))

    @Test fun oneLanguage_isUsedForBothBlocks() =
        assertEquals(LanguageId("en") to LanguageId("en"), LanguageRotation.languagesFor(3, ids("en")))

    @Test fun noLanguages_givesNull() = assertNull(LanguageRotation.languagesFor(0, emptyList()))
}
