# Miozira — Internal API / Service Contracts

**Document:** `api.md`  
**Version:** 0.1  
**Status:** Draft for Prototype 0.1  
**Product:** Miozira  
**Architecture:** Local-first, no external backend API  
**Primary stack:** KMP + Compose Multiplatform + Room/SQLite  
**Last updated:** 2026-08-25

---

## 1. Purpose

This document defines the internal application contracts for Miozira Prototype 0.1.

Despite the filename `api.md`, Prototype 0.1 has:

> **no external HTTP/network API**

This document instead defines the interfaces between:

- presentation/UI;
- child session orchestration;
- adaptive engine;
- repositories;
- content access;
- audio playback;
- child speech-attempt detection;
- parent family recording;
- settings;
- permissions;
- file storage;
- reset/recovery.

The goal is to make component boundaries explicit before implementation.

---

# 2. API Philosophy

Internal contracts should be:

- small;
- domain-oriented;
- platform-neutral where possible;
- explicit about errors;
- easy to fake in tests;
- independent of UI framework;
- independent of database implementation;
- safe for lifecycle interruption.

Avoid generic service abstractions with vague methods such as:

```text
doThing()
processData()
handleRequest()
```

Prefer semantic operations.

---

# 3. External API Policy

Prototype 0.1 must not require:

- REST API;
- GraphQL;
- WebSocket;
- cloud database;
- authentication endpoint;
- remote analytics endpoint;
- remote speech API.

The runtime child learning loop must work fully offline.

---

# 4. Contract Categories

Recommended internal contracts:

```text
LearningSessionEngine
AdaptiveEngine
LearningRepository
SessionRepository
ContentRepository
RealWorldObservationRepository
FamilyRecordingRepository
SettingsRepository

AudioService
SpeechAttemptDetector
FamilyRecordingService
PermissionService
FileStore
Clock
Randomizer
```

---

# 5. Result Model

Internal APIs should avoid throwing raw platform exceptions through multiple layers.

Recommended generic result shape:

```kotlin
sealed interface AppResult<out T> {
    data class Success<T>(val value: T) : AppResult<T>
    data class Failure(val error: AppError) : AppResult<Nothing>
}
```

The exact Kotlin syntax may vary.

---

# 6. Error Model

Recommended root error categories:

```text
ContentError
AudioError
MicrophoneError
PermissionError
PersistenceError
FileError
ValidationError
RecoveryError
UnexpectedError
```

Errors should carry semantic meaning.

Avoid exposing raw exception strings to child UI.

---

# 7. Identifiers

Core IDs should be strongly typed where practical:

```text
ConceptId
LanguageId
PairId
SessionId
InteractionId
ObservationId
RecordingId
SuggestionId
```

This reduces accidental argument mix-ups.

---

# 8. Core Domain Models

Representative models:

```kotlin
data class Concept(
    val id: ConceptId,
    val category: ConceptCategory,
    val visualAssetId: AssetId
)

data class ConceptLanguagePair(
    val id: PairId,
    val conceptId: ConceptId,
    val languageId: LanguageId,
    val spokenForm: String,
    val canonicalAudioAssetId: AssetId
)
```

These should remain independent from Room entity annotations where practical.

---

# 9. LearningSessionEngine

Purpose:

> own the semantic lifecycle of one child learning session.

Conceptual interface:

```kotlin
interface LearningSessionEngine {
    suspend fun startSession(
        request: StartSessionRequest
    ): AppResult<SessionSnapshot>

    suspend fun beginInteraction(
        sessionId: SessionId
    ): AppResult<InteractionPlan>

    suspend fun completeInteraction(
        result: InteractionResult
    ): AppResult<InteractionCompletion>

    suspend fun endSession(
        sessionId: SessionId,
        reason: SessionCompletionReason
    ): AppResult<SessionSummary>
}
```

---

# 10. StartSessionRequest

Recommended fields:

```kotlin
data class StartSessionRequest(
    val preferredStartingLanguage: LanguageId?,
    val contentVersion: String,
    val adaptiveConfigVersion: String
)
```

The engine should not receive UI objects or platform context.

---

# 11. SessionSnapshot

Representative:

