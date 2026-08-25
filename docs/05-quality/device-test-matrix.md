# Miozira — Device Test Matrix

**Document:** `device-test-matrix.md`  
**Version:** 0.1  
**Status:** Draft / Execution Template  
**Product:** Miozira  
**Last updated:** 2026-08-25

---

## 1. Purpose

This document defines the device/OS matrix for Prototype 0.1 and provides a simple execution record.

The family test must not begin until the primary physical tablet passes the mandatory rows.

---

# 2. Device Classes

## D1 — Primary family tablet

**Required:** Yes  
**Purpose:** authoritative real-world family-test device.

Record:

```text
Manufacturer:
Model:
RAM:
Storage:
Android version:
API level:
Screen size:
Resolution:
Build version:
```

---

## D2 — Current Android emulator

**Required:** Yes

Target:

```text
Android 16 / API 36
tablet profile
```

Purpose:

- current target-SDK behavior;
- lifecycle;
- permissions;
- orientation;
- database.

---

## D3 — Minimum-SDK emulator

**Required:** Yes

Target:

```text
final selected minSdk
tablet/large-screen profile where possible
```

Purpose:

- compatibility floor.

---

## D4 — Additional modest Android tablet

**Required:** Preferred, not mandatory before family test.

Suggested profile:

```text
4 GB RAM
mid-range CPU
8–11 inch screen
```

Purpose:

- lower-common-denominator sanity check.

---

## D5 — iPad/iOS compile/render sanity

**Required:** No before family test.

Purpose:

- future portability check only.

---

# 3. Mandatory Test Dimensions

Every required Android target should cover, where applicable:

```text
launch
portrait
landscape
rotation
audio
microphone permission
microphone denied
background/foreground
screen lock
offline
database persistence
reset
```

---

# 4. Primary Device Matrix

| Test | Portrait | Landscape | Pass/Fail | Notes |
|---|---:|---:|---|---|
| Cold launch | ✓ | ✓ |  |  |
| Warm launch | ✓ | ✓ |  |  |
| Child first item | ✓ | ✓ |  |  |
| Tap → word | ✓ | ✓ |  |  |
| Replay | ✓ | ✓ |  |  |
| Mic attempt detected | ✓ | ✓ |  |  |
| Mic no-attempt | ✓ | ✓ |  |  |
| Mic disabled | ✓ | ✓ |  |  |
| Mic denied | ✓ | ✓ |  |  |
| Parent gate | ✓ | ✓ |  |  |
| Mark recognized | ✓ | ✓ |  |  |
| Mark used | ✓ | ✓ |  |  |
| Family recording | ✓ | ✓ |  |  |
| Reset | ✓ | ✓ |  |  |
| Airplane mode | ✓ | ✓ |  |  |

---

# 5. Rotation Matrix

Rotate during each state:

| State | P→L | L→P | Duplicate evidence? | Pass/Fail |
|---|---:|---:|---:|---|
| PRESENTING |  |  | Must be No |  |
| READY |  |  | Must be No |  |
| PLAYING_WORD |  |  | Must be No |  |
| INVITING |  |  | Must be No |  |
| LISTENING |  |  | Must be No |  |
| RESPONDING |  |  | Must be No |  |
| TRANSITIONING |  |  | Must be No |  |
| Parent home |  |  | N/A |  |
| Family recording screen |  |  | N/A |  |

---

# 6. Audio Route Matrix

| Route | Required | Result | Notes |
|---|---:|---|---|
| Built-in speaker | Yes |  |  |
| Wired headphones | Optional |  |  |
| Bluetooth speaker/headphones | Optional sanity |  |  |

Built-in speaker is the authoritative family-test route.

---

# 7. Microphone Matrix

| Scenario | Expected | Result |
|---|---|---|
| Permission granted | Short listening window works |  |
| Permission not determined | Adult explanation → system dialog |  |
| Permission denied | Child flow continues |  |
| Permanently denied | Parent can open settings if desired |  |
| Permission revoked later | Child flow degrades safely |  |
| Mic busy | Child flow continues |  |
| Background during mic | Capture stops |  |
| Screen lock during mic | Capture stops |  |

---

# 8. Noise Matrix

Physical-device only.

| Environment | Attempt speech | Silence | Notes |
|---|---|---|---|
| Quiet room |  |  |  |
| Fan/AC |  |  |  |
| TV moderate |  |  |  |
| Parent nearby |  |  |  |
| Tap/handling noise |  |  |  |

Goal:

> usable weak signal, not perfect classification.

---

# 9. Offline Matrix

| State | Wi-Fi On | Wi-Fi Off | Airplane Mode |
|---|---|---|---|
| Launch |  |  |  |
| Start session |  |  |  |
| Canonical audio |  |  |  |
| Adaptive selection |  |  |  |
| Parent observation |  |  |  |
| Family recording |  |  |  |
| Reset |  |  |  |

Expected:

> no material difference in core functionality.

---

# 10. Persistence Matrix

| Scenario | Expected | Result |
|---|---|---|
| Normal app restart | Learning state persists |  |
| Kill after exposure committed | Exposure persists |  |
| Kill before exposure threshold | No exposure fabricated |  |
| Kill during mic window | No fake no-attempt |  |
| Kill during family recording | Temp recovered/cleaned |  |
| Restart after reset | Fresh learning state |  |

---

# 11. Performance Matrix

On primary device record:

```text
Cold launch:
Warm launch:
Tap feedback median:
Tap-to-audio median:
Tap-to-audio worst observed:
Active memory:
10-minute thermal observation:
```

---

# 12. Accessibility Matrix

| Test | Portrait | Landscape | Result |
|---|---:|---:|---|
| Large parent font | ✓ | ✓ |  |
| TalkBack parent navigation | ✓ | ✓ |  |
| Reduced motion | ✓ | ✓ |  |
| Touch targets | ✓ | ✓ |  |
| Tamil rendering | ✓ | ✓ |  |

---

# 13. Content Playback Matrix

Each pair must be verified once on the primary device:

```text
apple.en   [ ]
apple.ta   [ ]
ball.en    [ ]
ball.ta    [ ]
cup.en     [ ]
cup.ta     [ ]
hand.en    [ ]
hand.ta    [ ]
nose.en    [ ]
nose.ta    [ ]
cat.en     [ ]
cat.ta     [ ]
car.en     [ ]
car.ta     [ ]
water.en   [ ]
water.ta   [ ]
```

For each verify:

- correct image;
- correct language;
- clear audio;
- no clipping;
- reasonable loudness.

---

# 14. Test Result Status

Use:

```text
PASS
FAIL
BLOCKED
NOT APPLICABLE
NOT TESTED
```

Do not count `NOT TESTED` as pass.

---

# 15. Family-Test Device Gate

Primary family device must have:

```text
0 blocker failures
0 high-severity failures
all 16 pairs verified
portrait pass
landscape pass
airplane-mode pass
mic-denied pass
reset pass
```

---

# 16. Execution Record

```text
Build:
Commit:
Tester:
Date:
Primary device:
Android:
Overall result:
Open issues:
Family-test ready: YES / NO
```

---

# 17. Governing Rule

> **Emulators prove logic and compatibility; the real tablet proves that Miozira actually works where the child will use it.**
