package com.miozira.presentation.parent

import com.miozira.content.PrototypeContentRepository
import com.miozira.core.model.PairId
import com.miozira.core.result.AppResult
import com.miozira.domain.transfer.RealWorldObservation
import com.miozira.domain.transfer.RealWorldObservationType
import com.miozira.domain.transfer.RealWorldTransferService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ParentUiState(
    val sessionLines: List<String> = describeSession(null, PrototypeContentRepository.languageNames),
    val recentPairs: List<PairId> = emptyList(),
    val wordsForToday: List<String> = emptyList(),
    val languageLines: List<String> = emptyList(),
    val notice: Notice? = null,
) {
    /** SAVED and ERROR follow a save; READ_ERROR follows a failed read of local history. */
    enum class Notice { SAVED, ERROR, READ_ERROR }
}

interface ParentDataSource {
    suspend fun mostRecentSession(): AppResult<SessionSummary?>
    suspend fun pairEvidence(): AppResult<List<PairEvidence>>
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
        val names = PrototypeContentRepository.languageNames
        val evidence = dataSource.pairEvidence()
        val languageLines = (evidence as? AppResult.Success)?.value
            ?.let { rows -> languageOrder.map { describeLanguage(it, rows, names) } }
            .orEmpty()
        val readError = ParentUiState.Notice.READ_ERROR
        _state.value = when (val result = dataSource.mostRecentSession()) {
            is AppResult.Failure -> ParentUiState(
                sessionLines = listOf(SESSION_READ_FAILED),
                languageLines = languageLines,
                notice = readError,
            )
            is AppResult.Success -> {
                val summary = result.value
                ParentUiState(
                    sessionLines = describeSession(summary, names),
                    wordsForToday = wordsForToday(summary, names),
                    languageLines = languageLines,
                    recentPairs = summary?.let { it.tried + it.listened + it.heardUnmeasured }.orEmpty(),
                    notice = if (evidence is AppResult.Failure) readError else null,
                )
            }
        }
    }

    companion object {
        const val SESSION_READ_FAILED = "Miozira could not read his sessions just now."
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
