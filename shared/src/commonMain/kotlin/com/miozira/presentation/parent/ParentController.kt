package com.miozira.presentation.parent

import com.miozira.core.model.PairId
import com.miozira.core.result.AppResult
import com.miozira.domain.transfer.RealWorldObservation
import com.miozira.domain.transfer.RealWorldObservationType
import com.miozira.domain.transfer.RealWorldTransferService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ParentPairActivity(
    val pairId: PairId,
    val exposureCount: Int,
    val lastExposedAtMs: Long?,
    val observations: Set<RealWorldObservationType> = emptySet(),
)

data class ParentUiState(
    val activities: List<ParentPairActivity> = emptyList(),
    val notice: Notice? = null,
) {
    enum class Notice { SAVED, ERROR }
}

interface ParentDataSource {
    suspend fun recentActivities(): AppResult<List<ParentPairActivity>>
}

class ParentController(
    private val dataSource: ParentDataSource,
    private val transferService: RealWorldTransferService,
    private val nowMs: () -> Long,
    private val idGenerator: () -> String,
) {
    private val _state = MutableStateFlow(ParentUiState())
    val state: StateFlow<ParentUiState> = _state.asStateFlow()

    suspend fun refresh() {
        _state.value = when (val result = dataSource.recentActivities()) {
            is AppResult.Failure -> ParentUiState(notice = ParentUiState.Notice.ERROR)
            is AppResult.Success -> ParentUiState(activities = result.value)
        }
    }

    suspend fun recordRecognition(pairId: PairId) = record(pairId, RealWorldObservationType.RECOGNIZED_OUTSIDE_APP)

    suspend fun recordUse(pairId: PairId) = record(pairId, RealWorldObservationType.USED_OUTSIDE_APP)

    private suspend fun record(pairId: PairId, type: RealWorldObservationType) {
        when (transferService.record(RealWorldObservation(idGenerator(), pairId, type, nowMs()))) {
            is AppResult.Failure -> _state.value = _state.value.copy(notice = ParentUiState.Notice.ERROR)
            is AppResult.Success -> {
                refresh()
                _state.value = _state.value.copy(notice = ParentUiState.Notice.SAVED)
            }
        }
    }
}
