# Miozira — Open Questions & Decision Log

**Document:** `docs/OPEN-QUESTIONS.md`  
**Status:** Active  
**Product:** Miozira Prototype 0.1  
**Last updated:** 2026-11-17  

---

## 1. Purpose

This document tracks unresolved product, content, engineering, and UX questions identified across the documentation suite. It ensures that differences or decisions requiring validation are transparently documented rather than silently decided in code.

---

## 2. Open Questions & Identified Tensions

### OQ-001: Tamil Lexical Form for "Cup" (`கப்` vs `கோப்பை`) [Resolved]
- **Referenced in:** `docs/01-learning/content-spec.md` (lines 330–338, 1575), `docs/05-quality/testing.md` (line 1407), `docs/05-quality/acceptance-criteria.md` (line 246)
- **Context:** Everyday colloquial Tamil, especially among young bilingual children and diaspora families, overwhelmingly uses `கப்` (loan word). Traditional or formal Tamil uses `கோப்பை` (*koappai*).
- **Conflict / Tension:** `content-spec.md` marks this as `decision required` before recording freeze.
- **Recommendation:** Use `கப்` if prioritizing immediate conversational familiarity for bilingual 4–5-year-olds in family routines, or `கோப்பை` if prioritizing formal linguistic purity. Record `கப்` as the primary family-tested term for Prototype 0.1, with optional family custom voice recording allowing parents to record their preferred regional term.
- **Resolution (parent, 2026-11-16):** `கப்`. Tamil uses the conversational forms the parents say at home, and the parents record the Tamil words themselves (`docs/superpowers/specs/2026-11-16-five-languages-and-thinking-puzzles-design.md`, sections 7.6 and 9). `content-spec.md` and `acceptance-criteria.md` were updated on 2026-11-17.

### OQ-002: Tamil Lexical Form for "Water" (`தண்ணீர்` vs `தண்ணி`) [Resolved]
- **Referenced in:** `docs/01-learning/content-spec.md` (lines 340–348, 1580), `docs/05-quality/qa-checklist.md` (line 38)
- **Context:** `தண்ணீர்` (*thanniir*) is the standard literary and canonical spoken form, while `தண்ணி` (*thanni*) is common colloquial spoken shorthand.
- **Conflict / Tension:** `content-spec.md` lists `தண்ணீர்` with note `preferred; review required`.
- **Earlier recommendation (superseded):** Adopt `தண்ணீர்` (*thanniir*) for canonical bundled audio recorded with natural, warm maternal intonation.
- **Resolution (parent, 2026-11-16):** `தண்ணி`, the conversational form, for the same reason as OQ-001 (`docs/superpowers/specs/2026-11-16-five-languages-and-thinking-puzzles-design.md`, sections 7.6 and 9). This overrides the earlier recommendation; `content-spec.md` and `acceptance-criteria.md` were updated on 2026-11-17.

### OQ-003: Parent Gate Mechanism (Timed Discreet Hold vs Math Challenge) [Resolved]
- **Referenced in:** `docs/02-design/parent-experience.md` (§14–16), `docs/02-design/ui-ux.md` (§30–32), `docs/04-privacy-safety/security.md` (§13–14), `docs/05-quality/acceptance-criteria.md` (`AC-H07`)
- **Context:** `parent-experience.md` and `ui-ux.md` explicitly specify avoiding child-friendly math questions (which turn into attractive puzzles for toddlers) and recommend a discreet 2-second hold on a quiet, low-contrast corner hotspot.
- **Conflict / Tension:** Earlier drafts mentioned arithmetic challenge modals, which create cognitive friction for parents and attract child curiosity.
- **Recommendation:** Standardize on the discreet $\approx 2\text{-second}$ hold gesture on the low-contrast corner hotspot as defined in `parent-experience.md` and `ui-ux.md`. Treat the gate as a light friction boundary (`AC-H07`) against accidental toddler navigation, not authentication.
- **Resolution (parent, 2026-11-17):** Two steps: a 3-second hold on an undrawn top-right corner, then typing a two-digit number that is written as a word and changes every time (`docs/superpowers/specs/2026-11-16-ui-ux-design.md`, section 10.1). This is a reading step, not arithmetic, so it is not a puzzle a pre-reader can enjoy solving; a wrong answer or 10 seconds of nothing returns silently to the child screen. It remains a friction boundary, not authentication. `security.md` and `AC-H07` were updated on 2026-11-17.

