package com.miozira.domain.session

import com.miozira.content.LanguageId
import kotlin.test.Test
import kotlin.test.assertEquals

class SessionStepsTest {
    @Test fun steps_followWarmUpFirstBreathSecondEnding() {
        val en = LanguageId("en"); val ta = LanguageId("ta")
        assertEquals(
            listOf(
                SessionStep.Word(SessionPart.WARM_UP, en, easy = true),
                SessionStep.Word(SessionPart.FIRST_LANGUAGE, en, easy = false),
                SessionStep.Word(SessionPart.FIRST_LANGUAGE, en, easy = false),
                SessionStep.Breath,
                SessionStep.Word(SessionPart.SECOND_LANGUAGE, ta, easy = false),
                SessionStep.Word(SessionPart.SECOND_LANGUAGE, ta, easy = false),
                SessionStep.Word(SessionPart.ENDING, ta, easy = true),
            ),
            sessionSteps(SessionShape(warmUpTurns = 1, blockTurns = 2, endingTurns = 1), en, ta),
        )
    }

    @Test fun defaultShape_isTwentyThreeWordsAndOneBreath() {
        val steps = sessionSteps(SessionShape(), LanguageId("en"), LanguageId("ta"))
        assertEquals(23, steps.count { it is SessionStep.Word })
        assertEquals(1, steps.count { it is SessionStep.Breath })
    }
}
