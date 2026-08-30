package com.miozira.presentation.parent

import kotlin.math.hypot

data class ParentGateState(
    val phase: Phase = Phase.IDLE,
    val startedAtMs: Long? = null,
    val startX: Float? = null,
    val startY: Float? = null,
) {
    enum class Phase { IDLE, HOLDING, UNLOCKED }
}

sealed interface ParentGateEvent {
    data class PressStarted(val atMs: Long, val x: Float, val y: Float) : ParentGateEvent
    data class Moved(val x: Float, val y: Float) : ParentGateEvent
    data class Released(val atMs: Long) : ParentGateEvent
    data object Cancelled : ParentGateEvent
    data object Consumed : ParentGateEvent
}

object ParentGate {
    const val REQUIRED_HOLD_MS = 2_000L
    const val MAX_MOVEMENT_PX = 32f

    fun reduce(state: ParentGateState, event: ParentGateEvent): ParentGateState = when (event) {
        is ParentGateEvent.PressStarted -> ParentGateState(
            phase = ParentGateState.Phase.HOLDING,
            startedAtMs = event.atMs,
            startX = event.x,
            startY = event.y,
        )
        is ParentGateEvent.Moved -> if (state.phase == ParentGateState.Phase.HOLDING && movedTooFar(state, event.x, event.y)) {
            ParentGateState()
        } else state
        is ParentGateEvent.Released -> if (
            state.phase == ParentGateState.Phase.HOLDING &&
                state.startedAtMs != null &&
                event.atMs - state.startedAtMs >= REQUIRED_HOLD_MS
        ) state.copy(phase = ParentGateState.Phase.UNLOCKED) else ParentGateState()
        ParentGateEvent.Cancelled -> ParentGateState()
        ParentGateEvent.Consumed -> ParentGateState()
    }

    private fun movedTooFar(state: ParentGateState, x: Float, y: Float): Boolean =
        state.startX != null && state.startY != null && hypot(x - state.startX, y - state.startY) > MAX_MOVEMENT_PX
}
