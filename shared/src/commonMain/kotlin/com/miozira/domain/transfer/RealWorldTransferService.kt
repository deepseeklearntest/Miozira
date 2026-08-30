package com.miozira.domain.transfer

import com.miozira.core.model.PairId
import com.miozira.core.result.AppError
import com.miozira.core.result.AppResult

enum class RealWorldObservationType { RECOGNIZED_OUTSIDE_APP, USED_OUTSIDE_APP }

data class RealWorldObservation(
    val observationId: String,
    val pairId: PairId,
    val type: RealWorldObservationType,
    val recordedAtMs: Long,
)

data class RealWorldSuggestion(
    val suggestionId: String,
    val pairId: PairId,
    val text: String,
)

data class SuggestionContext(
    val meaningfulActivity: Boolean,
    val recentlyExposedPairs: List<PairId>,
    val suggestions: List<RealWorldSuggestion>,
    val previousSuggestionIds: Set<String> = emptySet(),
)

interface RealWorldObservationRepository {
    suspend fun record(observation: RealWorldObservation): AppResult<RealWorldObservation>
}

class RealWorldTransferService(
    private val repository: RealWorldObservationRepository,
) {
    suspend fun record(observation: RealWorldObservation): AppResult<RealWorldObservation> =
        repository.record(observation)

    fun chooseSuggestion(context: SuggestionContext): AppResult<RealWorldSuggestion?> {
        if (!context.meaningfulActivity) return AppResult.Success(null)
        val pairIds = context.recentlyExposedPairs.toSet()
        val suggestion = context.suggestions.firstOrNull {
            it.pairId in pairIds && it.suggestionId !in context.previousSuggestionIds
        }
            ?: return AppResult.Failure(AppError.Unavailable("No eligible real-world suggestion"))
        return AppResult.Success(suggestion)
    }
}
