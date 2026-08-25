# Miozira — Child Safety Specification

**Document:** `child-safety.md`  
**Version:** 0.2  
**Status:** Draft for Prototype 0.1 — Palette & Wellbeing Update  
**Product:** Miozira  
**Target child:** approximately 4–5 years old  
**Primary platform:** Android tablet  
**Last updated:** 2026-08-25

---

## 1. Purpose

This document defines child-safety requirements for Miozira Prototype 0.1.

It covers:

- emotional safety;
- interaction pressure;
- session pacing;
- stopping and disengagement;
- microphone/privacy safeguards;
- parent involvement;
- content appropriateness;
- visual/audio stimulation;
- error handling;
- destructive/adult controls;
- compulsive-use prevention;
- recovery from product failure;
- child-facing language;
- safety testing.

The goal is:

> **Miozira should help a young child encounter language without creating pressure, shame, surveillance, compulsion, or unnecessary risk.**

---

# 2. Child Safety North Star

A safe Miozira session should feel like:

> “I can look, tap, listen, try if I want, and stop when I am done.”

It should not feel like:

> “I am being tested, judged, rushed, or made to keep going.”

---

# 3. Child Safety Principles

## SAFE-P01 — No coercion

The child is never forced to:

- speak;
- replay;
- finish a session;
- complete a quota.

## SAFE-P02 — No shame

The app never labels the child:

- wrong;
- bad;
- slow;
- behind;
- failed.

## SAFE-P03 — No urgency

No countdowns, streak warnings, expiring rewards, or time pressure.

## SAFE-P04 — No surveillance framing

The microphone does not imply that Miozira “understands” or grades the child.

## SAFE-P05 — Real life outranks screen time

Miozira supports real-world language use rather than maximizing app time.

## SAFE-P06 — Parent retains control

Sensitive settings and recording features remain parent-facing.

## SAFE-P07 — Safety failures fail gently

Technical problems do not become child-facing alarms or punishment.

## SAFE-P08 — No stimulation engineering

Miozira must not use sensory intensity as a mechanism to prolong use.

This includes:

- bright multi-color UI;
- repeated animation;
- reward sounds;
- reward haptics;
- flashing;
- urgency cues;
- variable-reward effects.

## SAFE-P09 — Calm pacing

The product never speeds up, escalates effects, or increases prompt intensity because a child takes time to respond.

---

# 4. Emotional Safety

Prototype 0.1 must avoid interaction patterns that produce:

- fear of mistakes;
- pressure to perform;
- comparison;
- guilt for stopping;
- repeated correction;
- reward dependence.

---

# 5. No Correct / Wrong States

Child UI must not display:

```text
Correct
Wrong
Try again
Oops
Almost
Bad pronunciation
```

No red X / green check correctness system.

---

# 6. No Pronunciation Judgment

Even if microphone detects speech-like activity, the response must not say:

```text
Great pronunciation!
You said it correctly!
```

because the system has not measured that.

---

# 7. Safe Response Language

Acceptable child-facing responses are brief and non-evaluative.

Examples:

```text
Your turn.
Let's hear it again.
There it is.
```

or simple non-verbal acknowledgement.

Exact wording requires content review.

---

# 8. Silence Is Safe

If the child does not speak:

- no error;
- no warning;
- no sad animation;
- no forced repeat.

The app may continue after a short pause.

---

# 9. Mispronunciation Is Safe

Prototype 0.1 does not attempt to detect mispronunciation.

The canonical word can act as a gentle model/recast.

No explicit correction loop is needed.

---

# 10. Repetition Is Optional

The child may repeat a word many times or not at all.

Replay is available but not demanded.

---

# 11. Stopping Is Valid

A session can end because:

- child walks away;
- parent exits;
- attention shifts;
- child chooses not to continue.

This is not recorded as failure.

---

# 12. No Mandatory Completion Screen

Do not require the child to reach:

