# Miozira — System Architecture

**Document:** `architecture.md`  
**Version:** 0.1  
**Status:** Draft architecture baseline for Prototype 0.1  
**Product:** Miozira  
**Prototype:** Android-first, 8 concepts × English + Tamil  
**Primary runtime model:** Fully offline / local-first  
**Future portability target:** iPadOS  
**Last updated:** 2026-08-25

---

## 1. Purpose

This document defines the system architecture for Miozira Prototype 0.1.

It describes:

- major architectural layers;
- module boundaries;
- state ownership;
- domain logic;
- persistence responsibilities;
- audio and microphone boundaries;
- local file handling;
- content delivery;
- dependency direction;
- lifecycle/recovery rules;
- testability;
- Android-first implementation constraints;
- future iPadOS portability.

The technology stack is decided: **Kotlin Multiplatform + Compose Multiplatform**, with native Android as the only fallback, per [`ADR-0001-technology-stack.md`](../decisions/ADR-0001-technology-stack.md) (Accepted). This document describes the layered architecture in stack-independent terms by design, not because the stack is still open — the layering (Presentation → Domain → Data → Platform) is a boundary discipline that would hold under the fallback too, not an unresolved choice.

---

# 2. Architecture Goals

The Prototype 0.1 architecture must optimize for:

1. **family-test speed**
2. **offline reliability**
3. **child privacy**
4. **simple inspectable adaptive logic**
5. **clear state ownership**
6. **testability**
7. **Android tablet quality**
8. **reasonable future iPadOS portability**
9. **minimal technical complexity**

It must not optimize for hypothetical scale before the product loop is validated.

---

# 3. Architecture Non-Goals

Prototype 0.1 does not need:

- backend services;
- authentication;
- cloud synchronization;
- event streaming;
- remote configuration;
- microservices;
- distributed caches;
- push infrastructure;
- analytics pipeline;
- cloud speech recognition;
- AI/LLM infrastructure;
- feature-flag platform;
- account recovery;
- multi-device synchronization.

If the architecture requires any of these to run the child learning loop, it is too complex for Prototype 0.1.

---

# 4. Architectural Style

Miozira should use a layered, local-first architecture with explicit boundaries.

Recommended conceptual structure:

```text
┌──────────────────────────────────────────────┐
│                  PRESENTATION                │
│  Child UI · Parent UI · UI State · Events   │
└──────────────────────┬───────────────────────┘
                       │
┌──────────────────────▼───────────────────────┐
│                    DOMAIN                    │
│ Learning · Session · Adaptive · Transfer     │
│ State machines · Use cases · Domain models  │
└──────────────────────┬───────────────────────┘
                       │
┌──────────────────────▼───────────────────────┐
│                     DATA                     │
│ Repositories · Database · Preferences        │
│ Local event/history persistence              │
└──────────────────────┬───────────────────────┘
                       │
┌──────────────────────▼───────────────────────┐
│               PLATFORM SERVICES              │
│ Audio · Microphone · Files · Clock · Assets  │
└──────────────────────────────────────────────┘
```

Cross-cutting concerns:

```text
Privacy
Testing
Diagnostics
Accessibility
Lifecycle
```

---

# 5. Dependency Rule

Dependencies should point inward.

Recommended:

```text
Presentation
    ↓
Domain
    ↓
Repository interfaces / domain ports
    ↑
Data + platform implementations
```

The domain layer must not depend directly on:

- Android `Activity`;
- Compose UI classes;
- Android `AudioRecord`;
- filesystem paths;
- Room DAOs;
- concrete audio libraries.

---

# 6. Why Miozira Needs a Domain Layer

In many small apps, a separate domain layer may be unnecessary.

Miozira has enough reusable product logic to justify one because it contains:

- adaptive scheduling;
- learning-state derivation;
- session planning;
- rest/reintroduction logic;
- real-world evidence handling;
- state-machine rules;
- language balancing.

These rules should remain independent of UI implementation.

---

# 7. Presentation Layer

The presentation layer owns:

- child screens;
- parent screens;
- UI state;
- user-generated UI events;
- accessibility semantics;
- responsive layout;
- transient visual state.

It does **not** own:

- adaptive formulas;
- database writes directly;
- microphone implementation;
- permanent learning-state calculations.

---

# 8. Presentation Data Flow

