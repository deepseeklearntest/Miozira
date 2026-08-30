# Technical spike — microphone evidence

**Date:** 2026-08-26
**Build:** local uncommitted debug APK, `com.miozira` 0.1.0
**Detector configuration:** mono 16 kHz PCM, 25 ms frames, 200 ms ambient calibration, 250 ms sustained relative-energy threshold, 2,500 ms default response window.

## Automated emulator checks — PASS

Device: Android 15 Pixel Tablet emulator (`medium_tablet`).

- The deterministic energy tests passed: ambient energy does not create an attempt, 250 ms sustained energy above the measured baseline does, and one impulse does not.
- The Android instrumented test grants only microphone access, opens a short `AudioRecord` window, receives exactly one terminal result, and compares the complete app files/cache snapshots before and after.
- No app-private child-audio file or cache file was created.
- The capture loop uses non-blocking reads, so a 300 ms test window reached its terminal result promptly on the emulator even when no microphone frames were available.

## Physical-tablet checks — PENDING

The emulator does not establish sensitivity or user-facing behavior of a real tablet microphone. Test the primary tablet after Task 1.4 supplies lifecycle ownership.

| Scenario | Expected result | Status |
|---|---|---|
| Quiet child speech | One `ATTEMPT_DETECTED` result | Pending |
| Silence | One `NO_ATTEMPT_DETECTED` result | Pending |
| Tap/handling noise | No false attempt | Pending |
| Nearby adult speech | May count only as weak participation; never identify speaker | Pending |
| Background during capture | Capture stops; no fabricated no-attempt result | Pending |
| Rotation during capture | No duplicate capture or terminal result | Pending |
| Files/cache before and after | No child PCM or temporary recording | Pending |

## Result

The Android technical path is proven to capture and classify transient in-memory PCM on an emulator, with a no-file instrumented guard. The physical calibration and lifecycle checks remain open and are required for Phase 1 exit.
