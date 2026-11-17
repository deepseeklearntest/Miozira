package com.miozira.data.repository

import com.miozira.content.PrototypeContentRepository
import com.miozira.core.model.AttemptState
import com.miozira.core.model.PairId
import com.miozira.core.result.AppError
import com.miozira.core.result.AppResult
import com.miozira.data.db.dao.InteractionDao
import com.miozira.presentation.parent.ParentDataSource
import com.miozira.presentation.parent.SessionSummary

class RoomParentDataSource(
    private val interactionDao: InteractionDao,
) : ParentDataSource {
    override suspend fun mostRecentSession(): AppResult<SessionSummary?> = try {
        val languageOf = PrototypeContentRepository.allPairs().associate { it.pairId.value to it.languageId }
        val rows = interactionDao.heardInMostRecentSession().filter { it.pairId in languageOf }
        if (rows.isEmpty()) {
            AppResult.Success(null)
        } else {
            val byPair = rows.groupBy { it.pairId }
            val tried = byPair.filterValues { r -> r.any { it.attemptState == AttemptState.ATTEMPT_DETECTED.name } }
            val listened = byPair.filterValues { r ->
                r.none { it.attemptState == AttemptState.ATTEMPT_DETECTED.name } &&
                    r.any { it.attemptState == AttemptState.NO_ATTEMPT_DETECTED.name }
            }
            val unmeasured = byPair.keys - tried.keys - listened.keys
            AppResult.Success(
                SessionSummary(
                    languages = byPair.keys.map { languageOf.getValue(it) }.distinct(),
                    tried = tried.keys.map(::PairId),
                    listened = listened.keys.map(::PairId),
                    heardUnmeasured = unmeasured.map(::PairId),
                ),
            )
        }
    } catch (error: Exception) {
        AppResult.Failure(AppError.Unexpected("Could not read local parent summary"))
    }
}
