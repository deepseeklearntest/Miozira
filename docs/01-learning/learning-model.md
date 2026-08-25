# Miozira — Learning Model

**Document:** `learning-model.md`  
**Version:** 0.1  
**Status:** Draft for Prototype 0.1  
**Product:** Miozira  
**Prototype:** 8 concepts × English + Tamil  
**Target child:** approximately 4–5 years old  
**Last updated:** 2026-08-25

---

## 1. Purpose

This document defines the learning concepts and internal terminology used by Miozira Prototype 0.1.

It answers questions such as:

- What counts as an exposure?
- What counts as a speaking attempt?
- What does “familiar” mean?
- What makes an item “due”?
- When should an item rest?
- What is real-world recognition?
- What is real-world use?
- What does Miozira consider evidence of progress?
- What must Miozira **not** infer from the available evidence?

This document intentionally does **not** define the final scheduling formula.

That belongs in:

- `adaptive-engine.md`
- `session-design.md`

This document defines the meaning of the data those systems operate on.

---

# 2. Core Learning Unit

The fundamental learning unit in Miozira is the:

> **Concept-Language Pair**

A concept-language pair is one concept associated with one spoken form in one language.

Examples:

- apple + English
- apple + Tamil
- water + English
- water + Tamil

Prototype 0.1 therefore contains:

> **8 concepts × 2 languages = 16 concept-language pairs**

The learning model tracks each pair independently while also retaining the fact that both language forms refer to the same underlying concept.

---

# 3. Concept vs Word

Miozira distinguishes between:

## 3.1 Concept

The underlying meaning or referent.

Examples:

- an apple;
- a ball;
- water;
- a hand.

## 3.2 Spoken form

The way that concept is expressed in a particular language.

Example:

```text
Concept: water

English spoken form: "water"
Tamil spoken form: "தண்ணீர்"
```

The concept is shared.

The spoken forms are language-specific.

---

# 4. Why Concept-First Matters

Miozira should build:

> **meaning ↔ spoken form**

rather than primarily building:

> **English word ↔ Tamil translation**

This allows the child to learn that two different spoken forms can independently refer to the same real-world concept.

Therefore, Miozira's internal model must not treat English as the universal source language.

---

# 5. Learning Evidence vs Learning Truth

Miozira does not directly know what a child “knows.”

It observes limited evidence.

Examples of evidence include:

- the child saw and heard a concept;
- the child replayed the audio;
- speech-like activity occurred;
- the parent reported real-world recognition;
- the parent reported real-world use.

These signals do not prove mastery.

The product must therefore distinguish:

> **observed evidence**

from:

> **inferred learning state**

and from:

> **actual child ability**

Only the first two exist inside Miozira.

Actual language ability remains broader than the app can measure.

---

# 6. Evidence Hierarchy

Prototype 0.1 should interpret evidence approximately in this order:

### Strongest product evidence

1. Parent reports spontaneous real-world use.
2. Parent reports real-world recognition.
3. Child demonstrates recall or recognition inside a later session.
4. Child voluntarily produces a speaking attempt.
5. Child voluntarily replays the spoken form.
6. Child receives an exposure.

The hierarchy is conceptual.

It is **not** a numerical scoring formula.

---

# 7. Exposure

## LM-001 — Exposure

An **exposure** occurs when the child has a reasonable opportunity to associate a concept with its spoken form.

A valid exposure normally requires:

1. the concept is visibly presented;
2. the correct target-language audio is played sufficiently;
3. the interaction remains active long enough to make exposure plausible.

A screen being rendered for a few milliseconds does not automatically count.

---

## 7.1 Exposure examples

Counts as exposure:

- apple image shown;
- Tamil word audio plays fully or meaningfully;
- child remains on the item.

May not count:

- screen immediately disappears;
- audio fails to start;
- app crashes before meaningful presentation;
- orientation recreation displays the same screen without a new learning event.

---

## 7.2 Exposure is not mastery

Ten exposures do not mean the child knows the word.

Exposure count is only:

> evidence of how often Miozira has presented the pair.

---

# 8. Replay

## LM-002 — Replay

