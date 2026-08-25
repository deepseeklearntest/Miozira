# Miozira — Testing Strategy

**Document:** `testing.md`  
**Version:** 0.1  
**Status:** Draft for Prototype 0.1  
**Product:** Miozira  
**Primary platform:** Android tablet  
**Prototype:** 8 concepts × English + Tamil  
**Last updated:** 2026-08-25

---

## 1. Purpose

This document defines the complete testing strategy for Miozira Prototype 0.1.

It covers:

- unit tests;
- domain/adaptive-engine tests;
- state-machine tests;
- repository/database tests;
- audio-service tests;
- microphone-detector tests;
- file/storage tests;
- lifecycle tests;
- integration tests;
- UI tests;
- accessibility tests;
- privacy tests;
- security tests;
- performance tests;
- real-device tests;
- family-test readiness.

The goal is:

> **prove that Miozira behaves predictably enough that family-test observations reflect the product idea, not avoidable software defects.**

---

# 2. Testing Philosophy

Prototype 0.1 does not need enterprise-scale test infrastructure.

It does need strong tests around:

- learning evidence integrity;
- state transitions;
- audio/microphone lifecycle;
- privacy guarantees;
- offline behavior;
- orientation;
- reset/recovery.

These are the areas where subtle bugs could invalidate family-test conclusions.

---

# 3. Test Priority Order

Priority order:

```text
1. Learning evidence integrity
2. Child interaction state correctness
3. Audio playback correctness
4. Microphone privacy/lifecycle
5. Database/storage integrity
6. Offline reliability
7. Child safety
8. Accessibility
9. Performance
10. Parent-area polish
```

---

# 4. Test Pyramid

Recommended shape:

```text
many unit/domain tests
        ↓
repository/service tests
        ↓
integration/state-machine tests
        ↓
small number of UI/instrumented tests
        ↓
real-device/manual tests
        ↓
family test
```

Do not rely on manual testing for logic that can be deterministic.

---

# 5. What Must Be Automated

Automate wherever practical:

- adaptive scheduling;
- exposure idempotency;
- attempt-state transitions;
- session state machine;
- repository persistence;
- reset behavior;
- stale callback rejection;
- rotation/recreation integrity;
- content validation;
- permission-state decision logic.

---

# 6. What Must Be Manual

Manual/device testing is required for:

- real audio quality;
- real microphone sensitivity;
- speaker leakage;
- child touch behavior;
- visual comfort;
- Tamil pronunciation/content review;
- parent gate usability;
- family recording quality;
- real-device performance.

---

# 7. Test Environments

Use:

```text
JVM/common unit tests
Android local tests where appropriate
Android instrumented tests
Android emulator
physical Android tablet
```

Future iOS tests are outside the family-test critical path.

---

# 8. Test Data Principles

Use deterministic fake content.

Do not use real child voice recordings in automated test fixtures.

Allowed:

- generated amplitude arrays;
- synthetic PCM;
- adult-consented test clips;
- bundled canonical prototype assets.

---

# 9. Fake Clock

All adaptive/date-sensitive tests should use:

```text
FakeClock
```

so tests can advance time deterministically.

Example:

```text
Day 1 → exposure
advance 1 day
assert pair due
```

---

# 10. Fake Randomizer

If adaptive tie-breaking uses randomness:

inject:

```text
FakeRandomizer
```

to make selection deterministic.

---

# 11. Unit Tests — Adaptive Engine

Test:

- NEW pair selection;
- EMERGING due timing;
- FAMILIAR spacing;
- real-world-use shortcut;
- RESTING behavior;
- rest duration progression;
- language-block behavior;
- max new items per session;
- same-session spacing;
- selection priority;
- tie-breaking;
- no-attempt weak evidence;
- replay evidence handling.

---

# 12. Adaptive Engine — NEW Pair

Given:

```text
no evidence
```

Expected:

```text
state = NEW
```

and pair is eligible only according to new-item/session rules.

---

# 13. Adaptive Engine — Emerging Spacing

Given:

```text
valid exposure
```

Expected:

```text
state = EMERGING
nextDue ≈ +1 day
```

according to frozen Prototype 0.1 config.

