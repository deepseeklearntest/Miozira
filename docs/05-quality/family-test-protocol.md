# Miozira — Seven-Day Family Test Protocol

**Document:** `family-test-protocol.md`  
**Version:** 0.1  
**Status:** Draft / Prototype 0.1  
**Product:** Miozira  
**Test duration:** 7 days  
**Primary learner:** one familiar 4–5-year-old child  
**Last updated:** 2026-08-25

---

## 1. Purpose

This protocol defines the first real family test of Miozira Prototype 0.1.

The test is designed to answer:

> **Does the Miozira interaction make sense to a young child, invite voluntary language participation, remain calm, and lead to any recognition or use away from the tablet?**

This is not a clinical study and not a test of the child's ability.

It is a test of the product.

---

# 2. Preconditions

Do not begin until:

- `acceptance-criteria.md` passes;
- primary tablet passes device matrix;
- no blocker/high defects remain;
- all 16 content pairs are approved;
- adaptive config frozen;
- microphone config frozen;
- build version recorded.

---

# 3. Prototype Configuration

Freeze:

```text
Concepts:
apple
ball
cup
hand
nose
cat
car
water

Languages:
English
Tamil

Session target:
approximately 5–10 minutes

Mic:
speaking-attempt signal only

No:
scores
streaks
quizzes
mandatory completion
pronunciation grading
```

---

# 4. Main Test Questions

By the end of seven days, answer:

1. Does the child understand tap → hear without instruction?
2. Does the child voluntarily replay some words?
3. Does the child voluntarily repeat/vocalize sometimes?
4. Does the child remain emotionally comfortable?
5. Does the child return willingly on later days?
6. Does the adaptive repetition feel natural rather than repetitive?
7. Does switching between English and Tamil remain understandable?
8. Does at least one word get recognized or used outside Miozira?
9. Is the parent burden low enough to sustain?
10. Does the microphone add useful signal, or mostly noise/complexity?

---

# 5. What Is Not a Failure

Do **not** treat these as failure:

- session lasts only 3–4 minutes;
- child refuses to speak;
- child mispronounces;
- child prefers one language;
- child skips a concept;
- child wants to replay one favorite item;
- child stops unexpectedly.

---

# 6. What Would Be Concerning

Watch for:

- child cannot infer what to do after repeated use;
- repeated frustration;
- fear of speaking;
- app feels like a quiz;
- adaptive engine repeats one item too aggressively;
- language switching causes confusion;
- microphone feedback misleads;
- child becomes fixated on screen rather than language;
- parent must constantly instruct the child.

---

# 7. Stop Conditions

End a session immediately if:

- child is genuinely distressed;
- child clearly wants to stop;
- app crashes repeatedly;
- audio becomes uncomfortably loud;
- microphone behaves unexpectedly;
- technical bug compromises privacy/safety.

Do not continue merely to complete a test quota.

---

# 8. Daily Test Structure

Recommended:

```text
one natural session per day
```

A second session is allowed if the child independently wants it.

Do not force daily use.

If a day is skipped naturally:

> record that fact; do not compensate with extra sessions.

---

# 9. Parent Role During Session

Parent should:

- make tablet available;
- remain nearby initially;
- avoid teaching the controls repeatedly;
- avoid correcting pronunciation;
- avoid quizzing;
- intervene only for safety/technical help.

---

# 10. First-Day Observation

On Day 1, observe especially:

- does the child tap the picture?
- does the child connect tap with audio?
- does the child know what replay means?
- does the child understand the speaking invitation?
- does parent need to explain every step?

---

# 11. Days 2–3

Observe:

- recognition of interaction pattern;
- return behavior;
- voluntary vocalization;
- favorite concepts;
- frustration/repetition balance;
- English/Tamil preference.

---

# 12. Days 4–5

Observe:

- whether previously encountered words are remembered;
- whether adaptive reappearance feels natural;
- whether rested items returning cause friction;
- whether any word appears outside the app.

---

# 13. Days 6–7

Observe:

- overall willingness;
- whether product feels familiar;
- whether parent still needs to explain;
- real-world use;
- whether novelty has worn off but value remains.

---

# 14. Session Observation Template

After each session, parent records only a few observations:

```text
Date:
Approx session length:
Child started willingly? Y/N
Understood tap→hear? Y/N
Voluntary replay? Y/N
Voluntary speaking/vocalization? Y/N
Any frustration? None / Mild / Significant
Preferred language noticed? English / Tamil / Neither / Mixed
Stopped naturally? Y/N
Technical problem?:
One short note:
```

Do not produce a detailed behavioral diary.

---

# 15. Real-World Transfer Observation

When something happens naturally, parent may mark:

```text
Recognized outside Miozira
Used outside Miozira
```

Examples:

- child sees a car and reacts to the target word;
- child says Tamil word for water during routine;
- child identifies nose during play.

Do not stage elaborate tests.

---

# 16. No Parent Quizzing Protocol

Avoid:

```text
What is this called?
Say it in Tamil.
What did Miozira teach you?
Do you remember yesterday's word?
```

Prefer natural opportunities.

---

# 17. Microphone Validation

For a small number of attempts, parent may privately compare:

```text
Did the child actually vocalize?
vs
What did Miozira record as attempt state?
```

Goal:

> determine whether the weak detector is useful enough.

Do not tell child they are being evaluated.

---

# 18. Microphone Decision After Test

At the end choose:

```text
KEEP
KEEP BUT RETUNE
MAKE OPTIONAL/OFF BY DEFAULT
REMOVE FROM NEXT PROTOTYPE
```

based on usefulness vs complexity.

---

# 19. Language-Switching Decision

At the end choose:

```text
CURRENT BLOCKING WORKS
BLOCKS NEED TO BE LONGER
BLOCKS NEED TO BE SHORTER
ONE LANGUAGE PER SESSION MAY BE BETTER
MORE RESEARCH NEEDED
```

---

# 20. Real-World Suggestion Test

After sessions, parent assesses:

- was suggestion easy?
- did it feel natural?
- did it feel like homework?
- was one suggestion enough?

If it feels burdensome, simplify/remove.

---

# 21. Parent Burden Measure

At end of week ask:

> “Would I realistically keep doing the parent part without feeling like I am maintaining a school system?”

If no:

> reduce parent burden.

---

# 22. Core Success Signals

Strong positive evidence includes:

- child understands loop without instruction;
- voluntary replay;
- voluntary vocalization;
- several comfortable minutes;
- returns willingly;
- recognizes words across days;
- at least one word “escapes the app”;
- parent workflow feels light.

---

# 23. Minimum Product Signal

Prototype 0.1 does **not** need every signal to succeed.

The strongest minimum signal is:

> **The child understands and willingly uses the interaction, and at least one concept-language pair shows recognition/use beyond the immediate tap-and-hear moment.**

---

# 24. Failure Signals

Product-level failure may include:

- child repeatedly cannot understand interaction;
- app requires constant adult coaching;
- speech invitation creates discomfort;
- audio/visual design distracts from words;
- adaptive repetition feels mechanical/annoying;
- no evidence of later recall/transfer after repeated exposure;
- parent workflow feels burdensome.

---

# 25. Technical Bugs vs Product Findings

Separate:

```text
TECHNICAL BUG
PRODUCT/UX FINDING
LEARNING HYPOTHESIS FINDING
CONTENT/LANGUAGE FINDING
```

Example:

```text
audio didn't play = technical bug
child ignores listening cue = UX finding
Tamil word appears naturally outside app = learning signal
cup word feels unnatural at home = content finding
```

---

# 26. Do Not Change Build Mid-Test

Avoid changing during seven days:

- content;
- adaptive timing;
- microphone threshold;
- child UI;
- audio.

Only fix a material blocker/safety/privacy defect.

If build changes:

> record exact date/build and treat results as two test phases.

---

# 27. End-of-Week Review

Answer:

```text
KEEP:
CHANGE:
REMOVE:
ADD LATER:
UNKNOWN:
```

for:

- core tap/hear loop;
- speaking invitation;
- microphone detector;
- adaptive repetition;
- English/Tamil block structure;
- parent observation buttons;
- real-world suggestion;
- family voice.

---

# 28. Go / Iterate / Stop Decision

## GO

Continue to Prototype 0.2 if:

- core loop understood;
- child comfortable;
- voluntary participation exists;
- some evidence of recall/transfer;
- no major parent burden.

## ITERATE

Most likely outcome.

Use if:

- core idea works;
- one or more mechanics need adjustment.

## STOP / REFRAME

Use if:

- core tap/hear/repeat interaction fails to make sense;
- repeated exposure produces no meaningful behavior;
- child consistently dislikes the experience;
- product requires adult coaching to function.

---

# 29. Family-Test Final Summary Template

```text
Build:
Test dates:
Primary device:
Sessions completed:
Skipped days:
Average rough session length:

1. Child understood loop:
2. Voluntary replays:
3. Voluntary speaking:
4. Comfort/frustration:
5. Return willingness:
6. English/Tamil preference:
7. Adaptive repetition quality:
8. Real-world recognitions:
9. Real-world uses:
10. Parent burden:
11. Microphone usefulness:
12. Biggest UX problem:
13. Biggest positive signal:
14. Content/language issue:
15. Technical issue:
16. Decision: GO / ITERATE / STOP
17. Top 3 changes for next build:
```

---

# 30. Governing Rule

> **The family test measures whether Miozira belongs in the child's real life—not whether the child can satisfy Miozira.**
