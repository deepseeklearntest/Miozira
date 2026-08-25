# Miozira — Platform Support Specification

**Document:** `platform-support.md`  
**Version:** 0.1  
**Status:** Draft for Prototype 0.1  
**Product:** Miozira  
**Primary release platform:** Android tablet  
**Future platform:** iPadOS  
**Architecture:** KMP + Compose Multiplatform, Android-first  
**Last updated:** 2026-08-25

---

## 1. Purpose

This document defines the hardware and operating-system support envelope for Miozira Prototype 0.1.

It specifies:

- Android target and minimum SDK strategy;
- supported device class;
- tablet screen requirements;
- portrait/landscape behavior;
- resizability;
- memory/storage expectations;
- speaker/microphone assumptions;
- touch requirements;
- accessibility/platform assumptions;
- performance tiers;
- unsupported scenarios;
- future iPadOS boundary.

The goal is not to support every Android device.

The goal is:

> **support a realistic family tablet envelope well enough to validate Miozira's learning interaction.**

---

# 2. Platform Priorities

Priority order:

```text
1. Android tablet family-test reliability
2. Common Android tablet compatibility
3. Responsive large-screen behavior
4. Future iPadOS portability
5. Other device categories
```

Prototype 0.1 must not delay the Android family test in order to support unrelated platforms.

---

# 3. Primary Platform

Prototype 0.1 primary target:

> **Android tablets**

The interaction and layouts should be designed tablet-first rather than phone-first.

---

# 4. Future Platform

Future intended platform:

> **iPadOS**

The architecture should preserve portability through:

- shared domain logic;
- shared persistence where practical;
- shared Compose UI where beneficial;
- platform-specific audio/microphone adapters.

A production iPadOS release is not required for the first family test.

---

# 5. Unsupported Platforms — Prototype 0.1

Not release targets:

- Android phones;
- Android TV;
- Android Automotive;
- Wear OS;
- Android XR;
- ChromeOS as a specifically validated target;
- Windows;
- macOS desktop;
- Linux desktop;
- web browsers.

Some may technically run later, but they are not part of Prototype 0.1 acceptance.

---

# 6. Android Target SDK

For Google Play submissions beginning **2026-08-31**, new Android apps and app updates must target:

> **Android 16 / API level 36 or higher**

Therefore Prototype 0.1 should compile and target against:

```text
compileSdk = 36 or current required stable level
targetSdk  = 36 or current required stable level
```

at release time.

Do not freeze this document's API number forever; the build should track current Play requirements.

---

# 7. Minimum Android Version

The minimum Android version should be selected for:

- likely family tablet availability;
- Compose/KMP library support;
- audio/microphone reliability;
- testing burden.

Recommended initial Prototype 0.1 direction:

> **minSdk 26 (Android 8.0) or newer**

However, the final `minSdk` should be confirmed during the technical spike rather than locked solely for theoretical reach.

---

# 8. Why Not Extremely Old Android

Supporting very old Android versions can increase:

- permission edge cases;
- audio API differences;
- lifecycle quirks;
- testing burden;
- dependency constraints.

Miozira's family-test goal benefits more from predictable behavior than maximum historical device reach.

---

# 9. Recommended Minimum-SDK Decision Rule

Select the lowest Android version that:

1. current stable chosen libraries support well;
2. can be tested realistically;
3. still represents tablets likely to be in family use;
4. does not materially complicate audio/microphone handling.

If no older physical device is available, do not claim broad old-version support based only on emulator success.

---

# 10. Tablet Definition

Miozira's primary UI is intended for large-screen devices.

For Android large-screen behavior, an important platform threshold is:

```text
smallest width >= 600 dp
```

Prototype 0.1 should treat this as the primary tablet design envelope.

---

# 11. Small-Screen Devices

Prototype 0.1 does not need to provide a polished phone experience.

If installed on a smaller supported Android device, it should preferably:

- not crash;
- retain safe state;
- remain minimally operable.

But phone UX is not a family-test acceptance criterion.

---

# 12. Screen Size Envelope

Primary validation target:

```text
approximately 8–13 inch Android tablets
```

with common tablet resolutions and density buckets.

Do not hardcode layout based on physical inches.

Use:

- available window size;
- dp dimensions;
- adaptive layout logic.