A **replay** is a child-initiated request to hear the same active spoken form again during the same learning interaction.

Replay is useful because it may indicate:

- interest;
- uncertainty;
- curiosity;
- enjoyment;
- desire to imitate;
- accidental repeated tapping.

Therefore replay count should not be interpreted as either:

> “struggling”

or:

> “mastered.”

It is contextual evidence only.

---

# 9. Speaking Attempt

## LM-003 — Speaking Attempt

A **speaking attempt** means Miozira detected speech-like vocal activity during the intended child-response window.

It does not mean:

- the correct word was spoken;
- the language was correct;
- pronunciation was accurate;
- the word was intelligible;
- the child recalled the word independently.

Prototype 0.1 treats speaking attempts as participation evidence.

---

## 9.1 Attempt states

Prototype 0.1 may internally represent:

```text
ATTEMPT_DETECTED
NO_ATTEMPT_DETECTED
MICROPHONE_UNAVAILABLE
NOT_MEASURED
```

These states must not be converted into child-facing grades.

---

# 10. Voluntary Interaction

## LM-004 — Voluntary Interaction

A child action is considered **voluntary interaction evidence** when the child initiates it without the app mechanically requiring that action to continue.

Examples:

- tapping the concept again to replay audio;
- attempting the word during the response window;
- independently starting another interaction;
- voluntarily returning to Miozira later.

Voluntary interaction is useful because Prototype 0.1 is testing whether the learning loop invites participation rather than merely tolerating forced completion.

---

# 11. New Pair

## LM-005 — New

A concept-language pair is **new** when Miozira has no meaningful prior exposure recorded for that pair.

Example:

The child has heard `apple + English` before but never `apple + Tamil`.

Then:

```text
apple + English = not new
apple + Tamil   = new
```

The concept itself may already be familiar in real life.

“New” refers only to Miozira history for that concept-language pair.

---

# 12. Seen Pair

## LM-006 — Seen

A pair becomes **seen** after at least one valid exposure has been stored.

Seen does not imply familiarity.

---

# 13. Familiar

## LM-007 — Familiar

A concept-language pair may be classified as **familiar** when Miozira has enough positive repeated evidence that it is appropriate to use the pair as a relatively easy item.

Possible evidence includes:

- multiple separated exposures;
- repeated speaking attempts;
- replay behavior;
- parent-reported recognition/use;
- successful later-session interaction.

The exact threshold belongs in `adaptive-engine.md`.

---

## 13.1 Familiar is not mastered

“Familiar” means:

> Miozira has reason to believe this pair is less novel than others.

It does **not** mean:

- fluent;
- mastered;
- correctly pronounced;
- permanently learned.

---

# 14. Emerging / Uncertain

## LM-008 — Emerging

An **emerging** concept-language pair has some learning evidence but not enough to treat as comfortably familiar.

Typical characteristics may include:

- a small number of exposures;
- inconsistent voluntary attempts;
- repeated replay;
- little or no real-world evidence;
- recent introduction.

---

## LM-009 — Uncertain

**Uncertain** is a scheduling interpretation that means:

> Miozira does not have enough evidence to confidently classify this pair as familiar, but the pair is not necessarily new.

Uncertain is not a child deficit label.

It is an internal planning state.

---

# 15. Due

## LM-010 — Due

A concept-language pair is **due** when the scheduling system determines that enough time or intervening learning has passed that another encounter is useful.

Due is based on:

- last exposure;
- current familiarity;
- prior evidence;
- spacing rules;
- temporary rest state.

Due does not mean overdue or late in a punitive sense.

There is no child-facing overdue condition.

---

# 16. Not Due

## LM-011 — Not Due

A pair is **not due** when Miozira has recently presented it enough that another immediate exposure would provide little additional value.

This is primarily a spacing control.

---

# 17. Resting

## LM-012 — Resting

A concept-language pair may enter **resting** state when repeated low-engagement encounters suggest that showing it again soon could become frustrating or unproductive.

Possible triggers may include:

- several encounters with no interaction;
- repeated immediate disengagement;
- repeated abandonment around the item;
- excessive recent exposure.

