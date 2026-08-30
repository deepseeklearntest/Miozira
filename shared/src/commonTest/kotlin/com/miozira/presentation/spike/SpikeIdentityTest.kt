package com.miozira.presentation.spike

import com.miozira.core.model.InteractionId
import com.miozira.core.model.SessionId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SpikeIdentityTest {
    @Test
    fun recreation_keepsActiveSessionAndInteractionIds() {
        val state = SpikeInteractionState.start(SessionId("session-1"), InteractionId("interaction-1"))

        val recreated = state

        assertEquals(state.sessionId, recreated.sessionId)
        assertEquals(state.interactionId, recreated.interactionId)
    }

    @Test
    fun callbackForDifferentInteraction_cannotCommitExposure() {
        val state = SpikeInteractionState.start(SessionId("session-1"), InteractionId("interaction-1"))

        val afterStaleCallback = state.onExposureThreshold(InteractionId("interaction-old"))

        assertFalse(afterStaleCallback.exposureCommitted)
        assertTrue(state.onExposureThreshold(state.interactionId).exposureCommitted)
    }
}
