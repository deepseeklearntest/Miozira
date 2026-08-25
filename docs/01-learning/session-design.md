# Miozira — Session Design

**Document:** `session-design.md`  
**Version:** 0.1  
**Status:** Draft for Prototype 0.1  
**Product:** Miozira  
**Prototype:** 8 concepts × English + Tamil  
**Target child:** approximately 4–5 years old  
**Last updated:** 2026-08-25

---

## 1. Purpose

This document defines the structure and pacing of a Miozira Prototype 0.1 learning session.

It describes **how an item is experienced**.

The adaptive engine decides:

> what concept-language pair comes next.

This document decides:

> how that pair is presented to the child.

It covers:

- session entry;
- opening item;
- concept presentation;
- tap interaction;
- word playback;
- imitation invitation;
- microphone timing;
- child response handling;
- replay;
- transitions;
- movement opportunities;
- session ending;
- parent handoff;
- early exit;
- interruption recovery.

---

# 2. Session Goal

A Miozira session should feel like a calm sequence of small spoken-language encounters.

It should not feel like:

- a lesson to finish;
- a quiz;
- a test;
- a game level;
- a performance evaluation;
- a reward loop.

The intended child experience is:

> **See → Tap → Hear → Try if wanted → Receive a warm response → Move on → Meet it again later**

---

# 3. Typical Session Length

The design target is:

> approximately **5–10 minutes**

This is not a requirement shown to the child.

A valid session may be:

- 2 minutes;
- 4 minutes;
- 7 minutes;
- 10 minutes;
- longer on an unusually engaged day.

The child may stop at any time.

---

# 4. Target Interaction Count

A typical session may contain approximately:

> **8–12 scheduled concept interactions**

This count excludes:

- voluntary audio replays;
- parent-area activity;
- accidental touches;
- unshown planned items.

The session should end early if the child disengages.

---

# 5. Session Phases

Prototype 0.1 uses five broad phases:

```text
1. Enter
2. Warm start
3. Core learning loop
4. Gentle ending
5. Parent handoff
```

These phases are internal structure.

The child should not see labels such as “Phase 2” or “Lesson complete.”

---

# 6. Phase 1 — Enter

## SD-001 — Immediate child context

When child mode opens, Miozira should reach a learning-ready state quickly.

Avoid:

- splash animations longer than necessary;
- loading bars;
- menu choices;
- text-heavy onboarding;
- lesson selection.

---

## SD-002 — Resume or start cleanly

If a previous session ended normally:

- start a new session.

If a previous session was interrupted:

- recover into a valid state;
- do not repeat a learning event merely because the app restarted;
- do not force the child to continue the old session.

Prototype 0.1 may choose to start a new session after process death while preserving prior evidence.

---

# 7. Phase 2 — Warm Start

## SD-003 — Familiar opener

When learning history exists, the first scheduled item should normally be:

- familiar;
- previously positive;
- or otherwise easy.

If no history exists, use a simple familiar real-world concept.

Examples suitable for first-ever use:

- ball;
- apple;
- car.

---

## SD-004 — No verbal explanation required

The opener should teach the interaction through design.

Preferred pattern:

1. concept appears;
2. subtle visual cue suggests tap;
3. child taps;
4. word plays.

Avoid an adult-style tutorial such as:

> “Tap the image to hear the Tamil translation and then repeat after the speaker.”

---

# 8. Item Interaction State Machine

Each scheduled concept interaction should follow a simple state machine.

```text
PRESENT
  ↓
READY_FOR_TAP
  ↓
WORD_PLAYING
  ↓
IMITATION_INVITE
  ↓
LISTENING
  ↓
RESPONSE
  ↓
TRANSITION
```

Some states may be skipped depending on:

- microphone availability;
- child replay;
- early exit;
- audio interruption.

---

# 9. PRESENT State

## SD-005 — One dominant concept

The active concept visual appears as the dominant screen element.

The screen should contain minimal competing content.

---

## SD-006 — Stable presentation

The visual should remain still long enough for the child to recognize it before interaction.

A small entry animation is allowed if:

- it is brief;
- it does not distract;
- it does not loop indefinitely.

---

# 10. READY_FOR_TAP State

## SD-007 — Primary interaction is obvious

The concept itself is the main tap target.

A child should not have to locate a tiny play button.

---

## SD-008 — Optional subtle prompt

If no interaction occurs after approximately:

> **3–5 seconds**

Miozira may provide a subtle cue.

Examples:

- small pulse;
- gentle scale change;
- brief audio cue;
- animated tap hint.

The cue should not be loud or urgent.

