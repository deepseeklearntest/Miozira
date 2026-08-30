package com.miozira.services.microphone

import com.miozira.core.model.InteractionId
import com.miozira.core.model.SessionId
import com.miozira.core.result.AppResult

data class SpeechAttemptRequest(
    val sessionId: SessionId,
    val interactionId: InteractionId,
    val durationMs: Long = DEFAULT_CAPTURE_DURATION_MS,
) {
    init {
        require(durationMs > 0)
    }

    private companion object {
        const val DEFAULT_CAPTURE_DURATION_MS = 2_500L
    }
}

data class SpeechAttemptHandle(
    val id: String,
    val sessionId: SessionId,
    val interactionId: InteractionId,
)

sealed interface MicrophoneError {
    val message: String

    data class PermissionDenied(override val message: String) : MicrophoneError

    data class Unavailable(override val message: String) : MicrophoneError
}

interface SpeechAttemptListener {
    fun onAttemptDetected(handle: SpeechAttemptHandle)

    fun onNoAttemptDetected(handle: SpeechAttemptHandle)

    fun onMicrophoneUnavailable(error: MicrophoneError)
}

/** Emits semantic activity only; this contract intentionally exposes no PCM, path, transcript, or score. */
interface SpeechAttemptDetector {
    suspend fun start(
        request: SpeechAttemptRequest,
        listener: SpeechAttemptListener,
    ): AppResult<SpeechAttemptHandle>

    suspend fun stop(handle: SpeechAttemptHandle): AppResult<Unit>

    suspend fun stopAll(): AppResult<Unit>
}
