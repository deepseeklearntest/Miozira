package com.miozira.domain.reset

import com.miozira.core.result.AppResult
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ResetServiceTest {
    @Test
    fun successfulReset_returnsDeletionSummary() = runBlocking {
        val service = ResetService(FakeCoordinator())
        val summary = assertIs<AppResult.Success<ResetSummary>>(service.resetUserData()).value
        assertEquals(3, summary.sessionsDeleted)
        assertEquals(2, summary.observationsDeleted)
        assertEquals(1, summary.familyRecordingsDeleted)
        assertEquals(true, summary.settingsReset)
    }

    @Test
    fun structuredFailure_doesNotContinueOrClaimSuccess() = runBlocking {
        val coordinator = FakeCoordinator().apply { failStructured = true }
        val result = ResetService(coordinator).resetUserData()
        assertIs<AppResult.Failure>(result)
        assertEquals(false, coordinator.recordingsDeleted)
    }

    @Test
    fun repeatedReset_remainsSafeAndDoesNotInventData() = runBlocking {
        val coordinator = FakeCoordinator()
        val service = ResetService(coordinator)

        val first = assertIs<AppResult.Success<ResetSummary>>(service.resetUserData()).value
        val second = assertIs<AppResult.Success<ResetSummary>>(service.resetUserData()).value

        assertEquals(first, second)
        assertEquals(2, coordinator.resetCount)
    }

    private class FakeCoordinator : ResetCoordinator {
        var failStructured = false
        var recordingsDeleted = false
        var resetCount = 0
        override suspend fun clearStructuredData(): AppResult<StructuredDataDeletion> =
            if (failStructured) AppResult.Failure(com.miozira.core.result.AppError.Unexpected("db"))
            else AppResult.Success(StructuredDataDeletion(3, 2))
        override suspend fun deleteFamilyRecordings(): AppResult<Int> {
            recordingsDeleted = true
            return AppResult.Success(1)
        }
        override suspend fun resetSettings(): AppResult<Boolean> {
            resetCount++
            return AppResult.Success(true)
        }
    }
}