The exact rule belongs in `adaptive-engine.md`.

---

## 17.1 Rest is temporary

Resting does not mean:

- failed;
- removed;
- too difficult forever;
- child cannot learn it.

A resting pair is intentionally withheld for a period before reintroduction.

---

# 18. Reintroduced

## LM-013 — Reintroduced

A **reintroduced** pair is a previously resting or long-unseen concept-language pair brought back into a later session.

Where possible, reintroduction may vary:

- visual example;
- sequence position;
- context;
- family prompt;
- associated action.

Prototype 0.1 may implement only limited variation, but the learning model should support the idea.

---

# 19. Recognition

Miozira distinguishes between app-internal and real-world recognition.

---

## LM-014 — In-App Recognition Evidence

**In-app recognition evidence** means the child's behavior suggests a previously encountered spoken form or concept was recognized during Miozira use.

Prototype 0.1 may have limited ability to measure this directly.

Examples could include:

- correct concept selection in a future gentle recognition activity;
- anticipatory interaction with a familiar item;
- behavior observed by the parent during use.

Unless explicitly measured, Miozira must not claim recognition solely from exposure count.

---

# 20. Real-World Recognition

## LM-015 — Real-World Recognition

A parent may report **real-world recognition** when the child demonstrates understanding of a Miozira concept-language pair outside the application.

Examples:

- parent says the Tamil word for ball and the child points to the ball;
- child reacts correctly to the spoken word for water;
- child identifies the real object after hearing the word.

The evidence is parent-reported.

Miozira does not independently verify it.

---

# 21. Prompted Real-World Use

## LM-016 — Prompted Use

**Prompted real-world use** occurs when the child says or meaningfully uses the word outside Miozira after a parent/caregiver invitation.

Example:

Parent asks for or models the Tamil word and the child says it.

This is meaningful evidence, but weaker than spontaneous use because an immediate cue was provided.

Prototype 0.1 may simplify parent reporting and not expose this distinction in the UI, but the data model may preserve it if feasible.

---

# 22. Spontaneous Real-World Use

## LM-017 — Spontaneous Use

**Spontaneous real-world use** occurs when the child independently uses the learned spoken form in an appropriate real-world context without an immediate request to produce it.

Example:

The child sees an apple in the kitchen and independently says the Tamil word.

This is among the strongest learning signals available to Miozira Prototype 0.1.

---

# 23. Parent-Reported Evidence

## LM-018 — Parent Observation

A **parent observation** is an explicit adult-entered record describing behavior outside Miozira.

Prototype 0.1 should support at minimum:

```text
RECOGNIZED_OUTSIDE_APP
USED_OUTSIDE_APP
```

A future version may distinguish:

```text
USED_AFTER_PROMPT
USED_SPONTANEOUSLY
```

Parent reports should include:

- concept;
- language where known;
- observation type;
- timestamp.

Optional free-text context is deferred unless needed.

---

# 24. Why Parent Evidence Matters

The app cannot directly observe what happens:

- at breakfast;
- during play;
- in the car;
- during bath time;
- while shopping;
- with grandparents.

Yet those contexts are exactly where meaningful transfer can occur.

Therefore parent observation is not an accessory metric.

It is part of Miozira's core learning evidence model.

---

# 25. Recall

## LM-019 — Recall

**Recall** means producing or retrieving the spoken form from memory when the concept is present and the word has not just been played.

Prototype 0.1 should treat recall carefully because:

- a young child may know a word and choose not to speak;
- microphone detection does not know what was said;
- parent prompting may influence production.

Therefore Miozira 0.1 may schedule opportunities for recall but should not claim verified recall from voice activity alone.

---

# 26. Recognition vs Recall

These are distinct:

```text
Recognition:
"Where is the ball?"
Child identifies ball.

Recall:
[ball shown]
Child produces the word without hearing it first.
```

Recognition is generally easier than recall.

Miozira should not treat them as equivalent evidence.

---

# 27. Receptive vs Expressive Evidence

The learning model should preserve the distinction between:

### Receptive evidence

The child appears to understand the spoken form.