---

# 14. Adaptive Engine — Familiar Spacing

Given sufficient evidence:

```text
>=3 valid exposures
across >=2 sessions
positive evidence
```

Expected:

```text
FAMILIAR
nextDue ≈ +3 days
```

unless real-world-use rule applies.

---

# 15. Adaptive Engine — Real-World Use

Given:

```text
>=1 valid exposure
+
USED_OUTSIDE_APP
```

Expected:

- positive state advancement according to adaptive spec;
- longer spacing where configured;
- only the exact language pair is affected.

---

# 16. Adaptive Engine — Language Isolation

Given:

```text
pair.apple.ta used outside app
```

Expected:

```text
pair.apple.ta changes
pair.apple.en does not inherit evidence
```

---

# 17. Adaptive Engine — Rest

Given:

```text
3 low-interaction encounters
across >=2 sessions
no positive evidence
```

Expected:

```text
RESTING
restUntil ≈ +4 days
```

and later recurring difficulty may extend to ~7 days.

---

# 18. Adaptive Engine — No Hammering

While pair is:

```text
RESTING
```

it should not be selected as a normal due item.

---

# 19. Adaptive Engine — Session Mix

Validate:

- max 2 NEW pairs;
- due pairs prioritized;
- same pair not repeated immediately unless alternatives exhausted;
- language block stays coherent;
- no endless loop.

---

# 20. Unit Tests — Learning Model

Test derived-state calculations independently from persistence.

Examples:

- one exposure ≠ mastery;
- attempt detected is weak evidence;
- no attempt is not failure;
- parent recognition/use evidence ranks higher;
- state remains rebuildable from evidence.

---

# 21. Unit Tests — Session Engine

Test:

- start session;
- next item generation;
- natural end;
- inactivity end;
- parent stop;
- interrupted session;
- meaningful-activity flag;
- suggestion eligibility.

---

# 22. Session Length Logic

Ensure:

- session target is not quota;
- child can stop early;
- engine does not mark early stop as failure.

---

# 23. State-Machine Tests

Test every valid transition from `interaction-states.md`.

Core child states:

```text
SESSION_STARTING
PRESENTING
READY
PLAYING_WORD
INVITING
LISTENING
RESPONDING
TRANSITIONING
IDLE
SESSION_ENDING
ENDED
```

---

# 24. Invalid Transition Tests

Example:

```text
READY → LISTENING
```

without canonical word playback should be rejected.

---

# 25. State-Machine — Happy Path

Test:

```text
SESSION_STARTING
→ PRESENTING
→ READY
→ PLAYING_WORD
→ INVITING
→ LISTENING
→ RESPONDING
→ TRANSITIONING
→ PRESENTING
```

---

# 26. State-Machine — Mic Disabled Path

Test:

```text
READY
→ PLAYING_WORD
→ INVITING
→ RESPONDING
→ TRANSITIONING
```

without opening detector.

---

# 27. State-Machine — Mic Unavailable Path

Expected:

- session continues;
- attempt = MICROPHONE_UNAVAILABLE where appropriate;
- no dead end.

---

# 28. State-Machine — Silence

Expected:

```text
LISTENING
→ NO_ATTEMPT_DETECTED
→ RESPONDING
→ TRANSITIONING
```

No forced retry.

---

# 29. State-Machine — Parent Stop

Test stopping from:

- READY;
- PLAYING_WORD;
- LISTENING;
- RESPONDING.

No duplicate evidence.

---

# 30. State-Machine — Background

Test background from each active child state.

Expected:

- audio stops safely;
- microphone stops;
- committed evidence remains;
- uncommitted evidence not fabricated.

---

# 31. Stale Callback Tests

Required for:

- AudioService;
- SpeechAttemptDetector;
- delayed transition timer.

Scenario:

```text
interaction A ends
interaction B begins
late callback from A arrives
```

Expected:

> ignored.

---

# 32. Exposure Idempotency Test

Call:

```text
commitExposure(interactionId)
commitExposure(interactionId)
```

Expected:

```text
one exposure only
```

---

# 33. Replay Tests

Validate:

- replay increments replay count;
- replay does not create a new scheduled interaction;
- replay does not increment scheduled exposure count;
- rapid taps do not overlap audio.

---

# 34. Repository Tests

Test each repository against an isolated database.

Required:

```text
ContentRepository
SessionRepository
LearningRepository
RealWorldObservationRepository
FamilyRecordingRepository
```

---

# 35. Database Schema Tests

Validate:

- foreign keys;
- unique constraints;
- enum strings;
- nullability;
- indexes;
- cascade behavior where intended.

---

# 36. Content Seed Tests

Run seed operation:

```text
once
twice
many times
```

Expected:

- exactly 8 concepts;
- exactly 2 languages;
- exactly 16 active pairs;
- no duplicates.

---

# 37. Content Integrity Tests

Fail build/test if:

- missing canonical audio;
- missing primary visual;
- duplicate pair ID;
- invalid language ID;
- missing spoken form;
- unreviewed required content accidentally marked release-ready.

---

# 38. Session Persistence Tests

Test:

- create session;
- create interactions;
- complete interaction;
- complete session;
- fetch recent sessions;
- recover incomplete session.

---

# 39. Process Death Persistence Test

Simulate:

```text
exposure committed
→ process killed
```

Expected:

- exposure survives.

Simulate:

```text
audio starts
→ process killed before threshold
```

Expected:

- no exposure fabricated.

---

# 40. Pair-State Rebuild Test

Given stored evidence:

1. delete/corrupt derived PairLearningState;
2. rebuild;
3. compare to expected derived state.

Evidence remains authoritative.

---

# 41. Real-World Observation Tests

Test:

- record recognition;
- record use;
- duplicate legitimate observations over time;
- exact pair isolation;
- observation influences adaptive state correctly.

---

# 42. Family Recording Metadata Tests

Validate:

- one active recording per pair;
- replacement updates metadata;
- deletion removes metadata;
- missing file handled gracefully.

---

# 43. FileStore Tests

Test:

```text
create temp
promote
exists
delete
```

Ensure paths remain internal.

---

# 44. Family Recording Replacement Test

Scenario:

```text
existing good recording
→ record new temp
→ promotion fails
```

Expected:

> old recording remains valid.

---

# 45. Orphan Temp Cleanup Test

Create stale temp file.

Run recovery.

Expected:

> file removed according to cleanup rules.

---

# 46. Reset Tests

Verify reset removes:

- sessions;
- interactions;
- adaptive state;
- observations;
- family metadata;
- family files;
- selected preferences;
- temp files.

Verify it preserves:

- canonical content;
- app integrity.

---

# 47. Reset Idempotency

Run reset twice.

Expected:

> second reset succeeds safely.

---

# 48. Partial Reset Failure Test

Simulate file deletion failure.

Expected:

- reset does not report full success;
- retry/recovery possible.

---

# 49. AudioService Fake Tests

`FakeAudioService` must support:

```text
Started
ExposureThresholdReached
Completed
Failed
Interrupted
```

---

# 50. Audio Happy Path Test

Expected:

```text
play requested
→ started
→ threshold
→ exposure commit
→ complete
```

---

# 51. Audio Failure Before Threshold

Expected:

```text
no exposure
```

---

# 52. Audio Failure After Threshold

Expected:

```text
exposure remains committed
```

---

# 53. Rapid-Tap Audio Test

Multiple taps during playback:

Expected:

- one active player;
- no overlap;
- no duplicate exposure.

---

# 54. Rotation Audio Test

Rotate mid-word.

Expected:

- no automatic restart;
- no duplicate threshold;
- same interaction preserved.

---

# 55. Audio Asset Device Test

All 16 canonical word clips must:

- resolve;
- decode;
- play audibly;
- match intended pair.

---

# 56. Audio Human QC

A fluent human reviewer must confirm:

- correct word;
- correct language;
- pronunciation quality;
- loudness consistency;
- no clipping/noise.

Tamil requires native/family review.

---

# 57. SpeechAttemptDetector Fake Tests

Fake supports:

```text
AttemptDetected
Timeout
Unavailable
Error
```

---

# 58. Mic Attempt Test

Expected:

```text
LISTENING
→ ATTEMPT_DETECTED
→ stop detector
→ persist once
```

---

# 59. Mic Timeout Test

Expected:

```text
NO_ATTEMPT_DETECTED
```

only after a real completed listening window.

---

# 60. Mic Cancel Test

If app backgrounds during listening:

Expected:

```text
NOT_MEASURED/interrupted
```

not:

```text
NO_ATTEMPT_DETECTED
```

---

# 61. Mic Stale Callback Test

Old detector callback after new interaction starts:

Expected:

> ignored.

---

# 62. Signal-Processing Unit Tests

Using synthetic amplitude arrays, test:

- silence;
- sustained voice-like energy;
- one sharp tap;
- noisy baseline;
- two bursts;
- threshold edge;
- immediate speech after start.

---

# 63. Mic Real-Device Tests

Test:

- child/adult normal speech;
- quiet speech;
- silence;
- fan/AC;
- TV;
- tap noise;
- parent nearby;
- mic busy;
- permission denied.

The goal is usability, not perfect classification.

---

# 64. Privacy Test — No Child Audio File

Procedure:

1. clear app data;
2. run multiple child mic windows;
3. inspect app-private files/cache/temp.

Expected:

> no child speech file exists.

---

# 65. Privacy Test — No Transcript

Inspect:

- database;
- logs;
- domain models.

Expected:

- no transcript field/value.

---

# 66. Privacy Test — No Pronunciation Score

Inspect schema/models/UI.

Expected:

> no score exists.

---

# 67. Privacy Test — Family Audio Private

Create family recording.

Verify:

- stored in app-private directory;
- absent from media gallery;
- no public share URI created.

---

# 68. Privacy Test — Offline

Run full child session in airplane mode.

Expected:

- same behavior;
- no missing assets;
- no network warning.

---

# 69. Manifest Privacy Test

Inspect merged release manifest.

Expected sensitive permission:

```text
RECORD_AUDIO
```

No unexplained:

```text
INTERNET
CAMERA
LOCATION
STORAGE
NOTIFICATIONS
CONTACTS
```

---

# 70. Dependency Privacy/Security Test

Review:

- direct dependencies;
- transitive dependencies;
- permissions;
- network behavior;
- analytics/tracking.

---

# 71. Security Test — Background Mic

Start listening.

Background app.

Expected:

> microphone releases promptly.

---

# 72. Security Test — Parent Recording Background

Start parent recording.

Background/lock screen.

Expected:

> recording stops/cancels safely.

---

# 73. Security Test — Path Boundary

Attempt to supply arbitrary path through higher-level API.

Expected:

> impossible by contract / rejected.

---

# 74. Security Test — Corrupt Family File

Corrupt/delete private family audio.

Expected:

- no crash;
- canonical fallback;
- parent can replace.

---

# 75. Security Test — Database Corruption Simulation

Where practical:

- force recoverable DB inconsistency;
- verify app does not silently create unsafe evidence.

---

# 76. Accessibility Tests

Test child and parent flows against `accessibility.md`.

---

# 77. Touch Target Test

Verify:

- child primary target is large;
- adult controls meet minimum 48dp;
- child secondary controls target ~72dp or more where applicable.

---

# 78. Text Scale Test

Parent mode:

> test around 200% font scale where practical.

No clipping of critical controls.

---

# 79. TalkBack Test

Parent mode should have:

- meaningful labels;
- predictable order;
- controls identifiable.

Child mode semantics should be useful where feasible without requiring reading.

---

# 80. Color Test

Ensure meaning does not depend on:

- red/green;
- color alone.

---

# 81. Reduced Motion Test

Enable reduced-motion preference/system setting.

Expected:

- core meaning remains;
- no essential information lost.

---

# 82. Orientation Accessibility Test

Accessibility remains usable in:

- portrait;
- landscape.

---

# 83. UI Tests — Child Flow

Automate a small number of high-value paths:

```text
launch
tap concept
word plays
response flow
next concept
stop session
```

Use fake audio/mic for deterministic UI tests where possible.

---

# 84. UI Tests — Parent Flow

Automate:

- enter parent area;
- view suggestion;
- mark recognized;
- mark used;
- toggle mic;
- reset confirmation.

