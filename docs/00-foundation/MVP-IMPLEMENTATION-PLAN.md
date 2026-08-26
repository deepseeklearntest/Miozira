# Miozira Prototype 0.1 Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Deliver a trustworthy, installable, fully offline Miozira Prototype 0.1 for a seven-day family test on one Android tablet.

**Architecture:** Build thin, testable vertical slices through Compose UI, platform audio/microphone adapters, domain state, and local persistence. Keep `:shared` and `:androidApp` as the only production Gradle modules unless the mandatory spike proves KMP/CMP is a material blocker; dependencies point inward and UI never calls Room DAOs directly.

**Tech Stack:** Kotlin Multiplatform, Compose Multiplatform, Kotlin Coroutines/StateFlow, Room KMP/SQLite, DataStore, Android platform audio APIs, `AudioRecord`, app-private files, JUnit/Kotlin Test, Android instrumented/UI tests.

**Spec:** [`docs/00-foundation/PRD-prototype-0.1.md`](PRD-prototype-0.1.md), [`docs/03-engineering/architecture.md`](../03-engineering/architecture.md), [`docs/decisions/ADR-0001-technology-stack.md`](../decisions/ADR-0001-technology-stack.md), [`docs/03-engineering/database.md`](../03-engineering/database.md), [`docs/03-engineering/api.md`](../03-engineering/api.md), [`docs/03-engineering/audio.md`](../03-engineering/audio.md), [`docs/03-engineering/microphone.md`](../03-engineering/microphone.md), [`docs/03-engineering/local-storage.md`](../03-engineering/local-storage.md), [`docs/03-engineering/permissions.md`](../03-engineering/permissions.md), [`docs/05-quality/testing.md`](../05-quality/testing.md), [`docs/05-quality/acceptance-criteria.md`](../05-quality/acceptance-criteria.md), and [`docs/05-quality/family-test-protocol.md`](../05-quality/family-test-protocol.md).

## Global Constraints

- Android tablet first; landscape primary and portrait secondary.
- Begin the spike at `minSdk 26`; close OQ-005 after testing the actual family tablet and minimum-SDK emulator. Use `targetSdk 34+`, raising it to the applicable release requirement without changing product behavior.
- Exactly 8 concepts: `apple`, `ball`, `cup`, `hand`, `nose`, `cat`, `car`, `water`.
- Exactly 2 languages: English and Tamil; 16 independently scheduled concept-language pairs.
- The app is 100% offline: no backend, accounts, sync, telemetry, remote content, cloud speech, ads, subscriptions, or IAP. Omit `android.permission.INTERNET`.
- The only sensitive Android runtime permission is `android.permission.RECORD_AUDIO`; core child learning remains complete when it is denied or disabled.
- Child microphone input is 2–3 seconds, memory-only, and reduced to `ATTEMPT_DETECTED`, `NO_ATTEMPT_DETECTED`, `MICROPHONE_UNAVAILABLE`, or `NOT_MEASURED`. No PCM file, transcript, expected-word input, pronunciation score, or biometric inference.
- A scheduled exposure commits only after clip completion or approximately 80% meaningful playback. Replay increments `replay_count` but never scheduled exposure count.
- Every async audio/microphone callback carries stable `SessionId` and `InteractionId`; stale callbacks are ignored and exposure writes are idempotent.
- Family recordings are adult-controlled, app-private, replaceable, deletable, and never replace the bundled canonical source of truth.
- Preserve the locked child palette: background `#F2EEE7`, primary content `#26312E`, primary action `#4F746B`.
- Never add scores, streaks, XP, lives, timers, races, badges, confetti, leaderboards, red/green grading, mandatory completion, forced retry, background music, or engagement pressure.
- Silence, replay, stopping early, language preference, and mispronunciation are normal behavior, not failure states.
- Session target is approximately 5–10 minutes, selecting 4–8 items with no more than 2 new pairs per session.
- A family-test release requires all BLOCKER and HIGH acceptance criteria, zero BLOCKER/HIGH defects, frozen content/adaptive/microphone config, and a pass on the primary physical tablet.

---

## Planned File and Module Map

Keep the module count lean. The paths below establish responsibility; implementation tasks may add adjacent focused files, but must not introduce another Gradle module without recording why the current two-module structure is insufficient.

```text
androidApp/
  src/main/AndroidManifest.xml                 # minimal manifest; RECORD_AUDIO only sensitive permission
  src/main/kotlin/com/miozira/MainActivity.kt  # Android host and lifecycle forwarding
  src/main/kotlin/com/miozira/AppContainer.kt  # composition root
  src/androidTest/...                          # device, permission, manifest, lifecycle, UI tests

shared/src/commonMain/kotlin/com/miozira/
  core/model/Ids.kt                            # strongly typed IDs
  core/result/AppResult.kt                     # platform-neutral failures
  core/time/Clock.kt                           # Clock and FakeClock boundary
  content/ContentModels.kt                     # concepts, languages, pairs, logical asset refs
  content/ContentRepository.kt                 # bundled content contract
  domain/session/InteractionState.kt           # state/event/effect algebra
  domain/session/InteractionReducer.kt         # pure legal transitions
  domain/session/LearningSessionEngine.kt       # session semantic lifecycle
  domain/session/ChildSessionController.kt      # UI coordination and stale-callback checks
  domain/adaptive/AdaptivePolicy.kt             # versioned, frozen scheduling constants
  domain/adaptive/AdaptiveEngine.kt             # pure pair update and next-item selection
  domain/transfer/RealWorldTransferService.kt   # observations and one quiet suggestion
  data/db/MioziraDatabase.kt                    # Room database and transactions
  data/db/entity/*.kt                           # one focused entity per specified table
  data/db/dao/*.kt                              # narrow DAOs; never exposed to UI
  data/repository/*.kt                          # Room/DataStore repository implementations
  services/audio/AudioService.kt                # playback contract and semantic callbacks
  services/microphone/SpeechAttemptDetector.kt  # semantic detector contract only
  services/microphone/EnergyAttemptClassifier.kt# pure PCM-frame classifier
  services/files/FileStore.kt                   # logical private-file IDs, no raw paths above it
  services/recording/FamilyRecordingService.kt  # adult recorder contract
  presentation/child/*.kt                       # child state and calm Compose screen
  presentation/parent/*.kt                      # gate, summary, transfer, settings, reset, voice UI

shared/src/androidMain/kotlin/com/miozira/
  data/db/DatabaseFactory.android.kt
  services/audio/AndroidAudioService.kt
  services/microphone/AndroidSpeechAttemptDetector.kt
  services/files/AndroidPrivateFileStore.kt
  services/recording/AndroidFamilyRecordingService.kt
  services/permissions/AndroidPermissionService.kt

shared/src/commonMain/composeResources/
  files/content/manifest-v1.json                # exactly 8 concepts, 2 languages, 16 pairs
  drawable/concept_*.webp                       # approved concept visuals
  files/audio/word_<concept>_<language>_v1.m4a  # approved canonical clips

shared/src/commonTest/kotlin/com/miozira/...    # pure domain, state, content, classifier tests
shared/src/androidInstrumentedTest/...          # Room, files, audio/mic lifecycle tests
config/android-permission-allowlist.txt         # RECORD_AUDIO allowlist
docs/05-quality/evidence/...                    # build/device/manual verification records
```

## Implementation Discipline

Each task is one review gate and should normally be one small PR. Use red-green-refactor: add the named failing test, observe the expected failure, implement only the stated outcome, run the narrow test, then run the affected regression suite. Do not merge a task merely because it compiles.

Use these concrete commands unless the task names a narrower one:

```bash
# Pure common/domain/content tests
./gradlew :shared:allTests

# Room, Android service, and shared Android instrumented tests
./gradlew :shared:connectedAndroidTest

# Android host/UI/permission/lifecycle tests
./gradlew :androidApp:connectedDebugAndroidTest

# Installable debug verification
./gradlew :androidApp:assembleDebug

# Release-candidate verification
./gradlew :shared:allTests :androidApp:lintRelease :androidApp:assembleRelease
```

For each Step 2, the expected result is a failure in the named new behavior—not a configuration or unrelated test failure. For each Step 4, the expected result is that the named narrow test and every affected command above pass. If actual Gradle task names differ after the Phase 0 source-set setup, Phase 0 must document the exact replacement commands here before feature work begins.

When a task references an exact learning or interaction rule, read the directly governing specification before writing the test: `docs/01-learning/adaptive-engine.md`, `docs/01-learning/session-design.md`, `docs/02-design/interaction-states.md`, `docs/02-design/parent-experience.md`, or `docs/02-design/accessibility.md`. If those rules contradict the source set listed in this plan, stop, record the conflict in `docs/OPEN-QUESTIONS.md`, and resolve it before production behavior is chosen.

---

## Phase 0 — Repository/bootstrap

**Goal:** Turn the initialized repository into a reproducible, test-running two-module KMP/CMP application shell.

**Why this phase exists:** Every risk experiment needs the same trustworthy build, test, lifecycle, and manifest foundation. It prevents the spike from becoming an unrepeatable demo project.

**Files/modules likely involved:** root Gradle files, `:shared`, `:androidApp`, CI workflow, Android manifest, `AppContainer`, common test source set.

**Automated tests:** Gradle configuration, common smoke test, Android assemble, manifest allowlist test, CI wrapper validation.

**Manual tests:** Install the empty shell on the primary tablet; verify cold/warm launch and both orientations.

**Acceptance criteria:** AC-B01 app launch foundation; no unexplained permission; no network dependency; JDK 17 and Gradle wrapper work locally and in CI.

**Exit criteria:** `./gradlew :shared:allTests :androidApp:assembleDebug` succeeds from a clean checkout and CI runs the same meaningful checks.

**Dependencies:** None.

