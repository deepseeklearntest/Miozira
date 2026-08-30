package com.miozira.domain.transfer

import com.miozira.core.model.PairId
import com.miozira.core.result.AppResult
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class RealWorldTransferServiceTest {
    @Test
    fun meaningfulSession_getsAtMostOneEligiblePairSuggestion() {
        val service = RealWorldTransferService(NoOpRepository())
        val suggestion = RealWorldSuggestion("s1", PairId("apple:en"), "Try this word together at snack time.")
        val result = service.chooseSuggestion(
            SuggestionContext(true, listOf(PairId("apple:en")), listOf(suggestion)),
        )
        assertEquals(suggestion, assertIs<AppResult.Success<RealWorldSuggestion?>>(result).value)
    }

    @Test
    fun insignificantSession_getsNoSuggestion() {
        val service = RealWorldTransferService(NoOpRepository())
        val result = service.chooseSuggestion(SuggestionContext(false, listOf(PairId("apple:en")), emptyList()))
        assertEquals(null, assertIs<AppResult.Success<RealWorldSuggestion?>>(result).value)
    }

    @Test
    fun observationRepository_receivesExactPairAndType() = runBlocking {
        val repository = RecordingRepository()
        val service = RealWorldTransferService(repository)
        val observation = RealWorldObservation("o1", PairId("apple:ta"), RealWorldObservationType.USED_OUTSIDE_APP, 10L)
        assertIs<AppResult.Success<RealWorldObservation>>(service.record(observation))
        assertEquals(observation, repository.last)
    }

    private open class NoOpRepository : RealWorldObservationRepository {
        override suspend fun record(observation: RealWorldObservation): AppResult<RealWorldObservation> = AppResult.Success(observation)
    }

    private class RecordingRepository : NoOpRepository() {
        var last: RealWorldObservation? = null
        override suspend fun record(observation: RealWorldObservation): AppResult<RealWorldObservation> {
            last = observation
            return AppResult.Success(observation)
        }
    }
}