```kotlin
data class SessionSnapshot(
    val sessionId: SessionId,
    val startedAt: Instant,
    val startingLanguage: LanguageId?,
    val interactionCount: Int
)
```

---

# 12. InteractionPlan

Represents the next scheduled item.

Recommended:

```kotlin
data class InteractionPlan(
    val interactionId: InteractionId,
    val sessionId: SessionId,
    val pair: ConceptLanguagePair,
    val sequenceIndex: Int,
    val selectionReason: SelectionReason,
    val allowSpeakingInvite: Boolean
)
```

---

# 13. InteractionResult

Passed back after one scheduled interaction.

Recommended:

```kotlin
data class InteractionResult(
    val interactionId: InteractionId,
    val sessionId: SessionId,
    val pairId: PairId,
    val exposureCommitted: Boolean,
    val replayCount: Int,
    val attemptState: AttemptState,
    val completionReason: InteractionCompletionReason,
    val completedAt: Instant
)
```

---

# 14. InteractionCompletion

Representative:

```kotlin
data class InteractionCompletion(
    val shouldContinue: Boolean,
    val recommendedNextAction: SessionNextAction
)
```

Potential next actions:

```text
CONTINUE
END_PLANNED
END_INACTIVE
```

Presentation should not infer adaptive behavior independently.

---

# 15. AdaptiveEngine

Purpose:

> choose and update concept-language scheduling using explicit local rules.

Conceptual interface:

```kotlin
interface AdaptiveEngine {
    suspend fun selectNext(
        context: AdaptiveSelectionContext
    ): AppResult<SelectionDecision>

    suspend fun recalculatePairState(
        pairId: PairId
    ): AppResult<PairLearningState>

    suspend fun rebuildAllStates(): AppResult<Unit>
}
```

---

# 16. AdaptiveSelectionContext

Recommended fields:

```kotlin
data class AdaptiveSelectionContext(
    val sessionId: SessionId,
    val recentInteractions: List<InteractionSummary>,
    val allPairStates: List<PairLearningState>,
    val activeLanguageBlock: LanguageId?,
    val now: Instant
)
```

The engine should not query the database directly if the architecture keeps data retrieval outside it.

Alternative:

- adaptive use case may compose repository + engine.

Either is acceptable if dependency direction remains clear.

---

# 17. SelectionDecision

Recommended:

```kotlin
data class SelectionDecision(
    val pairId: PairId,
    val reason: SelectionReason
)
```

No UI wording belongs here.

---

# 18. SelectionReason

Persistent semantic values:

```text
DUE_EMERGING
DUE_FAMILIAR
NEW_INTRODUCTION
EASY_OPENER
RECOVERY_ITEM
SESSION_CLOSER
LANGUAGE_BALANCE
REINTRODUCTION_AFTER_REST
INSUFFICIENT_ALTERNATIVES
```

---

# 19. PairLearningState

Representative:

```kotlin
data class PairLearningState(
    val pairId: PairId,
    val state: LearningState,
    val nextDueAt: Instant?,
    val restUntil: Instant?,
    val validExposureCount: Int,
    val attemptDetectedCount: Int,
    val replayCountTotal: Int,
    val realWorldRecognitionCount: Int,
    val realWorldUseCount: Int,
    val lastExposedAt: Instant?,
    val updatedAt: Instant
)
```

---

# 20. LearningRepository

Purpose:

> persist/query learning evidence and derived pair state.

Conceptual interface:

```kotlin
interface LearningRepository {
    suspend fun getPairState(
        pairId: PairId
    ): AppResult<PairLearningState>

    suspend fun getAllPairStates():
        AppResult<List<PairLearningState>>

    suspend fun commitExposure(
        interactionId: InteractionId,
        committedAt: Instant
    ): AppResult<ExposureCommitResult>

    suspend fun incrementReplay(
        interactionId: InteractionId
    ): AppResult<Int>

    suspend fun recordAttempt(
        interactionId: InteractionId,
        attemptState: AttemptState,
        recordedAt: Instant
    ): AppResult<Unit>

    suspend fun savePairState(
        state: PairLearningState
    ): AppResult<Unit>
}
```