---

# 13. Reference Device Classes

Test at least three conceptual classes:

## Class A — Compact tablet

Approximately:

```text
8–9 inch
600dp+ shortest width
```

## Class B — Typical tablet

Approximately:

```text
10–11 inch
```

## Class C — Large tablet

Approximately:

```text
12–13 inch
```

The family-test device will likely fall into Class B/C.

---

# 14. Candidate Family Hardware

Previously considered modern tablets such as:

- Redmi Pad 2 Pro;
- Oppo Pad 5;

have substantially more CPU/RAM capability than Miozira requires.

Miozira should not architect itself around flagship-class performance.

---

# 15. Lower Common Denominator

The app should remain comfortable on a modest tablet with approximately:

```text
4 GB RAM
mid-range ARM CPU
standard integrated GPU
internal speaker
built-in microphone
```

This is a product target, not a strict certification minimum.

---

# 16. Memory

Prototype 0.1 should not require high memory.

Target:

- small local database;
- a few decoded/prepared audio assets;
- one main concept image;
- minimal screen hierarchy.

Avoid loading all high-resolution images decoded at full size simultaneously.

---

# 17. Storage

Prototype 0.1 should require modest device storage.

Expected runtime user-generated storage:

```text
well below 50 MB
```

under normal use, excluding application bundle overhead.

See:

> `local-storage.md`

---

# 18. CPU

No continuous heavy CPU work is expected.

Core CPU tasks:

- Compose rendering;
- local SQLite;
- short audio decode/playback;
- brief PCM energy analysis;
- simple adaptive scheduling.

No neural inference is required.

---

# 19. GPU

The child UI should not require high-end GPU capability.

Avoid:

- particle systems;
- complex shaders;
- continuous 3D;
- high-cost blur layers;
- large composited animation stacks.

---

# 20. Battery

Miozira should be low-impact because it does not use:

- continuous microphone;
- continuous GPS;
- background sync;
- background network polling;
- persistent animation.

The microphone opens only for brief child response or parent recording windows.

---

# 21. Orientation Support

Prototype 0.1 must support:

> **portrait and landscape**

No orientation should be considered the "broken fallback."

---

# 22. Android 16 Large-Screen Behavior

For apps targeting Android 16 / API 36, Android's large-screen behavior increasingly expects applications to support:

- orientation changes;
- varied aspect ratios;
- resizability.

Miozira should embrace this rather than depend on fixed-orientation restrictions.

---

# 23. Orientation Lock

Do not lock Prototype 0.1 to:

```text
portrait only
```

or:

```text
landscape only
```

unless a severe platform defect is discovered during testing.

---

# 24. Rotation State Preservation

Rotation must not:

- restart canonical audio automatically;
- create a new scheduled interaction;
- duplicate exposure;
- reopen a microphone window;
- lose parent recording state unexpectedly.

See:

> `interaction-states.md`

---

# 25. Resizability

Miozira should be implemented as a resizable/adaptive application.

It should not assume:

```text
window size = physical screen size
```

This supports:

- large-screen Android behavior;
- possible multi-window;
- future foldable/resizable environments.

---

# 26. Multi-Window

Prototype 0.1 does not need optimized multi-window UX.

However, if the app becomes partially resized:

- content should reflow safely;
- audio/microphone should remain lifecycle-safe;
- database state should remain valid.

If a window becomes too small for child UX, Miozira may present a simple parent-oriented size guidance state rather than broken controls.

---

# 27. Foldables

Foldables are not a Prototype 0.1 validation target.

If installed:

- standard adaptive layout behavior should prevent crashes;
- hinge-specific optimization is not required.

---

# 28. Insets and System Bars

Layouts must respect:

- status bar;
- navigation bar;
- gesture navigation region;
- display cutouts where applicable.

Do not place the main child concept underneath unusable system interaction regions.

---

# 29. Edge-to-Edge

If using modern Android edge-to-edge rendering:

- visual background may extend beneath system bars;
- tappable learning content should remain within safe insets.

---

# 30. Touchscreen Requirement

A touchscreen is assumed.

Core child interaction requires:

> single tap

The prototype does not require:

- stylus;
- mouse;
- keyboard.

