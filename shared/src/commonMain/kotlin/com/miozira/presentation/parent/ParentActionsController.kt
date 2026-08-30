package com.miozira.presentation.parent

import com.miozira.core.result.AppResult
import com.miozira.domain.reset.ResetService
import com.miozira.domain.reset.ResetSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ParentActionsState(
    val resetConfirmationRequired: Boolean = false,
    val lastReset: ResetSummary? = null,
    val notice: Notice? = null,
) {
    enum class Notice { RESET_COMPLETE, ERROR }
}

class ParentActionsController(
    private val resetService: ResetService,
) {
    private val _state = MutableStateFlow(ParentActionsState())
    val state: StateFlow<ParentActionsState> = _state.asStateFlow()

    fun requestReset() {
        _state.value = _state.value.copy(resetConfirmationRequired = true, notice = null)
    }

    fun cancelReset() {
        _state.value = _state.value.copy(resetConfirmationRequired = false)
    }

    suspend fun confirmReset() {
        if (!_state.value.resetConfirmationRequired) return
        when (val result = resetService.resetUserData()) {
            is AppResult.Failure -> _state.value = _state.value.copy(
                resetConfirmationRequired = false,
                notice = ParentActionsState.Notice.ERROR,
            )
            is AppResult.Success -> _state.value = ParentActionsState(
                lastReset = result.value,
                notice = ParentActionsState.Notice.RESET_COMPLETE,
            )
        }
    }
}
