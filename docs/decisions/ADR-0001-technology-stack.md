# ADR-0001 — Technology Stack for Miozira Prototype 0.1

**Document:** `ADR-0001-technology-stack.md`  
**ADR:** 0001  
**Status:** Accepted for Prototype 0.1  
**Date:** 2026-08-25  
**Product:** Miozira  
**Scope:** Android-first family-testable vertical slice with future iPadOS portability

---

## 1. Decision

Miozira Prototype 0.1 will use:

> **Kotlin Multiplatform (KMP) + Compose Multiplatform (CMP) + Room/SQLite + DataStore + platform-specific audio/microphone adapters**

Primary implementation target:

> **Android tablet first**

Future target:

> **iPadOS**

The implementation should remain lean:

- one Android app;
- one shared KMP module;
- minimal build-module count;
- shared domain logic;
- shared data/repository logic where practical;
- shared Compose UI where straightforward;
- platform-specific audio, microphone, permission, file, and lifecycle integrations where needed.

The project shall not force 100% code sharing.

---

## 2. Context

Miozira Prototype 0.1 needs:

- fully offline behavior;
- deterministic adaptive learning logic;
- local relational persistence;
- bundled visual/audio content;
- low-latency audio playback;
- short child microphone attempt-detection windows;
- parent family-voice recording;
- portrait/landscape state continuity;
- explicit lifecycle handling;
- strong privacy boundaries;
- no backend;
- no cloud ASR;
- future iPadOS portability.

The core product loop remains:

```text
Tap picture
→ hear word
→ optional child speaking attempt
→ gentle response
→ adaptive next item
```

---

## 3. Decision Drivers

The stack is evaluated against:

1. Android delivery speed
2. Audio/microphone control
3. Offline/local database maturity
4. Lifecycle reliability
5. State-management fit
6. iPadOS portability
7. Ability to share adaptive/domain logic
8. UI sharing potential
9. Debugging/tooling maturity
10. AI-assisted development friendliness
11. Solo-builder maintainability
12. Dependency/privacy risk
13. Long-term escape options
14. Complexity relative to Prototype 0.1

---

## 4. Options Considered

1. Kotlin Multiplatform + Compose Multiplatform
2. Native Android Kotlin + Jetpack Compose
3. Flutter
4. React Native

---

# 5. Option A — Kotlin Multiplatform + Compose Multiplatform

## Summary

Use KMP for shared domain/data code and CMP for shared Android/iOS UI where practical.

Possible stack:

```text
Kotlin Multiplatform
Compose Multiplatform
Room / SQLite
DataStore
Coroutines / Flow
Android AudioRecord
platform audio playback adapters
future iOS AVFoundation adapters
```

## Advantages

### Shared domain logic

Excellent candidates for shared Kotlin:

- learning model;
- adaptive engine;
- session selection;
- state transitions;
- real-world transfer;
- repositories/contracts;
- content metadata;
- validation.

### Android-native access remains direct

KMP still allows native Android code for:

```text
SpeechAttemptDetector
```

using APIs such as:

```text
AudioRecord
```

### Strong offline-data fit

Room provides structured SQLite-backed storage and supports KMP.

### Coroutines fit the interaction model

Miozira has asynchronous:

- audio;
- microphone windows;
- local database writes;
- lifecycle cancellation.

Kotlin coroutines fit this well.

### Compose matches the UI architecture

Miozira's UI is:

- state-driven;
- minimal;
- responsive;
- orientation-sensitive.

### Credible iPadOS path

A future iOS app can reuse domain/data logic and, where suitable, Compose UI while replacing native service adapters.

## Disadvantages

### More setup than Android-only

KMP adds:

- multiplatform Gradle configuration;
- source sets;
- iOS target setup;
- Xcode integration;
- compatibility management.

### Apple tooling still required

KMP does not eliminate:

- Xcode;
- signing;
- provisioning;
- iOS-specific debugging.

### Shared UI is not free

Accessibility and native behavior still require platform testing.

---

# 6. Option B — Native Android Kotlin + Jetpack Compose

## Advantages

- fastest Android path;
- simplest debugging;
- direct Android APIs;
- mature Compose/Room/DataStore tooling;
- strongest native lifecycle integration.

## Disadvantages

- future iPadOS duplication;
- adaptive/session logic would need extraction or rewrite;
- portability debt begins immediately;
- later migration could disrupt a growing codebase.

## Assessment

Best option if the only goal were:

> get one Android prototype running as quickly as possible.

Not selected because Miozira has unusually valuable shareable domain logic and a credible iPadOS direction.

---

