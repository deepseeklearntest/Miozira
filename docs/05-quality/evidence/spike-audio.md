# Technical spike — bundled audio evidence

**Date:** 2026-08-26
**Build:** local uncommitted debug APK, `com.miozira` 0.1.0
**Temporary asset:** `word_apple_en_spike.m4a` — synthetic technical-spike clip only; not approved canonical content.

## Automated emulator checks — PASS

Device: Android 15 Pixel Tablet emulator (`medium_tablet`).

- `AndroidAudioServiceTest` passed: the packaged asset emitted `Started`, `ExposureThresholdReached`, and `Completed`, in order.
- `SpikeAudioAssetTest` passed: the clip is readable from the Android asset package.
- The complete instrumentation suite passed (three tests): audio playback, asset packaging, and manifest privacy policy.
- The debug APK installed and launched. The full-screen Apple tap target was present; single and double taps completed without a `com.miozira` crash.
- The temporary clip has an audible waveform (peak -3.5 dB; RMS -17.8 dB), but direct emulator audibility was inconclusive because its music stream was initially 5/15 and the host audio route is not a reliable device measurement.

## Physical-tablet latency evidence — PENDING

The emulator proves packaging, player callbacks, and basic interaction only. It cannot establish speaker latency, volume, or interruption behavior on the primary family tablet.

| Trial | Tap acknowledgement ≤100 ms | Audible audio start ≤250 ms | Notes |
|---|---:|---:|---|
| 1 | Pending | Pending | Requires primary physical tablet |
| 2 | Pending | Pending | Requires primary physical tablet |
| 3 | Pending | Pending | Requires primary physical tablet |
| 4 | Pending | Pending | Requires primary physical tablet |
| 5 | Pending | Pending | Requires primary physical tablet |
| 6 | Pending | Pending | Requires primary physical tablet |
| 7 | Pending | Pending | Requires primary physical tablet |
| 8 | Pending | Pending | Requires primary physical tablet |
| 9 | Pending | Pending | Requires primary physical tablet |
| 10 | Pending | Pending | Requires primary physical tablet |

## Interruption behavior — PENDING

Run on the primary physical tablet once Task 1.4 owns lifecycle stop behavior. Confirm that backgrounding stops playback and that returning to the app does not invent an exposure completion.

## Result

The local bundled-audio path is technically proven on the Android emulator. Task 1.1 remains open until the ten physical-tablet latency observations are recorded.