Parent mode should remain accessible to keyboard/accessibility navigation where practical.

---

# 31. Multi-Touch

Multi-touch hardware may exist but is not required by Miozira.

No core interaction requires:

- pinch;
- two-finger gesture;
- simultaneous touches.

---

# 32. Touch Sampling

The app should not depend on unusually high touch sampling rates.

Standard consumer tablet touchscreens are sufficient.

---

# 33. Speaker Requirement

A usable audio output route is required for the intended child learning experience.

Primary expected route:

> built-in tablet speaker

Supported routes may include:

- wired headphones;
- Bluetooth audio.

---

# 34. Speaker Quality Assumption

Do not assume premium speakers.

Canonical clips must remain intelligible on ordinary tablet speakers.

This reinforces:

- clean source recording;
- no background music;
- speech-focused mastering.

---

# 35. Stereo Requirement

No stereo speaker requirement.

Target audio can be mono.

---

# 36. Microphone Requirement

The child-speaking feature benefits from:

> built-in microphone

but the application must remain functional if microphone is:

- unavailable;
- denied;
- disabled by parent;
- occupied.

Therefore microphone hardware is:

> optional for core learning progression.

---

# 37. Microphone Distance Assumption

The child is assumed to be:

> near the tablet during use

The detector does not need room-scale far-field speech capability.

---

# 38. Camera

No camera required.

Prototype 0.1 must not request camera permission.

---

# 39. Location

No location hardware or permission required.

---

# 40. Bluetooth

Bluetooth permission should not be requested merely to play through an already system-managed audio route unless platform behavior actually requires it.

Miozira does not manage Bluetooth devices.

---

# 41. Network Hardware

No Wi-Fi or cellular connection is required for the child learning flow.

The application should work in:

> airplane mode

after installation.

---

# 42. INTERNET Permission

Prototype 0.1 should aim to omit Android:

```text
android.permission.INTERNET
```

unless a selected dependency creates a justified requirement.

---

# 43. Audio Permission

Audio playback itself should require no special dangerous permission.

---

# 44. Microphone Permission

Child attempt detection / family recording require Android microphone permission.

Permission request must remain parent-controlled.

See:

> `permissions.md`

---

# 45. External Storage Permission

Not required.

All user-generated media stays in app-private storage.

---

# 46. Notifications

Prototype 0.1 does not need notification permission.

No push reminders are part of the vertical slice.

---

# 47. Background Services

No persistent foreground/background service should be required.

Audio/mic work occurs only while app interaction is active.

---

# 48. Internet-Free First Launch

After installation, first launch should not stall because of:

- remote config;
- analytics initialization;
- content download;
- auth;
- cloud connection.

Bundled content is immediately available.

---

# 49. Android Runtime Performance Targets

On supported tablets:

```text
tap acknowledgement        normally <=100 ms
canonical audio start      normally <=250 ms
local UI transition        normally <=300 ms where appropriate
cold launch                approximately <=4 s
warm launch                approximately <=2 s
```

These are product targets from `non-functional-requirements.md`.

---

# 50. Frame Performance

Child transitions should remain smooth on modest tablets.

Target:

> avoid visible jank during the core loop.

Do not require high-refresh-rate displays.

60 Hz is sufficient.

---

# 51. Refresh Rate

The app must work correctly at:

- 60 Hz;
- higher refresh rates.

Animation timing should be duration/state-based, not frame-count dependent.

---

# 52. Display Density

Use dp/sp sizing.

Do not use raw pixel constants for layout.

Concept media must be delivered at sufficient resolution for common tablet densities without unnecessary oversized decoding.

---

# 53. Aspect Ratios

Support common portrait and landscape tablet aspect ratios.

Avoid designs that require exactly:

```text
16:10
```

or:

```text
4:3
```

---

# 54. Very Wide Screens

On large/wide devices:

- keep child concept centered/dominant;
- use whitespace;
- limit parent text width.

Do not stretch controls edge to edge just because space exists.

---

# 55. Very Tall Portrait Screens

Maintain:

- concept dominance;
- accessible touch size;
- listening cue space;
- safe parent gate placement.

Avoid vertically scattering unrelated controls.

---

# 56. Accessibility Platform Support

Parent-area testing should include Android accessibility mechanisms such as:

- TalkBack;
- large font;
- display scaling;
- reduced animation where available.

See:

> `accessibility.md`

---

# 57. Font Scale

Parent UI should aim to remain functional at approximately:

> 200% font scaling

where platform behavior permits.

---

# 58. System Language

The app must not assume system language equals learning language.

Examples:

```text
Android UI locale = English
active learning language = Tamil
```

is valid.

---

# 59. Tamil Rendering

Supported device/font stack must render Unicode Tamil correctly.

If a bundled font is ever considered, licensing/bundle size/accessibility must be reviewed.

System-supported fonts are preferred unless typography testing proves inadequate.

---

# 60. Device Time

Adaptive scheduling depends on device time.

Miozira assumes:

- reasonably correct local system clock;
- timezone may change.

Persist timestamps in UTC-like epoch form and compute due logic consistently.

---

# 61. Timezone Change

Timezone changes must not corrupt learning evidence.

A timezone move may affect local-date presentation, but historical timestamps remain stable.

---

# 62. Clock Manipulation

Prototype 0.1 does not need anti-cheat logic for changed device clock.

There is no competitive progress system.

If clock moves dramatically, scheduling may be recalculated conservatively.

---

# 63. App Updates

Application update must preserve:

- local database;
- parent observations;
- family recordings;
- settings

unless an explicit migration says otherwise.

---

# 64. OS Updates

Miozira should not depend on undocumented OS behavior.

Test on current supported Android versions where possible.

---

# 65. Android Version Test Envelope

Recommended Prototype 0.1 validation:

```text
minimum supported Android version
one intermediate Android version
current Android 16 / API 36 environment
```

Physical-device coverage may be limited, so combine:

- physical device;
- Android emulator.

Audio/microphone quality must be validated physically.

---

# 66. Emulator Limits

Emulator tests are useful for:

- layout;
- orientation;
- database;
- lifecycle;
- permissions;
- state machine.

They are insufficient for validating:

- real microphone sensitivity;
- speaker leakage;
- acoustic latency;
- family voice quality.

---

# 67. Physical Device Requirement

At least one real Android tablet is mandatory for Prototype 0.1 acceptance.

Preferably test:

- target family device;
- one lower/mid-range additional device if accessible.

---

# 68. Device Performance Tiers

## Tier 1 — Target family device

Must pass all acceptance criteria.

## Tier 2 — Common modest tablet

Should provide acceptable core experience.

## Tier 3 — Unvalidated older/low-end device

Best effort only until specifically tested.

---

# 69. Hardware Features Not Required

Prototype 0.1 does not require:

- GPS;
- camera;
- NFC;
- biometrics;
- accelerometer;
- gyroscope;
- stylus;
- cellular modem;
- fingerprint sensor;
- face recognition.

---

# 70. Motion Sensors

If movement interactions are included:

they should be physical-child prompts such as:

> touch your nose

not sensor-driven device motion.

No accelerometer permission/API dependency is needed.

---

# 71. Vibration/Haptics

Optional.

The app must not depend on a vibration motor for core meaning.

---

# 72. Hardware Keyboard

Not required in child mode.

Parent mode should not break if hardware keyboard is present.

---

# 73. External Mouse/Trackpad

Not a target interaction, but conventional parent controls should remain clickable if supported by platform UI.

---

# 74. Audio Focus Capability

Platform must support ordinary media audio focus/session handling.

Miozira should follow standard Android media behavior rather than implement custom audio-device management.

---

# 75. Concurrent Audio

If another app is playing media:

Miozira should request appropriate focus and recover gracefully.

Exact focus policy belongs in `audio.md`.

---

# 76. Microphone Conflict

If another app/service owns the microphone:

- child detector returns unavailable;
- learning continues;
- parent recording may show a parent-facing error.

---

# 77. Do Not Gate Installation on Microphone

Because child learning works without the microphone, microphone hardware should not be declared as an installation-blocking required feature if Android manifest configuration allows it to remain optional.

---

# 78. Do Not Gate Installation on Camera

Camera not used.

---

# 79. Screen-On Behavior

Prototype 0.1 should normally let standard OS screen timeout behavior apply.

Do not keep screen awake indefinitely unless brief active-session testing shows normal timeout is disruptive.

