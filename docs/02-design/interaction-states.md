# Miozira — Interaction States

**Document:** `interaction-states.md`  
**Version:** 0.1  
**Status:** Draft for Prototype 0.1  
**Product:** Miozira  
**Prototype:** Android-first, 8 concepts × English + Tamil  
**Last updated:** 2026-08-25

---

## 1. Purpose

This document defines the major UI and interaction state machines for Miozira Prototype 0.1.

It makes explicit:

- valid states;
- valid events;
- transitions;
- timeouts;
- replay behavior;
- microphone behavior;
- parent-gate behavior;
- orientation changes;
- app background/foreground handling;
- error fallback;
- persistence boundaries.

The goal is to prevent ambiguous implementation.

This specification should be referenced by:

- `ui-ux.md`
- `session-design.md`
- `architecture.md`
- `api.md`
- `audio.md`
- `microphone.md`
- `database.md`
- `testing.md`

---

# 2. State-Machine Principles

## IS-P01 — State is explicit

The UI should never infer important behavior from scattered booleans when a clear state can be represented.

Prefer:

```text
interactionState = LISTENING
```

over:

```text
audioPlaying = false
micEnabled = true
promptShown = true
waiting = true
```

---

## IS-P02 — One canonical current state

For the child interaction, there should be one authoritative current interaction state.

---

## IS-P03 — Events drive transitions

State changes should happen because of explicit events.

Examples:

```text
ConceptPresented
ConceptTapped
WordPlaybackCompleted
ListeningTimeout
SpeechAttemptDetected
OrientationChanged
AppBackgrounded
```

---

## IS-P04 — Side effects do not define state

Playing audio, writing to storage, or opening the microphone are effects caused by transitions.

They should not themselves be used as the only source of truth for the UI.

---

## IS-P05 — Rotation is not a learning event

Orientation change must never create:

- a new exposure;
- a replay;
- a new session item;
- a new speaking attempt.

---

## IS-P06 — Planned is not exposed

A scheduled concept does not count as exposed until exposure requirements are actually met.

---

# 3. High-Level Application States

Prototype 0.1 may be modeled with:

```text
APP_STARTING
CHILD_MODE
PARENT_GATE
PARENT_MODE
APP_BACKGROUND
RECOVERY
```

These are top-level application states.

---

# 4. APP_STARTING

Entry conditions:

- cold launch;
- process restart;
- returning after system termination.

Responsibilities:

- load required local state;
- recover settings;
- determine whether setup is complete;
- initialize local services;
- avoid network dependency.

Valid transitions:

```text
APP_STARTING → CHILD_MODE
APP_STARTING → PARENT_MODE      if adult setup required
APP_STARTING → RECOVERY         if local state is inconsistent
```

---

# 5. CHILD_MODE

`CHILD_MODE` contains the active learning session state machine.

Possible child-session states:

```text
SESSION_STARTING
PRESENTING
READY
PLAYING_WORD
INVITING
LISTENING
RESPONDING
TRANSITIONING
IDLE
SESSION_ENDING
SESSION_ENDED
```

---

# 6. SESSION_STARTING

Purpose:

- create or initialize session context;
- ask adaptive engine for initial item;
- choose familiar opener where available;
- set active language;
- prepare UI.

Entry event:

```text
StartChildSession
```

Valid transitions:

```text
SESSION_STARTING → PRESENTING
SESSION_STARTING → SESSION_ENDED   if session cannot validly start
```

No exposure is recorded here.

---

# 7. PRESENTING

Purpose:

- display the selected concept;
- establish stable visual state;
- prepare tap interaction.

Entry side effects may include:

- load local visual asset;
- set active concept;
- set active language.

No exposure is recorded merely because `PRESENTING` begins.

Valid events:

```text
PresentationSettled
AppBackgrounded
OrientationChanged
FatalContentUnavailable
```

Transitions:

```text
PRESENTING → READY
PRESENTING → APP_BACKGROUND
PRESENTING → RECOVERY
```