# 7. Option C — Flutter

## Advantages

- mature cross-platform framework;
- shared Android/iOS UI;
- strong hot reload;
- broad package ecosystem;
- good prototype velocity.

## Disadvantages for Miozira

- Dart becomes the primary language;
- sensitive audio/mic behavior may still require native integrations;
- less direct alignment with Room/DataStore/Android-native APIs;
- more dependence on plugin review for privacy-sensitive capabilities.

## Assessment

A credible second cross-platform choice, but KMP fits Miozira's Android-native and domain-sharing needs better.

---

# 8. Option D — React Native

## Advantages

- large React/TypeScript ecosystem;
- substantial shared UI;
- production-ready New Architecture;
- native module support.

## Disadvantages for Miozira

- native audio/mic work becomes a central cross-boundary concern;
- TypeScript/React Native runtime plus Kotlin/Swift modules adds layers;
- less direct fit with the Kotlin/Android-first architecture;
- more complexity than justified for an 8-concept offline prototype.

## Assessment

Not selected.

---

# 9. Evaluation Matrix

Scoring:

```text
5 = excellent
4 = strong
3 = acceptable
2 = weak
1 = poor
```

| Criterion | KMP + CMP | Native Android | Flutter | React Native |
|---|---:|---:|---:|---:|
| Android delivery speed | 4 | 5 | 4 | 3 |
| Android audio/mic control | 5 | 5 | 3 | 3 |
| Offline database fit | 5 | 5 | 4 | 3 |
| Lifecycle control | 5 | 5 | 4 | 3 |
| Adaptive/domain sharing | 5 | 2 | 5 | 5 |
| Future iPadOS portability | 5 | 2 | 5 | 5 |
| Shared UI potential | 5 | 1 | 5 | 5 |
| Native escape hatch | 5 | 5 | 4 | 4 |
| Kotlin/Android ecosystem fit | 5 | 5 | 1 | 2 |
| Tooling simplicity | 3 | 5 | 4 | 3 |
| Solo-builder maintainability | 4 | 5 | 4 | 3 |
| Privacy/dependency control | 5 | 5 | 4 | 3 |
| Avoid future rewrite | 5 | 2 | 5 | 5 |
| Fit for Miozira architecture | 5 | 4 | 4 | 3 |

Overall:

```text
KMP + CMP       strongest overall fit
Native Android  strongest short-term simplicity
Flutter         credible cross-platform alternative
React Native    least attractive for this prototype
```

---

# 10. Weighted Decision

The highest-weight Miozira criteria are:

- audio/microphone control;
- offline reliability;
- domain sharing;
- future iPadOS portability;
- solo-builder maintainability.

Because these matter more than absolute first-week implementation speed, KMP + CMP wins over Android-only development.

---

# 11. Accepted Stack

Use:

> **Kotlin Multiplatform + Compose Multiplatform**

with:

> **Room/SQLite for structured local data**

and:

> **DataStore or equivalent for small preferences**

and:

> **platform-specific adapters for audio, microphone, permissions, files, and lifecycle behavior**

---

# 12. Important Qualification

This decision does **not** mean:

> share everything.

Miozira will:

> **share what is stable and valuable; keep platform-specific code where platform behavior matters.**

---

# 13. Code-Sharing Policy

## Strongly prefer shared code for

- learning models;
- adaptive engine;
- session engine;
- real-world transfer logic;
- content metadata;
- repository interfaces;
- repository/data logic where supported;
- database models/schema where practical;
- state-machine logic.

## May share UI for

- child concept screen;
- listening indicator;
- response cue;
- parent cards;
- observation controls;
- recording-management UI;
- settings.

## Keep platform-specific where needed

- Android `AudioRecord`;
- iOS audio capture;
- audio focus/session handling;
- permission prompts;
- app-private file locations;
- lifecycle integration;
- platform-specific accessibility behavior.

---

# 14. Compose Multiplatform UI Decision

Prototype 0.1 will begin with:

> **shared Compose Multiplatform UI**

because the child UI is intentionally small and state-driven.

However, if shared UI materially slows the Android family-test milestone, Android UI quality takes priority over code-sharing purity.

---

# 15. UI Sharing Is Not a KPI

Do not measure success by:

- percentage of shared files;
- percentage of shared UI;
- number of platform-specific lines.

Measure success by:

- reliable child experience;
- clear architecture;
- Android delivery;
- future portability without duplicating the learning engine.

---

# 16. Database Decision

Use:

> **Room with SQLite**

for structured learning data.

Reasons:

- relational model;
- Android maturity;
- KMP support;
- migrations;
- type-safe access;
- testability.