### OQ-004: Speech Attempt Detection Thresholding (RMS vs Lightweight VAD)
- **Referenced in:** `docs/03-engineering/microphone.md` (§80), `docs/03-engineering/architecture.md` (§95–96), `docs/decisions/ADR-0001-technology-stack.md` (§38)
- **Context:** The microphone detector must classify speech attempts strictly within memory, without storing raw audio and without speech-to-text / pronunciation grading.
- **Conflict / Tension:** Simple RMS energy thresholding may trigger on room noise (clapping, doors), while neural VADs (Silero/WebRTC) add native binary dependencies.
- **Recommendation:** Validate calibrated energy/RMS with duration gating (minimum 250ms sustained energy above ambient noise baseline) during the mandatory Technical Spike. If insufficient on target Android tablets, evaluate lightweight native WebRTC VAD.

### OQ-005: Android `minSdk` Baseline Confirmation
- **Referenced in:** `docs/03-engineering/platform-support.md` (§133–135), `docs/05-quality/device-test-matrix.md` (§70)
- **Context:** `minSdk 26` (Android 8.0 Oreo) provides broad hardware compatibility for older repurposed family tablets. However, modern Compose Multiplatform and Android AudioRecord APIs have better support on `minSdk 28+` (Android 9 Pie).
- **Conflict / Tension:** Target minSdk is provisionally set to 26 pending technical spike verification.
- **Recommendation:** Target `minSdk 26` during the Technical Spike; raise to `minSdk 28` only if AudioRecord / OpenSL ES or Compose Multiplatform compatibility on API 26–27 creates stability issues.

### OQ-006: Android Auto-Backup & Cloud Sync Exclusion
- **Referenced in:** `docs/03-engineering/database.md`, `docs/04-privacy-safety/data-inventory.md` (§1373–1374), `docs/04-privacy-safety/privacy.md`
- **Context:** Android OS provides automatic Google Drive cloud backup for app private data (`android:allowBackup="true"` by default).
- **Conflict / Tension:** Miozira's privacy guarantee states 100% offline local-only operation with zero cloud storage of family interactions or custom voice recordings.
- **Recommendation:** Explicitly set `android:allowBackup="false"` and `android:fullBackupContent="false"` in `AndroidManifest.xml` to prevent any OS-level cloud leakage of learning logs or family voice recordings.

### OQ-007: Repository Gradle Build Setup [Resolved]
- **Referenced in:** `README.md` (§3), `CLAUDE.md`, repository root
- **Resolution:** Root and module Gradle Kotlin DSL scripts (`build.gradle.kts`, `settings.gradle.kts`, `gradle/libs.versions.toml`, `gradlew`) have been established for the Multiplatform project, scaffolding `:shared`, `:androidApp`, and `:iosApp`.

### OQ-008: `architecture.md` Preamble Predates the Accepted ADR [Resolved]
- **Referenced in:** `docs/03-engineering/architecture.md` (§1 "Purpose"), `docs/decisions/ADR-0001-technology-stack.md` (Status: Accepted)
- **Resolution:** The `architecture.md` §1 preamble now references ADR-0001 as decided (Kotlin Multiplatform + Compose Multiplatform, native Android fallback) and states the layered architecture is stack-independent by design, not stack-undecided. Flutter is no longer listed as a live option.

### OQ-009: Prototype 0.1 Content Is Packaged but Not Yet Reviewed
- **Referenced in:** `docs/00-foundation/MVP-IMPLEMENTATION-PLAN.md` (Phase 2), `docs/01-learning/content-spec.md` (§10, §47).
- **Context:** A local draft manifest now maps all eight concepts and sixteen English/Tamil pairs. The bundled clips are development drafts, and the Cat, Cup, Hand, and Water visuals are supplied drafts with provenance not yet recorded.
- **Impact:** The structural content package is testable, but it cannot be frozen or described as reviewed canonical content. This preserves the content-review and child-safety rules.
- **Update 2026-11-17:** The scope is now five languages (forty pairs). Six of the eight draft pictures are to be replaced or repainted under the picture rules in `docs/superpowers/specs/2026-11-16-ui-ux-design.md`, section 8 (painted realistic, no faces, no glow); only apple and ball are kept. No Arabic, Japanese or Spanish word reaches the child before a native speaker has checked it.
- **Required before family-test freeze:** Record visual provenance/licences; obtain fluent Tamil and English review of wording, pronunciation, and loudness; then promote each accepted pair from `DRAFT` to an approved review status.