Examples:

- points;
- selects;
- responds correctly;
- parent reports recognition.

### Expressive evidence

The child produces or attempts the spoken form.

Examples:

- speaking attempt;
- prompted use;
- spontaneous use.

Prototype 0.1 will measure expressive evidence only weakly inside the app because speech recognition is intentionally absent.

---

# 28. Real-World Transfer

## LM-020 — Transfer

**Transfer** means evidence that learning associated with Miozira appears in a context different from the original app interaction.

Examples:

- different physical object of the same category;
- family routine;
- play;
- conversation;
- parent request;
- spontaneous production.

Transfer is central to Miozira's product hypothesis.

---

# 29. Learning Evidence Timeline

A concept-language pair may accumulate evidence like:

```text
New
 ↓
Exposed
 ↓
Seen
 ↓
Emerging
 ↓
Familiar
 ↓
Recognized outside app
 ↓
Used outside app
```

But this is **not** a mandatory ladder.

Real child learning may instead look like:

```text
Exposed
 ↓
Silent
 ↓
Exposed again
 ↓
Replay
 ↓
No attempt
 ↓
Parent reports spontaneous use
```

Miozira must support non-linear progress.

---

# 30. No Permanent Mastery State

## LM-021 — No Permanent Mastery

Prototype 0.1 shall not use a permanent terminal state called:

- mastered;
- completed;
- learned forever;
- finished.

Language knowledge can weaken, strengthen, or change with context.

Even highly familiar pairs may occasionally return.

---

# 31. No Failure State

## LM-022 — No Learning Failure

The learning model shall not assign a permanent failure state because of:

- silence;
- low engagement;
- mispronunciation;
- missed days;
- repeated replay;
- early session exit.

The response should be adaptation, spacing, rest, or reintroduction.

---

# 32. Progress

## LM-023 — Progress

Miozira defines progress as:

> **the accumulation of useful evidence that a concept-language association is becoming more familiar, retrievable, recognizable, or usable across time and contexts.**

Progress is multidimensional.

It should not collapse into one number.

---

# 33. Internal Progress Dimensions

Prototype 0.1 may internally reason about dimensions such as:

1. exposure history;
2. recency;
3. replay behavior;
4. speaking participation;
5. spacing history;
6. parent-reported recognition;
7. parent-reported use.

These may influence scheduling.

They should not be combined into a child-facing percentage.

---

# 34. Evidence Strength

For scheduling purposes only, evidence may be considered roughly:

### Weak evidence

- single exposure;
- immediate replay;
- single speaking attempt.

### Moderate evidence

- repeated separated exposures;
- repeated voluntary attempts;
- later-session interaction;
- parent-reported recognition.

### Strong evidence

- repeated real-world recognition;
- prompted real-world use;
- spontaneous real-world use.

This is conceptual guidance.

Exact weights are deferred.

---

# 35. Evidence Recency

Recent evidence and older evidence should both matter.

A pair used yesterday and a pair last seen three weeks ago should not necessarily receive identical scheduling treatment.

The adaptive engine may use recency while avoiding rigid “forgetting penalties.”

---

# 36. Interaction Neutrality

Miozira must not interpret every absence of interaction as lack of knowledge.

A child may not respond because of:

- distraction;
- tiredness;
- mood;
- microphone discomfort;
- interest elsewhere;
- already knowing the word;
- not wanting to perform.

Therefore:

> **No attempt ≠ does not know**

---

# 37. Replay Neutrality

Similarly:

> **Many replays ≠ struggling**

A child may replay because the sound is enjoyable.

Therefore replay is supporting evidence, not a direct difficulty score.

---

# 38. Parent Evidence Neutrality

Parent observations are valuable but subjective.

Potential biases include:

- remembering successful moments more than unsuccessful ones;
- uncertainty about which language form was used;
- interpreting imitation as spontaneous use.

Therefore the system should store parent evidence as:

> **reported observation**

not:

> **verified fact**

---

# 39. Same Concept Across Languages

Evidence for one language should not automatically equal evidence for the other.

Example:

```text
apple + English
parent reports real-world use

does NOT automatically mean

apple + Tamil
is familiar
```

However, concept familiarity may make the second language easier to introduce.

This cross-language relationship belongs in `adaptive-engine.md`.

---

# 40. Concept Familiarity vs Language Familiarity

Miozira should distinguish:

### Concept familiarity

The child clearly knows what an apple is.

### Language-pair familiarity

The child has evidence of knowing or recognizing the Tamil spoken form for apple.

Prototype 0.1 assumes the eight selected concepts are already familiar real-world concepts.

The prototype is testing new spoken associations, not teaching what an apple is.

---

# 41. Canonical Audio Evidence

Hearing canonical audio counts as exposure.

Hearing a family recording may also count as exposure to the same concept-language pair if the recording has been explicitly associated with that pair.

The database should preserve which audio source was used where useful.

---

# 42. Family Voice and Learning State

Family voice does not create a separate concept-language learning state by default.

Example:

```text
apple + Tamil
```

remains the learning pair whether played through:

- canonical Tamil audio;
- parent-recorded Tamil audio.

Audio source may be logged as context, not as a separate vocabulary item.

---

# 43. Session-Level Learning Evidence

A **session** is a container for interactions.

The child does not “pass” or “fail” a session.

A session may contain:

- zero speaking attempts;
- several replays;
- early exit;
- only familiar items.

It can still be a valid learning session.

---

# 44. Meaningfully Active Session

## LM-024 — Meaningfully Active Session

A session is **meaningfully active** when the child completes enough interaction to justify:

- storing a session record;
- updating learning history;
- offering a parent real-world suggestion.

The exact threshold should remain minimal.

A candidate rule may be:

> at least one valid concept exposure.

The final rule belongs in `session-design.md`.

---

# 45. Session Abandonment

## LM-025 — Abandoned Session

A session may be considered **abandoned** operationally if it starts but contains no meaningful learning interaction.

This state is useful for debugging and UX analysis.

It must not penalize the child or alter learning state strongly.

---

# 46. Engagement Evidence

Miozira may observe behavior such as:

- replay;
- voluntary continuation;
- session duration;
- return to app.

This should be called:

> **interaction or engagement evidence**

not:

> **learning evidence**

unless a clear learning interpretation exists.

The product must avoid optimizing engagement merely for longer screen time.

---

# 47. Session Duration Interpretation

Longer sessions are not automatically better.

A four-minute session with real-world transfer may be more valuable than a twelve-minute passive session.

Therefore session duration should be used primarily for:

- usability observation;
- fatigue detection;
- product validation.

Not as a progress score.

---

# 48. Missed Days

## LM-026 — Missed Days

Miozira shall not interpret missed days as failure.

No streak exists.

A gap may increase the scheduling priority of previously seen items because of elapsed time, but it shall not generate:

- punishment;
- reset;
- guilt;
- lost progress.

---

# 49. Difficult Encounter

## LM-027 — Difficult Encounter

Prototype 0.1 may internally identify a **difficult encounter** when several signals suggest the current interaction did not work well.

Possible signals:

- immediate disengagement;
- no interaction;
- repeated session exits around the same pair;
- repeated low-response encounters.

Difficulty is inferred cautiously.

One silent encounter is not enough.

---

# 50. Preferred Item

## LM-028 — Preferred Item

A concept-language pair may show signs of being preferred if the child repeatedly:

- replays it;
- selects it when choice exists;
- stays with it longer;
- shows positive voluntary interaction.

Preference is not mastery.

Preferred items may be useful as:

- session openers;
- session closers;
- recovery items after difficult interactions.

---

# 51. Language Preference

## LM-029 — Language Preference

Prototype 0.1 may observe a temporary preference for English or Tamil.

Signals could include:

- more voluntary interactions;
- more replay;
- more speaking attempts;
- parent observation.

Miozira should not permanently reduce the other language solely because of short-term preference.

---

# 52. Confidence Labels Are Internal Only

If the implementation uses labels such as:

```text
NEW
EMERGING
FAMILIAR
DUE
RESTING
```

these are internal scheduling labels.

