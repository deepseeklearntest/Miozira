package com.miozira.services.recording

import android.media.MediaRecorder
import android.os.SystemClock
import com.miozira.core.model.PairId
import com.miozira.core.result.AppError
import com.miozira.core.result.AppResult
import com.miozira.services.files.AndroidPrivateFileStore
import com.miozira.services.files.FileStore
import com.miozira.services.files.PrivateFileId
import com.miozira.services.microphone.SpeechAttemptDetector
import java.util.UUID

class AndroidFamilyRecordingService(
    private val fileStore: FileStore,
    private val privateFileStore: AndroidPrivateFileStore,
    private val childDetector: SpeechAttemptDetector? = null,
) : FamilyRecordingService {
    private val lock = Any()
    private var active: ActiveRecording? = null

    override suspend fun start(pairId: PairId): AppResult<RecordingSession> {
        childDetector?.stopAll()
        synchronized(lock) {
            if (active != null) return AppResult.Failure(AppError.InvalidState("A recording is already active"))
        }
        val fileId = when (val result = fileStore.createTemporary(pairId.value)) {
            is AppResult.Failure -> return result
            is AppResult.Success -> result.value
        }
        val file = privateFileStore.temporaryFile(fileId)
            ?: return AppResult.Failure(AppError.Unavailable("Private recording file is unavailable"))
        return try {
            val recorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setMaxDuration(MAX_DURATION_MS.toInt())
                setOutputFile(file.absolutePath)
                prepare()
                start()
            }
            val session = RecordingSession(UUID.randomUUID().toString(), pairId, fileId)
            synchronized(lock) { active = ActiveRecording(session, recorder, SystemClock.elapsedRealtime()) }
            AppResult.Success(session)
        } catch (error: Exception) {
            fileStore.delete(fileId)
            AppResult.Failure(AppError.Unavailable("Could not start family recording"))
        }
    }

    override suspend fun stop(session: RecordingSession): AppResult<RecordingPreview> {
        val recording = synchronized(lock) { active?.takeIf { it.session.id == session.id } ?: return failure() }
        return try {
            recording.recorder.stop()
            recording.recorder.release()
            synchronized(lock) { active = null }
            AppResult.Success(RecordingPreview(session, (SystemClock.elapsedRealtime() - recording.startedAtMs).coerceAtMost(MAX_DURATION_MS)))
        } catch (error: Exception) {
            synchronized(lock) { active = null }
            recording.recorder.release()
            fileStore.delete(session.temporaryFileId)
            AppResult.Failure(AppError.Unavailable("Could not finish family recording"))
        }
    }

    override suspend fun cancel(session: RecordingSession): AppResult<Unit> {
        synchronized(lock) {
            active?.takeIf { it.session.id == session.id }?.let {
                runCatching { it.recorder.stop() }
                it.recorder.release()
                active = null
            }
        }
        return fileStore.delete(session.temporaryFileId)
    }

    override suspend fun save(preview: RecordingPreview): AppResult<PrivateFileId> =
        fileStore.promoteTemporary(preview.session.temporaryFileId, preview.session.pairId.value)

    override suspend fun play(recordingId: PrivateFileId): AppResult<Unit> = when (val result = fileStore.exists(recordingId)) {
        is AppResult.Failure -> result
        is AppResult.Success -> if (result.value) AppResult.Success(Unit) else failure()
    }

    override suspend fun delete(recordingId: PrivateFileId): AppResult<Unit> = fileStore.delete(recordingId)

    private fun <T> failure(): AppResult<T> = AppResult.Failure(AppError.Unavailable("Family recording is unavailable"))

    private data class ActiveRecording(
        val session: RecordingSession,
        val recorder: MediaRecorder,
        val startedAtMs: Long,
    )

    private companion object { const val MAX_DURATION_MS = 5_000L }
}
