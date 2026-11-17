package com.miozira.domain.session

import com.miozira.content.LanguageId
import com.miozira.content.PrototypeContentRepository
import com.miozira.core.model.AttemptState
import com.miozira.core.model.InteractionCompletionReason
import com.miozira.core.model.InteractionId
import com.miozira.core.model.SessionId
import com.miozira.core.result.AppError
import com.miozira.core.result.AppResult
import com.miozira.core.time.Clock
import com.miozira.core.time.LocalDay
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
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

private val en = LanguageId("en")
private val ta = LanguageId("ta")
private const val DAY_MS = 86_400_000L

// Distinct values so each pending pause can be finished by its length.
private val T = SessionTimings(
    invitePauseMs = 1,
    turnMs = 2,
    nodMs = 3,
    afterTurnMs = 4,
    breathMs = 5,
    idleQuietMs = 6,
    maxClipFailuresInARow = 3,
)

class ChildSessionControllerTest {
    @Test
    fun fullSession_walksEveryStep_thenGoesQuiet_andIsRecordedCompletedOnce() = runBlocking<Unit> {
        val h = Harness()
        h.controller.open()
        repeat(6) { i ->
            if (i == 3) {
                assertIs<ChildScreenState.Breath>(h.controller.state.value)
                h.pauses.finish(T.breathMs)
            }
            h.runTurn()
        }
        assertEquals(listOf(en, en, en, ta, ta, ta), h.persistence.interactions.map { it.pair.languageId })
        assertIs<ChildScreenState.Quiet>(h.controller.state.value)
        assertEquals(listOf(SessionEndReason.COMPLETED), h.persistence.ended.map { it.second })
        assertEquals(6, h.persistence.completions.count { it.second == InteractionCompletionReason.NORMAL })
    }

    @Test
    fun writtenWord_isHiddenUntilTheWordPlays() = runBlocking<Unit> {
        val h = Harness()
        h.controller.open()
        assertFalse(h.word().wordVisible)
        h.controller.tap()
        assertTrue(h.word().wordVisible)
    }

    @Test
    fun ring_showsOnlyDuringHisTurn() = runBlocking<Unit> {
        val h = Harness()
        h.controller.open()
        assertFalse(h.word().ringVisible)
        h.controller.tap()
        assertFalse(h.word().ringVisible)
        h.audio.finishWord()
        assertEquals(InteractionPhase.INVITING, h.word().phase)
        assertFalse(h.word().ringVisible)
        h.pauses.finish(T.invitePauseMs)
        assertEquals(InteractionPhase.LISTENING, h.word().phase)
        assertTrue(h.word().ringVisible)
        h.pauses.finish(T.turnMs)
        assertEquals(InteractionPhase.RESPONDING, h.word().phase)
        assertFalse(h.word().ringVisible)
    }

    @Test
    fun silentTurn_movesOn_withNoNod() = runBlocking<Unit> {
        val detector = FakeDetector()
        val h = Harness(measurement = AttemptMeasurementDecision.OPEN_WINDOW, detector = detector)
        h.controller.open()
        h.controller.tap()
        h.audio.finishWord()
        h.pauses.finish(T.invitePauseMs)
        assertEquals(1, detector.starts.size)
        assertEquals(T.turnMs, detector.starts.single().durationMs)
        h.pauses.finish(T.turnMs)
        assertEquals(InteractionPhase.RESPONDING, h.word().phase)
        assertFalse(h.word().nod)
        assertEquals(1, detector.stops)
        assertEquals(listOf(AttemptState.NO_ATTEMPT_DETECTED), h.persistence.attempts)
        val first = h.persistence.interactions.single().interactionId
        h.pauses.finish(T.afterTurnMs)
        assertEquals(2, h.persistence.interactions.size)
        assertEquals(InteractionPhase.READY, h.word().phase)
        assertEquals(listOf(first to InteractionCompletionReason.NORMAL), h.persistence.completions)
    }

