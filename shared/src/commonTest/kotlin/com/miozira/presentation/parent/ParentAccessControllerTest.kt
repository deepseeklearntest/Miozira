package com.miozira.presentation.parent

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotEquals

class ParentAccessControllerTest {
    private fun controller(vararg numbers: Int): ParentAccessController {
        val queue = numbers.toMutableList()
        return ParentAccessController(random = { queue.removeAt(0) })
    }

    private fun ParentAccessController.typeNumber(n: Int) = n.toString().forEach { digit(it - '0') }

    private fun ParentAccessController.number() = (state.value as ParentAccess.Challenge).challenge.number

    @Test fun startsLocked() {
        assertEquals(ParentAccess.Locked, controller().state.value)
    }

    @Test fun cornerHeld_showsAChallenge() {
        val c = controller(47)
        c.cornerHeld()
        assertEquals(47, c.number())
    }

    @Test fun correctDigits_open() {
        val c = controller(47)
        c.cornerHeld()
        c.typeNumber(47)
        assertEquals(ParentAccess.Open, c.state.value)
    }

    @Test fun wrongDigits_returnToLocked() {
        val c = controller(47)
        c.cornerHeld()
        c.typeNumber(48)
        assertEquals(ParentAccess.Locked, c.state.value)
    }

    @Test fun timeout_returnsToLocked() {
        val c = controller(47)
        c.cornerHeld()
        c.timedOut()
        assertEquals(ParentAccess.Locked, c.state.value)
    }

    @Test fun digitsWhileLocked_doNothing() {
        val c = controller()
        c.typeNumber(47)
        assertEquals(ParentAccess.Locked, c.state.value)
    }

    @Test fun cornerHeldWhileOpen_isIgnored() {
        val c = controller(47)
        c.cornerHeld()
        c.typeNumber(47)
        c.cornerHeld()
        assertEquals(ParentAccess.Open, c.state.value)
    }

    @Test fun lock_fromAnyState_returnsToLocked() {
        val c = controller(47, 52)
        c.lock()
        assertEquals(ParentAccess.Locked, c.state.value)
        c.cornerHeld()
        assertIs<ParentAccess.Challenge>(c.state.value)
        c.lock()
        assertEquals(ParentAccess.Locked, c.state.value)
        c.cornerHeld()
        c.typeNumber(52)
        assertEquals(ParentAccess.Open, c.state.value)
        c.lock()
        assertEquals(ParentAccess.Locked, c.state.value)
    }

    @Test fun aSecondChallenge_usesADifferentNumber() {
        val c = controller(47, 47)
        c.cornerHeld()
        val first = c.number()
        c.timedOut()
        c.cornerHeld()
        assertNotEquals(first, c.number())
    }
}
