# Miozira — Non-Functional Requirements

**Document:** `non-functional-requirements.md`  
**Version:** 0.1  
**Status:** Draft for Prototype 0.1  
**Product:** Miozira  
**Primary platform:** Android tablet  
**Future portability target:** iPadOS  
**Prototype target age:** approximately 4–5 years  
**Last updated:** 2026-08-25

---

## 1. Purpose

This document defines the **quality attributes and operating constraints** for Miozira Prototype 0.1.

`requirements.md` defines **what** Miozira must do.

This document defines **how well** it must do it.

The requirements below should guide:

- `architecture.md`
- `database.md`
- `api.md`
- `audio.md`
- `microphone.md`
- `ui-ux.md`
- `security.md`
- `privacy.md`
- `testing.md`
- `performance.md`
- release/QA work

These are product-quality requirements, not implementation instructions.

---

# 2. Priority Model

Each non-functional requirement is marked:

- **P0 — Prototype blocker:** failure undermines the validity or safety of Prototype 0.1.
- **P1 — Strong requirement:** important for a dependable family test, but a documented limited fallback may be acceptable.
- **P2 — Supporting requirement:** desirable, but can be simplified temporarily.

---

# 3. Performance and Responsiveness

### NFR-PERF-001 — Fast child interaction response — P0

After a valid child tap, Miozira shall provide visible or audible acknowledgement quickly enough that the child does not reasonably interpret the app as unresponsive.

**Target:** initial interaction feedback should normally begin within **100 ms**.

Canonical spoken-word playback should normally begin within **250 ms** after the triggering tap when the asset is already local and the device is not under abnormal system load.

These are engineering targets, not guarantees under all OS conditions.

---

### NFR-PERF-002 — Smooth concept transitions — P0

Normal transitions between learning items shall not contain prolonged blank screens, blocking loaders, or visible UI freezes.

**Target:** common local transitions should complete within approximately **300 ms**, excluding intentional instructional pauses.

---

### NFR-PERF-003 — Startup without network delay — P0

Application startup shall not wait for a network request.

**Target:** on supported midrange hardware, the child experience should become usable within approximately **2 seconds** of a warm launch and **4 seconds** of a normal cold launch.

Exact benchmark methodology will be defined in `performance.md`.

---

### NFR-PERF-004 — No unnecessary heavy computation — P0

Core Prototype 0.1 behavior shall not depend on:

- large on-device language models;
- GPU-intensive inference;
- NPU-only processing;
- continuous audio inference;
- video decoding;
- background AI workloads.

---

### NFR-PERF-005 — Local database operations remain imperceptible — P1

Normal persistence operations such as recording an exposure, replay, attempt state, or parent observation shall not visibly block the UI.

Database writes should execute asynchronously where appropriate.

---

# 4. Reliability

### NFR-REL-001 — Core learning loop reliability — P0

The tap → hear → invitation → response → continue loop shall operate reliably during ordinary family use.

A failure in an optional subsystem shall not terminate the entire session where graceful fallback is possible.

---

### NFR-REL-002 — Microphone failure isolation — P0

Failure, denial, or unavailability of microphone functionality shall not prevent:

- concept display;
- audio playback;
- replay;
- session progression;
- adaptive exposure based on non-microphone evidence.

---

### NFR-REL-003 — Family recording failure isolation — P1

A missing, unreadable, or deleted family recording shall not prevent canonical language audio from playing.

---

### NFR-REL-004 — Partial session durability — P0

Meaningful learning events already persisted shall survive:

- app termination;
- process death;
- orientation change;
- ordinary device restart.

---

### NFR-REL-005 — Idempotent critical persistence — P1

Where a lifecycle event or retry could cause duplicate writes, persistence logic should prevent obviously duplicated learning evidence.

Example:

A single tap should not become three exposure records merely because the UI recomposed or the activity was recreated.

---

### NFR-REL-006 — No silent data loss — P0