```text
Lesson complete
```

before leaving.

---

# 13. No Guilt Mechanics

Do not display:

- “You missed yesterday”;
- “Come back to keep your streak”;
- “Only 2 more”;
- “Don't give up.”

---

# 14. No Competitive Metrics

No:

- scores;
- ranks;
- leaderboards;
- stars;
- XP;
- lives;
- badges;
- streaks.

---

# 15. Session Length

Prototype target:

> approximately 5–10 minutes

This is a product design target, not a medical threshold.

Child can stop earlier.

---

# 16. Session Duration Is Not Achievement

Do not reward longer sessions.

Do not tell parent:

> “Your child only used Miozira for 4 minutes.”

Short sessions can be successful.

---

# 17. No Autoplay Marathon

Miozira should not continuously push new content indefinitely.

The session engine has a modest interaction target and natural stopping behavior.

---

# 18. Natural Ending

A session should end calmly.

No:

- fireworks;
- escalating celebration;
- teaser forcing another lesson.

---

# 19. Screen-Time Safety

Miozira should not optimize for:

- daily active minutes;
- retention at all costs;
- consecutive sessions.

The product should encourage transfer away from the tablet.

---

# 20. Real-World Handoff

After a meaningful session, parent may receive one simple real-world suggestion.

This should not become another task the child must complete.

---

# 21. Parent Co-Use

Parent involvement is encouraged when useful.

Examples:

- sit nearby;
- repeat naturally;
- notice a word in daily life.

Avoid turning parent into examiner.

---

# 22. Parent Should Not Quiz

Parent guidance should discourage:

```text
What is this?
Say it properly.
Again.
You know this.
```

as repeated performance testing.

Prefer natural use.

---

# 23. Parent Observation Is Subjective

Parent reports are evidence, not diagnosis.

Do not tell parents:

> “Your child has mastered Tamil apple.”

---

# 24. Developmental Variation

Children vary in:

- speech;
- attention;
- motor control;
- language exposure;
- confidence.

Miozira must not diagnose or rank developmental ability.

---

# 25. No Developmental Labels

Do not classify child as:

```text
advanced
behind
struggling learner
slow speaker
```

---

# 26. No Medical Claims

Prototype 0.1 is not:

- speech therapy;
- developmental screening;
- clinical language assessment.

Do not market it as such.

---

# 27. Microphone Safety

Child microphone use is:

- optional;
- brief;
- parent-enabled;
- local;
- non-grading.

---

# 28. No Hidden Recording

Microphone must not activate outside explicit speaking-attempt windows.

No background recording.

---

# 29. Child Listening Cue

When microphone is active, child UI may show a calm cue.

It should not look like:

- surveillance camera;
- recording countdown;
- accuracy meter.

---

# 30. Ordinary Child Speech Not Saved

Raw child attempt audio must not persist.

This is both privacy and child-safety requirement.

---

# 31. Family Recording Safety

Family voice recording:

- parent initiated;
- parent managed;
- local;
- optional.

The child should not accidentally enter recording-management mode.

---

# 32. Parent Gate

Adult controls stay behind a parent gate.

The gate is designed to reduce accidental child access.

It is not strong authentication.

---

# 33. Destructive Controls

Reset/delete must require adult-facing confirmation.

Child mode cannot expose destructive actions.

---

# 34. Purchase Safety

Prototype 0.1 has:

- no ads;
- no IAP;
- no subscription;
- no purchase button.

Therefore child cannot accidentally purchase anything.

---

# 35. External Links

Child mode should contain no external web links.

Parent mode should minimize links.

Any future link should remain adult-facing.

---

# 36. Social Safety

Prototype 0.1 has no:

- chat;
- comments;
- friend requests;
- messaging;
- public profiles;
- user-generated social content.

---

# 37. Stranger Interaction

No feature allows strangers to contact the child.

---

# 38. Camera/Location Safety

