# Miozira — Child Experience Specification

**Document:** `child-experience.md`  
**Version:** 0.2  
**Status:** Draft for Prototype 0.1 — Palette & Wellbeing Update  
**Product:** Miozira  
**Prototype:** Android-first, 8 concepts × English + Tamil  
**Target child:** approximately 4–5 years old  
**Last updated:** 2026-08-25

---

## 1. Purpose

This document defines the intended **child experience** of Miozira Prototype 0.1.

It focuses on:

- emotional tone;
- behavioral expectations;
- first-use experience;
- replay;
- hesitation;
- silence;
- speaking attempts;
- language switching;
- frustration;
- stopping;
- returning;
- interruption;
- repeated sessions.

`ui-ux.md` defines the interface structure.

`session-design.md` defines the session sequence.

This document defines:

> **how the experience should feel to the child.**

---

# 2. Child Experience North Star

Miozira should feel like:

> **“I touch something, I hear it, I can try it if I want, and the app is happy to keep going with me.”**

It should not feel like:

> **“I am being tested.”**

The child should experience:

- curiosity;
- predictability;
- choice;
- gentle repetition;
- freedom to stop;
- no fear of being wrong.

---

# 3. The Child Is Not a Student Account

Prototype 0.1 should not treat the child like:

- a learner profile to optimize;
- a score to improve;
- a lesson-completion target;
- an engagement metric;
- a test subject.

The product should treat the child as:

> a young person exploring sounds and meaning through touch, repetition, and family life.

---

# 4. Core Emotional Principles

## CE-P01 — No pressure

The child never needs to:

- finish;
- answer;
- speak;
- perform;
- return tomorrow.

---

## CE-P02 — No shame

The experience never communicates:

- wrong;
- poor;
- failed;
- behind;
- not good enough.

---

## CE-P03 — No artificial urgency

The experience avoids:

- countdowns;
- streak loss;
- time pressure;
- disappearing rewards;
- “hurry.”

---

## CE-P04 — Predictability creates confidence

The child should gradually learn:

```text
tap → hear → pause → continue
```

without needing repeated adult explanation.

---

## CE-P05 — Curiosity is enough

A child tapping to hear a word again is meaningful interaction.

Miozira does not need to turn curiosity into a task.

---

## CE-P06 — Silence is acceptable

A quiet child is still participating.

---

## CE-P07 — Real life matters more than screen behavior

A child who says nothing in Miozira but later uses a word at dinner may be showing more meaningful transfer than a child who repeats every prompt inside the app.

---

# 5. First-Ever Use

The first-ever session has two goals:

1. help the child understand what tapping does;
2. make the experience feel safe and interesting.

It should **not** try to maximize content exposure.

---

# 6. First Screen

The first child-facing screen should show:

- one familiar object;
- no text instruction requirement;
- no adult settings;
- no menu.

The object should be obvious enough that the child can naturally explore it.

---

# 7. First Tap

When the child taps the object:

- response should feel immediate;
- the word should play clearly;
- the visual should respond subtly.

The child should quickly learn:

> **“Touching this makes it speak.”**

That understanding is more important than anything else in the first few interactions.

---

# 8. First Speaking Invitation

The first speaking invitation should be gentle and brief.

It should not demand:

> “Say it now.”

Preferred behavior:

- word plays;
- short pause;
- subtle invitation;
- child may respond or remain silent.

---

# 9. First Silence

If the child says nothing during first use:

Miozira should behave as though nothing has gone wrong.

Possible flow:

```text
word
→ short pause
→ optional replay
→ next item
```

The child should not learn that silence causes pressure.

---

# 10. First Replay

If the child taps again:

- replay the word;
- keep the interaction stable;
- do not introduce a reward.

The child may replay because:

- the sound is interesting;
- the word is funny;
- the child wants to imitate;
- the child is experimenting with cause and effect.

All are acceptable.

---

# 11. Discovering the Pattern

After a few interactions, the child should begin understanding:

> **“I can touch these pictures and hear the words.”**

At that point:

- explicit hints may reduce;
- pauses may become quieter;
- the interaction remains consistent.

---

# 12. Returning Session Experience

A returning child should not need to relearn the interface.

A normal returning session should feel:

- familiar at the start;
- gently varied;
- predictable in interaction;
- different enough not to become mechanical.

---

# 13. Familiar Opener

A returning session should usually begin with an item the child has previously handled comfortably.

Purpose:

- rebuild confidence;
- re-establish interaction;
- make the start easy.

---

# 14. New Content Experience