The app shall not intentionally discard previously committed learning history without:

- explicit parent reset;
- schema migration decision;
- documented recovery action.

---

# 5. Availability and Offline Operation

### NFR-OFF-001 — Fully usable without internet — P0

After installation and required local setup, all core Prototype 0.1 learning functions shall remain usable with the device in airplane mode.

---

### NFR-OFF-002 — No hidden cloud dependency — P0

The prototype shall not silently depend on an online service for:

- word audio;
- concept images;
- session planning;
- adaptive scheduling;
- microphone attempt detection;
- progress storage;
- parent observations;
- real-world suggestions.

---

### NFR-OFF-003 — No degraded “offline mode” label required — P1

Because local operation is the normal architecture, Miozira should not treat offline use as an error condition or a secondary mode.

---

### NFR-OFF-004 — Network absence is non-exceptional — P0

No child-facing error shall appear solely because:

- Wi-Fi is disabled;
- mobile data is unavailable;
- DNS fails;
- internet connectivity is absent.

---

# 6. Data Integrity

### NFR-DATA-001 — Local database is authoritative — P0

For Prototype 0.1, locally persisted application data shall be the source of truth for learning history and parent observations.

---

### NFR-DATA-002 — Referential integrity — P0

Learning-history records shall not reference nonexistent:

- concepts;
- languages;
- concept-language pairs;
- sessions

except during a controlled migration/recovery procedure.

---

### NFR-DATA-003 — Stable identifiers — P0

Canonical concept and language identifiers shall remain stable across ordinary app updates.

Display text shall not be used as the primary database identity.

---

### NFR-DATA-004 — Migration safety — P1

Database schema changes shall use explicit versioned migrations once persistent family-test data exists.

Destructive migration shall not be the default production behavior.

---

### NFR-DATA-005 — Time handling — P1

Stored timestamps shall use a consistent machine-readable representation.

Learning scheduling logic shall not depend on locale-formatted date strings.

---

### NFR-DATA-006 — Clock-change tolerance — P2

The adaptive engine should behave reasonably if the device time or timezone changes.

A clock change shall not corrupt learning history.

---

# 7. Privacy

### NFR-PRIV-001 — Data minimisation — P0

Miozira shall collect and retain only data necessary for Prototype 0.1 behavior and validation.

---

### NFR-PRIV-002 — No remote child profile — P0

Prototype 0.1 shall not create or transmit a remote child profile.

---

### NFR-PRIV-003 — Raw child speech ephemeral — P0

Raw microphone audio used for speech-attempt detection shall be processed transiently and discarded after the interaction unless a future explicitly approved feature changes this policy.

---

### NFR-PRIV-004 — Family recordings local — P0

Parent/family voice recordings shall remain on-device in Prototype 0.1.

---

### NFR-PRIV-005 — No tracking identifiers — P0

Miozira shall not intentionally collect advertising IDs, cross-app tracking identifiers, or device fingerprinting data.

---

### NFR-PRIV-006 — No analytics by default — P0

Prototype 0.1 shall operate without remote behavioral analytics.

Family-test evaluation shall use local records and parent observation.

---

### NFR-PRIV-007 — Privacy should survive dependency changes — P0

No library or SDK may be added if its default or required behavior violates Miozira's local-only privacy model.

Every dependency with data-access capability must be reviewed.

---

# 8. Security

### NFR-SEC-001 — Least privilege — P0

The Android application shall request only permissions necessary for enabled Prototype 0.1 features.

---

### NFR-SEC-002 — Microphone permission only when needed — P0

Microphone access shall not be continuously active outside the intended listening window.

---

### NFR-SEC-003 — No exported internal components by accident — P1

Android components that do not need to be externally accessible should not be exported.

---

### NFR-SEC-004 — No secrets in client code — P0

Prototype 0.1 shall not contain production secrets, private API keys, or service credentials embedded in the app package.

---

### NFR-SEC-005 — Safe local file handling — P1