**Risks:** incompatible plugin versions, KMP source-set misconfiguration, accidental dependency-added permissions, building an elaborate architecture before risk validation.

**Explicit things NOT to build:** child session logic, Room schema, microphone flow, parent UI, navigation framework, DI framework, extra Gradle modules, production iOS UI.

### Task 0.1: Make the two-module shell build and test

**Files:**
- Create: `shared/build.gradle.kts`
- Create: `androidApp/build.gradle.kts`
- Create: `shared/src/commonMain/kotlin/com/miozira/App.kt`
- Create: `shared/src/commonTest/kotlin/com/miozira/AppSmokeTest.kt`
- Create: `androidApp/src/main/kotlin/com/miozira/MainActivity.kt`
- Modify: `gradle/libs.versions.toml`
- Modify: `.github/workflows/ci.yml`

**Interfaces:**
- Consumes: existing root plugin aliases and `:shared`/`:androidApp` includes.
- Produces: `@Composable fun MioziraApp()` and a common test task usable by every later PR.

- [ ] **Step 1: Add a failing common smoke test.**

```kotlin
@Test fun appIdentity_isStable() {
    assertEquals("Miozira", appDisplayName())
}
```

- [ ] **Step 2: Run `./gradlew :shared:allTests`; expect failure because `appDisplayName` and the shared build do not exist.**
- [ ] **Step 3: Configure only `commonMain`, `commonTest`, `androidMain`, and the Android app host; implement `fun appDisplayName() = "Miozira"` and a warm-stone `MioziraApp()` shell.**
- [ ] **Step 4: Run `./gradlew :shared:allTests :androidApp:assembleDebug`; expect PASS and a debug APK.**
- [ ] **Step 5: Commit as `build: bootstrap KMP Compose application shell`.**

### Task 0.2: Enforce the offline permission boundary from the first build

**Files:**
- Create: `androidApp/src/main/AndroidManifest.xml`
- Create: `config/android-permission-allowlist.txt`
- Create: `androidApp/src/androidTest/kotlin/com/miozira/ManifestPolicyTest.kt`
- Modify: `.github/workflows/ci.yml`

**Interfaces:**
- Consumes: assembled Android manifest from Task 0.1.
- Produces: a build gate that permits `RECORD_AUDIO` and rejects `INTERNET`, storage, location, camera, notification, and unexpected dangerous permissions.

- [ ] **Step 1: Add a manifest-policy test that asserts the merged permission set equals `setOf("android.permission.RECORD_AUDIO")` once sensitive permissions are introduced and never contains `android.permission.INTERNET`.**
- [ ] **Step 2: Run the policy test against a deliberately injected `INTERNET` declaration; expect FAIL naming the forbidden permission, then remove the injected declaration.**
- [ ] **Step 3: Add the minimal manifest and CI task that dumps and checks the release merged manifest against `config/android-permission-allowlist.txt`.**
- [ ] **Step 4: Run `./gradlew :androidApp:processDebugMainManifest :androidApp:assembleDebug`; expect PASS with no network permission.**
- [ ] **Step 5: Commit as `test: guard offline Android manifest`.**

---

## Phase 1 — Mandatory technical spike

**Goal:** Prove or reject KMP/CMP on the actual risky path: tap, local audio, short memory-only capture, Room write, rotation, restart, and offline operation.

**Why this phase exists:** Audio latency, microphone lifecycle, Room KMP integration, and state identity are the uncertainties most capable of invalidating the chosen stack. They must be tested together before broad implementation.

**Files/modules likely involved:** one `apple:en` resource, shared service contracts, Android audio/mic adapters, minimal Room database, spike screen, lifecycle state holder, ADR evidence note.

**Automated tests:** fake audio semantic callbacks, energy classifier smoke test, Room round-trip/idempotency, saved interaction identity, manifest/network guard.

**Manual tests:** primary tablet tap latency, AudioRecord open/release, background/rotation, process restart, airplane mode, basic TalkBack/CMP check.

**Acceptance criteria:** all ten mandatory ADR spike proofs; tap acknowledgement normally ≤100 ms; canonical audio normally begins ≤250 ms; no PCM file; one exposure/attempt survives restart; no duplicate identity on rotation.

**Exit criteria:** a written GO decision retains KMP/CMP, or a written FALLBACK decision switches to native Android while preserving contracts/schema/state architecture. Do not begin Phase 2 without this decision.

**Dependencies:** Phase 0.

**Risks:** mistaking emulator success for device proof, polishing throwaway UI, hiding latency behind animation, leaving microphone resources open, allowing spike shortcuts into production unnoticed.

**Explicit things NOT to build:** all 16 pairs, final adaptive algorithm, parent portal, polished visuals, speech recognition, pronunciation scoring, iOS product flow, reusable media framework.

### Task 1.1: Prove “tap apple → bundled English audio starts” on the tablet

**Files:**
- Create: `shared/src/commonMain/kotlin/com/miozira/services/audio/AudioService.kt`
- Create: `shared/src/androidMain/kotlin/com/miozira/services/audio/AndroidAudioService.kt`
- Create: `shared/src/commonMain/composeResources/files/audio/word_apple_en_spike.m4a`
- Create: `shared/src/commonMain/kotlin/com/miozira/presentation/spike/SpikeScreen.kt`
- Test: `shared/src/commonTest/kotlin/com/miozira/services/audio/FakeAudioServiceTest.kt`

**Interfaces:**
- Produces: `playCanonicalWord(request, listener): AppResult<PlaybackHandle>` with `Started`, `ExposureThresholdReached`, `Completed`, and `Failed` events carrying `SessionId` and `InteractionId`.
- Produces: one visual tap target invoking one bundled clip with no overlap.

- [ ] **Step 1: Add a fake-service test asserting one accepted tap emits `Started` once and three rapid taps never create two active handles.**
- [ ] **Step 2: Run `./gradlew :shared:allTests`; expect failure because `AudioService` is absent.**
- [ ] **Step 3: Implement the smallest Android local-player adapter and spike screen; acknowledge the tap immediately and ignore taps while active.**
- [ ] **Step 4: Run unit/assemble tasks, install on the primary tablet, and record ten tap-to-start measurements plus interruption behavior in `docs/05-quality/evidence/spike-audio.md`.**
- [ ] **Step 5: Commit as `spike: prove bundled tap to audio path`.**

### Task 1.2: Prove short speech-like activity detection without writing child audio

**Files:**
- Create: `shared/src/commonMain/kotlin/com/miozira/services/microphone/SpeechAttemptDetector.kt`
- Create: `shared/src/commonMain/kotlin/com/miozira/services/microphone/EnergyAttemptClassifier.kt`
- Create: `shared/src/androidMain/kotlin/com/miozira/services/microphone/AndroidSpeechAttemptDetector.kt`
- Test: `shared/src/commonTest/kotlin/com/miozira/services/microphone/EnergyAttemptClassifierTest.kt`
- Test: `shared/src/androidInstrumentedTest/kotlin/com/miozira/services/microphone/ChildAudioPrivacyTest.kt`

**Interfaces:**
- Produces: `start(SpeechAttemptRequest, SpeechAttemptListener): AppResult<SpeechAttemptHandle>`, `stop(handle)`, and `stopAll()`.
- Produces: only `ATTEMPT_DETECTED`, `NO_ATTEMPT_DETECTED`, or `MICROPHONE_UNAVAILABLE`; PCM remains inside the adapter/classifier call stack.

- [ ] **Step 1: Add deterministic frame tests: ambient frames return no attempt, 250 ms sustained energy above a measured noise floor returns attempt, and one impulse does not.**
- [ ] **Step 2: Run the classifier test; expect failure because the classifier is absent.**
- [ ] **Step 3: Implement configurable 2,500 ms capture, 20–40 ms frames, relative-noise RMS gating, one terminal event, prompt release on stop/background, and no file/path API.**
- [ ] **Step 4: On the primary tablet test quiet speech, silence, tap noise, adult speech, background, and rotation; inspect app-private storage before/after and record results in `docs/05-quality/evidence/spike-microphone.md`.**
- [ ] **Step 5: Commit as `spike: prove memory-only attempt detection`.**

### Task 1.3: Prove one idempotent evidence record survives restart

**Files:**
- Create: `shared/src/commonMain/kotlin/com/miozira/data/db/MioziraDatabase.kt`
- Create: `shared/src/commonMain/kotlin/com/miozira/data/db/entity/SpikeInteractionEntity.kt`
- Create: `shared/src/commonMain/kotlin/com/miozira/data/db/dao/SpikeInteractionDao.kt`
- Create: `shared/src/androidMain/kotlin/com/miozira/data/db/DatabaseFactory.android.kt`
- Test: `shared/src/androidInstrumentedTest/kotlin/com/miozira/data/db/SpikePersistenceTest.kt`

**Interfaces:**
- Consumes: `SessionId`, `InteractionId`, exposure-threshold event, and semantic attempt result from Tasks 1.1–1.2.
- Produces: `commitSpikeEvidence(interactionId, exposureCommitted, attemptState)` with a unique interaction key and observable persisted row.

- [ ] **Step 1: Add a Room test that commits the same interaction twice and asserts one row, one exposure, and one attempt state.**
- [ ] **Step 2: Run the Android database test; expect failure because the database is absent.**
- [ ] **Step 3: Implement the minimal Room KMP entity/DAO/transaction and wire threshold/attempt completion into the spike.**
- [ ] **Step 4: Complete one interaction, force-stop/relaunch, and assert the row remains; record schema and restart evidence in `docs/05-quality/evidence/spike-persistence.md`.**
- [ ] **Step 5: Commit as `spike: prove Room evidence persistence`.**

### Task 1.4: Gate the stack with rotation, lifecycle, accessibility, and offline evidence

