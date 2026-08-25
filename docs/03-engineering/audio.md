# Miozira — Audio Specification

**Document:** `audio.md`  
**Version:** 0.1  
**Status:** Draft for Prototype 0.1  
**Product:** Miozira  
**Architecture:** Local-first, KMP/CMP with platform audio adapters  
**Prototype:** 8 concepts × English + Tamil  
**Last updated:** 2026-08-25

---

## 1. Purpose

This document defines audio behavior for Miozira Prototype 0.1.

It covers:

- canonical word playback;
- prompt playback;
- family voice playback;
- audio source priority;
- latency targets;
- valid exposure thresholds;
- replay behavior;
- overlapping-audio prevention;
- loudness consistency;
- file and codec guidance;
- audio focus / interruptions;
- device route changes;
- lifecycle behavior;
- preload/caching;
- failure handling;
- accessibility and privacy boundaries.

This document does **not** define child microphone signal detection.

That belongs in:

> `microphone.md`

---

# 2. Audio Role in Miozira

Audio is the primary linguistic signal in Prototype 0.1.

The child interaction is:

```text
see concept
→ tap
→ hear target spoken form
→ optionally imitate
```

Therefore canonical audio quality is a core product requirement, not decorative media.

---

# 3. Audio Categories

Prototype 0.1 contains four conceptual audio categories:

```text
1. Canonical target-word audio
2. Short interaction prompt audio
3. Optional neutral acknowledgement audio
4. Parent/family voice recordings
```

Each category has different quality and lifecycle rules.

---

# 4. Canonical Target-Word Audio

Canonical target-word audio is the reviewed pronunciation model for a concept-language pair.

Examples:

```text
pair.apple.en
pair.apple.ta
```

Each active pair must have:

> exactly one active canonical target-word audio asset.

---

# 5. Canonical Audio Requirements

Canonical clips must be:

- human-recorded where practical;
- clearly pronounced;
- natural;
- short;
- free of background music;
- free of competing speech;
- free of clipping;
- reasonably consistent in loudness;
- reviewed for the target language.

---

# 6. Word-Only Principle

Canonical word audio should normally contain only the target spoken form.

Example:

```text
"Apple."
```

not:

```text
"This is an apple. Can you say apple?"
```

Prompts remain separate assets.

---

# 7. No Translation Chaining in One Clip

Do not create canonical assets such as:

```text
"Apple. ஆப்பிள்."
```

Canonical identity is one:

```text
concept-language pair
```

per clip.

---

# 8. Canonical Asset Identity

Use logical asset IDs.

Example:

```text
audio.word.apple.en.v1
audio.word.apple.ta.v1
```

Code should not depend semantically on:

```text
word_apple_en_v1.m4a
```

as the permanent identity.

---

# 9. Physical File Naming

Recommended development naming:

```text
word_apple_en_v1.m4a
word_apple_ta_v1.m4a
word_ball_en_v1.m4a
word_ball_ta_v1.m4a
```

The extension may change without changing the logical asset ID.

---

# 10. Audio Format Direction

Prototype 0.1 should use a compact, widely supported compressed audio format suitable for short speech clips.

Leading candidates:

- AAC in `.m4a`;
- another platform-supported speech-friendly format if testing shows a clear advantage.

The final choice should prioritize:

- low startup latency;
- broad Android/iOS support;
- small bundle size;
- clean speech reproduction.

Do not introduce an exotic codec for negligible size savings.

---

# 11. Source Recording Quality

Source recordings should be captured at higher quality than final packaged delivery where practical.

Recommended production workflow:

```text
clean source recording
→ edit/trim
→ loudness/quality review
→ export app-ready asset
```

Keep source masters separately from compressed runtime files.

---

# 12. Sample Rate Guidance

Speech does not require unusually high sample rates.

A practical runtime target is likely:

```text
44.1 kHz or 48 kHz
```

depending on the chosen recording/playback pipeline.

Do not resample repeatedly across build steps.

Final value should be standardized before asset production.

---

# 13. Channel Count

Canonical word clips should normally be:

> mono

unless a platform pipeline makes stereo packaging materially simpler without cost.

There is no pedagogical need for stereo positioning in Prototype 0.1.

---

# 14. Bitrate

Choose a bitrate sufficient for clear speech.

Do not over-compress to the point where:

- consonants become smeared;
- Tamil phonetic distinctions become less clear;
- artifacts become audible.

Bundle size is not currently a serious constraint with only 16 canonical word clips plus a small prompt set.

