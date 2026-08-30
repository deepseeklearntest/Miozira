package com.miozira.data.repository

import com.miozira.content.PrototypeContentRepository
import com.miozira.core.result.AppError
import com.miozira.core.result.AppResult
import com.miozira.data.db.dao.ContentDao
import com.miozira.domain.transfer.RealWorldObservationType
import com.miozira.presentation.parent.ParentDataSource
import com.miozira.presentation.parent.ParentPairActivity

class RoomParentDataSource(
    private val contentDao: ContentDao,
) : ParentDataSource {
    override suspend fun recentActivities(): AppResult<List<ParentPairActivity>> = try {
        val pairs = PrototypeContentRepository.allPairs().associateBy { it.pairId.value }
        AppResult.Success(
            contentDao.allPairLearningStates().mapNotNull { state ->
                val pair = pairs[state.pairId] ?: return@mapNotNull null
                val observations = buildSet {
                    if (state.realWorldRecognitionCount > 0) add(RealWorldObservationType.RECOGNIZED_OUTSIDE_APP)
                    if (state.realWorldUseCount > 0) add(RealWorldObservationType.USED_OUTSIDE_APP)
                }
                ParentPairActivity(
                    pairId = pair.pairId,
                    exposureCount = state.validExposureCount,
                    lastExposedAtMs = state.lastExposedAtMs,
                    observations = observations,
                )
            }.filter { it.exposureCount > 0 },
        )
    } catch (error: Exception) {
        AppResult.Failure(AppError.Unexpected("Could not read local parent summary"))
    }
}