    @Test
    fun detectedAttempt_nodsOnce_thenMovesOn() = runBlocking<Unit> {
        val detector = FakeDetector()
        val h = Harness(measurement = AttemptMeasurementDecision.OPEN_WINDOW, detector = detector)
        h.controller.open()
        h.controller.tap()
        h.audio.finishWord()
        h.pauses.finish(T.invitePauseMs)
        detector.emitDetected()
        assertEquals(InteractionPhase.RESPONDING, h.word().phase)
        assertTrue(h.word().nod)
        detector.emitDetected()
        assertEquals(listOf(AttemptState.ATTEMPT_DETECTED), h.persistence.attempts)
        h.pauses.finish(T.nodMs)
        assertFalse(h.word().nod)
        h.pauses.finish(T.afterTurnMs)
        assertEquals(2, h.persistence.interactions.size)
        assertEquals(InteractionPhase.READY, h.word().phase)
        assertFalse(h.word().nod)
    }

    @Test
    fun microphoneOff_ringStillShowsForTheFullTurn_detectorNeverStarts_noNod() = runBlocking<Unit> {
        val detector = FakeDetector()
        val h = Harness(measurement = AttemptMeasurementDecision.NOT_MEASURED, detector = detector)
        h.controller.open()
        h.controller.tap()
        h.audio.finishWord()
        h.pauses.finish(T.invitePauseMs)
        assertTrue(h.word().ringVisible)
        assertTrue(h.pauses.pending.any { it.first == T.turnMs })
        h.pauses.finish(T.turnMs)
        assertFalse(h.word().ringVisible)
        assertFalse(h.word().nod)
        assertEquals(0, detector.starts.size)
        assertEquals(listOf(AttemptState.NOT_MEASURED), h.persistence.attempts)
    }

    @Test
    fun tapDuringHisTurn_replaysTheWord_withoutASecondExposure() = runBlocking<Unit> {
        val h = Harness()
        h.controller.open()
        h.controller.tap()
        h.audio.finishWord()
        h.pauses.finish(T.invitePauseMs)
        h.controller.tap()
        assertEquals(InteractionPhase.PLAYING_WORD, h.word().phase)
        assertFalse(h.pauses.pending.any { it.first == T.turnMs })
        h.audio.finishWord()
        assertEquals(2, h.audio.requests.size)
        assertEquals(1, h.persistence.exposures)
        assertEquals(1, h.persistence.replays)
        assertEquals(InteractionPhase.INVITING, h.word().phase)
    }

    @Test
    fun tapWhileTheWordPlays_isIgnored() = runBlocking<Unit> {
        val h = Harness()
        h.controller.open()
        h.controller.tap()
        h.controller.tap()
        assertEquals(1, h.audio.requests.size)
        assertEquals(0, h.persistence.replays)
        val pair = h.word().pair
        assertEquals("composeResources/miozira.shared.generated.resources/${pair.audio.assetPath}", h.audio.requests.single().assetRef.assetPath)
    }

    @Test
    fun aFailedClip_skipsToTheNextWord() = runBlocking<Unit> {
        val h = Harness()
        h.controller.open()
        h.controller.tap()
        h.audio.fail()
        val first = h.persistence.interactions.first().interactionId
        assertEquals(listOf(first to InteractionCompletionReason.CONTENT_ERROR), h.persistence.completions)
        assertEquals(2, h.persistence.interactions.size)
        assertEquals(InteractionPhase.READY, h.word().phase)
        assertEquals(0, h.persistence.exposures)
        assertTrue(h.persistence.ended.isEmpty())
    }

    @Test
    fun threeAudioFailuresInARow_endSessionWithoutMarkingTodayFinished() = runBlocking<Unit> {
        val h = Harness()
        h.controller.open()
        repeat(3) {
            h.controller.tap()
            h.audio.fail()
        }
        assertEquals(listOf(SessionEndReason.CONTENT_ERROR), h.persistence.ended.map { it.second })
        assertIs<ChildScreenState.Quiet>(h.controller.state.value)
        assertFalse(h.schedule.todayIsFinished())
    }

    @Test
    fun aSuccessfulWord_resetsTheClipFailureCount() = runBlocking<Unit> {
        val h = Harness()
        h.controller.open()
        repeat(2) { h.controller.tap(); h.audio.fail() }
        h.runTurn()
        h.pauses.finish(T.breathMs)
        repeat(2) { h.controller.tap(); h.audio.fail() }
        assertTrue(h.persistence.ended.isEmpty())
        assertIs<ChildScreenState.Word>(h.controller.state.value)
    }