Orientation change should preserve `PRESENTING`.

---

# 8. READY

Purpose:

- wait for the child to tap the concept.

Entry conditions:

- concept visible;
- primary hit target enabled;
- canonical word audio available.

Valid events:

```text
ConceptTapped
InactivityHintTimeout
ParentGateGestureStarted
OrientationChanged
AppBackgrounded
SessionStopRequested
```

Transitions:

```text
READY → PLAYING_WORD
READY → READY               after subtle inactivity hint
READY → PARENT_GATE         if deliberate parent gesture completes
READY → APP_BACKGROUND
READY → SESSION_ENDING
```

---

# 9. Concept Tap Handling

When `ConceptTapped` occurs in `READY`:

1. acknowledge tap visually;
2. begin canonical word playback;
3. move to `PLAYING_WORD`.

The tap itself does not yet guarantee a valid exposure.

Exposure becomes valid only when enough of the target word is actually presented according to audio rules.

---

# 10. PLAYING_WORD

Purpose:

- play canonical target-language audio;
- prevent overlapping playback;
- establish the main exposure.

Valid events:

```text
WordPlaybackReachedExposureThreshold
WordPlaybackCompleted
ConceptTappedAgain
AudioPlaybackFailed
OrientationChanged
AppBackgrounded
SessionStopRequested
```

---

# 11. Exposure Threshold

The system should distinguish:

```text
audio started
```

from:

```text
valid exposure occurred
```

Recommended rule:

A valid exposure is committed when either:

- the canonical word clip completes; or
- enough of the clip has played that the target spoken form was meaningfully audible.

Exact threshold belongs in `audio.md`.

---

# 12. Exposure Commit

When `WordPlaybackReachedExposureThreshold` occurs:

- mark the current interaction as exposure-valid;
- persist exposure exactly once;
- do not repeat this write on orientation recreation or replay.

Suggested interaction field:

```text
exposureCommitted = true
```

---

# 13. Tap During Playback

If the child taps again while audio is already playing:

allowed Prototype 0.1 behaviors:

### Option A — Ignore until completion

Safest initial choice.

### Option B — Queue one replay

Possible if testing shows children expect repeated tapping to replay.

Not allowed:

- simultaneous overlapping word audio;
- multiple exposure records from tap flooding.

Recommended v0.1:

> ignore or debounce additional taps until current playback ends.

---

# 14. PLAYING_WORD Transitions

On successful completion:

```text
PLAYING_WORD → INVITING
```

If microphone invitation is disabled for this item:

```text
PLAYING_WORD → RESPONDING
```

On playback failure:

```text
PLAYING_WORD → RECOVERY
```

or skip safely to another item if canonical fallback exists.

---

# 15. INVITING

Purpose:

- provide a brief imitation opportunity.

Possible effects:

- short spoken cue;
- visual speech cue;
- short pause.

Valid events:

```text
InviteCompleted
MicrophoneUnavailable
SessionStopRequested
OrientationChanged
AppBackgrounded
```

Transitions:

```text
INVITING → LISTENING
INVITING → RESPONDING     if microphone unavailable/disabled
INVITING → SESSION_ENDING
INVITING → APP_BACKGROUND
```

---

# 16. LISTENING

Purpose:

- activate microphone for a brief child-speaking window.

Entry effect:

```text
MicrophoneService.startAttemptWindow()
```

Recommended duration:

> approximately 2–3 seconds.

Valid events:

```text
SpeechAttemptDetected
ListeningTimeout
MicrophoneFailure
SessionStopRequested
OrientationChanged
AppBackgrounded
```

---

# 17. Speech Attempt Detected

When `SpeechAttemptDetected` occurs:

- record participation evidence;
- do not claim correctness;
- stop microphone promptly;
- transition to `RESPONDING`.

Transition:

```text
LISTENING → RESPONDING
```

Attempt state:

```text
ATTEMPT_DETECTED
```

---

# 18. Listening Timeout