---

# 17. Preferences Decision

Use a lightweight preference store such as:

> **DataStore**

for small settings such as:

- setup complete;
- starting language;
- microphone enabled.

Do not put structured learning history in DataStore.

---

# 18. Audio Playback Decision Boundary

This ADR does not choose a third-party playback library.

Expose:

```text
AudioService
```

and let `audio.md` plus the technical spike decide whether the implementation uses:

- platform playback APIs;
- Media3;
- a multiplatform library;
- another lightweight adapter.

---

# 19. Microphone Decision Boundary

Child speaking-attempt detection shall use a platform-native adapter.

Android leading direction:

> `AudioRecord`

because Miozira needs:

- short PCM windows;
- local signal analysis;
- no file creation;
- no cloud service.

Future iOS uses the corresponding native audio-capture implementation.

---

# 20. Family Recording Boundary

Family voice recording remains separate from child attempt detection.

It may use a higher-level recording API because it intentionally creates a local file.

---

# 21. Project Structure

Recommended initial structure:

```text
miozira/
│
├── androidApp/
├── iosApp/                    # minimal shell / future target
├── shared/
│   └── src/
│       ├── commonMain/
│       │   ├── presentation/
│       │   ├── domain/
│       │   ├── data/
│       │   └── services/
│       ├── androidMain/
│       │   └── platform/
│       └── iosMain/
│           └── platform/
└── content/
```

Keep the module count lean.

---

# 22. Initial Build Modules

Recommended:

```text
:androidApp
:shared
```

plus the iOS shell required by the KMP/CMP project.

Split further only when there is a concrete benefit.

---

# 23. Language

Primary implementation language:

> **Kotlin**

iOS-specific integration may require Swift/Apple APIs.

No Dart or TypeScript application runtime is introduced.

---

# 24. Concurrency

Use:

> **Kotlin coroutines**

for:

- database operations;
- audio coordination;
- microphone windows;
- state/effect execution.

Use structured concurrency.

---

# 25. State Model

Use explicit state/events aligned with `interaction-states.md`.

Conceptually:

```text
State + Event
→ reducer/controller
→ New State + Effects
```

Compose recomposition must never trigger domain effects.

---

# 26. Dependency Injection

Start with:

> **constructor injection + simple composition root**

Do not add a heavy DI framework unless project growth creates a real need.

---

# 27. Networking

Prototype 0.1 runtime shall have:

> **no network client**

and should aim to omit Android:

> `INTERNET`

permission.

---

# 28. Analytics

No remote analytics SDK.

Family-test evidence remains local.

---

# 29. Logging

Use restrained local development logging.

Do not log:

- raw child speech;
- family recording contents;
- unnecessary personal information.

---

# 30. Minimum Platform Targets

Exact minimum OS versions will be finalized after technical spike and device testing.

Miozira should choose minimums based on realistic family hardware, not on maximum framework reach.

---

# 31. Android Build Target

For Play-distributable builds, target the applicable current Google Play Android API requirement at release time.

Target SDK and minimum Android version remain separate decisions.

---

# 32. iOS Development Environment

Future iOS delivery still requires:

- macOS;
- Xcode;
- signing/provisioning;
- iOS platform testing.

KMP does not remove these requirements.

---

# 33. AI-Assisted Development Consideration

Miozira will use significant AI coding assistance.

Kotlin, Android, Compose, and coroutines have broad official documentation and examples.

KMP/CMP build configuration must still be checked against current official docs because:

- Gradle;
- Kotlin;
- KSP;
- Room;
- Android Gradle Plugin;
- Xcode compatibility

change over time.

Keep build files simple.

---

# 34. Solo-Builder Consideration

KMP/CMP has more setup cost than Android-only development.

That cost is accepted because Miozira has:

- highly shareable non-UI logic;
- a credible iPadOS goal;
- a very small child UI;
- clear platform-specific boundaries.

The project must not turn KMP into a framework experiment.

---

# 35. Why Native Android Was Not Selected

Native Android is not technically inferior.

It was not selected because:

- domain logic is highly shareable;
- iPadOS is a real direction;
- later extraction would create avoidable migration work.

If the spike shows KMP/CMP materially blocks the family test, native Android is the fallback.

---

# 36. Why Flutter Was Not Selected

Flutter remains credible.

It was not selected because Miozira places unusual emphasis on:

- Android-native audio capture;
- Kotlin ecosystem alignment;
- Room KMP;
- shared Kotlin domain logic;
- direct native escape hatches.

---

# 37. Why React Native Was Not Selected

