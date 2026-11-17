package com.miozira.domain.transfer

import com.miozira.core.model.PairId
import com.miozira.core.result.AppResult

enum class RealWorldObservationType { RECOGNIZED_OUTSIDE_APP, USED_OUTSIDE_APP }

data class RealWorldObservation(
    val observationId: String,
    val pairId: PairId,
    val type: RealWorldObservationType,
    val recordedAtMs: Long,
)

interface RealWorldObservationRepository {
    suspend fun record(observation: RealWorldObservation): AppResult<RealWorldObservation>
}

class RealWorldTransferService(
    private val repository: RealWorldObservationRepository,
) {
    suspend fun record(observation: RealWorldObservation): AppResult<RealWorldObservation> =
        repository.record(observation)
}
