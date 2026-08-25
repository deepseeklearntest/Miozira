# Miozira — Adaptive Engine

**Document:** `adaptive-engine.md`  
**Version:** 0.1  
**Status:** Draft for Prototype 0.1  
**Product:** Miozira  
**Prototype:** 8 concepts × English + Tamil  
**Target child:** approximately 4–5 years old  
**Last updated:** 2026-08-25

---

## 1. Purpose

This document defines the adaptive scheduling rules for Miozira Prototype 0.1.

The adaptive engine decides:

- which concept-language pair should appear next;
- when a previously seen pair should return;
- how much spacing is required between repeats;
- when a pair should temporarily rest;
- how a rested pair should return;
- how familiar and uncertain items are mixed;
- how English and Tamil exposure is balanced;
- how parent-reported real-world evidence affects future scheduling.

The engine must remain:

- local;
- deterministic or otherwise explainable;
- lightweight;
- testable;
- reversible;
- suitable for a very small prototype.

Prototype 0.1 shall **not** use:

- machine-learning recommendation models;
- cloud personalization;
- reinforcement learning;
- hidden engagement optimization;
- mastery scores;
- pronunciation scores.

---

# 2. Governing Principles

The adaptive engine follows these principles in order.

## AE-P01 — Avoid drilling

The same concept-language pair should not repeat immediately unless replay was explicitly requested.

## AE-P02 — Prefer spaced return

A pair that needs reinforcement should usually return after other items have intervened.

## AE-P03 — Keep sessions emotionally easy

A session should mix easier/familiar pairs with newer or less-certain pairs.

## AE-P04 — Do not punish silence

No-attempt events are weak evidence and must not aggressively increase repetition.

## AE-P05 — Rest instead of hammering

Repeated low interaction should cause temporary rest, not escalating repetition.

## AE-P06 — Real-world evidence matters

Parent-reported recognition/use should reduce the need for frequent repetition.

## AE-P07 — Keep language exposure structured

English and Tamil should both appear, but not in a chaotic back-to-back translation pattern.

## AE-P08 — Adaptation must be inspectable

For every selected item, the engine should be able to provide a human-readable reason.

Example:

> “Selected because due, not shown this session, and lower familiarity than alternatives.”

---

# 3. Core Input Data

The adaptive engine operates on concept-language pair state.

For each pair it may use:

```text
concept_id
language_id
exposure_count
last_exposed_at
replay_count
attempt_count
no_attempt_count
parent_recognition_count
parent_use_count
last_real_world_event_at
derived_state
next_due_at
rest_until
last_session_id
times_seen_this_session
```

Not every field must exist physically in the final database.

The data model may derive some fields from events.

---

# 4. Derived Pair States

Prototype 0.1 uses four main descriptive scheduling states:

```text
NEW
EMERGING
FAMILIAR
RESTING
```

A separate due condition is calculated independently.

This means a pair may be:

```text
FAMILIAR + DUE
EMERGING + NOT_DUE
```

but:

```text
RESTING
```

temporarily overrides ordinary due scheduling.

---

# 5. Initial State

## AE-001 — New pair

A concept-language pair begins as:

```text
state = NEW
exposure_count = 0
next_due_at = null
rest_until = null
```

No pair is pre-classified as familiar merely because the underlying concept is familiar in real life.

---

# 6. Exposure-Based State Changes

## AE-002 — First exposure

After the first valid exposure:

```text
NEW → EMERGING
```

unless exceptional evidence already exists through parent reporting.

---

## AE-003 — Familiarity candidate

A pair becomes eligible for `FAMILIAR` when all of the following are true:

1. at least **3 valid exposures** have occurred;
2. the exposures span at least **2 separate sessions**;
3. there is at least one additional positive signal.

Positive signals include:

- speaking attempt detected;
- voluntary replay;
- parent-reported real-world recognition;
- parent-reported real-world use.

This is intentionally conservative.

---

## AE-004 — Strong real-world shortcut

A parent-reported real-world use may allow an `EMERGING` pair to become `FAMILIAR` even if it has fewer than 3 app exposures.

Recommended Prototype 0.1 rule:

```text
if parent_use_count >= 1
and exposure_count >= 1
then state may become FAMILIAR
```

This acknowledges that meaningful use outside Miozira can be stronger evidence than repeated in-app exposure.

---

# 7. Familiar Does Not Mean Finished

## AE-005 — Familiar pairs remain schedulable

