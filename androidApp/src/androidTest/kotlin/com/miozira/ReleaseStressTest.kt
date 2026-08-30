package com.miozira

import android.Manifest
import android.content.pm.PackageManager
import android.os.ParcelFileDescriptor
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.miozira.core.model.InteractionId
import com.miozira.core.model.SessionId
import com.miozira.domain.session.InteractionEvent
import com.miozira.domain.session.InteractionPhase
import com.miozira.domain.session.InteractionState
import com.miozira.domain.session.reduce
import com.miozira.core.model.PairId
import com.miozira.services.microphone.AndroidSpeechAttemptDetector
import com.miozira.services.microphone.MicrophoneError
import com.miozira.services.microphone.SpeechAttemptHandle
import com.miozira.services.microphone.SpeechAttemptListener
import com.miozira.services.microphone.SpeechAttemptRequest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ReleaseStressTest {
    @Test
    fun oneHundredInteractionTransitions_andTwentyFiveRotations_remainBounded() {
        repeat(100) { index ->
            var state = InteractionState(
                phase = InteractionPhase.SESSION_STARTING,
                sessionId = SessionId("stress-session"),
                interactionId = InteractionId("stress-$index"),
                pairId = PairId("apple:en"),
            )
            state = reduce(state, InteractionEvent.StartSession).state
            state = reduce(state, InteractionEvent.PresentationSettled).state
            repeat(25) { state = reduce(state, InteractionEvent.OrientationChanged).state }
            state = reduce(state, InteractionEvent.ConceptTapped).state
            state = reduce(state, InteractionEvent.WordPlaybackCompleted).state
            state = reduce(state, InteractionEvent.ResponseAcknowledged).state
            assertEquals(InteractionPhase.TRANSITIONING, state.phase)
        }
    }

    @Test
    fun oneHundredMicrophoneWindows_emitOneSemanticTerminalAndCreateNoFiles() = runBlocking {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val context = instrumentation.targetContext
        val grant = instrumentation.uiAutomation.executeShellCommand(
            "pm grant ${context.packageName} ${Manifest.permission.RECORD_AUDIO}",
        )
        ParcelFileDescriptor.AutoCloseInputStream(grant).use { it.readBytes() }
        val before = appFiles(context)
        val detector = AndroidSpeechAttemptDetector(context)
        try {
            repeat(100) { index ->
                val terminal = CountDownLatch(1)
                var terminalCount = 0
                val result = detector.start(
                    SpeechAttemptRequest(SessionId("stress-session"), InteractionId("mic-$index"), durationMs = 50),
                    object : SpeechAttemptListener {
                        override fun onAttemptDetected(handle: SpeechAttemptHandle) { terminalCount++; terminal.countDown() }
                        override fun onNoAttemptDetected(handle: SpeechAttemptHandle) { terminalCount++; terminal.countDown() }
                        override fun onMicrophoneUnavailable(error: MicrophoneError) { terminalCount++; terminal.countDown() }
                    },
                )
                assertTrue(result is com.miozira.core.result.AppResult.Success)
                assertTrue(terminal.await(2, TimeUnit.SECONDS), "window $index did not terminate")
                assertEquals(1, terminalCount)
            }
            val after = appFiles(context)
            assertEquals(before, after)
        } finally {
            detector.stopAll()
        }
    }

    private fun appFiles(context: android.content.Context): Set<String> = context.filesDir.walkTopDown()
        .filter { it.isFile }
        .map { it.relativeTo(context.filesDir).path }
        .filterNot { it == "profileInstalled" }
        .toSet()
}
