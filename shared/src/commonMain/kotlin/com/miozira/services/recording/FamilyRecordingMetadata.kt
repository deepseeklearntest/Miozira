package com.miozira.services.recording

import com.miozira.core.model.PairId
import com.miozira.services.files.PrivateFileId

data class FamilyRecordingMetadata(
    val recordingId: String,
    val pairId: PairId,
    val fileId: PrivateFileId,
    val createdAtMs: Long,
    val updatedAtMs: Long,
    val durationMs: Long?,
    val format: String = "audio/mp4a-latm",
    val status: String = "AVAILABLE",
)

interface FamilyRecordingRepository {
    suspend fun get(pairId: PairId): com.miozira.core.result.AppResult<FamilyRecordingMetadata?>
    suspend fun save(metadata: FamilyRecordingMetadata): com.miozira.core.result.AppResult<Unit>
    suspend fun delete(pairId: PairId): com.miozira.core.result.AppResult<Unit>
    suspend fun getAll(): com.miozira.core.result.AppResult<List<FamilyRecordingMetadata>>
}