When no speech-like activity occurs before the window ends:

- stop microphone;
- record `NO_ATTEMPT_DETECTED` only if measurement was actually active;
- transition to `RESPONDING`.

Transition:

```text
LISTENING → RESPONDING
```

---

# 19. Microphone Failure

If microphone fails during listening:

- stop/close microphone resources;
- record `MICROPHONE_UNAVAILABLE` or equivalent;
- do not show child error;
- continue.

Transition:

```text
LISTENING → RESPONDING
```

---

# 20. Orientation During LISTENING

Rotation must not:

- restart the listening window from zero;
- create a second microphone session;
- duplicate attempt events.

Preferred behavior:

- preserve remaining listening duration;
- preserve whether attempt has already been detected;
- retain one logical microphone-attempt window.

---

# 21. App Background During LISTENING

When app backgrounds:

1. stop microphone immediately;
2. persist any already committed interaction data;
3. move to app-background state.

Do not keep listening in background.

---

# 22. RESPONDING

Purpose:

- provide brief neutral/warm acknowledgement;
- optionally replay canonical model after no attempt;
- prepare transition.

Response type may be:

```text
ATTEMPT_ACK
NO_ATTEMPT_MODEL
MIC_UNAVAILABLE_NEUTRAL
```

No response may imply pronunciation correctness.

---

# 23. RESPONDING Events

Valid events:

```text
ResponseCompleted
ReplayRequested
SessionStopRequested
OrientationChanged
AppBackgrounded
```

Transitions:

```text
RESPONDING → TRANSITIONING
RESPONDING → PLAYING_WORD      if intentional replay
RESPONDING → SESSION_ENDING
RESPONDING → APP_BACKGROUND
```

---

# 24. Replay After Response

If child taps the concept again before transition completes and replay is allowed:

- replay current canonical word;
- increment replay evidence;
- do not create a second scheduled exposure.

This is a child-initiated replay.

---

# 25. Scheduled Exposure vs Replay

Each interaction instance should distinguish:

```text
scheduledExposure = true
replay = false
```

from:

```text
scheduledExposure = false
replay = true
```

Replay may still be useful learning evidence but should not create a new scheduled interaction count.

---

# 26. TRANSITIONING

Purpose:

- complete current interaction;
- pass interaction result to adaptive engine;
- request next item;
- animate to next concept.

Valid events:

```text
TransitionCompleted
NoNextItem
SessionStopRequested
AppBackgrounded
OrientationChanged
```

Transitions:

```text
TRANSITIONING → PRESENTING
TRANSITIONING → SESSION_ENDING
TRANSITIONING → APP_BACKGROUND
```

---

# 27. Interaction Completion Boundary

An interaction result should be finalized before requesting the next item.

Possible result:

```text
conceptLanguagePair
exposureValid
replayCount
attemptState
startedAt
completedAt
selectionReason
```

Once finalized, it should not mutate because of the next item's UI state.

---

# 28. IDLE

`IDLE` is optional and represents sustained child inactivity.

Entry may occur after repeated inactivity timeouts.

Behavior:

- no microphone;
- no repeated nagging;
- minimal/no animation.

Valid events:

```text
ConceptTapped
SessionStopRequested
IdleEndTimeout
ParentGateGesture
AppBackgrounded
```

Transitions:

```text
IDLE → PLAYING_WORD
IDLE → SESSION_ENDING
IDLE → PARENT_GATE
IDLE → APP_BACKGROUND
```

---

# 29. SESSION_ENDING

Purpose:

- stop active audio/microphone;
- commit valid pending interaction data;
- close session record;
- generate/store real-world suggestion if appropriate.

Entry reasons may include:

```text
PLANNED_END
CHILD_INACTIVE
PARENT_STOP
APP_EXIT
LONG_INTERRUPTION
```

No reason is treated as child failure.

---

# 30. SESSION_ENDED

Session is inactive.

Possible next actions:

```text
StartNewSession
EnterParentGate
ExitApp
```

