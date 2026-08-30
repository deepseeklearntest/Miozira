package com.miozira.services.recording

import com.miozira.core.model.PairId
import com.miozira.core.result.AppResult
import com.miozira.services.files.PrivateFileId

data class RecordingSession(
    val id: String,
    val pairId: PairId,
    val temporaryFileId: PrivateFileId,
)

data class RecordingPreview(
    val session: RecordingSession,
    val durationMs: Long,
)

interface FamilyRecordingService {
    suspend fun start(pairId: PairId): AppResult<RecordingSession>
    suspend fun stop(session: RecordingSession): AppResult<RecordingPreview>
    suspend fun cancel(session: RecordingSession): AppResult<Unit>
    suspend fun save(preview: RecordingPreview): AppResult<PrivateFileId>
    suspend fun play(recordingId: PrivateFileId): AppResult<Unit>
    suspend fun delete(recordingId: PrivateFileId): AppResult<Unit>
}
