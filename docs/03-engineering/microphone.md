# Miozira — Microphone Specification

**Document:** `microphone.md`  
**Version:** 0.1  
**Status:** Draft for Prototype 0.1  
**Product:** Miozira  
**Architecture:** Local-only child speech-attempt detection  
**Primary Android direction:** `AudioRecord`  
**Prototype:** 8 concepts × English + Tamil  
**Last updated:** 2026-08-25

---

## 1. Purpose

This document defines microphone behavior for Miozira Prototype 0.1.

It covers:

- child speaking-attempt detection;
- Android capture direction;
- transient PCM handling;
- short listening windows;
- local signal-processing heuristics;
- noise-floor handling;
- false-positive/false-negative strategy;
- permission behavior;
- lifecycle and interruption;
- privacy;
- testability;
- parent family-recording separation.

This document does **not** define pronunciation grading, transcription, or speech recognition.

Prototype 0.1 explicitly does not perform those tasks.

---

# 2. Core Microphone Principle

The microphone answers only one question:

> **Did speech-like activity probably occur during the response window?**

It does **not** answer:

- what word was said;
- whether the word was correct;
- how well it was pronounced;
- which phonemes were produced;
- what language the child spoke;
- whether the child knows the concept.

---

# 3. Output States

The child speaking-attempt detector returns one of:

```text
ATTEMPT_DETECTED
NO_ATTEMPT_DETECTED
MICROPHONE_UNAVAILABLE
NOT_MEASURED
```

Potential internal technical errors may map to:

```text
MICROPHONE_UNAVAILABLE
```

or an error object, while the child flow remains non-blocking.

---

# 4. Privacy Boundary

Prototype 0.1 child microphone handling must satisfy:

```text
capture transient PCM
→ process locally in memory
→ emit attempt/no-attempt result
→ discard PCM
```

It must not:

```text
save PCM
upload PCM
transcribe speech
send speech to a cloud service
build a voice profile
store pronunciation features permanently
```

---

# 5. Android Capture Direction

Recommended Android implementation:

> **`AudioRecord`**

Reasons:

- direct access to PCM;
- supports short controlled windows;
- no need to create an audio file;
- suitable for local amplitude/voice-activity analysis;
- explicit lifecycle control.

This direction should be validated in the technical spike.

---

# 6. Why Not SpeechRecognizer

Prototype 0.1 should not use platform/cloud speech-recognition APIs as the primary child detector because Miozira does not need:

- transcription;
- word matching;
- language decoding.

Using recognition would add:

- privacy complexity;
- reliability problems with preschool speech;
- network/service dependency risk;
- temptation to misinterpret recognition confidence as pronunciation quality.

---

# 7. Why Not MediaRecorder for Child Attempts

`MediaRecorder` is designed around recording media output.

Prototype 0.1 does not need a child speech file.

`AudioRecord` is therefore a better conceptual fit for:

> transient signal analysis only.

Parent family recording is a separate use case and may use a higher-level recorder.

---

# 8. Listening Window

Recommended Prototype 0.1 duration:

> approximately **2–3 seconds**

The exact value should be configurable.

Suggested constant:

```text
speechAttemptWindowMs = 2500
```

Initial tests may compare:

```text
2000 ms
2500 ms
3000 ms
```

---

# 9. Window Start

The microphone window should begin only after:

1. canonical word playback completes;
2. optional short post-word pause/prompt completes;
3. the child is actually being invited to speak.

Do not listen while the app is playing the target word.

---

# 10. Window End

Listening ends when:

- attempt is detected; or
- timeout occurs; or
- app backgrounds; or
- session ends; or
- permission/capture fails.

Stop capture promptly.

---

# 11. No Continuous Microphone

The child microphone must never run continuously across:

- entire session;
- app background;
- parent mode;
- idle state.

It opens only for short explicit response windows.

---

# 12. Audio Buffer Handling

PCM buffers should remain in memory only long enough to compute simple detection features.

After use:

- release references;
- clear/reuse buffers;
- do not persist to disk.

