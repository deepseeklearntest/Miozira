package com.miozira.services.permissions

enum class MicrophonePermissionState {
    GRANTED,
    DENIED,
    DENIED_DONT_ASK_AGAIN,
    NOT_DETERMINED,
    UNAVAILABLE,
}

enum class AttemptMeasurementDecision {
    OPEN_WINDOW,
    NOT_MEASURED,
    MICROPHONE_UNAVAILABLE,
}

interface PermissionService {
    fun getMicrophonePermissionState(): MicrophonePermissionState

    /** Called only from an adult-facing flow after its explanation screen. */
    fun requestMicrophonePermission(onResult: (MicrophonePermissionState) -> Unit)

    fun openMicrophoneSettings()
}

fun decideAttemptMeasurement(
    detectionEnabled: Boolean,
    permissionState: MicrophonePermissionState,
): AttemptMeasurementDecision = when {
    !detectionEnabled -> AttemptMeasurementDecision.NOT_MEASURED
    permissionState == MicrophonePermissionState.GRANTED -> AttemptMeasurementDecision.OPEN_WINDOW
    else -> AttemptMeasurementDecision.MICROPHONE_UNAVAILABLE
}