Family recordings and internal app files should be stored within app-private storage unless a documented feature requires otherwise.

---

### NFR-SEC-006 — Parent destructive actions protected — P1

Data reset, recording deletion, and other destructive operations shall be accessible only through the parent experience and shall require explicit confirmation.

---

# 9. Child Safety and Developmental Appropriateness

### NFR-CHILD-001 — No engagement-maximizing design — P0

Miozira shall not deliberately optimize for prolonged screen time.

---

### NFR-CHILD-002 — No manipulative return mechanics — P0

The prototype shall not use:

- streak loss;
- urgency;
- guilt;
- scarcity;
- reward withholding;
- push-notification pressure.

---

### NFR-CHILD-003 — Voluntary interaction — P0

The child experience shall tolerate:

- silence;
- early exit;
- inactivity;
- repeated replay;
- uneven progress.

These behaviors shall not trigger punitive feedback.

---

### NFR-CHILD-004 — Calm audio environment — P0

Learning audio shall avoid unnecessary competing sound.

Continuous background music is prohibited in the core learning loop.

---

### NFR-CHILD-005 — No accidental external navigation — P0

The child interface shall not provide ordinary one-tap paths to:

- web pages;
- app stores;
- external social apps;
- system settings;
- purchases.

---

# 10. Usability

### NFR-UX-001 — Minimal instruction dependence — P0

A child in the target age range should be able to infer the basic interaction pattern after a small number of examples.

Repeated adult explanation should not be required for every item.

---

### NFR-UX-002 — Consistent interaction semantics — P0

The same gesture should produce the same general kind of outcome across child learning screens.

Example:

Tap the main concept → hear the active spoken word.

---

### NFR-UX-003 — Forgiving interaction — P0

The UI shall tolerate reasonable preschool inaccuracies in touch timing and location.

---

### NFR-UX-004 — No dense child navigation — P0

The child experience shall not expose nested settings, tab bars, complex lists, or hierarchical menus.

---

### NFR-UX-005 — Parent area remains compact — P1

The parent experience should expose only information/actions useful to Prototype 0.1.

It should not grow into a full learning-management dashboard.

---

# 11. Accessibility

### NFR-ACC-001 — No text-only critical child instruction — P0

Essential child interaction shall not rely only on written text.

---

### NFR-ACC-002 — No colour-only critical state — P0

Colour shall not be the sole indicator of essential status or action.

---

### NFR-ACC-003 — Contrast — P0

Essential child and parent UI elements shall have sufficient foreground/background contrast for clear visibility.

---

### NFR-ACC-004 — Large interaction targets — P0

Child-facing interactive elements shall be larger and more forgiving than ordinary minimum adult touch targets where practical.

Exact dimensions shall be defined in `ui-ux.md`.

---

### NFR-ACC-005 — Audio clarity — P0

Spoken-word recordings shall prioritize intelligibility over decorative sound design.

---

### NFR-ACC-006 — System accessibility compatibility — P1

The parent area should remain compatible with normal Android accessibility mechanisms where practical, including scalable text and semantic labeling.

---

# 12. Orientation and Adaptive Layout

### NFR-LAYOUT-001 — Portrait and landscape parity — P0

Core child learning functionality shall be available in portrait and landscape.

---

### NFR-LAYOUT-002 — State continuity — P0

Changing orientation shall not:

- count an additional exposure;
- replay audio unintentionally;
- lose the active concept;
- reset the session;
- duplicate a speaking-attempt event.

---

### NFR-LAYOUT-003 — No hardcoded device resolution dependency — P0

The UI shall not assume one exact tablet resolution.

---

### NFR-LAYOUT-004 — Safe-area awareness — P1

Interactive controls shall respect system insets and device cutouts where applicable.

---

# 13. Resource and Battery Usage

### NFR-RES-001 — No continuous background processing — P0

Miozira shall not require continuous background execution for learning or adaptation.

---