When a new concept-language pair appears:

- it should not be visually labeled “NEW”;
- it should not be treated like a challenge;
- it should simply appear as another object.

The child should experience novelty naturally.

---

# 15. Emerging / Less-Familiar Item Experience

Miozira must never reveal internal learning states such as:

```text
EMERGING
UNCERTAIN
RESTING
```

The child should not know that Miozira thinks one item needs more exposure.

All items should feel equally valid.

---

# 16. Familiar Item Experience

Familiar items may return because:

- they anchor the session;
- they create retrieval opportunities;
- they make repetition comfortable.

They should not be marked:

> “Easy!”

The product should avoid turning internal familiarity into a child comparison.

---

# 17. Speaking Attempt Experience

If the child tries to say something:

- Miozira acknowledges participation;
- it does not grade the content.

The child should feel:

> **“It heard that I tried.”**

not:

> **“It judged how well I said it.”**

---

# 18. Mispronunciation

Prototype 0.1 does not know whether pronunciation is correct.

Therefore the product must not visually or verbally imply otherwise.

If the child says:

- part of the word;
- a different word;
- a sound;
- an approximation;

the product may still acknowledge the speaking attempt.

---

# 19. Silence

Silence may mean:

- not ready;
- already knows it;
- tired;
- shy;
- distracted;
- listening carefully;
- simply does not want to perform.

Therefore:

> **silence is not a negative state.**

---

# 20. Hesitation

If the child pauses:

- Miozira should wait briefly;
- use at most a gentle cue;
- avoid repeated prompts.

The child should not feel chased by the interface.

---

# 21. Repeated Silence

Even repeated silence should not create escalating pressure.

The adaptive engine may later:

- reduce frequency;
- rest the item;
- bring it back another day.

The child-facing experience remains neutral.

---

# 22. High Replay Behavior

If the child repeatedly replays one word:

Do not interrupt solely to enforce the planned session.

A few replays may be useful exploration.

Technical safeguards may prevent:

- audio overlap;
- extreme rapid tap flooding.

But no visible “replay limit” is needed.

---

# 23. Favorite Items

A child may develop favorites.

Examples:

- cat;
- car;
- ball.

Miozira may use preferred items as:

- opener;
- recovery item;
- closer.

The child should not receive a badge for having a favorite.

---

# 24. Low-Interest Items

If one item repeatedly receives little interest:

- do not repeat it more aggressively;
- do not visibly mark it difficult;
- let the adaptive engine rest it.

The child should experience only:

> **less of that item for a while.**

---

# 25. Resting and Return

When a rested item returns days later:

- it should appear normally;
- no warning;
- no remedial label;
- preferably after an easy item.

The child should not know the system classified it differently.

---

# 26. Language Switching Experience

English and Tamil should feel like two natural spoken possibilities, not a translation drill.

The child should not be required to understand:

> “Now we are switching languages.”

The session may simply transition naturally.

---

# 27. If Language Switching Confuses the Child

Possible signs:

- stops tapping;
- looks toward parent;
- repeated unexpected reaction;
- switches away immediately.

If this happens consistently:

- session design may add a subtle language cue;
- block lengths may change;
- switching frequency may decrease.

Do not assume confusion from a single unusual reaction.

---

# 28. No Language Preference Judgment

If the child prefers one language:

Miozira does not label:

- stronger language;
- weaker language;
- good language;
- bad language.

Preference is observation, not judgment.

---

# 29. Movement Interaction Experience

A movement prompt should feel playful and natural.

Example:

> “Touch your nose.”

The child may:

- respond;
- laugh;
- ignore it;
- do something else.

No outcome is graded.

---

# 30. Movement and Screen Break

Movement prompts can create a small shift from passive screen attention toward physical interaction.

They should not become:

- exercise quotas;
- mini-games;
- scored motor challenges.

---

# 31. Inactivity

If the child stops touching the screen:

Miozira should become quieter, not louder.

Possible sequence:

```text
wait
→ subtle cue
→ wait
→ optional second cue
→ quiet idle
```

The product should not escalate.

---

# 32. Natural Stopping

A child may stop because:

- attention moved elsewhere;
- another activity became interesting;
- tiredness;
- parent interaction;
- session felt complete.

This is acceptable.

A session does not need formal completion to be valid.

---

# 33. Early Exit Experience

If the child leaves after 3–4 minutes:

- no failure message;
- no “come back to finish” prompt;
- no loss of progress.

The child should simply be done.

---

# 34. Parent Ends Session

If the parent ends the session:

the child should not receive an implication that:

- the session was incomplete;
- the child did poorly.

The experience should end calmly.

---

# 35. App Interruption

If Miozira is interrupted:

- returning should feel stable;
- no repeated reward;
- no duplicate audio;
- no restart shock where avoidable.

A technical interruption should not be transformed into a learning event.

---

# 36. Orientation Change

If the tablet rotates:

- the same concept remains;
- the same interaction remains;
- no new word is triggered automatically;
- the child does not need to rediscover controls.

Rotation should feel like layout adaptation, not navigation.

---

# 37. Microphone Disabled

If microphone access is not available:

the child experience should still feel normal.

The only difference may be:

- no listening cue;
- slightly simpler pause;
- same progression.

The child should never see:

> “Microphone permission denied.”

---

# 38. Microphone Cue

If microphone detection is enabled:

the cue should communicate:

> **“You may speak now.”**

not:

> **“You are being recorded and evaluated.”**

Avoid:

- waveforms;
- volume meters;
- red recording indicators beyond OS requirements;
- pronunciation gauges.

---

# 39. Response Tone

Acknowledgements should feel:

- warm;
- brief;
- non-evaluative.

Examples:

- “Mm-hm.”
- “Nice.”
- gentle sound.

Avoid constant praise after every action.

Too much praise can become mechanical and can accidentally turn participation into performance.

---

# 40. No Over-Praise

Miozira should not say:

- “Amazing!”
- “Perfect!”
- “You're a genius!”
- “Best job ever!”

after every tap.

The child does not need exaggerated reward language for ordinary participation.

---

# 41. No Negative Correction

Never use:

- “No.”
- “Wrong.”
- “Try again.”
- “That's not it.”
- “Say it properly.”

Prototype 0.1 is not a pronunciation tutor.

---

# 42. Recasting

If future content introduces phrases or more expressive interaction, Miozira may use recasting:

Child:

> “ca”

Miozira:

> “Car.”

This models the target without explicitly saying the child was wrong.

Prototype 0.1 word-level audio replay already approximates this pattern.

---

# 43. Quiet Moments

The child experience should intentionally include small quiet periods.

Miozira does not need to continuously fill silence with:

- music;
- narration;
- animation;
- prompts.

Quiet processing time is acceptable.

---

# 44. Visual Stimulation — STRICT LOW-STIMULATION RULE

The child should not be exposed to constant decorative motion or bright multi-accent UI.

Prototype 0.1 uses:

- warm neutral background;
- natural concept colors;
- one temporary state accent when needed;
- no decorative animation layer.

When nothing important is happening:

> **the screen should normally remain still.**

Attention should be drawn by:

- the object;
- the word;
- cause-and-effect;
- meaningful motion;
- curiosity about what happens next.

It should not be drawn by:

- brightness;
- visual noise;
- looping motion;
- reward effects.

---

# 45. Sound and Haptic Stimulation

The product should avoid:

- background music loops;
- layered effects;
- sudden loud sounds;
- repeated vibration patterns;
- reward sounds.

The spoken word is the main audio event.

A single soft haptic tick may accompany a deliberate child tap if real-device testing shows that it strengthens cause-and-effect understanding.

Haptics must never become:

- praise;
- reward;
- urgency;
- a pattern the child chases.

---

# 46. Cause and Effect

At this age, clear cause-and-effect interaction is important.

Miozira should maintain:

```text
tap object
→ object responds
→ word plays
```

The relationship should remain consistent.

---

# 47. Avoid Hidden Child Rules

The child should not need to infer rules such as:

- tap only after animation ends;
- speak before next button unlocks;
- wait for score;
- finish 5 words before exiting.

The experience should remain transparent.

---

# 48. Child Agency

The child has agency over:

- whether to tap;
- whether to replay;
- whether to speak;
- whether to continue;
- whether to stop.

The adaptive engine controls sequence, not compliance.

---

# 49. No Daily Obligation

Returning tomorrow is not required.

Miozira should not create:

- daily challenge;
- calendar chain;
- missed-day warning.

The child may return when family circumstances allow.

---

# 50. Re-entry After Several Days

After a gap:

- start gently;
- use familiar content;
- do not show backlog;
- do not say “Welcome back — you missed 4 days.”

The child should experience continuity, not debt.

---

# 51. Real-World Success Is Invisible to Child UI

If the parent reports:

> “Used outside Miozira”

the child does not need to see:

- trophy;
- unlocked level;
- badge.

The adaptive engine may widen spacing quietly.

---

# 52. Parent Observation Should Not Change Child Tone

