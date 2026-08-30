# Miozira Documentation Index

Welcome to the canonical specification and documentation suite for **Miozira Prototype 0.1**.

These documents serve as the single source of truth for all product, pedagogical, architectural, design, privacy, and testing decisions.

---

## Documentation Structure

```text
docs/
├── 00-foundation/        # Product vision, prototype scope, functional & non-functional requirements, research
├── 01-learning/          # Learning model, adaptive engine, content specification, session design, real-world transfer
├── 02-design/            # Design system, color palette, UI/UX, child experience, parent experience, accessibility
├── 03-engineering/       # Architecture, database schema, internal API contracts, audio, microphone, platform support
├── 04-privacy-safety/    # Privacy guarantees, child safety principles, security architecture, threat models, data inventory
├── 05-quality/           # Acceptance criteria, testing strategy, device matrix, family test protocol, QA checklists
├── decisions/            # Architecture Decision Records (ADRs)
└── OPEN-QUESTIONS.md     # Documented tensions, open questions, and recommended resolutions
```

---

## Index of Documents

### 00. Foundation
- [`00-foundation/PRD-prototype-0.1.md`](00-foundation/PRD-prototype-0.1.md) — Comprehensive implementation-facing Product Requirements Document.
- [`00-foundation/product-vision.md`](00-foundation/product-vision.md) — High-level vision, mission, and learning philosophy.
- [`00-foundation/prototype-0.1-scope.md`](00-foundation/prototype-0.1-scope.md) — Exact boundary and non-negotiables for Prototype 0.1.
- [`00-foundation/requirements.md`](00-foundation/requirements.md) — Core functional requirements.
- [`00-foundation/non-functional-requirements.md`](00-foundation/non-functional-requirements.md) — Latency, memory, battery, reliability, and accessibility benchmarks.
- [`00-foundation/research.md`](00-foundation/research.md) — Pedagogical and developmental research foundations.

### 01. Learning & Pedagogy
- [`01-learning/learning-model.md`](01-learning/learning-model.md) — Core learning model: See → Hear → Imitate → Encounter Again → Use with a Human.
- [`01-learning/adaptive-engine.md`](01-learning/adaptive-engine.md) — Local adaptive repetition logic, spacing intervals, and weighting algorithms.
- [`01-learning/content-spec.md`](01-learning/content-spec.md) — Specification for the 8 initial concepts across English and Tamil (16 concept-language pairs).
- [`01-learning/session-design.md`](01-learning/session-design.md) — Structure, pacing, and flow of child learning sessions.
- [`01-learning/real-world-transfer.md`](01-learning/real-world-transfer.md) — Real-world prompt generation and parent observation loop.

### 02. Design & Experience
- [`02-design/design-system.md`](02-design/design-system.md) — Visual design tokens, component hierarchy, spacing, typography, and motion guidelines.
- [`02-design/color-palette.md`](02-design/color-palette.md) — Locked color palette (*Quiet interface, vivid reality*).
- [`02-design/ui-ux.md`](02-design/ui-ux.md) — Screen flows, layouts, touch target dimensions, and tablet layouts (portrait + landscape).
- [`02-design/child-experience.md`](02-design/child-experience.md) — Child interface constraints: zero pressure, no scores, no gamified manipulation.
- [`02-design/parent-experience.md`](02-design/parent-experience.md) — Protected parent portal, real-world transfer marking, custom voice recording, and reset tools.
- [`02-design/interaction-states.md`](02-design/interaction-states.md) — Complete state machine for child touch, audio playback, and attempt detection.
- [`02-design/accessibility.md`](02-design/accessibility.md) — Contrast, target sizes, hearing/visual accommodation guidelines.

### 03. Engineering & Architecture
- [`03-engineering/architecture.md`](03-engineering/architecture.md) — Layered architecture (Presentation → Domain → Data → Platform), clean boundary rules.
- [`03-engineering/database.md`](03-engineering/database.md) — Local Room/SQLite schema, entities, queries, and migration policies.
- [`03-engineering/api.md`](03-engineering/api.md) — Internal service contracts (audio playback, detector, repository, settings).
- [`03-engineering/audio.md`](03-engineering/audio.md) — Audio asset encoding, low-latency playback pipeline, and audio session management.
- [`03-engineering/microphone.md`](03-engineering/microphone.md) — Speech-attempt detection pipeline, memory-only PCM processing, zero file persistence.
- [`03-engineering/local-storage.md`](03-engineering/local-storage.md) — Private app directory filesystem management for family custom recordings.
- [`03-engineering/performance.md`](03-engineering/performance.md) — Frame rate, memory caps, latency targets, and low-end tablet optimization.
- [`03-engineering/platform-support.md`](03-engineering/platform-support.md) — Platform support matrix (Android tablet primary, future iPadOS considerations).
- [`03-engineering/development-environment.md`](03-engineering/development-environment.md) — Host tools, Android SDK, pinned build versions, and environment verification.
- [`03-engineering/permissions.md`](03-engineering/permissions.md) — Runtime permission flows (Record Audio) with safe fallbacks.

### 04. Privacy, Safety & Security
- [`04-privacy-safety/privacy.md`](04-privacy-safety/privacy.md) — 100% offline guarantee, zero telemetry, zero analytics, zero data exfiltration.
- [`04-privacy-safety/child-safety.md`](04-privacy-safety/child-safety.md) — Safeguards against overstimulation, dark patterns, ads, and accidental external navigation.
- [`04-privacy-safety/security.md`](04-privacy-safety/security.md) — Local storage security, parent gate design, and data sanitization on reset.
- [`04-privacy-safety/threat-model.md`](04-privacy-safety/threat-model.md) — Threat model and mitigation strategies.
- [`04-privacy-safety/data-inventory.md`](04-privacy-safety/data-inventory.md) — Exhaustive inventory of all locally stored data items and retention policies.

### 05. Quality & Verification
- [`05-quality/acceptance-criteria.md`](05-quality/acceptance-criteria.md) — Definitive acceptance criteria for Prototype 0.1.
- [`05-quality/testing.md`](05-quality/testing.md) — Unit, integration, automated UI, and manual test strategies.
- [`05-quality/device-test-matrix.md`](05-quality/device-test-matrix.md) — Target hardware testing matrix.
- [`05-quality/family-test-protocol.md`](05-quality/family-test-protocol.md) — 7-day family trial protocol and evaluation metrics.
- [`05-quality/qa-checklist.md`](05-quality/qa-checklist.md) — Release readiness checklist.
- [`05-quality/release.md`](05-quality/release.md) — Build signing, packaging, and local side-loading instructions for family test devices.
- [`05-quality/evidence/phase-3-local-persistence.md`](05-quality/evidence/phase-3-local-persistence.md) — Phase 3 emulator validation record for local persistence.

### Architecture Decision Records (ADRs)
- [`decisions/README.md`](decisions/README.md) — Architecture Decision Records log and authoring template.
- [`decisions/ADR-0001-technology-stack.md`](decisions/ADR-0001-technology-stack.md) — Selection of Kotlin Multiplatform + Compose Multiplatform with native Android fallback.

### Open Questions
- [`OPEN-QUESTIONS.md`](OPEN-QUESTIONS.md) — Documented questions, lexical choices, and architectural validations.