Prototype 0.1 does not use:

- camera;
- location.

No visual/location child tracking.

---

# 39. Content Safety

All child content should be curated.

No open user-generated content feed.

---

# 40. Concept Safety

Prototype concepts are familiar and low-risk:

```text
apple
ball
cup
hand
nose
cat
car
water
```

---

# 41. Visual Content Rules

Images should avoid:

- frightening imagery;
- weapons;
- injury;
- unsafe behavior;
- advertising/brands;
- sexual content;
- graphic material.

---

# 42. Audio Content Rules

Canonical/prompts should avoid:

- shouting;
- startling effects;
- manipulative praise;
- threats;
- ridicule;
- emotionally intense sounds.

---

# 43. Brand/Commercial Content

Child screens should not contain branded product placement.

Use generic concept imagery.

---

# 44. Low-Stimulation Safety

Miozira intentionally uses:

- one focal concept;
- warm neutral UI surfaces;
- natural object colors;
- minimal purposeful animation;
- no background music;
- sparse or zero non-word sound effects;
- optional single soft haptic acknowledgement.

The design does **not** attempt to keep the child engaged through sensory intensity.

The child screen should remain visually calm even if that makes it less immediately “exciting” than mainstream children's apps.

The learning itself is responsible for interest.

---

# 45. Flashing

No rapid flashing effects.

---

# 46. Motion

No continuous decorative motion.

Reduced-motion settings should be respected.

---

# 47. Audio Volume

Do not force device volume.

Avoid sudden loudness changes.

---

# 48. Headphones

Miozira should not require headphones.

If family uses them, normal system volume control applies.

---

# 49. Inactivity

If child becomes inactive:

- do not get louder;
- do not rapidly pulse;
- do not repeatedly call for attention.

Use one subtle cue or naturally end.

---

# 50. Attention Capture

Do not use manipulative attention patterns such as:

- fake notifications;
- flashing “come back” cues;
- endless mascot calls.

---

# 51. Error Safety

Child should not see technical errors.

If content/audio/mic fails:

- fallback;
- skip;
- end calmly.

---

# 52. No Blame for Errors

Never phrase technical failure as child action.

Avoid:

> “You didn't say it.”

if microphone failed.

---

# 53. Mic Failure

If mic unavailable:

- child experience continues;
- no warning needed in child mode.

---

# 54. Audio Failure

If canonical audio cannot play:

- do not present silent fake exposure;
- skip/recover;
- parent/developer diagnostics later.

---

# 55. Database Failure

If core state cannot be safely persisted:

- do not continue creating misleading learning evidence;
- end/recover gracefully.

---

# 56. App Crash Risk

Crash prevention is child safety because abrupt failure can create confusion/distress.

Family-test build should pass lifecycle/stress tests.

---

# 57. Orientation Safety

Rotation should not:

- restart audio loudly;
- restart microphone;
- duplicate interaction;
- confuse child with a sudden different screen.

---

# 58. Background/Foreground Safety

Returning from background should not unexpectedly:

- play audio;
- open mic;
- jump concepts.

---

# 59. Screen Lock

Locking the screen stops microphone/audio safely.

---

# 60. Accessibility Safety

Core child flow works without:

- reading;
- speaking;
- precision touch.

This reduces exclusion pressure.

---

# 61. Motor Safety

Large touch regions reduce repeated frustration.

Do not require fine motor precision.

---

# 62. Cognitive Safety

One action, one concept.

Avoid dense instructions and nested navigation.

---

# 63. Language Safety

Language switching should be calm and not imply one language is superior.

---

# 64. Home-Language Respect

Family/home-language variants should be reviewed respectfully.

Do not label natural household forms as “incorrect” merely because formal register differs.

---

# 65. Tamil Register

Tamil lexical choices should receive native/family review.

This avoids exposing the child to unnatural or inappropriate register.

---

