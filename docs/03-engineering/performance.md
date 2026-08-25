# Miozira — Performance Specification

**Document:** `performance.md`  
**Version:** 0.1  
**Status:** Draft for Prototype 0.1  
**Product:** Miozira  
**Primary platform:** Android tablet  
**Architecture:** KMP + Compose Multiplatform, local-first  
**Last updated:** 2026-08-25

---

## 1. Purpose

This document defines measurable performance expectations for Miozira Prototype 0.1.

It covers:

- startup;
- tap acknowledgement;
- tap-to-audio latency;
- child interaction transitions;
- local database work;
- adaptive-engine computation;
- image loading;
- audio preparation;
- microphone CPU cost;
- memory use;
- battery behavior;
- low-end tablet performance;
- profiling;
- regression testing.

The goal is not benchmark prestige.

The goal is:

> **make Miozira feel immediate, calm, and dependable on ordinary family tablets.**

---

# 2. Performance Philosophy

Performance matters in Miozira because delay changes the learning interaction.

If:

```text
tap
→ long unexplained delay
→ word
```

the child may not clearly associate the tap with the audio response.

Therefore performance is part of:

- usability;
- accessibility;
- learning clarity.

---

# 3. Performance Priorities

Priority order:

```text
1. immediate tap acknowledgement
2. fast local word playback
3. stable child-state transitions
4. no UI-thread blocking
5. no audio/mic glitches
6. reliable modest-tablet operation
7. low memory/battery cost
8. startup polish
```

---

# 4. Prototype 0.1 Performance Targets

Starting targets from the NFR baseline:

```text
Tap acknowledgement:       <=100 ms normally
Canonical audio start:     <=250 ms normally after tap
Local state transition:    <=300 ms normally, excluding intentional animation duration
Warm launch:               ~<=2 s
Cold launch:               ~<=4 s
```

These are product targets, not guarantees for every pathological device condition.

---

# 5. Definitions

## Tap acknowledgement latency

Time from:

```text
touch accepted
```

to:

```text
visible state acknowledgement begins
```

Examples:

- subtle scale;
- highlight;
- halo.

---

## Tap-to-audio latency

Time from:

```text
ConceptTapped event
```

to:

```text
audible canonical word onset
```

---

## Transition latency

Time from:

```text
domain decision / prior interaction completion
```

to:

```text
next visual state ready to render
```

This excludes intentionally designed animation duration where the animation itself is the experience.

---

# 6. Child Interaction Budget

Target flow:

```text
tap
  ↓ <=100 ms
visible acknowledgement

tap
  ↓ <=250 ms normally
audible word

word completes
  ↓ immediate state update
invite/listen/respond

response completes
  ↓ <=300 ms local processing
next item prepared
```

The child should not encounter unexplained spinner-like pauses.

---

# 7. Performance Must Not Change Semantics

Do not improve perceived speed by:

- committing exposure before audio is heard;
- starting microphone before target audio finishes;
- skipping database writes;
- dropping state validation;
- pre-marking an interaction complete.

Correctness remains higher priority than microseconds.

---

# 8. Cold Launch

Cold launch means:

- app process not resident;
- local database opened;
- settings loaded;
- canonical content validated enough to proceed;
- initial screen displayed.

Target:

> approximately **4 seconds or less** on supported family hardware.

Prefer substantially faster.

---

# 9. Warm Launch

Warm launch means process/resources are already partially resident.

Target:

> approximately **2 seconds or less**.

---

# 10. Launch Critical Path

The launch critical path should include only what is necessary to show a usable first state.

Do not block first render on:

- scanning all historical sessions;
- recomputing every diagnostic;
- loading every image;
- decoding every audio file;
- checking network;
- analytics initialization.

---

# 11. Content Validation at Startup

Prototype content is tiny, so validation can be simple.

However, full expensive validation need not run synchronously every launch if build-time validation already guarantees asset integrity.

Preferred:

```text
fast runtime sanity check
+
strong build/test validation
```

---

# 12. Database Startup

Database open/migration must happen off the rendering path where possible.

If a migration is required:

- show parent-safe startup state;
- do not start child session until DB is valid.

---

# 13. First Child Item Preparation

Before displaying `READY`:

- current visual should be available;
- current canonical audio reference should be resolved;
- interaction ID should exist;
- tap action should not need heavy disk work.

---

# 14. Audio Preparation

Because only 16 canonical clips exist, current-word playback should use lightweight preparation.

Recommended:

- resolve current clip before item becomes tappable;
- optionally prepare likely next item.

Avoid:

```text
tap
→ locate manifest
→ query multiple tables
→ decode large asset
→ then play
```

