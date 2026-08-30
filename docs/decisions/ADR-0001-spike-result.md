# ADR-0001 technical spike result

**Date:** 2026-08-26
**Decision:** `GO_KMP` — provisional emulator GO

## Decision

Keep Kotlin Multiplatform and Compose Multiplatform for Prototype 0.1 development. The Android 15 Pixel Tablet emulator passed the technical spike’s risky software paths: Compose rendering, bundled local audio, memory-only `AudioRecord` capture, idempotent local persistence, stable interaction identity, offline manifest policy, packaging, and instrumentation tests.

This is deliberately an **emulator GO**, not a final device acceptance. Phase 2 may begin. The physical-device gates below remain mandatory before the seven-day family test and before Phase 6 microphone calibration is accepted.

## Tested compatible version set

| Component | Version | Result |
|---|---:|---|
| Kotlin | 2.0.21 | PASS |
| Compose Multiplatform | 1.7.0 | PASS |
| Android Gradle Plugin | 8.6.1 | PASS with compatibility warnings retained |
| Gradle | 8.11.1 | PASS on JDK 17 |
| Room | 2.7.2 | PASS with KSP on emulator |
| KSP | 2.0.21-1.0.28 | PASS |
| Bundled SQLite | 2.6.2 | PASS |

Room 2.8.4 was evaluated and rejected for this toolchain: KSP schema export failed with a Kotlin serialization `AbstractMethodError`. No Kotlin, Compose, AGP, or Gradle upgrade is justified by the emulator spike result.

## Emulator evidence

- Bundled Apple clip emitted `Started`, exposure threshold, and `Completed` in order.
- Rapid-tap contract prevents overlapping player handles.
- A short `AudioRecord` window completed with exactly one terminal semantic result; PCM created no files in app files or cache storage.
- Duplicate Room commits created one row; reopening the same database retained it.
- Stable IDs survived state recreation and stale interaction callbacks could not commit exposure.
- The app stops local audio from `onStop`; the screen’s actual exposure callback commits one `NOT_MEASURED` spike record.
- Debug APK packaging, five Pixel Tablet instrumentation tests, shared unit tests, and the release manifest policy gate passed.
- The Android manifest has `RECORD_AUDIO` as its only Android platform permission and no `INTERNET` permission.

## Warnings retained for the checkpoint

- Kotlin 2.0.21 reports AGP 8.6.1 above its maximum tested AGP 8.5.
- AGP 8.6.1 reports testing only through compileSdk 35 while this project uses compileSdk 36.
- Android SDK XML version 4 exceeds the command-line tool’s understood version 3.
- Gradle reports deprecated build behavior incompatible with Gradle 9.

These warnings were not suppressed. The emulator spike found no KMP, Compose, Room, AudioRecord, instrumentation, or packaging failure caused by them. Recommendation: **A. Keep current versions temporarily for Prototype 0.1**, with Room pinned to 2.7.2. Re-evaluate only if a real device exposes a material issue or before any broader toolchain upgrade.

## Mandatory physical-device gates — not passed

- Ten primary-tablet tap-to-audible-start measurements must show normal audio start within 250 ms; emulator output cannot prove this.
- Physical microphone tests must cover quiet child speech, silence, tap noise, nearby adult speech, backgrounding, and rotation; emulator output cannot establish family-environment reliability.
- Physical rotation/background/foreground, TalkBack focus, airplane-mode interaction, and force-stop/relaunch of the integrated spike must be recorded.

Until those checks pass, do not claim real-device audio latency compliance or reliable family-environment microphone detection. The physical proof is required before the seven-day family test and before final Phase 6 microphone calibration.
