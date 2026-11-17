package com.miozira.domain.session

import com.miozira.content.PrototypeContentRepository
import com.miozira.core.model.AttemptState
import com.miozira.core.model.InteractionCompletionReason
import com.miozira.core.model.InteractionId
import com.miozira.core.model.SessionId
import com.miozira.core.result.AppResult
import com.miozira.core.time.Clock
import com.miozira.services.audio.AudioError
import com.miozira.services.audio.AudioPlaybackListener
import com.miozira.services.audio.AudioService
import com.miozira.services.audio.PlaybackHandle
import com.miozira.services.audio.WordPlaybackRequest
import com.miozira.services.microphone.SpeechAttemptDetector
import com.miozira.services.microphone.SpeechAttemptHandle
import com.miozira.services.microphone.SpeechAttemptListener
import com.miozira.services.microphone.SpeechAttemptRequest
import com.miozira.services.permissions.AttemptMeasurementDecision
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ChildSessionControllerTest {
    @Test
    fun startAndTap_usesFixturePair_andAudioIdentityIsStable() = runBlocking {
        val clock = object : Clock { override fun nowMs() = 100L }
        val persistence = RecordingPersistence()
        val audio = RecordingAudioService()
        val controller = ChildSessionController(
            engine = LearningSessionEngine(
                pairs = PrototypeContentRepository.allPairs(),
                persistence = persistence,
                clock = clock,
                idGenerator = { prefix -> "$prefix-1" },
            ),
            persistence = persistence,
            audioService = audio,
            clock = clock,
            scope = CoroutineScope(Dispatchers.Unconfined),
        )

        assertTrue(controller.start() is AppResult.Success)
        assertEquals("apple:en", controller.state.value?.pair?.pairId?.value)
        controller.tap()

        assertEquals(1, audio.requests.size)
        assertEquals(SessionId("session-1"), audio.requests.single().sessionId)
        assertEquals(InteractionId("interaction-1"), audio.requests.single().interactionId)
        assertEquals(1, persistence.interactions.size)
    }

    @Test
    fun staleAudioCallbacks_doNotCommitExposureForCurrentInteraction() = runBlocking {
        val clock = Clock { 100L }
        val persistence = RecordingPersistence()
        val audio = RecordingAudioService()
        val controller = ChildSessionController(
            LearningSessionEngine(PrototypeContentRepository.allPairs(), persistence, clock, idGenerator = { "$it-1" }),
            persistence,
            audio,
            clock,
            CoroutineScope(Dispatchers.Unconfined),
        )
        controller.start()
        controller.tap()

        audio.emitThreshold(PlaybackHandle("stale", SessionId("old"), InteractionId("old")))
        assertEquals(0, persistence.exposures)

        audio.emitThreshold(audio.activeHandle!!)
        assertEquals(1, persistence.exposures)
    }

    @Test
    fun background_stopsPlayback_withoutFabricatingEvidence_andForegroundRestoresReady() = runBlocking {
        val clock = Clock { 100L }
        val persistence = RecordingPersistence()
        val audio = RecordingAudioService()
        val detector = CountingAttemptDetector()
        val controller = ChildSessionController(
            LearningSessionEngine(PrototypeContentRepository.allPairs(), persistence, clock, idGenerator = { "$it-1" }),
            persistence,
            audio,
            clock,
            CoroutineScope(Dispatchers.Unconfined),
            speechAttemptDetector = detector,
        )
        controller.start()
        controller.tap()
        controller.onBackground()

        audio.emitThreshold(audio.activeHandle!!)
        assertEquals(0, persistence.exposures)
        assertEquals(1, detector.stopAllCalls)
        assertEquals(InteractionPhase.IDLE, controller.state.value?.phase)

        controller.onForeground()
        assertEquals(InteractionPhase.READY, controller.state.value?.phase)
    }

    @Test
    fun enabledInvitation_recordsOnlySemanticAttemptResult() = runBlocking {
        val clock = Clock { 100L }
        val persistence = RecordingPersistence()
        val audio = RecordingAudioService()
        val detector = ImmediateAttemptDetector()
        val controller = ChildSessionController(
            LearningSessionEngine(
                PrototypeContentRepository.allPairs(), persistence, clock,
                idGenerator = { "$it-1" },
            ),
            persistence,
            audio,
            clock,
            CoroutineScope(Dispatchers.Unconfined),
            speechAttemptDetector = detector,
            speakingInviteEnabled = true,
        )
        controller.start()
        controller.tap()
        audio.emitCompleted(audio.activeHandle!!)
        controller.completeInvitation(AttemptMeasurementDecision.OPEN_WINDOW)

        assertEquals(AttemptState.ATTEMPT_DETECTED, persistence.lastAttempt)
        assertEquals(InteractionPhase.RESPONDING, controller.state.value?.phase)
    }

    private class RecordingPersistence : SessionPersistence {
        val interactions = mutableListOf<InteractionPlan>()
        var exposures = 0
        var lastAttempt: AttemptState? = null
        override suspend fun createSession(snapshot: SessionSnapshot) = AppResult.Success(Unit)
        override suspend fun createInteraction(plan: InteractionPlan): AppResult<Unit> {
            interactions += plan
            return AppResult.Success(Unit)
        }
        override suspend fun commitExposure(interactionId: InteractionId, committedAtMs: Long): AppResult<Unit> {
            exposures++
            return AppResult.Success(Unit)
        }
        override suspend fun recordReplay(interactionId: InteractionId) = AppResult.Success(Unit)
        override suspend fun recordAttempt(interactionId: InteractionId, state: AttemptState, recordedAtMs: Long): AppResult<Unit> {
            lastAttempt = state
            return AppResult.Success(Unit)
        }
        override suspend fun completeInteraction(interactionId: InteractionId, reason: InteractionCompletionReason, completedAtMs: Long) = AppResult.Success(Unit)
        override suspend fun endSession(sessionId: com.miozira.core.model.SessionId, reason: SessionEndReason, endedAtMs: Long) = AppResult.Success(Unit)
    }

    private class RecordingAudioService : AudioService {
        val requests = mutableListOf<WordPlaybackRequest>()
        var listener: AudioPlaybackListener? = null
        var activeHandle: PlaybackHandle? = null

        override suspend fun playCanonicalWord(request: WordPlaybackRequest, listener: AudioPlaybackListener): AppResult<PlaybackHandle> {
            if (activeHandle != null) return AppResult.Failure(com.miozira.core.result.AppError.InvalidState("active"))
            requests += request
            this.listener = listener
            val handle = PlaybackHandle("playback-1", request.sessionId, request.interactionId)
            activeHandle = handle
            listener.onStarted(handle)
            return AppResult.Success(handle)
        }
        override suspend fun stop(handle: PlaybackHandle) = AppResult.Success(Unit)
        override suspend fun stopAll() = AppResult.Success(Unit)
        fun emitThreshold(handle: PlaybackHandle) { listener?.onExposureThresholdReached(handle) }
        fun emitCompleted(handle: PlaybackHandle) { listener?.onCompleted(handle) }
    }

    private class ImmediateAttemptDetector : SpeechAttemptDetector {
        override suspend fun start(request: SpeechAttemptRequest, listener: SpeechAttemptListener): AppResult<SpeechAttemptHandle> {
            val handle = SpeechAttemptHandle("attempt-1", request.sessionId, request.interactionId)
            listener.onAttemptDetected(handle)
            return AppResult.Success(handle)
        }
        override suspend fun stop(handle: SpeechAttemptHandle) = AppResult.Success(Unit)
        override suspend fun stopAll() = AppResult.Success(Unit)
    }

    private class CountingAttemptDetector : SpeechAttemptDetector {
        var stopAllCalls = 0
        override suspend fun start(request: SpeechAttemptRequest, listener: SpeechAttemptListener) =
            AppResult.Success(SpeechAttemptHandle("attempt", request.sessionId, request.interactionId))
        override suspend fun stop(handle: SpeechAttemptHandle) = AppResult.Success(Unit)
        override suspend fun stopAll(): AppResult<Unit> {
            stopAllCalls++
            return AppResult.Success(Unit)
        }
    }
}
