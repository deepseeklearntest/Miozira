# Miozira — Real-World Transfer

**Document:** `real-world-transfer.md`  
**Version:** 0.1  
**Status:** Draft for Prototype 0.1  
**Product:** Miozira  
**Prototype:** 8 concepts × English + Tamil  
**Target child:** approximately 4–5 years old  
**Last updated:** 2026-08-25

---

## 1. Purpose

This document defines how Miozira Prototype 0.1 helps language move from the tablet into ordinary family life.

It specifies:

- what “real-world transfer” means;
- what Miozira should ask parents to do;
- what Miozira should **not** ask parents to do;
- how recognition and use are reported;
- how prompted and spontaneous use differ;
- what contexts are suitable;
- how transfer evidence feeds the adaptive engine;
- how to keep parent involvement lightweight.

The core idea is:

> **Miozira succeeds when something heard on the tablet later becomes meaningful between the child, family, and real world.**

---

# 2. Why Transfer Matters

Prototype 0.1 does not exist to maximize:

- lesson completion;
- session duration;
- tap count;
- in-app scores;
- daily usage.

Its stronger outcome is:

> a concept-language association showing up outside Miozira.

Examples:

- the child responds to the Tamil word for water at dinner;
- the child points to the ball when hearing the target word;
- the child spontaneously says the Tamil word for car during play.

These moments show that the app interaction may have connected to real-life meaning.

---

# 3. Definition of Real-World Transfer

## RWT-001 — Transfer

Real-world transfer is:

> evidence that a concept-language association encountered in Miozira later appears in a different physical or social context.

A different context may include:

- a real object;
- a toy;
- a family routine;
- a meal;
- bath time;
- play;
- travel;
- conversation;
- a book;
- outdoors.

The context must be meaningfully outside the original app interaction.

---

# 4. Transfer Is Not Testing

Miozira should not convert parents into examiners.

Avoid:

> “Ask your child what ‘apple’ is in Tamil.”

Prefer:

> “At snack time, say the Tamil word for apple while handing over a slice.”

The goal is:

> natural use first, observation second.

---

# 5. Transfer Evidence Types

Prototype 0.1 should distinguish at least two parent-facing evidence types:

```text
RECOGNIZED_OUTSIDE_APP
USED_OUTSIDE_APP
```

A richer future model may distinguish:

```text
USED_AFTER_PROMPT
USED_SPONTANEOUSLY
```

The UI can remain simpler than the underlying model.

---

# 6. Real-World Recognition

## RWT-002 — Recognition

A parent may report **recognition** when the child appears to understand a target spoken form outside Miozira.

Examples:

- parent says the Tamil word for ball and the child looks at or points to the ball;
- parent says the target word for water and the child reaches for water;
- child identifies the correct real object after hearing the word.

Recognition does not require the child to speak.

---

# 7. Prompted Use

## RWT-003 — Prompted Use

Prompted use occurs when the child says or meaningfully uses a target word after an immediate adult cue.

Example:

Parent points to a car and asks for the word; child says it.

This is meaningful evidence.

It is weaker than spontaneous use because the child received a production cue.

Prototype 0.1 may group prompted and spontaneous production under “Used outside Miozira” in the parent UI for simplicity.

---

# 8. Spontaneous Use

## RWT-004 — Spontaneous Use

Spontaneous use occurs when the child independently produces the target word in an appropriate real-world context without an immediate request to say it.

Example:

The child sees a toy car and independently says the Tamil word.

This is among the strongest forms of transfer evidence available to Prototype 0.1.

---

# 9. Parent-Reported Evidence

All real-world transfer evidence is:

> **parent/caregiver reported**

Miozira does not independently verify it.

The product should never convert:

```text
parent reported use
```

into:

```text
scientifically verified mastery
```

---

# 10. Transfer Suggestion

After a meaningfully active session, Miozira should provide:

> **one short real-world suggestion**

The suggestion is for the parent, not the child.

Its purpose is to create one easy opportunity for the recently encountered word to appear naturally in family life.

---

# 11. Suggestion Design Rules

Every suggestion should be:

- short;
- concrete;
- easy to do;
- tied to normal family life;
- connected to a recently encountered concept;
- language-specific;
- non-testing;
- optional.

