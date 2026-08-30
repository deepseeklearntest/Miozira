package com.miozira.presentation.parent

import com.miozira.core.model.PairId
import com.miozira.core.result.AppResult
import com.miozira.domain.transfer.RealWorldObservation
import com.miozira.domain.transfer.RealWorldObservationRepository
import com.miozira.domain.transfer.RealWorldObservationType
import com.miozira.domain.transfer.RealWorldTransferService
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class ParentControllerTest {
    @Test
    fun recordUse_savesExactPair_refreshesSummary_andusesNeutralNotice() = runBlocking {
        val repository = RecordingRepository()
        val data = object : ParentDataSource {
            override suspend fun recentActivities() = AppResult.Success(
                listOf(ParentPairActivity(PairId("apple:ta"), 2, 10L)),
            )
        }
        val controller = ParentController(data, RealWorldTransferService(repository), { 20L }, { "observation-1" })
        controller.recordUse(PairId("apple:ta"))
        assertEquals(PairId("apple:ta"), repository.last?.pairId)
        assertEquals(RealWorldObservationType.USED_OUTSIDE_APP, repository.last?.type)
        assertEquals(ParentUiState.Notice.SAVED, controller.state.value.notice)
        assertEquals(2, controller.state.value.activities.single().exposureCount)
    }

    private class RecordingRepository : RealWorldObservationRepository {
        var last: RealWorldObservation? = null
        override suspend fun record(observation: RealWorldObservation): AppResult<RealWorldObservation> {
            last = observation
            return AppResult.Success(observation)
        }
    }
}