**Files:**
- Create: `shared/src/commonMain/kotlin/com/miozira/presentation/spike/SpikeInteractionState.kt`
- Test: `shared/src/commonTest/kotlin/com/miozira/presentation/spike/SpikeIdentityTest.kt`
- Create: `docs/decisions/ADR-0001-spike-result.md`

**Interfaces:**
- Consumes: the integrated spike from Tasks 1.1–1.3.
- Produces: a stable `sessionId`/`interactionId` across configuration recreation and an explicit `GO_KMP` or `FALLBACK_NATIVE_ANDROID` result.

- [ ] **Step 1: Add a recreation test asserting the same active IDs survive rotation and a callback with a different interaction ID cannot mutate evidence.**
- [ ] **Step 2: Run the test; expect failure until identity is state-owned rather than Composable-owned.**
- [ ] **Step 3: Move identity to the lifecycle-aware controller/state holder and stop audio/mic on background without fabricating completion.**
- [ ] **Step 4: Test portrait↔landscape, background/foreground, force-stop/restart, TalkBack focus, and a full interaction in airplane mode on the primary tablet; evaluate every ADR failure criterion.**
- [ ] **Step 5: Record the evidence and architecture decision. If fallback is selected, change only stack-specific Phase 0 paths before continuing; preserve every interface and test expectation in this plan. Commit as `docs: decide stack from mandatory spike`.**

**Earliest meaningful real-tablet install:** Task 1.1 is the first engineering-meaningful install: a child-sized apple target reliably produces local audio. It is not yet a family-test build. The first child-meaningful mini-experience is Task 2.1; the first content-complete child build is Task 2.2.

---

## Phase 2 — Static child vertical slice

**Goal:** Deliver a calm, content-backed child loop where pictures and approved local words work for all 16 pairs without persistence or adaptation.

**Why this phase exists:** It validates comprehension, content packaging, audio quality, orientation, and visual interaction before database/session complexity can obscure failures.

**Files/modules likely involved:** content models/repository, manifest/resources, child UI state/screen, production audio source lookup, content validation tests.

**Automated tests:** manifest cardinality and referential integrity, required-asset existence, pair switching, no-overlap/replay semantics, Compose orientation smoke tests.

**Manual tests:** fluent-speaker audio review, child-sized touch, visual comfort, English/Tamil clarity, portrait/landscape, speaker volume.

**Acceptance criteria:** AC-B02 static portion, AC-B03 all 16 pairs, AC-H01, AC-H02, AC-H03, content acceptance checks, no prohibited mechanics.

**Exit criteria:** On a real tablet, any of 16 pair cards can be shown and tapped; canonical audio is local, prompt-free, responsive, and non-overlapping in both orientations.

**Dependencies:** Phase 1 GO/fallback decision.

**Risks:** unapproved placeholder assets becoming permanent, Tamil rendered as a reading requirement, UI navigation becoming a game, content lookup coupled to physical filenames.

**Explicit things NOT to build:** Room history, adaptive selection, microphone invitation, parent gate, achievements, quizzes, translation chaining, remote asset loader.

### Task 2.1: Make one production-shaped apple pair work end to end

**Files:**
- Create: `shared/src/commonMain/kotlin/com/miozira/content/ContentModels.kt`
- Create: `shared/src/commonMain/kotlin/com/miozira/content/ContentRepository.kt`
- Create: `shared/src/commonMain/kotlin/com/miozira/presentation/child/ChildSessionUiState.kt`
- Create: `shared/src/commonMain/kotlin/com/miozira/presentation/child/ChildSessionScreen.kt`
- Test: `shared/src/commonTest/kotlin/com/miozira/content/ContentManifestTest.kt`

**Interfaces:**
- Produces: strongly typed `ConceptId`, `LanguageId`, `PairId`, `ConceptLanguagePair`, `VisualAssetRef`, and `AudioAssetRef`.
- Produces: `ChildSessionUiState.Ready(pair, isReplayEnabled)` and a picture-first tap target.

- [ ] **Step 1: Add a manifest test asserting `apple:en` and `apple:ta` resolve to one shared visual and two distinct local canonical audio IDs.**
- [ ] **Step 2: Run the test; expect failure because the production content repository does not exist.**
- [ ] **Step 3: Implement logical resource lookup and the calm full-screen card using locked colors; keep written labels out of the child’s required path.**
- [ ] **Step 4: Run common/Compose tests and install the two-language apple build; verify tap acknowledgement, local playback, replay after completion, and both orientations.**
- [ ] **Step 5: Commit as `feat: add production apple tap-hear slice`.**

### Task 2.2: Freeze and validate all eight concepts across both languages

**Files:**
- Create: `shared/src/commonMain/composeResources/files/content/manifest-v1.json`
- Create: `shared/src/commonMain/composeResources/drawable/concept_*.webp`
- Create: `shared/src/commonMain/composeResources/files/audio/word_*_v1.m4a`
- Test: `shared/src/commonTest/kotlin/com/miozira/content/PrototypeContentTest.kt`
- Create: `docs/05-quality/evidence/content-v1-review.md`

**Interfaces:**
- Consumes: logical content contracts and child card from Task 2.1.
- Produces: exactly 8 concepts, 2 languages, 16 enabled pairs, 8 visuals, and 16 reviewed canonical clips under content version `prototype-0.1-v1`.

- [ ] **Step 1: Add tests asserting exact concept/language sets, unique pair IDs, 16 enabled pairs, resolvable visual/audio assets, Tamil `cup = கப்`, and `water = தண்ணீர்`.**
- [ ] **Step 2: Run tests against the partial manifest; expect cardinality and missing-asset failures.**
- [ ] **Step 3: Add approved resources and a simple non-adaptive next/previous test harness used only for content review; do not expose scores or progress.**
- [ ] **Step 4: Run validation and have a fluent Tamil reviewer plus English reviewer sign the asset/lexical/loudness checklist; test every pair on the primary tablet.**
- [ ] **Step 5: Commit as `content: add reviewed Prototype 0.1 pair set`.**

---

## Phase 3 — Local persistence

**Goal:** Persist content identity, sessions, interactions, evidence, derived pair state, observations, and settings locally with idempotent transactions.

**Why this phase exists:** Later session and adaptive behavior require trustworthy evidence. Persistence is introduced through user-visible outcomes—restart retention and duplicate-proof commits—not as a detached database layer.

**Files/modules likely involved:** Room entities/DAOs/database, database factory, repositories, DataStore settings, content seed service, recovery service.

**Automated tests:** schema/foreign keys/indexes, idempotent seed, exposure/replay/attempt transaction, session recovery, process restart, pair-state rebuild, reset transaction, DataStore persistence.

**Manual tests:** complete interactions, force-stop/restart, inspect parent-neutral local state through debug diagnostics, uninstall/reinstall behavior.

**Acceptance criteria:** AC-B04 evidence integrity foundation, app-private DB, 16 pairs remain available, no analytics/raw-audio columns, UI remains main-safe.

**Exit criteria:** A valid exposure/replay/attempt is committed once, survives restart, and can rebuild pair state; duplicate callbacks do not change totals.

**Dependencies:** Phase 2 content manifest; Phase 1 Room proof.

**Risks:** mirroring an event-sourcing system, storing derived truth without rebuild, weak foreign keys, exposing DAOs to presentation, blocking main thread.

**Explicit things NOT to build:** cloud sync, accounts/profiles, analytics event table, raw error log table, child audio/transcript columns, generic key-value domain storage, export UI.

### Task 3.1: Seed the specified Room schema idempotently

**Files:**
- Replace: `shared/src/commonMain/kotlin/com/miozira/data/db/entity/SpikeInteractionEntity.kt`
- Create: `shared/src/commonMain/kotlin/com/miozira/data/db/entity/{ContentVersion,Concept,Language,ConceptLanguagePair,Session,Interaction,PairLearningState,RealWorldObservation,FamilyRecording}Entity.kt`
- Create: `shared/src/commonMain/kotlin/com/miozira/data/db/dao/ContentDao.kt`
- Modify: `shared/src/commonMain/kotlin/com/miozira/data/db/MioziraDatabase.kt`
- Test: `shared/src/androidInstrumentedTest/kotlin/com/miozira/data/db/SchemaAndSeedTest.kt`

**Interfaces:**
- Consumes: `manifest-v1.json` and stable logical IDs.
- Produces: specified tables, foreign keys/indexes, schema export, and `seedContent(version): AppResult<Unit>`.

- [ ] **Step 1: Add a fresh-database test that seeds twice and asserts 8 concepts, 2 languages, 16 unique pairs, 16 `NEW` pair states, and no duplicate rows.**
- [ ] **Step 2: Run the test; expect failure against the spike schema.**
- [ ] **Step 3: Implement the privacy-minimal Room entities/DAOs and a single seed transaction matching `database.md` fields and stable enum strings.**
- [ ] **Step 4: Run schema/seed/foreign-key tests and export the initial schema for future migration tests.**
- [ ] **Step 5: Commit as `feat: seed Prototype 0.1 Room schema`.**

### Task 3.2: Commit one completed interaction atomically and idempotently

**Files:**
- Create: `shared/src/commonMain/kotlin/com/miozira/data/repository/RoomLearningRepository.kt`
- Create: `shared/src/commonMain/kotlin/com/miozira/data/repository/RoomSessionRepository.kt`
- Test: `shared/src/androidInstrumentedTest/kotlin/com/miozira/data/repository/InteractionCommitTest.kt`

**Interfaces:**
- Produces: `commitExposure(interactionId, committedAt)`, `incrementReplay(interactionId)`, `recordAttempt(interactionId, attemptState, recordedAt)`, and `completeInteraction(interactionId, result)` returning `AppResult`.
- Guarantees: one scheduled exposure per interaction; replay never increments exposure; `NOT_MEASURED` is not silently converted to no attempt.