---

# 12. Suggestion Structure

Recommended format:

```text
[when/where] + [natural parent action]
```

Examples:

> “At snack time, say the Tamil word for apple while handing over a slice.”

> “During play, say the Tamil word for ball when you roll it to him.”

> “At bath time, say the Tamil word for nose while washing his face.”

---

# 13. Suggestion Length

A suggestion should normally fit in:

> **one short sentence**

The parent should be able to understand it in a few seconds.

Avoid long coaching instructions.

---

# 14. One Suggestion, Not a Task List

After one session, Miozira should not give the parent:

- five activities;
- homework;
- a checklist;
- a daily quota.

Prototype 0.1 should normally give:

> **one suggestion**

That keeps parent burden low.

---

# 15. Suggested Contexts by Concept

## Apple

Natural contexts:

- snack time;
- kitchen;
- grocery shopping.

Example:

> “At snack time, say the Tamil word for apple while handing over a piece.”

---

## Ball

Natural contexts:

- indoor play;
- outdoor play;
- toy cleanup.

Example:

> “During play, say the Tamil word for ball when rolling it across the floor.”

---

## Cup

Natural contexts:

- drinking;
- snack time;
- table setting.

Example:

> “At drink time, say the Tamil word for cup while handing it over.”

Final suggestion wording depends on the approved Tamil lexical form.

---

## Hand

Natural contexts:

- hand washing;
- dressing;
- waving;
- holding hands.

Example:

> “While washing hands, say the Tamil word for hand naturally.”

---

## Nose

Natural contexts:

- bath time;
- washing face;
- getting dressed;
- playful body-part interaction.

Example:

> “While getting ready, say the Tamil word for nose as you gently point to it.”

---

## Cat

Natural contexts:

- seeing a real cat;
- looking at a book;
- toy animals;
- neighborhood walk.

Example:

> “If you see a cat today, say the Tamil word once and keep moving.”

---

## Car

Natural contexts:

- toy play;
- travel;
- parking area;
- road observation.

Example:

> “During toy-car play, say the Tamil word for car when handing it over.”

---

## Water

Natural contexts:

- drinking;
- meals;
- bath time;
- washing hands.

Example:

> “When offering water, say the Tamil word naturally before handing it over.”

---

# 16. Context Selection

Miozira should prefer contexts that are:

- likely to occur soon;
- easy to understand;
- safe;
- already part of family routine.

The app should not suggest artificial activities merely to create a learning opportunity.

---

# 17. Recently Encountered Content

The suggestion should normally reference:

> a concept-language pair encountered during the recent session.

This creates a bridge between:

```text
app exposure
→ family reinforcement
```

---

# 18. Avoiding Repetition Fatigue

Miozira should avoid giving the exact same real-world suggestion after every session.

The product may rotate:

- contexts;
- wording;
- concept selection.

This does not require generative AI.

A small local rule/template system is sufficient for Prototype 0.1.

---

# 19. Suggestion Selection Priority

Recommended selection order:

1. a recently encountered concept;
2. preferably emerging/due rather than extremely familiar;
3. concept with an obvious real-world context;
4. avoid a concept currently in rest if it may be frustrating;
5. avoid repeatedly choosing the same concept across consecutive sessions.

---

# 20. Familiar Content Still Useful

A familiar concept may occasionally be used for transfer suggestions because easy family success can reinforce the product loop.

However, suggestions should not always favor only the easiest pair.

---

# 21. Language Selection

The suggestion must clearly reflect the target language.

If the relevant pair was:

```text
apple + Tamil
```

the suggestion should encourage Tamil use.

It should not automatically use English because the parent UI is in English.

---

# 22. Parent UI Presentation

The suggestion should appear in the parent area after the child session.

Recommended content:

```text
Try this outside Miozira

At snack time, say the Tamil word for apple while handing over a slice.
```

The exact UI is defined later in `parent-experience.md` / `ui-ux.md`.

---

# 23. No Child-Facing Homework

The child should not see:

- “Practice this later.”
- “Homework.”
- “Say this to your parent.”
- “Complete this challenge.”

Transfer remains a natural family action.

