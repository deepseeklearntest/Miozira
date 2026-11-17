package com.miozira.data.repository

import com.miozira.content.PrototypeContentRepository
import com.miozira.core.result.AppError
import com.miozira.core.result.AppResult
import com.miozira.data.db.dao.InteractionDao
import com.miozira.presentation.parent.ParentDataSource
import com.miozira.presentation.parent.SessionSummary
import com.miozira.presentation.parent.summarize

class RoomParentDataSource(
    private val interactionDao: InteractionDao,
) : ParentDataSource {
    override suspend fun mostRecentSession(): AppResult<SessionSummary?> = try {
        val languageOf = PrototypeContentRepository.allPairs().associate { it.pairId.value to it.languageId }
        AppResult.Success(
            summarize(interactionDao.heardInMostRecentSession().map { it.pairId to it.attemptState }, languageOf),
        )
    } catch (error: Exception) {
        AppResult.Failure(AppError.Unexpected("Could not read local parent summary"))
    }
}
