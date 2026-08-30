package com.miozira.domain.reset

import com.miozira.core.result.AppError
import com.miozira.core.result.AppResult

data class ResetSummary(
    val sessionsDeleted: Int,
    val observationsDeleted: Int,
    val familyRecordingsDeleted: Int,
    val settingsReset: Boolean,
)

interface ResetCoordinator {
    suspend fun clearStructuredData(): AppResult<StructuredDataDeletion>
    suspend fun deleteFamilyRecordings(): AppResult<Int>
    suspend fun resetSettings(): AppResult<Boolean>
}

data class StructuredDataDeletion(
    val sessionsDeleted: Int,
    val observationsDeleted: Int,
)

class ResetService(
    private val coordinator: ResetCoordinator,
) {
    suspend fun resetUserData(): AppResult<ResetSummary> {
        val structured = when (val result = coordinator.clearStructuredData()) {
            is AppResult.Failure -> return result
            is AppResult.Success -> result.value
        }
        val recordings = when (val result = coordinator.deleteFamilyRecordings()) {
            is AppResult.Failure -> return result
            is AppResult.Success -> result.value
        }
        return when (val result = coordinator.resetSettings()) {
            is AppResult.Failure -> AppResult.Failure(AppError.Unexpected("Miozira reset did not finish safely"))
            is AppResult.Success -> AppResult.Success(
                ResetSummary(
                    sessionsDeleted = structured.sessionsDeleted,
                    observationsDeleted = structured.observationsDeleted,
                    familyRecordingsDeleted = recordings,
                    settingsReset = result.value,
                ),
            )
        }
    }
}
