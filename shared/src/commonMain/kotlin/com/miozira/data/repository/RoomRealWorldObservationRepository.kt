package com.miozira.data.repository

import com.miozira.core.result.AppError
import com.miozira.core.result.AppResult
import com.miozira.data.db.dao.RealWorldObservationDao
import com.miozira.data.db.entity.RealWorldObservationEntity
import com.miozira.domain.transfer.RealWorldObservation
import com.miozira.domain.transfer.RealWorldObservationRepository

class RoomRealWorldObservationRepository(
    private val dao: RealWorldObservationDao,
) : RealWorldObservationRepository {
    override suspend fun record(observation: RealWorldObservation): AppResult<RealWorldObservation> = try {
        val inserted = dao.insertIgnore(
            RealWorldObservationEntity(
                observationId = observation.observationId,
                pairId = observation.pairId.value,
                observationType = observation.type.name,
                recordedAtMs = observation.recordedAtMs,
                observedAtMs = null,
                context = null,
            ),
        )
        if (inserted != -1L) dao.updatePairEvidence(observation.pairId.value, observation.type.name, observation.recordedAtMs)
        val saved = dao.find(observation.pairId.value, observation.type.name)
            ?: return AppResult.Failure(AppError.Unexpected("Could not read saved observation"))
        AppResult.Success(
            observation.copy(
                observationId = saved.observationId,
                recordedAtMs = saved.recordedAtMs,
            ),
        )
    } catch (error: Exception) {
        AppResult.Failure(AppError.Unexpected("Could not save local observation"))
    }
}