---

# 15. Silence Trimming

Canonical clips should avoid excessive:

- leading silence;
- trailing silence.

But do not trim so tightly that speech onset/offset feels clipped.

Recommended rough target:

```text
leading silence: minimal
trailing silence: short and natural
```

Exact millisecond values should be decided after real recordings exist.

---

# 16. Loudness Consistency

Canonical clips should be normalized to a reasonably consistent perceived loudness.

Goal:

> the child should not need to change volume from one word to another.

Avoid:

- one whisper-quiet clip;
- one startlingly loud clip.

---

# 17. Peak Safety

Audio processing must avoid digital clipping.

Normalization should preserve clean peaks.

---

# 18. No Background Music

Core child learning sessions shall not use continuous background music underneath target speech.

Reasons:

- reduces linguistic masking;
- lowers stimulation;
- simplifies microphone coordination;
- supports accessibility.

---

# 19. Prompt Audio

Prompt audio is separate from target-word audio.

Possible prompt IDs:

```text
prompt.your_turn.en
prompt.your_turn.ta
prompt.listen.en
prompt.listen.ta
```

Prototype 0.1 should keep this set extremely small.

---

# 20. Prompt Minimalism

A prompt should exist only when it materially improves the child flow.

Do not automatically add spoken narration for every state.

The target word remains the most important sound.

---

# 21. Prompt Overlap Rule

Prompt audio must never overlap canonical word audio.

Preferred sequence:

```text
canonical word completes
→ brief pause
→ optional prompt
→ listening window
```

---

# 22. Acknowledgement Audio

Neutral acknowledgement audio may be:

- a short spoken phrase;
- a soft non-verbal sound;
- omitted entirely.

It must not imply:

- correct pronunciation;
- score;
- mastery.

---

# 23. Family Voice Audio

Family voice is optional parent-recorded audio associated with a concept-language pair.

It is:

- local;
- replaceable;
- deletable;
- not the canonical pronunciation record.

---

# 24. Canonical vs Family Audio

Both may coexist:

```text
pair.apple.ta

canonical:
audio.word.apple.ta.v1

family:
family_recording:<local-id>
```

Family audio never deletes the canonical asset.

---

# 25. Playback Source Policy

Prototype 0.1 should make the playback policy explicit.

Recommended starting rule:

> canonical audio remains the default learning model.

Family voice may be used:

- on parent request;
- in a controlled alternate playback mode;
- in future experimental scheduling.

Do **not** automatically replace every canonical playback with family audio in Prototype 0.1.

---

# 26. Why Canonical Remains Default

Canonical audio provides:

- reviewed pronunciation;
- consistent recording quality;
- stable family-test conditions.

Family voice provides:

- familiarity;
- home-register connection;
- emotional continuity.

They serve related but distinct roles.

---

# 27. Future Family/Canonical Alternation

A later version may test:

```text
canonical exposure
→ family voice re-encounter
```

or:

```text
family opener
→ canonical model
```

Prototype 0.1 should not complicate the initial family test unless family voice itself is one of the behaviors being validated.

---

# 28. AudioService Contract

As defined in `api.md`, playback should occur through:

```text
AudioService
```

High-level code should request:

```text
playCanonicalWord(...)
playPrompt(...)
playFamilyRecording(...)
stop(...)
stopAll()
```

The domain layer must not depend on platform player objects.

---

# 29. Playback Identity

Every child target-word playback should be associated with:

```text
sessionId
interactionId
pairId
playbackHandle
```

This is essential for stale-callback rejection.

---

# 30. Playback States

Platform implementation may use:

```text
IDLE
LOADING
PLAYING
COMPLETED
INTERRUPTED
FAILED
```

These are service states.

The child interaction state machine remains authoritative for UX.

---

# 31. Tap-to-Audio Latency

From `non-functional-requirements.md`:

> target canonical playback start normally within approximately **250 ms** after tap on supported local devices.

Tap acknowledgement should occur sooner:

> normally within approximately **100 ms**.

The UI can acknowledge the tap immediately while audio begins.

---

# 32. Preloading

Because Prototype 0.1 has only 16 canonical word clips, the app should favor simple local preloading/caching strategies.

Possible approach:

- resolve current asset before `READY`;
- optionally prepare next one or two likely clips;
- keep memory usage modest.

Do not build a complex streaming cache.

---

# 33. No Network Streaming

All child-session audio is local.

There is no:

- streaming URL;
- buffering over network;
- CDN;
- download-on-first-use.

---

