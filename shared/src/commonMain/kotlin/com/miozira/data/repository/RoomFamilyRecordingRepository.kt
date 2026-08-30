package com.miozira.data.repository

import com.miozira.core.model.PairId
import com.miozira.core.result.AppError
import com.miozira.core.result.AppResult
import com.miozira.data.db.dao.FamilyRecordingDao
import com.miozira.data.db.entity.FamilyRecordingEntity
import com.miozira.services.files.PrivateFileId
import com.miozira.services.recording.FamilyRecordingMetadata
import com.miozira.services.recording.FamilyRecordingRepository

class RoomFamilyRecordingRepository(
    private val dao: FamilyRecordingDao,
) : FamilyRecordingRepository {
    override suspend fun get(pairId: PairId): AppResult<FamilyRecordingMetadata?> = try {
        AppResult.Success(dao.find(pairId.value)?.toMetadata())
    } catch (error: Exception) {
        AppResult.Failure(AppError.Unexpected("Could not read family recording"))
    }

    override suspend fun save(metadata: FamilyRecordingMetadata): AppResult<Unit> = try {
        dao.replace(metadata.toEntity())
        AppResult.Success(Unit)
    } catch (error: Exception) {
        AppResult.Failure(AppError.Unexpected("Could not save family recording metadata"))
    }

    override suspend fun delete(pairId: PairId): AppResult<Unit> = try {
        dao.delete(pairId.value)
        AppResult.Success(Unit)
    } catch (error: Exception) {
        AppResult.Failure(AppError.Unexpected("Could not delete family recording metadata"))
    }

    override suspend fun getAll(): AppResult<List<FamilyRecordingMetadata>> = try {
        AppResult.Success(dao.all().map { it.toMetadata() })
    } catch (error: Exception) {
        AppResult.Failure(AppError.Unexpected("Could not read family recordings"))
    }
}

private fun FamilyRecordingMetadata.toEntity() = FamilyRecordingEntity(
    recordingId = recordingId,
    pairId = pairId.value,
    logicalFileId = fileId.value,
    createdAtMs = createdAtMs,
    updatedAtMs = updatedAtMs,
    durationMs = durationMs,
    format = format,
    status = status,
)

private fun FamilyRecordingEntity.toMetadata() = FamilyRecordingMetadata(
    recordingId = recordingId,
    pairId = PairId(pairId),
    fileId = PrivateFileId(logicalFileId),
    createdAtMs = createdAtMs,
    updatedAtMs = updatedAtMs,
    durationMs = durationMs,
    format = format ?: "audio/mp4a-latm",
    status = status,
)
