package com.miozira.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.miozira.data.db.entity.InteractionEntity
import com.miozira.data.db.entity.PairLearningStateEntity

@Dao
interface InteractionDao {
    @Query("SELECT * FROM interaction WHERE interaction_id = :interactionId")
    suspend fun findInteraction(interactionId: String): InteractionEntity?

    @Query("SELECT * FROM pair_learning_state WHERE pair_id = :pairId")
    suspend fun findPairState(pairId: String): PairLearningStateEntity?

    @Query(
        "UPDATE interaction SET exposure_committed = 1, exposure_committed_at_ms = :committedAtMs " +
            "WHERE interaction_id = :interactionId AND exposure_committed = 0",
    )
    suspend fun markExposureOnce(interactionId: String, committedAtMs: Long): Int

    @Query(
        "UPDATE interaction SET replay_count = replay_count + 1 WHERE interaction_id = :interactionId",
    )
    suspend fun incrementReplayCount(interactionId: String): Int

    @Query(
        "UPDATE interaction SET attempt_state = :attemptState, attempt_recorded_at_ms = :recordedAtMs " +
            "WHERE interaction_id = :interactionId AND attempt_recorded_at_ms IS NULL",
    )
    suspend fun recordAttemptOnce(interactionId: String, attemptState: String, recordedAtMs: Long): Int

    @Query(
        "UPDATE interaction SET completed_at_ms = :completedAtMs, completion_reason = :completionReason " +
            "WHERE interaction_id = :interactionId AND completed_at_ms IS NULL",
    )
    suspend fun completeOnce(interactionId: String, completionReason: String, completedAtMs: Long): Int

    @Query(
        "UPDATE pair_learning_state SET " +
            "valid_exposure_count = (SELECT COUNT(*) FROM interaction WHERE pair_id = :pairId AND exposure_committed = 1), " +
            "session_exposure_count = (SELECT COUNT(DISTINCT session_id) FROM interaction WHERE pair_id = :pairId AND exposure_committed = 1), " +
            "attempt_detected_count = (SELECT COUNT(*) FROM interaction WHERE pair_id = :pairId AND attempt_state = 'ATTEMPT_DETECTED'), " +
            "replay_count_total = (SELECT COALESCE(SUM(replay_count), 0) FROM interaction WHERE pair_id = :pairId), " +
            "last_exposed_at_ms = (SELECT MAX(exposure_committed_at_ms) FROM interaction WHERE pair_id = :pairId AND exposure_committed = 1), " +
            "last_attempt_at_ms = (SELECT MAX(attempt_recorded_at_ms) FROM interaction WHERE pair_id = :pairId), " +
            "updated_at_ms = :updatedAtMs WHERE pair_id = :pairId",
    )
    suspend fun rebuildPairEvidence(pairId: String, updatedAtMs: Long): Int

    @Transaction
    suspend fun commitExposureAndRebuild(interactionId: String, committedAtMs: Long): CommitMutationResult {
        val interaction = findInteraction(interactionId) ?: return CommitMutationResult.NOT_FOUND
        return if (markExposureOnce(interactionId, committedAtMs) == 1) {
            rebuildPairEvidence(interaction.pairId, committedAtMs)
            CommitMutationResult.CHANGED
        } else {
            CommitMutationResult.ALREADY_APPLIED
        }
    }

    @Transaction
    suspend fun incrementReplayAndRebuild(interactionId: String): CommitMutationResult {
        val interaction = findInteraction(interactionId) ?: return CommitMutationResult.NOT_FOUND
        incrementReplayCount(interactionId)
        val existingState = findPairState(interaction.pairId)
            ?: return CommitMutationResult.NOT_FOUND
        rebuildPairEvidence(interaction.pairId, existingState.updatedAtMs)
        return CommitMutationResult.CHANGED
    }

    @Transaction
    suspend fun recordAttemptAndRebuild(
        interactionId: String,
        attemptState: String,
        recordedAtMs: Long,
    ): CommitMutationResult {
        val interaction = findInteraction(interactionId) ?: return CommitMutationResult.NOT_FOUND
        return if (recordAttemptOnce(interactionId, attemptState, recordedAtMs) == 1) {
            rebuildPairEvidence(interaction.pairId, recordedAtMs)
            CommitMutationResult.CHANGED
        } else {
            CommitMutationResult.ALREADY_APPLIED
        }
    }

    @Transaction
    suspend fun completeInteractionOnce(
        interactionId: String,
        completionReason: String,
        completedAtMs: Long,
    ): CommitMutationResult = when (completeOnce(interactionId, completionReason, completedAtMs)) {
        1 -> CommitMutationResult.CHANGED
        0 -> if (findInteraction(interactionId) == null) CommitMutationResult.NOT_FOUND else CommitMutationResult.ALREADY_APPLIED
        else -> error("Unexpected interaction completion update count")
    }
}

enum class CommitMutationResult {
    CHANGED,
    ALREADY_APPLIED,
    NOT_FOUND,
}