- [ ] **Step 1: Add a transaction test that sends duplicate threshold/completion/attempt callbacks and asserts one exposure, exact replay total, one terminal attempt value, and updated pair summary.**
- [ ] **Step 2: Run the test; expect failure because repositories are absent.**
- [ ] **Step 3: Implement narrow DAOs and one database transaction that finalizes interaction fields and pair summary without exposing Room types above data.**
- [ ] **Step 4: Run repository tests including interruption before/after threshold, planned-but-unseen interaction, and concurrent duplicate commits.**
- [ ] **Step 5: Commit as `feat: persist interaction evidence idempotently`.**

### Task 3.3: Restore settings and interrupted sessions after process death

**Files:**
- Create: `shared/src/commonMain/kotlin/com/miozira/data/repository/DataStoreSettingsRepository.kt`
- Create: `shared/src/commonMain/kotlin/com/miozira/data/repository/RoomRecoveryService.kt`
- Test: `shared/src/androidInstrumentedTest/kotlin/com/miozira/data/repository/RecoveryTest.kt`

**Interfaces:**
- Produces: `SettingsRepository` methods for setup, starting language, and speech-attempt enablement.
- Produces: `recoverInterruptedSessions(): AppResult<RecoverySummary>` that closes stale active sessions without fabricating exposure/attempt evidence.

- [ ] **Step 1: Add tests that restart with one complete and one incomplete interaction; assert only real evidence survives and the interrupted session closes with `APP_EXITED` or `ERROR_RECOVERY`.**
- [ ] **Step 2: Run tests; expect failure because recovery/settings implementations are absent.**
- [ ] **Step 3: Implement DataStore preferences and startup recovery using `Clock`; keep permission truth in the OS rather than DataStore.**
- [ ] **Step 4: Run restart/recovery tests and manually force-stop during READY, playback-before-threshold, playback-after-threshold, and listening.**
- [ ] **Step 5: Commit as `feat: recover local session state safely`.**

---

## Phase 4 — Session state machine

**Goal:** Make the full child interaction an explicit, deterministic state machine coordinated by stable identity.

**Why this phase exists:** Audio, microphone, lifecycle, and persistence callbacks can otherwise corrupt evidence or trap the child. A pure reducer makes normal silence, stopping, errors, and stale callbacks testable.

**Files/modules likely involved:** interaction state/event/effect types, reducer, session engine, controller, fake audio/mic, child UI rendering.

**Automated tests:** every valid/invalid transition, happy path, mic-disabled/unavailable/silence, replay, parent stop, background, stale callbacks, duplicate effects, session length.

**Manual tests:** tap/replay/stop naturally; rotate/background at every state; verify no dead ends, forced retry, blame, or evaluation.

**Acceptance criteria:** AC-B02 full state path without real mic, AC-H08 child safety, AC-H10 state integrity, exposure only at threshold.

**Exit criteria:** Fake-driven and real-audio child sessions progress from start to end, survive lifecycle changes, and preserve evidence exactly once.

**Dependencies:** Phase 3 repositories and Phase 2 child card/audio.

**Risks:** putting effects inside Composables, treating recomposition as an event, accepting callbacks without IDs, conflating no attempt with failure, auto-advancing on playback start.

**Explicit things NOT to build:** adaptive policy (use deterministic fixture order), production mic capture, parent dashboards, child-facing technical errors, retry loops, animations unrelated to cause/effect.

### Task 4.1: Encode legal child states, events, and effects as a pure reducer

**Files:**
- Create: `shared/src/commonMain/kotlin/com/miozira/domain/session/InteractionState.kt`
- Create: `shared/src/commonMain/kotlin/com/miozira/domain/session/InteractionReducer.kt`
- Test: `shared/src/commonTest/kotlin/com/miozira/domain/session/InteractionReducerTest.kt`

**Interfaces:**
- Produces states `SESSION_STARTING`, `PRESENTING`, `READY`, `PLAYING_WORD`, `INVITING`, `LISTENING`, `RESPONDING`, `TRANSITIONING`, `IDLE`, `SESSION_ENDING`, `ENDED`.
- Produces `reduce(state, event): Transition(newState, effects)`; effects include play, commit exposure, start/stop listening, complete interaction, and end session.

- [ ] **Step 1: Add table-driven tests for the documented happy, mic-disabled, silence, parent-stop, and background paths plus invalid transitions.**
- [ ] **Step 2: Run the reducer tests; expect failure because the state algebra is absent.**
- [ ] **Step 3: Implement the smallest exhaustive pure reducer; invalid events leave evidence unchanged and return a diagnostic effect unavailable to child UI.**
- [ ] **Step 4: Run all transition tests and mutation/property checks proving one input cannot emit duplicate commit/listen effects.**
- [ ] **Step 5: Commit as `feat: define child interaction state machine`.**

### Task 4.2: Coordinate audio, persistence, and fixture-order next items

**Files:**
- Create: `shared/src/commonMain/kotlin/com/miozira/domain/session/LearningSessionEngine.kt`
- Create: `shared/src/commonMain/kotlin/com/miozira/domain/session/ChildSessionController.kt`
- Create: `shared/src/commonTest/kotlin/com/miozira/fakes/FakeAudioService.kt`
- Test: `shared/src/commonTest/kotlin/com/miozira/domain/session/ChildSessionControllerTest.kt`

**Interfaces:**
- Consumes: reducer, `AudioService`, content/session/learning repositories, `Clock`, and a deterministic fixture selector.
- Produces: `StateFlow<ChildSessionUiState>` and the API methods specified in `api.md`.

- [ ] **Step 1: Add a controller test: tap → fake started → threshold → completed → no-mic response → next pair; assert one persisted exposure and no DB calls from UI.**
- [ ] **Step 2: Run the test; expect failure because the controller/engine are absent.**
- [ ] **Step 3: Implement effect execution behind the controller, translating service failures to a calm continue/stop state while retaining technical diagnostics only in debug builds.**
- [ ] **Step 4: Run controller, fake-audio, repository, rapid-tap, replay, and failure-before/after-threshold tests.**
- [ ] **Step 5: Commit as `feat: run persisted child sessions with fake scheduling`.**

### Task 4.3: Make lifecycle and stale callbacks harmless

**Files:**
- Modify: `shared/src/commonMain/kotlin/com/miozira/domain/session/ChildSessionController.kt`
- Modify: `androidApp/src/main/kotlin/com/miozira/MainActivity.kt`
- Test: `shared/src/commonTest/kotlin/com/miozira/domain/session/StaleCallbackTest.kt`
- Test: `androidApp/src/androidTest/kotlin/com/miozira/SessionLifecycleTest.kt`

**Interfaces:**
- Consumes: callbacks tagged with stable session/interaction IDs.
- Produces: background/foreground/rotation behavior that cancels resources, preserves or safely restarts presentation, and never invents a no-attempt result.

- [ ] **Step 1: Add tests delivering old audio and mic terminal callbacks after a new interaction begins; assert state and evidence are unchanged.**
- [ ] **Step 2: Run tests; expect failure until every callback is identity-checked.**
- [ ] **Step 3: Add identity guards and lifecycle forwarding; make stop/cancel idempotent and keep IDs outside Composable recreation.**
- [ ] **Step 4: Run a rotation/background matrix for every state and a 25-rotation stress test on the primary tablet.**
- [ ] **Step 5: Commit as `fix: reject stale session callbacks across lifecycle`.**

---

## Phase 5 — Adaptive scheduling

**Goal:** Replace fixture ordering with the deterministic, local Prototype 0.1 scheduling policy for 16 independent pairs.

**Why this phase exists:** Adaptation should consume already trustworthy evidence, not define it. Implementing it after the session slice makes its visible effect testable without entangling UI, Room, and algorithms.

**Files/modules likely involved:** adaptive policy/version, pure pair-state reducer, selector, session engine integration, FakeClock/FakeRandomizer, persistence rebuild.

**Automated tests:** NEW/EMERGING/FAMILIAR/RESTING, due timing, real-world use, language isolation, rest progression, no hammering, max two new, 4–8 mix, same-session spacing, deterministic tie-breaks, rebuild.

**Manual tests:** debug time-advance scenario over simulated seven days; child session ordering review in both language blocks; no noticeable selection stall.

**Acceptance criteria:** AC-H05; pair independence; due items reappear; RESTING withheld; real-world evidence affects exact pair only; selector is deterministic under fake time/randomness.

**Exit criteria:** Persisted evidence produces the specified next-item decisions and pair states after restart/rebuild; a real session uses adaptive output with no UI logic duplication.

**Dependencies:** Phase 4 session engine and Phase 3 persistence.

**Risks:** inventing intervals, encoding randomness that makes tests flaky, allowing replay/no-attempt to dominate learning evidence, selecting new items too aggressively, cross-language leakage.

**Explicit things NOT to build:** ML/recommendation service, remote config, opaque scoring, child-visible mastery level, per-device tuning, additional concepts/languages.

### Task 5.1: Update one pair’s learning state from evidence and time

**Files:**
- Create: `shared/src/commonMain/kotlin/com/miozira/domain/adaptive/AdaptivePolicy.kt`
- Create: `shared/src/commonMain/kotlin/com/miozira/domain/adaptive/PairStateReducer.kt`
- Create: `shared/src/commonTest/kotlin/com/miozira/core/time/FakeClock.kt`
- Test: `shared/src/commonTest/kotlin/com/miozira/domain/adaptive/PairStateReducerTest.kt`

**Interfaces:**
- Produces: versioned `AdaptivePolicy` containing only constants approved in `docs/01-learning/adaptive-engine.md`.
- Produces: `recalculate(pairId, evidence, now, policy): PairLearningState` for `NEW`, `EMERGING`, `FAMILIAR`, and `RESTING`.

