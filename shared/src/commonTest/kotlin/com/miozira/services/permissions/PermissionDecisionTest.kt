package com.miozira.services.permissions

import kotlin.test.Test
import kotlin.test.assertEquals

class PermissionDecisionTest {
    @Test
    fun disabledNeverOpensMicrophone_regardlessOfOsState() {
        MicrophonePermissionState.entries.forEach { state ->
            assertEquals(AttemptMeasurementDecision.NOT_MEASURED, decideAttemptMeasurement(false, state))
        }
    }

    @Test
    fun grantedAndEnabled_opensOneShortMeasurementWindow() {
        assertEquals(
            AttemptMeasurementDecision.OPEN_WINDOW,
            decideAttemptMeasurement(true, MicrophonePermissionState.GRANTED),
        )
    }

    @Test
    fun deniedOrUnavailableNeverPretendsToMeasure() {
        listOf(
            MicrophonePermissionState.DENIED,
            MicrophonePermissionState.DENIED_DONT_ASK_AGAIN,
            MicrophonePermissionState.NOT_DETERMINED,
            MicrophonePermissionState.UNAVAILABLE,
        ).forEach { state ->
            assertEquals(
                AttemptMeasurementDecision.MICROPHONE_UNAVAILABLE,
                decideAttemptMeasurement(true, state),
            )
        }
    }
}