### NFR-RES-002 — Microphone window minimisation — P0

The microphone shall be active only for short, intentional listening windows.

---

### NFR-RES-003 — No persistent wake lock — P0

The application shall not keep the device awake indefinitely through a persistent wake lock.

---

### NFR-RES-004 — Modest memory footprint — P1

Prototype 0.1 shall avoid loading all high-resolution visual/audio assets into memory simultaneously.

---

### NFR-RES-005 — Battery-friendly idle state — P1

When the child is not actively interacting, the application should perform minimal ongoing computation.

---

# 14. Storage

### NFR-STOR-001 — Prototype storage remains small — P1

The Prototype 0.1 app data footprint should remain modest.

Because content consists primarily of 16 canonical word recordings, 8 visual assets, lightweight prompts, and local records, ordinary learning data should remain far below hundreds of megabytes.

---

### NFR-STOR-002 — Learning history is compact — P0

Event/history records shall store metadata rather than duplicate media files.

---

### NFR-STOR-003 — Family audio bounded — P1

Parent/family recording duration shall have a reasonable maximum to prevent accidental long recordings.

The exact duration limit will be defined in `audio.md`.

---

### NFR-STOR-004 — Reset removes user-generated data — P1

A confirmed prototype-data reset shall delete locally stored parent observations and family-generated recordings according to the reset policy.

---

# 15. Maintainability

### NFR-MAINT-001 — Separation of concerns — P0

Child UI, parent UI, learning/session logic, persistence, audio, and microphone behavior shall be represented as distinct architectural responsibilities.

---

### NFR-MAINT-002 — No business logic embedded only in UI — P0

Adaptive scheduling and learning-state rules shall not exist solely inside screen-rendering code.

---

### NFR-MAINT-003 — Replaceable microphone implementation — P1

The learning engine shall not depend directly on one Android microphone implementation.

Microphone capability should be behind an internal contract/interface.

---

### NFR-MAINT-004 — Replaceable audio implementation — P1

Core learning logic shall not be tightly coupled to one specific playback library.

---

### NFR-MAINT-005 — Centralized content metadata — P1

Concept IDs, language metadata, canonical audio references, and related content definitions should have one authoritative representation.

---

### NFR-MAINT-006 — Explicit dependency inventory — P1

Third-party libraries shall be documented with:

- purpose;
- version;
- data access;
- permissions impact;
- multiplatform implications where relevant.

---

# 16. Portability

### NFR-PORT-001 — Android-first delivery — P0

No iOS/iPadOS implementation is required for Prototype 0.1.

---

### NFR-PORT-002 — Avoid unnecessary Android coupling — P1

Domain logic, adaptive rules, content models, and persistence abstractions should avoid Android-specific APIs unless required.

---

### NFR-PORT-003 — Platform-specific capability boundaries — P1

Features such as microphone access, audio session behavior, and filesystem location shall have clear platform boundaries.

---

### NFR-PORT-004 — Technology choice must preserve exit options — P1

The architecture should not make a future iPadOS implementation unnecessarily dependent on rewriting the learning model from scratch.

---

### NFR-PORT-005 — Portability is secondary to prototype validity — P0

Prototype 0.1 shall not delay family testing merely to achieve theoretical 100% cross-platform code sharing.

---

# 17. Testability

### NFR-TEST-001 — Learning engine testable without UI — P0

Adaptive/session logic shall be testable independently from tablet UI rendering.

---

### NFR-TEST-002 — Time-dependent logic testable deterministically — P0

Scheduling logic that depends on time shall support deterministic tests rather than requiring real waiting periods.

---

### NFR-TEST-003 — Persistence testability — P0

Database operations and migrations shall be testable using isolated test data.

---

### NFR-TEST-004 — Microphone fallback testable — P0

The app shall support testing states for:

- permission granted;
- permission denied;
- microphone unavailable;
- speech-like activity detected;
- no speech-like activity detected.

---

