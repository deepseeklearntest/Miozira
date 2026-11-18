package com.miozira.domain.session

import com.miozira.core.model.AttemptState
import com.miozira.core.model.InteractionCompletionReason
import com.miozira.core.model.InteractionId
import com.miozira.core.model.PairId
import com.miozira.core.model.SessionId

enum class InteractionPhase {
    SESSION_STARTING,
    PRESENTING,
    READY,
    PLAYING_WORD,
    INVITING,
    LISTENING,
    RESPONDING,
    TRANSITIONING,
    IDLE,
    SESSION_ENDING,
    ENDED,
}

data class InteractionState(
    val phase: InteractionPhase,
    val sessionId: SessionId,
    val interactionId: InteractionId,
    val pairId: PairId,
    val allowSpeakingInvite: Boolean = false,
    val exposureCommitted: Boolean = false,
    val replayCount: Int = 0,
    val attemptState: AttemptState = AttemptState.NOT_MEASURED,
    val completionReason: InteractionCompletionReason? = null,
) {
    fun accepts(sessionId: SessionId, interactionId: InteractionId): Boolean =
        this.sessionId == sessionId && this.interactionId == interactionId
}

sealed interface InteractionEvent {
    data object StartSession : InteractionEvent
    data object PresentationSettled : InteractionEvent
    data object ConceptTapped : InteractionEvent
    data object WordPlaybackReachedExposureThreshold : InteractionEvent
    data object WordPlaybackCompleted : InteractionEvent
    data object InviteCompleted : InteractionEvent
    data object ListeningTimeout : InteractionEvent
    data class AttemptResultRecorded(
        val sessionId: SessionId,
        val interactionId: InteractionId,
        val attemptState: AttemptState,
    ) : InteractionEvent
    data object ResponseAcknowledged : InteractionEvent
    data object NextInteractionReady : InteractionEvent
    data object OrientationChanged : InteractionEvent
    data object AppBackgrounded : InteractionEvent
    data object AppForegrounded : InteractionEvent
    data object SessionStopRequested : InteractionEvent
    data object AudioPlaybackFailed : InteractionEvent
    data class ReplayRequested(val sessionId: SessionId, val interactionId: InteractionId) : InteractionEvent
}

sealed interface InteractionEffect {
    data object PreparePresentation : InteractionEffect
    data object PlayCanonicalWord : InteractionEffect
    data object CommitExposure : InteractionEffect
    data object StartListening : InteractionEffect
    data object StopListening : InteractionEffect
    data object RecordAttempt : InteractionEffect
    data object RecordReplay : InteractionEffect
    data object CompleteInteraction : InteractionEffect
    data object EndSession : InteractionEffect
    data object StopPlayback : InteractionEffect
    data object DiagnosticInvalidEvent : InteractionEffect
}

data class Transition(
    val state: InteractionState,
    val effects: List<InteractionEffect> = emptyList(),
)