---

# 24. Parent Observation Capture

The parent should be able to record a real-world event later.

Minimum options:

```text
Recognized outside Miozira
Used outside Miozira
```

The UI should allow this with very little effort.

---

# 25. Observation Entry Flow

Recommended Prototype 0.1 flow:

1. parent opens protected area;
2. sees recent concept activity;
3. selects concept/language;
4. taps:
   - Recognized outside Miozira; or
   - Used outside Miozira;
5. event is saved.

This should take only a few seconds.

---

# 26. Observation Timestamp

Each observation should store:

```text
recorded_at
```

Optionally:

```text
observed_at
```

may be added later if parents need to backdate an event.

Prototype 0.1 can use the recording time if simpler.

---

# 27. Observation Context

Prototype 0.1 does not require free-text notes.

A later version may optionally support context such as:

```text
meal
play
bath
travel
outdoors
book
other
```

For the first family test, adding extra data-entry burden may not be worthwhile.

---

# 28. No Parent Score

The parent should not receive a score derived from real-world reports.

Avoid:

- “5/8 concepts transferred.”
- “62% real-world mastery.”
- “Top-performing language.”

Simple evidence history is enough.

---

# 29. Adaptive Engine Feedback

Real-world observations affect future scheduling.

As defined in `adaptive-engine.md`:

### Recognition

Should modestly reduce short-term repetition pressure.

### Use

Should reduce repetition more strongly and may move an emerging pair to familiar.

---

# 30. Transfer Does Not Retire Content

Even after real-world use:

- pair remains part of the learning network;
- it may return later;
- spacing becomes wider.

There is no permanent “done” state.

---

# 31. Transfer and Rest

If a pair is currently close to being rested due to low in-app interaction but the parent reports real-world recognition/use:

> real-world evidence should override the assumption that the pair is simply too difficult.

The adaptive engine should avoid resting or should end rest early where strong positive evidence exists.

Exact implementation belongs in `adaptive-engine.md`.

---

# 32. Transfer and Language Separation

A real-world event applies to the specific language where known.

Example:

```text
car + Tamil
real-world use reported
```

does not automatically count as:

```text
car + English
real-world use reported
```

The shared concept does not mean shared language evidence.

---

# 33. Transfer and Family Voice

Family voice may strengthen contextual relevance.

Example:

- child hears grandmother's Tamil recording in Miozira;
- later hears grandmother say the same word during family interaction.

This continuity may be valuable.

Prototype 0.1 should support the possibility without claiming measured causal benefit.

---

# 34. Transfer Opportunity vs Transfer Evidence

These are different.

## Opportunity

Miozira suggested:

> “Say the Tamil word for ball during play.”

## Evidence

Parent later reports:

> “Child recognized/used the word.”

The app must not assume the suggestion caused a successful transfer event.

---

# 35. Suggested Local Data Model

The real-world transfer domain may use:

```text
RealWorldSuggestion
RealWorldObservation
```

Example suggestion:

```yaml
suggestion_id: rw.apple.snack.ta.v1
concept_id: concept.apple
language_code: ta
context: snack
text_parent_ui: ...
```

Example observation:

```yaml
concept_id: concept.apple
language_code: ta
type: USED_OUTSIDE_APP
recorded_at: ...
```

Final schema belongs in `database.md`.

---

# 36. Suggestion Content Should Be Local

Prototype 0.1 real-world suggestions shall be available offline.

No cloud generation is required.

Recommended implementation:

- small curated template library;
- concept metadata;
- local selection rules.

---

# 37. No Generative AI Requirement

Prototype 0.1 does not need an LLM to produce parent suggestions.

Eight concepts are small enough to curate manually.

This improves:

- predictability;
- privacy;
- tone control;
- language quality;
- offline reliability.

---

# 38. Parent Burden Constraint

Real-world transfer features must not make the parent feel they are maintaining a learning-management system.

Prototype 0.1 should avoid asking the parent to:

- log every word;
- grade pronunciation;
- track minutes;
- fill forms;
- write notes after every session.

The product should require only occasional lightweight observations.

---

# 39. Real-World Suggestion Frequency

Recommended Prototype 0.1 rule:

> one suggestion after each meaningfully active session.

If the child completes several tiny sessions in a short period, the app may avoid generating repeated parent prompts.

Exact suppression rule may be implementation-defined.

---

# 40. Suggestion Persistence

The most recent suggestion should remain visible long enough for the parent to act on it.

It should not disappear immediately if the parent does not open the area at once.

---

# 41. Suggestion Completion

Prototype 0.1 should not require the parent to mark a suggestion as “completed.”

Real family interaction is not a task-management workflow.

---

# 42. Recognized vs Used UI Wording

Recommended parent-facing wording:

```text
Recognized outside Miozira
Used outside Miozira
```

Avoid:

```text
Passed recognition
Passed production
Mastered in real life
```

---

# 43. Real-World Transfer Ladder

For conceptual understanding only:

```text
App exposure
   ↓
Real-world re-encounter
   ↓
Recognition
   ↓
Prompted use
   ↓
Spontaneous use
```

This is not a required linear path.

Children may jump between stages.

---

# 44. Transfer Can Precede In-App Participation

A child may:

- remain silent in the app;
- later use the word in real life.

This is valid.

Therefore:

> absence of in-app speech does not invalidate real-world evidence.

---

# 45. Transfer Can Be Delayed

The child may use a word:

- the same day;
- several days later;
- after multiple sessions.

Miozira should not impose a narrow transfer window.

---

# 46. Real-World Generalization

A strong future signal is when the child recognizes a different instance of the same concept.

Example:

Miozira shows one red apple.

Later, child recognizes:

- a green apple;
- an apple in a supermarket;
- an apple in a book.

Prototype 0.1 parent reporting may not capture this granularity, but the concept model should support future study.

---

# 47. Avoiding Artificial Parent Language

Parent suggestions should sound natural.

Avoid:

> “Please expose the learner to the lexical item ‘car’ in a contextual environment.”

Prefer:

> “During play, say the Tamil word for car when handing over a toy car.”

---

# 48. Family Register

Tamil suggestions should use natural family Tamil.

They should not be literal English translations if that produces awkward speech.

The Tamil version of each suggestion should be reviewed by fluent speakers.

---

# 49. Multilingual Family Context

The product should assume that real homes may switch languages naturally.

Miozira does not need parents to enforce strict monolingual interaction.

A transfer suggestion targets one language at a time for clarity, but normal family conversation can remain multilingual.

---

# 50. No Fluency Claim

Real-world use of one word does not justify claims such as:

- “child is fluent”;
- “child knows Tamil”;
- “child has mastered vocabulary.”

Prototype 0.1 only records specific concept-language evidence.

---

# 51. Transfer Success for Prototype 0.1

A highly encouraging seven-day signal would be:

> at least one concept-language pair recognized or used outside Miozira.

This is not a formal efficacy threshold.

The family test should interpret the whole pattern of behavior.

---

# 52. Transfer Failure Is Not Child Failure

If no real-world use occurs during the first week, possible explanations include:

- insufficient exposure;
- wrong concept set;
- poor prompts;
- weak family context;
- language already too familiar;
- language too unfamiliar;
- child not interested in speaking;
- timing;
- app interaction not memorable.

The product should investigate the loop, not label the child unsuccessful.

---

# 53. Family-Test Questions

During the seven-day test, observe:

1. Did the parent notice recognition without deliberately testing?
2. Did the child use any target word spontaneously?
3. Did suggestions fit normal family routines?
4. Were suggestions easy to remember?
5. Did they feel like homework?
6. Did the parent actually use them?
7. Which contexts produced the strongest reactions?
8. Did English/Tamil switching carry into family interaction naturally?
9. Did parent-reported use match in-app engagement?
10. Were there words used outside Miozira despite silence inside the app?

---

# 54. Transfer Metrics

Prototype 0.1 may summarize locally:

```text
recognition_observation_count
use_observation_count
concepts_with_real_world_evidence
languages_with_real_world_evidence
last_real_world_event_at
```

These are developer/parent evidence summaries.

They are not child performance scores.

---

# 55. No Competitive Ranking

Real-world transfer data shall never be used in Prototype 0.1 to compare:

- children;
- siblings;
- families;
- languages.