---

# 13. Sample Format Direction

A practical Android starting point is likely:

```text
mono
16-bit PCM
16 kHz or platform-stable supported rate
```

The final sample rate should be chosen based on:

- device compatibility;
- speech-attempt detection reliability;
- CPU/battery efficiency.

There is no need for high-fidelity studio capture.

---

# 14. Channel Count

Use:

> mono

No stereo information is required for attempt detection.

---

# 15. Signal Features

Prototype 0.1 should use simple local heuristics.

Possible features:

- RMS energy;
- short-term energy;
- peak amplitude;
- duration above threshold;
- noise-floor-relative energy;
- simple zero-crossing or spectral guard only if needed.

Do not build a complex ML speech recognizer.

---

# 16. Preferred Detection Strategy

Recommended starting strategy:

```text
1. estimate current/local noise floor
2. monitor short PCM frames
3. identify sustained energy sufficiently above noise floor
4. require minimum duration / multiple frames
5. emit ATTEMPT_DETECTED once
```

This is a heuristic, not a speech classifier.

---

# 17. Frame Size

Possible starting frame size:

```text
20–40 ms
```

This is common for lightweight voice activity processing.

Exact frame size should be tuned experimentally.

---

# 18. Noise-Floor Estimation

A fixed absolute amplitude threshold is fragile across:

- tablets;
- microphone gain;
- rooms;
- fan noise;
- distance from child.

Prefer a threshold relative to measured ambient noise.

---

# 19. Ambient Calibration

Before or at the beginning of the response window, the detector may sample a short baseline.

Possible approach:

```text
first 150–300 ms
→ estimate ambient RMS
```

Then detection threshold becomes:

```text
ambient + margin
```

Do not make calibration long enough to miss immediate child speech.

---

# 20. Immediate Speech Problem

A child may speak immediately after hearing the word.

Therefore ambient calibration must not discard the entire early segment.

Possible approaches:

- use a rolling noise estimate;
- use previous quiet frames;
- keep calibration brief;
- allow early high-energy frames to trigger detection.

This must be tested.

---

# 21. Attempt Threshold

A starting heuristic may require:

```text
energy > noiseFloor + thresholdMargin
```

for at least:

```text
N consecutive or near-consecutive frames
```

Exact constants must remain configurable.

---

# 22. Duration Guard

Single transient sounds should not always count as speech.

Require speech-like energy for a minimum duration.

Possible starting range:

> approximately **120–250 ms**

This helps reject:

- one tap;
- one click;
- handling noise.

---

# 23. False Positive Sources

Potential false positives:

- tablet speaker leakage;
- parent speaking;
- television;
- sibling;
- chair movement;
- tap impact;
- cough;
- laugh;
- toy sound.

Prototype 0.1 cannot perfectly distinguish these.

---

# 24. False Negative Sources

Potential false negatives:

- very quiet child speech;
- child far from microphone;
- whispered speech;
- noisy room;
- microphone gain differences;
- very short vocalization.

---

# 25. Product Bias

Because the signal is weak evidence, the detector should prefer:

> avoiding strong product conclusions.

A false negative should not create punishment.

A false positive should not produce a claim of correct pronunciation.

This makes heuristic imperfection acceptable for the prototype.

---

# 26. Attempt Detection Semantics

If a cough or parent voice triggers detection:

the stored event still only means:

> speech-like activity was detected.

The product must not translate that into:

> child correctly said the word.

---

# 27. No Pronunciation Confidence

The detector must not output:

```text
confidence = 0.87
pronunciationAccuracy = 92%
phonemeMatch = ...
```

These fields should not exist in Prototype 0.1 contracts.

---

# 28. No Expected-Word Input

The detector should not require:

```text
expectedWord = "apple"
```

It only needs:

```text
duration
sessionId
interactionId
```

This architectural constraint helps prevent scope drift into recognition.

---

# 29. Listening Indicator

While capture is active:

- child UI may show a calm listening indicator;
- no waveform;
- no volume meter;
- no score.