    @Test
    fun idleForAMinute_goesQuiet_andATapReturnsToTheSameWord() = runBlocking<Unit> {
        val h = Harness()
        h.controller.open()
        val before = h.word()
        h.pauses.finish(T.idleQuietMs)
        assertIs<ChildScreenState.Quiet>(h.controller.state.value)
        h.controller.tap()
        assertEquals(before.pair, h.word().pair)
        assertEquals(InteractionPhase.READY, h.word().phase)
        assertEquals(1, h.persistence.interactions.size)
        assertEquals(0, h.audio.requests.size)
        assertTrue(h.persistence.ended.isEmpty())
    }

    @Test
    fun tapAfterIdleOnAnotherDay_endsTheOldSessionAsIdle_andStartsANewOne() = runBlocking<Unit> {
        val h = Harness()
        h.controller.open()
        h.pauses.finish(T.idleQuietMs)
        h.now += DAY_MS
        h.controller.tap()
        assertEquals(listOf(SessionEndReason.IDLE), h.persistence.ended.map { it.second })
        assertEquals(2, h.persistence.sessions.size)
        assertEquals(InteractionPhase.READY, h.word().phase)
        assertEquals(h.persistence.sessions.last(), h.persistence.interactions.last().sessionId)
    }

    @Test
    fun openWhenTodayIsFinished_showsQuiet_andTapsDoNothing() = runBlocking<Unit> {
        val h = Harness()
        h.persistence.ended += SessionId("earlier") to SessionEndReason.COMPLETED
        h.persistence.endedAt += h.now - 10
        h.controller.open()
        assertIs<ChildScreenState.Quiet>(h.controller.state.value)
        h.controller.tap()
        assertIs<ChildScreenState.Quiet>(h.controller.state.value)
        assertEquals(0, h.persistence.sessions.size)
        assertEquals(0, h.audio.requests.size)
    }

    @Test
    fun startExtraSession_startsEvenWhenTodayIsFinished() = runBlocking<Unit> {
        val h = Harness()
        h.persistence.ended += SessionId("earlier") to SessionEndReason.COMPLETED
        h.persistence.endedAt += h.now - 10
        h.controller.open()
        h.controller.startExtraSession()
        assertEquals(InteractionPhase.READY, h.word().phase)
        assertEquals(1, h.persistence.sessions.size)
    }

    @Test
    fun noReadyLanguages_showsQuiet() = runBlocking<Unit> {
        val h = Harness(ready = emptyList())
        h.controller.open()
        assertIs<ChildScreenState.Quiet>(h.controller.state.value)
        assertEquals(0, h.persistence.sessions.size)
    }

    @Test
    fun oneReadyLanguage_runsBothBlocksInIt() = runBlocking<Unit> {
        val h = Harness(ready = listOf(en))
        h.controller.open()
        repeat(6) { i ->
            if (i == 3) h.pauses.finish(T.breathMs)
            h.runTurn()
        }
        assertEquals(List(6) { en }, h.persistence.interactions.map { it.pair.languageId })
        assertEquals(listOf(SessionEndReason.COMPLETED), h.persistence.ended.map { it.second })
    }

    @Test
    fun backgroundDuringTurn_stopsMicAndResumesSameWord() = runBlocking<Unit> {
        val detector = FakeDetector()
        val h = Harness(measurement = AttemptMeasurementDecision.OPEN_WINDOW, detector = detector)
        h.controller.open()
        val pair = h.word().pair
        h.controller.tap()
        h.audio.finishWord()
        h.pauses.finish(T.invitePauseMs)
        assertEquals(1, detector.starts.size)
        h.controller.onBackground()
        assertEquals(1, detector.stopAllCalls)
        assertEquals(1, h.audio.stopAllCalls)
        assertFalse(h.pauses.pending.any { it.first == T.turnMs })
        detector.emitDetected()
        assertTrue(h.persistence.attempts.isEmpty())
        h.controller.open()
        assertEquals(pair, h.word().pair)
        assertEquals(InteractionPhase.READY, h.word().phase)
        assertEquals(1, h.persistence.interactions.size)
        assertTrue(h.persistence.attempts.isEmpty())
    }

    @Test
    fun backgroundAfterTheTurn_resumesAtTheNextWord_notTheCompletedOne() = runBlocking<Unit> {
        val h = Harness()
        h.controller.open()
        h.runTurn()
        h.runTurn()
        h.runTurn()
        assertIs<ChildScreenState.Breath>(h.controller.state.value)
        h.controller.onBackground()
        h.controller.open()
        assertIs<ChildScreenState.Breath>(h.controller.state.value)
        h.pauses.finish(T.breathMs)
        assertEquals(4, h.persistence.interactions.size)
        assertEquals(ta, h.word().pair.languageId)
        assertEquals(3, h.persistence.completions.size)
    }

