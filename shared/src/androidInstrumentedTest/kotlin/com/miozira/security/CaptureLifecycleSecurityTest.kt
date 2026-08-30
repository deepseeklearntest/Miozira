package com.miozira.security

import android.Manifest
import android.os.ParcelFileDescriptor
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.miozira.core.model.InteractionId
import com.miozira.core.model.SessionId
import com.miozira.core.result.AppResult
import com.miozira.services.microphone.AndroidSpeechAttemptDetector
import com.miozira.services.microphone.MicrophoneError
import com.miozira.services.microphone.SpeechAttemptHandle
import com.miozira.services.microphone.SpeechAttemptListener
import com.miozira.services.microphone.SpeechAttemptRequest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CaptureLifecycleSecurityTest {
    @Test
    fun stopAll_releasesActiveCapture_withoutSemanticCallbackOrFileArtifact() = runBlocking {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val context = instrumentation.targetContext
        val grant = instrumentation.uiAutomation.executeShellCommand(
            "pm grant ${context.packageName} ${Manifest.permission.RECORD_AUDIO}",
        )
        ParcelFileDescriptor.AutoCloseInputStream(grant).use { it.readBytes() }

        val before = context.filesDir.walkTopDown().filter { it.isFile }.map { it.relativeTo(context.filesDir).path }.toSet()
        val detector = AndroidSpeechAttemptDetector(context)
        var callbackCount = 0
        try {
            val result = detector.start(
                SpeechAttemptRequest(SessionId("lifecycle-session"), InteractionId("lifecycle-interaction"), durationMs = 2_500),
                object : SpeechAttemptListener {
                    override fun onAttemptDetected(handle: SpeechAttemptHandle) { callbackCount++ }
                    override fun onNoAttemptDetected(handle: SpeechAttemptHandle) { callbackCount++ }
                    override fun onMicrophoneUnavailable(error: MicrophoneError) { callbackCount++ }
                },
            )
            assertTrue(result is AppResult.Success)
            assertTrue(detector.stopAll() is AppResult.Success)
            delay(300)
            assertEquals(0, callbackCount)
            val after = context.filesDir.walkTopDown().filter { it.isFile }.map { it.relativeTo(context.filesDir).path }.toSet()
            assertEquals(before, after)
        } finally {
            detector.stopAll()
        }
    }
}
