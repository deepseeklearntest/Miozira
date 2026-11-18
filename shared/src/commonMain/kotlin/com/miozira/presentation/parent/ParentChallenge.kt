package com.miozira.presentation.parent

private val TEENS = listOf(
    "ten", "eleven", "twelve", "thirteen", "fourteen",
    "fifteen", "sixteen", "seventeen", "eighteen", "nineteen",
)
private val TENS = listOf("twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety")
private val UNITS = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

/** The number written as English words, e.g. 47 -> "forty-seven". Never contains a digit. */
fun numberWords(n: Int): String {
    require(n in 10..99) { "numberWords supports 10..99" }
    if (n < 20) return TEENS[n - 10]
    val tens = TENS[n / 10 - 2]
    return if (n % 10 == 0) tens else "$tens-${UNITS[n % 10 - 1]}"
}

data class ChallengeState(val number: Int, val typed: String = "", val outcome: Outcome? = null) {
    enum class Outcome { ACCEPTED, CANCELLED }

    val words: String get() = numberWords(number)
}

sealed interface ChallengeEvent {
    data class Digit(val digit: Int) : ChallengeEvent

    /** Ten seconds without input. */
    data object TimedOut : ChallengeEvent
}

object ParentChallenge {
    const val TIMEOUT_MS = 10_000L

    fun start(previousNumber: Int?, random: (IntRange) -> Int): ChallengeState {
        val drawn = random(10..99)
        val number = if (drawn == previousNumber) (if (drawn == 99) 10 else drawn + 1) else drawn
        return ChallengeState(number)
    }

    fun reduce(state: ChallengeState, event: ChallengeEvent): ChallengeState {
        if (state.outcome != null) return state
        return when (event) {
            ChallengeEvent.TimedOut -> state.copy(outcome = ChallengeState.Outcome.CANCELLED)
            is ChallengeEvent.Digit -> {
                val typed = state.typed + event.digit
                val outcome = when {
                    typed.length < 2 -> null
                    typed == state.number.toString() -> ChallengeState.Outcome.ACCEPTED
                    else -> ChallengeState.Outcome.CANCELLED
                }
                state.copy(typed = typed, outcome = outcome)
            }
        }
    }
}
