# Phase 3 — Local persistence evidence

**Date:** 2026-08-30
**Status:** PASS — Pixel Tablet emulator validation
**Scope:** Phase 3 only; no Phase 4 implementation started.

## What this proves

- The on-device Room database seeds exactly 8 concepts, 2 languages, 16 concept-language pairs, and 16 `NEW` pair-learning states. Repeating the seed creates no duplicate rows, and foreign keys reject invalid pairs.
- Interaction evidence is idempotent: duplicate threshold, completion, and attempt callbacks result in one scheduled exposure, the correct replay count, and one preserved terminal attempt value.
- Planned-but-unseen interactions and interruptions before the exposure threshold do not create exposure evidence. Interruption after the threshold preserves the one real exposure.
- Derived pair-learning state can be rebuilt after closing and reopening the database.
- Settings stored in DataStore persist across repository recreation.
- Startup recovery closes interrupted sessions as `APP_EXITED`, marks incomplete interactions as `PROCESS_INTERRUPTION`, and does not invent exposure or attempt evidence.
- The exported Room schema contains the required local entities and has no raw child-audio, transcript, pronunciation-score, or analytics columns.

These are repository-level guarantees. Phase 4 must connect real child-session callbacks to these repositories before a child tap can persist learning evidence.

## Automated validation

Command run:

```text
./gradlew :shared:testDebugUnitTest :shared:connectedDebugAndroidTest :androidApp:connectedDebugAndroidTest :androidApp:assembleDebug :androidApp:checkManifestPolicy
```

Result: **BUILD SUCCESSFUL** in 29 seconds.

- Shared unit tests: passed.
- Shared Android instrumentation tests: 5 passed on the Pixel Tablet emulator.
- Android-app instrumentation tests: 6 passed on the Pixel Tablet emulator.
- Debug APK assembly: passed.
- Manifest policy: passed (`RECORD_AUDIO` only; no `INTERNET` permission).
- `git diff --check`: passed.

Known compatibility warnings from the existing technical spike remain visible and were not suppressed: Kotlin/AGP tested-version notice, `compileSdk 36` notice, and Gradle 9 deprecation notices.

## Manual emulator check

The debug APK was installed on the local Pixel Tablet emulator (Android 15). The app launched to the child content screen. After a force-stop and relaunch, the same screen remained available, and the crash check returned no crashes.

## Not proven here

- The READY, playback-before-threshold, playback-after-threshold, and listening manual lifecycle matrix cannot be exercised until Phase 4 supplies the child interaction state machine. This is explicitly deferred to that phase.
- This does not replace the physical-tablet audio latency and real-microphone gates required before the seven-day family test.
- Phase 2's fluent-review and physical-device content gates remain separate from this persistence result.

## Conclusion

Phase 3's persistence exit criteria are satisfied at the repository boundary: committed evidence survives restart, rebuilds pair state, and duplicate callbacks do not change totals.