Preferred model:

> **Unidirectional Data Flow (UDF)**

Conceptually:

```text
UI Event
   ↓
State holder / controller
   ↓
Domain operation
   ↓
Updated state
   ↓
UI render
```

Example:

```text
ConceptTapped
    ↓
ChildSessionController
    ↓
Play word / update interaction state
    ↓
ChildSessionUiState
    ↓
ConceptStage renders
```

---

# 9. Presentation State Must Be Explicit

Recommended child state:

```text
ChildSessionUiState
    sessionId
    interactionId
    activeConcept
    activeLanguage
    interactionState
    microphoneCapability
    replayAvailable
    parentGateState
```

Recommended parent state:

```text
ParentUiState
    latestSuggestion
    recentLearning
    familyRecordingState
    microphoneSetting
    languageSettings
```

---

# 10. Recomposition Is Not a Domain Event

If a declarative UI framework is used, UI recomposition must not trigger:

- exposure;
- replay;
- session start;
- microphone start;
- database write.

Domain events occur only through explicit commands/events.

---

# 11. Child Session Controller

A presentation/domain boundary component should coordinate one child session.

Conceptual responsibility:

```text
ChildSessionController
```

Responsibilities:

- start session;
- obtain first adaptive item;
- hold current interaction state;
- react to child taps;
- coordinate audio effects;
- coordinate microphone attempt window;
- finalize interaction;
- request next adaptive item;
- end session safely.

It should not contain database-driver code.

---

# 12. Parent Controller

Conceptual responsibility:

```text
ParentController
```

Responsibilities:

- load recent evidence;
- expose latest suggestion;
- save real-world observations;
- manage family recording commands;
- expose settings;
- reset data through repositories.

---

# 13. Domain Layer

The domain layer contains product meaning.

Recommended domain areas:

```text
domain/
  learning/
  adaptive/
  session/
  transfer/
  content/
```

---

# 14. Learning Domain

Owns definitions such as:

```text
Concept
Language
ConceptLanguagePair
Exposure
SpeakingAttempt
LearningEvidence
DerivedLearningState
```

It implements the semantics defined in:

> `learning-model.md`

---

# 15. Adaptive Domain

Owns:

- `NEW / EMERGING / FAMILIAR / RESTING`;
- due-date calculation;
- rest rules;
- new-item cap;
- same-session spacing;
- language balancing;
- selection reasons.

Conceptual API:

```text
AdaptiveEngine
```

---

# 16. Session Domain

Owns:

- session composition;
- current scheduled interaction;
- easy opener;
- recovery item;
- session closer;
- planned vs exposed distinction;
- interaction completion.

Conceptual API:

```text
LearningSessionEngine
```

---

# 17. Transfer Domain

Owns:

- parent real-world recognition;
- parent real-world use;
- real-world suggestion selection;
- transfer evidence semantics.

Conceptual API:

```text
RealWorldTransferService
```

This remains local and deterministic.

---

# 18. Domain Models Should Be Platform-Neutral

Preferred domain types:

```text
ConceptId
LanguageId
InteractionId
SessionId
Instant / timestamp abstraction
ExposureEvidence
AttemptState
SelectionReason
```

Avoid embedding:

```text
AndroidContext
Uri
Activity
Bitmap
AudioRecord
MediaPlayer
```

inside domain models.

---

# 19. Domain Time Abstraction

Adaptive scheduling depends on time.

Use an abstract clock:

```text
Clock.now()
```

rather than calling platform wall-clock APIs throughout the domain.

This enables deterministic tests.

---

# 20. Randomness Abstraction

If item selection requires tie-breaking randomness, use an abstraction:

```text
Randomizer
```

Tests should allow a deterministic seed/fake implementation.

---

# 21. Data Layer

The data layer owns durable local application state.

Recommended responsibilities:

- concept evidence;
- sessions;
- interactions;
- adaptive state;
- real-world observations;
- family-recording metadata;
- settings.

---

# 22. Local Source of Truth

Prototype 0.1 shall treat local storage as the authoritative source of truth.

There is no network source.

Conceptually:

```text
UI
 ↓
Domain
 ↓
Repository
 ↓
Local database / local files / preferences
```

No synchronization layer is required.

---

# 23. Repository Boundary

Higher layers should use repositories instead of accessing storage directly.