The prototype has one family-test context and no ranking system.

---

# 56. Privacy

Real-world observations should contain minimal data.

Prototype 0.1 does not require:

- location;
- photos;
- video;
- audio recording;
- free-text family diary;
- cloud upload.

A concept/language/event/timestamp record is sufficient.

---

# 57. Transfer Invariants

### RWT-INV-001
A suggestion is not a quiz.

### RWT-INV-002
A suggestion is optional.

### RWT-INV-003
Parent-reported evidence remains explicitly parent-reported.

### RWT-INV-004
Recognition does not require speech.

### RWT-INV-005
Prompted use and spontaneous use are conceptually distinct.

### RWT-INV-006
Real-world evidence applies to a specific concept-language pair where known.

### RWT-INV-007
Real-world use does not create a permanent mastery state.

### RWT-INV-008
No generative AI is required for suggestions.

### RWT-INV-009
No real-world observation requires internet access.

### RWT-INV-010
No parent is required to record every learning moment.

### RWT-INV-011
The product must not turn family life into homework.

---

# 58. Prototype 0.1 Suggestion Library Requirement

Before the family test, the content package should contain:

> at least **one approved real-world suggestion per concept per target language**, where natural.

For 8 concepts × 2 languages, this means up to:

> **16 curated language-specific suggestion entries**

The parent UI may display English explanatory text while indicating the target Tamil/English word, but actual family-language prompt quality should be reviewed.

---

# 59. Example Suggestion Inventory

Possible initial English-parent-UI descriptions:

```text
apple + Tamil
At snack time, say the Tamil word for apple while handing over a slice.

ball + Tamil
During play, say the Tamil word for ball when rolling it across the floor.

cup + Tamil
At drink time, say the chosen Tamil word for cup while handing it over.

hand + Tamil
While washing hands, say the Tamil word for hand naturally.

nose + Tamil
While getting ready, say the Tamil word for nose as you point to it.

cat + Tamil
If you see a cat, say the Tamil word naturally once.

car + Tamil
During toy-car play, say the Tamil word for car while handing it over.

water + Tamil
When offering water, say the Tamil word naturally before handing it over.
```

Equivalent English-target suggestions should be available if English is the active learning target.

---

# 60. Relationship to `adaptive-engine.md`

The adaptive engine must:

- consume recognition/use observations;
- reduce unnecessary near-term repetition after strong real-world evidence;
- never copy Tamil evidence to English or vice versa;
- preserve pair availability for future review;
- use real-world evidence as one of the strongest scheduling signals.

---

# 61. Relationship to `content-spec.md`

`content-spec.md` owns:

- concept inventory;
- canonical words;
- language review;
- suggestion content inventory.

This document owns:

- how suggestions are used;
- how transfer is interpreted;
- how observations are captured.

---

# 62. Relationship to `parent-experience.md`

The parent experience must make:

- suggestion viewing;
- recognition reporting;
- use reporting

quick and understandable.

It must not become a dashboard-heavy workflow.

---

# 63. Relationship to `database.md`

The database should support:

```text
RealWorldObservation
```

with at minimum:

```text
id
concept_id
language_id
observation_type
recorded_at
```

Optional future fields:

```text
observed_at
context
prompted_or_spontaneous
```

---

# 64. Relationship to `family-test-protocol.md`

The family-test protocol should treat real-world transfer as a major outcome.

It should capture both:

- parent-entered observations in Miozira;
- informal parent notes about unexpected real-world moments.

The protocol should not instruct the parent to engineer constant tests.

---

# 65. Decision Summary

Miozira Prototype 0.1 defines real-world transfer as:

> language encountered in Miozira later becoming recognizable or usable in ordinary family life.

The product will support this by:

1. giving one short real-world suggestion after a meaningful session;
2. tying suggestions to recently encountered concept-language pairs;
3. using natural family contexts;
4. avoiding quizzes and homework;
5. allowing parents to mark recognition/use;
6. treating parent reports as evidence, not verified mastery;
7. feeding that evidence back into local scheduling;
8. keeping the whole workflow fully offline and lightweight.

The product's most meaningful success signal remains:

> **Did a word escape the app?**