# 34. Audio Exposure Semantics

A critical distinction:

```text
playback started
```

does not automatically equal:

```text
valid exposure
```

A valid exposure means:

> enough of the canonical target spoken form was actually presented for the child to meaningfully hear the word.

---

# 35. Exposure Threshold

`AudioService` should emit:

```text
ExposureThresholdReached
```

once per canonical scheduled playback attempt.

The threshold should be defined from the actual clip duration.

Recommended Prototype 0.1 rule:

> exposure becomes valid when either the clip completes, or at least approximately **80% of the target-word clip** has played without failure/interruption.

This is an engineering heuristic, not a learning-science threshold.

---

# 36. Very Short Clips

If a word clip is extremely short, percentage-only thresholds may behave poorly.

Implementation may apply:

```text
minimum meaningful playback duration
```

in addition to percentage.

The final rule should be validated against actual 16 clips.

---

# 37. Exposure Threshold Constraints

The threshold must not:

- fire at playback start;
- fire twice;
- fire after playback was already invalidated by failure;
- duplicate after orientation recreation.

---

# 38. Exposure Commit Boundary

Flow:

```text
ConceptTapped
→ AudioService starts
→ playback progresses
→ ExposureThresholdReached
→ LearningRepository.commitExposure(interactionId)
```

The repository call remains idempotent.

---

# 39. Playback Completes Before Threshold

This should not occur if threshold logic is correct.

If it does because of a platform edge case:

> completed canonical word playback counts as valid exposure.

---

# 40. Playback Fails Before Threshold

If playback fails before the target word was meaningfully presented:

```text
exposure_committed = false
```

The system may:

- retry once;
- skip;
- recover.

Do not record a false exposure.

---

# 41. Playback Interrupted After Threshold

If exposure threshold was already reached:

> exposure remains valid.

The later interruption affects interaction completion, not the fact that the word was already heard.

---

# 42. Replay Semantics

A child-initiated replay:

- replays the same target word;
- increments replay evidence;
- does not create a new scheduled interaction.

---

# 43. Replay Exposure Accounting

Prototype 0.1 should not increment:

```text
scheduled exposure count
```

for replays.

However replay itself is preserved as evidence:

```text
replay_count += 1
```

---

# 44. Replay During READY

If the current concept is `READY` and has already been heard within the current scheduled interaction, another tap may be treated as replay according to state-machine design.

Implementation must distinguish:

```text
first scheduled playback
```

from:

```text
subsequent replay
```

---

# 45. Tap During Active Playback

Recommended Prototype 0.1 behavior:

> debounce/ignore additional taps until the current word clip completes.

Alternative:

- queue one replay.

Do not:

- start overlapping players;
- restart from zero on every tap.

---

# 46. No Overlapping Child Speech Audio

At most one child-facing spoken audio source should normally play at once.

This includes:

- target word;
- prompt;
- family voice;
- acknowledgement speech.

---

# 47. Playback Priority

Recommended priority:

```text
1. active canonical target word
2. active family word playback when explicitly selected
3. short prompt
4. non-verbal acknowledgement
```

A higher-priority new child learning event should stop lower-priority playback where safe.

---

# 48. Audio Focus

The Android implementation should participate correctly in platform audio focus behavior.

Goals:

- avoid fighting with other media apps;
- respond safely to interruptions;
- pause/stop Miozira audio when another app/session takes focus as appropriate.

Exact platform API implementation belongs in code.

---

# 49. External Audio Interruption

Examples:

- incoming call;
- voice assistant;
- another media app;
- Bluetooth change.

Miozira should:

1. stop/pause safely;
2. preserve interaction identity;
3. avoid duplicate exposure;
4. return to a safe state.

---

# 50. Resume After Interruption

Do not blindly resume target speech mid-word after a long interruption.

Preferred:

- if interruption occurs before exposure threshold, return to a safe replay-ready state;
- if after threshold, interaction may continue or move on depending on UX state.

Do not create a second exposure automatically.

---

# 51. App Backgrounding

On app background:

- stop/pause child audio;
- do not keep playback running accidentally;
- persist committed evidence;
- retain safe logical session state.

---

# 52. Foreground Resume

On return:

- do not automatically replay solely because the app foregrounded;
- resume a safe state;
- let the child tap again if necessary.

---

# 53. Orientation Change

Rotation must not restart word audio automatically.

If the player survives recreation:

- continue normally.

If platform/UI recreation requires rebuilding presentation state:

- preserve logical playback state;
- do not duplicate the exposure event.