### NFR-TEST-005 — Offline mode testable — P0

Core test cases shall be executable with network access disabled.

---

### NFR-TEST-006 — Orientation behavior testable — P0

Automated or repeatable manual tests shall verify that configuration/orientation changes do not duplicate or destroy learning state.

---

# 18. Observability Without Tracking

### NFR-OBS-001 — Local diagnostic logging — P1

Development/test builds may use local diagnostic logs to troubleshoot failures.

These logs shall not be transmitted automatically.

---

### NFR-OBS-002 — No sensitive speech logging — P0

Diagnostic logs shall not contain raw child audio.

---

### NFR-OBS-003 — Avoid unnecessary personal data in logs — P0

Logs should use internal concept/session identifiers rather than unnecessary personal information.

---

### NFR-OBS-004 — Production logging restrained — P1

Release builds should avoid verbose logging of child-learning events unless explicitly required for local troubleshooting.

---

# 19. Compatibility

### NFR-COMP-001 — Modern Android target — P0

The prototype shall be developed with modern Android behavior and permission models in mind.

For Google Play distribution beginning 2026-08-31, new apps/updates must target Android 16 / API 36.

---

### NFR-COMP-002 — Supported-device definition required — P1

Before public distribution, Miozira shall explicitly document:

- minimum Android version;
- minimum practical RAM;
- supported screen-size range;
- microphone requirement/fallback;
- storage requirement.

The exact values will be finalized after architecture and device testing.

---

### NFR-COMP-003 — Candidate tablet validation — P1

Prototype 0.1 should be manually tested on at least one of the intended family tablets and, where practical, a lower-capability Android device/emulator profile.

---

# 20. Localization and Language Quality

### NFR-L10N-001 — Unicode-safe content — P0

All text metadata shall safely support Tamil Unicode content.

---

### NFR-L10N-002 — No Latin-only assumptions — P0

Data schemas, file naming strategy, rendering, or validation shall not assume all languages use Latin script.

---

### NFR-L10N-003 — Language-specific audio authoritative — P0

Canonical pronunciation shall be associated with an explicit language identifier, not inferred from UI locale.

---

### NFR-L10N-004 — Parent UI localization deferred — P2

Prototype 0.1 may keep the parent UI in one language if needed for speed, provided child learning audio supports English and Tamil as specified.

---

# 21. Content Asset Quality

### NFR-ASSET-001 — Clear concept visuals — P0

Each concept asset shall unambiguously represent the intended concept for family testing.

---

### NFR-ASSET-002 — No unnecessary visual clutter — P0

Visual assets should not introduce unrelated details that compete with concept identification.

---

### NFR-ASSET-003 — Audio normalization — P1

Canonical recordings should have reasonably consistent perceived loudness so the child does not experience large volume jumps between words.

---

### NFR-ASSET-004 — Asset validation — P1

The build process or QA process should detect missing canonical concept assets before a family-test release.

---

# 22. Error Experience

### NFR-ERR-001 — Child-facing errors remain simple — P0

When recovery is required in child mode, the app shall use a simple neutral state rather than technical language.

---

### NFR-ERR-002 — Parent-facing errors actionable — P1

Parent-facing errors should explain:

- what failed;
- what still works;
- what the parent can do next.

---

### NFR-ERR-003 — Retry without duplicate learning events — P1

Retrying audio or a failed local operation shall not automatically create false duplicate progress evidence.

---

# 23. Family-Test Quality Bar

### NFR-FAMILY-001 — Prototype must feel coherent — P0

Even though Prototype 0.1 is not a commercial release, the family-test build shall be complete enough that obvious technical defects do not dominate the child's behavior.

---

### NFR-FAMILY-002 — No researcher-style intervention required — P0

The parent should not need to manually trigger every item, edit database state, or operate developer tooling during the child's normal test session.

---

### NFR-FAMILY-003 — Test data retrievable locally — P1