    @Test
    fun staleAudioCallbacks_doNotCommitExposureForCurrentInteraction() = runBlocking<Unit> {
        val h = Harness()
        h.controller.open()
        h.controller.tap()

        h.audio.emitThreshold(PlaybackHandle("stale", SessionId("old"), InteractionId("old")))
        assertEquals(0, h.persistence.exposures)

        h.audio.emitThreshold(h.audio.activeHandle!!)
        assertEquals(1, h.persistence.exposures)
    }

    @Test
    fun staleCallbacksFromThePreviousWord_areIgnored() = runBlocking<Unit> {
        val detector = FakeDetector()
        val h = Harness(measurement = AttemptMeasurementDecision.OPEN_WINDOW, detector = detector)
        h.controller.open()
        h.controller.tap()
        val oldPlayback = h.audio.activeHandle!!
        h.audio.finishWord()
        h.pauses.finish(T.invitePauseMs)
        val oldListen = detector.lastHandle!!
        h.pauses.finish(T.turnMs)
        h.pauses.finish(T.afterTurnMs)
        assertNotEquals(oldPlayback.interactionId, h.persistence.interactions.last().interactionId)
        h.controller.tap()
        h.audio.emitThreshold(oldPlayback)
        h.audio.emitCompletedFor(oldPlayback)
        detector.listener!!.onAttemptDetected(oldListen)
        assertEquals(1, h.persistence.exposures)
        assertEquals(InteractionPhase.PLAYING_WORD, h.word().phase)
        assertEquals(listOf(AttemptState.NO_ATTEMPT_DETECTED), h.persistence.attempts)
    }

    private class Harness(
        ready: List<LanguageId> = listOf(en, ta),
        measurement: AttemptMeasurementDecision = AttemptMeasurementDecision.NOT_MEASURED,
        detector: SpeechAttemptDetector? = null,
    ) {
        var now = 10 * DAY_MS + 100
        val clock = Clock { now }
        val pauses = Pauses()
        val persistence = RecordingPersistence()
        val audio = RecordingAudioService()
        val schedule = SessionSchedule(persistence, LocalDay { it / DAY_MS }, clock)
        private var nextId = 0
        val controller = ChildSessionController(
            engine = LearningSessionEngine(
                PrototypeContentRepository.allPairs(),
                persistence,
                clock,
                idGenerator = { prefix -> "$prefix-${++nextId}" },
            ),
            persistence = persistence,
            schedule = schedule,
            audioService = audio,
            clock = clock,
            scope = CoroutineScope(Dispatchers.Unconfined),
            readyLanguages = { ready },
            measurement = { measurement },
            speechAttemptDetector = detector,
            shape = SessionShape(1, 2, 1),
            timings = T,
            pause = pauses.pause,
        )

        fun word(): ChildScreenState.Word = assertIs(controller.state.value)

        fun runTurn() {
            assertEquals(InteractionPhase.READY, word().phase)
            controller.tap()
            audio.finishWord()
            pauses.finish(T.invitePauseMs)
            pauses.finish(T.turnMs)
            pauses.finish(T.afterTurnMs)
        }
    }

    // Cancelled timers remove their entry, so finish(ms) never completes a pause nobody awaits.
    private class Pauses {
        val pending = ArrayDeque<Pair<Long, CompletableDeferred<Unit>>>()
        val pause: suspend (Long) -> Unit = { ms ->
            val entry = ms to CompletableDeferred<Unit>()
            pending += entry
            try { entry.second.await() } finally { pending.remove(entry) }
        }
        fun finish(ms: Long) = pending.first { it.first == ms && !it.second.isCompleted }.second.complete(Unit)
    }