The indicator disappears immediately after capture ends.

---

# 30. Microphone Permission

Android microphone permission must be requested only after parent-facing explanation.

The child should never be responsible for interpreting the system permission dialog.

---

# 31. Permission States

Recommended internal values:

```text
GRANTED
DENIED
DENIED_DONT_ASK_AGAIN
NOT_DETERMINED
UNAVAILABLE
```

---

# 32. Permission Denied Flow

If permission is denied:

```text
word playback
→ short pause
→ neutral response
→ next item
```

No dead end.

Persist:

```text
MICROPHONE_UNAVAILABLE
```

only when an interaction expected measurement and capability was unavailable.

Otherwise:

```text
NOT_MEASURED
```

may be more appropriate.

---

# 33. Detection Disabled by Parent

If the parent explicitly disables speaking-attempt detection:

- do not request permission during child flow;
- do not open microphone;
- do not show listening indicator.

Attempt state should normally be:

```text
NOT_MEASURED
```

not:

```text
MICROPHONE_UNAVAILABLE
```

because the feature was intentionally disabled.

---

# 34. Microphone Unavailable

Examples:

- permission denied;
- hardware unavailable;
- capture API failure;
- another app owns microphone.

Child flow continues normally.

---

# 35. Lifecycle — Backgrounding

If app backgrounds during listening:

1. stop `AudioRecord`;
2. release resources;
3. discard transient PCM;
4. do not record `NO_ATTEMPT_DETECTED` merely because the window was interrupted;
5. end or safely resume the interaction according to state-machine rules.

---

# 36. Lifecycle — Orientation

Rotation should not create a second capture window.

Use one logical:

```text
SpeechAttemptHandle
```

per interaction attempt.

If platform recreation forces capture restart, the controller should preserve semantic identity and avoid double-recording evidence.

Preferred:

> preserve the active service/window across UI recreation.

---

# 37. Lifecycle — Process Death

If the process dies during listening:

- transient PCM disappears;
- no attempt result should be fabricated;
- interaction may remain `NOT_MEASURED` or interrupted;
- committed exposure remains unaffected.

---

# 38. Audio Coordination

Target-word playback and child listening should be mutually coordinated.

Sequence:

```text
play word
→ complete
→ optional prompt
→ start microphone
```

This reduces false detection from Miozira's own speaker.

---

# 39. Echo / Speaker Leakage

On some devices, residual speaker sound may leak into the microphone.

Mitigations:

- short post-playback pause;
- do not open capture until playback ends;
- noise threshold relative to current environment;
- minimum duration requirement.

Do not add heavy echo cancellation unless actual testing shows it is necessary.

---

# 40. Parent Voice During Child Window

A nearby parent may speak.

Prototype 0.1 cannot reliably identify speaker identity.

This is acceptable because the signal is only participation evidence.

Family-test guidance should encourage parents not to answer during the child's brief response window if they want cleaner observations.

---

# 41. Attempt Event Timing

When attempt is detected:

- stop capture promptly;
- emit one result;
- ignore later buffer events.

Only one terminal result per window.

---

# 42. Terminal Results

A listening window has exactly one terminal outcome:

```text
ATTEMPT_DETECTED
NO_ATTEMPT_DETECTED
MICROPHONE_UNAVAILABLE
CANCELLED/NOT_MEASURED internally
```

Persistence maps to the approved learning-model enum.

---

# 43. Callback Identity

Callbacks must be associated with:

```text
sessionId
interactionId
speechAttemptHandle
```

Stale callbacks are ignored.

---

# 44. No Duplicate Attempt Writes

Repository write:

```text
recordAttempt(interactionId, state)
```

should not overwrite a finalized attempt with a stale later callback.

Preferred rule:

> first valid terminal attempt state wins for the active window.

---

# 45. Attempt State Update Rules

Allowed:

```text
NOT_MEASURED
→ ATTEMPT_DETECTED
```

```text
NOT_MEASURED
→ NO_ATTEMPT_DETECTED
```

