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

**Gradle build system & multiplatform scaffolding initialized.** Root and module build scripts (`build.gradle.kts`, `settings.gradle.kts`, `gradle/libs.versions.toml`, `gradlew`) are configured for `:shared`, `:androidApp`, and `:iosApp`.

Consequences:
- The Gradle wrapper (`./gradlew`) is available for building multiplatform targets.
- The next planned step is the **Mandatory Technical Spike** (README §5 Step 1): validate tap-to-audio latency ($\le 250\text{ ms}$), memory-only attempt detection, Room persistence, and rotation without duplicated evidence on real Android tablet hardware before feature work.

---

## Where Code Goes

```text
shared/src/commonMain/kotlin/com/miozira/{domain,data,presentation,services}
shared/src/androidMain/kotlin/com/miozira/platform      # audio, mic, permissions, files
shared/src/iosMain/kotlin/com/miozira/platform          # minimal iOS shell
shared/src/commonTest/kotlin/com/miozira                # domain/adaptive-engine unit tests
shared/src/commonMain/resources
androidApp/src/main/{java,res}
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
