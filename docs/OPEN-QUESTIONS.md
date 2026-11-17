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

### OQ-010: Landscape Lock Is Not Enforced in the Manifest
- **Referenced in:** `CLAUDE.md` (invariants: "The app is locked to landscape"), `docs/superpowers/specs/2026-11-16-ui-ux-design.md` (sideways only), `androidApp/src/main/AndroidManifest.xml`
- **Context:** The manifest sets no `android:screenOrientation` and the code sets no `requestedOrientation`. Found during the parent-area review on 2026-11-17. The gap predates the parent-area plan and is outside its scope.
- **Impact:** The tablet can turn the app to portrait, which breaks a stated invariant.
- **Required:** Decide the enforcement point (manifest attribute or runtime) in a separate change.

### OQ-011: Parent Area Home Omits Two Spec Categories Until Their Data Exists
- **Referenced in:** `docs/superpowers/specs/2026-11-16-ui-ux-design.md` §10.2–10.3; `docs/superpowers/plans/2026-11-17-parent-area.md` ("Decisions made in this plan" and "Not in this plan")
- **Context:** Spec §10.2 lists "Said before the app did" and a puzzle line in the most recent session summary, and §10.3 lists "How the puzzles are going" and "Voices". The parent-area plan builds none of them: "say it first" and the puzzles do not exist yet, and the Voices screen belongs with the guide-voice plan.
- **Impact:** Until they land, the parent home shows fewer categories than the spec describes. No data is invented for them.
- **Required:** Add the two categories, the puzzle page and Voices with the child-session/puzzle and guide-voice plans.

### OQ-012: Microphone and Privacy Page Had No Baseline Explanation to Keep
- **Referenced in:** `docs/superpowers/plans/2026-11-17-parent-area.md` (Task 8: the page "keeps the existing explanation"), `shared/src/commonMain/kotlin/com/miozira/presentation/parent/ParentActionsScreen.kt`
- **Context:** The baseline page held only a settings button and the reset controls; there was no explanation to keep. Task 8 wrote one from `docs/03-engineering/microphone.md` and `docs/04-privacy-safety/security.md`, separating the listening switch from family recording.
- **Impact:** The wording is unreviewed by the parent or a copy owner.
- **Required:** Review and approve the wording, or supply the intended text.

### OQ-013: Parent-Area Dependencies on the Child-Session Plan
- **Referenced in:** `docs/superpowers/plans/2026-11-17-parent-area.md` (Tasks 1, 4, 7), `docs/superpowers/specs/2026-11-16-ui-ux-design.md` §10.1, `shared/src/commonMain/kotlin/com/miozira/presentation/parent/ParentActionsController.kt`, `shared/src/commonMain/kotlin/com/miozira/presentation/child/ChildSessionScreen.kt`, `shared/src/commonMain/kotlin/com/miozira/content/ContentRepository.kt`
- **Context:** The parent area was built before the child session state machine. Found in the final branch review on 2026-11-17:
  - **(a) The listening switch has no effect yet.** `isSpeechAttemptDetectionEnabled` is read only by `ParentActionsController.load()`; no production code calls `ChildSessionController.completeInvitation` or `decideAttemptMeasurement`. The Microphone and privacy page still says "When listening is on, the microphone only notices…", which describes intended behaviour, not current behaviour.
  - **(b) The parent corner is missing on the null state and the quiet screen.** `ChildSessionScreen` returns early when the session state is null (the corner is never drawn), and the quiet screen does not exist yet. Spec §10.1 requires the corner on every child screen, the quiet screen included.
  - **(c) The extra-session row and `startExtraSession` were left out** (parent-area ruling R4), and the `languageNames` parameter added to the parent sentences must be merged with the child-session plan's content changes (rulings R1, R11).
- **Impact:** A parent can turn listening on with no change in the child session; the parent area cannot be opened from a screen with no session state; no extra session can be started.
- **Required:** The child-session plan wires the setting into measurement, places the corner on every child screen including the quiet screen, adds the extra-session row with its confirmation, and merges `languageNames`.

### OQ-014: Three Docs Still Describe the Removed Suggestion Code
- **Referenced in:** `docs/03-engineering/api.md` (`getRealWorldSuggestions`, `chooseSuggestion`, §32 `SuggestionContext`), `docs/03-engineering/architecture.md` (`getRealWorldSuggestions`, `RealWorldSuggestion`), `docs/01-learning/real-world-transfer.md` (`RealWorldSuggestion`)
- **Context:** Parent-area Task 5 removed `chooseSuggestion`, `RealWorldSuggestion` and `SuggestionContext` from the code (ruling R9) and replaced them with the fixed daily-moments table. The three docs were not rewritten in that plan.
- **Impact:** A reader of these docs expects an API that no longer exists.
- **Required:** A docs pass updates the three files to describe `dailyMoments` and `wordsForToday`, or records why the suggestion service should return.

### OQ-015: Parent Data Source Imports Presentation Types
- **Referenced in:** `shared/src/commonMain/kotlin/com/miozira/data/repository/RoomParentDataSource.kt`, `shared/src/commonMain/kotlin/com/miozira/presentation/parent/ParentSentences.kt`, `docs/03-engineering/architecture.md` (dependency direction)
- **Context:** `RoomParentDataSource` (data layer) imports `PairEvidence`, `ParentDataSource`, `SessionSummary` and `summarize()` from `presentation.parent`. The grouping rule in `summarize()` is domain logic living in presentation. Moving these types to `domain` was deferred in the final branch review (2026-11-17) as too large for the fix wave.
- **Impact:** The data layer depends on presentation, the reverse of the stated direction. The child-session plan will merge against this shape.
- **Required:** Move `SessionSummary`, `PairEvidence`, `ParentDataSource` and `summarize()` to a domain package in a separate change.

### OQ-016: Parent-Area Wording Added in the Final Review Needs Copy Review
- **Referenced in:** `shared/src/commonMain/kotlin/com/miozira/presentation/parent/ParentSentences.kt` (`describeLanguage`), `ParentController.kt`, `ParentSummaryScreen.kt`, `ParentActionsScreen.kt`
- **Context:** The final branch review (2026-11-17) found the language page said "he has not tried" for words that were never measured (listening off). New sentences were written:
  - "<Language> — he has been hearing his <Language> words." (heard, nothing measured; unmeasured words are also left out of "Still just listening")
  - "Miozira could not read his sessions just now." (session read failed)
  - "Some of this page could not be shown. Try opening it again later." (any read failed)
  - "Could not change this yet." (listening switch could not be saved)
- **Impact:** The wording is unreviewed by the parent or a copy owner.
- **Required:** Review and approve the wording, or supply the intended text.

---

## 3. Decision Process

1. **Architecture Decisions:** Must be recorded as ADRs in `docs/decisions/`.
2. **Content & Pedagogical Decisions:** Reviewed with native language speakers prior to audio recording freeze.
3. **Safety & Privacy Constraints:** Non-negotiable; zero network permission and local-only processing remain strict invariants.