They should not appear to the child as achievement levels.

The parent UI may expose simplified descriptions if they are truthful and useful.

---

# 53. Recommended Prototype State Model

A concept-language pair should have two separate kinds of state:

## 53.1 Descriptive evidence

Facts such as:

```text
exposure_count
last_exposed_at
replay_count
attempt_count
parent_recognition_count
parent_use_count
last_real_world_event_at
```

## 53.2 Derived scheduling state

Computed labels such as:

```text
NEW
EMERGING
FAMILIAR
RESTING
```

and scheduling fields such as:

```text
next_due_at
rest_until
```

This separation is important.

Derived states can be recalculated later without destroying raw evidence.

---

# 54. Evidence Should Be Append-Oriented

Whenever practical, important learning observations should be stored as events or auditable facts before being summarized.

Example:

```text
ExposureEvent
ReplayEvent
AttemptEvent
RealWorldObservation
```

Then summary fields may be derived:

```text
exposure_count = 5
attempt_count = 3
```

The final storage strategy belongs in `database.md`.

---

# 55. Learning Events Do Not Need Raw Media

Learning evidence should generally store metadata.

Example:

```text
event:
  concept = apple
  language = ta
  type = ATTEMPT_DETECTED
  timestamp = ...
```

It does not require storing the child's raw microphone recording.

---

# 56. Suggested Event Vocabulary

Prototype 0.1 should consider the following event types:

```text
SESSION_STARTED
SESSION_ENDED
CONCEPT_EXPOSED
AUDIO_REPLAYED
SPEECH_ATTEMPT_DETECTED
NO_SPEECH_ATTEMPT
REAL_WORLD_RECOGNITION_REPORTED
REAL_WORLD_USE_REPORTED
FAMILY_AUDIO_PLAYED
```

Not every event must become a separate database table.

The vocabulary establishes semantic consistency.

---

# 57. Learning Model Invariants

The following rules must always remain true.

### INV-001

No speaking attempt shall be interpreted as verified correct pronunciation.

### INV-002

No lack of speaking attempt shall be interpreted as verified lack of knowledge.

### INV-003

Exposure count shall never equal mastery.

### INV-004

Replay count shall never independently equal difficulty or mastery.

### INV-005

Real-world observations shall remain identified as parent-reported.

### INV-006

A concept-language pair shall never enter permanent failure state.

### INV-007

A concept-language pair shall never require a permanent mastery/completed state.

### INV-008

English evidence shall not automatically become Tamil evidence, or vice versa.

### INV-009

Resting shall always be temporary/reversible.

### INV-010

Child-facing feedback shall not expose derived learning confidence as a grade.

---

# 58. Prototype 0.1 Parent-Facing Vocabulary

To keep the parent experience understandable, the following wording is recommended.

Prefer:

- Heard recently
- Heard a few times
- Tried saying it
- Replayed it
- Recognized outside Miozira
- Used outside Miozira
- Coming back later

Avoid:

- Weak
- Failed
- Poor
- 35% mastery
- Incorrect
- Behind
- Low performer
- Not learned

---

# 59. Prototype Learning Goal

For Prototype 0.1, the learning model exists to support this progression:

> **hear → notice → voluntarily interact → encounter again → remember → recognize/use in real life**

It is not trying to model full language competence.

---

# 60. What Prototype 0.1 Cannot Measure Reliably

Miozira 0.1 must not claim reliable measurement of:

- pronunciation accuracy;
- phoneme accuracy;
- fluency;
- grammar;
- expressive vocabulary size;
- receptive vocabulary size;
- spontaneous speech frequency;
- language dominance;
- general language proficiency;
- long-term retention beyond observed evidence.

---

# 61. What Prototype 0.1 Can Measure Reliably

Miozira can measure or record:

- what it presented;
- when it presented it;
- what language was active;
- whether audio replay was requested;
- whether speech-like activity was detected;
- whether the parent reported real-world recognition;
- whether the parent reported real-world use;
- session timing;
- session continuity;
- local scheduling state.

---

# 62. Core Product Metric

Prototype 0.1 should not use a single numeric north-star metric.