---

# 21. ExposureCommitResult

Recommended:

```text
COMMITTED
ALREADY_COMMITTED
INTERACTION_NOT_FOUND
```

The repository should make idempotency visible.

---

# 22. AttemptState

Stable values:

```text
ATTEMPT_DETECTED
NO_ATTEMPT_DETECTED
MICROPHONE_UNAVAILABLE
NOT_MEASURED
```

---

# 23. SessionRepository

Purpose:

> persist session and scheduled interaction lifecycle.

Conceptual interface:

```kotlin
interface SessionRepository {
    suspend fun createSession(
        draft: SessionDraft
    ): AppResult<SessionRecord>

    suspend fun createInteraction(
        draft: InteractionDraft
    ): AppResult<InteractionRecord>

    suspend fun completeInteraction(
        interactionId: InteractionId,
        result: InteractionPersistenceResult
    ): AppResult<Unit>

    suspend fun completeSession(
        sessionId: SessionId,
        reason: SessionCompletionReason,
        endedAt: Instant
    ): AppResult<Unit>

    suspend fun getRecentSessions(
        limit: Int
    ): AppResult<List<SessionSummary>>

    suspend fun recoverInterruptedSessions():
        AppResult<RecoverySummary>
}
```

---

# 24. SessionDraft

Representative:

```kotlin
data class SessionDraft(
    val sessionId: SessionId,
    val startedAt: Instant,
    val startingLanguageId: LanguageId?,
    val contentVersion: String,
    val adaptiveConfigVersion: String
)
```

---

# 25. InteractionDraft

Representative:

```kotlin
data class InteractionDraft(
    val interactionId: InteractionId,
    val sessionId: SessionId,
    val pairId: PairId,
    val sequenceIndex: Int,
    val selectionReason: SelectionReason,
    val startedAt: Instant
)
```

---

# 26. ContentRepository

Purpose:

> expose bundled canonical content through stable logical IDs.

Conceptual interface:

```kotlin
interface ContentRepository {
    suspend fun getConcept(
        conceptId: ConceptId
    ): AppResult<Concept>

    suspend fun getAllConcepts():
        AppResult<List<Concept>>

    suspend fun getPair(
        pairId: PairId
    ): AppResult<ConceptLanguagePair>

    suspend fun getAllPairs():
        AppResult<List<ConceptLanguagePair>>

    suspend fun getPrimaryVisual(
        conceptId: ConceptId
    ): AppResult<VisualAssetRef>

    suspend fun getCanonicalAudio(
        pairId: PairId
    ): AppResult<AudioAssetRef>

    suspend fun getRealWorldSuggestions(
        pairId: PairId
    ): AppResult<List<RealWorldSuggestion>>
}
```

---

# 27. Content Validation Contract

Recommended:

```kotlin
interface ContentValidator {
    suspend fun validatePrototypeContent():
        AppResult<ContentValidationReport>
}
```

Checks:

- 8 concepts;
- 2 languages;
- 16 active pairs;
- no missing canonical audio;
- no missing primary visuals;
- review status valid;
- duplicate IDs absent.

---

# 28. RealWorldObservationRepository

Purpose:

> save/query parent-reported transfer evidence.

Conceptual interface:

```kotlin
interface RealWorldObservationRepository {
    suspend fun record(
        observation: RealWorldObservationDraft
    ): AppResult<RealWorldObservation>

    suspend fun getRecent(
        limit: Int
    ): AppResult<List<RealWorldObservation>>

    suspend fun getForPair(
        pairId: PairId
    ): AppResult<List<RealWorldObservation>>
}
```

---

# 29. RealWorldObservationDraft

Recommended:

```kotlin
data class RealWorldObservationDraft(
    val observationId: ObservationId,
    val pairId: PairId,
    val type: RealWorldObservationType,
    val recordedAt: Instant,
    val source: ObservationSource = PARENT_REPORT
)
```

---

# 30. RealWorldObservationType

Prototype values:

```text
RECOGNIZED_OUTSIDE_APP
USED_OUTSIDE_APP
```

---

# 31. RealWorldTransferService

Purpose:

> select a lightweight real-world suggestion.

