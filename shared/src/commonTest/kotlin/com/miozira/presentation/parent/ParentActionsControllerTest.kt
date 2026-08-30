package com.miozira.presentation.parent

import com.miozira.core.result.AppResult
import com.miozira.domain.reset.ResetService
import com.miozira.domain.reset.ResetSummary
import com.miozira.domain.reset.ResetCoordinator
import com.miozira.domain.reset.StructuredDataDeletion
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlinx.coroutines.runBlocking

class ParentActionsControllerTest {
    @Test
    fun reset_requiresSecondAdultConfirmation_andReportsSummary() = runBlocking {
        val controller = ParentActionsController(ResetService(FakeCoordinator()))
        controller.confirmReset()
        assertNull(controller.state.value.lastReset)

        controller.requestReset()
        assertEquals(true, controller.state.value.resetConfirmationRequired)
        controller.confirmReset()

        assertEquals(false, controller.state.value.resetConfirmationRequired)
        assertEquals(ResetSummary(1, 2, 3, true), controller.state.value.lastReset)
    }

    private class FakeCoordinator : ResetCoordinator {
        override suspend fun clearStructuredData() = AppResult.Success(StructuredDataDeletion(1, 2))
        override suspend fun deleteFamilyRecordings() = AppResult.Success(3)
        override suspend fun resetSettings() = AppResult.Success(true)
    }
}