`FAMILIAR` pairs continue to return occasionally.

They are not permanently retired.

Their spacing simply becomes wider than `EMERGING` pairs.

---

# 8. Due Scheduling

Prototype 0.1 uses simple elapsed-time spacing.

The engine should not attempt to model a precise forgetting curve.

---

## AE-006 — Default due intervals

Recommended initial intervals:

### NEW

Immediately eligible for initial introduction.

### EMERGING

After a meaningful exposure:

```text
next_due_at = +1 day
```

### FAMILIAR

After a meaningful exposure:

```text
next_due_at = +3 days
```

### FAMILIAR with real-world use

If the pair has at least one parent-reported real-world use:

```text
next_due_at = +5 days
```

These values are Prototype 0.1 defaults and should be revisited after the seven-day test.

---

# 9. Same-Session Spacing

## AE-007 — Minimum intervening items

A pair should not normally return in the same session until at least:

> **2 other concept-language pairs**

have appeared since its previous exposure.

Preferred:

> **2–3 intervening items**

This reflects the product's use of spaced retrieval without copying laboratory protocols mechanically.

---

## AE-008 — Same-pair repeat cap

The same concept-language pair should normally appear no more than:

> **2 times in one session**

excluding child-initiated replay.

A third scheduled appearance requires an explicit reason, such as:

- extremely short session with too few eligible items;
- testing an intentional reintroduction rule;
- developer/family-test override.

---

# 10. Replay Handling

## AE-009 — Replay does not reschedule aggressively

A replay should:

- increment replay evidence;
- not count as a separate canonical scheduled exposure by default;
- not immediately cause the item to repeat later in the session.

Replay is treated primarily as interaction evidence.

---

# 11. Speaking Attempt Handling

## AE-010 — Attempt detected

A speaking attempt:

- increments attempt evidence;
- may strengthen familiarity;
- does not prove pronunciation correctness;
- does not automatically extend spacing by itself.

---

## AE-011 — No attempt detected

A no-attempt event:

- should be stored if measurement was active;
- does not immediately increase repetition frequency;
- contributes only weakly to difficult-encounter detection.

---

# 12. Difficult Encounter Model

Prototype 0.1 does not classify one silent response as difficulty.

A pair becomes a **rest candidate** only after repeated low-interaction encounters.

---

## AE-012 — Low-interaction encounter

An encounter may be considered low-interaction when several of the following occur:

- no speaking attempt;
- no voluntary replay;
- rapid disengagement;
- session exit immediately around the item;
- parent observation indicates the item was frustrating.

Automated detection should use only signals actually available.

---

## AE-013 — Rest trigger

Recommended Prototype 0.1 rule:

A pair enters `RESTING` when:

> **3 low-interaction encounters occur across at least 2 sessions**

and there is no strong positive evidence such as recent real-world recognition/use.

This matches the product principle of resting an item after repeated poor encounters instead of drilling it.

---

# 13. Rest Duration

## AE-014 — Default rest

Recommended default:

> **4 days**

After 4 days, the pair becomes eligible for reintroduction.

---

## AE-015 — Extended rest

If a pair returns after rest and again produces repeated low-interaction encounters:

> increase rest to **7 days**

Prototype 0.1 should not use rest periods longer than 7 days because the family test itself is short.

---

# 14. Resting Behavior

## AE-016 — Resting pair excluded

While:

```text
current_time < rest_until
```

the pair should not be selected for normal session scheduling.

---

## AE-017 — Rest does not erase history

Entering or leaving rest shall not reset:

- exposure count;
- attempts;
- parent observations;
- familiarity evidence.

---

# 15. Reintroduction

## AE-018 — Reintroduction priority

When rest expires, the pair should not necessarily be the first item shown.

It should return:

- after at least one familiar/easy item;
- preferably in a different position than before;
- without announcing that the item was difficult.

---

## AE-019 — Reintroduction context variation

Where possible, reintroduction should vary one or more contextual factors:

- sequence position;
- active language block;
- visual variant;
- parent real-world suggestion;
- associated physical action.

Prototype 0.1 may only support sequence variation initially.

---

# 16. Parent Real-World Evidence

## AE-020 — Recognition effect

A parent-reported real-world recognition should:

- strengthen familiarity evidence;
- reduce short-term repetition pressure;
- normally move `next_due_at` later.

Recommended effect:

```text
next_due_at = max(current next_due_at, observation_time + 3 days)
```