Conceptual interface:

```kotlin
interface RealWorldTransferService {
    suspend fun chooseSuggestion(
        context: SuggestionContext
    ): AppResult<RealWorldSuggestion?>
}
```

---

# 32. SuggestionContext

Potential fields:

```kotlin
data class SuggestionContext(
    val sessionId: SessionId,
    val recentlyExposedPairs: List<PairId>,
    val pairStates: List<PairLearningState>,
    val previousSuggestionIds: List<SuggestionId>
)
```

---

# 33. FamilyRecordingRepository

Purpose:

> persist family recording metadata.

Conceptual interface:

```kotlin
interface FamilyRecordingRepository {
    suspend fun get(
        pairId: PairId
    ): AppResult<FamilyRecordingMetadata?>

    suspend fun save(
        metadata: FamilyRecordingMetadata
    ): AppResult<Unit>

    suspend fun delete(
        pairId: PairId
    ): AppResult<Unit>

    suspend fun getAll():
        AppResult<List<FamilyRecordingMetadata>>
}
```

---

# 34. FamilyRecordingService

Purpose:

> capture/play parent family voice.

Conceptual interface:

```kotlin
interface FamilyRecordingService {
    suspend fun start(
        pairId: PairId
    ): AppResult<RecordingSession>

    suspend fun stop(
        session: RecordingSession
    ): AppResult<RecordingPreview>

    suspend fun cancel(
        session: RecordingSession
    ): AppResult<Unit>

    suspend fun save(
        preview: RecordingPreview
    ): AppResult<FamilyRecordingMetadata>

    suspend fun play(
        recordingId: RecordingId
    ): AppResult<PlaybackHandle>

    suspend fun delete(
        recordingId: RecordingId
    ): AppResult<Unit>
}
```

---

# 35. RecordingSession

A temporary in-progress parent recording.

It should not expose a public filesystem path.

Example:

```kotlin
data class RecordingSession(
    val token: String,
    val pairId: PairId
)
```

---

# 36. RecordingPreview

Represents a temporary captured file before final save.

```kotlin
data class RecordingPreview(
    val token: String,
    val pairId: PairId,
    val durationMs: Long
)
```

---

# 37. AudioService

Purpose:

> play canonical words, prompts, and optionally family recordings.

Conceptual interface:

```kotlin
interface AudioService {
    suspend fun playCanonicalWord(
        request: WordPlaybackRequest,
        listener: AudioPlaybackListener
    ): AppResult<PlaybackHandle>

    suspend fun playPrompt(
        request: PromptPlaybackRequest,
        listener: AudioPlaybackListener
    ): AppResult<PlaybackHandle>

    suspend fun playFamilyRecording(
        recordingId: RecordingId,
        listener: AudioPlaybackListener
    ): AppResult<PlaybackHandle>

    suspend fun stop(
        handle: PlaybackHandle
    ): AppResult<Unit>

    suspend fun stopAll(): AppResult<Unit>
}
```

---

# 38. WordPlaybackRequest

Recommended:

```kotlin
data class WordPlaybackRequest(
    val sessionId: SessionId,
    val interactionId: InteractionId,
    val pairId: PairId,
    val assetRef: AudioAssetRef
)
```

Including session/interaction identity helps stale callback protection.

---

# 39. AudioPlaybackListener

Conceptual callbacks:

```kotlin
interface AudioPlaybackListener {
    fun onStarted(handle: PlaybackHandle)
    fun onExposureThresholdReached(handle: PlaybackHandle)
    fun onCompleted(handle: PlaybackHandle)
    fun onFailed(handle: PlaybackHandle?, error: AudioError)
}
```

Callbacks must be tied to the playback request identity.

---

# 40. Exposure Threshold Contract

`AudioService` should signal:

```text
onExposureThresholdReached
```

exactly once per playback attempt.

The child/session controller decides whether that threshold should commit the scheduled exposure.

---

# 41. PlaybackHandle

Opaque token:

```kotlin
data class PlaybackHandle(
    val id: String
)
```

Do not expose `MediaPlayer` or platform-native player objects through the shared API.

---

# 42. SpeechAttemptDetector

Purpose:

