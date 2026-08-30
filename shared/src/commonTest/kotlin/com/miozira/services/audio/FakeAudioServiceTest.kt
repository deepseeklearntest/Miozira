package com.miozira.services.audio

import com.miozira.core.model.InteractionId
import com.miozira.core.model.PairId
import com.miozira.core.model.SessionId
import com.miozira.core.result.AppResult
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class FakeAudioServiceTest {
    @Test
    fun acceptedTap_startsOnce_andRapidTaps_doNotCreateOverlappingPlayback() = runBlocking {
        val service = FakeAudioService()
        val listener = RecordingListener()
        val request = WordPlaybackRequest(
            sessionId = SessionId("session-1"),
            interactionId = InteractionId("interaction-1"),
            pairId = PairId("apple:en"),
            assetRef = AudioAssetRef(
                logicalId = "audio.word.apple.en.spike",
                assetPath = "audio/word_apple_en_spike.m4a",
            ),
        )

        assertIs<AppResult.Success<PlaybackHandle>>(service.playCanonicalWord(request, listener))
        assertIs<AppResult.Failure>(service.playCanonicalWord(request, listener))
        assertIs<AppResult.Failure>(service.playCanonicalWord(request, listener))

        assertEquals(1, listener.startedHandles.size)
        assertEquals(1, service.activeHandleCount)
    }
}