---

## AE-021 — Use effect

A parent-reported real-world use should be treated as stronger evidence than recognition.

Recommended effect:

```text
state = FAMILIAR
next_due_at = max(current next_due_at, observation_time + 5 days)
```

provided at least one prior exposure exists.

---

## AE-022 — Real-world evidence never retires pair permanently

Even with repeated real-world use, the pair remains part of the long-term concept network.

Prototype 0.1 simply presents it less frequently.

---

# 17. Concept-Level Cross-Language Influence

Evidence for one language must not automatically transfer to the other language.

However, concept familiarity can influence introduction order.

---

## AE-023 — Underlying concept familiarity

Because Prototype 0.1 uses familiar real-world concepts, both language forms are assumed to share a known referent.

Therefore introducing the second language form does not require teaching the concept itself from scratch.

---

## AE-024 — No language evidence copying

The engine shall not copy:

- exposure count;
- attempt count;
- real-world recognition;
- real-world use;
- familiarity state

from English to Tamil or from Tamil to English.

---

## AE-025 — Second-language introduction bonus

If one language pair for a concept is `FAMILIAR`, the second language pair may be considered a slightly safer new introduction candidate.

This affects selection priority only.

It does not change the second pair's learning state.

---

# 18. Session Composition

Prototype 0.1 should use a simple session recipe rather than continuously optimizing each next item.

Recommended target for an ordinary session:

```text
40–50% familiar/easy
30–40% emerging/due
10–20% new
```

These are guidance ranges, not rigid quotas.

---

# 19. Number of New Pairs Per Session

## AE-026 — New-pair cap

A normal session should introduce at most:

> **2 NEW concept-language pairs**

This prevents an eight-concept prototype from becoming a novelty dump.

---

## AE-027 — No new pair late in weak session

If the child shows sustained low interaction or appears to be ending the session, the engine should not introduce additional new pairs merely to meet a planned session recipe.

---

# 20. Easy/Familiar Anchor Items

## AE-028 — Session opener

When history exists, start with:

1. a `FAMILIAR` pair;
2. otherwise a previously seen `EMERGING` pair;
3. otherwise a `NEW` pair.

---

## AE-029 — Recovery item

After a low-interaction encounter, the next item should preferably be a familiar or preferred pair rather than another difficult/new item.

---

## AE-030 — Session closer

If the system can infer that the session is approaching a natural end, prefer a familiar or previously positive pair.

Prototype 0.1 may implement this simply as the final planned item being familiar.

---

# 21. English and Tamil Balance

Prototype 0.1 needs enough exposure to both languages to test the system, but should avoid rapid translation chaining.

---

## AE-031 — Session language structure

Recommended default strategy:

> **short language blocks**

Example:

```text
English: 2–3 items
Tamil:   2–3 items
English or Tamil: remaining items based on due priority
```

The exact order may vary between sessions.

---

## AE-032 — Avoid immediate same-concept translation

Avoid:

```text
apple English
apple Tamil
```

as the default sequence.

Prefer intervening concepts:

```text
apple English
ball English
cup Tamil
apple Tamil
```

---

## AE-033 — Language exposure balancing

Across several sessions, the engine should aim for roughly comparable opportunities in English and Tamil.

It should not enforce exact 50/50 exposure in every session.

---

## AE-034 — Temporary language preference

If the child shows stronger engagement in one language, Miozira may use that language for an easy opener or closer.

It shall not starve the other language of exposure.

---

# 22. Preferred Items

## AE-035 — Preferred item as anchor

A pair showing repeated voluntary replay/positive interaction may be used as:

- opener;
- recovery item;
- closer.

---

## AE-036 — Preference does not inflate learning status

Preference shall not directly change a pair to `FAMILIAR`.

Preference and learning evidence remain separate concepts.

---

# 23. Missed Days

## AE-037 — No penalty

Missing a day or several days shall not:

- reset progress;
- reduce familiarity state automatically;
- create a failure status.

---

## AE-038 — Due backlog

After several missed days, multiple pairs may become due.

The engine should spread them across sessions rather than attempting to “catch up” in one session.

---

# 24. Selection Priority

Each candidate pair may receive a simple scheduling priority.

Prototype 0.1 may implement priority as ordered rules rather than a numerical score.

Recommended order:

1. exclude `RESTING`;
2. exclude pairs at same-session repeat cap;
3. avoid pairs shown too recently in the current session;
4. prioritize due `EMERGING`;
5. prioritize due `FAMILIAR`;
6. allow `NEW` if new-pair cap not reached;
7. use familiar/preferred pair when a recovery/easy item is needed;
8. use not-due pairs only if there are insufficient eligible items.

---

# 25. Optional Numerical Priority

If implementation is simpler with a numeric score, the following transparent model may be used:

```text
priority =
  due_weight
+ emerging_weight
+ overdue_weight
+ language_balance_weight
+ recovery_anchor_weight
+ new_candidate_weight
- recent_session_penalty
- repeat_penalty
- rest_penalty
```

The numeric values must be explicit constants in code/configuration.

No hidden learned weights.

---

# 26. Recommended Prototype Priority Constants

A possible v0.1 implementation:

```text
DUE_EMERGING            +50
DUE_FAMILIAR            +35
NEW_CANDIDATE           +25
LANGUAGE_UNDEREXPOSED   +10
PREFERRED_RECOVERY      +15
REAL_WORLD_USE_RECENT   -20
SHOWN_THIS_SESSION      -30
SHOWN_LAST_2_ITEMS      -100
REPEAT_CAP_REACHED      -1000
RESTING                 -10000
```

These values are implementation guidance, not product truth.

The engine must be tested to ensure they produce the intended behavior.

---

# 27. Human-Readable Selection Reason

## AE-039 — Explainability

Every scheduled item should be explainable through one or more reason codes.

Suggested codes:

```text
DUE_EMERGING
DUE_FAMILIAR
NEW_INTRODUCTION
EASY_OPENER
RECOVERY_ITEM
SESSION_CLOSER
LANGUAGE_BALANCE
REINTRODUCTION_AFTER_REST
INSUFFICIENT_ALTERNATIVES
```

This is useful for:

- development;
- debugging;
- family-test interpretation;
- future tuning.

---

# 28. Session Planning Strategy

Prototype 0.1 should prefer planning a short queue at session start, then adjusting after each interaction.

Recommended approach:

1. build an initial candidate set;
2. choose an easy opener;
3. add due/learning-priority items;
4. add at most 1–2 new pairs;
5. reserve one familiar item for later;
6. after each interaction, re-evaluate only the next few items if needed.

This avoids both extremes:

- fully fixed session;
- over-engineered real-time recommendation system.

---

# 29. Session Length Estimation

The adaptive engine should not enforce session length by countdown.

It may estimate a target number of scheduled interactions.

Recommended initial target:

> **8–12 scheduled concept interactions**

depending on pacing and replay behavior.

Because each interaction varies, actual duration may be shorter or longer.

---

# 30. Early Session End

## AE-040 — Child stops

If the child stops interacting:

- persist completed events;
- end gracefully;
- do not force planned items;
- do not mark remaining items failed;
- do not alter their state merely because they were scheduled but not shown.

---

# 31. Scheduled vs Exposed

A concept planned for the session does not count as exposed until the actual exposure requirements in `learning-model.md` are met.

This distinction prevents false learning history.

---

# 32. No-Response Handling

## AE-041 — First low-response encounter

Do not change state aggressively.

Normal behavior:

- re-model if appropriate;
- continue;
- keep pair `EMERGING` or `FAMILIAR` as previously classified.

---

## AE-042 — Repeated low-response encounters

Only repeated low-response patterns across sessions should influence rest.

---

# 33. Strong Positive Evidence Handling

## AE-043 — Parent-reported recognition

Increase spacing modestly.

## AE-044 — Parent-reported use

Increase spacing more strongly and classify as familiar if appropriate.

## AE-045 — Repeated spontaneous use

Prototype 0.1 may simply continue treating the pair as familiar with long spacing.

A separate “mastered” state shall not be introduced.

---

# 34. State Recalculation

Derived scheduling state should be recalculated from stored evidence whenever practical.

This is preferable to irreversible one-way transitions.

Example:

```text
deriveState(evidence) -> FAMILIAR
```

rather than:

```text
database permanently says mastered = true
```

---

# 35. State Downgrade

Prototype 0.1 should avoid automatically downgrading `FAMILIAR` to `EMERGING` merely because time passed.

Instead, elapsed time should affect:

```text
due status
```

not identity.

If later family testing shows repeated loss of recognition, a future model can revisit this.

---

# 36. Algorithm Outline

A simple next-item algorithm may follow:

```text
function chooseNextItem(session, allPairs, now):

    candidates = allPairs

    remove candidates where state == RESTING and rest_until > now

    remove candidates where times_seen_this_session >= 2

    strongly deprioritize items shown in last 2 scheduled positions

    if session needs easy opener:
        prefer familiar seen pair

    if session recovering from low interaction:
        prefer familiar or preferred pair

    if session new_pair_count < 2:
        allow NEW pairs

    prioritize:
        due emerging
        due familiar
        safe new
        not-due familiar/emerging

    adjust for language balance

    return highest eligible candidate
```

The implementation may differ while preserving the same rules.

---

# 37. State Update Outline

After an interaction:

```text
record exposure if valid

record replay if child requested one

record attempt status if measured

update exposure counts

recalculate pair state

calculate next_due_at

check repeated low-interaction history

if rest trigger reached:
    state = RESTING
    rest_until = now + 4 days
```

After parent observation:

```text
record real-world observation

if recognition:
    strengthen familiarity evidence
    push due date later

if use:
    state = FAMILIAR
    push due date later
```

---

# 38. Example — New Tamil Pair

Starting state:

```text
apple + Tamil
state = NEW
exposures = 0
```

Session 1:

```text
Exposure 1
Speaking attempt detected
```

Result:

```text
state = EMERGING
next_due_at = tomorrow
```

Session 2:

```text
Exposure 2
Replay
```

Result:

```text
state = EMERGING
```

Session 3:

```text
Exposure 3
Speaking attempt
```

Now:

- 3 exposures;
- across multiple sessions;
- positive evidence exists.

Result:

```text
state = FAMILIAR
next_due_at = +3 days
```

---

# 39. Example — Real-World Use Shortcut

Starting:

```text
water + Tamil
exposure_count = 1
state = EMERGING
```

Parent reports:

> child used the Tamil word for water at dinner.

Result:

```text
state = FAMILIAR
next_due_at = observation_time + 5 days
```

This avoids forcing repeated app exposure despite stronger real-world evidence.

---

# 40. Example — Resting Pair

`nose + Tamil`

Across 3 encounters in 2 sessions:

- no attempt;
- no replay;
- rapid disengagement.

No recent positive evidence.

Result:

```text
state = RESTING
rest_until = +4 days
```

The child sees other content instead.

After rest expires:

- start session with a familiar pair;
- reintroduce nose later;
- do not announce difficulty.

---

# 41. Example — Missed Days

Child does not use Miozira for 4 days.

Several pairs become due.

The next session should:

- choose a familiar opener;
- show some due emerging items;
- include at most 2 new pairs;
- spread remaining overdue items across future sessions.

There is no catch-up mode.

---

# 42. Example — English/Tamil Sequence

Possible session:

```text
1. ball + English      [easy opener]
2. apple + English     [due]
3. water + Tamil       [due]
4. cat + Tamil         [new]
5. ball + Tamil        [emerging]
6. apple + English     [spaced return]
7. car + Tamil         [familiar]
8. water + English     [closer]
```

Not every session must contain exactly equal languages.

---

# 43. Family-Test Configuration

Prototype 0.1 adaptive constants should be configurable centrally.

Recommended configuration fields:

```text
emerging_due_days = 1
familiar_due_days = 3
real_world_use_due_days = 5

same_session_min_intervening_items = 2
same_session_pair_cap = 2

new_pair_cap_per_session = 2

rest_trigger_low_interaction_count = 3
rest_trigger_min_sessions = 2

first_rest_days = 4
second_rest_days = 7

target_interactions_min = 8
target_interactions_max = 12
```

These values should not be scattered across UI code.

---

# 44. Adaptive Engine Invariants

### AE-INV-001
No pronunciation score influences scheduling.

### AE-INV-002
No cloud service is required to choose an item.

### AE-INV-003
No missed day produces punishment.

### AE-INV-004
No single silent encounter triggers rest.

### AE-INV-005
No pair is permanently failed.

### AE-INV-006
No pair is permanently mastered.

### AE-INV-007
A resting pair must eventually become eligible again.

### AE-INV-008
Child-initiated replay does not count as a scheduled repeat.

### AE-INV-009
English evidence must not be copied automatically to Tamil, or vice versa.

### AE-INV-010
Parent real-world observations remain parent-reported evidence.

### AE-INV-011
The child can end a session before the scheduled queue is exhausted.

### AE-INV-012
A planned-but-never-shown item is not counted as exposed.