Recommended repositories:

```text
LearningRepository
SessionRepository
RealWorldObservationRepository
ContentRepository
FamilyRecordingRepository
SettingsRepository
```

The final implementation may merge repositories if this remains simpler without collapsing responsibilities.

---

# 24. LearningRepository

Conceptual responsibility:

```text
getPairState(pairId)
getAllPairStates()
recordExposure(...)
recordReplay(...)
recordAttempt(...)
updateDerivedState(...)
```

It should expose domain models, not database row objects.

---

# 25. SessionRepository

Conceptual responsibility:

```text
createSession(...)
recordInteraction(...)
completeSession(...)
getRecentSessions(...)
```

---

# 26. RealWorldObservationRepository

Conceptual responsibility:

```text
recordRecognition(...)
recordUse(...)
getObservations(pairId)
getRecentObservations()
```

---

# 27. ContentRepository

Conceptual responsibility:

```text
getConcept(id)
getConcepts()
getSpokenForm(pair)
getCanonicalAudio(pair)
getPrimaryVisual(concept)
getRealWorldSuggestions(pair)
```

Prototype canonical content may be bundled assets/structured manifests rather than mutable database rows.

---

# 28. SettingsRepository

Owns small parent/device settings such as:

```text
setupComplete
startingLanguage
microphoneAttemptDetectionEnabled
```

This is a better fit for lightweight preference storage than the main relational learning database.

---

# 29. FamilyRecordingRepository

Owns:

- local family-recording metadata;
- logical association to concept-language pair;
- file lifecycle;
- deletion metadata.

Raw audio files themselves belong in app-private file storage.

---

# 30. Persistence Technologies — Architectural Mapping

The architecture expects two storage categories:

## Structured relational data

Suitable for:

- sessions;
- interactions;
- evidence;
- observations;
- adaptive state.

Likely implementation:

> SQLite through a structured persistence layer such as Room.

## Small preferences

Suitable for:

- setup complete;
- starting language;
- microphone enabled.

Likely implementation:

> DataStore or equivalent.

## User-generated media

Suitable for:

- family recordings.

Implementation:

> app-private files + database metadata.

The final technology decision belongs in the ADR.

---

# 31. Why Relational Storage Fits

Prototype data has relationships such as:

```text
Session
  └── Interaction
        └── ConceptLanguagePair

Concept
  └── Spoken forms by Language

ConceptLanguagePair
  └── Learning evidence
  └── Real-world observations
```

A relational model provides clearer integrity than storing all learning state as ad-hoc preference blobs.

---

# 32. Event and Summary Strategy

Recommended architecture:

> store important evidence facts, then maintain/derive summaries.

Examples of facts:

```text
ExposureEvent
ReplayEvent
AttemptEvent
RealWorldObservation
```

Possible summaries:

```text
exposureCount
attemptCount
lastExposedAt
nextDueAt
derivedState
```

The database design will decide whether all facts need individual tables.

---

# 33. Static Content vs Dynamic Data

Keep these conceptually distinct.

## Static/bundled content

- concept definitions;
- English/Tamil spoken forms;
- canonical visual assets;
- canonical audio assets;
- curated real-world suggestions.

## Dynamic local data

- sessions;
- exposures;
- attempts;
- replays;
- adaptive state;
- parent observations;
- family recordings;
- settings.

---

# 34. No Network Layer in Prototype 0.1

There should be no architectural module such as:

```text
network/
apiClient/
sync/
auth/
```

unless needed only by build/development tooling and excluded from runtime.

The production child runtime requires no network dependency.

---

# 35. INTERNET Permission

Prototype 0.1 should aim to omit Android `INTERNET` permission if all selected dependencies permit it.

If a dependency requires internet access:

- it must be justified;
- privacy impact must be reviewed;
- runtime network behavior must be documented.

---

# 36. Platform Services

Platform-specific functionality belongs behind service interfaces.

Recommended:

```text
AudioService
SpeechAttemptDetector
FamilyRecordingService
FileStore
PermissionService
Clock
AssetProvider
```

---

# 37. AudioService

The domain/presentation layer should request semantic audio actions.

Conceptual interface:

```text
interface AudioService {
    playCanonicalWord(pairId)
    playPrompt(promptId, languageId)
    playFamilyRecording(pairId)
    stop()
}
```

