package com.miozira.services.audio

import com.miozira.core.result.AppError
import com.miozira.core.result.AppResult

class FakeAudioService : AudioService {
    private var activeHandle: PlaybackHandle? = null
    private var nextHandleId = 0

    val activeHandleCount: Int
        get() = if (activeHandle == null) 0 else 1

    override suspend fun playCanonicalWord(
        request: WordPlaybackRequest,
        listener: AudioPlaybackListener,
    ): AppResult<PlaybackHandle> {
        if (activeHandle != null) {
            return AppResult.Failure(AppError.InvalidState("Playback is already active"))
        }

        val handle = PlaybackHandle(
            id = "playback-${++nextHandleId}",
            sessionId = request.sessionId,
            interactionId = request.interactionId,
        )
        activeHandle = handle
        listener.onStarted(handle)
        return AppResult.Success(handle)
    }

    override suspend fun stop(handle: PlaybackHandle): AppResult<Unit> {
        if (activeHandle?.id == handle.id) {
            activeHandle = null
        }
        return AppResult.Success(Unit)
    }

    override suspend fun stopAll(): AppResult<Unit> {
        activeHandle = null
        return AppResult.Success(Unit)
    }
}

class RecordingListener : AudioPlaybackListener {
    val startedHandles = mutableListOf<PlaybackHandle>()

    override fun onStarted(handle: PlaybackHandle) {
        startedHandles += handle
    }

    override fun onExposureThresholdReached(handle: PlaybackHandle) = Unit

    override fun onCompleted(handle: PlaybackHandle) = Unit

    override fun onFailed(handle: PlaybackHandle?, error: AudioError) = Unit
}