---

# 54. Process Death

If process dies during playback:

- previously committed exposure remains;
- uncommitted playback is abandoned;
- no fabricated completion event is written.

---

# 55. Audio Route Changes

Potential routes:

- built-in speaker;
- wired headphones;
- Bluetooth device;
- external audio device.

Route changes must not crash the session.

---

# 56. Bluetooth Latency

Bluetooth may add audible delay.

The app should not assume every playback route meets the same latency as built-in speakers.

Family-test measurements should preferably use the normal tablet speaker unless Bluetooth use is part of intended testing.

---

# 57. Volume

Miozira should respect system/media volume.

It should not programmatically force the device to maximum volume.

---

# 58. Quiet Device State

If volume is extremely low/muted, Miozira should not show child-facing errors.

Parent UI may eventually offer a simple volume hint if testing proves necessary.

Prototype 0.1 need not implement a volume checker unless it is a real usability issue.

---

# 59. Audio Accessibility

Audio must remain:

- clear;
- replayable;
- uncluttered by music;
- reasonably consistent in loudness.

This aligns with `accessibility.md`.

---

# 60. Tamil Audio Quality

Tamil canonical audio must be reviewed for:

- exact chosen lexical form;
- natural household pronunciation;
- clear articulation;
- appropriate register.

Final Tamil audio must not be recorded before disputed lexical choices are resolved.

---

# 61. English Audio Quality

English canonical audio must use one reviewed accent/variant policy.

The exact target accent is still open.

Do not mix noticeably different English pronunciation models across the 8 concepts without intent.

---

# 62. Speaker Consistency

Preferred Prototype 0.1:

> one primary canonical speaker per language.

Benefits:

- consistent tone;
- consistent pacing;
- consistent recording quality.

Multiple speakers are not needed for this vertical slice.

---

# 63. Child-Appropriate Delivery

Canonical speech should be:

- friendly;
- natural;
- clear;
- not theatrical;
- not babyish;
- not exaggerated into unnatural syllables.

---

# 64. Recording Environment

Preferred:

- quiet indoor room;
- low reflection;
- microphone at consistent distance;
- no fan/traffic/television noise.

A professional studio is not mandatory for the prototype if quality is clean.

---

# 65. Audio QC Checklist

Every canonical target clip:

- [ ] contains correct word;
- [ ] correct language;
- [ ] approved lexical form;
- [ ] no clipping;
- [ ] no background music;
- [ ] no competing speech;
- [ ] no obvious room noise;
- [ ] leading/trailing silence reasonable;
- [ ] loudness consistent;
- [ ] pronunciation reviewed;
- [ ] asset ID matches manifest;
- [ ] file decodes on target Android device.

---

# 66. Prompt QC Checklist

Every prompt:

- [ ] short;
- [ ] natural;
- [ ] non-judgmental;
- [ ] not louder than canonical word;
- [ ] does not overlap target word;
- [ ] language reviewed;
- [ ] not required for child progress.

---

# 67. Family Recording QC

Family recordings are not editorially approved.

The parent UI should encourage:

- one word;
- natural voice;
- quiet environment;
- short duration.

The app may reject or warn about:

- zero-length recording;
- unreadable/corrupt output.

It should not grade parent pronunciation.

---

# 68. Family Recording Duration

For one target word, recommended maximum:

> approximately **5 seconds**

This is intentionally generous.

Most recordings should be much shorter.

Longer limits add little value and increase accidental recording risk.

---

# 69. Family Recording File Lifecycle

Flow:

```text
start
→ temporary private file
→ stop
→ preview
→ save or cancel
```

On save:

```text
promote file
→ persist metadata
```

On cancel:

```text
delete temporary file
```

---

# 70. Family Recording Replacement

When replacing:

1. record new temp file;
2. preview;
3. save successfully;
4. update metadata;
5. delete old active file.

Do not delete the existing good recording before the replacement is safely saved.

---

# 71. Family Recording Deletion

Delete:

- active private file;
- active metadata record.

Canonical bundled audio remains untouched.

---

# 72. File Corruption

If family audio is corrupt:

- canonical audio remains available;
- parent may re-record;
- child flow does not break.

---

# 73. Canonical Asset Failure

If a required canonical audio file is unavailable or corrupt:

- treat as content/build defect;
- do not silently use family audio as canonical evidence;
- skip the pair in child mode if necessary;
- surface diagnostics to parent/developer.

Canonical audio is required content.

---

# 74. Pre-Session Audio Validation

