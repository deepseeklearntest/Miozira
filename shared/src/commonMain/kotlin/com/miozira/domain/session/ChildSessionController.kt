package com.miozira.domain.session

import com.miozira.content.ConceptLanguagePair
import com.miozira.content.LanguageId
import com.miozira.core.model.AttemptState
import com.miozira.core.model.InteractionCompletionReason
import com.miozira.core.model.InteractionId
import com.miozira.core.result.AppResult
import com.miozira.core.time.Clock
import com.miozira.services.audio.AudioAssetRef
import com.miozira.services.audio.BUNDLED_RESOURCES_ROOT
import com.miozira.services.audio.AudioError
import com.miozira.services.audio.AudioPlaybackListener
import com.miozira.services.audio.AudioService
import com.miozira.services.audio.PlaybackHandle
import com.miozira.services.audio.WordPlaybackRequest
import com.miozira.services.microphone.MicrophoneError
import com.miozira.services.microphone.SpeechAttemptDetector
import com.miozira.services.microphone.SpeechAttemptHandle
import com.miozira.services.microphone.SpeechAttemptListener
import com.miozira.services.microphone.SpeechAttemptRequest
import com.miozira.services.permissions.AttemptMeasurementDecision
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Runs one session from warm-up to quiet. Every word's phase change goes through [reduce];
 * this class only reacts to the reducer's effects and to its own timers.
 */