---

## SD-009 — Repeat inactivity prompt sparingly

If the child remains inactive:

- do not escalate rapidly;
- do not repeatedly nag;
- do not create a countdown.

After a small number of prompts, Miozira should allow the session to idle or end naturally.

---

# 11. WORD_PLAYING State

## SD-010 — Tap triggers target word

A valid tap starts the canonical spoken form for the active language.

---

## SD-011 — One clear spoken model

The default word playback should be concise.

For example:

> “Apple.”

rather than:

> “This is an apple. Apple. Can you say apple?”

Longer contextual phrases are deferred unless specifically introduced in a later content stage.

---

## SD-012 — Visual reinforcement during audio

While the word plays, Miozira may use a minimal visual response such as:

- slight scale pulse;
- soft highlight;
- gentle motion.

The visual should support the word rather than compete with it.

---

## SD-013 — No overlapping word playback

Additional taps during word playback should not create overlapping speech.

Possible handling:

- ignore;
- queue one replay;
- restart only after current audio finishes.

Exact implementation belongs in `audio.md`.

---

# 12. IMITATION_INVITE State

## SD-014 — Brief invitation

After the spoken word, Miozira may invite the child to try saying it.

Preferred examples:

- “Your turn.”
- “You can say it.”
- a short non-verbal cue plus microphone indicator.

Avoid:

- “Say it correctly.”
- “Repeat after me now.”
- “Your answer?”
- “Try again.”

---

## SD-015 — Invitation not mandatory every time

The app does not need to issue a spoken imitation prompt after every single exposure.

Repeated prompts can become mechanical.

Prototype 0.1 may vary between:

- explicit invitation;
- short pause;
- visual speech cue.

---

# 13. LISTENING State

## SD-016 — Short listening window

When microphone support is enabled, the listening window should be brief.

Recommended initial duration:

> approximately **2–3 seconds**

The exact value belongs in `microphone.md`.

---

## SD-017 — Visible listening cue

The child should receive a simple indication that Miozira is listening.

Examples:

- calm expanding ring;
- small microphone animation;
- soft visual pulse.

Avoid:

- waveform analysis UI;
- recording timer;
- score meter;
- red/green pronunciation indicator.

---

## SD-018 — No continuous microphone

The microphone should not remain active through the whole session.

Listening begins only around the intended speaking opportunity.

---

## SD-019 — No speech is acceptable

If no speech-like activity is detected:

- the session continues;
- the child is not corrected;
- the app may model the word again or move on.

---

# 14. RESPONSE State

The response must be based on participation, not correctness.

---

## SD-020 — Attempt detected response

If speech-like activity is detected, use a warm acknowledgement.

Examples:

- “Mm-hm.”
- “Nice.”
- “There it is.”
- short happy tone;
- subtle visual animation.

Avoid phrases that imply verified correctness unless future technology supports it.

Do not say:

- “Perfect pronunciation!”
- “Correct!”
- “You got it right!”

---

## SD-021 — No-attempt response

If no attempt is detected:

Possible response:

- replay the canonical word once;
- brief neutral animation;
- continue.

Do not say:

- “Try again.”
- “You didn't answer.”
- “Wrong.”
- “Say it.”

---

## SD-022 — Microphone unavailable response

If microphone access is unavailable, the child should experience essentially the same flow without the listening-dependent acknowledgement.

The child should not see permission or technical warnings.

---

# 15. Replay Behavior

## SD-023 — Child replay allowed

The child can tap the concept again to hear the word again.

---

## SD-024 — Replay does not reset the item

Replay should keep the child on the same concept rather than restart the entire interaction flow.

---

## SD-025 — Replay count does not limit child

Prototype 0.1 should not impose a visible replay cap.

If excessive rapid tapping becomes disruptive, audio playback may debounce taps technically.

---

# 16. TRANSITION State

## SD-026 — Calm transition

After the response, move to the next item with a brief, predictable transition.

Recommended duration:

> approximately **300–700 ms**

This is a design target.

---

## SD-027 — No reward explosion

Transitions shall not include:

- confetti;
- coin showers;
- fireworks;
- loud applause;
- progress bars filling;
- level-up effects.

---

## SD-028 — Next item arrives automatically

The child should not need to tap “Next.”

---

# 17. Same-Session Repetition

When the adaptive engine schedules a concept-language pair again later:

- present it as a normal encounter;
- do not say “Let's practice this one again because you missed it.”

Repeated items should feel natural, not remedial.

---

# 18. Language Switching Experience

Prototype 0.1 uses structured English/Tamil switching.

