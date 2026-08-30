package com.miozira

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.miozira.core.model.InteractionId
import com.miozira.core.model.PairId
import com.miozira.core.model.SessionId
import com.miozira.core.result.AppResult
import com.miozira.services.audio.AndroidAudioService
import com.miozira.services.audio.AudioAssetRef
import com.miozira.services.audio.AudioError
import com.miozira.services.audio.AudioPlaybackListener
import com.miozira.services.audio.PlaybackHandle
import com.miozira.services.audio.SpikeAudio
import com.miozira.services.audio.WordPlaybackRequest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AndroidAudioServiceTest {
    @Test
    fun bundledClip_emitsStartedExposureAndCompletion() = runBlocking {
        val service = AndroidAudioService(
            InstrumentationRegistry.getInstrumentation().targetContext,
        )
        val events = mutableListOf<String>()
        val completed = CountDownLatch(1)

        try {
            val result = service.playCanonicalWord(
                request = WordPlaybackRequest(
                    sessionId = SessionId("instrumented-session"),
                    interactionId = InteractionId("instrumented-interaction"),
                    pairId = PairId("apple:en"),
                    assetRef = AudioAssetRef("word_apple_en_spike", SpikeAudio.assetPath),
                ),
                listener = object : AudioPlaybackListener {
                    override fun onStarted(handle: PlaybackHandle) {
                        events += "started"
                    }

                    override fun onExposureThresholdReached(handle: PlaybackHandle) {
                        events += "exposure"
                    }

                    override fun onCompleted(handle: PlaybackHandle) {
                        events += "completed"
                        completed.countDown()
                    }

                    override fun onFailed(handle: PlaybackHandle?, error: AudioError) {
                        events += "failed:$error"
                        completed.countDown()
                    }
                },
            )

            assertIs<AppResult.Success<PlaybackHandle>>(result)
            assertTrue(completed.await(3, TimeUnit.SECONDS), "playback did not reach a terminal event")
            assertTrue(events == listOf("started", "exposure", "completed"), "events were $events")
        } finally {
            service.stopAll()
        }
    }
}