The most meaningful signal is:

> **Did language encountered in Miozira later appear meaningfully outside Miozira?**

This may take the form of:

- recognition;
- prompted use;
- spontaneous use.

---

# 63. Prototype Evaluation Interpretation

A promising seven-day result may include several of the following:

- child independently understands tap-to-hear;
- voluntary replay occurs;
- voluntary speaking attempts occur;
- familiar pairs become easier to use over time;
- child returns without pressure;
- parent reports recognition outside Miozira;
- parent reports at least one use outside Miozira.

No single event proves efficacy.

The pattern matters.

---

# 64. Relationship to Adaptive Engine

`adaptive-engine.md` must use the definitions in this document.

It will determine:

- how evidence changes scheduling;
- when pairs become familiar;
- how due dates are calculated;
- when resting starts;
- how long rest lasts;
- how familiar/easy items are mixed with uncertain items;
- how English/Tamil interactions are balanced.

It may not redefine learning semantics without revising this document.

---

# 65. Relationship to Database

`database.md` must preserve enough information to represent:

- concept identity;
- language identity;
- concept-language pairs;
- exposure;
- replay;
- attempt state;
- parent observations;
- session history;
- derived scheduling state;
- due/rest timestamps;
- optional audio-source context.

The database should avoid storing unsupported pseudo-measures such as:

```text
pronunciation_score
fluency_score
iq_score
mastery_percentage
```

---

# 66. Relationship to UI/UX

`ui-ux.md` must ensure internal learning classifications do not accidentally become child judgments.

For example:

Internal:

```text
RESTING
```

does not become:

> “This word is too hard for you.”

Internal:

```text
NO_ATTEMPT_DETECTED
```

does not become:

> “You didn't answer.”

The UI should remain supportive and neutral.

---

# 67. Relationship to Family Test

`family-test-protocol.md` should distinguish between:

### Automatically captured

- exposures;
- replay;
- detected attempts;
- session duration.

### Parent observed

- independent operation;
- spontaneous interest;
- recognition outside app;
- use outside app;
- language preference;
- confusion/friction.

Both matter.

---

# 68. Learning Model Decision Summary

Prototype 0.1 adopts the following learning philosophy:

1. The unit of learning is the concept-language pair.
2. Evidence is stored separately from inferred scheduling state.
3. Exposure is necessary but not equivalent to learning.
4. Speaking attempts are participation evidence, not pronunciation evidence.
5. Replays are ambiguous and should be interpreted cautiously.
6. Spacing matters more than drilling.
7. Familiarity is temporary/inferred, not mastery.
8. Rest is temporary and reversible.
9. Recognition and recall are distinct.
10. Receptive and expressive evidence are distinct.
11. Real-world transfer is more meaningful than session completion.
12. Parent observations are essential but explicitly subjective.
13. Learning can be non-linear.
14. Missed days carry no punishment.
15. No permanent failure or mastery state exists.
16. No child-facing score is derived from the model.

---

# 69. Open Questions for `adaptive-engine.md`

The next document must decide:

1. How many exposures are enough before a pair may become familiar?
2. How should real-world recognition affect scheduling?
3. How strongly should real-world use affect scheduling?
4. How many intervening items should separate same-session repetition?
5. What constitutes repeated low interaction?
6. When does resting begin?
7. How long should a rest last?
8. How should a rested item be reintroduced?
9. How many new pairs may appear in one session?
10. How should English and Tamil be balanced?
11. Should parent-reported spontaneous use reduce frequency?
12. How should preferred/favorite items be used as session anchors?
13. How should missed days change due priority?
14. Should concept-level familiarity influence the second language pair?
15. What minimum evidence is required before changing derived state?

These should be answered with simple, inspectable rules suitable for Prototype 0.1.

---

# 70. Change Policy

Changes to this learning model require review because they affect:

- adaptive scheduling;
- database structure;
- parent progress wording;
- family-test interpretation;
- UI feedback;
- future analytics.

A change should document:

- learning term affected;
- previous definition;
- new definition;
- reason;
- supporting evidence or family-test observation;
- downstream documents requiring update.
