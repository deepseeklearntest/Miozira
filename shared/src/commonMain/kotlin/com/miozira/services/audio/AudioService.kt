package com.miozira.services.audio

import com.miozira.core.model.InteractionId
import com.miozira.core.model.PairId
import com.miozira.core.model.SessionId
import com.miozira.core.result.AppResult

data class AudioAssetRef(
    val logicalId: String,
    val assetPath: String,
)

data class WordPlaybackRequest(
    val sessionId: SessionId,
    val interactionId: InteractionId,
    val pairId: PairId,
    val assetRef: AudioAssetRef,
)

data class PlaybackHandle(
    val id: String,
    val sessionId: SessionId,
    val interactionId: InteractionId,
)

sealed interface AudioError {
    val message: String

    data class AssetUnavailable(override val message: String) : AudioError

    data class PlaybackFailed(override val message: String) : AudioError
}

interface AudioPlaybackListener {
    fun onStarted(handle: PlaybackHandle)

    fun onExposureThresholdReached(handle: PlaybackHandle)

    fun onCompleted(handle: PlaybackHandle)

    fun onFailed(handle: PlaybackHandle?, error: AudioError)
}

interface AudioService {
    suspend fun playCanonicalWord(
        request: WordPlaybackRequest,
        listener: AudioPlaybackListener,
    ): AppResult<PlaybackHandle>

    suspend fun stop(handle: PlaybackHandle): AppResult<Unit>

    suspend fun stopAll(): AppResult<Unit>
}