---

## SD-029 — Language blocks

The child should normally experience short runs of one language before switching.

Example:

```text
English
English
Tamil
Tamil
Tamil
English
```

Exact scheduling belongs in `adaptive-engine.md`.

---

## SD-030 — Language switch cue

When changing language, Miozira may use a subtle cue so the change feels intentional.

Possible cues:

- small language-specific parent-configured indicator;
- distinct neutral transition sound;
- brief spoken language name in parent testing.

The child should not need to read a language label.

---

## SD-031 — Avoid translation-pair rhythm

The normal experience should not become:

```text
apple English
apple Tamil
ball English
ball Tamil
```

unless intentionally testing a comparison mode.

---

# 19. Movement Interactions

Miozira's long-term design should include physical/action-based language.

Prototype 0.1 may include very small movement prompts using existing concepts.

Examples:

- “Touch your nose.”
- “Show your hand.”
- “Find the ball.”

---

## SD-032 — Movement item optional

A normal session may contain:

> **0–1 movement interaction**

during Prototype 0.1.

Movement is supporting behavior, not required for every session.

---

## SD-033 — Movement is not scored

The child is not graded on the movement prompt.

If no response occurs:

- model;
- move on;
- do not mark failure.

---

# 20. Session Pacing

## SD-034 — No rigid fixed pace

Miozira should adapt naturally to:

- replay;
- child hesitation;
- speaking attempts;
- inactivity.

---

## SD-035 — Intentional pauses

Short pauses are acceptable and useful.

The product should not fill every second with:

- music;
- speech;
- animation.

---

## SD-036 — Avoid rapid-fire vocabulary

Do not advance so quickly that the child lacks time to process the spoken form.

---

# 21. Inactivity Handling

## SD-037 — First inactivity

After a few seconds:

- subtle prompt.

## SD-038 — Continued inactivity

After further inactivity:

- optional second gentle cue.

## SD-039 — Sustained inactivity

If the child remains inactive:

- allow session to idle;
- optionally end session after a reasonable quiet period;
- do not trigger repeated spoken prompts.

Exact idle timeout may be defined in implementation.

---

# 22. Session Ending

A session should end naturally rather than through a performance-completion event.

---

## SD-040 — Natural ending

Possible triggers:

- planned interaction range completed;
- child stops interacting;
- parent ends session;
- sustained inactivity;
- app closes.

---

## SD-041 — No “lesson complete” pressure

Avoid:

> “Congratulations! You completed today's lesson.”

Possible gentle ending:

- favorite/familiar concept;
- calm closing sound;
- simple visual fade.

---

## SD-042 — Child may immediately leave

No mandatory summary screen should trap the child.

---

# 23. Gentle Ending Phase

When the session reaches a planned end:

1. choose a familiar/easy item where possible;
2. complete normal interaction;
3. use a calm transition;
4. exit child learning mode or show a neutral resting screen;
5. make the parent handoff available.

---

# 24. Parent Handoff

After a meaningfully active session, Miozira should provide the parent with:

1. one short real-world suggestion;
2. access to mark recognition/use later;
3. optional lightweight recent-session evidence.

This information belongs in the parent area, not in the child's path.

---

## SD-043 — Real-world suggestion timing

The suggestion should appear:

- after the child session ends;
- in parent context;
- not as a child quiz.

---

## SD-044 — Suggestion format

Preferred structure:

```text
Context + natural phrase/action
```

Example:

> “At snack time, say the Tamil word for apple while handing over a slice.”

---

# 25. Early Exit

## SD-045 — Early exit is valid

A session ending after only a few items is valid.

---

## SD-046 — Persist completed learning evidence

Only completed meaningful interactions are recorded as exposures.

Planned but unseen items are ignored.

---

## SD-047 — No retry pressure

The app shall not immediately prompt the child to “finish the session.”

---

# 26. Parent-Initiated Exit

The parent may intentionally end a session.

This should:

- persist completed evidence;
- stop microphone activity;
- stop active audio;
- transition safely to parent mode or app home;
- avoid child-facing failure wording.

---

# 27. Orientation Change

## SD-048 — Preserve active item

Changing portrait ↔ landscape shall preserve:

- active concept;
- active language;
- session position;
- replay state where practical.

---

## SD-049 — No duplicate exposure on rotation

Orientation recreation shall not create a new exposure event.

---

## SD-050 — Audio handling during rotation

Rotation should not unintentionally restart word audio.

Exact behavior belongs in `audio.md` and architecture state management.

---

# 28. App Interruption

Examples:

- home button;
- incoming system overlay;
- app backgrounded;
- audio interruption.

---

## SD-051 — Short interruption

For a short interruption:

- preserve session state;
- resume safely;
- do not duplicate event logging.

---

## SD-052 — Long interruption

After a long interruption, the app may end the active session and begin fresh next time while preserving prior evidence.

Exact threshold can be implementation-defined.

---

# 29. Audio Interruption

If another app or system event interrupts audio:

- stop/pause cleanly;
- avoid simultaneous playback;
- resume only when safe;
- do not count an incomplete failed playback as a valid exposure unless enough of the word was actually presented.

---

# 30. Microphone Permission Flow

Microphone permission is adult-controlled.

Recommended flow:

1. parent reaches setup/parent area;
2. Miozira explains:
   - microphone only detects a speaking attempt;
   - no pronunciation grading;
   - no cloud processing;
   - no raw child speech stored;
3. parent grants or declines permission;
4. child session works either way.

The child should not be responsible for understanding the Android permission prompt.

---

# 31. First-Ever Session

The first session is special because the interaction pattern is not yet learned.

Recommended sequence:

```text
1. very familiar concept
2. clear tap cue
3. word playback
4. optional imitation cue
5. second familiar concept
6. repeat same interaction style
7. introduce language switch only after basic interaction is understood
```

---

## SD-053 — First-session simplification

The first session should prioritize learning **how Miozira works** over maximizing adaptive variety.

---

# 32. First Session Language Strategy

Recommended Prototype 0.1 default:

- begin in the language most familiar to the child;
- establish tap → hear behavior;
- introduce the second language after a few successful interactions.

For the first family test, this may mean starting in English or Tamil based on the family's chosen setup.

The exact default should be configurable.

---

# 33. Returning Sessions

Once the child understands the interaction:

- reduce instructional cues;
- use normal adaptive scheduling;
- include both languages;
- allow more silent imitation windows;
- maintain calm pacing.

---

# 34. Session-State Data

A session should minimally track:

```text
session_id
started_at
ended_at
current_item
current_language
planned_items
completed_interactions
new_pairs_introduced
recent_item_history
low_interaction_flag
microphone_available
```

Exact storage is deferred to `database.md`.

---

# 35. Interaction Result Data

Each scheduled interaction may return a result similar to:

```text
concept_language_pair
exposure_valid
replay_count
attempt_state
started_at
completed_at
ended_early
selection_reason
```

This result is passed to the adaptive engine.

---

# 36. Child-Facing Audio Vocabulary

Prototype 0.1 should keep child-directed spoken UI vocabulary extremely small.

Possible common prompts:

- “Your turn.”
- “Listen.”
- “Again?”
- “Let's see.”
- “Here we go.”

These require language-specific review before implementation.

Avoid long instructional speech.

---

# 37. Gentle Response Vocabulary

Possible acknowledgements:

- “Mm-hm.”
- “Nice.”
- “Yeah.”
- warm non-verbal tone.

The response should not imply accuracy judgment.

A future content review should determine which acknowledgements sound natural in English and Tamil.

---

# 38. Tamil Prompt Quality

Tamil child-directed prompts must be:

- natural household Tamil;
- short;
- age-appropriate;
- not overly formal;
- reviewed by fluent/native family speakers.

The product should not blindly translate English UI phrases word-for-word.

---

# 39. No Mandatory Speech

The session experience must remain complete even if the child never speaks.

A valid session may consist of:

- seeing;
- tapping;
- listening;
- replaying;
- moving;
- leaving.

Speaking is encouraged, not required.

---

# 40. No Mandatory Replay

The child is never required to replay audio.

Replay is available because children may want it.

---

# 41. No Mandatory Parent Presence During Session

After setup, the child should be able to operate the core interaction without a parent controlling each step.

However, Miozira is not intended to replace parent interaction outside the session.

---

# 42. Session Quality Invariants

### SD-INV-001
No item requires reading.

### SD-INV-002
No item requires pronunciation correctness.

### SD-INV-003
No item requires a score.

### SD-INV-004
Silence does not block progression.

### SD-INV-005
Replay remains child-controlled.

### SD-INV-006
No scheduled same-pair repetition occurs without adaptive spacing.

### SD-INV-007
No child-facing timer creates urgency.

### SD-INV-008
No uncompleted session is treated as failure.

### SD-INV-009
Microphone denial does not change access to learning content.

### SD-INV-010
Orientation changes do not create new learning evidence.

### SD-INV-011
A child may stop at any time.

### SD-INV-012
The parent suggestion is not a quiz.

---