- [ ] **Step 1: Add named tests for first valid exposure, emerging due interval, familiar spacing, no-attempt weak evidence, replay evidence, rest entry/progression, recognition, spontaneous use, and exact-pair language isolation.**
- [ ] **Step 2: Run tests; expect failure because the reducer/policy are absent.**
- [ ] **Step 3: Transcribe the frozen specification constants into `AdaptivePolicy(version = "prototype-0.1")`; implement pure state calculation without Room or platform time access.**
- [ ] **Step 4: Run adaptive unit tests with FakeClock across a simulated seven-day history and compare rebuild output to incrementally saved output.**
- [ ] **Step 5: Commit as `feat: calculate adaptive pair state deterministically`.**

### Task 5.2: Select a calm 4–8 item session and integrate it

**Files:**
- Create: `shared/src/commonMain/kotlin/com/miozira/domain/adaptive/AdaptiveEngine.kt`
- Modify: `shared/src/commonMain/kotlin/com/miozira/domain/session/LearningSessionEngine.kt`
- Test: `shared/src/commonTest/kotlin/com/miozira/domain/adaptive/AdaptiveSelectionTest.kt`
- Test: `shared/src/commonTest/kotlin/com/miozira/domain/session/AdaptiveSessionIntegrationTest.kt`

**Interfaces:**
- Produces: `selectNext(AdaptiveSelectionContext): AppResult<SelectionDecision>` with persisted `SelectionReason`.
- Consumes: recent interactions, all pair states, active language block, `Clock`, and deterministic `Randomizer` only for specified tie-breaking.

- [ ] **Step 1: Add scenario tests for due priority, active items, maximum two new pairs, easy opener, session closer, same-session spacing, language block, insufficient alternatives, and 4–8 planned interactions.**
- [ ] **Step 2: Run tests; expect failure while the fixture selector remains active.**
- [ ] **Step 3: Implement selector rules from the adaptive spec, persist selection reason/config version, and make the session engine—not presentation—request the next decision.**
- [ ] **Step 4: Run all adaptive/session/rebuild tests and execute a debug seven-day time simulation; verify selection remains main-safe and fast.**
- [ ] **Step 5: Commit as `feat: drive child sessions with local adaptive selection`.**

---

## Phase 6 — Microphone attempt detection

**Goal:** Turn the spike detector into an optional, permission-aware production path that records only weak semantic attempt evidence.

**Why this phase exists:** The core session is already complete without a microphone. This keeps the highest privacy/lifecycle feature removable and prevents it from becoming a prerequisite for learning.

**Files/modules likely involved:** production classifier config, Android `AudioRecord` adapter, permission service/settings, controller effects, mic UI state, calibration evidence.

**Automated tests:** signal vectors, exactly one terminal result, timeout/cancel/stopAll, permission decision table, mic-disabled/unavailable paths, stale callbacks, no false evidence on background/rotation, no file creation.

**Manual tests:** primary/minimum-SDK devices at near/normal/far distance, quiet/fan/TV/tap/adult-speech scenarios, screen lock/background, repeated windows, permission denial/revocation/permanent denial.

**Acceptance criteria:** AC-B05, AC-B06, AC-H06, AC-H10; microphone remains optional; only semantic states persist; no transcript, score, raw file, continuous capture, or network use.

**Exit criteria:** A frozen versioned detector config produces useful-enough results on the primary tablet, stops promptly under every lifecycle event, and can be disabled without changing session completion.

**Dependencies:** Phase 4 controller, Phase 5 engine, Phase 1 spike measurements.

**Risks:** false positives from taps/speaker leakage, false negatives harming perceived invitation, permission nagging, duplicate windows, buffers escaping the adapter, over-tuning to one room.

**Explicit things NOT to build:** `SpeechRecognizer`, ASR, phonemes, expected-word comparison, confidence score, speaker identification, child audio files, background/continuous listening, downloadable VAD model.

### Task 6.1: Productionize the memory-only classifier and capture adapter

**Files:**
- Modify: `shared/src/commonMain/kotlin/com/miozira/services/microphone/EnergyAttemptClassifier.kt`
- Create: `shared/src/commonMain/kotlin/com/miozira/services/microphone/SpeechAttemptConfig.kt`
- Modify: `shared/src/androidMain/kotlin/com/miozira/services/microphone/AndroidSpeechAttemptDetector.kt`
- Test: `shared/src/commonTest/kotlin/com/miozira/services/microphone/EnergyAttemptClassifierTest.kt`
- Test: `shared/src/androidInstrumentedTest/kotlin/com/miozira/services/microphone/MicrophoneLifecycleTest.kt`

**Interfaces:**
- Produces: `SpeechAttemptConfig(version, windowDurationMs, frameDurationMs, noiseCalibrationMs, thresholdMargin, minimumActiveDurationMs, maximumSilenceGapMs)`.
- Guarantees: one active child detector, one terminal semantic callback, idempotent `stop`/`stopAll`, and no public PCM/path property.

- [ ] **Step 1: Expand deterministic fixtures for silence, steady ambient, short impulse, sustained speech-like energy, gaps, clipping, and end-of-window boundary; assert one terminal result.**
- [ ] **Step 2: Run common and lifecycle tests; expect failures for unhandled production cases.**
- [ ] **Step 3: Implement the smallest relative-noise classifier and capture owner; overwrite/release buffers after each window and cancel on background/screen lock.**
- [ ] **Step 4: Run classifier/lifecycle/privacy/stress tests for 100 sequential windows; inspect app files and resource use.**
- [ ] **Step 5: Commit as `feat: harden memory-only speech attempt detector`.**

### Task 6.2: Put microphone consent under adult control

**Files:**
- Create: `shared/src/androidMain/kotlin/com/miozira/services/permissions/AndroidPermissionService.kt`
- Create: `shared/src/commonMain/kotlin/com/miozira/services/permissions/PermissionDecision.kt`
- Test: `shared/src/commonTest/kotlin/com/miozira/services/permissions/PermissionDecisionTest.kt`
- Test: `androidApp/src/androidTest/kotlin/com/miozira/MicrophonePermissionFlowTest.kt`

**Interfaces:**
- Produces: `PermissionService.getMicrophonePermissionState()`, `requestMicrophonePermission()`, and `openMicrophoneSettings()`.
- Produces: pure decision mapping from product setting + OS state to `OPEN_WINDOW`, `NOT_MEASURED`, or `MICROPHONE_UNAVAILABLE`.

- [ ] **Step 1: Add table-driven tests for setting off, granted, denied, permanently denied, revoked, and unavailable hardware exactly matching `permissions.md`.**
- [ ] **Step 2: Run tests; expect failure because permission decisions are absent.**
- [ ] **Step 3: Implement the platform service and adult-intent request entry point; never request on launch or automatically re-prompt after denial.**
- [ ] **Step 4: Run UI/instrumented permission tests on minimum and current API emulators, including deny, don’t ask again, revoke in settings, background, and child flow with mic off.**
- [ ] **Step 5: Commit as `feat: make microphone permission optional and adult-controlled`.**

### Task 6.3: Integrate, calibrate, and freeze the attempt path

**Files:**
- Modify: `shared/src/commonMain/kotlin/com/miozira/domain/session/ChildSessionController.kt`
- Modify: `shared/src/commonMain/kotlin/com/miozira/presentation/child/ChildSessionScreen.kt`
- Test: `shared/src/commonTest/kotlin/com/miozira/domain/session/MicrophoneSessionIntegrationTest.kt`
- Create: `docs/05-quality/evidence/microphone-config-v1.md`

**Interfaces:**
- Consumes: `INVITING → LISTENING → RESPONDING` effects and semantic detector results.
- Produces: persisted `AttemptState` for the active interaction only; silence and unavailable mic continue without retry or evaluation.

- [ ] **Step 1: Add integration tests for detected, silence, disabled, unavailable, timeout, stale result, background cancellation, and rotation without duplicate window.**
- [ ] **Step 2: Run tests; expect failure while the controller skips production mic effects.**
- [ ] **Step 3: Wire the detector behind permission/setting decisions and render only a quiet temporary listening state plus neutral response.**
- [ ] **Step 4: Execute the documented distance/noise device calibration, record false-positive/false-negative observations, choose RMS vs a small native VAD, and freeze exact values/version before Phase 11.**
- [ ] **Step 5: Commit as `feat: integrate optional speaking-attempt evidence`.**

---

## Phase 7 — Parent experience

**Goal:** Give an adult protected access to a non-evaluative summary, transfer marking, one real-world suggestion, microphone setting, and reset.

**Why this phase exists:** Real-world transfer and adult control are core product outcomes, but they should consume stable child/session data rather than dictate its early architecture.

**Files/modules likely involved:** parent gate/controller/screens, observation repository/service, suggestion selection, settings, reset service, navigation owned by the app shell.

**Automated tests:** two-second gate gesture, accidental-tap resistance, parent state aggregation, idempotent observation save, exact-pair transfer effect, suggestion eligibility, permission setting, reset success/failure/idempotency.

**Manual tests:** one-handed adult gate use, child casual-tap resistance, large text, portrait/landscape, TalkBack, reset confirmation clarity, suggestion burden.

**Acceptance criteria:** AC-H07 parent area, AC-B08 reset, AC-H09 parent accessibility, one quiet context-appropriate suggestion after meaningful session, no evaluative wording.

**Exit criteria:** Adult can enter, understand activity without scores, mark exact-pair recognition/use, manage mic, view one suggestion, and reset all user data safely.

**Dependencies:** Phases 3–6.

**Risks:** making the gate an enticing child puzzle, presenting exposure counts as achievement, duplicate observations from double taps, destructive reset ambiguity, excessive parent workflow.

**Explicit things NOT to build:** PIN/accounts, multi-profile dashboards, charts/grades/mastery scores, push prompts, detailed behavior diary, quizzing tools, editable curriculum, remote backup.

