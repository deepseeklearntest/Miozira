package com.miozira.services.audio

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.miozira.core.result.AppError
import com.miozira.core.result.AppResult

class AndroidAudioService(
    private val context: Context,
) : AudioService {
    private val mainHandler = Handler(Looper.getMainLooper())
    private var activePlayback: ActivePlayback? = null

    override suspend fun playCanonicalWord(
        request: WordPlaybackRequest,
        listener: AudioPlaybackListener,
    ): AppResult<PlaybackHandle> {
        if (activePlayback != null) {
            return AppResult.Failure(AppError.InvalidState("Playback is already active"))
        }

        val handle = PlaybackHandle(
            id = "${request.sessionId.value}:${request.interactionId.value}",
            sessionId = request.sessionId,
            interactionId = request.interactionId,
        )

        return try {
            val player = MediaPlayer()
            val descriptor = context.assets.openFd(request.assetRef.assetPath)
            descriptor.use {
                player.setDataSource(it.fileDescriptor, it.startOffset, it.length)
            }
            val active = ActivePlayback(handle, player, listener)
            activePlayback = active
            player.setOnPreparedListener {
                if (!isActive(handle)) return@setOnPreparedListener
                it.start()
                listener.onStarted(handle)
                scheduleExposureThreshold(active)
            }
            player.setOnCompletionListener {
                if (!isActive(handle)) return@setOnCompletionListener
                emitExposureThreshold(active)
                listener.onCompleted(handle)
                releaseActive(handle)
            }
            player.setOnErrorListener { _, what, extra ->
                if (isActive(handle)) {
                    listener.onFailed(
                        handle,
                        AudioError.PlaybackFailed("MediaPlayer error $what/$extra"),
                    )
                    releaseActive(handle)
                }
                true
            }
            player.prepareAsync()
            AppResult.Success(handle)
        } catch (exception: Exception) {
            listener.onFailed(
                null,
                AudioError.AssetUnavailable(exception.message ?: "Bundled spike audio is unavailable"),
            )
            releaseActive(handle)
            AppResult.Failure(AppError.Unavailable("Bundled spike audio is unavailable"))
        }
    }

    override suspend fun stop(handle: PlaybackHandle): AppResult<Unit> {
        releaseActive(handle)
        return AppResult.Success(Unit)
    }

    override suspend fun stopAll(): AppResult<Unit> {
        activePlayback?.let { releaseActive(it.handle) }
        return AppResult.Success(Unit)
    }

    private fun scheduleExposureThreshold(active: ActivePlayback) {
        val durationMs = active.player.duration.coerceAtLeast(1)
        val thresholdMs = (durationMs * EXPOSURE_THRESHOLD_FRACTION).toLong().coerceAtLeast(1)
        active.thresholdRunnable = Runnable { emitExposureThreshold(active) }
        mainHandler.postDelayed(active.thresholdRunnable!!, thresholdMs)
    }

    private fun emitExposureThreshold(active: ActivePlayback) {
        if (!isActive(active.handle) || active.exposureThresholdEmitted) return
        active.exposureThresholdEmitted = true
        active.listener.onExposureThresholdReached(active.handle)
    }

    private fun isActive(handle: PlaybackHandle): Boolean = activePlayback?.handle?.id == handle.id

    private fun releaseActive(handle: PlaybackHandle) {
        val active = activePlayback ?: return
        if (active.handle.id != handle.id) return
        active.thresholdRunnable?.let(mainHandler::removeCallbacks)
        activePlayback = null
        runCatching {
            if (active.player.isPlaying) active.player.stop()
            active.player.reset()
            active.player.release()
        }
    }

    private class ActivePlayback(
        val handle: PlaybackHandle,
        val player: MediaPlayer,
        val listener: AudioPlaybackListener,
        var exposureThresholdEmitted: Boolean = false,
        var thresholdRunnable: Runnable? = null,
    )

    private companion object {
        const val EXPOSURE_THRESHOLD_FRACTION = 0.8
    }
}