# 43. Prototype Interaction Timing

Recommended initial timing values:

```text
concept entry settle          300–500 ms
tap acknowledgement          ≤100 ms target
word playback start          ≤250 ms target
post-word pause              300–600 ms
imitation invitation         brief
listening window             2–3 s
response                     500–1200 ms
transition                   300–700 ms
inactivity hint              after ~3–5 s
```

These are starting design values.

Real-device and family behavior should override assumptions.

---

# 44. Example Interaction — Microphone Enabled

```text
[Ball appears]

Child taps ball.

Audio:
"Ball."

[brief pause]

Audio/visual:
"Your turn."

[2–3 second listening window]

Child vocalizes.

Miozira:
warm acknowledgement

[brief transition]

Next concept appears.
```

No correctness judgment occurs.

---

# 45. Example Interaction — No Speech

```text
[Apple appears]

Child taps apple.

Audio:
Tamil word for apple

[brief invitation]

No speech detected.

Miozira:
replays the Tamil word once

[brief transition]

Next item appears.
```

No “try again.”

---

# 46. Example Interaction — Microphone Denied

```text
[Water appears]

Child taps water.

Audio:
Tamil word for water

[brief visual imitation cue]

[short natural pause]

Miozira:
gentle transition

Next item appears.
```

The interaction still makes sense.

---

# 47. Example Replay

```text
[Cat appears]

Child taps.

"Cat."

Child taps again.

"Cat."

Child listens.

Miozira continues normally after the interaction.
```

The second tap is recorded as replay evidence, not a second scheduled exposure.

---

# 48. Example Natural End

```text
[Car appears as familiar closer]

Child taps.

"Car."

Child smiles/replays.

Miozira gives calm ending transition.

Child mode rests.

Parent can later open parent area and sees:

"Try this outside Miozira:
During play, say the Tamil word for car when handing over a toy car."
```

---

# 49. Family-Test Observations

During the seven-day family test, observe:

- does the child understand tapping without explanation?
- how many prompts are needed?
- does the child wait through the imitation pause?
- is the listening window too long?
- does the microphone cue attract or distract?
- does replay happen naturally?
- do transitions feel too fast or slow?
- does the child tolerate language switches?
- does the child end sessions naturally?
- does the parent understand when the session has ended?
- does the real-world suggestion feel useful rather than burdensome?

---

# 50. Session Metrics for Validation

Prototype 0.1 may locally capture:

```text
session duration
scheduled interactions completed
early exit
average interaction duration
replay events
attempt/no-attempt
language distribution
concept distribution
selection reason
```

These are debugging/product-validation signals.

They are not child scores.

---

# 51. Session Design Must Not Optimize for Duration

A longer session is not inherently better.

Session design should optimize for:

- clarity;
- calmness;
- willingness;
- repetition;
- memory opportunity;
- transfer to family life.

Not:

- maximum minutes;
- maximum taps;
- maximum retention.

---

# 52. Relationship to `adaptive-engine.md`

The adaptive engine determines the next concept-language pair.

Session design must respect:

- new-pair cap;
- same-session repeat cap;
- spacing;
- rest;
- familiar opener;
- recovery items;
- language balance.

Session design shall not override those rules simply to fill time.

---

# 53. Relationship to `audio.md`

`audio.md` will define:

- file formats;
- audio focus;
- playback queueing;
- interruptions;
- loudness consistency;
- canonical/family source selection;
- timing behavior.

---

# 54. Relationship to `microphone.md`

`microphone.md` will define:

- Android permission handling;
- audio capture;
- transient processing;
- speech-like activity detection;
- noise thresholds;
- listening duration;
- no-storage guarantee;
- fallback behavior.

---

# 55. Relationship to `ui-ux.md`

`ui-ux.md` will translate this sequence into:

- screen layouts;
- interaction states;
- component sizes;
- animations;
- responsive portrait/landscape rules;
- parent gate;
- parent handoff screens.

---

# 56. Decision Summary

Prototype 0.1 session design is:

1. fast entry;
2. familiar/easy opener;
3. one dominant concept;
4. tap-to-hear;
5. short spoken model;
6. optional imitation invitation;
7. 2–3 second microphone window when enabled;
8. warm participation response;
9. no correctness judgment;
10. automatic calm transition;
11. adaptive next item;
12. optional 0–1 movement interaction;
13. familiar/easy ending;
14. no completion pressure;
15. one parent real-world suggestion after a meaningful session;
16. child may stop at any time.

The session should feel simple enough that a preschooler can understand the interaction pattern without repeated adult instruction.
