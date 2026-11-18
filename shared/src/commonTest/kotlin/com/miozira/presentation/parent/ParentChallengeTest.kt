package com.miozira.presentation.parent

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ParentChallengeTest {
    private fun run(number: Int, vararg events: ChallengeEvent): ChallengeState =
        events.fold(ChallengeState(number)) { s, e -> ParentChallenge.reduce(s, e) }

    @Test fun numberWords_coverTheIrregularAndRegularForms() {
        assertEquals("ten", numberWords(10)); assertEquals("thirteen", numberWords(13)); assertEquals("nineteen", numberWords(19))
        assertEquals("twenty", numberWords(20)); assertEquals("forty-seven", numberWords(47)); assertEquals("ninety-nine", numberWords(99))
    }

    @Test fun numberWords_neverContainADigit() = (10..99).forEach { n -> assertTrue(numberWords(n).none(Char::isDigit)) }

    @Test fun numberWords_rejectOutOfRange() {
        assertFailsWith<IllegalArgumentException> { numberWords(9) }
        assertFailsWith<IllegalArgumentException> { numberWords(100) }
    }

    @Test fun typingTheNumber_isAccepted() {
        val s = run(47, ChallengeEvent.Digit(4), ChallengeEvent.Digit(7))
        assertEquals(ChallengeState(47, "47", ChallengeState.Outcome.ACCEPTED), s)
    }

    @Test fun typingAnotherNumber_isCancelled_withoutAnyOtherChange() {
        val s = run(47, ChallengeEvent.Digit(4), ChallengeEvent.Digit(8))
        assertEquals(ChallengeState(47, "48", ChallengeState.Outcome.CANCELLED), s)
    }

    @Test fun oneDigitThenTimeout_isCancelled() {
        val s = run(47, ChallengeEvent.Digit(4), ChallengeEvent.TimedOut)
        assertEquals(ChallengeState.Outcome.CANCELLED, s.outcome)
    }

    @Test fun oneDigit_hasNoOutcomeYet() {
        assertNull(run(47, ChallengeEvent.Digit(4)).outcome)
    }

    @Test fun afterAnOutcome_furtherEventsChangeNothing() {
        val accepted = run(47, ChallengeEvent.Digit(4), ChallengeEvent.Digit(7))
        assertEquals(accepted, ParentChallenge.reduce(accepted, ChallengeEvent.Digit(1)))
        assertEquals(accepted, ParentChallenge.reduce(accepted, ChallengeEvent.TimedOut))
        val cancelled = run(47, ChallengeEvent.TimedOut)
        assertEquals(cancelled, ParentChallenge.reduce(cancelled, ChallengeEvent.Digit(4)))
    }

    @Test fun start_neverRepeatsThePreviousNumber() {
        val s = ParentChallenge.start(47) { 47 }
        assertNotEquals(47, s.number)
        assertTrue(s.number in 10..99)
        assertEquals(10, ParentChallenge.start(99) { 99 }.number)
    }
}