Parent-reported evidence should change scheduling, not child-facing praise intensity.

Example:

A word with spontaneous real-world use may appear less often.

It should not suddenly become a “gold word.”

---

# 53. What Counts as a Good Session

A good session may involve:

- several taps;
- a replay;
- one speaking attempt;
- no speaking attempts;
- laughter;
- quiet listening;
- early stop.

No single behavior is required.

---

# 54. What Does Not Mean Failure

The following are explicitly **not failures**:

- session lasts 3–4 minutes;
- child does not repeat;
- child mispronounces;
- child taps only favorite items;
- child wants one language more than another;
- child ignores a movement prompt;
- child leaves the app;
- child is silent one day.

---

# 55. What Might Signal UX Friction

Repeated patterns may indicate a product problem:

- child cannot find the tap target;
- child repeatedly looks to parent for instructions;
- child waits for a missing “next” button;
- language changes repeatedly stop interaction;
- listening cue causes discomfort;
- child exits at the same state every session;
- touch misses are frequent;
- audio replay happens because initial playback is hard to hear.

These are product signals, not child deficits.

---

# 56. Family-Test Observation Guidance

During the seven-day test, parents should observe without constantly intervening.

Useful observations:

- Does the child understand the tap interaction independently?
- Does the child voluntarily replay?
- Does the child imitate without being asked by the parent?
- Does the child react to language changes?
- Does the child have favorites?
- Does the child stop naturally?
- Does the child ask to use Miozira again?
- Does a word show up later away from the tablet?

---

# 57. Parent Intervention

During normal family testing, the parent should avoid:

- correcting every pronunciation;
- forcing repetition;
- telling the child to finish;
- demonstrating every tap after the child already understands;
- turning the session into a quiz.

Parent support is welcome, but it should preserve the child's voluntary experience.

---

# 58. Co-Use

Parent or caregiver co-use can be valuable.

Examples:

- sitting nearby;
- repeating a word naturally;
- smiling/reacting;
- later using the word in real life.

Co-use should not mean controlling every app interaction.

---

# 59. Emotional Safety

Miozira should never intentionally cause:

- fear of error;
- embarrassment;
- guilt about stopping;
- anxiety about losing progress;
- pressure to perform for the device.

This is a product design constraint.

---

# 60. Preschool Variability

Prototype 0.1 assumes wide variability in:

- speech development;
- attention;
- motor precision;
- willingness to imitate;
- language exposure;
- temperament.

The UI must remain robust across these differences.

---

# 61. Avoid Developmental Comparison

Miozira must not tell parents or children:

- “Most children your age know this.”
- “Your child is behind.”
- “Your child is ahead.”

Prototype 0.1 does not contain a developmental norming system.

---

# 62. No Child Identity Performance Label

The product should not produce labels such as:

- fast learner;
- slow learner;
- visual learner;
- auditory learner;
- weak speaker.

Evidence remains item-specific and contextual.

---

# 63. Child-Facing Language

Child-facing spoken prompts should be:

- short;
- warm;
- natural;
- non-instruction-heavy.

For Tamil, use natural household Tamil rather than literal translations.

---

# 64. Child-Facing Visual Language

The visual language should prioritize:

1. clear concept;
2. clear action;
3. calm state feedback.

Decorative branding is secondary.

---

# 65. Child Experience States

Conceptual emotional states Miozira should accommodate:

```text
CURIOUS
ENGAGED
QUIET
HESITANT
REPLAYING
VOCALIZING
DISTRACTED
DONE
```

These are design lenses.

The app does not need to automatically classify the child into these states.

---

# 66. CURIOUS

Likely behavior:

- exploratory tapping;
- replay;
- looking at visual.

Miozira response:

- immediate cause/effect;
- no extra instruction.

---

# 67. ENGAGED

Likely behavior:

- repeated interaction;
- vocalization;
- continued attention.

Miozira response:

- maintain pace;
- do not intensify rewards.

---

# 68. QUIET

Likely behavior:

- watches;
- listens;
- does not speak.

Miozira response:

- allow silence;
- continue normally.

---

# 69. HESITANT

Likely behavior:

- pauses;
- looks to parent;
- delayed tap.

Miozira response:

- subtle cue;
- easy next item if needed.

---

# 70. REPLAYING

Likely behavior:

- taps same concept multiple times.

Miozira response:

- replay cleanly;
- avoid overlapping audio.

---

# 71. VOCALIZING

Likely behavior:

- says word;
- approximates;
- makes sound.

Miozira response:

- acknowledge attempt;
- never grade pronunciation.

---

# 72. DISTRACTED

Likely behavior:

- looks away;
- stops touching;
- moves physically.

Miozira response:

- reduce prompts;
- allow natural stop.

---

# 73. DONE

Likely behavior:

- leaves;
- says no;
- turns away;
- closes app;
- gives tablet back.

Miozira response:

- end gracefully;
- preserve evidence;
- no pressure.

---

# 74. Session Recovery Pattern

If a difficult or low-engagement interaction occurs:

recommended child-facing flow:

```text
uncertain item
→ calm response
→ familiar/preferred item
```

The child should not be told this is a recovery strategy.

---

# 75. No Consecutive Pressure

Avoid sequencing:

```text
hard item
→ hard item
→ hard item
```

when the child is showing low interaction.

This is partly enforced by the adaptive engine.

---

# 76. Child Experience and Real-World Transfer

The app session should end before the product begins over-directing family activity.

The important continuation happens outside the app:

```text
tablet word
→ family moment
→ recognition/use
```

The child should not experience that as assigned homework.

---

# 77. Child Experience Success Criteria

Prototype 0.1 child experience is promising if, across the seven-day test, several of these occur:

- child understands tap-to-hear with little instruction;
- child voluntarily replays;
- child voluntarily vocalizes;
- child tolerates both languages;
- child remains engaged for several minutes without pressure;
- child stops naturally without distress;
- child returns willingly;
- child recognizes or uses at least one word outside Miozira.

---

# 78. Child Experience Warning Signs

Review the product if several of these recur:

- child needs repeated adult instruction every session;
- child seems confused about what to tap;
- child reacts negatively to listening cue;
- language switches repeatedly disrupt engagement;
- audio/animation is overstimulating;
- the child becomes focused on triggering effects rather than meaning;
- session end causes frustration because the experience behaves like a game reward loop;
- parent feels compelled to pressure the child to get “results.”

---

# 79. Child Experience Invariants

### CE-INV-001
The child can use Miozira without reading.

### CE-INV-002
The child is never required to speak.

### CE-INV-003
The child is never graded on pronunciation.

### CE-INV-004
Silence never creates negative feedback.

### CE-INV-005
Early stopping is always allowed.

### CE-INV-006
No streak or daily quota exists.

### CE-INV-007
Replay is permitted without penalty.

### CE-INV-008
No learning state is shown as a child label.

### CE-INV-009
No internal “rest” state is exposed as difficulty.

### CE-INV-010
No missed days create guilt.

### CE-INV-011
No real-world use becomes a trophy mechanic.

### CE-INV-012
Parent-reported evidence changes scheduling, not child worth.

---

# 80. Relationship to `ui-ux.md`

`ui-ux.md` defines:

- screens;
- layouts;
- visual states;
- parent gate;
- controls.

This document constrains those designs emotionally and behaviorally.

If a visually attractive UI violates the principles here, it should be rejected.

---

# 81. Relationship to `session-design.md`

`session-design.md` defines the session sequence.

This document defines how that sequence should feel:

- low pressure;
- predictable;
- forgiving;
- optional.

---

# 82. Relationship to `adaptive-engine.md`

The adaptive engine must support the child experience by:

- avoiding drill;
- using familiar anchors;
- resting repeated low-interaction items;
- not punishing missed days;
- not treating silence as failure.

---

# 83. Relationship to `parent-experience.md`

The parent experience must protect the child experience by:

- discouraging pressure;
- using evidence language;
- avoiding mastery scores;
- framing real-world transfer as natural family interaction.

---

# 84. Decision Summary

Miozira Prototype 0.1 should make the child feel:

- free to explore;
- safe to stay silent;
- welcome to replay;
- welcome to try;
- never judged;
- never rushed;
- free to stop.

The app should become understandable through repeated cause-and-effect interaction rather than instruction.

Its ideal emotional tone is:

> **calm curiosity without performance pressure.**


---

# v0.2 Emotional Design Principle — Interest Comes From Learning

Miozira should be interesting because:

- the child recognizes an object;
- the object responds predictably;
- the word sounds interesting;
- the child can imitate it;
- a meaningful action can occur;
- the concept later appears in real life.

Miozira should **not** manufacture interest using heightened stimulation.

The desired state is:

> **“I wonder what this is / what it does / how it sounds.”**

not:

> **“I need the next reward.”**

The visual system therefore uses subdued interface colors and lets real-world concept imagery carry natural color.

Slow interaction is acceptable.

No screen state should imply that the child needs to hurry.