> detect whether speech-like activity occurred during a short child response window.

Conceptual interface:

```kotlin
interface SpeechAttemptDetector {
    suspend fun start(
        request: SpeechAttemptRequest,
        listener: SpeechAttemptListener
    ): AppResult<SpeechAttemptHandle>

    suspend fun stop(
        handle: SpeechAttemptHandle
    ): AppResult<Unit>

    suspend fun stopAll(): AppResult<Unit>
}
```

---

# 43. SpeechAttemptRequest

Recommended:

```kotlin
data class SpeechAttemptRequest(
    val sessionId: SessionId,
    val interactionId: InteractionId,
    val durationMs: Long
)
```

No expected word/transcript is required.

---

# 44. SpeechAttemptListener

Conceptual callbacks:

```kotlin
interface SpeechAttemptListener {
    fun onAttemptDetected(handle: SpeechAttemptHandle)
    fun onTimeout(handle: SpeechAttemptHandle)
    fun onUnavailable(error: MicrophoneError)
    fun onError(error: MicrophoneError)
}
```

No pronunciation or transcription callback exists.

---

# 45. SpeechAttemptHandle

Opaque identifier:

```kotlin
data class SpeechAttemptHandle(
    val id: String
)
```

---

# 46. Privacy Contract for SpeechAttemptDetector

The shared contract shall not expose:

- PCM buffers;
- file path;
- transcript;
- phoneme sequence;
- pronunciation score.

The detector returns only semantic attempt state.

---

# 47. PermissionService

Conceptual interface:

```kotlin
interface PermissionService {
    suspend fun getMicrophonePermissionState():
        AppResult<MicrophonePermissionState>

    suspend fun requestMicrophonePermission():
        AppResult<MicrophonePermissionState>

    suspend fun openMicrophoneSettings():
        AppResult<Unit>
}
```

---

# 48. MicrophonePermissionState

Recommended:

```text
GRANTED
DENIED
DENIED_DONT_ASK_AGAIN
NOT_DETERMINED
UNAVAILABLE
```

---

# 49. SettingsRepository

Conceptual interface:

```kotlin
interface SettingsRepository {
    suspend fun isSetupComplete(): AppResult<Boolean>
    suspend fun setSetupComplete(value: Boolean): AppResult<Unit>

    suspend fun getStartingLanguage(): AppResult<LanguageId?>
    suspend fun setStartingLanguage(languageId: LanguageId?): AppResult<Unit>

    suspend fun isSpeechAttemptDetectionEnabled(): AppResult<Boolean>
    suspend fun setSpeechAttemptDetectionEnabled(value: Boolean): AppResult<Unit>
}
```

---

# 50. FileStore

Purpose:

> own app-private logical file locations.

Conceptual interface:

```kotlin
interface FileStore {
    suspend fun createTemporaryFamilyRecording():
        AppResult<TemporaryFileRef>

    suspend fun promoteTemporaryFile(
        temp: TemporaryFileRef,
        targetId: String
    ): AppResult<PrivateFileRef>

    suspend fun delete(
        file: PrivateFileRef
    ): AppResult<Unit>

    suspend fun exists(
        file: PrivateFileRef
    ): AppResult<Boolean>
}
```

---

# 51. No Raw Paths Above FileStore

Higher layers should use:

```text
PrivateFileRef
```

rather than:

```text
/data/user/0/...
```

This keeps storage platform-neutral.

---

# 52. Clock

Conceptual interface:

```kotlin
interface Clock {
    fun now(): Instant
}
```

Production implementation uses platform/system time.

Tests use a fake clock.

---

# 53. Randomizer

Conceptual interface:

```kotlin
interface Randomizer {
    fun nextInt(bound: Int): Int
}
```

Only use randomness for controlled tie-breaking where needed.

Tests should inject deterministic behavior.

---

# 54. ResetService

Purpose:

> coordinate full local user-data reset.

Conceptual interface:

```kotlin
interface ResetService {
    suspend fun resetUserData():
        AppResult<ResetSummary>
}
```

---

# 55. ResetSummary

Potential:

```kotlin
data class ResetSummary(
    val sessionsDeleted: Int,
    val observationsDeleted: Int,
    val familyRecordingsDeleted: Int,
    val settingsReset: Boolean
)
```

Child UI never needs this detail.

Parent UI may simply show:

> “Miozira data was reset.”

---

# 56. Reset Transaction Expectations

`ResetService` should coordinate:

- database structured-data reset;
- family recording file deletion;
- settings reset.

If partial failure occurs:

- return explicit failure/recovery state;
- do not silently claim success.

---

# 57. RecoveryService

Recommended:

```kotlin
interface RecoveryService {
    suspend fun recoverOnLaunch():
        AppResult<RecoveryReport>
}
```

Responsibilities may include:

- mark interrupted sessions;
- clean orphan temporary files;
- validate pair-learning state;
- seed canonical content if needed.

---

# 58. RecoveryReport

Potential fields:

```text
interruptedSessionsRecovered
temporaryFilesDeleted
contentValidated
pairStatesRebuilt
```

No parent UI is required unless recovery fails materially.

---

# 59. Content Seed Service

If using seeded content tables:

```kotlin
interface ContentSeedService {
    suspend fun ensureSeeded(
        contentVersion: String
    ): AppResult<ContentSeedReport>
}
```

Must be idempotent.

---

# 60. Controller Layer

The UI should normally interact with:

```text
ChildSessionController
ParentController
SetupController
```

rather than directly calling many repositories.

Controllers compose domain services.

---

# 61. ChildSessionController API

Conceptual:

```kotlin
interface ChildSessionController {
    val state: StateFlow<ChildSessionUiState>

    suspend fun start()
    suspend fun onConceptTapped()
    suspend fun onReplayRequested()
    suspend fun onSessionStopRequested()
    suspend fun onParentGateAccepted()
    suspend fun onAppBackgrounded()
    suspend fun onAppForegrounded()
}
```

Exact `StateFlow` use assumes Kotlin stack; conceptually the UI observes state.

---

# 62. ParentController API

Conceptual:

```kotlin
interface ParentController {
    val state: StateFlow<ParentUiState>

    suspend fun refresh()
    suspend fun recordRecognition(pairId: PairId)
    suspend fun recordUse(pairId: PairId)
    suspend fun deleteFamilyRecording(pairId: PairId)
    suspend fun resetUserData()
}
```

Family recording UI may use a dedicated controller.

---

# 63. FamilyRecordingController

Conceptual:

```kotlin
interface FamilyRecordingController {
    val state: StateFlow<FamilyRecordingUiState>

    suspend fun startRecording(pairId: PairId)
    suspend fun stopRecording()
    suspend fun playPreview()
    suspend fun save()
    suspend fun cancel()
    suspend fun delete(pairId: PairId)
}
```

---

# 64. Service Lifetime Rules

Recommended lifetimes:

```text
Database                  application lifetime
Repositories              application lifetime
AdaptiveEngine            application lifetime
ContentRepository         application lifetime
AudioService              application/session-capable
SpeechAttemptDetector     application/service lifetime, windows short-lived
ChildSessionController    child session / screen scope
ParentController          parent area scope
Recording session         short-lived
```

---

# 65. Cancellation

Any async operation tied to a child interaction should be cancellable.

Examples:

- audio playback;
- listening window;
- transition delay.

Cancellation must not generate false evidence.

---

# 66. Idempotency Rules

Internal write APIs should explicitly support idempotency where duplicate invocation is possible.

Required:

```text
commitExposure(interactionId)
completeInteraction(interactionId)
completeSession(sessionId)
save observation after debounced UI action
```

---

# 67. Stale Callback Rule

Audio/mic callbacks must verify:

```text
sessionId
interactionId
expected state
```

before changing state.

If stale:

> ignore.

---

# 68. Error Translation

Low-level errors should be translated at boundaries.

Example:

```text
SQLiteConstraintException
  ↓
PersistenceError.ConstraintViolation
```

Child controller may decide:

```text
safe recovery / end session
```

Parent UI gets plain-language error.

---

# 69. Child Error Contract

Child-facing controller state should expose only semantic safe states.

Avoid:

```text
errorMessage = "java.io.FileNotFoundException"
```