If keeping awake during an active child session is later chosen:

- limit it to active use;
- release on background/session end.

---

# 80. Session Interruptions

Platform events may include:

- screen lock;
- home gesture;
- app switch;
- phone/VoIP interruption;
- audio route change.

All should preserve data integrity.

---

# 81. Screen Lock

If screen locks during session:

- stop microphone;
- stop/pause audio;
- persist committed evidence;
- resume safely or end interrupted session later.

---

# 82. App Process Recreation

Miozira must tolerate process recreation without:

- duplicate exposure;
- duplicate attempt;
- corrupt session state.

See:

> `interaction-states.md`

---

# 83. iPadOS Portability Boundary

The future iPadOS implementation should reuse where practical:

```text
domain models
adaptive engine
session engine
transfer logic
repository abstractions
Room/KMP data layer
content manifest
Compose UI
```

Platform adapters differ for:

```text
audio session
microphone capture
permissions
file storage
lifecycle
backup
accessibility details
```

---

# 84. iPadOS Is Not Android Emulation

Future iPadOS implementation must respect:

- Apple audio-session behavior;
- iOS privacy permission behavior;
- safe areas;
- iPad multitasking/resizability;
- VoiceOver/accessibility semantics;
- file backup semantics.

Do not force Android-specific assumptions into shared code.

---

# 85. KMP/CMP Platform Status

Current Kotlin platform documentation classifies:

- Kotlin Multiplatform Android: stable;
- Kotlin Multiplatform iOS: stable;
- Compose Multiplatform Android: stable;
- Compose Multiplatform iOS: stable.

This supports the selected portability strategy.

---

# 86. iOS Technical Spike

The Prototype 0.1 Android milestone does not require a full iOS build.

Recommended during/after stack spike:

```text
minimal iOS app compiles
shared domain loads
one shared screen renders
```

This validates architecture without delaying family testing.

---

# 87. Minimum iPadOS Version

Not decided in Prototype 0.1.

Choose later based on:

- current Compose Multiplatform support;
- likely target devices;
- Apple App Store requirements;
- audio/microphone APIs.

---

# 88. App Store Distribution

Prototype 0.1 family test may use:

- local developer build;
- internal testing;
- Play testing track

depending on deployment workflow.

Public store release is not required to validate the family slice.

---

# 89. Google Play Families Implication

If distributed publicly to children/families:

- child-directed policy requirements apply;
- microphone use must be documented appropriately;
- target API requirements apply;
- data-safety declarations must match actual behavior.

See privacy/safety documents.

---

# 90. Device Matrix — Prototype 0.1

Recommended initial matrix:

| Test target | Required | Purpose |
|---|---:|---|
| Primary family Android tablet | Yes | Real child/family test |
| Android 16 / API 36 emulator | Yes | Current target SDK/lifecycle |
| Minimum-SDK emulator | Yes | Compatibility |
| Additional modest Android tablet | Preferred | Performance/acoustic sanity |
| iOS/iPad simulator compile/render | Optional before family test | Portability sanity |

---

# 91. Orientation Matrix

For the primary device test:

```text
cold launch portrait
cold launch landscape
rotate READY
rotate PLAYING_WORD
rotate LISTENING
rotate RESPONDING
rotate parent home
rotate family recording screen
```

No duplicate learning evidence.

---

# 92. Audio Hardware Matrix

Test:

```text
built-in speaker + built-in mic
speaker at low/moderate volume
optional Bluetooth route
```

Built-in speaker/mic is the authoritative family-test setup.

---

# 93. Permission Matrix

Test:

```text
microphone not determined
granted
denied
don't ask again / settings required
revoked after grant
```

The child flow must survive every state.

---

# 94. Offline Matrix

Test:

```text
Wi-Fi on
Wi-Fi off
airplane mode
network unavailable at launch
```

Core behavior should be identical.

---

# 95. Low-Resource Behavior

If the system is under memory pressure:

- allow asset/player recreation;
- preserve persistent learning evidence;
- do not crash because a cache was cleared.

---

# 96. Thermal Behavior

Miozira's workload should not generate meaningful thermal pressure during a 5–10 minute session.

If a prototype does:

> investigate it as a technical defect.

---

# 97. Unsupported / Degraded Scenarios

