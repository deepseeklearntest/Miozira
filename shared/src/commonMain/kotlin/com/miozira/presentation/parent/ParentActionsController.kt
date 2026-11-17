package com.miozira.presentation.parent

import com.miozira.core.result.AppResult
import com.miozira.data.repository.SettingsRepository
import com.miozira.domain.reset.ResetService
import com.miozira.domain.reset.ResetSummary
import com.miozira.services.permissions.MicrophonePermissionState
import com.miozira.services.permissions.PermissionService
import kotlin.coroutines.resume
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine

data class ParentActionsState(
    val resetConfirmationRequired: Boolean = false,
    val lastReset: ResetSummary? = null,
    val notice: Notice? = null,
    val listeningOn: Boolean = false,
    val microphoneBlocked: Boolean = false,
) {
    /** ERROR is a failed reset; LISTENING_ERROR is a failed save of the listening switch. */
    enum class Notice { RESET_COMPLETE, ERROR, LISTENING_ERROR }
}

class ParentActionsController(
    private val resetService: ResetService,
    private val settings: SettingsRepository,
    private val permissions: PermissionService,
) {
    private val _state = MutableStateFlow(ParentActionsState())
    val state: StateFlow<ParentActionsState> = _state.asStateFlow()

    suspend fun load() {
        val on = (settings.isSpeechAttemptDetectionEnabled() as? AppResult.Success)?.value ?: false
        _state.value = _state.value.copy(listeningOn = on)
    }

    /** Asks for the microphone only when not yet determined; saves on only when granted. */
    suspend fun setListening(on: Boolean) {
        if (!on) {
            save(false)
            return
        }
        var permission = permissions.getMicrophonePermissionState()
        if (permission == MicrophonePermissionState.NOT_DETERMINED) {
            permission = suspendCancellableCoroutine { cont ->
                permissions.requestMicrophonePermission { cont.resume(it) }
            }
        }
        if (permission == MicrophonePermissionState.GRANTED) {
            save(true)
        } else {
            save(false)
            _state.value = _state.value.copy(microphoneBlocked = true)
        }
    }

    private suspend fun save(on: Boolean) {
        if (settings.setSpeechAttemptDetectionEnabled(on) is AppResult.Success) {
            _state.value = _state.value.copy(
                listeningOn = on,
                microphoneBlocked = if (on) false else _state.value.microphoneBlocked,
            )
        } else {
            _state.value = _state.value.copy(notice = ParentActionsState.Notice.LISTENING_ERROR)
        }
    }

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
