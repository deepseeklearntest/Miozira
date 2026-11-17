package com.miozira.domain.session

import com.miozira.content.LanguageId
import com.miozira.core.model.AttemptState
import com.miozira.core.model.InteractionCompletionReason
import com.miozira.core.model.InteractionId
import com.miozira.core.model.SessionId
import com.miozira.core.result.AppResult
import com.miozira.core.time.Clock
import com.miozira.presentation.child.ChildSessionUiState
import com.miozira.services.audio.AudioPlaybackListener
import com.miozira.services.audio.AudioService
import com.miozira.services.audio.PlaybackHandle
import com.miozira.services.audio.WordPlaybackRequest
import com.miozira.services.audio.AudioAssetRef
import com.miozira.services.permissions.AttemptMeasurementDecision
import com.miozira.services.microphone.SpeechAttemptConfig
import com.miozira.services.microphone.SpeechAttemptDetector
import com.miozira.services.microphone.SpeechAttemptHandle
import com.miozira.services.microphone.SpeechAttemptListener
import com.miozira.services.microphone.SpeechAttemptRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope

class ChildSessionController(
    private val engine: LearningSessionEngine,
    private val persistence: SessionPersistence,
    private val audioService: AudioService,
    private val clock: Clock,
    private val scope: CoroutineScope,
    private val speechAttemptDetector: SpeechAttemptDetector? = null,
    private val speechAttemptConfig: SpeechAttemptConfig = SpeechAttemptConfig(),
    private val speakingInviteEnabled: Boolean = false,
) {
    private val _state = MutableStateFlow<ChildSessionUiState?>(null)
    val state: StateFlow<ChildSessionUiState?> = _state.asStateFlow()
    private var interaction: InteractionState? = null
    private var playback: PlaybackHandle? = null

    suspend fun start(): AppResult<Unit> {
        val session = when (val result = engine.startSession()) {
            is AppResult.Failure -> return result
            is AppResult.Success -> result.value
        }
        // Temporary bridge until the controller drives the full step list (plan Task 7 rewrites this).
        val firstStep = SessionStep.Word(SessionPart.WARM_UP, session.startingLanguage ?: LanguageId("en"), easy = true)
        return when (val result = engine.beginInteraction(firstStep, allowSpeakingInvite = speakingInviteEnabled)) {
            is AppResult.Failure -> result
            is AppResult.Success -> {
                interaction = InteractionState(
                    phase = InteractionPhase.SESSION_STARTING,
                    sessionId = session.sessionId,
                    interactionId = result.value.interactionId,
                    pairId = result.value.pair.pairId,
                    allowSpeakingInvite = result.value.allowSpeakingInvite,
                )
                dispatch(InteractionEvent.StartSession)
                dispatch(InteractionEvent.PresentationSettled)
                publish(result.value.pair)
                AppResult.Success(Unit)
            }
        }
    }

    fun tap() {
        dispatch(InteractionEvent.ConceptTapped)
        val current = interaction ?: return
        val pair = _state.value?.pair ?: return
        scope.launch {
            val result = audioService.playCanonicalWord(
                WordPlaybackRequest(
                    sessionId = current.sessionId,
                    interactionId = current.interactionId,
                    pairId = current.pairId,
                    assetRef = AudioAssetRef(pair.audio.logicalId, pair.audio.assetPath),
                ),
                listener,
            )
            if (result is AppResult.Success) playback = result.value
        }
    }

    fun acknowledgeResponse() {
        val current = interaction ?: return
        val transition = dispatch(InteractionEvent.ResponseAcknowledged)
        if (InteractionEffect.CompleteInteraction in transition.effects) {
            scope.launch {
                if (current.attemptState != AttemptState.NOT_MEASURED) {
                    persistence.recordAttempt(current.interactionId, current.attemptState, clock.nowMs())
                }
                persistence.completeInteraction(
                    current.interactionId,
                    InteractionCompletionReason.NORMAL,
                    clock.nowMs(),
                )
            }
        }
    }

    fun completeInvitation(measurementDecision: AttemptMeasurementDecision) {
        val current = interaction ?: return
        dispatch(InteractionEvent.InviteCompleted)
        when (measurementDecision) {
            AttemptMeasurementDecision.NOT_MEASURED -> recordAttemptResult(current, AttemptState.NOT_MEASURED)
            AttemptMeasurementDecision.MICROPHONE_UNAVAILABLE -> recordAttemptResult(current, AttemptState.MICROPHONE_UNAVAILABLE)
            AttemptMeasurementDecision.OPEN_WINDOW -> {
                val detector = speechAttemptDetector ?: run {
                    recordAttemptResult(current, AttemptState.MICROPHONE_UNAVAILABLE)
                    return
                }
                scope.launch {
                    detector.start(
                        SpeechAttemptRequest(
                            sessionId = current.sessionId,
                            interactionId = current.interactionId,
                            durationMs = speechAttemptConfig.windowDurationMs,
                        ),
                        speechListener,
                    )
                }
            }
        }
    }

    fun onBackground() {
        dispatch(InteractionEvent.AppBackgrounded)
        scope.launch {
            audioService.stopAll()
            speechAttemptDetector?.stopAll()
        }
    }

    fun onForeground() {
        if (interaction?.phase == InteractionPhase.IDLE) {
            dispatch(InteractionEvent.AppForegrounded)
        }
    }

    private val listener = object : AudioPlaybackListener {
        override fun onStarted(handle: PlaybackHandle) {
            if (isCurrent(handle)) playback = handle
        }

        override fun onExposureThresholdReached(handle: PlaybackHandle) {
            if (!isCurrent(handle)) return
            val current = interaction ?: return
            val transition = dispatch(InteractionEvent.WordPlaybackReachedExposureThreshold)
            if (InteractionEffect.CommitExposure in transition.effects) {
                scope.launch { persistence.commitExposure(current.interactionId, clock.nowMs()) }
            }
        }

        override fun onCompleted(handle: PlaybackHandle) {
            if (!isCurrent(handle)) return
            dispatch(InteractionEvent.WordPlaybackCompleted)
            playback = null
        }

        override fun onFailed(handle: PlaybackHandle?, error: com.miozira.services.audio.AudioError) {
            if (handle == null || isCurrent(handle)) dispatch(InteractionEvent.AudioPlaybackFailed)
            playback = null
        }
    }

    private val speechListener = object : SpeechAttemptListener {
        override fun onAttemptDetected(handle: SpeechAttemptHandle) {
            if (isCurrent(handle)) interaction?.let { recordAttemptResult(it, AttemptState.ATTEMPT_DETECTED) }
        }

        override fun onNoAttemptDetected(handle: SpeechAttemptHandle) {
            if (isCurrent(handle)) interaction?.let { recordAttemptResult(it, AttemptState.NO_ATTEMPT_DETECTED) }
        }

        override fun onMicrophoneUnavailable(error: com.miozira.services.microphone.MicrophoneError) {
            val current = interaction ?: return
            if (current.phase == InteractionPhase.LISTENING) {
                recordAttemptResult(current, AttemptState.MICROPHONE_UNAVAILABLE)
            }
        }
    }

    private fun isCurrent(handle: PlaybackHandle): Boolean =
        interaction?.let { it.accepts(handle.sessionId, handle.interactionId) } == true

    private fun isCurrent(handle: SpeechAttemptHandle): Boolean =
        interaction?.let { it.accepts(handle.sessionId, handle.interactionId) } == true

    private fun recordAttemptResult(current: InteractionState, attemptState: AttemptState) {
        val transition = dispatch(
            InteractionEvent.AttemptResultRecorded(current.sessionId, current.interactionId, attemptState),
        )
        if (InteractionEffect.RecordAttempt in transition.effects) {
            scope.launch { persistence.recordAttempt(current.interactionId, attemptState, clock.nowMs()) }
        }
    }

    private fun dispatch(event: InteractionEvent): Transition {
        val current = interaction ?: return Transition(
            InteractionState(
                InteractionPhase.ENDED,
                SessionId("none"),
                InteractionId("none"),
                com.miozira.core.model.PairId("none"),
            ),
        )
        val transition = reduce(current, event)
        interaction = transition.state
        _state.value = _state.value?.copy(phase = transition.state.phase)
        return transition
    }

    private fun publish(pair: com.miozira.content.ConceptLanguagePair) {
        _state.value = ChildSessionUiState.ready(pair).copy(phase = InteractionPhase.READY)
    }
}
