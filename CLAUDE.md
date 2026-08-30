# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

[AGENTS.md](AGENTS.md) is the canonical operating guide — read it in full before writing production code. This file is the auto-loaded summary plus repo-specific orientation.

---

## Quick Reference Summary

- **Product:** Miozira Prototype 0.1 (Offline multilingual learning for pre-readers, age 4–5).
- **Curriculum:** 8 concepts (`apple`, `ball`, `cup`, `hand`, `nose`, `cat`, `car`, `water`) in English + Tamil.
- **Invariants:** 100% offline, zero network access, zero cloud, zero tracking, no gamification (no streaks, XP, timers, scores), low-stimulation palette (`#F2EEE7`), memory-only speech attempt detection.
- **Tech Stack:** Kotlin Multiplatform + Compose Multiplatform + Room / DataStore + Kotlin Coroutines (Android tablet first).
- **Documentation:** Single source of truth is located in [`docs/`](docs/README.md).
- **Open Questions:** Tracked in [`docs/OPEN-QUESTIONS.md`](docs/OPEN-QUESTIONS.md).

---

## Current Repository State

**Gradle build verified working.** `:shared` (KMP, Android target only so far) and `:androidApp` build, test, and assemble cleanly. See [`docs/03-engineering/development-environment.md`](docs/03-engineering/development-environment.md) for full host-tool setup (JDK 17, Android SDK).

Verified emulator command (run from repo root):

```bash
./gradlew :shared:testDebugUnitTest :shared:connectedDebugAndroidTest :androidApp:connectedDebugAndroidTest :androidApp:assembleDebug :androidApp:checkManifestPolicy
```

- `:shared:testDebugUnitTest` — common Kotlin unit tests (`shared/src/commonTest`).
- `:shared:connectedDebugAndroidTest` and `:androidApp:connectedDebugAndroidTest` — Room/repository and Android integration tests on an emulator or device.
- `:androidApp:assembleDebug` — builds the debug APK.
- `:androidApp:checkManifestPolicy` — fails the build if the merged manifest's `android.permission.*` set drifts from [`config/android-permission-allowlist.txt`](config/android-permission-allowlist.txt) (currently `RECORD_AUDIO` only) or gains `INTERNET`. This is the CI-enforced form of the no-network invariant, not just a docs claim.
- Instrumented tests need a device/emulator to run; `./gradlew :androidApp:compileDebugAndroidTestKotlin` verifies the Android-app tests compile without one.

Implemented through Phase 3: Android audio and memory-only speech-attempt technical spikes, the draft content-review surface, a Room-backed local schema and idempotent evidence persistence, DataStore settings, and safe startup recovery. The iOS target/shell, production child session state machine, adaptive selection, and parent area are not yet built.

The next planned implementation phase is the **child session state machine**. Physical-tablet audio latency and real-microphone validation remain mandatory before the seven-day family test; emulator results must not be presented as physical-device proof.

---

## Where Code Goes

```text
shared/src/commonMain/kotlin/com/miozira/{domain,data,presentation,services}
shared/src/androidMain/kotlin/com/miozira/platform      # audio, mic, permissions, files
shared/src/iosMain/kotlin/com/miozira/platform          # minimal iOS shell
shared/src/commonTest/kotlin/com/miozira                # domain/adaptive-engine unit tests
shared/src/commonMain/resources
androidApp/src/main/{kotlin,res}
androidApp/src/androidTest/kotlin/com/miozira        # instrumented tests (need a device/emulator)
iosApp/
content/concepts/<concept>/                             # illustrations + en/ta audio, 8 concepts
```

`services/` is a sibling package holding the internal service contracts from
[`docs/03-engineering/api.md`](docs/03-engineering/api.md) (audio playback, attempt detector,
repository, settings). It is not a fifth layer in the `Presentation → Domain → Data → Platform`
dependency chain — implementations live in `platform`, interfaces are consumed by `domain`.

---

## Read These First

`docs/README.md` indexes all 39 specs. For most tasks, these four carry the load:

1. [`docs/00-foundation/prototype-0.1-scope.md`](docs/00-foundation/prototype-0.1-scope.md) — the scope boundary; check before adding anything.
2. [`docs/03-engineering/architecture.md`](docs/03-engineering/architecture.md) — layers, state ownership, dependency direction.
3. [`docs/05-quality/acceptance-criteria.md`](docs/05-quality/acceptance-criteria.md) — the definition of done. Compiling is not done.
4. [`docs/decisions/ADR-0001-technology-stack.md`](docs/decisions/ADR-0001-technology-stack.md) — accepted stack, plus the native-Android fallback condition.

Then the doc closest to the work: `database.md`, `audio.md`, `microphone.md`, `api.md`, `permissions.md` (engineering); `design-system.md`, `color-palette.md`, `interaction-states.md` (design); `adaptive-engine.md`, `session-design.md` (learning).

---

## Invariants That Fail Review

Full rationale and the complete prohibited list are in [AGENTS.md](AGENTS.md). The ones that
silently break the product if violated:

- `android.permission.INTERNET` must be absent from the manifest. No backend, no analytics SDK, no remote config, no cloud ASR.
- Child microphone PCM stays in memory. Never written to disk, never transcribed, never scored. Family voice recordings are a separate, adult-initiated feature writing to app-private storage.
- No gamification of any kind (streaks, XP, timers, scores, badges, confetti, mascot pressure, correctness colors) — including "just for the spike."
- Exposure and evidence writes must be idempotent; audio/mic callbacks carry session/interaction IDs so stale callbacks are rejected across rotation and lifecycle events.
- UI never calls DAOs directly.

When a spec contradicts another spec or blocks Prototype 0.1, log it in
[`docs/OPEN-QUESTIONS.md`](docs/OPEN-QUESTIONS.md) rather than deciding silently.