Prefer:

```text
recovering = true
```

or transition to next valid content item.

---

# 70. Parent Error Contract

Parent UI may receive:

```text
ParentUiError(
    messageKey,
    recoveryAction
)
```

Example semantic keys:

```text
family_recording_failed
microphone_unavailable
reset_failed
canonical_audio_unavailable
```

---

# 71. No Stringly-Typed Domain Commands

Avoid passing arbitrary command strings such as:

```text
"PLAY_AUDIO"
"RECORD_ATTEMPT"
```

Prefer typed methods/events.

---

# 72. No Generic Key-Value Domain API

Avoid:

```text
save(key, value)
get(key)
```

for learning evidence.

Use domain-specific repository methods.

---

# 73. Thread / Dispatcher Policy

Public repository/service methods should be safe for callers.

The implementation owns:

- database dispatcher;
- file I/O dispatcher;
- audio callback threading;
- microphone processing thread.

Presentation should not manually decide thread pools for each operation.

---

# 74. Audio and Microphone Coordination Contract

The session controller must enforce:

```text
word playback
→ playback completes
→ invitation
→ listening starts
```

The microphone should not normally listen while canonical word audio is playing.

---

# 75. Parent Recorder and Child Detector Coordination

Before family recording starts:

```text
SpeechAttemptDetector.stopAll()
AudioService.stopAll()   # if needed
```

No simultaneous child-attempt listening and parent recording.

---

# 76. Offline Contract

All internal APIs used by the child session must succeed or fail based only on:

- local content;
- local data;
- local device capabilities.

No method should return:

```text
NETWORK_UNAVAILABLE
```

for the normal child learning flow because the flow does not depend on network.

---

# 77. API Versioning

Internal Kotlin interfaces do not need HTTP-style versioning.

But persisted semantic contracts should be versioned where necessary:

```text
contentVersion
adaptiveConfigVersion
databaseSchemaVersion
stateVersion
```

---

# 78. Testing Contracts

Every major interface must have a fake/test implementation.

Required fakes:

```text
FakeLearningRepository
FakeSessionRepository
FakeContentRepository
FakeAudioService
FakeSpeechAttemptDetector
FakePermissionService
FakeClock
FakeRandomizer
FakeFileStore
```

---

# 79. Fake AudioService

Should support deterministic events:

```text
emitStarted()
emitExposureThreshold()
emitCompleted()
emitFailure()
```

This allows state-machine tests without real audio.

---

# 80. Fake SpeechAttemptDetector

Should support:

```text
emitAttemptDetected()
emitTimeout()
emitUnavailable()
emitError()
```

No real waiting.

---

# 81. Fake Clock

Allows tests such as:

```text
now = Day 1
advance by 1 day
assert EMERGING pair becomes due
```

---

# 82. Contract Test — Exposure Idempotency

Test:

```text
commitExposure(interactionId)
commitExposure(interactionId)
```

Expected:

```text
COMMITTED
ALREADY_COMMITTED
```

with one exposure count increment.

---

# 83. Contract Test — Microphone Disabled

Given:

```text
speech detection disabled
```

child session must still proceed through:

```text
PLAYING_WORD
→ RESPONDING
→ TRANSITIONING
```

without calling the detector.

---

# 84. Contract Test — Stale Audio Callback

Given:

```text
interaction A replaced by interaction B
```

then callback from A arrives.

Expected:

```text
ignored
```

No state mutation for B.

---

# 85. Contract Test — Real-World Observation

Given:

```text
pair.apple.ta
USED_OUTSIDE_APP
```

Expected:

- observation persisted;
- Tamil pair state recalculated;
- English pair unchanged.

---

# 86. Contract Test — Family Recording

Given:

```text
start
stop
save
```

Expected:

- temporary file promoted;
- metadata saved;
- canonical audio untouched.

---

# 87. Contract Test — Reset

Expected:

- sessions removed;
- interactions removed;
- observations removed;
- family recordings removed;
- settings reset as defined;
- canonical content remains.

---

# 88. API Security Rules

Internal contracts must not expose:

- network tokens;
- API keys;
- child raw audio;
- hidden filesystem paths;
- SQL statements to UI;
- platform permission objects to domain.