Prototype 0.1 may provide degraded behavior when:

- no microphone;
- Bluetooth audio high latency;
- multi-window extremely narrow;
- external keyboard only;
- old untested Android device.

Degraded must mean:

> core learning remains safe where feasible,

not:

> silently corrupt evidence.

---

# 98. Hard Failure Conditions

The app may be unable to run a valid child session if:

- required bundled canonical content is corrupted;
- database cannot be opened/recovered;
- device cannot decode required audio format;
- essential graphics cannot render.

These should produce parent/developer recovery flow, not child technical errors.

---

# 99. Platform Support Acceptance Criteria

Before seven-day family testing:

- [ ] app builds with current required target SDK;
- [ ] primary tablet installs and launches;
- [ ] portrait works;
- [ ] landscape works;
- [ ] rotations preserve state;
- [ ] all 16 local word clips play;
- [ ] built-in mic attempt detector works;
- [ ] mic denial path works;
- [ ] airplane-mode session works;
- [ ] no broad storage permission exists;
- [ ] app restart preserves learning data;
- [ ] screen lock/background does not corrupt state;
- [ ] current Android emulator passes lifecycle tests;
- [ ] minimum-SDK emulator launches successfully.

---

# 100. Platform Invariants

### PLAT-INV-001
Android tablet is the Prototype 0.1 primary platform.

### PLAT-INV-002
The first family test does not depend on iOS completion.

### PLAT-INV-003
Portrait and landscape are both supported.

### PLAT-INV-004
The app is resizable/adaptive rather than fixed to one exact resolution.

### PLAT-INV-005
Core learning requires no network.

### PLAT-INV-006
Microphone is optional for learning progression.

### PLAT-INV-007
Camera, location, and broad external storage are not required.

### PLAT-INV-008
Standard built-in tablet speaker/microphone are sufficient.

### PLAT-INV-009
No high-refresh-rate display is required.

### PLAT-INV-010
No flagship-class CPU/GPU is required.

### PLAT-INV-011
Orientation/lifecycle events never become learning evidence.

### PLAT-INV-012
Android target SDK follows current Play requirements.

### PLAT-INV-013
Future iPadOS platform-specific behavior stays behind adapters.

---

# 101. Open Platform Decisions

Before implementation/release freeze, confirm:

1. final Android `minSdk`;
2. actual primary family tablet model;
3. second physical test device if available;
4. exact Android build/target SDK at distribution time;
5. multi-window minimum-size handling;
6. whether active sessions temporarily keep screen awake;
7. whether phone installation is allowed but unsupported, or explicitly blocked;
8. future minimum iPadOS version.

---

# 102. Relationship to `architecture.md`

`architecture.md` separates platform-neutral logic from platform adapters.

This document defines which platform behaviors those adapters must support.

---

# 103. Relationship to `ADR-0001-technology-stack.md`

The ADR chooses:

```text
KMP + Compose Multiplatform
```

with Android-first delivery.

This document defines the actual platform support scope within that choice.

---

# 104. Relationship to `audio.md`

`audio.md` defines playback semantics.

This document defines the expected device audio capabilities and test routes.

---

# 105. Relationship to `microphone.md`

`microphone.md` defines transient child attempt detection.

This document defines microphone hardware/permission support expectations.

---

# 106. Relationship to `device-test-matrix.md`

`device-test-matrix.md` should turn this support envelope into an executable matrix containing:

- real devices;
- emulator API levels;
- orientations;
- permissions;
- audio routes;
- pass/fail status.

---

# 107. Decision Summary

Miozira Prototype 0.1 supports:

```text
Android tablets first
portrait + landscape
adaptive/resizable layouts
local speaker/audio
optional built-in microphone
offline operation
modest tablet hardware
```

Current release direction:

```text
target Android 16 / API 36 or current Play-required level
minSdk provisionally Android 8 / API 26 or newer,
to be confirmed by technical spike
```

The product does not require:

- camera;
- GPS;
- external storage;
- cellular network;
- flagship GPU;
- continuous background services.

Future iPadOS remains architecturally supported through KMP/CMP and platform adapters, but Android family-test reliability takes priority.

The governing rule is:

> **Support the tablets families are realistically likely to use, not every theoretical device the framework can compile for.**