    private class RecordingPersistence : SessionPersistence, SessionHistory {
        val sessions = mutableListOf<SessionId>()
        val interactions = mutableListOf<InteractionPlan>()
        val completions = mutableListOf<Pair<InteractionId, InteractionCompletionReason>>()
        val attempts = mutableListOf<AttemptState>()
        val ended = mutableListOf<Pair<SessionId, SessionEndReason>>()
        val endedAt = mutableListOf<Long>()
        var exposures = 0
        var replays = 0
        override suspend fun createSession(snapshot: SessionSnapshot): AppResult<Unit> {
            sessions += snapshot.sessionId
            return AppResult.Success(Unit)
        }
        override suspend fun createInteraction(plan: InteractionPlan): AppResult<Unit> {
            interactions += plan
            return AppResult.Success(Unit)
        }
        override suspend fun commitExposure(interactionId: InteractionId, committedAtMs: Long): AppResult<Unit> {
            exposures++
            return AppResult.Success(Unit)
        }
        override suspend fun recordReplay(interactionId: InteractionId): AppResult<Unit> {
            replays++
            return AppResult.Success(Unit)
        }
        override suspend fun recordAttempt(interactionId: InteractionId, state: AttemptState, recordedAtMs: Long): AppResult<Unit> {
            attempts += state
            return AppResult.Success(Unit)
        }
        override suspend fun completeInteraction(interactionId: InteractionId, reason: InteractionCompletionReason, completedAtMs: Long): AppResult<Unit> {
            completions += interactionId to reason
            return AppResult.Success(Unit)
        }
        override suspend fun endSession(sessionId: SessionId, reason: SessionEndReason, endedAtMs: Long): AppResult<Unit> {
            ended += sessionId to reason
            endedAt += endedAtMs
            return AppResult.Success(Unit)
        }
        override suspend fun completedSessionCount(): AppResult<Int> =
            AppResult.Success(ended.count { it.second == SessionEndReason.COMPLETED })
        override suspend fun lastCompletedAtMs(): AppResult<Long?> =
            AppResult.Success(ended.indices.filter { ended[it].second == SessionEndReason.COMPLETED }.maxOfOrNull { endedAt[it] })
    }

    private class RecordingAudioService : AudioService {
        val requests = mutableListOf<WordPlaybackRequest>()
        var listener: AudioPlaybackListener? = null
        var activeHandle: PlaybackHandle? = null
        var stopAllCalls = 0

        override suspend fun playCanonicalWord(request: WordPlaybackRequest, listener: AudioPlaybackListener): AppResult<PlaybackHandle> {
            if (activeHandle != null) return AppResult.Failure(AppError.InvalidState("active"))
            requests += request
            this.listener = listener
            val handle = PlaybackHandle("playback-${requests.size}", request.sessionId, request.interactionId)
            activeHandle = handle
            listener.onStarted(handle)
            return AppResult.Success(handle)
        }
        override suspend fun stop(handle: PlaybackHandle): AppResult<Unit> {
            if (activeHandle == handle) activeHandle = null
            return AppResult.Success(Unit)
        }
        override suspend fun stopAll(): AppResult<Unit> {
            stopAllCalls++
            activeHandle = null
            return AppResult.Success(Unit)
        }
        fun emitThreshold(handle: PlaybackHandle) { listener?.onExposureThresholdReached(handle) }
        fun emitCompletedFor(handle: PlaybackHandle) { listener?.onCompleted(handle) }
        fun finishWord() {
            val handle = activeHandle!!
            activeHandle = null
            listener!!.onExposureThresholdReached(handle)
            listener!!.onCompleted(handle)
        }
        fun fail() {
            val handle = activeHandle!!
            activeHandle = null
            listener!!.onFailed(handle, AudioError.PlaybackFailed("test"))
        }
    }

    private class FakeDetector : SpeechAttemptDetector {
        val starts = mutableListOf<SpeechAttemptRequest>()
        var listener: SpeechAttemptListener? = null
        var lastHandle: SpeechAttemptHandle? = null
        var stops = 0
        var stopAllCalls = 0
        override suspend fun start(request: SpeechAttemptRequest, listener: SpeechAttemptListener): AppResult<SpeechAttemptHandle> {
            starts += request
            this.listener = listener
            val handle = SpeechAttemptHandle("attempt-${starts.size}", request.sessionId, request.interactionId)
            lastHandle = handle
            return AppResult.Success(handle)
        }
        override suspend fun stop(handle: SpeechAttemptHandle): AppResult<Unit> {
            stops++
            return AppResult.Success(Unit)
        }
        override suspend fun stopAll(): AppResult<Unit> {
            stopAllCalls++
            return AppResult.Success(Unit)
        }
        fun emitDetected() { listener!!.onAttemptDetected(lastHandle!!) }
    }
}
