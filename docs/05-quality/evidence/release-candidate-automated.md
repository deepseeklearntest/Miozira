# Prototype 0.1 automated candidate evidence

Date: 2026-08-30

## Passed in this worktree

- `./gradlew :shared:testDebugUnitTest` — PASS (53 tests)
- `./gradlew :androidApp:lintRelease` — PASS
- `./gradlew :androidApp:assembleRelease` — PASS
- Release manifest/offline policy checks — PASS
- Release configuration identity test — PASS (`0.1.0`, content `prototype-0.1-v1`, schema `1`, 8 concepts, 2 languages, 16 pairs)
- `git diff --check` — PASS
- `./gradlew :androidApp:connectedDebugAndroidTest` — PASS (10 tests on `Pixel Tablet`, API 35 emulator).
- Debug APK cold process launch — PASS on the same emulator (`com.miozira` process started successfully after reinstall).
- Room projection compatibility after adaptive/transfer updates — PASS via the same 6 connected instrumentation tests.
- Parent two-step reset controller — PASS in shared unit tests; Android reset execution remains part of device review.
- Repeated reset safety — PASS in shared unit tests; both reset calls return the same deletion summary without inventing data.
- Reset while family-voice UI is open — guarded in the parent flow by cancelling the active recording/preview before reset and refreshing the parent summary.
- Parent actions layout constraint check — PASS in the latest debug build and 9 connected Android tests.
- Controller-based child card accessibility label — PASS in release lint/assembly and connected instrumentation.
- Release dependency denylist — PASS; resolved release groups contain no Firebase, analytics, advertising, or remote-crash SDK group from `config/dependency-denylist.txt`.
- `./gradlew :shared:connectedDebugAndroidTest` — PASS (7 tests on `Pixel Tablet`, API 35 emulator), including lifecycle, privacy, storage-boundary, and idempotent-deletion coverage.
- `ReleasePrivacyPolicyTest` — PASS on the Pixel Tablet emulator.
- 100 interaction transitions with 25 orientation events per transition — PASS.
- 100 microphone windows with one semantic terminal result each and no child-audio files — PASS.
- Launcher activity four-second gate — PASS; direct cold platform launch measured 1,798 ms, and the instrumentation timing gate passed.
- Final integrated command (`shared` unit + instrumentation, app instrumentation, release lint, release assembly) — PASS on 2026-08-30; 7 shared instrumented tests and 10 app instrumented tests passed.
- Emulator smoke checks — PASS: clean debug install, resumed launch in portrait and landscape settings, and launch with Wi-Fi disabled; the emulator has no telephony service for a mobile-data toggle.
- Parent gate/reset emulator smoke — PASS: touchscreen long-press opened the parent area; reset confirmation completed and displayed “Local data reset.”
- Parent navigation emulator smoke — PASS: the back control returned from the parent area to the child card after being moved below the system-bar edge.

## Not executable in this environment

- Release instrumentation was not runnable because the release APK is unsigned in this repository; the debug instrumentation result is not release-signing evidence.
- TalkBack, large text, thermal/battery, real audio quality, and physical-tablet storage review still require dedicated manual execution.
- Physical thermal/battery, audio-quality, Tamil-content, and child-touch review: requires the named primary tablet.

These are release gates, not inferred passes. They must be completed against the exact APK before family testing.