---

# 89. API Privacy Rules

No method should create or return:

```text
speechTranscript
pronunciationScore
childVoiceFile
cloudUploadResult
analyticsUserId
```

in Prototype 0.1.

---

# 90. API Observability

Development logging may record semantic events such as:

```text
SessionStarted
InteractionSelected(pair.apple.ta, DUE_EMERGING)
ExposureCommitted
AttemptDetected
InteractionCompleted
```

Never log raw PCM/audio.

---

# 91. API Acceptance Criteria

Before implementation is considered contract-complete:

- [ ] every UI write path goes through a controller/use case;
- [ ] repositories hide DAOs;
- [ ] domain logic does not depend on Android classes;
- [ ] audio is behind `AudioService`;
- [ ] speech attempt detection exposes no raw audio;
- [ ] family recording is separate from child detector;
- [ ] permission requests are platform-service concerns;
- [ ] file paths are hidden behind `FileStore`;
- [ ] exposure write is idempotent;
- [ ] stale callbacks are rejectable;
- [ ] all critical services have fakes;
- [ ] child loop has no network API dependency.

---

# 92. API Invariants

### API-INV-001
Prototype 0.1 has no external runtime network API.

### API-INV-002
UI never calls Room DAO directly.

### API-INV-003
Domain does not receive platform-native audio objects.

### API-INV-004
SpeechAttemptDetector never returns transcription.

### API-INV-005
SpeechAttemptDetector never returns pronunciation score.

### API-INV-006
Raw child audio does not cross the detector boundary.

### API-INV-007
Family recording and child attempt detection use separate contracts.

### API-INV-008
Exposure commit is idempotent.

### API-INV-009
Callbacks are scoped to session/interaction identity.

### API-INV-010
Microphone denial does not block session progression.

### API-INV-011
Real-world observations are pair-specific.

### API-INV-012
File-system paths stay below FileStore.

### API-INV-013
All critical asynchronous services can be faked in tests.

### API-INV-014
No API method requires an account or backend.

---

# 93. Open Implementation Decisions

The following remain for later documents/spike:

1. exact audio playback backend;
2. exact microphone signal algorithm;
3. whether controllers expose `StateFlow` or another state-holder abstraction;
4. exact Room entity/domain mapping style;
5. whether `AdaptiveEngine` queries repositories itself or receives prepared context;
6. exact error sealed-class hierarchy;
7. exact family recording file format.

These do not change the architectural contracts defined here.

---

# 94. Relationship to `architecture.md`

`architecture.md` defines the layers.

This document defines the callable boundaries between those layers.

---

# 95. Relationship to `database.md`

`database.md` defines persistence structure.

Repositories in this document are the only normal path to that persistence from higher layers.

---

# 96. Relationship to `audio.md`

`audio.md` must provide the concrete behavior behind:

```text
AudioService
```

including playback latency, exposure threshold, interruptions, and routing.

---

# 97. Relationship to `microphone.md`

`microphone.md` must provide the concrete behavior behind:

```text
SpeechAttemptDetector
```

including capture window, local signal processing, thresholds, and privacy guarantees.

---

# 98. Relationship to `interaction-states.md`

Controllers must respect the valid state transitions defined there.

An API callback alone cannot force an invalid state transition.

---

# 99. Decision Summary

Miozira Prototype 0.1 has no external backend API.

Its internal application API is centered on:

```text
LearningSessionEngine
AdaptiveEngine

LearningRepository
SessionRepository
ContentRepository
RealWorldObservationRepository
FamilyRecordingRepository
SettingsRepository

AudioService
SpeechAttemptDetector
FamilyRecordingService
PermissionService
FileStore
Clock
Randomizer
ResetService
RecoveryService
```

The contracts enforce:

- offline operation;
- platform-neutral domain logic;
- repository-based persistence;
- idempotent learning writes;
- strict audio/microphone boundaries;
- no raw child speech exposure;
- pair-specific real-world evidence;
- lifecycle-safe asynchronous callbacks;
- easy deterministic testing.

These contracts should be treated as the implementation boundary unless the technical spike exposes a concrete reason to revise them.