---

# 85. Permission UI Tests

Test semantic product logic for:

```text
NOT_DETERMINED
GRANTED
DENIED
DENIED_DONT_ASK_AGAIN
UNAVAILABLE
```

System-dialog automation may vary by API/device.

---

# 86. Performance Tests

Measure on release-like build:

- cold launch;
- warm launch;
- tap feedback;
- tap-to-audio;
- active memory;
- repeated-session memory;
- mic CPU/jank.

---

# 87. Startup Acceptance

Targets:

```text
cold launch ~<=4 s
warm launch ~<=2 s
```

on target family hardware.

---

# 88. Tap-to-Audio Acceptance

Target:

```text
normally <=250 ms
```

on built-in speaker.

---

# 89. Tap Feedback Acceptance

Target:

```text
normally <=100 ms
```

---

# 90. Stress Test

Automate/manual:

```text
50–100 interaction transitions
```

Check:

- memory stability;
- player release;
- mic release;
- no duplicate events.

---

# 91. Rotation Stress

Rotate repeatedly during:

- READY;
- PLAYING_WORD;
- LISTENING;
- RESPONDING;
- parent screens.

No state/evidence corruption.

---

# 92. Long-History Test

Generate synthetic history representing months of use.

Expected:

- startup remains responsive;
- adaptive selection remains fast;
- parent recent view remains usable.

---

# 93. Low-Storage Test

Simulate low device storage.

Test:

- family recording save failure;
- DB write failure behavior.

No false success.

---

# 94. Low-Memory Test

Under memory pressure:

- caches may disappear;
- app should recreate resources safely;
- persistent evidence remains.

---

# 95. Device Matrix

At minimum:

```text
Primary family Android tablet
Android 16/API 36 emulator
Minimum-SDK emulator
Additional modest tablet if available
```

---

# 96. Physical-Device Requirements

Real hardware is mandatory for:

- audio latency;
- mic behavior;
- volume;
- child touch;
- orientation feel;
- family recording.

---

# 97. Prototype Content Acceptance

Before family test:

- all 8 concepts approved;
- all English forms approved;
- all Tamil forms approved;
- all 16 canonical audio clips reviewed;
- all visuals reviewed;
- no unresolved `TBD` in active child content.

---

# 98. Tamil Language Review

Native/family reviewer confirms:

- lexical choice;
- household-natural register;
- pronunciation;
- concept-image match.

Cup and water require explicit resolution before recording freeze.

---

# 99. Family-Test Readiness Gate

A build is ready for seven-day family testing only when:

```text
critical tests pass
release blockers = 0
content frozen
adaptive config frozen
mic config frozen
primary device passes
```

---

# 100. Family-Test Build Freeze

During the seven-day test:

Do not change:

- adaptive constants;
- content words;
- canonical audio;
- core UI flow;
- microphone thresholds

unless a material defect prevents meaningful use.

---

# 101. Family-Test Observation Is Not Automated Test

The family test answers questions such as:

- does child understand the loop?
- does child voluntarily repeat?
- is interaction calm?
- does child return willingly?
- does a word appear outside the app?

It should not be used to discover obvious crashes that automated/manual QA should already catch.

---

# 102. Defect Severity

## BLOCKER

Examples:

- app cannot start;
- raw child audio persists;
- background mic;
- duplicate learning evidence;
- reset leaks family audio;
- canonical audio unavailable broadly.

## HIGH

Examples:

- frequent audio delay/failure;
- rotation corrupts session;
- mic denial dead-ends child.

## MEDIUM

Examples:

- parent copy issue;
- rare visual layout problem;
- family recording replacement friction.

## LOW

Examples:

- cosmetic spacing;
- minor non-blocking animation issue.

---

# 103. Release Criteria

Before family test:

```text
BLOCKER defects = 0
HIGH defects = 0
MEDIUM defects = explicitly reviewed
LOW defects = acceptable if not child-impacting
```

---

# 104. Regression Suite

After any substantial code change, rerun:

- adaptive tests;
- state-machine tests;
- DB/repository tests;
- stale callback tests;
- reset tests;
- privacy tests;
- permission tests;
- critical UI smoke path.