on every interaction.

---

# 15. Tap Acknowledgement

Tap acknowledgement is purely visual and should occur without waiting for:

- database;
- audio completion;
- microphone;
- adaptive engine.

Target:

> normally **<=100 ms**.

---

# 16. Tap-to-Audio

Canonical audio should normally become audible within:

> **<=250 ms** after the accepted child tap.

This should be measured on real tablet hardware.

---

# 17. Audio Latency Variability

Latency may differ due to:

- cold decoder/player initialization;
- Bluetooth route;
- OS audio focus;
- device load.

Built-in speaker is the primary acceptance route.

---

# 18. First Playback Penalty

The first playback after cold launch may be slower.

If necessary, initialize/prewarm minimal audio infrastructure before first item reaches `READY`.

Do not play inaudible dummy audio merely as a hack unless required and reviewed.

---

# 19. No Overlapping Playback

Performance optimization must never allow:

- two canonical clips simultaneously;
- prompt overlapping word;
- replay stacking.

Correct serialization is a hard requirement.

---

# 20. Transition Performance

When an interaction completes, adaptive selection should be fast enough that intentional visual transition dominates perceived time.

Target:

> adaptive selection + local retrieval normally **well under 100 ms** for 16 pairs.

If it takes hundreds of milliseconds, implementation should be investigated.

---

# 21. Adaptive Engine Complexity

Prototype 0.1 has only:

```text
16 concept-language pairs
```

Therefore selection logic should be effectively trivial in computational cost.

No background precomputation is necessary.

---

# 22. Database Query Budget

Common queries should typically complete in:

> single-digit to low tens of milliseconds on target hardware

for such a small dataset.

Exact benchmark values are less important than:

- no UI-thread blocking;
- no repeated unnecessary queries.

---

# 23. Avoid N+1 Query Patterns

Do not query separately for every pair when one query can return all 16 pair states.

Prefer:

```text
getAllPairStates()
```

over:

```text
for pair in pairs:
    getPairState(pair)
```

if the latter creates avoidable database calls.

---

# 24. Write Behavior

Learning writes are small.

Examples:

- commit exposure;
- increment replay;
- record attempt;
- finalize interaction.

These should happen asynchronously without freezing UI.

---

# 25. Write Durability

Do not defer critical evidence indefinitely solely for speed.

Persist promptly enough that process death does not lose already-established evidence.

---

# 26. Transaction Size

Transactions should be small.

Avoid holding a transaction open across:

- audio playback;
- user interaction;
- microphone window;
- animation.

Transactions should cover only related database writes.

---

# 27. UI Thread Rule

The rendering/main thread must not perform:

- SQLite disk reads/writes;
- family audio file writes;
- long PCM analysis loops;
- asset transcoding;
- migration work.

---

# 28. Compose Recomposition

Recomposition should remain cheap.

Avoid reading large mutable models directly into UI.

Expose concise state such as:

```text
ChildSessionUiState
ParentUiState
```

---

# 29. Recomposition Is Not a Bug by Itself

Declarative UI may recompose frequently.

Optimize only when measurement shows:

- jank;
- unnecessary allocations;
- expensive repeated work.

Do not prematurely micro-optimize simple Compose code.

---

# 30. Expensive Work in Composables

Do not perform in render/composable code:

- image decoding;
- DB queries;
- audio-player creation;
- microphone initialization;
- adaptive recalculation.

Use state/effects/controllers.

---

# 31. Image Performance

Only one primary child concept image is normally visible.

Therefore memory usage can remain low if images are decoded near display size.

---

# 32. Image Resolution Policy

Source assets may be high resolution.

Runtime should avoid decoding an enormous source at full resolution when display size is much smaller.

Use image-loading/downsampling strategy appropriate to Compose/platform tooling.

---

# 33. Image Cache

A small memory cache may contain:

- current concept;
- previous concept;
- next likely concept.

With only eight visuals, loading all appropriately sized assets may also be acceptable if memory remains modest.

Measure rather than assume.

---

# 34. Image Memory Example

A decoded RGBA image uses roughly:

```text
width × height × 4 bytes
```

A 2000×2000 image can use about:

```text
16 MB
```

decoded.

Therefore shipping a small file does not guarantee small runtime memory.

---

# 35. Image Asset Target

Final concept artwork should be sized for tablet display quality without extreme excess resolution.

Exact pixel dimensions should be set once visual style is known.

---

# 36. Audio Memory

Do not decode all audio into large PCM buffers permanently.

Short compressed canonical clips can remain packaged and be prepared/decoded as needed.

---