### Task 7.1: Add the discreet parent gate and non-evaluative summary

**Files:**
- Create: `shared/src/commonMain/kotlin/com/miozira/presentation/parent/ParentGate.kt`
- Create: `shared/src/commonMain/kotlin/com/miozira/presentation/parent/ParentController.kt`
- Create: `shared/src/commonMain/kotlin/com/miozira/presentation/parent/ParentSummaryScreen.kt`
- Test: `shared/src/commonTest/kotlin/com/miozira/presentation/parent/ParentGateTest.kt`
- Test: `androidApp/src/androidTest/kotlin/com/miozira/ParentEntryTest.kt`

**Interfaces:**
- Produces: approximately two-second long-press on a discreet corner hotspot, cancelled by early release/movement.
- Produces: high-level active concepts/encounters using neutral language and no child score.

- [ ] **Step 1: Add gate timing tests for short tap, interrupted hold, valid hold, repeated taps, and lifecycle cancellation; add UI assertions that prohibited score/streak copy is absent.**
- [ ] **Step 2: Run tests; expect failure because the parent entry flow is absent.**
- [ ] **Step 3: Implement the hidden hold gesture and parent navigation with ≥48dp parent controls, large-text support, and no visual lure in child mode.**
- [ ] **Step 4: Run UI tests and manually test entry with an adult plus casual child-style tapping in both orientations.**
- [ ] **Step 5: Commit as `feat: add protected parent summary`.**

### Task 7.2: Mark transfer, show one suggestion, manage mic, and reset

**Files:**
- Create: `shared/src/commonMain/kotlin/com/miozira/domain/transfer/RealWorldTransferService.kt`
- Create: `shared/src/commonMain/kotlin/com/miozira/data/repository/RoomRealWorldObservationRepository.kt`
- Create: `shared/src/commonMain/kotlin/com/miozira/domain/reset/ResetService.kt`
- Create: `shared/src/commonMain/kotlin/com/miozira/presentation/parent/ParentActionsScreen.kt`
- Test: `shared/src/commonTest/kotlin/com/miozira/domain/transfer/RealWorldTransferServiceTest.kt`
- Test: `shared/src/androidInstrumentedTest/kotlin/com/miozira/domain/reset/ResetServiceTest.kt`

**Interfaces:**
- Produces: exact-pair `RECOGNIZED` and `USED_SPONTANEOUSLY` observations with idempotent UI submission.
- Produces: at most one eligible suggestion after meaningful activity and `resetUserData(): AppResult<ResetSummary>`.

- [ ] **Step 1: Add tests proving observations affect only the selected language pair, duplicate taps save once, insignificant sessions emit no suggestion, and reset leaves 16 `NEW` pairs plus canonical assets.**
- [ ] **Step 2: Run tests; expect failure because transfer/reset services are absent.**
- [ ] **Step 3: Implement observation/suggestion flow, mic setting entry, explicit two-step reset confirmation, transactional DB clear, preference reset, and post-reset reseed.**
- [ ] **Step 4: Run transfer/adaptive/reset tests; manually verify the suggestion feels optional and a second reset is harmless.**
- [ ] **Step 5: Commit as `feat: add parent transfer and reset actions`.**

---

## Phase 8 — Family voice

**Goal:** Let an adult record, preview, save, play, replace, and delete one private family pronunciation per pair while canonical audio always remains available.

**Why this phase exists:** Family voice is valuable but combines microphone ownership, filesystem atomicity, privacy, recovery, and adult UX. It follows the simpler child detector and parent area so those boundaries already exist.

**Files/modules likely involved:** `FileStore`, family recorder adapter/service/repository, parent family-voice screen/controller, startup cleanup, audio source fallback.

**Automated tests:** exclusive recorder ownership, temp cleanup, preview/save/cancel, atomic replacement, metadata/file consistency, deletion, corrupt/missing file fallback, quota/low-space failure, background cancellation.

**Manual tests:** real parent recording quality/duration, preview/replace/delete, gallery/media visibility, background/lock/rotation, low volume, corrupted file fallback.

**Acceptance criteria:** AC-B09; private app storage; safe replacement; deletion; no public media entry; canonical fallback; family recording never mistaken for child attempt evidence.

**Exit criteria:** All family-voice actions are recoverable and adult-controlled on the primary tablet; no operation can remove or invalidate canonical audio.

**Dependencies:** Parent gate (Phase 7), mic ownership (Phase 6), Room metadata (Phase 3).

**Risks:** concurrent child/parent capture, deleting old good file before replacement commits, raw paths escaping FileStore, orphan temp files, backup leakage, family clip used as canonical learning evidence.

**Explicit things NOT to build:** child-controlled recording, audio sharing/export, cloud backup/sync, audio editing, multiple takes library, names in filenames, family clip scoring, automatic child-flow substitution unless explicitly resolved in OQ/audio policy.

### Task 8.1: Implement private record → preview → save/cancel

**Files:**
- Create: `shared/src/commonMain/kotlin/com/miozira/services/files/FileStore.kt`
- Create: `shared/src/androidMain/kotlin/com/miozira/services/files/AndroidPrivateFileStore.kt`
- Create: `shared/src/commonMain/kotlin/com/miozira/services/recording/FamilyRecordingService.kt`
- Create: `shared/src/androidMain/kotlin/com/miozira/services/recording/AndroidFamilyRecordingService.kt`
- Test: `shared/src/androidInstrumentedTest/kotlin/com/miozira/services/recording/FamilyRecordingLifecycleTest.kt`

**Interfaces:**
- Produces: `start(pairId)`, `stop(session)`, `cancel(session)`, `save(preview)`, `play(recordingId)`, and `delete(recordingId)`.
- Produces: opaque logical file IDs; temporary files live in app-private temp storage and are promoted only after parent confirmation.

- [ ] **Step 1: Add tests for start/stop/preview/save, cancel deletion, one active recorder, child-detector exclusion, background cancellation, random UUID filename, and no public media scan.**
- [ ] **Step 2: Run tests; expect failure because FileStore/recorder implementations are absent.**
- [ ] **Step 3: Implement app-private temp capture, duration/space checks, preview playback, atomic promotion, Room metadata save, and startup cleanup of orphan temp files.**
- [ ] **Step 4: Run lifecycle/privacy tests and record/preview/save on the primary tablet; confirm only semantic child-attempt data and the explicit adult family file exist.**
- [ ] **Step 5: Commit as `feat: record private family pronunciations`.**

### Task 8.2: Make replacement, deletion, and canonical fallback failure-safe

**Files:**
- Create: `shared/src/commonMain/kotlin/com/miozira/data/repository/RoomFamilyRecordingRepository.kt`
- Modify: `shared/src/androidMain/kotlin/com/miozira/services/recording/AndroidFamilyRecordingService.kt`
- Modify: `shared/src/androidMain/kotlin/com/miozira/services/audio/AndroidAudioService.kt`
- Test: `shared/src/androidInstrumentedTest/kotlin/com/miozira/services/recording/FamilyRecordingRecoveryTest.kt`

**Interfaces:**
- Consumes: one recording metadata row per pair and opaque FileStore IDs.
- Guarantees replacement sequence: new temp → preview → promote → metadata update → old-file delete; failure before metadata update preserves old recording.

- [ ] **Step 1: Add fault-injection tests at every replacement step, delete twice, missing/corrupt file, orphan active file, and canonical playback after each failure.**
- [ ] **Step 2: Run tests; expect failures against the basic save path.**
- [ ] **Step 3: Implement transaction-aware replacement/recovery and make audio resolution fall back to bundled canonical source without changing scheduled exposure semantics.**
- [ ] **Step 4: Run recovery/reset/storage tests and manually replace/delete/corrupt a recording on device; verify the canonical word still plays.**
- [ ] **Step 5: Commit as `fix: make family voice replacement recoverable`.**

### Task 8.3: Add the adult family-voice management screen

**Files:**
- Create: `shared/src/commonMain/kotlin/com/miozira/presentation/parent/FamilyRecordingController.kt`
- Create: `shared/src/commonMain/kotlin/com/miozira/presentation/parent/FamilyVoiceScreen.kt`
- Test: `shared/src/commonTest/kotlin/com/miozira/presentation/parent/FamilyRecordingControllerTest.kt`
- Test: `androidApp/src/androidTest/kotlin/com/miozira/FamilyVoiceFlowTest.kt`

**Interfaces:**
- Consumes: parent gate, permission service, family recording service/repository.
- Produces: explicit record, stop, preview, save, replace, delete, and cancel states with parent-facing errors only.

- [ ] **Step 1: Add controller/UI tests for denied permission, record/preview/save, cancel, replace confirmation, delete confirmation, background, and service failure.**
- [ ] **Step 2: Run tests; expect failure because the management UI is absent.**
- [ ] **Step 3: Implement the smallest accessible adult flow; do not expose it in child mode and never auto-request permission without adult action.**
- [ ] **Step 4: Run parent UI tests and complete the entire flow in portrait/landscape with large text and TalkBack on the primary tablet.**
- [ ] **Step 5: Commit as `feat: add parent family-voice controls`.**

---

## Phase 9 — Privacy/security hardening

**Goal:** Turn privacy and offline promises into release-blocking build/runtime checks and verify storage/lifecycle boundaries under failure.

**Why this phase exists:** Privacy cannot rely on code review memory. Final dependencies, merged manifests, backup rules, logs, files, and lifecycle behavior must be checked after all features exist.

**Files/modules likely involved:** manifest/backup XML, permission allowlist, ProGuard/R8/logging policy, dependency audit, FileStore path checks, security/privacy tests, release configuration.

**Automated tests:** final merged manifest, dependency denylist, offline/no-network, no child audio/transcript/score artifact, backup exclusions, path traversal, reset, background capture, corrupt data recovery.