### OQ-010: Child Screen Orientation: `landscape` or `sensorLandscape`
- **Referenced in:** `docs/superpowers/specs/2026-11-16-ui-ux-design.md` (§3 line 53, §12), `docs/superpowers/plans/2026-11-17-child-session-shape.md` (Task 9, Decisions).
- **Context:** The UI/UX spec says "sideways (landscape) only; the screen does not turn". The child-session plan chose `sensorLandscape`, which lets the screen flip 180° between the two sideways directions.
- **Ruling (session build, 2026-11-17):** `android:screenOrientation="landscape"`, the conservative reading of the spec. `ManifestPolicyTest` asserts `SCREEN_ORIENTATION_LANDSCAPE`.
- **Impact:** The screen does not flip when the tablet is turned round. One attribute changes if the owner wants `sensorLandscape`.
- **Required:** The owner confirms which reading is wanted.

### OQ-011: Where `ChildScreenState` Lives
- **Context:** The child-session plan put `ChildScreenState` in `presentation/child`, but the domain controller produces it.
- **Ruling (session build):** `domain/session/ChildScreenState.kt`, so dependencies keep pointing inward (`AGENTS.md`, Presentation → Domain).
- **Impact:** Only the import path differs from the plan.

### OQ-012: Two Meanings of "Speaking Invite"
- **Context:** `InteractionState.allowSpeakingInvite` gates the silent turn (INVITING → LISTENING). `InteractionPlan.allowSpeakingInvite` is set to `false` by the controller (no spoken invite is given) and is not persisted anywhere. The two flags share a name and mean different things.
- **Impact:** No behaviour error today. The name misleads future readers.
- **Required:** Rename the reducer flag (for example `runsSilentTurn`) when the spoken "say it first" invite (product step 3) lands.

### OQ-013: Spoken "Your Turn" and Closing Phrase Deferred
- **Referenced in:** `docs/superpowers/specs/2026-11-16-ui-ux-design.md` (§5.2, §7), §11 build order.
- **Context:** The spec says "your turn" is spoken after every word and a closing phrase ends the session. The UI/UX spec's build order (§11) puts the Malayalam guide voice in product step 4, so the child-session build has a silent turn and no closing phrase until then.
- **Impact:** The child hears no spoken prompt between words until product step 4.
- **Required:** None before step 4. Recorded so the gap is not forgotten.

### OQ-014: A Failed Session-End Write Keeps the Screen Quiet Until Restart
- **Context:** `ChildSessionController` clears its session flag before the end write completes. If the write fails, the engine still holds the session, so `open()` shows Quiet until the app restarts. The session is not recorded as completed, so the next launch starts again, and recovery closes the open session.
- **Also:** `open()` during the end write could see `todayIsFinished()` as false and start a new session.
- **Impact:** Rare database failure only. Not a lockout: the day is not marked finished.
- **Required:** Retry or reset the engine state on a failed end write. Decide before the family test.

### OQ-015: Which Turn Counts After a Replay
- **Context:** `recordAttemptOnce` is first-write-wins. A silent first turn that is replayed and then answered with speech stores `NO_ATTEMPT_DETECTED`, because the second result cannot overwrite the first.
- **Impact:** Evidence only; no child-visible effect. The nod does show on the second turn.
- **Required:** Decide whether the first turn or the last turn counts, and record it.

### OQ-016: Fresh-Install Fallback May Revisit Introduced Words
- **Context:** When every candidate is new and the planner's new-word cap is reached, the adaptive fallback (`INSUFFICIENT_ALTERNATIVES`) cycles through the words already introduced, outside the last two turns. It introduces no new word.
- **Impact:** Within the spec's limit of two or three new words per block. Accepted for the prototype.

### OQ-017: `CancellationException` Is Swallowed by Room Helpers
- **Context:** `RoomSessionRepository.readSafely` and `writeSafely` catch `Exception`, which includes `CancellationException`, so cancellation becomes an ordinary failure.
- **Impact:** The child-session controller guards its own paths (`NonCancellable` on the end write; a cancelled-or-paused check before ending as a content error), so today's flows are safe. Other callers may not be.
- **Required:** A sweep of the Room helpers to rethrow cancellation.

### OQ-018: Child Session Deferred Items
- The microphone-off state is written at the end of the turn, not at its start (the reducer decides this); the detector window is now the full turn (4000 ms).
- The word cross-fade is keyed on the pair, so a repeat of the same pair does not cross-fade (the per-interaction flags do reset).
- `SessionEndTest` (Room) and the other connected tests were compiled but not run: no device was attached.
- The Xiaomi Pad check and the emulator screenshot check of the word screen were not done.

---

## 3. Decision Process

1. **Architecture Decisions:** Must be recorded as ADRs in `docs/decisions/`.
2. **Content & Pedagogical Decisions:** Reviewed with native language speakers prior to audio recording freeze.
3. **Safety & Privacy Constraints:** Non-negotiable; zero network permission and local-only processing remain strict invariants.