# 37. Audio Cache

Keep:

- current;
- maybe next;
- reusable prompts

prepared where beneficial.

No complex cache eviction framework is needed.

---

# 38. Family Recording Memory

Family recordings should stream/play from app-private files.

Do not load long files fully into memory by default.

Prototype clips are short, but clean architecture still matters.

---

# 39. Microphone CPU Budget

Child speech-attempt detection should be lightweight.

Expected processing:

```text
mono PCM
short 2–3 sec window
RMS/noise-floor computation
simple frame heuristics
```

No neural network is required.

---

# 40. Microphone Threading

PCM reads and analysis run off the UI thread.

The UI receives only terminal/semantic events.

---

# 41. Microphone CPU Acceptance

On target tablet, one listening window should not cause:

- visible UI jank;
- thermal spike;
- noticeable battery drain;
- delayed animation.

---

# 42. Microphone Memory

Use reusable small frame buffers.

Do not accumulate the entire session's microphone stream.

---

# 43. Garbage Collection

Avoid generating excessive short-lived objects inside high-frequency PCM processing.

Use primitive arrays/buffers where appropriate.

---

# 44. Battery

A 5–10 minute session should have modest battery impact.

Miozira uses no:

- network polling;
- GPS;
- continuous microphone;
- continuous video;
- 3D rendering.

If battery use is significant, treat it as a defect.

---

# 45. Background Battery

When app is backgrounded, it should perform essentially no learning workload.

No:

- active microphone;
- playback;
- polling;
- scheduler loop.

---

# 46. Screen Power

Display power will likely dominate the short session.

The app should not unnecessarily:

- force maximum brightness;
- keep screen awake indefinitely.

---

# 47. Memory Target Philosophy

Do not define a huge allowed heap just because tablets have RAM.

Target:

> compact enough to run comfortably alongside normal system processes on a 4 GB tablet.

---

# 48. Memory Budget — Prototype Direction

A practical target for active app private memory:

> preferably well below **200 MB** under normal child use.

This is a generous ceiling, not a consumption goal.

The actual app should likely be far smaller.

---

# 49. Memory Warning Threshold

If normal child mode regularly exceeds:

```text
~200 MB
```

investigate:

- image decoding;
- leaked audio players;
- retained screens;
- recording buffers;
- cache.

---

# 50. Parent Mode Memory

Parent screens should not load all historical content indefinitely.

Recent data can be:

- limited;
- paged if necessary later.

Prototype history volume will be tiny.

---

# 51. Memory Leaks

Specifically test for leaks around:

- `AudioRecord`;
- playback service;
- family recording;
- screen rotation;
- session controller;
- parent gate;
- repeated navigation.

---

# 52. Session Stress Test

Run:

```text
50–100 interaction transitions
```

in an automated/manual stress build.

Observe:

- memory trend;
- player count;
- microphone resource release;
- DB stability.

Memory should stabilize rather than rise continuously.

---

# 53. Rotation Stress Test

Repeatedly rotate during:

- READY;
- PLAYING_WORD;
- LISTENING;
- parent mode.

Confirm:

- no duplicate resources;
- no increasing memory;
- no state corruption.

---

# 54. App Restart Performance

After many sessions exist, startup should remain similar.

Do not load all historical rows into memory on every launch.

---

# 55. Database Growth Test

Generate synthetic history equivalent to:

```text
6–12 months
```

of use.

Verify:

- startup remains responsive;
- adaptive queries remain fast;
- recent parent view remains responsive.

Dataset is still expected to be small.

---

# 56. Low-End Tablet Target

A modest Android tablet should remain usable with:

```text
4 GB RAM
mid-range CPU
60 Hz display
ordinary internal storage
```

Avoid requiring current flagship hardware.

---

# 57. Slow Storage

Some low-cost tablets have slow flash.

Mitigations:

- small writes;
- no heavy synchronous I/O;
- bundled local assets;
- minimal startup scanning.

---

# 58. Thermal Budget

A normal 10-minute session should not cause meaningful device heating attributable to Miozira.

No workload justifies sustained high CPU.

---

# 59. Network Performance

Not applicable to core runtime.

There is no network dependency.

Therefore no performance budget is needed for:

- API latency;
- sync time;
- upload;
- download.

This is a major reliability advantage.

---

# 60. Startup Offline Equality

Startup time should be essentially the same in:

- Wi-Fi connected;
- airplane mode.

If launch slows offline, investigate hidden dependency behavior.

---

# 61. Release Build Measurement

Performance should be measured in:

> release-like builds

not only debug builds.

Debug instrumentation can distort:

- startup;
- Compose runtime;
- logging;
- DB.

---

# 62. Profiling Tools

Android implementation should use platform tools such as:

- Android Studio Profiler;
- system trace / Perfetto where useful;
- Compose layout/recomposition tooling;
- memory profiler;
- CPU profiler.

Exact tooling evolves, so use current stable Android guidance.

---

# 63. Macrobenchmark / Benchmark Direction

If practical, create benchmark tests for:

```text
cold startup
warm startup
first child screen
```

Prototype 0.1 does not require an elaborate benchmark lab.

---

# 64. Custom Latency Instrumentation

Development builds should measure:

```text
tapAcceptedAt
tapFeedbackStartedAt
audioRequestedAt
audioStartedAt
exposureThresholdAt
interactionCompletedAt
nextItemReadyAt
```

These local timestamps make regressions visible.

---

# 65. No Child Analytics

Technical latency measurement is engineering instrumentation.

It must not become remote child behavioral analytics.

---

# 66. Performance Log Retention

Keep development metrics:

- local;
- temporary;
- minimal.

No permanent fine-grained event warehouse.

---

# 67. Jank Detection

Observe child transitions for:

- dropped frames;
- delayed touch feedback;
- stuttering concept entry;
- frozen listening indicator.

A 60 Hz display gives roughly:

```text
16.7 ms/frame
```

but Miozira need not manually optimize every frame unless jank appears.

---

# 68. Animation Performance

Animations should be simple:

- fade;
- scale;
- ring/pulse.

Avoid:

- heavy blur;
- particles;
- complex shadows;
- multiple simultaneous transforms.

This helps both low stimulation and performance.

---

# 69. Reduced Motion

Reduced-motion mode should also naturally reduce rendering work.

However accessibility, not performance, is the reason for it.

---

# 70. Parent UI Performance

Parent actions should feel immediate.

Target:

```text
tap → visual response <=100 ms
```

Saving a real-world observation should normally complete quickly enough that:

```text
Saved
```

appears without noticeable waiting.

---

# 71. Family Recording Start

Parent tapping Record should begin capture promptly.

Target:

> normally within **300 ms** after permission/resources are ready.

If the microphone is busy/unavailable, return a clear result rather than hanging.

---

# 72. Family Recording Save

A ≤5-second recording should save locally quickly.

Target:

> normally below **500 ms–1 s** after stop on supported devices.

If transcoding is required, keep it minimal.

---

# 73. Reset Performance

Reset deals with a tiny dataset.

It should normally complete quickly.

Do not promise an exact sub-second result if file deletion/storage conditions vary.

UI must remain responsive.

---

# 74. Recovery Performance

Recovery checks at launch should be bounded.

Avoid walking huge directories.

Prototype storage layout is small enough for direct validation.

---

# 75. Performance Degradation Policy

If a device cannot meet the ideal latency target but remains:

- understandable;
- stable;
- safe;

it may still be usable.

But repeated:

```text
tap → >500 ms silence
```

for canonical audio is likely a product issue.

---

# 76. Hard Performance Failures

Treat as release blockers:

- repeated multi-second tap-to-word delay;
- UI freezes during DB writes;
- audio overlap;
- microphone causing UI jank;
- runaway memory;
- crash under rotation stress;
- audio resource exhaustion;
- 10-minute session causing excessive heat.

---

# 77. Performance Acceptance — Startup

Before family test:

- [ ] cold launch measured on target tablet;
- [ ] warm launch measured;
- [ ] no network dependency affects launch;
- [ ] launch does not decode all media unnecessarily.

---

# 78. Performance Acceptance — Child Loop

- [ ] tap feedback normally <=100 ms;
- [ ] canonical audio normally starts <=250 ms;
- [ ] no overlapping audio;
- [ ] adaptive next-item decision has no visible stall;
- [ ] transition remains smooth;
- [ ] replay remains responsive.

---

# 79. Performance Acceptance — Database

- [ ] DB work never blocks UI thread;
- [ ] all 16 pair states load quickly;
- [ ] exposure writes are prompt;
- [ ] synthetic long history does not degrade startup materially;
- [ ] reset does not freeze UI.

---

# 80. Performance Acceptance — Microphone

- [ ] capture start is prompt;
- [ ] 2–3 second detector produces no visible jank;
- [ ] resources release after every window;
- [ ] no memory trend across repeated windows;
- [ ] background stops capture promptly.

---

# 81. Performance Acceptance — Memory

- [ ] child mode memory remains stable;
- [ ] repeated sessions do not leak;
- [ ] image assets are downsampled appropriately;
- [ ] audio players are released/reused;
- [ ] rotation stress does not increase memory continuously.

