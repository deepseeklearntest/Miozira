package com.miozira.domain.session

import com.miozira.content.LanguageId

data class SessionShape(val warmUpTurns: Int = 2, val blockTurns: Int = 10, val endingTurns: Int = 1)

enum class SessionPart { WARM_UP, FIRST_LANGUAGE, SECOND_LANGUAGE, ENDING }

sealed interface SessionStep {
    data class Word(val part: SessionPart, val language: LanguageId, val easy: Boolean) : SessionStep
    data object Breath : SessionStep
}

fun sessionSteps(shape: SessionShape, first: LanguageId, second: LanguageId): List<SessionStep> = buildList {
    repeat(shape.warmUpTurns) { add(SessionStep.Word(SessionPart.WARM_UP, first, easy = true)) }
    repeat(shape.blockTurns) { add(SessionStep.Word(SessionPart.FIRST_LANGUAGE, first, easy = false)) }
    add(SessionStep.Breath)
    repeat(shape.blockTurns) { add(SessionStep.Word(SessionPart.SECOND_LANGUAGE, second, easy = false)) }
    repeat(shape.endingTurns) { add(SessionStep.Word(SessionPart.ENDING, second, easy = true)) }
}