The child should not be forced through a completion screen.

---

# 31. Parent Gate State Machine

Top-level states:

```text
GATE_HIDDEN
GATE_HOLDING
GATE_CONFIRMING
GATE_ACCEPTED
GATE_CANCELLED
```

---

# 32. GATE_HIDDEN

Normal child mode.

Parent hotspot is not visually prominent.

Event:

```text
ParentHotspotLongPressStarted
```

Transition:

```text
GATE_HIDDEN → GATE_HOLDING
```

---

# 33. GATE_HOLDING

Purpose:

- verify deliberate sustained gesture.

Recommended duration:

> approximately 2 seconds.

Events:

```text
HoldCompleted
HoldCancelled
ChildInteractionElsewhere
```

Transitions:

```text
GATE_HOLDING → GATE_CONFIRMING
GATE_HOLDING → GATE_HIDDEN
```

---

# 34. GATE_CONFIRMING

Adult-oriented confirmation screen.

Example:

```text
Parent area
[Continue]
[Cancel]
```

Transitions:

```text
GATE_CONFIRMING → GATE_ACCEPTED
GATE_CONFIRMING → GATE_CANCELLED
```

---

# 35. GATE_ACCEPTED

Effects:

- stop child-session audio;
- stop microphone;
- safely pause/end child session depending current state;
- enter parent mode.

Transition:

```text
GATE_ACCEPTED → PARENT_MODE
```

---

# 36. GATE_CANCELLED

Return to previous child state where safe.

No exposure or session event should be created merely because the gate opened.

---

# 37. Parent Mode States

Prototype parent experience may use:

```text
PARENT_HOME
RECENT_LEARNING
OBSERVATION_ENTRY
FAMILY_RECORDINGS
FAMILY_RECORDING_ACTIVE
SETTINGS
MIC_PERMISSION_EXPLANATION
PRIVACY
RESET_CONFIRMATION
```

---

# 38. PARENT_HOME

Shows:

- latest real-world suggestion;
- recent learning summary;
- quick observation actions;
- navigation to recordings/settings.

Valid events:

```text
OpenRecentLearning
OpenObservation
OpenFamilyRecordings
OpenSettings
ReturnToChild
```

---

# 39. OBSERVATION_ENTRY

Purpose:

- record parent-reported real-world recognition/use.

Valid events:

```text
RecognizedSelected
UsedSelected
Cancel
```

On save:

1. persist observation;
2. update adaptive evidence;
3. show small confirmation;
4. return to parent home or recent-learning context.

---

# 40. Observation Idempotency

Rapid double taps should not create accidental duplicate parent observations.

Possible safeguards:

- disable action immediately after save;
- assign unique observation ID;
- debounce repeated command.

---

# 41. FAMILY_RECORDINGS

Purpose:

- choose concept-language pair;
- play canonical audio;
- manage local family recording.

Valid events:

```text
PlayCanonical
StartRecording
PlayFamilyRecording
DeleteFamilyRecording
ReplaceFamilyRecording
Back
```

---

# 42. FAMILY_RECORDING_ACTIVE

Purpose:

- record one short parent/family voice clip.

Entry effect:

```text
start parent recording
```

This is distinct from child speaking-attempt detection.

Valid events:

```text
RecordingStopped
RecordingTimeout
RecordingFailed
Cancel
```

---

# 43. Family Recording Completion

On successful recording:

- save to app-private local storage;
- persist metadata;
- allow preview;
- do not alter canonical audio.

---

# 44. Family Recording Failure

On failure:

- show parent-facing actionable message;
- preserve canonical audio;
- no child-facing effect.

---

# 45. MIC_PERMISSION_EXPLANATION

This state appears before requesting Android microphone permission.

Events:

```text
EnableMicrophone
NotNow
```

If enable:

```text
MIC_PERMISSION_EXPLANATION → SYSTEM_PERMISSION_REQUEST
```

If not now:

```text
MIC_PERMISSION_EXPLANATION → SETTINGS or PARENT_HOME
```