---

# 82. Performance Acceptance — Battery/Thermal

- [ ] 10-minute session causes no obvious abnormal heating;
- [ ] app performs no meaningful background work after exit/background;
- [ ] microphone is short-lived;
- [ ] no polling service exists.

---

# 83. Performance Test Matrix

Test on:

1. primary family tablet;
2. minimum-SDK emulator;
3. Android 16/API 36 emulator;
4. additional modest tablet if available;
5. low-storage condition;
6. memory pressure;
7. airplane mode;
8. repeated orientation;
9. repeated sessions;
10. repeated mic windows.

---

# 84. Performance Regression Triggers

Investigate if a code/content change causes:

- tap-to-audio median increase >50–100 ms;
- cold startup increase >500 ms;
- memory increase >25% without clear reason;
- new visible jank;
- audio glitches;
- additional background activity.

These are engineering heuristics, not rigid product laws.

---

# 85. Baseline Recording

Before seven-day family testing, record a performance baseline:

```text
device model
Android version
build version
cold launch
warm launch
tap-to-audio samples
active memory
10-minute session thermal observation
```

This helps identify later regressions.

---

# 86. Family-Test Freeze

Do not tune performance during the seven-day test unless:

- latency prevents normal use;
- crashes occur;
- audio/mic defects materially affect the test.

Behavioral evidence is easier to interpret when build behavior is stable.

---

# 87. Development vs Product Metrics

Engineering may measure:

```text
latency
CPU
memory
DB timing
```

Do not convert these into child-facing:

- scores;
- speed goals;
- completion metrics.

---

# 88. Performance Invariants

### PERF-INV-001
Tap acknowledgement does not wait for database/audio completion.

### PERF-INV-002
Canonical playback normally starts within the defined responsive target.

### PERF-INV-003
Database work does not block the UI rendering thread.

### PERF-INV-004
Adaptive scheduling is computationally trivial for 16 pairs.

### PERF-INV-005
Only necessary media is decoded/prepared.

### PERF-INV-006
Microphone processing remains lightweight and short-lived.

### PERF-INV-007
No continuous background workload exists.

### PERF-INV-008
No network latency exists on the child critical path.

### PERF-INV-009
Orientation/recomposition does not duplicate expensive resources.

### PERF-INV-010
Memory stabilizes across repeated sessions.

### PERF-INV-011
Performance optimization cannot weaken evidence correctness.

### PERF-INV-012
The app does not require flagship-class hardware.

---

# 89. Open Performance Decisions

Before implementation freeze, confirm:

1. measured cold/warm startup on actual family tablet;
2. real tap-to-audio baseline;
3. exact image resolutions;
4. exact playback/preload strategy;
5. actual active-memory baseline;
6. microphone CPU/latency measurements;
7. whether benchmark tests are worth maintaining;
8. whether active child session should keep screen awake;
9. whether a second low-end physical device is available.

---

# 90. Relationship to `non-functional-requirements.md`

That document defines high-level quality targets.

This document converts them into engineering measurements and test expectations.

---

# 91. Relationship to `audio.md`

`audio.md` defines semantics such as:

- exposure threshold;
- playback ordering.

This document defines latency and resource budgets around that behavior.

---

# 92. Relationship to `microphone.md`

`microphone.md` defines the detector.

This document requires it to remain:

- lightweight;
- off the UI thread;
- short-lived;
- leak-free.

---

# 93. Relationship to `platform-support.md`

`platform-support.md` defines the modest-device envelope.

This document defines what “acceptable performance” means within that envelope.

---

# 94. Relationship to `testing.md`

`testing.md` should include:

- launch benchmarks;
- latency checks;
- stress tests;
- memory/leak tests;
- repeated microphone tests;
- low-resource scenarios.

---

# 95. Decision Summary

Miozira Prototype 0.1 performance is governed by a few meaningful budgets:

```text
tap feedback         <=100 ms normally
canonical audio      <=250 ms normally after tap
local transition     <=300 ms normally, excluding designed animation
warm launch          ~<=2 s
cold launch          ~<=4 s
```

The engineering strategy is:

1. keep the child critical path local;
2. prepare current audio before interaction where practical;
3. keep database work off the UI thread;
4. keep adaptive computation trivial;
5. decode images near display size;
6. keep microphone processing simple;
7. prevent resource leaks across lifecycle changes;
8. target ordinary 4 GB mid-range tablets;
9. measure on real hardware;
10. optimize only where measurement shows a real problem.

The governing rule is:

> **The child should notice the word and the interaction—not the computer doing work behind it.**
