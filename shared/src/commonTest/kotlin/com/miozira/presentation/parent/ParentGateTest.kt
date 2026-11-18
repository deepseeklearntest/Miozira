package com.miozira.presentation.parent

import kotlin.test.Test
import kotlin.test.assertEquals

class ParentGateTest {
    @Test
    fun shortTap_doesNotUnlock() {
        val holding = ParentGate.reduce(ParentGateState(), ParentGateEvent.PressStarted(1_000L, 0f, 0f))
        assertEquals(ParentGateState.Phase.IDLE, ParentGate.reduce(holding, ParentGateEvent.Released(1_100L)).phase)
    }

    @Test
    fun holdOfTwoPointNineSeconds_doesNotUnlock() {
        val holding = ParentGate.reduce(ParentGateState(), ParentGateEvent.PressStarted(0L, 0f, 0f))
        assertEquals(ParentGateState.Phase.IDLE, ParentGate.reduce(holding, ParentGateEvent.Released(2_999L)).phase)
    }

    @Test
    fun exactThreeSecondStableHold_unlocks() {
        val holding = ParentGate.reduce(ParentGateState(), ParentGateEvent.PressStarted(1_000L, 0f, 0f))
        assertEquals(ParentGateState.Phase.UNLOCKED, ParentGate.reduce(holding, ParentGateEvent.Released(4_000L)).phase)
    }

    @Test
    fun movementAndCancellation_abortHold() {
        val holding = ParentGate.reduce(ParentGateState(), ParentGateEvent.PressStarted(1_000L, 0f, 0f))
        val moved = ParentGate.reduce(holding, ParentGateEvent.Moved(40f, 0f))
        assertEquals(ParentGateState.Phase.IDLE, moved.phase)
        assertEquals(ParentGateState.Phase.IDLE, ParentGate.reduce(holding, ParentGateEvent.Cancelled).phase)
    }

    @Test
    fun unlockedState_isConsumedOnlyByExplicitEvent() {
        val unlocked = ParentGate.reduce(
            ParentGate.reduce(ParentGateState(), ParentGateEvent.PressStarted(0L, 0f, 0f)),
            ParentGateEvent.Released(ParentGate.REQUIRED_HOLD_MS),
        )
        assertEquals(ParentGateState.Phase.IDLE, ParentGate.reduce(unlocked, ParentGateEvent.Consumed).phase)
    }
}
