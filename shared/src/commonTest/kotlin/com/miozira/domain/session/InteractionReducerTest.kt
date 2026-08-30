package com.miozira.domain.session

import com.miozira.core.model.AttemptState
import com.miozira.core.model.InteractionId
import com.miozira.core.model.PairId
import com.miozira.core.model.SessionId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class InteractionReducerTest {
    private val base = InteractionState(
        phase = InteractionPhase.SESSION_STARTING,
        sessionId = SessionId("session-1"),
        interactionId = InteractionId("interaction-1"),
        pairId = PairId("apple:en"),
        allowSpeakingInvite = true,
    )

    @Test
    fun happyPath_commitsExposureOnce_andCompletesAfterResponse() {
        var state = base
        state = reduce(state, InteractionEvent.StartSession).state
        state = reduce(state, InteractionEvent.PresentationSettled).state
        state = reduce(state, InteractionEvent.ConceptTapped).state
        val threshold = reduce(state, InteractionEvent.WordPlaybackReachedExposureThreshold)
        assertTrue(threshold.state.exposureCommitted)
        assertEquals(listOf(InteractionEffect.CommitExposure), threshold.effects)
        state = threshold.state
        val duplicate = reduce(state, InteractionEvent.WordPlaybackReachedExposureThreshold)
        assertTrue(duplicate.effects.isEmpty())
        state = reduce(state, InteractionEvent.WordPlaybackCompleted).state
        state = reduce(state, InteractionEvent.InviteCompleted).state
        state = reduce(
            state,
            InteractionEvent.AttemptResultRecorded(state.sessionId, state.interactionId, AttemptState.ATTEMPT_DETECTED),
        ).state
        val response = reduce(state, InteractionEvent.ResponseAcknowledged)
        assertEquals(InteractionPhase.TRANSITIONING, response.state.phase)
        assertEquals(listOf(InteractionEffect.CompleteInteraction), response.effects)
    }

    @Test
    fun microphoneDisabled_skipsInvitation_andSilenceIsNotFailure() {
        var state = base.copy(allowSpeakingInvite = false)
        state = reduce(state, InteractionEvent.StartSession).state
        state = reduce(state, InteractionEvent.PresentationSettled).state
        state = reduce(state, InteractionEvent.ConceptTapped).state
        state = reduce(state, InteractionEvent.WordPlaybackCompleted).state
        assertEquals(InteractionPhase.RESPONDING, state.phase)
        val invalid = reduce(
            state,
            InteractionEvent.AttemptResultRecorded(state.sessionId, state.interactionId, AttemptState.ATTEMPT_DETECTED),
        )
        assertEquals(listOf(InteractionEffect.DiagnosticInvalidEvent), invalid.effects)
        val completed = reduce(state, InteractionEvent.ResponseAcknowledged)
        assertEquals(InteractionPhase.TRANSITIONING, completed.state.phase)
    }

    @Test
    fun listeningTimeout_recordsNoAttempt_withoutEvaluation() {
        var state = base
        state = reduce(state, InteractionEvent.StartSession).state
        state = reduce(state, InteractionEvent.PresentationSettled).state
        state = reduce(state, InteractionEvent.ConceptTapped).state
        state = reduce(state, InteractionEvent.WordPlaybackCompleted).state
        state = reduce(state, InteractionEvent.InviteCompleted).state
        val timeout = reduce(state, InteractionEvent.ListeningTimeout)
        assertEquals(InteractionPhase.RESPONDING, timeout.state.phase)
        assertEquals(listOf(InteractionEffect.RecordAttempt), timeout.effects)
        assertEquals(AttemptState.NO_ATTEMPT_DETECTED, timeout.state.attemptState)
    }

    @Test
    fun rotation_preservesState_andbackgroundDoesNotFabricateEvidence() {
        val playing = reduce(
            reduce(
                reduce(base, InteractionEvent.StartSession).state,
                InteractionEvent.PresentationSettled,
            ).state,
            InteractionEvent.ConceptTapped,
        ).state
        val rotated = reduce(playing, InteractionEvent.OrientationChanged)
        assertEquals(playing, rotated.state)
        assertFalse(rotated.state.exposureCommitted)
        val backgrounded = reduce(playing, InteractionEvent.AppBackgrounded)
        assertEquals(InteractionPhase.IDLE, backgrounded.state.phase)
        assertFalse(backgrounded.state.exposureCommitted)
    }

    @Test
    fun invalidEvents_leaveStateUnchanged_andonlyExposeDiagnosticEffect() {
        val result = reduce(base, InteractionEvent.ConceptTapped)
        assertEquals(base, result.state)
        assertEquals(listOf(InteractionEffect.DiagnosticInvalidEvent), result.effects)
    }
}