Callbacks/events may report:

```text
PlaybackStarted
ExposureThresholdReached
PlaybackCompleted
PlaybackFailed
```

---

# 38. Audio Implementation Boundary

The child/session logic must not depend directly on:

- `MediaPlayer`;
- ExoPlayer/Media3;
- AVFoundation;
- platform audio-session APIs.

Those are implementation choices behind `AudioService`.

---

# 39. SpeechAttemptDetector

Prototype 0.1 needs speech-presence evidence only.

Conceptual interface:

```text
interface SpeechAttemptDetector {
    startWindow(duration)
    stop()
}
```

Possible results:

```text
ATTEMPT_DETECTED
NO_ATTEMPT
UNAVAILABLE
ERROR
```

No transcript is returned.

No pronunciation score is returned.

---

# 40. Android Speech Attempt Implementation Direction

On Android, a likely low-level implementation is:

> `AudioRecord` with short-lived PCM reads.

The detector may compute simple local features such as:

- RMS/energy;
- noise-floor-relative amplitude;
- lightweight voice-activity heuristics.

This is an engineering direction, not a finalized algorithm.

The microphone document will define details.

---

# 41. Child Audio Privacy Boundary

`SpeechAttemptDetector` must not expose raw audio to the domain layer.

Preferred architecture:

```text
Android AudioRecord
      ↓
local transient detector
      ↓
AttemptResult
      ↓
domain
```

Not:

```text
Android AudioRecord
      ↓
raw PCM stored in database
      ↓
domain
```

---

# 42. FamilyRecordingService

Family recording is a separate capability from child attempt detection.

Conceptual interface:

```text
startRecording(pairId)
stopRecording()
cancelRecording()
playPreview()
saveRecording()
deleteRecording(pairId)
```

It may use a different capture abstraction because it intentionally creates a local file.

---

# 43. No Shared Ambiguous Recorder State

Do not use one giant microphone component with unclear modes.

Separate conceptual responsibilities:

```text
ChildSpeechAttemptDetector
ParentFamilyRecorder
```

They may share a low-level platform utility internally, but their public contracts are distinct.

---

# 44. PermissionService

Permission behavior should be separated from learning logic.

Conceptual API:

```text
microphonePermissionState()
requestMicrophonePermission()
openSystemSettingsIfNeeded()
```

Child domain logic should consume capability state, not invoke Android dialogs directly.

---

# 45. AssetProvider

Bundled content access should be abstracted.

Conceptual API:

```text
getVisualAsset(conceptId)
getCanonicalAudioAsset(pairId)
getPromptAudioAsset(promptId)
```

Logical asset IDs should remain stable even if physical filenames/codecs change.

---

# 46. FileStore

Owns app-private file locations and safe file operations.

Used for:

- parent/family recordings;
- temporary recording files.

Domain logic should not manipulate raw filesystem paths.

---

# 47. State-Machine Architecture

`interaction-states.md` should be implemented as explicit state transitions.

Recommended conceptual pattern:

```text
State + Event
    ↓
Reducer / Controller
    ↓
New State + Effects
```

Example:

```text
READY + ConceptTapped
    ↓
PLAYING_WORD
    +
PlayCanonicalWord(pairId)
```

---

# 48. Effects

Effects may include:

```text
PlayAudio
StartMicrophoneWindow
StopMicrophone
PersistExposure
PersistAttempt
AskAdaptiveEngineForNext
NavigateToParent
```

Effects should be executed once and safely tied to session/interaction IDs.

---

# 49. Stale Callback Protection

All asynchronous audio/microphone callbacks should carry or be associated with:

```text
sessionId
interactionId
```

Before mutating state, the receiver verifies the callback still belongs to the active interaction.

This prevents:

- old playback callbacks advancing a new item;
- delayed microphone callbacks creating false attempts.

---

# 50. Interaction Identity

Every scheduled interaction should have a unique stable local identifier.

Uses:

- exposure idempotency;
- lifecycle restoration;
- stale callback rejection;
- test diagnostics.

---

# 51. Exposure Idempotency

Persistence must enforce:

> one scheduled exposure commit per interaction.

Possible strategies:

- unique constraint on interaction exposure;
- repository idempotency key;
- transaction check.

The exact database mechanism belongs in `database.md`.

---

# 52. Lifecycle Architecture

