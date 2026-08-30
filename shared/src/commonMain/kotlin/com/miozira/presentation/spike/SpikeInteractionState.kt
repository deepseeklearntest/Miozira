package com.miozira.presentation.spike

import com.miozira.core.model.InteractionId
import com.miozira.core.model.SessionId

data class SpikeInteractionState(
    val sessionId: SessionId,
    val interactionId: InteractionId,
    val exposureCommitted: Boolean = false,
) {
    fun onExposureThreshold(callbackInteractionId: InteractionId): SpikeInteractionState =
        if (callbackInteractionId == interactionId) copy(exposureCommitted = true) else this

    companion object {
        fun start(sessionId: SessionId, interactionId: InteractionId): SpikeInteractionState =
            SpikeInteractionState(sessionId, interactionId)
    }
}
