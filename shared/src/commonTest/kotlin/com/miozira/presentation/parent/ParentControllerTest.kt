package com.miozira.presentation.parent

import com.miozira.content.LanguageId
import com.miozira.content.PrototypeContentRepository
import com.miozira.core.model.PairId
import com.miozira.core.result.AppError
import com.miozira.core.result.AppResult
import com.miozira.domain.transfer.RealWorldObservation
import com.miozira.domain.transfer.RealWorldObservationRepository
import com.miozira.domain.transfer.RealWorldObservationType
import com.miozira.domain.transfer.RealWorldTransferService
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ParentControllerTest {
    @Test
    fun recordUse_savesExactPair_refreshesSummary_andusesNeutralNotice() = runBlocking {
        val repository = RecordingRepository()
        val data = object : ParentDataSource {
            override suspend fun mostRecentSession() = AppResult.Success<SessionSummary?>(
                SessionSummary(listOf(LanguageId("ta")), listOf(PairId("apple:ta")), emptyList(), emptyList()),
            )
            override suspend fun pairEvidence() = AppResult.Success(emptyList<PairEvidence>())
        }
        val controller = ParentController(data, RealWorldTransferService(repository), { 20L }, { "observation-1" })
        controller.recordUse(PairId("apple:ta"))
        assertEquals(PairId("apple:ta"), repository.last?.pairId)
        assertEquals(RealWorldObservationType.USED_OUTSIDE_APP, repository.last?.type)
        assertEquals(ParentUiState.Notice.SAVED, controller.state.value.notice)
        assertEquals(listOf(PairId("apple:ta")), controller.state.value.recentPairs)
        assertEquals("Tried saying: apple (Tamil)", controller.state.value.sessionLines.last())
    }

    private fun controllerWith(
        evidence: AppResult<List<PairEvidence>>,
        session: AppResult<SessionSummary?> = AppResult.Success(null),
    ) = ParentController(
        object : ParentDataSource {
            override suspend fun mostRecentSession() = session
            override suspend fun pairEvidence() = evidence
        },
        RealWorldTransferService(RecordingRepository()), { 20L }, { "o" },
    )

    @Test
    fun refresh_withEvidence_showsOneLinePerLanguage() = runBlocking {
        val controller = controllerWith(AppResult.Success(emptyList()))
        controller.refresh()
        assertEquals(2, controller.state.value.languageLines.size)
        assertEquals(null, controller.state.value.notice)
    }

    @Test
    fun refresh_whenEvidenceReadFails_hasNoLanguageLines_andShowsError() = runBlocking {
        val controller = controllerWith(AppResult.Failure(AppError.Unexpected("x")))
        controller.refresh()
        assertTrue(controller.state.value.languageLines.isEmpty())
        assertEquals(ParentUiState.Notice.READ_ERROR, controller.state.value.notice)
    }

    @Test
    fun refresh_whenSessionReadFails_doesNotShowTheFirstUseSentence() = runBlocking {
        val controller = controllerWith(AppResult.Success(emptyList()), AppResult.Failure(AppError.Unexpected("x")))
        controller.refresh()
        val state = controller.state.value
        assertEquals(listOf("Miozira could not read his sessions just now."), state.sessionLines)
        assertTrue(describeSession(null, PrototypeContentRepository.languageNames).none { it in state.sessionLines })
        assertEquals(ParentUiState.Notice.READ_ERROR, state.notice)
        assertEquals(2, state.languageLines.size)
    }

    private class RecordingRepository : RealWorldObservationRepository {
        var last: RealWorldObservation? = null
        override suspend fun record(observation: RealWorldObservation): AppResult<RealWorldObservation> {
            last = observation
            return AppResult.Success(observation)
        }
    }
}