Android lifecycle events are technical events, not learning events.

The architecture must handle:

- orientation changes;
- backgrounding;
- process death;
- audio interruptions;
- permission-dialog transitions.

---

# 53. Orientation

UI state should survive portrait/landscape changes without recreating domain interaction meaning.

Required preservation:

```text
sessionId
interactionId
activePair
interactionState
exposureCommitted
attemptState
```

Transient rendering details may be recreated.

---

# 54. Backgrounding

On background:

- stop child microphone immediately;
- pause/stop audio safely;
- persist committed evidence;
- preserve safe logical session state.

Do not run child listening in the background.

---

# 55. Process Death

Persisted domain evidence survives.

Uncommitted transient interaction may be abandoned.

On relaunch:

- recover database;
- do not fabricate missing events;
- start a valid child state.

---

# 56. Error Architecture

Errors should be classified by recoverability.

Recommended categories:

```text
RecoverableMediaError
RecoverablePermissionError
RecoverableOptionalContentError
PersistenceError
FatalLocalStateError
```

---

# 57. Error Ownership

Low-level layers should return typed failures.

Presentation decides how to communicate them.

Example:

```text
AudioService → AudioAssetUnavailable
```

Child UI:

```text
skip/fallback silently
```

Parent diagnostics:

```text
"The standard word recording could not be played."
```

---

# 58. No Raw Technical Error in Child UI

Exceptions must not flow directly into child-facing strings.

---

# 59. Transactions

Persistence operations that must remain consistent should use local transactions.

Examples:

- save interaction + update pair summary;
- save parent observation + update derived scheduling state;
- reset related learning tables.

The database document will define exact boundaries.

---

# 60. Concurrency

Prototype 0.1 should keep concurrency simple.

Likely asynchronous operations:

- audio playback;
- microphone capture;
- database writes;
- family file I/O.

Domain state should serialize meaningful interaction events to avoid race conditions.

---

# 61. Coroutine / Async Model

If Kotlin is selected, structured concurrency with coroutines is a natural fit.

Architectural requirement regardless of framework:

- long-running/blocking work stays off the UI thread;
- lifecycle cancellation is explicit;
- stale work cannot mutate a newer interaction.

---

# 62. Main-Safe Repository Contract

Repository APIs should be safe for presentation/domain callers to invoke without manually managing disk threads at each call site.

Storage implementation owns appropriate dispatching/concurrency.

---

# 63. Content Bootstrapping

On first launch, bundled canonical content should be available without download.

Possible strategies:

### A. Read static manifest directly from bundled assets

or

### B. Seed selected canonical metadata into local database.

Recommendation for 8 concepts:

> keep canonical content definition in a versioned bundled manifest and persist only what needs relational querying/learning state.

Final design belongs in `database.md`.

---

# 64. Content Versioning

Architecture must support:

```text
contentVersion
```

so a future content correction can be distinguished from old family-test data.

---

# 65. Tamil Content Independence

The architecture must not encode English as the primary content key.

Correct:

```text
ConceptId + LanguageId
```

Incorrect:

```text
EnglishWord → TamilTranslation
```

---

# 66. Real-World Suggestion Engine

Prototype 0.1 uses a local curated rule system.

Architecture:

```text
RecentSessionEvidence
      ↓
RealWorldTransferService
      ↓
CuratedSuggestionRepository
      ↓
RealWorldSuggestion
```

No LLM/API is required.

---

# 67. Parent Observation Flow

```text
Parent UI
   ↓
RecordRealWorldObservation
   ↓
RealWorldObservationRepository
   ↓
Local database
   ↓
AdaptiveEngine recalculation
```

The adaptive engine does not depend on the UI.

---

# 68. Data Reset Flow

```text
Parent confirmation
      ↓
ResetUseCase
      ↓
Repositories / FileStore
      ↓
transactional learning-data clear
      +
family recording delete
      +
settings reset as defined
```

Canonical bundled assets remain.

---

# 69. Diagnostics

Prototype 0.1 may use local development diagnostics.

Useful diagnostic fields:

```text
sessionId
interactionId
pairId
state transition
selection reason
audio result
mic result
persistence result
```

Do not log raw child audio.

---

# 70. Production Diagnostics

Release/family-test builds should keep logs restrained.

No remote telemetry is required.