The parent/developer shall have a practical way to review enough local evidence after the seven-day test to understand:

- sessions;
- concept-language exposures;
- replays where captured;
- speech-attempt states where enabled;
- real-world observations.

The exact review/export method is deferred.

---

# 24. Quantitative Prototype Targets

These values are **engineering targets for Prototype 0.1**, not child-performance targets.

| Attribute | Prototype target |
|---|---|
| Tap acknowledgement | normally ≤ 100 ms |
| Local target-word playback start | normally ≤ 250 ms |
| Common local screen transition | normally ≤ 300 ms |
| Warm launch usable state | approximately ≤ 2 s |
| Typical cold launch usable state | approximately ≤ 4 s |
| Core internet dependency | 0 |
| Required remote services | 0 |
| Required user accounts | 0 |
| Child audio uploaded | 0 |
| Behavioral analytics SDKs | 0 |
| Ads | 0 |
| Required child reading | 0 |
| Required pronunciation score | 0 |
| Supported orientations | portrait + landscape |

These thresholds should be measured and revised after the first real device build.

---

# 25. Explicit Quality Non-Goals

Prototype 0.1 does **not** require:

- five-nines availability;
- multi-region infrastructure;
- cloud disaster recovery;
- account recovery;
- cross-device synchronization;
- enterprise audit logging;
- server-side rate limiting;
- web accessibility certification;
- desktop support;
- formal medical-device validation;
- production-scale telemetry;
- large-scale performance benchmarking;
- zero-defect commercial polish.

These would be inappropriate for the current prototype.

---

# 26. Architecture Decision Constraints Derived from NFRs

Any proposed architecture should be rejected or reconsidered if it:

1. requires internet for the child loop;
2. requires cloud ASR;
3. makes microphone denial fatal;
4. stores raw child audio by default;
5. forces child learning logic into Android UI code;
6. cannot preserve session state across orientation changes;
7. requires flagship hardware;
8. introduces behavioral tracking;
9. makes future iPadOS portability unnecessarily expensive;
10. adds more technical complexity than needed to test Prototype 0.1.

---

# 27. Requirement Traceability

Key functional requirements supported by these NFRs include:

| Functional requirement area | Primary NFR areas |
|---|---|
| Child loop | Performance, reliability, usability |
| Adaptive learning | Data integrity, maintainability, testability |
| Microphone | Reliability, privacy, security, resource use |
| Parent voice | Storage, privacy, reliability |
| Offline mode | Availability, data integrity |
| Parent area | Security, usability |
| Real-world evidence | Data integrity, privacy |
| Portrait/landscape | Layout, reliability, testability |
| Family validation | Observability, family-test quality |

---

# 28. Definition of Non-Functional Acceptance

Prototype 0.1 may proceed to the seven-day family test only when the team can reasonably demonstrate that:

- the app works without internet;
- the main child interaction feels immediate;
- no child-facing score or failure judgment exists;
- microphone denial does not break the session;
- learning data survives normal restarts;
- orientation changes do not reset or duplicate learning state;
- raw child audio is not persistently stored;
- no behavioral tracking SDK is present;
- core functions operate on target midrange hardware;
- parent observations can be stored;
- obvious crashes or blocking defects have been resolved.

---

# 29. Change Policy

NFR changes should record:

- requirement ID;
- previous target;
- new target;
- reason;
- measurement/evidence;
- affected architecture or tests.

Performance thresholds in particular are expected to evolve after real-device measurement.

Privacy and child-safety constraints should require stronger justification before being weakened.

---

# 30. Next Document Dependency

These non-functional requirements, together with:

- `product-vision.md`
- `prototype-0.1-scope.md`
- `research.md`
- `requirements.md`

form the baseline for defining Miozira's learning semantics.

The recommended next document is:

> **`learning-model.md`**

It should define exactly what Miozira means by exposure, attempt, familiar, due, resting, recognition, use, and other learning states before the adaptive engine or database schema is designed.