```text
NOT_MEASURED
→ MICROPHONE_UNAVAILABLE
```

Avoid arbitrary later transitions.

---

# 46. Parent Family Recording Separation

Parent recording is a separate service.

Child detector:

```text
SpeechAttemptDetector
```

Parent recorder:

```text
FamilyRecordingService
```

They should not share a public API.

---

# 47. No Concurrent Capture

Before parent recording starts:

```text
SpeechAttemptDetector.stopAll()
```

Before child attempt detection starts:

- parent recording must not be active.

---

# 48. Resource Ownership

The microphone subsystem must have one clear owner at a time.

Possible owner states:

```text
NONE
CHILD_ATTEMPT
PARENT_RECORDING
```

This can be enforced by a small platform microphone coordinator if needed.

---

# 49. Threading

PCM capture and frame analysis should run off the UI thread.

State result callbacks should be marshaled safely to the controller.

---

# 50. CPU Usage

The detector should remain lightweight.

A 2–3 second window with simple RMS/noise analysis should not require:

- neural models;
- continuous FFT-heavy processing;
- background worker.

---

# 51. Battery Usage

Because capture windows are short and event-driven, microphone energy cost should remain low.

No microphone work occurs while idle.

---

# 52. Configuration

Detection constants should be centralized.

Example:

```text
windowDurationMs
frameDurationMs
noiseCalibrationMs
thresholdMarginDbOrRatio
minimumActiveDurationMs
maximumSilenceGapMs
```

Do not scatter magic numbers through platform code.

---

# 53. Config Version

If detection constants change materially during future tests, track:

```text
microphone_config_version
```

in development diagnostics.

Prototype 0.1 may not need this in the production database unless comparisons require it.

---

# 54. Freeze During Family Test

Once the 7-day family test begins:

- do not casually retune thresholds daily.

Otherwise:

- attempt evidence becomes incomparable across days.

Bug fixes are allowed if detection is clearly broken.

---

# 55. Technical Calibration Test

Before family testing, test the detector with:

- adult normal speech;
- adult quiet speech;
- child normal speech;
- child quiet speech;
- silence;
- fan noise;
- TV noise;
- tap noise;
- cough;
- parent speaking nearby.

The purpose is not perfect classification.

The purpose is to ensure thresholds are not obviously unusable.

---

# 56. Device Calibration

Test on:

- intended family tablet;
- at least one lower-end Android device/tablet profile if available.

Microphone gain varies by hardware.

---

# 57. Distance Testing

Test roughly at:

```text
close tablet use
normal arm/table distance
slightly farther than normal
```

Do not optimize for room-scale capture.

Miozira expects the child to be using the tablet nearby.

---

# 58. Noise Scenarios

At minimum:

```text
quiet room
fan/AC
normal family conversation in background
TV at moderate volume
```

The detector should degrade gracefully.

---

# 59. False Positive Policy

A moderate false-positive rate may be acceptable because:

- attempt detection is weak evidence;
- no correctness is inferred;
- no reward/score depends on it.

But excessive false positives would make the signal meaningless.

---

# 60. False Negative Policy

A moderate false-negative rate may also be acceptable because:

- silence/no-attempt does not punish;
- parent real-world evidence can override;
- adaptive engine treats no-attempt weakly.

The detector should not be tuned aggressively at the cost of many false positives.

---

# 61. Recommended Optimization Objective

Prefer:

> **reasonable precision over maximum recall**

In other words:

if uncertain, it is better to miss some quiet attempts than to treat every environmental sound as a child speaking attempt.

This remains a prototype heuristic.

---

# 62. No Child-Facing Retry Requirement

If no attempt is detected:

- child is not forced to repeat;
- the app may replay once;
- then continue.

---

# 63. Data Persistence

Persist only:

```text
attempt_state
attempt_recorded_at
```

Do not persist:

- RMS trace;
- frame-level energy;
- raw PCM;
- waveform;
- voice fingerprint.

---

# 64. Development Diagnostics