Development/family-test builds should validate that all 16 required canonical word assets:

- exist;
- can be resolved;
- have non-zero duration;
- are decodable.

This can occur during content validation rather than every session.

---

# 75. Audio Cache

A small in-memory/prepared cache may be used.

Recommended scope:

- current clip;
- likely next clip;
- reusable short prompts.

Avoid caching entire decoded audio library if unnecessary.

---

# 76. Memory Pressure

If the OS reclaims audio resources:

- recreate them lazily;
- do not invalidate learning data;
- maintain logical asset IDs.

---

# 77. Decoder Reuse

Implementation may reuse playback objects where safe.

Correctness and lifecycle clarity are more important than micro-optimizing 16 tiny clips.

---

# 78. Audio Threading

Decoding/file operations must not block the UI rendering thread.

Callbacks must be marshaled safely into the session controller/state system.

---

# 79. Stale Callback Protection

Every callback must be checked against:

```text
active sessionId
active interactionId
active playbackHandle
expected interaction state
```

If stale:

> ignore it.

---

# 80. Example — Normal Playback

```text
READY
→ child taps
→ PLAYING_WORD
→ audio starts
→ exposure threshold reached
→ exposure committed
→ audio completes
→ INVITING
```

---

# 81. Example — Playback Failure Before Exposure

```text
READY
→ tap
→ PLAYING_WORD
→ audio starts/fails immediately
→ no exposure commit
→ safe retry or recovery
```

---

# 82. Example — Background After Exposure

```text
PLAYING_WORD
→ exposure threshold reached
→ exposure committed
→ app backgrounds
→ audio stops
→ session safely pauses/ends
```

Exposure remains valid.

---

# 83. Example — Rapid Repeated Taps

```text
tap
tap
tap
```

Expected:

- one active playback;
- no overlap;
- one scheduled exposure;
- optional replay only after active playback resolves.

---

# 84. Example — Rotation During Word

Expected:

- same interaction;
- same playback handle if possible;
- no restart;
- no duplicate exposure.

---

# 85. Example — Family Playback

Parent or future child flow requests:

```text
playFamilyRecording(pair.apple.ta)
```

Expected:

- local private file;
- no change to canonical asset;
- no scheduled canonical exposure unless product logic explicitly defines otherwise.

---

# 86. Canonical Exposure vs Family Playback Evidence

Prototype 0.1 should treat:

```text
canonical scheduled word playback
```

as the standard exposure event.

Family playback should not silently increment the same exposure count unless future learning-model rules explicitly define it.

This keeps family-test evidence interpretable.

---

# 87. Audio Error Types

Recommended semantic errors:

```text
AssetNotFound
DecodeFailed
PlaybackStartFailed
PlaybackInterrupted
AudioRouteUnavailable
FamilyRecordingMissing
FamilyRecordingCorrupt
UnexpectedAudioError
```

---

# 88. Child Error Behavior

Child mode should not show:

```text
"Audio decode error"
```

Instead:

- fallback;
- retry once where safe;
- skip item;
- end session gracefully if required.

---

# 89. Parent Error Behavior

Example:

> “The standard word recording couldn't be played. You can try again.”

If family recording fails:

> “The family recording couldn't be played. The standard voice is still available.”

---

# 90. Logging

Development logs may record:

```text
playback requested
asset ID
playback started
exposure threshold emitted
completed
interrupted
failed
```

Do not log:

- family audio bytes;
- child microphone PCM;
- unnecessary filesystem paths.

---

# 91. Audio Metrics for Technical Testing

Useful local development measurements:

```text
tap_to_playback_start_ms
playback_duration_ms
exposure_threshold_ms
playback_failure_count
route_change_count
```

These are engineering diagnostics, not child analytics.

---

# 92. Audio Performance Targets

Prototype starting targets:

```text
tap visual acknowledgement: ≤100 ms
canonical audio start:      ≤250 ms normally
no overlapping word audio:  100% requirement
```

Real-device tests determine whether these are met consistently.

---

# 93. Bundle Size

With only 16 canonical clips and a minimal prompt set, audio should remain a small part of the app bundle.

Do not sacrifice speech quality for premature bundle-size optimization.

---

# 94. Offline Guarantee

Canonical audio and required prompt audio must ship with the app.

Family recordings are created locally.

No audio path in Prototype 0.1 requires internet.

---

# 95. Audio Security / Privacy

Canonical assets:

- bundled app content.

Family recordings:

- app-private user-generated files.