---

# 45. What the Engine Must Not Optimize

Prototype 0.1 must not optimize for:

- session duration;
- number of taps;
- daily return rate;
- streak survival;
- maximum content consumption;
- maximum microphone responses.

The engine optimizes only for a calm, sensible sequence of learning opportunities.

---

# 46. Test Requirements

The adaptive engine must have unit tests covering at minimum:

1. new pair selection;
2. first exposure transition;
3. emerging-to-familiar transition;
4. parent-use shortcut;
5. due-date calculation;
6. same-session spacing;
7. same-session repeat cap;
8. new-pair cap;
9. rest trigger;
10. rest expiration;
11. extended rest;
12. language balancing;
13. missed-day backlog;
14. early session ending;
15. microphone unavailable behavior;
16. no-attempt neutrality;
17. parent recognition effect;
18. parent use effect;
19. planned-but-not-exposed behavior;
20. explainability reason codes.

---

# 47. Simulation Requirement

Before family testing, the engine should be run through simulated histories.

Examples:

- child always attempts;
- child never attempts;
- child frequently replays;
- child only engages with Tamil;
- child only engages with English;
- parent reports real-world use early;
- several days are skipped;
- one pair repeatedly triggers low interaction;
- microphone is permanently disabled.

The goal is to verify that the engine remains calm and understandable under edge cases.

---

# 48. Prototype 0.1 Tuning Policy

Adaptive constants may be changed during internal development.

Once the seven-day family test begins, configuration should be frozen unless a bug or harmful behavior requires intervention.

This prevents changing the rules mid-test and making the observations difficult to interpret.

---

# 49. Evidence for Future Revision

After the family test, revisit:

- whether 3 exposures is too many/few for familiarity;
- whether +1/+3/+5 day intervals are too aggressive or too sparse;
- whether 2 intervening items feels natural;
- whether 4–7 day resting is effective;
- whether two new pairs per session is manageable;
- whether language blocks are understandable;
- whether real-world evidence should have stronger scheduling effects.

Changes should be based on observed behavior rather than theoretical preference alone.

---

# 50. Relationship to Session Design

`session-design.md` will define the child's actual session flow:

- opening;
- concept presentation;
- audio timing;
- imitation window;
- microphone timing;
- gentle response;
- movement interactions;
- transition pacing;
- session ending;
- parent handoff.

The adaptive engine decides **what** item comes next.

`session-design.md` defines **how** that item is experienced.

---

# 51. Relationship to Database

`database.md` must support enough evidence to reproduce adaptive decisions.

The database should preserve:

- exposure events or counts;
- attempt events;
- replay evidence;
- parent observations;
- session history;
- due timestamps;
- rest timestamps;
- derived state;
- selection reason where useful for testing.

The database must not need to store opaque model embeddings or remote recommendation data.

---

# 52. Relationship to Architecture

`architecture.md` should implement the adaptive engine as platform-independent domain logic where practical.

The engine should not require:

- Android UI classes;
- microphone implementation details;
- audio playback classes;
- database driver details.

It should operate on abstract evidence/state and return a scheduling decision.

---

# 53. Recommended Internal Contract

A future `api.md` may expose an internal contract resembling:

```text
AdaptiveEngine.startSession(context)
AdaptiveEngine.chooseNextItem(sessionState, learnerState)
AdaptiveEngine.recordInteraction(result)
AdaptiveEngine.recalculatePairState(pairId)
AdaptiveEngine.recordRealWorldObservation(observation)
AdaptiveEngine.endSession()
```

Exact names are deferred.

---

# 54. Decision Summary

Prototype 0.1 adaptive behavior is intentionally simple:

- first exposure → emerging;
- 3 separated exposures + positive evidence → familiar;
- emerging pairs generally return after 1 day;
- familiar pairs generally return after 3 days;
- real-world use extends spacing to around 5 days;
- same pair needs at least 2 intervening items before scheduled repetition;
- normal same-session cap is 2 appearances;
- at most 2 new pairs per session;
- 3 low-interaction encounters across at least 2 sessions → 4-day rest;
- repeated difficulty after return → 7-day rest;
- sessions mix familiar, due, and a small amount of new content;
- English and Tamil are structured in short blocks rather than constant translation pairs;
- real-world use counts more than app repetition;
- silence does not count as failure;
- no permanent mastery or failure state exists.

These rules are hypotheses to test, not claims about universal child learning.