---

# 46. System Permission Result

Possible results:

```text
GRANTED
DENIED
DENIED_DONT_ASK_AGAIN
```

Behavior:

### GRANTED

Enable attempt detection.

### DENIED

Keep feature off and explain that Miozira still works.

### DENIED_DONT_ASK_AGAIN

Parent may be directed to Android settings only if they explicitly want to enable it later.

No repeated nagging.

---

# 47. RESET_CONFIRMATION

Purpose:

- protect destructive reset.

State sequence:

```text
SETTINGS
  ↓
RESET_CONFIRMATION
  ↓
RESETTING
  ↓
RESET_COMPLETE
```

---

# 48. Reset Data Scope

A reset may remove:

- learning history;
- sessions;
- adaptive state;
- parent observations;
- family recordings;
- prototype settings according to reset policy.

Canonical bundled content remains.

---

# 49. Reset Failure

If reset is incomplete:

- parent must see an actionable error;
- child mode should not resume into inconsistent state;
- enter `RECOVERY` if necessary.

---

# 50. App Background State

When application enters background:

```text
APP_BACKGROUND
```

Required effects:

- stop child microphone;
- stop parent recording unless intentionally supported and safe;
- pause/stop spoken audio as appropriate;
- preserve current logical state;
- persist committed data.

---

# 51. Returning From Background

If interruption was short:

```text
APP_BACKGROUND → previous safe state
```

If interruption was long:

```text
APP_BACKGROUND → SESSION_ENDING → SESSION_ENDED
```

Exact long-interruption threshold may be implementation-defined.

---

# 52. Safe Resume State

Do not resume directly into an unsafe transient state if the OS may have destroyed resources.

Examples:

If background occurred during:

```text
LISTENING
```

resume as:

```text
RESPONDING
```

or a safe re-presented item state.

Do not silently restart microphone.

---

# 53. Process Death

After process death:

- persisted evidence remains;
- uncommitted transient interaction may be abandoned;
- active session may be marked interrupted;
- new launch starts in a valid state.

No attempt should be made to pretend exact transient audio/microphone state survived if it did not.

---

# 54. Orientation Change Events

Orientation changes should be treated as UI-layout events, not domain events.

Affected state:

```text
PRESENTING
READY
PLAYING_WORD
INVITING
LISTENING
RESPONDING
TRANSITIONING
```

The logical interaction ID remains unchanged.

---

# 55. Interaction ID

Each scheduled interaction should have a stable unique ID.

Example:

```text
interaction_id
```

This helps prevent duplicate:

- exposure writes;
- replay counts;
- attempt events

across lifecycle recreation.

---

# 56. Session ID

Each active session has:

```text
session_id
```

All scheduled interaction records reference that session.

---

# 57. Selection Reason

Each scheduled item should preserve its adaptive selection reason.

Examples:

```text
EASY_OPENER
DUE_EMERGING
DUE_FAMILIAR
NEW_INTRODUCTION
RECOVERY_ITEM
REINTRODUCTION_AFTER_REST
LANGUAGE_BALANCE
SESSION_CLOSER
```

This is useful for testing and debugging.

---

# 58. Error State Categories

Prototype 0.1 should distinguish:

```text
RECOVERABLE_CHILD_ERROR
RECOVERABLE_PARENT_ERROR
FATAL_LOCAL_STATE_ERROR
```

---

# 59. RECOVERABLE_CHILD_ERROR

Examples:

- optional family audio missing;
- microphone unavailable;
- one content asset fails but another valid item exists.

Response:

- fall back;
- skip safely;
- no technical child message.

---

# 60. RECOVERABLE_PARENT_ERROR

Examples:

- recording failed;
- deletion failed;
- permission denied;
- optional asset unavailable.

Response:

- clear parent-facing explanation;
- preserve working fallback.

---

# 61. FATAL_LOCAL_STATE_ERROR

Examples:

- database cannot open;
- required canonical content manifest invalid;
- reset partially corrupts state.

Response:

- do not continue into undefined child behavior;
- enter recovery flow;
- provide parent-facing guidance.

---

# 62. Recovery State

`RECOVERY` should:

1. stop audio/microphone;
2. avoid additional learning writes;
3. inspect local state;
4. attempt safe fallback where possible;
5. expose technical detail only in development diagnostics.

Prototype 0.1 should not silently destroy data as first recovery action.

---

# 63. Audio Service State

The audio subsystem may internally use:

```text
AUDIO_IDLE
AUDIO_LOADING
AUDIO_PLAYING
AUDIO_INTERRUPTED
AUDIO_ERROR
```

The child UI should not mirror these states one-to-one.

The interaction state remains authoritative.

---

# 64. Microphone Service State

The child attempt-detection microphone may use:

```text
MIC_DISABLED
MIC_READY
MIC_LISTENING
MIC_ATTEMPT_DETECTED
MIC_TIMEOUT
MIC_ERROR
```

Parent recording is a separate capability and should not reuse child attempt state ambiguously.

---

# 65. No Concurrent Child Mic and Parent Recording

The app must prevent simultaneous:

- child speech-attempt capture;
- family voice recording.

Entering parent recording should ensure child attempt detection is stopped.

---

# 66. Audio/Mic Mutual Coordination

During the target-word audio:

- child attempt detector should normally not listen for the child's response yet.

Recommended sequence:

```text
word playback
→ playback complete
→ invite
→ microphone window
```

This reduces false speech detection from the app's own speaker audio.

---

# 67. State Transition Table — Child Core Loop

| Current state | Event | Next state | Key side effect |
|---|---|---|---|
| SESSION_STARTING | item selected | PRESENTING | load concept |
| PRESENTING | settled | READY | enable tap |
| READY | concept tapped | PLAYING_WORD | start word audio |
| PLAYING_WORD | exposure threshold | PLAYING_WORD | commit exposure once |
| PLAYING_WORD | playback complete | INVITING | prepare imitation |
| INVITING | invite complete | LISTENING | start mic if enabled |
| INVITING | mic unavailable | RESPONDING | use neutral fallback |
| LISTENING | speech detected | RESPONDING | save attempt |
| LISTENING | timeout | RESPONDING | save no-attempt if measured |
| RESPONDING | completed | TRANSITIONING | finalize interaction |
| TRANSITIONING | next item ready | PRESENTING | update active pair |
| TRANSITIONING | end session | SESSION_ENDING | close session |
| SESSION_ENDING | finalized | SESSION_ENDED | suggestion may be stored |

---

# 68. State Transition Table — Replay

| Current state | Event | Result |
|---|---|---|
| READY | tap | scheduled word playback |
| PLAYING_WORD | extra tap | ignore/debounce or queue one replay |
| RESPONDING | replay tap | play word again |
| replay playback | complete | return to current interaction flow |

Replay must not increment scheduled interaction count.

---

# 69. State Transition Table — Orientation

| Current state | Orientation event behavior |
|---|---|
| PRESENTING | retain item |
| READY | retain item and tap readiness |
| PLAYING_WORD | do not restart audio automatically |
| INVITING | preserve state |
| LISTENING | preserve one logical attempt window |
| RESPONDING | preserve acknowledgement state |
| TRANSITIONING | preserve target transition |
| PARENT_MODE | reflow parent layout |

No learning event is created.

---

# 70. State Transition Table — Backgrounding

| Current state | Background behavior |
|---|---|
| READY | preserve logical session |
| PLAYING_WORD | stop/pause audio safely |
| INVITING | stop transient prompt |
| LISTENING | stop microphone immediately |
| RESPONDING | preserve committed result |
| TRANSITIONING | persist completed interaction |
| FAMILY_RECORDING_ACTIVE | stop/cancel safely |
| PARENT_HOME | preserve UI context |

---

# 71. State Transition Table — Microphone