During engineering tests, temporary local debug instrumentation may expose:

```text
noise floor
peak RMS
active-frame count
threshold
detection latency
```

These should be:

- development-only;
- not uploaded;
- not stored as child-learning history.

---

# 65. Production Logging

Production/family-test logs may record:

```text
window started
attempt detected
timeout
unavailable
error
```

No audio buffers.

---

# 66. Error Types

Recommended semantic errors:

```text
PermissionDenied
CaptureInitializationFailed
CaptureStartFailed
CaptureReadFailed
MicrophoneBusy
UnsupportedConfiguration
UnexpectedMicrophoneError
```

---

# 67. Child Error Handling

Child mode never shows raw microphone errors.

Response:

```text
neutral fallback
→ continue session
```

---

# 68. Parent Error Handling

Parent settings may say:

> “Miozira couldn't use the microphone. Speaking-attempt detection is off for now.”

No technical code.

---

# 69. Accessibility

Speech attempt detection is optional.

A child with:

- speech delay;
- articulation difference;
- selective mutism;
- quiet voice;
- no interest in repeating

still receives the full learning flow.

---

# 70. Language Neutrality

The detector should be language-independent.

It does not need separate English/Tamil models.

This is another reason to use generic voice-activity heuristics.

---

# 71. No Speaker Identification

Prototype 0.1 does not attempt to distinguish:

- child;
- parent;
- sibling.

Speaker identification would add privacy and technical complexity disproportionate to the prototype.

---

# 72. No Biometric Use

Voice is not used for:

- identity;
- authentication;
- profile recognition;
- biometrics.

---

# 73. No Cloud SDK

Do not integrate:

- cloud speech-to-text SDK;
- cloud pronunciation SDK;
- remote VAD API.

The detector remains local.

---

# 74. No Model Download

Prototype 0.1 should not download speech models at runtime.

Simple local heuristic processing is sufficient.

---

# 75. API Contract

As defined in `api.md`:

```kotlin
interface SpeechAttemptDetector {
    suspend fun start(
        request: SpeechAttemptRequest,
        listener: SpeechAttemptListener
    ): AppResult<SpeechAttemptHandle>

    suspend fun stop(
        handle: SpeechAttemptHandle
    ): AppResult<Unit>

    suspend fun stopAll(): AppResult<Unit>
}
```

---

# 76. Start Preconditions

Before starting:

- parent setting enabled;
- permission granted;
- no parent recording active;
- no existing child window active;
- target audio finished;
- interaction still active.

---

# 77. Start Failure

If any precondition fails:

return semantic unavailable/failure result.

Do not partially open capture.

---

# 78. Stop Idempotency

Calling:

```text
stop(handle)
```

more than once should be safe.

Expected:

- first call releases resources;
- later call no-ops or returns already-stopped success.

---

# 79. stopAll()

`stopAll()` is required for:

- app background;
- session end;
- parent gate entry;
- recovery;
- parent family recording start.

---

# 80. Technical Spike Requirements

The stack spike must prove:

```text
1. AudioRecord can initialize on target tablet
2. short PCM windows can be captured
3. data stays in memory
4. simple energy/noise detection works
5. stop releases microphone promptly
6. app background stops capture
7. rotation does not create duplicate windows
8. child attempt result reaches shared state layer
9. no file is created
10. no network is used
```

---

# 81. Automated Tests

Use a fake detector for state-machine tests.

Test:

- attempt detected;
- timeout;
- unavailable;
- error;
- stale callback;
- stop during window;
- session end during window;
- parent gate during window.

---

# 82. Signal-Processing Unit Tests

If the detection algorithm is factored into pure shared/platform-independent code:

test with synthetic/sample amplitude arrays for:

- silence;
- sustained speech-like energy;
- one transient spike;
- noisy baseline;
- two short bursts;
- threshold edge.

Avoid storing real child voice samples in the repository.

---

# 83. Manual Device Tests

At minimum:

1. quiet room;
2. normal child speech;
3. quiet speech;
4. silence;
5. tap noise;
6. adult speaking nearby;
7. TV/fan background;
8. permission denied;
9. permission revoked mid-use;
10. microphone busy;
11. app background;
12. rotation;
13. rapid session stop;
14. parent recording after child window.

---

# 84. Family-Test Interpretation

Attempt detection is exploratory evidence.

During the family test, compare:

- detector result;
- parent observation of whether child actually tried to speak.

This helps determine whether the heuristic is useful enough to keep.

---

# 85. Keep / Remove Decision

After Prototype 0.1, microphone attempt detection should be retained only if it provides useful signal without adding:

- privacy concern;
- confusion;
- significant false detections;
- engineering instability.

The product does not depend on it.

---

# 86. Microphone Invariants

### MIC-INV-001
Child microphone windows are short-lived.

### MIC-INV-002
No continuous background listening occurs.

### MIC-INV-003
Raw child PCM is never persisted.

### MIC-INV-004
Raw child audio is never uploaded.

### MIC-INV-005
No transcription is produced.

### MIC-INV-006
No pronunciation score is produced.

### MIC-INV-007
No expected target word is required by the detector.

### MIC-INV-008
Microphone denial does not block learning.

### MIC-INV-009
Parent-disabled detection maps to `NOT_MEASURED`, not failure.

### MIC-INV-010
No-attempt is persisted only after a completed real listening window.

### MIC-INV-011
Target-word audio does not intentionally overlap the child listening window.

### MIC-INV-012
Only one child listening window is active at a time.

### MIC-INV-013
Child detector and parent recorder cannot capture concurrently.

### MIC-INV-014
A stale callback cannot modify a newer interaction.

### MIC-INV-015
The detector is language-neutral.

### MIC-INV-016
The detector is not a biometric system.

---

# 87. Open Microphone Decisions

Before implementation freeze, confirm:

1. Android sample rate;
2. frame size;
3. noise-floor estimation method;
4. threshold margin;
5. minimum active duration;
6. exact listening-window duration;
7. whether a simple VAD library is justified or custom RMS logic is enough;
8. how to handle immediate post-word child speech;
9. whether microphone attempt detection remains enabled by default after parent consent;
10. whether device-specific threshold tuning is necessary.

These decisions should come from the technical spike and real-device tests.

---

# 88. Relationship to `audio.md`

Required sequence:

```text
canonical word
→ optional short pause/prompt
→ microphone window
```

Audio and microphone must coordinate ownership.

---

# 89. Relationship to `interaction-states.md`

Microphone results drive:

```text
LISTENING
→ RESPONDING
```

but cannot bypass valid state transitions.

---

# 90. Relationship to `database.md`

The database stores only:

```text
attempt_state
attempt_recorded_at
```

No raw signal data.

---

# 91. Relationship to `privacy.md`

`privacy.md` must repeat the exact operational fact:

> child speech-attempt audio is processed transiently on-device and is not saved or uploaded.

---

# 92. Relationship to `parent-experience.md`

Parent messaging must remain consistent:

- microphone use is optional;
- Miozira only detects whether the child probably tried to speak;
- pronunciation is not graded;
- ordinary child speech is not saved.

---

# 93. Decision Summary

Prototype 0.1 microphone behavior is deliberately narrow:

1. Android uses a likely `AudioRecord`-based short capture window;
2. capture lasts roughly 2–3 seconds;
3. PCM remains transient in memory;
4. simple local noise-relative voice-activity heuristics determine whether speech-like activity occurred;
5. no transcript or pronunciation score exists;
6. no expected word is supplied to the detector;
7. no raw child audio is saved or uploaded;
8. false positives/negatives are tolerated because evidence is weak and non-punitive;
9. permission denial or detector failure never blocks learning;
10. parent family recording is a separate microphone capability;
11. thresholds are configurable and frozen during the seven-day family test;
12. the feature can be removed later if it proves more complex than useful.

The governing rule is:

> **The microphone may notice participation, but it must never pretend to understand or judge the child.**
