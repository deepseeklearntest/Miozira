package com.miozira.presentation.parent

import com.miozira.core.result.AppResult
import com.miozira.domain.reset.ResetService
import com.miozira.domain.reset.ResetSummary
import com.miozira.domain.reset.ResetCoordinator
import com.miozira.domain.reset.StructuredDataDeletion
import com.miozira.data.repository.SettingsRepository
import com.miozira.content.LanguageId
import com.miozira.services.permissions.MicrophonePermissionState
import com.miozira.services.permissions.MicrophonePermissionState.*
import com.miozira.services.permissions.PermissionService
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlinx.coroutines.runBlocking

class ParentActionsControllerTest {
    @Test
    fun reset_requiresSecondAdultConfirmation_andReportsSummary() = runBlocking {
        val controller = controller(ResetService(FakeCoordinator()))
        controller.confirmReset()
        assertNull(controller.state.value.lastReset)

        controller.requestReset()
        assertEquals(true, controller.state.value.resetConfirmationRequired)
        controller.confirmReset()

        assertEquals(false, controller.state.value.resetConfirmationRequired)
        assertEquals(ResetSummary(1, 2, 3, true), controller.state.value.lastReset)
    }

    private fun controller(
        reset: ResetService = ResetService(FakeCoordinator()),
        settings: FakeSettings = FakeSettings(),
        permissions: FakePermissions = FakePermissions(NOT_DETERMINED),
    ) = ParentActionsController(reset, settings, permissions)

    @Test
    fun turningListeningOn_withPermissionGranted_savesOn() = runBlocking {
        val settings = FakeSettings()
        val permissions = FakePermissions(GRANTED)
        val c = controller(settings = settings, permissions = permissions)
        c.setListening(true)
        assertEquals(true, settings.listening)
        assertEquals(true, c.state.value.listeningOn)
        assertEquals(0, permissions.requests)
    }

    @Test
    fun turningListeningOn_whenNotDetermined_asksOnce_andSavesOnlyIfGranted() = runBlocking {
        val settings = FakeSettings()
        val permissions = FakePermissions(NOT_DETERMINED, result = GRANTED)
        val c = controller(settings = settings, permissions = permissions)
        c.setListening(true)
        assertEquals(1, permissions.requests)
        assertEquals(true, settings.listening)
        assertEquals(false, c.state.value.microphoneBlocked)
    }

    @Test
    fun turningListeningOn_whenDenied_staysOff_andReportsBlocked() = runBlocking {
        for (denied in listOf(DENIED, DENIED_DONT_ASK_AGAIN)) {
            val settings = FakeSettings()
            val permissions = FakePermissions(denied)
            val c = controller(settings = settings, permissions = permissions)
            c.setListening(true)
            assertEquals(false, settings.listening)
            assertEquals(false, c.state.value.listeningOn)
            assertEquals(true, c.state.value.microphoneBlocked)
            assertEquals(0, permissions.requests)
        }
        val settings = FakeSettings()
        val c = controller(settings = settings, permissions = FakePermissions(NOT_DETERMINED, result = DENIED))
        c.setListening(true)
        assertEquals(false, settings.listening)
        assertEquals(true, c.state.value.microphoneBlocked)
    }

    @Test
    fun turningListeningOff_neverAsksForPermission() = runBlocking {
        val settings = FakeSettings(listening = true)
        val permissions = FakePermissions(NOT_DETERMINED)
        val c = controller(settings = settings, permissions = permissions)
        c.load()
        assertEquals(true, c.state.value.listeningOn)
        c.setListening(false)
        assertEquals(false, settings.listening)
        assertEquals(false, c.state.value.listeningOn)
        assertEquals(0, permissions.requests)
    }

    private class FakeSettings(var listening: Boolean = false) : SettingsRepository {
        override suspend fun isSetupComplete() = AppResult.Success(true)
        override suspend fun setSetupComplete(value: Boolean) = AppResult.Success(Unit)
        override suspend fun getStartingLanguage() = AppResult.Success<LanguageId?>(null)
        override suspend fun setStartingLanguage(languageId: LanguageId?) = AppResult.Success(Unit)
        override suspend fun isSpeechAttemptDetectionEnabled() = AppResult.Success(listening)
        override suspend fun setSpeechAttemptDetectionEnabled(value: Boolean): AppResult<Unit> {
            listening = value
            return AppResult.Success(Unit)
        }
        override suspend fun resetForUserData() = AppResult.Success(Unit)
    }

    private class FakePermissions(
        private val state: MicrophonePermissionState,
        private val result: MicrophonePermissionState = GRANTED,
    ) : PermissionService {
        var requests = 0
        override fun getMicrophonePermissionState() = state
        override fun requestMicrophonePermission(onResult: (MicrophonePermissionState) -> Unit) {
            requests++
            onResult(result)
        }
        override fun openMicrophoneSettings() {}
    }

    private class FakeCoordinator : ResetCoordinator {
        override suspend fun clearStructuredData() = AppResult.Success(StructuredDataDeletion(1, 2))
        override suspend fun deleteFamilyRecordings() = AppResult.Success(3)
        override suspend fun resetSettings() = AppResult.Success(true)
    }
}