| Mic state | Child behavior |
|---|---|
| permission granted | listening cue may appear |
| permission denied | no listening cue, session continues |
| mic unavailable | neutral fallback |
| speech detected | warm acknowledgement |
| no speech detected | neutral response |
| mic error | no child error message |

---

# 72. Invalid Transitions

Examples that should be prevented:

```text
READY → LISTENING
```

without word playback/invitation.

```text
LISTENING → PLAYING_WORD
```

unless explicitly replaying after mic closure.

```text
SESSION_ENDED → RESPONDING
```

from delayed callbacks.

```text
PARENT_MODE → child microphone listening
```

without starting/resuming child session.

---

# 73. Late Callback Protection

Async audio/mic callbacks may arrive after state has changed.

Every callback should verify:

- current interaction ID;
- current session ID;
- expected state.

If stale, ignore it.

This prevents delayed callbacks from corrupting the current item.

---

# 74. Example: Stale Audio Callback

Scenario:

1. apple audio begins;
2. parent exits session;
3. delayed `playbackComplete` callback arrives.

Correct behavior:

- callback sees session/interaction no longer active;
- callback is ignored.

It must not move UI into `INVITING`.

---

# 75. Example: Rotation During Playback

Scenario:

1. ball is active;
2. child taps;
3. word begins;
4. tablet rotates;
5. UI recreates.

Correct behavior:

- same interaction ID retained;
- audio does not duplicate;
- exposure is committed once;
- state continues.

---

# 76. Example: Mic Permission Denied

Scenario:

1. word plays;
2. imitation phase begins;
3. microphone is unavailable.

Correct behavior:

```text
PLAYING_WORD
→ INVITING
→ RESPONDING
→ TRANSITIONING
```

No dead end.

---

# 77. Example: Child Stops During Listening

Scenario:

1. LISTENING active;
2. child/parent ends session.

Correct behavior:

- stop mic immediately;
- record only valid evidence already obtained;
- do not record `NO_ATTEMPT_DETECTED` merely because session was manually stopped unless the design explicitly chooses that meaning;
- transition to `SESSION_ENDING`.

---

# 78. Example: Family Recording Starts

Scenario:

1. parent enters recording screen;
2. taps Record.

Correct behavior:

- ensure child mic is stopped;
- acquire microphone for parent recording;
- record to local temporary file;
- on save, promote to family recording;
- on cancel, delete temporary capture.

---

# 79. Temporary Files

Incomplete family recordings should use temporary storage.

On:

- cancel;
- failure;
- app restart;

orphaned temporary recordings should be safely removed where possible.

---

# 80. Persistence Boundaries

Persist immediately or near-immediately:

- valid exposure;
- replay event where captured;
- attempt state;
- real-world observation;
- family recording metadata;
- settings change.

Session summary may be finalized at session end.

---

# 81. Do Not Persist Transient UI State as Learning Evidence

Examples of transient state that should not itself become learning data:

- animation progress;
- listening-ring size;
- parent-gate hold progress;
- orientation;
- visual fade state.

---

# 82. Child Interaction Result State

Recommended conceptual model:

```text
InteractionResult
  interactionId
  sessionId
  conceptId
  languageId
  selectionReason
  exposureCommitted
  replayCount
  attemptState
  startedAt
  completedAt
  completionReason
```

---

# 83. Completion Reasons

Suggested:

```text
NORMAL
EARLY_SESSION_END
APP_BACKGROUND
PROCESS_INTERRUPTION
CONTENT_ERROR
```

Completion reason is operational.

It must not be interpreted as child success/failure.

---

# 84. Session Completion Reasons

Suggested:

```text
PLANNED
CHILD_INACTIVE
PARENT_ENDED
APP_EXITED
LONG_INTERRUPTION
ERROR_RECOVERY
```

Again, none imply performance.

---

# 85. State Invariants

### IS-INV-001
Only one scheduled child interaction is active at a time.

### IS-INV-002
Only one child microphone attempt window is active at a time.

