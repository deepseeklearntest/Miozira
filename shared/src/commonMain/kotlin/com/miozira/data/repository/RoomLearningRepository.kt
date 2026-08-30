package com.miozira.data.repository

import com.miozira.core.model.AttemptState
import com.miozira.core.model.InteractionCompletionReason
import com.miozira.core.model.InteractionId
import com.miozira.core.model.PairId
import com.miozira.core.result.AppError
import com.miozira.core.result.AppResult
import com.miozira.data.db.dao.CommitMutationResult
import com.miozira.data.db.dao.InteractionDao
import com.miozira.data.db.entity.InteractionEntity
import com.miozira.data.db.entity.PairLearningStateEntity

data class InteractionEvidenceSummary(
    val exposureCommitted: Boolean,
    val replayCount: Int,
    val attemptState: AttemptState,
    val completionReason: InteractionCompletionReason?,
)

data class PairEvidenceSummary(
    val validExposureCount: Int,
    val replayCountTotal: Int,
    val attemptDetectedCount: Int,
    val updatedAtMs: Long,
)

class RoomLearningRepository(
    private val interactionDao: InteractionDao,
) {
    suspend fun commitExposure(
        interactionId: InteractionId,
        committedAtMs: Long,
    ): AppResult<Unit> = mutate {
        interactionDao.commitExposureAndRebuild(interactionId.value, committedAtMs)
    }

    suspend fun incrementReplay(interactionId: InteractionId): AppResult<Unit> = mutate {
        interactionDao.incrementReplayAndRebuild(interactionId.value)
    }

    suspend fun recordAttempt(
        interactionId: InteractionId,
        attemptState: AttemptState,
        recordedAtMs: Long,
    ): AppResult<Unit> = mutate {
        interactionDao.recordAttemptAndRebuild(interactionId.value, attemptState.name, recordedAtMs)
    }

    suspend fun completeInteraction(
        interactionId: InteractionId,
        completionReason: InteractionCompletionReason,
        completedAtMs: Long,
    ): AppResult<Unit> = mutate {
        interactionDao.completeInteractionOnce(interactionId.value, completionReason.name, completedAtMs)
    }

    suspend fun rebuildPairState(pairId: PairId, rebuiltAtMs: Long): AppResult<Unit> = try {
        if (interactionDao.findPairState(pairId.value) == null) {
            AppResult.Failure(AppError.InvalidState("Unknown content pair"))
        } else {
            interactionDao.rebuildPairEvidence(pairId.value, rebuiltAtMs)
            AppResult.Success(Unit)
        }
    } catch (error: Exception) {
        AppResult.Failure(AppError.Unexpected("Could not rebuild local pair evidence"))
    }

    suspend fun getInteractionEvidence(
        interactionId: InteractionId,
    ): AppResult<InteractionEvidenceSummary> = try {
        val interaction = interactionDao.findInteraction(interactionId.value)
            ?: return AppResult.Failure(AppError.InvalidState("Unknown interaction"))
        AppResult.Success(interaction.toEvidenceSummary())
    } catch (error: Exception) {
        AppResult.Failure(AppError.Unexpected("Could not read local interaction evidence"))
    }

    suspend fun getPairEvidence(pairId: PairId): AppResult<PairEvidenceSummary> = try {
        val state = interactionDao.findPairState(pairId.value)
            ?: return AppResult.Failure(AppError.InvalidState("Unknown content pair"))
        AppResult.Success(state.toEvidenceSummary())
    } catch (error: Exception) {
        AppResult.Failure(AppError.Unexpected("Could not read local pair evidence"))
    }

    private suspend fun mutate(
        mutation: suspend () -> CommitMutationResult,
    ): AppResult<Unit> = try {
        when (mutation()) {
            CommitMutationResult.CHANGED,
            CommitMutationResult.ALREADY_APPLIED,
            -> AppResult.Success(Unit)
            CommitMutationResult.NOT_FOUND -> AppResult.Failure(AppError.InvalidState("Unknown interaction"))
        }
    } catch (error: Exception) {
        AppResult.Failure(AppError.Unexpected("Could not update local learning evidence"))
    }
}

private fun InteractionEntity.toEvidenceSummary() = InteractionEvidenceSummary(
    exposureCommitted = exposureCommitted,
    replayCount = replayCount,
    attemptState = AttemptState.valueOf(attemptState),
    completionReason = completionReason?.let(InteractionCompletionReason::valueOf),
)

private fun PairLearningStateEntity.toEvidenceSummary() = PairEvidenceSummary(
    validExposureCount = validExposureCount,
    replayCountTotal = replayCountTotal,
    attemptDetectedCount = attemptDetectedCount,
    updatedAtMs = updatedAtMs,
)
