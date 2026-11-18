package com.miozira.domain.session

import com.miozira.core.model.AttemptState
import com.miozira.core.model.InteractionCompletionReason
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

    private fun playing(): InteractionState {
        var state = base
        state = reduce(state, InteractionEvent.StartSession).state
        state = reduce(state, InteractionEvent.PresentationSettled).state
        return reduce(state, InteractionEvent.ConceptTapped).state
    }

    private fun listening(): InteractionState {
        var state = reduce(playing(), InteractionEvent.WordPlaybackReachedExposureThreshold).state
        state = reduce(state, InteractionEvent.WordPlaybackCompleted).state
        return reduce(state, InteractionEvent.InviteCompleted).state
    }

    @Test
    fun replayDuringListening_replaysTheWord_andCountsTheReplay() {
        val state = listening()
        assertEquals(InteractionPhase.LISTENING, state.phase)
        val replay = reduce(state, InteractionEvent.ReplayRequested(state.sessionId, state.interactionId))
        assertEquals(InteractionPhase.PLAYING_WORD, replay.state.phase)
        assertEquals(1, replay.state.replayCount)
        assertEquals(
            listOf(InteractionEffect.StopListening, InteractionEffect.PlayCanonicalWord, InteractionEffect.RecordReplay),
            replay.effects,
        )
        val inviting = reduce(reduce(playing(), InteractionEvent.WordPlaybackCompleted).state, InteractionEvent.ReplayRequested(state.sessionId, state.interactionId))
        assertEquals(InteractionPhase.PLAYING_WORD, inviting.state.phase)
        val responding = reduce(state, InteractionEvent.ListeningTimeout).state
        val fromResponding = reduce(responding, InteractionEvent.ReplayRequested(state.sessionId, state.interactionId))
        assertEquals(InteractionPhase.PLAYING_WORD, fromResponding.state.phase)
        assertEquals(1, fromResponding.state.replayCount)
    }

    @Test
    fun afterAReplay_theExposureThresholdDoesNotCommitAgain() {
        val state = listening()
        val replayed = reduce(state, InteractionEvent.ReplayRequested(state.sessionId, state.interactionId)).state
        assertTrue(replayed.exposureCommitted)
        val threshold = reduce(replayed, InteractionEvent.WordPlaybackReachedExposureThreshold)
        assertTrue(threshold.state.exposureCommitted)
        assertFalse(InteractionEffect.CommitExposure in threshold.effects)
    }

    @Test
    fun replayWithStaleIds_isIgnored() {
        val state = listening()
        val staleInteraction = reduce(state, InteractionEvent.ReplayRequested(state.sessionId, InteractionId("old")))
        assertEquals(state, staleInteraction.state)
        assertEquals(listOf(InteractionEffect.DiagnosticInvalidEvent), staleInteraction.effects)
        val staleSession = reduce(state, InteractionEvent.ReplayRequested(SessionId("old"), state.interactionId))
        assertEquals(state, staleSession.state)
        assertEquals(listOf(InteractionEffect.DiagnosticInvalidEvent), staleSession.effects)
    }

    @Test
    fun failedClip_completesTheInteractionAsContentError_andDoesNotEndTheSession() {
        val failed = reduce(playing(), InteractionEvent.AudioPlaybackFailed)
        assertEquals(InteractionPhase.TRANSITIONING, failed.state.phase)
        assertEquals(InteractionCompletionReason.CONTENT_ERROR, failed.state.completionReason)
        assertEquals(listOf(InteractionEffect.CompleteInteraction), failed.effects)
    }

    @Test
    fun invalidEvents_leaveStateUnchanged_andonlyExposeDiagnosticEffect() {
        val result = reduce(base, InteractionEvent.ConceptTapped)
        assertEquals(base, result.state)
        assertEquals(listOf(InteractionEffect.DiagnosticInvalidEvent), result.effects)
    }
}