**Manual tests:** airplane mode full session/parent actions, device backup/extraction review, app-private/public storage inspection, logcat inspection, screen lock, uninstall/reinstall, signed release behavior.

**Acceptance criteria:** all privacy/security acceptance criteria, AC-B05/06/07/09, only justified permission, family backup behavior explicitly reviewed, no SDK violating scope.

**Exit criteria:** release-variant audit evidence shows no network permission/dependency, no child voice artifact, private family files, safe backup/reset/lifecycle, and understood signing.

**Dependencies:** Feature-complete Phases 2–8.

**Risks:** debug-only behavior hiding release leakage, transitive permissions/SDKs, family files included in OS backup, raw platform exceptions/paths in logs, incomplete reset under file failure.

**Explicit things NOT to build:** custom encryption without a documented threat need, security telemetry, account/auth system, network monitoring SDK, data export, remote crash reporting, device identifiers.

### Task 9.1: Make privacy/offline policy executable in CI

**Files:**
- Create: `androidApp/src/main/res/xml/backup_rules.xml`
- Create: `androidApp/src/main/res/xml/data_extraction_rules.xml`
- Modify: `androidApp/src/main/AndroidManifest.xml`
- Create: `androidApp/src/androidTest/kotlin/com/miozira/ReleasePrivacyPolicyTest.kt`
- Modify: `.github/workflows/ci.yml`
- Create: `docs/05-quality/evidence/privacy-release-audit.md`

**Interfaces:**
- Consumes: release merged manifest, runtime file tree, dependency report, backup configuration.
- Produces: CI failures for `INTERNET`, unexpected dangerous permissions, missing required assets/schema, prohibited SDK groups, or family-recording backup inclusion.

- [ ] **Step 1: Add release-policy tests that assert permission allowlist, no network-capable/prohibited SDK, no child-audio filename/path, and explicit family-recording backup exclusion.**
- [ ] **Step 2: Run against a controlled forbidden manifest/dependency fixture; expect a precise FAIL, then remove the fixture.**
- [ ] **Step 3: Configure backup/data-extraction rules, release dependency/manifest checks, and privacy-safe logging with no raw paths, PCM, transcript, or child-performance fields.**
- [ ] **Step 4: Run the release audit, a full airplane-mode session, and app-private/public storage plus logcat inspection; record exact build evidence.**
- [ ] **Step 5: Commit as `security: enforce offline privacy release policy`.**

### Task 9.2: Verify lifecycle, path, reset, and corruption boundaries

**Files:**
- Create: `shared/src/androidInstrumentedTest/kotlin/com/miozira/security/StorageBoundaryTest.kt`
- Create: `shared/src/androidInstrumentedTest/kotlin/com/miozira/security/CaptureLifecycleSecurityTest.kt`
- Modify: `shared/src/commonMain/kotlin/com/miozira/data/repository/RoomRecoveryService.kt`
- Modify: `shared/src/commonMain/kotlin/com/miozira/domain/reset/ResetService.kt`

**Interfaces:**
- Consumes: FileStore logical IDs, reset service, child detector, parent recorder, recovery service.
- Guarantees: no path escape, no background capture, canonical fallback on corruption, reset retry/report semantics, and no deletion of bundled assets.

- [ ] **Step 1: Add adversarial tests for `../` logical IDs, corrupt family file, corrupt cache/temp, interrupted reset, background/screen-lock capture, and database-open failure.**
- [ ] **Step 2: Run tests; expect failures where recovery/reporting is incomplete.**
- [ ] **Step 3: Implement the minimal boundary validation and recovery behaviors specified in storage/database docs; preserve old valid data when repair cannot complete safely.**
- [ ] **Step 4: Run all security, storage, reset, capture, repository, and privacy tests twice to prove idempotent recovery.**
- [ ] **Step 5: Commit as `security: harden local lifecycle and storage boundaries`.**

---

## Phase 10 — Device/performance/accessibility QA

**Goal:** Validate the release candidate on real hardware and supported emulators for responsiveness, stability, comfort, accessibility, orientation, and content quality.

**Why this phase exists:** Audio latency, mic sensitivity, child touch, TalkBack, thermal behavior, and visual comfort cannot be established by unit tests or compilation.

**Files/modules likely involved:** device test evidence, UI semantics/touch targets, reduced-motion behavior, performance harness, regression workflow, defect log.

**Automated tests:** child/parent UI smoke, ≥48dp parent targets, large text, reduced motion, rotation stress, 50–100 interactions, 100 mic windows, long history, low storage/memory where automatable.

**Manual tests:** primary tablet, API 36 emulator, minimum-SDK emulator, additional modest tablet if available; TalkBack; content listening; thermal/battery; child touch; portrait/landscape.

**Acceptance criteria:** all HIGH criteria; cold launch approximately ≤4s, warm ≤2s, tap acknowledgement ≤100ms, canonical audio ≤250ms normally; no freezes/leaks/heating/background workload; device/content/accessibility criteria pass.

**Exit criteria:** zero BLOCKER/HIGH defects, accepted reviewed MEDIUM/LOW list, complete evidence for the exact release-candidate build on the primary tablet.

**Dependencies:** Phase 9 hardened feature-complete build.

**Risks:** testing only one orientation/device, averaging away worst-case latency, fixing polish while ignoring evidence integrity, changing frozen config during QA, treating TalkBack automation as human review.

**Explicit things NOT to build:** new features, visual redesign, speculative performance framework, engagement animation, device-specific learning rules, post-QA architecture refactor without a blocker.

### Task 10.1: Run automated stress, performance, and orientation gates

**Files:**
- Create: `androidApp/src/androidTest/kotlin/com/miozira/ReleaseStressTest.kt`
- Create: `androidApp/src/androidTest/kotlin/com/miozira/ReleasePerformanceTest.kt`
- Modify: `.github/workflows/ci.yml`
- Create: `docs/05-quality/evidence/release-candidate-automated.md`

**Interfaces:**
- Consumes: release candidate and fixed test content/config versions.
- Produces: repeatable metrics/evidence for startup, tap acknowledgement, audio start, 100 interactions, 25 rotations, 100 mic windows, long history, and idle background.

- [ ] **Step 1: Add gates with explicit assertions for state/evidence counts, resource release, UI responsiveness, and specified latency targets; avoid flaky wall-clock assertions in common unit tests.**
- [ ] **Step 2: Run the harness on an emulator; expect it to expose any unbounded wait, leaked handle, or duplicate evidence before threshold tuning.**
- [ ] **Step 3: Fix only measured blockers in the owning component and keep performance probes out of child UI.**
- [ ] **Step 4: Run the regression suite on API 36 and minimum-SDK emulators, then the timing/stress subset on the primary physical tablet; record raw ranges and pass/fail.**
- [ ] **Step 5: Commit as `test: add Prototype 0.1 release stress gates`.**

### Task 10.2: Complete human device, accessibility, and content review

**Files:**
- Modify: `shared/src/commonMain/kotlin/com/miozira/presentation/child/ChildSessionScreen.kt`
- Modify: `shared/src/commonMain/kotlin/com/miozira/presentation/parent/*.kt`
- Create: `docs/05-quality/evidence/release-candidate-manual.md`
- Create: `docs/05-quality/evidence/known-issues.md`

**Interfaces:**
- Consumes: automated candidate from Task 10.1.
- Produces: signed manual matrix for audio/Tamil quality, touch, TalkBack, large text, reduced motion, color independence, orientation, volume, mic, recording, thermal/battery, and visual comfort.

- [ ] **Step 1: Execute each applicable `testing.md` and acceptance checklist row on named devices/build; record PASS/FAIL and evidence, not a general impression.**
- [ ] **Step 2: Classify failures as BLOCKER/HIGH/MEDIUM/LOW and reproduce each BLOCKER/HIGH with an automated test when practical.**
- [ ] **Step 3: Apply the smallest accessibility/performance/content-safe fixes; do not change adaptive or mic config without re-freezing and restarting affected evidence.**
- [ ] **Step 4: Rerun the full regression/manual matrix until BLOCKER/HIGH are zero and reviewers explicitly accept remaining MEDIUM/LOW issues.**
- [ ] **Step 5: Commit as `qa: record Prototype 0.1 device acceptance`.**

---

## Phase 11 — Family-test release

**Goal:** Freeze, build, install, dry-run, and hand off one traceable Prototype 0.1 build for the seven-day family test.

**Why this phase exists:** The family test must evaluate one stable product, not a moving implementation. Reproducible configuration, acceptance sign-off, installation instructions, stop conditions, and lightweight observation materials protect the validity and safety of the test.

**Files/modules likely involved:** release version/config, signing configuration, changelog, release evidence, family-test observation packet, install/rollback instructions.

**Automated tests:** full release regression, signed APK install/launch, content/config version assertions, clean-state seed, upgrade if applicable, final manifest/privacy audit.

**Manual tests:** clean install and dry-run on primary tablet, airplane mode, volume/permission/reset/recording, parent protocol rehearsal, observation form, stop/incident procedure.

**Acceptance criteria:** every BLOCKER and HIGH row passes; content/adaptive/mic frozen; primary tablet passes; exact build recorded; preconditions in family-test protocol met.

**Exit criteria:** one signed, checksummed build is installed on the primary tablet; release evidence and known issues are signed off; parent can follow the protocol without developer coaching.

**Dependencies:** Phase 10 sign-off.

**Risks:** last-minute feature change, debug build behavior, config drift, unclear build identity, mid-test updates, parent burden, continuing after child distress/privacy incident.

**Explicit things NOT to build:** new features/content/languages, mid-test tuning, analytics/telemetry, detailed behavioral diary, quotas, forced daily use, staged quizzes, release to a public store before the family test.

### Task 11.1: Freeze and produce the family-test candidate