React Native was not selected because:

- non-trivial native audio/mic work is central;
- the JS/native boundary adds another layer;
- the project gains too little from React/TypeScript to justify that layer;
- KMP better matches the domain/native profile.

---

# 38. Mandatory Technical Spike

Before full implementation, build a small spike proving:

```text
1. KMP/CMP project builds
2. Android child concept screen renders
3. bundled local word audio plays
4. Android AudioRecord opens briefly
5. simple speech-attempt signal can be produced
6. raw child audio is not saved
7. Room stores one exposure/attempt
8. portrait ↔ landscape preserves interaction identity
9. app restart retains persisted state
10. no runtime network dependency exists
```

Optional:

```text
11. minimal iOS target compiles
```

---

# 39. Spike Failure Criteria

Revisit this ADR if the spike reveals:

- unacceptable audio latency;
- unstable microphone lifecycle;
- major CMP accessibility blocker;
- excessive KMP build complexity;
- Room KMP issues that materially slow Prototype 0.1;
- significantly impaired Android debugging;
- shared UI requiring disproportionate work.

---

# 40. Fallback

If KMP/CMP fails the spike:

> fallback to **native Android Kotlin + Jetpack Compose**

while preserving:

- domain boundaries;
- repository interfaces;
- service interfaces;
- database schema;
- state-machine architecture.

---

# 41. Positive Consequences

Choosing KMP/CMP means:

- adaptive logic can be shared;
- session semantics stay single-source;
- future iPadOS avoids a full rewrite;
- Room provides a shared persistence path;
- Android-native low-level capabilities remain available;
- one primary language ecosystem dominates the project;
- shared UI is available but optional;
- local-only privacy remains straightforward.

---

# 42. Negative Consequences

The project accepts:

- more setup than Android-only;
- multiplatform Gradle complexity;
- Xcode/iOS integration;
- some platform-specific code;
- version compatibility tracking;
- possible UI divergence later.

These tradeoffs are accepted.

---

# 43. Decision Guardrails

### ADR-G01
Do not build web/desktop targets.

### ADR-G02
Do not optimize for code-sharing percentage.

### ADR-G03
Do not introduce platform abstractions for trivial code without need.

### ADR-G04
Do not wrap every library in multiple layers.

### ADR-G05
Do not create backend abstractions for a nonexistent backend.

### ADR-G06
Do not add heavy DI without need.

### ADR-G07
Do not add an event bus beyond the explicit state/event model.

### ADR-G08
Do not delay Android family testing for iOS polish.

### ADR-G09
Do not depend on experimental framework features for the core loop.

### ADR-G10
Keep platform-specific audio/mic code explicit and small.

---

# 44. Dependency Policy

Prefer:

1. official Android/Jetpack/Kotlin libraries;
2. well-maintained multiplatform libraries with clear privacy behavior;
3. small platform-native implementations for sensitive capabilities.

This is especially important for:

- microphone;
- recording;
- analytics;
- networking.

---

# 45. Initial Technology Baseline

Use current stable releases when implementation starts.

Logical baseline:

```text
Kotlin
Kotlin Multiplatform
Compose Multiplatform
Coroutines
Room KMP
SQLite
DataStore
Android AudioRecord
platform audio playback
future iOS native audio adapters
```

Exact version pinning belongs in the repository dependency catalog, not this ADR.

---

# 46. Review After Family Test

Review the stack only if technology itself creates meaningful problems such as:

- crashes;
- audio latency;
- iteration difficulty;
- device compatibility issues;
- accessibility blockers;
- excessive build/debug burden.

Do not rewrite the stack merely because another framework becomes fashionable.

---

# 47. Status

**Accepted**

Implementation may proceed using KMP/CMP, subject to the mandatory technical spike.

---

# 48. Decision Summary

Miozira Prototype 0.1 will use:

```text
Kotlin Multiplatform
+
Compose Multiplatform
+
Room / SQLite
+
DataStore
+
Kotlin Coroutines
+
Platform-specific Audio/Microphone adapters
```

Why:

- Android remains first-class;
- core learning logic is highly shareable;
- future iPadOS is credible;
- KMP and CMP are stable for Android/iOS;
- Google officially supports KMP for Android/iOS business-logic sharing;
- Room supports KMP;
- low-level native microphone access remains available;
- the architecture avoids a later full learning-engine rewrite.

Fallback:

> Native Android Kotlin + Jetpack Compose if the technical spike demonstrates that KMP/CMP materially threatens the family-test milestone.

This gives Miozira a cross-platform foundation without making cross-platform purity more important than validating the child-learning experience.