fun reduce(state: InteractionState, event: InteractionEvent): Transition {
    fun unchanged(vararg effects: InteractionEffect) = Transition(state, effects.toList())
    fun invalid() = unchanged(InteractionEffect.DiagnosticInvalidEvent)
    fun move(phase: InteractionPhase, vararg effects: InteractionEffect) =
        Transition(state.copy(phase = phase), effects.toList())
    // Replay during his turn: play the word again; exposureCommitted is untouched, so no second exposure.
    fun replay(event: InteractionEvent.ReplayRequested) = if (state.accepts(event.sessionId, event.interactionId)) {
        Transition(
            state.copy(phase = InteractionPhase.PLAYING_WORD, replayCount = state.replayCount + 1),
            listOf(InteractionEffect.StopListening, InteractionEffect.PlayCanonicalWord, InteractionEffect.RecordReplay),
        )
    } else invalid()

    return when (state.phase) {
        InteractionPhase.SESSION_STARTING -> when (event) {
            InteractionEvent.StartSession -> move(InteractionPhase.PRESENTING, InteractionEffect.PreparePresentation)
            else -> invalid()
        }
        InteractionPhase.PRESENTING -> when (event) {
            InteractionEvent.PresentationSettled -> move(InteractionPhase.READY)
            InteractionEvent.OrientationChanged -> unchanged()
            InteractionEvent.AppBackgrounded -> move(InteractionPhase.IDLE, InteractionEffect.StopPlayback, InteractionEffect.StopListening)
            else -> invalid()
        }
        InteractionPhase.READY -> when (event) {
            InteractionEvent.ConceptTapped -> move(InteractionPhase.PLAYING_WORD, InteractionEffect.PlayCanonicalWord)
            is InteractionEvent.ReplayRequested -> unchanged()
            InteractionEvent.OrientationChanged -> unchanged()
            InteractionEvent.AppBackgrounded -> move(InteractionPhase.IDLE, InteractionEffect.StopPlayback, InteractionEffect.StopListening)
            InteractionEvent.SessionStopRequested -> move(InteractionPhase.SESSION_ENDING, InteractionEffect.EndSession)
            else -> invalid()
        }
        InteractionPhase.PLAYING_WORD -> when (event) {
            InteractionEvent.WordPlaybackReachedExposureThreshold -> if (state.exposureCommitted) {
                unchanged()
            } else {
                Transition(state.copy(exposureCommitted = true), listOf(InteractionEffect.CommitExposure))
            }
            InteractionEvent.WordPlaybackCompleted -> move(
                if (state.allowSpeakingInvite) InteractionPhase.INVITING else InteractionPhase.RESPONDING,
            )
            InteractionEvent.ConceptTapped -> unchanged()
            InteractionEvent.OrientationChanged -> unchanged()
            InteractionEvent.AppBackgrounded -> move(InteractionPhase.IDLE, InteractionEffect.StopPlayback, InteractionEffect.StopListening)
            InteractionEvent.SessionStopRequested -> move(InteractionPhase.SESSION_ENDING, InteractionEffect.StopPlayback, InteractionEffect.EndSession)
            InteractionEvent.AudioPlaybackFailed -> Transition(
                state.copy(phase = InteractionPhase.TRANSITIONING, completionReason = InteractionCompletionReason.CONTENT_ERROR),
                listOf(InteractionEffect.CompleteInteraction),
            )
            else -> invalid()
        }
        InteractionPhase.INVITING -> when (event) {
            InteractionEvent.InviteCompleted -> move(InteractionPhase.LISTENING, InteractionEffect.StartListening)
            is InteractionEvent.ReplayRequested -> replay(event)
            InteractionEvent.OrientationChanged -> unchanged()
            InteractionEvent.AppBackgrounded -> move(InteractionPhase.IDLE, InteractionEffect.StopListening)
            InteractionEvent.SessionStopRequested -> move(InteractionPhase.SESSION_ENDING, InteractionEffect.StopListening, InteractionEffect.EndSession)
            else -> invalid()
        }
        InteractionPhase.LISTENING -> when (event) {
            is InteractionEvent.AttemptResultRecorded -> if (state.accepts(event.sessionId, event.interactionId)) {
                Transition(
                    state.copy(phase = InteractionPhase.RESPONDING, attemptState = event.attemptState),
                    listOf(InteractionEffect.RecordAttempt),
                )
            } else unchanged(InteractionEffect.DiagnosticInvalidEvent)
            is InteractionEvent.ReplayRequested -> replay(event)
            InteractionEvent.ListeningTimeout -> Transition(
                state.copy(phase = InteractionPhase.RESPONDING, attemptState = AttemptState.NO_ATTEMPT_DETECTED),
                listOf(InteractionEffect.RecordAttempt),
            )
            InteractionEvent.OrientationChanged -> unchanged()
            InteractionEvent.AppBackgrounded -> move(InteractionPhase.IDLE, InteractionEffect.StopListening)
            InteractionEvent.SessionStopRequested -> move(InteractionPhase.SESSION_ENDING, InteractionEffect.StopListening, InteractionEffect.EndSession)
            else -> invalid()
        }
        InteractionPhase.RESPONDING -> when (event) {
            InteractionEvent.ResponseAcknowledged -> move(InteractionPhase.TRANSITIONING, InteractionEffect.CompleteInteraction)
            is InteractionEvent.ReplayRequested -> replay(event)
            InteractionEvent.OrientationChanged -> unchanged()
            InteractionEvent.AppBackgrounded -> move(InteractionPhase.IDLE, InteractionEffect.StopListening)
            InteractionEvent.SessionStopRequested -> move(InteractionPhase.SESSION_ENDING, InteractionEffect.EndSession)
            else -> invalid()
        }
        InteractionPhase.TRANSITIONING -> when (event) {
            InteractionEvent.NextInteractionReady -> move(InteractionPhase.PRESENTING, InteractionEffect.PreparePresentation)
            InteractionEvent.SessionStopRequested -> move(InteractionPhase.SESSION_ENDING, InteractionEffect.EndSession)
            InteractionEvent.AppBackgrounded -> move(InteractionPhase.IDLE)
            else -> invalid()
        }
        InteractionPhase.IDLE -> when (event) {
            InteractionEvent.AppForegrounded -> move(InteractionPhase.READY)
            InteractionEvent.SessionStopRequested -> move(InteractionPhase.SESSION_ENDING, InteractionEffect.EndSession)
            else -> unchanged()
        }
        InteractionPhase.SESSION_ENDING -> when (event) {
            InteractionEvent.AppBackgrounded -> unchanged()
            else -> move(InteractionPhase.ENDED)
        }
        InteractionPhase.ENDED -> unchanged()
    }
}
