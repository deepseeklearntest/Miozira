package com.miozira.presentation.parent

import com.miozira.core.model.PairId
import com.miozira.core.result.AppResult
import com.miozira.services.permissions.PermissionService
import com.miozira.services.permissions.MicrophonePermissionState
import com.miozira.services.recording.FamilyRecordingMetadata
import com.miozira.services.recording.FamilyRecordingRepository
import com.miozira.services.recording.FamilyRecordingService
import com.miozira.services.recording.RecordingPreview
import com.miozira.services.recording.RecordingSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed interface FamilyRecordingState {
    data object Idle : FamilyRecordingState
    data class Recording(val session: RecordingSession, val replacing: Boolean) : FamilyRecordingState
    data class Previewing(val preview: RecordingPreview, val replacing: Boolean) : FamilyRecordingState
    data class Available(val metadata: FamilyRecordingMetadata) : FamilyRecordingState
    data class Error(val message: String) : FamilyRecordingState
}

class FamilyRecordingController(
    private val permissionService: PermissionService,
    private val recordingService: FamilyRecordingService,
    private val repository: FamilyRecordingRepository,
    private val nowMs: () -> Long,
    private val idGenerator: () -> String,
) {
    private val _state = MutableStateFlow<FamilyRecordingState>(FamilyRecordingState.Idle)
    val state: StateFlow<FamilyRecordingState> = _state.asStateFlow()

    suspend fun load(pairId: PairId) {
        _state.value = when (val result = repository.get(pairId)) {
            is AppResult.Failure -> FamilyRecordingState.Error(result.error.message)
            is AppResult.Success -> result.value?.let { FamilyRecordingState.Available(it) }
                ?: FamilyRecordingState.Idle
        }
    }

    fun requestPermission() {
        permissionService.requestMicrophonePermission { result ->
            if (result != MicrophonePermissionState.GRANTED) {
                _state.value = FamilyRecordingState.Error("Microphone permission was not granted.")
            }
        }
    }

    suspend fun start(pairId: PairId, replacing: Boolean = false) {
        if (permissionService.getMicrophonePermissionState() != MicrophonePermissionState.GRANTED) {
            _state.value = FamilyRecordingState.Error("Microphone permission is needed for a family recording.")
            return
        }
        _state.value = when (val result = recordingService.start(pairId)) {
            is AppResult.Failure -> FamilyRecordingState.Error(result.error.message)
            is AppResult.Success -> FamilyRecordingState.Recording(result.value, replacing)
        }
    }

    suspend fun stop() {
        val current = _state.value as? FamilyRecordingState.Recording ?: return
        _state.value = when (val result = recordingService.stop(current.session)) {
            is AppResult.Failure -> FamilyRecordingState.Error(result.error.message)
            is AppResult.Success -> FamilyRecordingState.Previewing(result.value, current.replacing)
        }
    }

    suspend fun cancel() {
        val current = _state.value
        if (current is FamilyRecordingState.Recording) recordingService.cancel(current.session)
        if (current is FamilyRecordingState.Previewing) recordingService.cancel(current.preview.session)
        _state.value = FamilyRecordingState.Idle
    }

    suspend fun save() {
        val current = _state.value as? FamilyRecordingState.Previewing ?: return
        val old = when (val result = repository.get(current.preview.session.pairId)) {
            is AppResult.Failure -> {
                _state.value = FamilyRecordingState.Error(result.error.message)
                return
            }
            is AppResult.Success -> result.value
        }
        val newFile = when (val result = recordingService.save(current.preview)) {
            is AppResult.Failure -> {
                _state.value = FamilyRecordingState.Error(result.error.message)
                return
            }
            is AppResult.Success -> result.value
        }
        val metadata = FamilyRecordingMetadata(
            recordingId = idGenerator(),
            pairId = current.preview.session.pairId,
            fileId = newFile,
            createdAtMs = old?.createdAtMs ?: nowMs(),
            updatedAtMs = nowMs(),
            durationMs = current.preview.durationMs,
        )
        when (val result = repository.save(metadata)) {
            is AppResult.Failure -> {
                recordingService.delete(newFile)
                _state.value = FamilyRecordingState.Error(result.error.message)
            }
            is AppResult.Success -> {
                if (old != null && old.fileId != newFile) recordingService.delete(old.fileId)
                _state.value = FamilyRecordingState.Available(metadata)
            }
        }
    }

    suspend fun preview() {
        val current = _state.value
        val fileId = when (current) {
            is FamilyRecordingState.Previewing -> current.preview.session.temporaryFileId
            is FamilyRecordingState.Available -> current.metadata.fileId
            else -> return
        }
        if (recordingService.play(fileId) is AppResult.Failure) {
            _state.value = FamilyRecordingState.Error("This family recording is unavailable; canonical audio remains available.")
        }
    }

    suspend fun play() {
        val current = _state.value as? FamilyRecordingState.Available ?: return
        if (recordingService.play(current.metadata.fileId) is AppResult.Failure) {
            _state.value = FamilyRecordingState.Error("This family recording is unavailable; canonical audio remains available.")
        }
    }

    suspend fun delete() {
        val current = _state.value as? FamilyRecordingState.Available ?: return
        when (val result = repository.delete(current.metadata.pairId)) {
            is AppResult.Failure -> _state.value = FamilyRecordingState.Error(result.error.message)
            is AppResult.Success -> {
                recordingService.delete(current.metadata.fileId)
                _state.value = FamilyRecordingState.Idle
            }
        }
    }

    suspend fun onBackground() {
        if (_state.value is FamilyRecordingState.Recording || _state.value is FamilyRecordingState.Previewing) cancel()
    }
}
