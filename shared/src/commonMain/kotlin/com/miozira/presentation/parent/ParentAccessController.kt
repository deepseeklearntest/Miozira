package com.miozira.presentation.parent

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed interface ParentAccess {
    data object Locked : ParentAccess
    data class Challenge(val challenge: ChallengeState) : ParentAccess
    data object Open : ParentAccess
}

/** A friction boundary against a child's curiosity, not authentication. */
class ParentAccessController(private val random: (IntRange) -> Int = { it.random() }) {
    private val _state = MutableStateFlow<ParentAccess>(ParentAccess.Locked)
    val state: StateFlow<ParentAccess> = _state.asStateFlow()
    private var previousNumber: Int? = null

    fun cornerHeld() {
        if (_state.value != ParentAccess.Locked) return
        val challenge = ParentChallenge.start(previousNumber, random)
        previousNumber = challenge.number
        _state.value = ParentAccess.Challenge(challenge)
    }

    fun digit(digit: Int) = reduce(ChallengeEvent.Digit(digit))

    fun timedOut() = reduce(ChallengeEvent.TimedOut)

    fun lock() {
        _state.value = ParentAccess.Locked
    }

    private fun reduce(event: ChallengeEvent) {
        val current = _state.value as? ParentAccess.Challenge ?: return
        val next = ParentChallenge.reduce(current.challenge, event)
        _state.value = when (next.outcome) {
            ChallengeState.Outcome.ACCEPTED -> ParentAccess.Open
            ChallengeState.Outcome.CANCELLED -> ParentAccess.Locked
            null -> ParentAccess.Challenge(next)
        }
    }
}
