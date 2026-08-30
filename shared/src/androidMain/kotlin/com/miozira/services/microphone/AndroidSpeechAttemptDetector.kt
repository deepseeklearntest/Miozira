package com.miozira.services.microphone

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.SystemClock
import com.miozira.core.result.AppError
import com.miozira.core.result.AppResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AndroidSpeechAttemptDetector(
    private val context: Context,
) : SpeechAttemptDetector {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val lock = Any()
    private var activeCapture: ActiveCapture? = null

    override suspend fun start(
        request: SpeechAttemptRequest,
        listener: SpeechAttemptListener,
    ): AppResult<SpeechAttemptHandle> {
        if (context.checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            val error = MicrophoneError.PermissionDenied("Microphone permission is not granted")
            listener.onMicrophoneUnavailable(error)
            return AppResult.Failure(AppError.Unavailable(error.message))
        }

        synchronized(lock) {
            if (activeCapture != null) {
                return AppResult.Failure(AppError.InvalidState("Microphone capture is already active"))
            }

            val recorder = createRecorder()
                ?: return unavailable(listener, "AudioRecord could not be initialized")
            val handle = SpeechAttemptHandle(
                id = "${request.sessionId.value}:${request.interactionId.value}",
                sessionId = request.sessionId,
                interactionId = request.interactionId,
            )
            val capture = ActiveCapture(handle, recorder, listener)

            return try {
                recorder.startRecording()
                if (recorder.recordingState != AudioRecord.RECORDSTATE_RECORDING) {
                    release(capture)
                    unavailable(listener, "AudioRecord did not enter recording state")
                } else {
                    activeCapture = capture
                    capture.job = scope.launch {
                        captureWindow(capture, request.durationMs)
                    }
                    AppResult.Success(handle)
                }
            } catch (exception: Exception) {
                release(capture)
                unavailable(listener, exception.message ?: "AudioRecord could not start")
            }
        }
    }

    override suspend fun stop(handle: SpeechAttemptHandle): AppResult<Unit> {
        releaseIfActive { it.handle.id == handle.id }
        return AppResult.Success(Unit)
    }

    override suspend fun stopAll(): AppResult<Unit> {
        releaseIfActive { true }
        return AppResult.Success(Unit)
    }

    private suspend fun captureWindow(capture: ActiveCapture, durationMs: Long) {
        val classifier = EnergyAttemptClassifier()
        val frame = ShortArray(FRAME_SAMPLE_COUNT)
        val deadlineMs = SystemClock.elapsedRealtime() + durationMs

        while (SystemClock.elapsedRealtime() < deadlineMs && isActive(capture)) {
            val readCount = capture.recorder.read(
                frame,
                0,
                frame.size,
                AudioRecord.READ_NON_BLOCKING,
            )
            when {
                readCount > 0 && classifier.observe(frame, readCount) -> {
                    finish(capture) { capture.listener.onAttemptDetected(capture.handle) }
                    return
                }

                readCount < 0 -> {
                    finish(capture) {
                        capture.listener.onMicrophoneUnavailable(
                            MicrophoneError.Unavailable("AudioRecord read failed: $readCount"),
                        )
                    }
                    return
                }

                readCount == 0 -> delay(IDLE_READ_DELAY_MS)
            }
        }

        finish(capture) { capture.listener.onNoAttemptDetected(capture.handle) }
    }

    private fun createRecorder(): AudioRecord? {
        val minBufferBytes = AudioRecord.getMinBufferSize(
            SAMPLE_RATE_HZ,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
        )
        if (minBufferBytes <= 0) return null

        return AudioRecord.Builder()
            .setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION)
            .setAudioFormat(
                AudioFormat.Builder()
                    .setSampleRate(SAMPLE_RATE_HZ)
                    .setChannelMask(AudioFormat.CHANNEL_IN_MONO)
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .build(),
            )
            .setBufferSizeInBytes(maxOf(minBufferBytes, FRAME_SAMPLE_COUNT * Short.SIZE_BYTES))
            .build()
            .takeIf { it.state == AudioRecord.STATE_INITIALIZED }
    }

    private fun finish(capture: ActiveCapture, callback: () -> Unit) {
        val ownsCapture = synchronized(lock) {
            if (activeCapture !== capture) false else {
                activeCapture = null
                true
            }
        }
        if (!ownsCapture) return
        release(capture)
        callback()
    }

    private fun releaseIfActive(matches: (ActiveCapture) -> Boolean) {
        val capture = synchronized(lock) {
            activeCapture?.takeIf(matches)?.also { activeCapture = null }
        } ?: return
        release(capture)
    }

    private fun isActive(capture: ActiveCapture): Boolean = synchronized(lock) { activeCapture === capture }

    private fun release(capture: ActiveCapture) {
        capture.job?.cancel()
        runCatching {
            if (capture.recorder.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                capture.recorder.stop()
            }
            capture.recorder.release()
        }
    }

    private fun unavailable(
        listener: SpeechAttemptListener,
        message: String,
    ): AppResult<Nothing> {
        listener.onMicrophoneUnavailable(MicrophoneError.Unavailable(message))
        return AppResult.Failure(AppError.Unavailable(message))
    }

    private class ActiveCapture(
        val handle: SpeechAttemptHandle,
        val recorder: AudioRecord,
        val listener: SpeechAttemptListener,
        var job: Job? = null,
    )

    private companion object {
        const val SAMPLE_RATE_HZ = 16_000
        const val FRAME_SAMPLE_COUNT = 400 // 25 ms at 16 kHz.
        const val IDLE_READ_DELAY_MS = 10L
    }
}