# 66. Family Voice

Family recordings should not be used to compare parent pronunciation against a canonical speaker.

No score.

---

# 67. Child Data Visibility to Parent

Parent area may show:

- recent concepts;
- attempts;
- observations.

Avoid surveillance-like granular timelines.

---

# 68. Parent Dashboard Limits

Do not create a dashboard that encourages obsessive tracking.

No:

- percentile;
- daily productivity chart;
- “performance score.”

---

# 69. Parent Language

Use neutral wording.

Good:

> “Tried saying it.”

Avoid:

> “Failed 3 times.”

---

# 70. Resting Difficult Items

Repeated low engagement should cause a concept to rest.

Do not hammer the same difficult item.

---

# 71. Reintroduction

Rested items return gently after time.

No:

> “Let's fix your weak word.”

---

# 72. Preferred Items

Favorites may anchor a session.

Do not overuse them as reward bait.

---

# 73. New Item Limit

Prototype 0.1 limits new-item introduction to reduce cognitive overload.

Recommended adaptive rule:

> at most 2 new pairs per session.

---

# 74. Language Block Safety

Avoid immediate repeated translation chaining such as:

```text
apple English
apple Tamil
apple English
apple Tamil
```

unless future testing intentionally explores it.

---

# 75. Content Quantity

Do not expand beyond the 8×2 prototype before interaction quality is validated.

More content is not automatically safer/better.

---

# 76. Child Choice

Child can:

- tap;
- replay;
- remain silent;
- stop.

Prototype 0.1 does not need complex explicit choice menus.

---

# 77. No Deceptive UI

Do not disguise:

- ads;
- purchases;
- data collection;
- parent actions

as child content.

---

# 78. No Manipulative Reward Schedule

Do not use variable reward mechanics.

---

# 79. No Scarcity

No:

- limited-time reward;
- expiring lesson;
- daily prize.

---

# 80. No Loss Aversion

No:

- losing streak;
- losing hearts;
- losing points.

---

# 81. No Social Comparison

No peer/family rankings.

---

# 82. No Public Sharing

Prototype 0.1 has no share-to-social feature.

---

# 83. No Push Notifications

No child-targeted re-engagement notifications.

---

# 84. No Night-Time Engagement Design

Miozira does not need scheduled nudges.

Parent determines when tablet use is appropriate.

---

# 85. Family-Test Safety Observation

During family testing, adults should watch for:

- frustration;
- repeated accidental taps;
- fear of speaking;
- over-focus on replay;
- resistance to stopping;
- overstimulation;
- confusion after language switching.

---

# 86. Stop Test If Distress

If child appears genuinely distressed or upset by the prototype:

> end the session.

Do not continue testing to “get data.”

---

# 87. Family Test Is Not Experiment on Child

The family test is product validation.

Do not create pressure to complete predetermined exposure quotas.

---

# 88. Parent Observation Questions

Useful:

- Did the child understand what to do?
- Did the child willingly tap/replay?
- Did the child voluntarily vocalize?
- Did the child seem calm?
- Did any interaction cause frustration?
- Did language appear later in real life?

---

# 89. Unsafe Success Metric

Do not use:

> “How long could we keep the child engaged?”

as the north-star metric.

---

# 90. Safer Success Metric

Better:

> “Did the interaction make sense, remain comfortable, and lead to any real-world recognition or use?”

---

# 91. Child Safety Incident Types

Examples:

```text
unexpected continuous mic capture
frightening/loud audio
destructive child-accessible control
reward-pressure behavior
incorrect child data exposure
inappropriate content asset
persistent nagging
```

---

# 92. Incident Response

If a child-safety issue is found:

1. stop using affected build if material;
2. document issue;
3. remove/mitigate root cause;
4. retest before continuing family test.

---

# 93. QA Checklist — Child Safety

Before family test:

- [ ] no score/streak/reward system;
- [ ] no “wrong” response;
- [ ] silence allowed;
- [ ] child can stop anytime;
- [ ] no forced completion;
- [ ] no push notifications;
- [ ] no ads/purchases;
- [ ] microphone optional;
- [ ] no raw child audio stored;
- [ ] no background mic;
- [ ] parent controls gated;
- [ ] reset protected;
- [ ] audio loudness consistent;
- [ ] no flashing;
- [ ] no background music;
- [ ] content reviewed;
- [ ] errors are child-safe.

---

# 94. Child Safety Invariants

### SAFE-INV-001
The child can stop at any time.

### SAFE-INV-002
Speech is optional.

### SAFE-INV-003
Silence is never treated as failure.

### SAFE-INV-004
No pronunciation score exists.

### SAFE-INV-005
No score, streak, XP, lives, or leaderboard exists.

### SAFE-INV-006
No child-facing purchase or ad exists.

### SAFE-INV-007
No background microphone capture occurs.

### SAFE-INV-008
Ordinary child speaking audio is not stored.

### SAFE-INV-009
Parent/destructive controls are separated from child UI.

### SAFE-INV-010
Technical failures do not blame the child.

### SAFE-INV-011
No session-length target is treated as achievement.

### SAFE-INV-012
No social comparison exists.

### SAFE-INV-013
No push notification re-engagement exists.

### SAFE-INV-014
Child content is curated and age-appropriate.

### SAFE-INV-015
Real-world transfer is valued over maximizing screen time.

---

# 95. Open Child-Safety Decisions

Before public launch, confirm:

1. final parent-gate mechanism;
2. exact child acknowledgement phrases;
3. whether any acknowledgement sound is needed;
4. whether active session screen-awake behavior is appropriate;
5. whether family voice is ever used automatically in child mode;
6. whether additional parental controls are needed for public distribution;
7. jurisdiction-specific child-safety/policy review.

---

# 96. Relationship to `child-experience.md`

`child-experience.md` defines the desired emotional feel.

This document makes those expectations safety constraints.

---

# 97. Relationship to `privacy.md`

Privacy safeguards against surveillance and unnecessary child-data retention are part of child safety.

---

# 98. Relationship to `permissions.md`

Microphone permission remains adult-controlled and optional.

---

# 99. Relationship to `adaptive-engine.md`

Adaptive rules must avoid:

- over-repetition;
- punishment;
- backlog pressure.

---

# 100. Relationship to `session-design.md`

Sessions remain:

- short;
- voluntary;
- naturally stoppable.

---

# 101. Relationship to `family-test-protocol.md`

The family test must include child-comfort observations and stop conditions.

---

# 102. Decision Summary

Miozira Prototype 0.1 is safe by design when it:

```text
does not test or shame
does not force speech
does not maximize screen time
does not use rewards/streaks
does not record child speech persistently
does not expose purchases/ads/social features
does not nag
does not compare children
does not hide adult controls in ordinary child interaction
```

Instead it should provide:

```text
calm repetition
clear cause and effect
optional speaking
large forgiving touch
natural stopping
real-world family use
minimal local data
```

The governing rule is:

> **A young child should be able to leave Miozira at any moment with nothing lost, nothing owed, and nothing to feel bad about.**


---

# v0.2 Persuasive-Design Safety Rule

Prototype 0.1 adopts a stronger rule:

> **Any design element intended primarily to increase time-on-app is presumed inappropriate until proven otherwise.**

A feature may be engaging while still being safe if its purpose is intrinsic to the learning activity.

Examples of acceptable intrinsic engagement:

```text
tap → object responds
word → child imitates
car → brief meaningful roll
water → brief meaningful pour
real object → later recognition at home
```

Examples of prohibited attention engineering:

```text
random surprise reward
bright pulsing CTA
“one more” prompt
celebration loop
progress treasure
mascot emotional pressure
reward vibration
timer/race
```