If logs are exportable in a developer build later, that must be an explicit parent/developer action.

---

# 71. Testing Architecture

The architecture must allow major logic to run without real hardware.

Injectable/fake components:

```text
FakeClock
FakeRandomizer
FakeAudioService
FakeSpeechAttemptDetector
FakeRepositories
FakeAssetProvider
```

---

# 72. Unit-Test Boundaries

Unit tests should cover:

- adaptive engine;
- state derivation;
- session planning;
- transfer suggestion selection;
- child interaction reducer/state machine.

These tests should not require Android instrumentation.

---

# 73. Integration-Test Boundaries

Integration tests should cover:

- repositories + database;
- database migrations;
- state machine + fake services;
- content manifest resolution;
- reset transactions.

---

# 74. Device-Test Boundaries

Real-device testing is required for:

- audio latency;
- microphone behavior;
- orientation;
- lifecycle;
- backgrounding;
- Android permission flow;
- tablet sizing;
- accessibility/TalkBack;
- family recording.

---

# 75. Architecture for Future iPadOS

Future portability should focus first on sharing:

- domain models;
- adaptive logic;
- session logic;
- repositories/contracts;
- database schema/models where practical;
- content metadata;
- transfer logic.

Platform-specific adapters remain appropriate for:

- microphone;
- audio session;
- permissions;
- files;
- OS lifecycle.

---

# 76. Shared UI Is Optional Architecture

The architecture should permit either:

### Shared UI

Example:

> Compose Multiplatform child + parent UI.

### Native platform UI

Example:

> shared KMP domain/data, Android Compose UI, future SwiftUI iOS UI.

The architecture should not force the decision before the ADR.

---

# 77. Current Technology Mapping Candidate

As of the current design stage, the strongest candidate mapping is:

```text
Kotlin Multiplatform
+
Compose Multiplatform
+
Room / SQLite
+
DataStore
+
platform audio/microphone adapters
```

Reasons:

- Android-first Kotlin ecosystem;
- stable Android/iOS KMP targets;
- stable Compose Multiplatform Android/iOS targets;
- Room has KMP support;
- shared domain logic aligns well with future iPadOS;
- Android platform APIs remain available for low-level microphone work.

This remains provisional until `ADR-0001-technology-stack.md`.

---

# 78. Candidate Module Structure

A KMP-compatible conceptual structure may look like:

```text
miozira/
│
├── androidApp/
│
├── iosApp/                 # future / optional early spike
│
├── shared/
│   ├── presentation/
│   │   ├── child/
│   │   └── parent/
│   │
│   ├── domain/
│   │   ├── learning/
│   │   ├── adaptive/
│   │   ├── session/
│   │   ├── transfer/
│   │   └── content/
│   │
│   ├── data/
│   │   ├── repository/
│   │   ├── database/
│   │   ├── preferences/
│   │   └── content/
│   │
│   └── services/
│       ├── audio/
│       ├── microphone/
│       ├── files/
│       └── clock/
│
└── content/
    ├── manifest/
    ├── images/
    └── audio/
```

Exact Gradle/module boundaries should remain lean.

Do not create one Gradle module for every small package.

---

# 79. Lean Module Rule

Architectural separation does not require excessive build modules.

Prototype 0.1 may begin with:

```text
androidApp
shared
```

plus an eventual iOS shell.

Inside `shared`, packages can enforce domain/data/service separation.

Split build modules only when there is a clear reason.

---

# 80. Android-Only Alternative Mapping

If the ADR chooses native Android first:

```text
Android App
  ├── Compose presentation
  ├── Kotlin domain
  ├── repositories
  ├── Room
  ├── DataStore
  ├── Android AudioService
  └── AudioRecord SpeechAttemptDetector
```

The same architecture rules still apply.

Future iOS would require porting or extracting domain logic later.

---

# 81. Flutter Alternative Mapping

If Flutter is chosen:

- domain/adaptive logic should still be isolated;
- SQLite/repository pattern still applies;
- audio/microphone remain plugin/platform boundaries;
- UI state still follows UDF-like explicit event/state handling.

Architecture principles are framework-independent.

---

# 82. Dependency Injection

Prototype 0.1 needs dependency substitution for testing, but not necessarily a heavy DI framework.

Acceptable approaches:

- constructor injection;
- simple application composition root;
- lightweight DI library if selected stack benefits.

Avoid adding a large DI framework solely because enterprise apps use one.

---

# 83. Composition Root

One place should create concrete implementations.

Conceptually:

```text
AppContainer
```

It wires:

```text
Database
Repositories
AdaptiveEngine
AudioService
SpeechAttemptDetector
Controllers
```

Tests substitute fakes.

---

# 84. Runtime Data Flow Example — Tap

```text
Child taps concept
      ↓
Child UI event
      ↓
ChildSessionController
      ↓
interaction state = PLAYING_WORD
      ↓
AudioService.playCanonicalWord()
      ↓
ExposureThresholdReached
      ↓
LearningRepository.recordExposure()
      ↓
UI/domain state remains active
```

---

# 85. Runtime Data Flow Example — Speaking Attempt

```text
Word playback completes
      ↓
interaction state = LISTENING
      ↓
SpeechAttemptDetector.startWindow()
      ↓
ATTEMPT_DETECTED
      ↓
LearningRepository.recordAttempt()
      ↓
interaction state = RESPONDING
```

No raw child audio crosses into the domain/database.

---

# 86. Runtime Data Flow Example — Next Item

```text
Interaction completed
      ↓
AdaptiveEngine receives current evidence/session state
      ↓
chooseNextItem()
      ↓
SelectionDecision
  pairId
  reason
      ↓
ChildSessionController
      ↓
PRESENTING
```

---

# 87. Runtime Data Flow Example — Real-World Use

```text
Parent taps "Used outside Miozira"
      ↓
ParentController
      ↓
RealWorldObservationRepository
      ↓
save local observation
      ↓
AdaptiveEngine recalculates pair
      ↓
pair may become FAMILIAR
nextDue may move later
```

---

# 88. Security Boundaries

Prototype architecture should enforce:

- app-private user-generated files;
- no embedded secrets;
- least-privilege permissions;
- microphone access only during intended windows;
- no hidden network clients;
- dependency review.

Full threat analysis belongs in `threat-model.md`.

---

# 89. Privacy Boundaries

Architectural privacy guarantees:

```text
No account
No backend
No child raw-speech persistence
No child audio upload
No behavioral analytics
Local learning source of truth
Local family recordings
```

Privacy is not merely a settings-screen promise.

It is a runtime architecture property.

---

# 90. Performance Boundaries

Architecture must make it possible to meet:

- tap acknowledgement ≤100 ms target;
- local word playback start ≤250 ms target;
- normal local transition ≤300 ms target where applicable;
- responsive UI while database writes occur.

Do not perform disk I/O synchronously on the rendering thread.

---

# 91. Battery Boundaries

Architecture shall not require:

- always-on microphone;
- background adaptive processing;
- polling;
- persistent network service;
- continuous wake lock.

The app should be mostly event-driven while active.

---

# 92. Architecture Invariants

### ARCH-INV-001
Core child learning requires no network.

### ARCH-INV-002
Domain logic has no direct Android UI dependency.

### ARCH-INV-003
Adaptive logic is testable without UI/device hardware.

### ARCH-INV-004
UI does not write directly to database DAOs.

### ARCH-INV-005
Raw child audio does not enter persistent learning storage.

### ARCH-INV-006
Child speech detection and parent voice recording are separate capabilities.

### ARCH-INV-007
Local storage is the learning source of truth.

### ARCH-INV-008
Canonical content is available locally.

### ARCH-INV-009
Rotation/recomposition is not a learning event.

### ARCH-INV-010
Every scheduled interaction has stable identity.

### ARCH-INV-011
Exposure persistence is idempotent.

### ARCH-INV-012
Stale async callbacks cannot advance a newer interaction.

### ARCH-INV-013
Microphone denial never blocks learning.

### ARCH-INV-014
Real-world observations are persisted as parent-reported evidence.

### ARCH-INV-015
No framework decision may require a backend for Prototype 0.1.

### ARCH-INV-016
Future iPadOS portability may not delay the first Android family test without a concrete reason.

---

# 93. Architecture Review Checklist

Before implementation begins, confirm:

- [ ] presentation/domain/data boundaries are clear;
- [ ] adaptive engine has no Android dependency;
- [ ] repositories own persistence access;
- [ ] local database is source of truth;
- [ ] settings are separated from structured learning history;
- [ ] family recordings use private files;
- [ ] child mic returns only attempt state;
- [ ] audio is behind a service boundary;
- [ ] state machine is explicit;
- [ ] interaction IDs prevent duplicate evidence;
- [ ] orientation cannot generate domain events;
- [ ] no network runtime module is required;
- [ ] architecture can be unit-tested with fake clock/audio/mic.

---

# 94. Architecture Risks

## Risk 1 — Over-engineering KMP before validation

Mitigation:

> keep shared module and build-module count small.

## Risk 2 — Shared UI becomes harder than native Android

Mitigation:

> ADR may select shared domain only or Android-first UI.

## Risk 3 — Audio/mic callbacks corrupt state

Mitigation:

> explicit state machine + session/interaction identity.

## Risk 4 — Database becomes a premature event-sourcing system

Mitigation:

> preserve useful evidence without building a generic analytics platform.

## Risk 5 — Privacy promise broken by dependency

Mitigation:

> dependency inventory and runtime network review.

## Risk 6 — Family recordings complicate microphone ownership

Mitigation:

> separate child detector and parent recorder contracts.

---

# 95. Technology Spike Before Full Build

Before committing deeply to the final stack, implement one small technical spike:

```text
1. full-screen concept
2. bundled canonical word audio
3. tap → playback
4. 2–3 second speech-attempt detector
5. persist one exposure/attempt row
6. rotate portrait ↔ landscape without duplicate evidence
7. restart app and retain state
```

If KMP/Compose is the leading option, optionally verify a minimal iOS compile during the spike.

The spike is not a separate product prototype.

It validates architectural risk.

---

# 96. Success Criteria for Architecture Spike

The architecture candidate is viable if:

- tap/audio feels immediate;
- microphone detector works without storing files;
- orientation preserves interaction identity;
- local write is reliable;
- architecture does not require Android-specific code in adaptive logic;
- code remains understandable for a small team/solo builder;
- future iOS boundary is visible rather than theoretical.

---

# 97. Relationship to `ADR-0001-technology-stack.md`

The ADR should compare at minimum:

1. Kotlin Multiplatform + Compose Multiplatform;
2. native Android Kotlin + Compose;
3. Flutter;
4. React Native only if still considered relevant.

Criteria should include:

- Android delivery speed;
- audio/microphone control;
- offline database maturity;
- lifecycle reliability;
- iOS portability;
- debugging;
- ecosystem maturity;
- AI-assisted development friendliness;
- complexity for the builder.

---

# 98. Relationship to `database.md`

`database.md` must translate this architecture into:

- entities;
- keys;
- relationships;
- indexes;
- event/history representation;
- migrations;
- idempotency constraints;
- reset behavior.

---

# 99. Relationship to `api.md`

Prototype 0.1 has no external network API.

`api.md` should instead define internal application contracts between:

- presentation;
- learning/session engine;
- repositories;
- audio;
- microphone;
- family recording;
- settings.

---

# 100. Relationship to `audio.md`

`audio.md` must decide:

- playback implementation;
- audio focus;
- canonical/family source selection;
- replay behavior;
- interruption handling;
- exposure threshold.

---

# 101. Relationship to `microphone.md`

`microphone.md` must decide:

- capture API;
- PCM processing;
- noise handling;
- attempt threshold;
- listening-window duration;
- lifecycle;
- permission behavior;
- transient data deletion.

---

# 102. Decision Summary

Miozira Prototype 0.1 adopts the following architecture baseline:

1. local-first, with no backend;
2. presentation, domain, data, and platform-service boundaries;
3. UDF/event-driven UI state;
4. explicit child interaction state machine;
5. platform-neutral adaptive/session logic;
6. repositories between domain and storage;
7. relational local persistence for structured learning data;
8. small preference store for settings;
9. app-private files for family recordings;
10. bundled canonical content;
11. audio and microphone behind interfaces;
12. raw child audio never reaches persistent domain storage;
13. stable session/interaction IDs for lifecycle safety;
14. deterministic testing through fake clock/services;
15. Android-first delivery with iPadOS portability protected but not overbuilt.

The current leading implementation candidate is Kotlin Multiplatform + Compose Multiplatform + Room/SQLite with platform-specific audio/microphone adapters, but that decision remains formally open until the technology ADR.
