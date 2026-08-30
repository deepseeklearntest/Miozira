package com.miozira.data.repository

import com.miozira.core.result.AppError
import com.miozira.core.result.AppResult
import com.miozira.core.time.Clock
import com.miozira.data.db.dao.RecoveryDao

data class RecoverySummary(
    val closedSessionCount: Int,
    val closedInteractionCount: Int,
)

class RoomRecoveryService(
    private val recoveryDao: RecoveryDao,
    private val clock: Clock,
) {
    suspend fun recoverInterruptedSessions(): AppResult<RecoverySummary> = try {
        val recovery = recoveryDao.closeAllInterruptedSessions(clock.nowMs())
        AppResult.Success(
            RecoverySummary(
                closedSessionCount = recovery.closedSessionCount,
                closedInteractionCount = recovery.closedInteractionCount,
            ),
        )
    } catch (error: Exception) {
        AppResult.Failure(AppError.Unexpected("Could not recover interrupted local sessions"))
    }
}
