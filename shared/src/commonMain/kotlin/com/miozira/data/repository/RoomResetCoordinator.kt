package com.miozira.data.repository

import com.miozira.core.result.AppError
import com.miozira.core.result.AppResult
import com.miozira.core.time.Clock
import com.miozira.data.db.dao.ResetDao
import com.miozira.domain.reset.ResetCoordinator
import com.miozira.domain.reset.StructuredDataDeletion

fun interface FamilyRecordingCleanup {
    suspend fun deleteAll(): AppResult<Int>
}

class RoomResetCoordinator(
    private val resetDao: ResetDao,
    private val recordingCleanup: FamilyRecordingCleanup,
    private val settings: SettingsRepository,
    private val clock: Clock,
    private val onResetComplete: () -> Unit = {},
) : ResetCoordinator {
    override suspend fun clearStructuredData(): AppResult<StructuredDataDeletion> = try {
        val deleted = resetDao.clearStructuredLearningData(clock.nowMs())
        AppResult.Success(StructuredDataDeletion(deleted.sessionsDeleted, deleted.observationsDeleted))
    } catch (error: Exception) {
        AppResult.Failure(AppError.Unexpected("Could not clear local learning data"))
    }

    override suspend fun deleteFamilyRecordings(): AppResult<Int> = recordingCleanup.deleteAll()

    override suspend fun resetSettings(): AppResult<Boolean> = when (val result = settings.resetForUserData()) {
        is AppResult.Failure -> AppResult.Failure(result.error)
        is AppResult.Success -> {
            onResetComplete()
            AppResult.Success(true)
        }
    }
}