### IS-INV-003
Only one canonical word clip plays at a time.

### IS-INV-004
Exposure is committed at most once per scheduled interaction.

### IS-INV-005
Rotation does not create domain events.

### IS-INV-006
Replay does not create a new scheduled interaction.

### IS-INV-007
A stale callback cannot advance a newer interaction.

### IS-INV-008
Microphone denial cannot dead-end the child flow.

### IS-INV-009
Parent recording and child attempt detection cannot run concurrently.

### IS-INV-010
Session ending closes active microphone resources.

### IS-INV-011
Planned-but-unshown items are not exposures.

### IS-INV-012
Technical errors are never shown raw in child mode.

### IS-INV-013
Entering parent mode stops child-only transient effects.

### IS-INV-014
No state transition may create a pronunciation score.

---

# 86. Recommended Implementation Pattern

A future architecture may use:

```text
State + Event → Reducer / State Machine → New State + Effects
```

Example:

```text
READY + ConceptTapped
    →
PLAYING_WORD
    +
PlayCanonicalWord
```

Then:

```text
PLAYING_WORD + PlaybackCompleted
    →
INVITING
    +
StartInvite
```

The exact technology is deferred to `architecture.md`.

---

# 87. Why This Matters for Compose

If Compose Multiplatform or Android Compose is selected, recomposition may happen frequently.

Recomposition must never be treated as:

- item presentation;
- exposure;
- replay;
- attempt.

Domain events must be triggered explicitly.

---

# 88. Test Requirements

State-machine tests must cover at minimum:

1. normal child loop;
2. replay;
3. no microphone;
4. microphone permission denied;
5. mic failure;
6. no speech;
7. speech detected;
8. orientation during every child state;
9. background during playback;
10. background during listening;
11. process death;
12. parent gate during child session;
13. stale audio callback;
14. stale mic callback;
15. double tap;
16. rapid replay;
17. early session end;
18. planned end;
19. family recording start/cancel/save;
20. reset success/failure;
21. missing optional audio;
22. fatal local-state recovery.

---

# 89. Deterministic Test Events

Tests should be able to inject:

```text
FakeClock
FakeAudioService
FakeMicrophoneService
FakeAdaptiveEngine
FakeRepository
```

This allows the state machine to be tested without real waiting or actual device audio.

---

# 90. Relationship to `architecture.md`

`architecture.md` must decide:

- where state machines live;
- how state is preserved;
- how effects are executed;
- how stale callbacks are rejected;
- how child and parent flows are separated.

---

# 91. Relationship to `api.md`

`api.md` should define internal service contracts used by states.

Likely contracts include:

```text
LearningEngine
AdaptiveEngine
AudioService
MicrophoneService
ProgressRepository
FamilyRecordingService
```

---

# 92. Relationship to `database.md`

`database.md` should persist domain evidence, not arbitrary UI-state details.

It should support:

- interaction IDs;
- session IDs;
- exposure evidence;
- replay;
- attempts;
- observations;
- adaptive state.

---

# 93. Relationship to `testing.md`

`testing.md` should turn every important transition and invariant into:

- unit tests;
- integration tests;
- lifecycle tests;
- manual family-test checks.

---

# 94. Decision Summary

Prototype 0.1 interaction logic uses explicit state machines for:

- child session;
- parent gate;
- parent mode;
- microphone permission;
- family recording;
- app lifecycle;
- recovery.

The core child state sequence is:

```text
SESSION_STARTING
→ PRESENTING
→ READY
→ PLAYING_WORD
→ INVITING
→ LISTENING
→ RESPONDING
→ TRANSITIONING
→ PRESENTING
```

with optional fallback paths that never block learning.

The most important implementation protections are:

- exposure committed once;
- replay separated from scheduled exposure;
- no duplicate events on rotation;
- no background microphone;
- stale callbacks ignored;
- microphone failure never dead-ends the child;
- parent recording separated from child attempt detection;
- technical lifecycle events never become learning evidence.