---

# 105. CI Requirements

Recommended CI pipeline:

```text
compile
→ unit tests
→ schema/content validation
→ lint
→ static checks
→ repository tests where feasible
```

Instrumented/emulator tests may run in a separate workflow if needed.

---

# 106. Build-Failure Conditions

CI should fail for:

- unit-test failure;
- invalid content manifest;
- missing required canonical asset;
- schema validation failure;
- forbidden permission if enforceable by automated check;
- compile/lint error deemed blocking.

---

# 107. Test Naming

Prefer behavior names such as:

```text
commitExposure_twice_incrementsOnlyOnce
rotation_duringPlayback_doesNotRestartExposure
micDenied_childSessionStillContinues
realWorldUse_tamilPair_doesNotChangeEnglishPair
```

---

# 108. Test Independence

Tests should not depend on:

- execution order;
- wall clock;
- network;
- previous database state.

---

# 109. Determinism

Where product behavior uses:

- time;
- randomness;
- callbacks;

inject fakes.

Flaky tests should be treated as test defects.

---

# 110. No Network in Tests

Core tests must not require internet.

This mirrors production architecture.

---

# 111. Test Evidence

For release/family test, keep a simple record:

```text
build version
date
device
test suite result
known issues
family-test readiness decision
```

No enterprise test-management platform is required.

---

# 112. Test Artifacts

Useful artifacts may include:

```text
test-report.md
device-test results
performance baseline
content review checklist
family-test readiness checklist
```

These belong in QA/release workflows.

---

# 113. Testing Invariants

### TEST-INV-001
Critical learning logic is deterministic and unit tested.

### TEST-INV-002
Exposure idempotency is tested.

### TEST-INV-003
Stale audio/mic callbacks are tested.

### TEST-INV-004
Rotation/background lifecycle is tested.

### TEST-INV-005
No child audio-file creation is manually/technically verified.

### TEST-INV-006
The app is tested with microphone denied.

### TEST-INV-007
Airplane-mode behavior is tested.

### TEST-INV-008
All 16 canonical audio assets receive human/device review.

### TEST-INV-009
Real-device testing is mandatory before family testing.

### TEST-INV-010
Family testing does not substitute for basic QA.

### TEST-INV-011
Adaptive/microphone/content constants remain frozen during the family test.

### TEST-INV-012
Release blockers must be zero before family testing.

---

# 114. Open Testing Decisions

Before implementation freeze, confirm:

1. exact Kotlin test libraries;
2. exact Android UI test framework;
3. whether screenshot/golden tests are useful for layout;
4. whether Macrobenchmark is maintained for Prototype 0.1;
5. actual physical device matrix;
6. exact CI split between JVM and emulator tests;
7. whether permission allowlist checking is automated in CI;
8. exact family-test build versioning scheme.

---

# 115. Relationship to `acceptance-criteria.md`

`acceptance-criteria.md` should convert these strategies into explicit pass/fail requirements for Prototype 0.1.

---

# 116. Relationship to `device-test-matrix.md`

That document records the actual:

- devices;
- OS versions;
- orientation;
- audio/mic routes;
- pass/fail status.

---

# 117. Relationship to `family-test-protocol.md`

This testing strategy must be complete enough that the family test is focused on behavior and product fit rather than obvious software defects.

---

# 118. Relationship to `qa-checklist.md`

`qa-checklist.md` should provide the concise release-execution checklist derived from this larger document.

---

# 119. Relationship to `release.md`

`release.md` defines how passing test evidence becomes an approved build.

---

# 120. Decision Summary

Miozira Prototype 0.1 testing focuses on the few things that can most seriously invalidate the product test:

```text
wrong learning evidence
bad lifecycle behavior
audio failure
microphone/privacy mistakes
storage/reset defects
unsafe child interactions
```

The strategy is:

```text
deterministic unit tests
+
repository/state integration tests
+
small focused UI suite
+
real-device audio/mic/accessibility testing
+
frozen seven-day family-test build
```

The governing rule is:

> **Before asking whether Miozira works for the child, first prove that the software itself is behaving exactly as designed.**