Child attempt audio:

- handled separately by `microphone.md`;
- transient only;
- not saved.

---

# 96. Audio Test Matrix

At minimum test:

1. all 16 canonical clips;
2. first playback after cold launch;
3. repeated replay;
4. rapid tapping;
5. portrait/landscape rotation mid-playback;
6. background mid-playback;
7. process kill after exposure commit;
8. volume low/high;
9. built-in speaker;
10. Bluetooth route if available;
11. missing/corrupt family file;
12. canonical asset resolution failure in test build;
13. parent family record/play/delete;
14. reduced-motion mode to ensure audio remains unaffected;
15. microphone flow begins only after playback ends.

---

# 97. Automated Audio Tests

Automated tests should focus on service/state contracts rather than acoustic quality.

Use `FakeAudioService` to assert:

- one threshold callback;
- one completion callback;
- stale callback ignored;
- replay separated;
- failure path does not commit exposure.

Acoustic quality remains a human/device QA task.

---

# 98. Manual Listening Review

Before the family test, at least one fluent speaker should review every canonical clip.

For Tamil, review should include natural household usage.

For English, review should include consistency of accent and clarity.

---

# 99. Audio Invariants

### AUDIO-INV-001
Every active pair has one canonical target-word audio asset.

### AUDIO-INV-002
Canonical target-word audio contains only the target spoken form unless explicitly documented otherwise.

### AUDIO-INV-003
No background music plays under canonical target audio.

### AUDIO-INV-004
Only one child-facing spoken audio source plays at a time.

### AUDIO-INV-005
Playback start does not automatically equal valid exposure.

### AUDIO-INV-006
Exposure threshold emits at most once per scheduled playback.

### AUDIO-INV-007
Exposure commit remains idempotent by interaction.

### AUDIO-INV-008
Replay does not create a new scheduled interaction.

### AUDIO-INV-009
Rotation does not automatically restart playback.

### AUDIO-INV-010
App foregrounding does not automatically replay a word.

### AUDIO-INV-011
Family audio never deletes canonical audio.

### AUDIO-INV-012
Family playback does not silently count as canonical scheduled exposure.

### AUDIO-INV-013
All required canonical audio is local.

### AUDIO-INV-014
Audio failures never expose raw technical errors to the child.

### AUDIO-INV-015
Target speech never overlaps the child microphone response window by design.

---

# 100. Open Audio Decisions

Before asset production/build completion, confirm:

1. final runtime codec/container;
2. standard sample rate;
3. canonical speaker for English;
4. canonical speaker for Tamil;
5. English accent/variant;
6. final Tamil lexical choices;
7. exact loudness normalization workflow;
8. exact exposure-threshold implementation;
9. exact Android playback backend;
10. whether family voice appears in the child flow during Prototype 0.1.

These are deliberately explicit rather than hidden assumptions.

---

# 101. Relationship to `content-spec.md`

`content-spec.md` defines:

- what each audio asset means;
- language forms;
- review state.

This document defines playback and technical handling.

---

# 102. Relationship to `interaction-states.md`

Audio events drive transitions such as:

```text
READY
→ PLAYING_WORD
→ INVITING
```

but may only do so if the callback belongs to the active interaction.

---

# 103. Relationship to `database.md`

The database stores:

- exposure commit;
- replay count;
- interaction identity.

It does not store canonical audio bytes or raw child attempt audio.

---

# 104. Relationship to `api.md`

This document implements the semantics behind:

```text
AudioService
FamilyRecordingService
```

---

# 105. Relationship to `microphone.md`

`microphone.md` starts where target-word playback ends.

The required sequencing is:

```text
word playback
→ short pause/invite
→ child microphone window
```

This separation reduces false detection of Miozira's own audio.

---

# 106. Decision Summary

Prototype 0.1 audio is intentionally simple and controlled:

1. one reviewed canonical clip per concept-language pair;
2. short local speech assets;
3. no network streaming;
4. no background music;
5. no overlapping child-facing speech;
6. canonical playback begins quickly after tap;
7. valid exposure is committed only after meaningful playback;
8. replay is tracked separately;
9. audio interruptions preserve interaction identity;
10. rotation/foregrounding never creates automatic replay;
11. family recordings remain optional and local;
12. canonical audio remains the stable learning model;
13. Tamil and English recordings receive human review;
14. audio quality is prioritized over bundle-size micro-optimization.

The governing rule is:

> **Miozira should make the word easy to hear, easy to replay, and difficult for the software to miscount.**
