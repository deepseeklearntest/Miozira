# Miozira — Open Questions & Decision Log

**Document:** `docs/OPEN-QUESTIONS.md`  
**Status:** Active  
**Product:** Miozira Prototype 0.1  
**Last updated:** 2026-08-25  

---

## 1. Purpose

This document tracks unresolved product, content, engineering, and UX questions identified across the documentation suite. It ensures that differences or decisions requiring validation are transparently documented rather than silently decided in code.

---

## 2. Open Questions & Identified Tensions

### OQ-001: Tamil Lexical Form for "Cup" (`கப்` vs `கோப்பை`)
- **Referenced in:** `docs/01-learning/content-spec.md` (lines 330–338, 1575), `docs/05-quality/testing.md` (line 1407), `docs/05-quality/acceptance-criteria.md` (line 246)
- **Context:** Everyday colloquial Tamil, especially among young bilingual children and diaspora families, overwhelmingly uses `கப்` (loan word). Traditional or formal Tamil uses `கோப்பை` (*koappai*).
- **Conflict / Tension:** `content-spec.md` marks this as `decision required` before recording freeze.
- **Recommendation:** Use `கப்` if prioritizing immediate conversational familiarity for bilingual 4–5-year-olds in family routines, or `கோப்பை` if prioritizing formal linguistic purity. Record `கப்` as the primary family-tested term for Prototype 0.1, with optional family custom voice recording allowing parents to record their preferred regional term.

### OQ-002: Tamil Lexical Form for "Water" (`தண்ணீர்` vs `தண்ணி`)
- **Referenced in:** `docs/01-learning/content-spec.md` (lines 340–348, 1580), `docs/05-quality/qa-checklist.md` (line 38)
- **Context:** `தண்ணீர்` (*thanniir*) is the standard literary and canonical spoken form, while `தண்ணி` (*thanni*) is common colloquial spoken shorthand.
- **Conflict / Tension:** `content-spec.md` lists `தண்ணீர்` with note `preferred; review required`.
- **Recommendation:** Adopt `தண்ணீர்` (*thanniir*) for canonical bundled audio recorded with natural, warm maternal intonation.

### OQ-003: Parent Gate Mechanism (Timed Discreet Hold vs Math Challenge)
- **Referenced in:** `docs/02-design/parent-experience.md` (§14–16), `docs/02-design/ui-ux.md` (§30–32), `docs/04-privacy-safety/security.md` (§13–14), `docs/05-quality/acceptance-criteria.md` (`AC-H07`)
- **Context:** `parent-experience.md` and `ui-ux.md` explicitly specify avoiding child-friendly math questions (which turn into attractive puzzles for toddlers) and recommend a discreet 2-second hold on a quiet, low-contrast corner hotspot.
- **Conflict / Tension:** Earlier drafts mentioned arithmetic challenge modals, which create cognitive friction for parents and attract child curiosity.
- **Recommendation:** Standardize on the discreet $\approx 2\text{-second}$ hold gesture on the low-contrast corner hotspot as defined in `parent-experience.md` and `ui-ux.md`. Treat the gate as a light friction boundary (`AC-H07`) against accidental toddler navigation, not authentication.

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
- **Required before family-test freeze:** Record visual provenance/licences; obtain fluent Tamil and English review of wording, pronunciation, and loudness; then promote each accepted pair from `DRAFT` to an approved review status.

---

## 3. Decision Process

1. **Architecture Decisions:** Must be recorded as ADRs in `docs/decisions/`.
2. **Content & Pedagogical Decisions:** Reviewed with native language speakers prior to audio recording freeze.
3. **Safety & Privacy Constraints:** Non-negotiable; zero network permission and local-only processing remain strict invariants.