class ChildSessionController(
    private val engine: LearningSessionEngine,
    private val persistence: SessionPersistence,
    private val schedule: SessionSchedule,
    private val audioService: AudioService,
    private val clock: Clock,
    private val scope: CoroutineScope,
    private val readyLanguages: () -> List<LanguageId>,
    private val measurement: suspend () -> AttemptMeasurementDecision,
    private val speechAttemptDetector: SpeechAttemptDetector? = null,
    private val shape: SessionShape = SessionShape(),
    private val timings: SessionTimings = SessionTimings(),
    private val pause: suspend (Long) -> Unit = { kotlinx.coroutines.delay(it) },
) {
    private val _state = MutableStateFlow<ChildScreenState>(ChildScreenState.Loading)
    val state: StateFlow<ChildScreenState> = _state.asStateFlow()

    private val lock = Mutex()
    private var sessionActive = false
    private var paused = false
    private var sessionStartedAtMs = 0L
    private var steps: List<SessionStep> = emptyList()
    private var stepIndex = 0

    // Null between words (after a word completes, during Breath); otherwise the current word.
    private var interaction: InteractionState? = null
    private var pair: ConceptLanguagePair? = null
    private var wordVisible = false
    private var nod = false
    private var clipFailuresInARow = 0

    // Microphone-off result, recorded when the turn ends so the ring stays up for the full turn.
    private var heldResult: AttemptState? = null
    private var listening: SpeechAttemptHandle? = null

    // One running timer at a time (idle, invite, turn, after-turn, breath, next step), plus the nod.
    private var timer: Job? = null
    private var nodTimer: Job? = null

    /** App start and every return to the foreground. */
    suspend fun open() = lock.withLock { openLocked() }

    /** Parent area: starts a session even when today's is finished. */
    suspend fun startExtraSession() = lock.withLock {
        if (sessionActive) endSession(SessionEndReason.IDLE)
        startSession()
    }

    fun tap() {
        when (_state.value) {
            ChildScreenState.Quiet -> if (sessionActive && paused) scope.launch { open() }
            is ChildScreenState.Word -> {
                val current = interaction ?: return
                when (current.phase) {
                    InteractionPhase.READY -> dispatch(InteractionEvent.ConceptTapped)
                    InteractionPhase.INVITING, InteractionPhase.LISTENING, InteractionPhase.RESPONDING ->
                        dispatch(InteractionEvent.ReplayRequested(current.sessionId, current.interactionId))
                    else -> Unit
                }
            }
            else -> Unit
        }
    }

    fun onBackground() {
        if (!sessionActive) return
        cancelTimers()
        paused = true
        dispatch(InteractionEvent.AppBackgrounded)
        listening = null
        scope.launch {
            audioService.stopAll()
            speechAttemptDetector?.stopAll()
        }
    }

    private suspend fun openLocked() {
        if (sessionActive) {
            if (!paused) return
            if (schedule.isSameDay(sessionStartedAtMs, clock.nowMs())) return resume()
            endSession(SessionEndReason.IDLE)
        }
        if (schedule.todayIsFinished()) {
            _state.value = ChildScreenState.Quiet
            return
        }
        startSession()
    }

    private suspend fun startSession() {
        val languages = schedule.languagesForNextSession(readyLanguages())
        val snapshot = languages?.let { (engine.startSession(it.first) as? AppResult.Success)?.value }
        if (languages == null || snapshot == null) {
            _state.value = ChildScreenState.Quiet
            return
        }
        steps = sessionSteps(shape, languages.first, languages.second)
        stepIndex = 0
        sessionStartedAtMs = snapshot.startedAtMs
        sessionActive = true
        paused = false
        clipFailuresInARow = 0
        runStep()
    }

    private suspend fun resume() {
        paused = false
        val current = interaction ?: return runStep()
        if (current.phase == InteractionPhase.IDLE) dispatch(InteractionEvent.AppForegrounded) else enterReady()
    }

    private suspend fun runStep() {
        interaction = null
        if (clipFailuresInARow >= timings.maxClipFailuresInARow) return endSession(SessionEndReason.CONTENT_ERROR)
        when (val step = steps.getOrNull(stepIndex)) {
            null -> endSession(SessionEndReason.COMPLETED)
            SessionStep.Breath -> {
                _state.value = ChildScreenState.Breath
                after(timings.breathMs) { stepIndex++; runStep() }
            }
            is SessionStep.Word -> {
                // The plan records that no spoken invite was given (product step 4 adds it); the
                // reducer flag below only gates the silent turn (INVITING -> LISTENING).
                val plan = (engine.beginInteraction(step, allowSpeakingInvite = false) as? AppResult.Success)?.value
                    ?: run {
                        // A background cancels this step mid-write; resume() runs it again. Only a
                        // real failure (no eligible pair, storage error) ends the session.
                        currentCoroutineContext().ensureActive()
                        if (paused) return
                        return endSession(SessionEndReason.CONTENT_ERROR)
                    }
                interaction = InteractionState(
                    phase = InteractionPhase.SESSION_STARTING,
                    sessionId = plan.sessionId,
                    interactionId = plan.interactionId,
                    pairId = plan.pair.pairId,
                    allowSpeakingInvite = true,
                )
                pair = plan.pair
                wordVisible = false
                nod = false
                dispatch(InteractionEvent.StartSession)
                dispatch(InteractionEvent.PresentationSettled)
            }
        }
    }

    // Often runs inside the timer job (runStep after the last word), so it must not cancel that
    // job, and the end write runs NonCancellable so a background cannot drop it either.
    private suspend fun endSession(reason: SessionEndReason) {
        val running = currentCoroutineContext()[Job]
        if (timer !== running) timer?.cancel()
        nodTimer?.cancel()
        nod = false
        stopListening()
        interaction = null
        pair = null
        sessionActive = false
        paused = false
        withContext(NonCancellable) {
            engine.endSession(reason)
            _state.value = ChildScreenState.Quiet
        }
    }

    private fun dispatch(event: InteractionEvent) {
        val before = interaction ?: return
        val transition = reduce(before, event)
        val after = transition.state
        interaction = after
        if (after.phase != before.phase) onEnter(after)
        transition.effects.forEach { onEffect(it, after) }
        publish()
    }

    private fun onEnter(state: InteractionState) {
        when (state.phase) {
            InteractionPhase.READY -> enterReady()
            InteractionPhase.PLAYING_WORD -> {
                cancelTimers()
                heldResult = null
            }
            InteractionPhase.INVITING -> after(timings.invitePauseMs) { dispatch(InteractionEvent.InviteCompleted) }
            InteractionPhase.RESPONDING -> {
                if (state.attemptState == AttemptState.ATTEMPT_DETECTED) startNod(state.interactionId)
                after(timings.afterTurnMs) { dispatch(InteractionEvent.ResponseAcknowledged) }
            }
            else -> Unit
        }
    }

    private fun onEffect(effect: InteractionEffect, state: InteractionState) {
        val id = state.interactionId
        when (effect) {
            InteractionEffect.PlayCanonicalWord -> scope.launch { play(state) }
            InteractionEffect.CommitExposure -> scope.launch { persistence.commitExposure(id, clock.nowMs()) }
            InteractionEffect.StartListening -> startTurn(state)
            InteractionEffect.StopListening -> stopListening()
            InteractionEffect.RecordAttempt -> scope.launch { persistence.recordAttempt(id, state.attemptState, clock.nowMs()) }
            // Exactly once per accepted replay; never retried, so one replay is never counted twice.
            InteractionEffect.RecordReplay -> scope.launch { persistence.recordReplay(id) }
            InteractionEffect.CompleteInteraction -> {
                val reason = state.completionReason ?: InteractionCompletionReason.NORMAL
                if (reason == InteractionCompletionReason.CONTENT_ERROR) clipFailuresInARow++
                // Advance before any suspension, so a background now resumes at the next step.
                interaction = null
                stepIndex++
                scope.launch { persistence.completeInteraction(id, reason, clock.nowMs()) }
                replaceTimer { runStep() }
            }
            // onBackground stops all audio itself; nothing else emits these here.
            InteractionEffect.StopPlayback,
            InteractionEffect.PreparePresentation,
            InteractionEffect.EndSession,
            InteractionEffect.DiagnosticInvalidEvent -> Unit
        }
    }

    private fun enterReady() {
        publish()
        after(timings.idleQuietMs) {
            paused = true
            _state.value = ChildScreenState.Quiet
        }
    }

    private suspend fun play(state: InteractionState) {
        val word = pair ?: return
        val result = audioService.playCanonicalWord(
            WordPlaybackRequest(
                sessionId = state.sessionId,
                interactionId = state.interactionId,
                pairId = word.pairId,
                assetRef = AudioAssetRef(word.audio.logicalId, BUNDLED_RESOURCES_ROOT + word.audio.assetPath),
            ),
            audioListener,
        )
        if (result is AppResult.Failure && isPlaying(state.interactionId)) dispatch(InteractionEvent.AudioPlaybackFailed)
    }

    private fun startTurn(state: InteractionState) = replaceTimer {
        heldResult = null
        val decision = measurement()
        if (!isListening(state.interactionId)) return@replaceTimer
        when (decision) {
            AttemptMeasurementDecision.OPEN_WINDOW ->
                if (!startDetector(state)) heldResult = AttemptState.MICROPHONE_UNAVAILABLE
            AttemptMeasurementDecision.NOT_MEASURED -> heldResult = AttemptState.NOT_MEASURED
            AttemptMeasurementDecision.MICROPHONE_UNAVAILABLE -> heldResult = AttemptState.MICROPHONE_UNAVAILABLE
        }
        if (!isListening(state.interactionId)) return@replaceTimer
        pause(timings.turnMs)
        if (!isListening(state.interactionId)) return@replaceTimer
        stopListening()
        val held = heldResult
        dispatch(
            if (held != null) InteractionEvent.AttemptResultRecorded(state.sessionId, state.interactionId, held)
            else InteractionEvent.ListeningTimeout,
        )
    }

    private suspend fun startDetector(state: InteractionState): Boolean {
        val detector = speechAttemptDetector ?: return false
        val request = SpeechAttemptRequest(state.sessionId, state.interactionId, durationMs = timings.turnMs)
        return when (val result = detector.start(request, speechListener)) {
            is AppResult.Failure -> false
            is AppResult.Success -> {
                if (isListening(state.interactionId)) listening = result.value
                true
            }
        }
    }

    // Safe when nothing is listening (a replay from INVITING or RESPONDING emits StopListening).
    private fun stopListening() {
        val handle = listening ?: return
        listening = null
        val detector = speechAttemptDetector ?: return
        scope.launch { detector.stop(handle) }
    }

    private fun startNod(id: InteractionId) {
        nod = true
        nodTimer?.cancel()
        nodTimer = scope.launch {
            pause(timings.nodMs)
            if (interaction?.interactionId != id) return@launch
            nod = false
            publish()
        }
    }

    private fun after(ms: Long, action: suspend () -> Unit) {
        val token = interaction?.interactionId
        replaceTimer { pauseThen(ms, token, action) }
    }

    private suspend fun pauseThen(ms: Long, token: InteractionId?, action: suspend () -> Unit) {
        pause(ms)
        if (interaction?.interactionId == token) action()
    }

    private fun replaceTimer(body: suspend () -> Unit) {
        timer?.cancel()
        timer = scope.launch { body() }
    }

    private fun cancelTimers() {
        timer?.cancel()
        nodTimer?.cancel()
        nod = false
    }

    private fun publish() {
        val current = interaction ?: return
        val word = pair ?: return
        if (paused && _state.value == ChildScreenState.Quiet) return
        _state.value = ChildScreenState.Word(
            pair = word,
            phase = current.phase,
            wordVisible = wordVisible,
            ringVisible = current.phase == InteractionPhase.LISTENING,
            nod = nod,
        )
    }

    private fun isPlaying(id: InteractionId) =
        interaction?.let { it.interactionId == id && it.phase == InteractionPhase.PLAYING_WORD } == true

    private fun isListening(id: InteractionId) =
        interaction?.let { it.interactionId == id && it.phase == InteractionPhase.LISTENING } == true

    private fun isCurrent(handle: PlaybackHandle) = interaction?.accepts(handle.sessionId, handle.interactionId) == true

    private fun isCurrent(handle: SpeechAttemptHandle) = interaction?.accepts(handle.sessionId, handle.interactionId) == true

    private val audioListener = object : AudioPlaybackListener {
        override fun onStarted(handle: PlaybackHandle) {
            if (!isCurrent(handle)) return
            wordVisible = true
            publish()
        }

        override fun onExposureThresholdReached(handle: PlaybackHandle) {
            if (isCurrent(handle)) dispatch(InteractionEvent.WordPlaybackReachedExposureThreshold)
        }

        override fun onCompleted(handle: PlaybackHandle) {
            if (!isCurrent(handle) || interaction?.phase != InteractionPhase.PLAYING_WORD) return
            clipFailuresInARow = 0
            dispatch(InteractionEvent.WordPlaybackCompleted)
        }

        // A null handle comes from a clip that could not open; the reducer accepts it only while playing.
        override fun onFailed(handle: PlaybackHandle?, error: AudioError) {
            if (handle == null || isCurrent(handle)) dispatch(InteractionEvent.AudioPlaybackFailed)
        }
    }

    private val speechListener = object : SpeechAttemptListener {
        override fun onAttemptDetected(handle: SpeechAttemptHandle) = report(handle, AttemptState.ATTEMPT_DETECTED)

        override fun onNoAttemptDetected(handle: SpeechAttemptHandle) = report(handle, AttemptState.NO_ATTEMPT_DETECTED)

        override fun onMicrophoneUnavailable(error: MicrophoneError) {
            // No handle to check: accept only while this interaction's detector is listening.
            val current = interaction ?: return
            if (listening == null || !isListening(current.interactionId)) return
            listening = null
            heldResult = AttemptState.MICROPHONE_UNAVAILABLE
        }

        private fun report(handle: SpeechAttemptHandle, state: AttemptState) {
            if (!isCurrent(handle)) return
            if (listening == handle) listening = null
            dispatch(InteractionEvent.AttemptResultRecorded(handle.sessionId, handle.interactionId, state))
        }
    }
}