**Files:**
- Modify: `gradle.properties`
- Modify: `shared/src/commonMain/kotlin/com/miozira/domain/adaptive/AdaptivePolicy.kt`
- Modify: `shared/src/commonMain/kotlin/com/miozira/services/microphone/SpeechAttemptConfig.kt`
- Modify: `CHANGELOG.md`
- Create: `docs/05-quality/evidence/family-test-release.md`

**Interfaces:**
- Produces: recorded app build version, content version, schema version, adaptive config version, microphone config version, APK checksum, signing identity, device ID/model, and known-issue decision.

- [ ] **Step 1: Add a release-config test asserting the expected non-empty fixed version identifiers and exact 8/2/16 content cardinality.**
- [ ] **Step 2: Run the full release suite; any test, manifest, content, schema, BLOCKER, or HIGH failure stops the release.**
- [ ] **Step 3: Freeze versions, update changelog, build the signed release artifact using the understood local signing process, and compute its checksum.**
- [ ] **Step 4: Clean-install on the primary tablet, run cold/warm launch and one full airplane-mode child/parent/reset/recording smoke test, then restore the intended clean seed state.**
- [ ] **Step 5: Commit release metadata as `release: freeze Prototype 0.1 family-test build`; keep signing secrets outside the repository.**

### Task 11.2: Rehearse and hand off the seven-day protocol

**Files:**
- Create: `docs/05-quality/evidence/family-test-handoff.md`
- Create: `docs/05-quality/evidence/family-test-observations.md`
- Modify: `docs/05-quality/evidence/family-test-release.md`

**Interfaces:**
- Consumes: exact installed build from Task 11.1 and `family-test-protocol.md`.
- Produces: parent-ready one-session-per-day guidance, short observation template, build-change log, stop/incident contacts, and end-of-week GO/ITERATE/STOP review template.

- [ ] **Step 1: Dry-run parent gate, optional mic, transfer marking, suggestion, family voice, reset explanation, observation template, and stop conditions with the adult tester.**
- [ ] **Step 2: Verify the adult can explain that silence/stopping/replay/mispronunciation are normal and that the product—not the child—is being tested.**
- [ ] **Step 3: Install only the frozen build, set comfortable device volume, disable update pressure, and document recovery/rollback steps for material safety/privacy defects.**
- [ ] **Step 4: Sign off all preconditions and begin the test; do not change content, adaptive timing, microphone threshold, child UI, or audio during seven days unless a material blocker/safety/privacy defect requires a recorded new phase.**
- [ ] **Step 5: Commit the non-sensitive handoff templates as `docs: prepare seven-day family-test handoff`.**

---

## Critical Path

```text
Phase 0 reproducible build
→ Phase 1 mandatory spike and stack decision
→ Phase 2 local tap/hear content slice
→ Phase 3 trustworthy evidence persistence
→ Phase 4 explicit session state machine
→ Phase 5 adaptive next-item selection
→ Phase 6 optional production microphone path
→ Phase 7 parent transfer/reset controls
→ Phase 8 private family voice
→ Phase 9 privacy/security release gates
→ Phase 10 physical-device/accessibility/performance acceptance
→ Phase 11 frozen family-test release
```

The product-critical core is Phases 0–7 plus 9–11. Family voice is in Prototype 0.1 scope and therefore required for release, but its implementation should never block or destabilize canonical child audio; if Phase 8 reveals a material privacy/lifecycle risk, stop and resolve it rather than weakening the canonical loop.

## Acceptance Traceability

| Acceptance row | Primary implementation and proof |
|---|---|
| AC-B01 App launches | Tasks 0.1, 10.1, 11.1 |
| AC-B02 Core loop | Tasks 2.1, 4.2, 6.3 |
| AC-B03 All 16 pairs | Task 2.2; Tasks 3.1 and 11.1 preserve/verify cardinality |
| AC-B04 Exposure integrity | Tasks 1.3, 3.2, 4.2–4.3 |
| AC-B05 Child mic privacy | Tasks 1.2, 6.1–6.3, 9.1 |
| AC-B06 Background mic safety | Tasks 6.1, 6.3, 9.2 |
| AC-B07 Offline operation | Tasks 0.2, 1.4, 9.1, 11.1 |
| AC-B08 Parent reset | Tasks 7.2 and 9.2 |
| AC-B09 Family recording privacy | Tasks 8.1–8.3 and 9.1–9.2 |
| AC-B10 No prohibited mechanics | Global constraints; Tasks 2.1, 7.1, 10.2 |
| AC-H01 Portrait/landscape | Tasks 1.4, 2.1–2.2, 4.3, 10.1–10.2 |
| AC-H02 Tap responsiveness | Tasks 1.1, 2.1, 10.1 |
| AC-H03 Audio responsiveness | Tasks 1.1, 2.1, 10.1 |
| AC-H04 Startup | Tasks 10.1 and 11.1 |
| AC-H05 Adaptive behavior | Tasks 5.1–5.2 |
| AC-H06 Mic denial path | Tasks 6.2–6.3 |
| AC-H07 Parent area | Tasks 7.1–7.2 |
| AC-H08 Child safety | Tasks 4.1–4.3, 6.3, 10.2 |
| AC-H09 Accessibility | Tasks 2.1, 7.1, 8.3, 10.2 |
| AC-H10 State integrity | Tasks 4.3, 6.3, 10.1 |

## Recommended Implementation Order

1. Execute tasks in phase order, and tasks within a phase in numeric order unless listed as safely parallel below.
2. Treat Task 1.4 as a hard stop/go gate. If KMP/CMP falls back, update build/platform paths once and keep domain contracts/tests intact.
3. Freeze content identity before Room seeding; freeze evidence semantics before the state controller; freeze state behavior before adaptive scheduling.
4. Keep the child loop complete with mic disabled before integrating real microphone permission/capture.
5. Add parent transfer/reset before family voice so the recorder lives behind an already-tested adult boundary.
6. Freeze content/adaptive/mic only after calibration, then run privacy hardening and QA against release variants.

## Work That Can Safely Run in Parallel

Parallel work is safe only after its consumed interfaces are merged and when agents do not edit the same Gradle/composition-root files.

- After Task 0.2: Task 1.1 audio proof and Task 1.2 microphone proof can run in parallel; Task 1.3 can prepare Room independently, then all converge in Task 1.4.
- After the Phase 1 GO: review/production of the 16 approved visual/audio assets can run alongside Task 2.1 content/UI contracts. The manifest is merged only after IDs and lexical choices are fixed.
- After Phase 3 interfaces: pure state-machine reducer tests (Task 4.1) and pure adaptive rule test preparation (Task 5.1) can run in parallel, but adaptive integration waits for Phase 4.
- During Phase 6: signal-classifier hardening and pure permission decision tests can run in parallel; controller integration/calibration remains after both.
- During Phase 8: parent UI state tests may be prepared while FileStore/recorder internals are implemented, but end-to-end UI waits for failure-safe replacement.
- During Phase 10: emulator automated regression and scheduled human audio/Tamil content review may run in parallel against the identical checksummed candidate.

## Work That Must Remain Sequential

- Repository bootstrap → mandatory spike → stack decision.
- Stable content IDs/assets → database seed/schema.
- Idempotent evidence transaction → state-machine effect execution.
- Pure reducer → controller integration → lifecycle/stale-callback validation.
- Pair-state rules → selector → session-engine adaptive integration.
- Mic classifier + permission policy → child-session mic integration → device calibration/config freeze.
- Parent gate → reset/transfer actions → family recorder UI.
- Recorder temp/save path → atomic replace/delete/recovery.
- Feature completion → privacy/security audit → device QA → release freeze/install.
- Any fix that changes content, adaptive policy, mic config, schema, or audio after freeze requires rerunning and re-signing the affected downstream gates.

## Recommended PR Count

Recommend approximately **30 small PRs**, normally one per numbered task. This is intentionally more PRs than phases: each PR has one outcome, one focused test cycle, and a reviewer can reject it without entangling a neighboring capability. If repository overhead is high, combine only adjacent documentation/evidence-only work; do not combine the Phase 1 spike proofs or the Phase 9–11 release gates into a single large PR.

## First PR to Create

Create **Task 0.1 — “Make the two-module shell build and test”** first.

That PR should contain only:

- valid `:shared` and `:androidApp` Gradle configuration;
- one common smoke test;
- one minimal warm-stone Compose shell hosted by Android;
- CI running a real shared test and Android assemble;
- no Room, audio, microphone, navigation, or product feature code.

Its review question is deliberately simple: **Can every later risk slice start from a clean, reproducible, installable KMP/CMP build?**

## Self-Review Record

- **Spec coverage:** Every BLOCKER/HIGH acceptance category maps to at least one task; audio, microphone, database, local storage, permissions, state identity, adaptation, parent transfer/reset, family voice, privacy, device QA, and family-test freeze have explicit implementation and verification gates.
- **Risk ordering:** Stack/audio/mic/Room/lifecycle risks are proven before broad feature implementation. Content comprehension precedes persistence; evidence precedes session control; session control precedes adaptation; the optional mic path follows a complete no-mic loop.
- **Product scope:** The plan adds no feature beyond Prototype 0.1 and repeats explicit non-build lists in every phase.
- **Type consistency:** `SessionId`, `InteractionId`, `PairId`, `AttemptState`, `AudioService`, `SpeechAttemptDetector`, `LearningSessionEngine`, `AdaptiveEngine`, repository, and controller names remain consistent with `api.md` across tasks.
- **Open-decision handling:** Stack, minimum SDK, playback backend/codec/threshold, VAD configuration, backup rules, recording format, and test framework/device choices are resolved at named evidence gates rather than silently invented.
- **Placeholder scan:** The plan contains no deferred implementation markers or unnamed error/test work. Content-review tests explicitly forbid unresolved content markers before release.
