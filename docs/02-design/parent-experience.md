# Miozira — Parent Experience Specification

**Document:** `parent-experience.md`  
**Version:** 0.1  
**Status:** Draft for Prototype 0.1  
**Product:** Miozira  
**Prototype:** Android-first, 8 concepts × English + Tamil  
**Primary adult user:** parent/caregiver  
**Last updated:** 2026-08-25

---

## 1. Purpose

This document defines the parent/caregiver experience for Miozira Prototype 0.1.

It covers:

- initial setup;
- parent gate;
- microphone explanation and consent;
- language settings;
- recent learning;
- real-world suggestions;
- recognition/use reporting;
- family voice recordings;
- privacy messaging;
- local-data reset;
- parent-facing wording;
- parent burden limits.

The parent experience exists to support the child experience.

It must not turn Miozira into:

- a school dashboard;
- a grading system;
- a homework tracker;
- a surveillance tool;
- a parent productivity app.

---

# 2. Parent Experience North Star

The parent should feel:

> **“I understand what Miozira is doing, I can help lightly, and I can notice what happens in real life without managing a complicated learning system.”**

The parent should not feel:

> **“I need to monitor, grade, and optimize my child.”**

---

# 3. Parent Responsibilities in Prototype 0.1

The parent has five main roles:

1. complete initial adult setup;
2. grant or decline microphone permission;
3. optionally add family voice recordings;
4. notice/report real-world recognition or use;
5. occasionally use the real-world suggestion.

Everything else should be handled by Miozira.

---

# 4. Parent Experience Principles

## PE-P01 — Minimal burden

Parent actions should take seconds, not minutes.

## PE-P02 — Evidence, not judgment

Show observations and activity, not scores.

## PE-P03 — Explain sensitive features clearly

Microphone and local recording behavior must be understandable.

## PE-P04 — Keep family life natural

Real-world suggestions must not become homework.

## PE-P05 — Child controls stay protected

Adult settings remain behind a deliberate parent gate.

## PE-P06 — Privacy visible by design

Parents should not need to search legal text to understand that Prototype 0.1 is local/offline.

## PE-P07 — No micromanagement of adaptation

Parents should not manually configure detailed scheduling rules.

---

# 5. Initial Parent Setup

Before normal child use, Miozira may present a short adult setup flow.

Recommended flow:

```text
1. What Miozira is
2. Languages
3. Microphone explanation
4. Family voice option
5. Start child mode
```

This should remain brief.

---

# 6. Setup Step 1 — Product Explanation

Recommended parent-facing summary:

> Miozira helps young children connect familiar things with spoken words in more than one language. Sessions are short, offline, and designed without scores, streaks, or pronunciation grading.

Avoid long onboarding essays.

A short “Learn more” link inside the parent area may provide additional detail.

---

# 7. Setup Step 2 — Languages

Prototype 0.1 supports:

- English;
- Tamil.

The parent should be able to see both languages clearly.

Possible setting:

```text
Starting language
[ English ]
[ Tamil   ]
```

Both remain available to the session system.

---

# 8. Starting Language

The starting language affects the beginning of early sessions.

It does not mean the other language is disabled.

Recommended explanation:

> “Choose the language Miozira should begin with. Both English and Tamil can still appear.”

---

# 9. Setup Step 3 — Microphone Explanation

Before Android permission is requested, Miozira should explain the feature in plain language.

Recommended wording:

> **Speaking attempts**
>
> Miozira can briefly use the microphone to notice whether your child tried to speak.
>
> It does not:
> - grade pronunciation;
> - convert speech to text;
> - upload child audio;
> - save ordinary speaking attempts.
>
> Miozira still works if you leave this off.

Actions:

```text
[Enable microphone]
[Not now]
```

---

# 10. Microphone Consent Principles

The microphone feature must be:

- optional;
- understandable;
- reversible;
- non-blocking.

The parent must be able to disable it later.

---

# 11. Permission Denied

If Android permission is denied:

Parent UI may say:

> “Speaking-attempt detection is off. Miozira still works normally.”

Do not repeatedly pressure the parent to enable it.

---

# 12. Setup Step 4 — Family Voice

Prototype 0.1 may introduce family voice as optional.

Recommended explanation:

> “You can record your own voice for familiar home-language words. These recordings stay on this device.”

Actions:

```text
[Record later]
[Try it now]
```

Family recording must not block setup completion.

---

# 13. Setup Completion

After setup:

- enter child mode;
- no account creation;
- no email verification;
- no cloud sync setup;
- no subscription wall.

---

# 14. Parent Gate

The parent area must require an intentional adult action.

Recommended Prototype 0.1 candidate:

> long-press an unobtrusive corner hotspot for approximately 2 seconds.

Then:

```text
Parent area
[Continue]
```

The final interaction may evolve after testing.

---

# 15. Parent Gate Goals

The gate should:

- prevent accidental entry;
- remain easy for an adult;
- not look like a child reward;
- work offline;
- require no account/password.

---

# 16. Parent Gate Must Not Become a Game

Avoid gates such as:

- solve a colorful puzzle;
- match animals;
- answer a child-friendly math question;
- drag shapes.

These can attract children rather than separate adult controls.

---

# 17. Parent Home

Recommended Prototype 0.1 parent home:

```text
Parent Home
│
├── Try this outside Miozira
├── Recent learning
├── Recognized / Used outside Miozira
├── Family recordings
└── Settings & privacy
```

This may be one vertically scrolling screen.

---

# 18. Parent Home Priority

Information order should reflect Miozira's philosophy.

Recommended priority:

1. real-world suggestion;
2. quick observation actions;
3. recent activity;
4. recordings/settings.

Not:

1. time spent;
2. lessons completed;
3. percentage progress.

---

# 19. Real-World Suggestion Card

The suggestion should be prominent but lightweight.

Example:

> **Try this outside Miozira**
>
> At snack time, say the Tamil word for apple while handing over a slice.

No completion checkbox is required.

---

# 20. Suggestion Actions

Prototype 0.1 does not need:

- “Mark done”;
- reminders;
- scheduling;
- sharing.

The parent can simply read it and use it naturally.

---

# 21. Suggestion Persistence

The latest suggestion should remain visible until:

- a new meaningful session creates another suggestion; or
- implementation-defined expiration.

It should not disappear immediately after the session.

---

# 22. Suggestion Repetition

Avoid presenting the exact same suggestion after every session.

The local system should rotate:

- concepts;
- contexts;
- wording where curated.

No generative AI is required.

---

# 23. Recent Learning

Recent learning should show evidence-oriented summaries.

Example:

```text
Apple — Tamil
Heard recently
Tried saying it

Ball — English
Replayed
```

Avoid:

```text
Apple — 72%
Ball — Mastered
```

---

# 24. Parent Vocabulary

Preferred wording:

- Heard
- Heard recently
- Tried saying it
- Replayed
- Recognized outside Miozira
- Used outside Miozira
- Coming back later

Avoid:

- Failed
- Weak
- Incorrect
- Behind
- Mastered
- Fluent
- Poor pronunciation

---

# 25. Evidence Detail Level

Prototype 0.1 should avoid overwhelming the parent with event logs.

Default view should summarize.

Optional detail may show:

```text
Apple — Tamil
Heard 3 times
Last heard yesterday
Used outside Miozira
```

Even here, counts should remain descriptive rather than competitive.

---

# 26. No Parent Score

Do not show:

- total score;
- daily score;
- mastery percentage;
- progress rank;
- language percentage;
- comparison to norms.

---

# 27. Real-World Observation Entry

The parent should be able to record:

```text
[Recognized outside Miozira]
[Used outside Miozira]
```

for a relevant concept-language pair.

---

# 28. Observation Entry Speed

The action should take only a few taps.

Recommended:

1. open parent area;
2. find recent concept;
3. tap recognition/use;
4. confirmation appears.

No form should be required.

---

# 29. Observation Confirmation

Recommended:

> “Saved.”

Optional:

> “Miozira will use this as learning evidence.”

Avoid:

> “Your child mastered this word.”

---

# 30. Observation History

Prototype 0.1 may show a short chronological history.

Example:

```text
Today
Car — Tamil
Used outside Miozira

Yesterday
Ball — English
Recognized outside Miozira
```

---

# 31. Prompted vs Spontaneous Use

The learning model distinguishes these concepts.

Prototype 0.1 parent UI may keep one simple button:

```text
Used outside Miozira
```

If later testing shows value, the parent flow may expand to:

```text
Used after a prompt
Used spontaneously
```

Do not add this complexity before evidence supports it.

---

# 32. Parent Should Not Be Asked to Log Everything

Miozira should not ask the parent to record every:

- exposure;
- attempt;
- replay;
- word spoken at home.

Observation logging is occasional and lightweight.

---

# 33. Family Recording Area

Recommended structure:

```text
Family recordings

Apple — Tamil
Standard voice [Play]
Family voice   [Record / Play / Replace / Delete]

Ball — Tamil
...
```

The UI may filter by language.

---

# 34. Family Recording Flow

Recommended flow:

1. select concept-language pair;
2. listen to canonical audio if desired;
3. tap Record;
4. record one short word;
5. preview;
6. save, retry, or delete.

---

# 35. Family Recording Guidance

Before recording:

> “Say the word naturally and clearly. One short recording is enough.”

No studio-quality setup is required.

---

# 36. Recording Duration

The interface should stop or limit unusually long recordings.

Exact maximum duration belongs in `audio.md`.

For a single-word recording, only a few seconds should be necessary.

---

# 37. Family Recording Privacy

The parent UI should state:

> “Family recordings stay on this device.”

Do not imply:

- backup;
- cloud storage;
- sharing.

---

# 38. Canonical vs Family Voice

The parent should understand that family voice is additional.

Recommended labels:

```text
Standard voice
Family voice
```

Avoid:

```text
Correct voice
Your voice
```

because that may imply family speech is less valid.

---

# 39. Family Recording Delete

Deleting family audio:

- requires deliberate action;
- should not delete canonical audio;
- should not delete learning history.

---

# 40. Settings

Prototype 0.1 settings should remain small.

Recommended:

```text
Languages
Starting language
Speaking-attempt detection
Family recordings
Privacy
Reset Miozira data
```

No advanced adaptive controls.

---

# 41. No Adaptive Micromanagement

Do not expose settings such as:

- “Set repetition interval to 3 days”;
- “Mastery threshold = 75%”;
- “Increase Tamil by 18%”;
- “Difficulty multiplier.”

The adaptive engine remains product logic.

---

# 42. Parent May Influence Language Context

Parent controls may allow:

- starting language;
- temporarily enabling/disabling one language for testing if needed.

If this exists, explain that it changes session content, not child ability.

---

# 43. Privacy Screen

Prototype 0.1 privacy messaging should summarize:

- fully local learning history;
- no account;
- no ads;
- no behavioral analytics;
- no child-audio upload;
- family recordings stay local;
- microphone is optional;
- local data can be reset.

A longer formal privacy policy may exist separately.

---

# 44. Privacy Wording

Prefer plain language.

Example:

> “Miozira Prototype 0.1 keeps learning data on this device.”

Avoid relying solely on legal wording such as:

> “Data processing occurs under legitimate interest...”

---

# 45. Data Reset

Reset should be available only in parent mode.

Recommended flow:

```text
Reset Miozira data
↓
Explanation
↓
Explicit confirmation
```

---

# 46. Reset Explanation

The parent should know what will be deleted.

Example:

> This will remove:
> - local learning history;
> - real-world observations;
> - family recordings;
> - prototype settings.
>
> Standard Miozira content will remain.

---

# 47. Reset Confirmation

A destructive action should require deliberate confirmation.

Possible:

```text
[Cancel]
[Reset data]
```

Optional second confirmation is acceptable if testing shows accidental resets are possible.

---

# 48. No Account Recovery Promise

Because Prototype 0.1 has no cloud account:

- deleted data cannot be recovered from Miozira servers;
- app reinstall may remove local data depending on platform behavior.

This should be explained plainly where relevant.

---

# 49. Parent Error States

Parent-facing errors should explain:

1. what happened;
2. what still works;
3. what can be done.

Example:

> “The family recording couldn't be played. The standard voice is still available. You can record it again.”

---

# 50. Microphone Error State

Example:

> “Miozira can't access the microphone right now. Speaking-attempt detection is off, but learning sessions still work.”

---

# 51. Empty Recent Activity

Before the first session:

> “Recent learning will appear here after your child uses Miozira.”

No pressure to begin immediately.

---

# 52. Empty Real-World Observation History

Use a neutral empty state.

Example:

> “When you notice a word being recognized or used outside Miozira, you can save it here.”

---

# 53. Parent Education

Prototype 0.1 may include a short “How Miozira works” section.

Recommended topics:

- audio-first learning;
- no pronunciation grading;
- repetition is spaced;
- silence is okay;
- real-world use matters;
- sessions can end anytime.

Keep this brief.

---

# 54. Parent Guidance — What to Do

Useful guidance:

- let the child explore;
- use words naturally;
- notice recognition/use;
- keep real-world suggestions casual;
- allow early stopping.

---

# 55. Parent Guidance — What Not to Do

Avoid instructing parents to:

- force repetition;
- correct every sound;
- finish every session;
- test repeatedly;
- compare languages;
- compare the child to other children.

---

# 56. Parent During Child Session

The parent may:

- sit nearby;
- smile/respond;
- repeat naturally;
- help if genuinely needed.

The parent should not need to:

- tap every item;
- explain every screen;
- tell the child when to speak.

---

# 57. Co-Use

Miozira should support but not require co-use.

A parent present during a session can enrich the interaction.

The product should not assume constant adult supervision for each tap after setup.

---

# 58. Parent Burden Limits

Prototype 0.1 should avoid requiring parents to:

- create lesson plans;
- choose daily words;
- log minutes;
- grade attempts;
- review detailed charts;
- manage schedules;
- maintain profiles.

---

# 59. Parent Notifications

Prototype 0.1 does not require push notifications.

Avoid engagement reminders such as:

> “Your child hasn't practiced today.”

---

# 60. No Guilt Messaging

Never say:

- “You missed yesterday.”
- “Your child is falling behind.”
- “Practice now to keep progress.”
- “Only 5 minutes today!”

The parent experience must support family flexibility.

---

# 61. No Comparison Messaging

Do not compare:

- siblings;
- peers;
- age norms;
- languages;
- families.

Prototype 0.1 is not normed for developmental comparison.

---

# 62. Parent Language Preferences

The parent UI may initially remain in English for Prototype 0.1.

Child content still supports English and Tamil.

A future version may localize the parent experience.

---

# 63. Tamil Content Review in Parent Experience

Where Tamil words appear in parent mode:

- show approved Tamil script;
- avoid unreviewed transliteration as primary display;
- clearly indicate target language.

---

# 64. Real-World Suggestion Language

If the parent UI is English but the target pair is Tamil:

Recommended:

```text
Apple — Tamil
ஆப்பிள்

At snack time, say this naturally while handing over a slice.
```

Exact presentation belongs in final UI design.

---

# 65. Parent Home Wireframe

```text
┌────────────────────────────────────┐
│ Miozira — Parent                   │
├────────────────────────────────────┤
│ Try this outside Miozira           │
│                                    │
│ Apple — Tamil                      │
│ ஆப்பிள்                            │
│                                    │
│ At snack time, say it naturally    │
│ while handing over a slice.        │
├────────────────────────────────────┤
│ Recent learning                    │
│                                    │
│ Apple — Tamil                      │
│ Heard recently · Tried saying it   │
│                                    │
│ [Recognized outside Miozira]       │
│ [Used outside Miozira]             │
├────────────────────────────────────┤
│ Family recordings                  │
│ Settings & privacy                 │
└────────────────────────────────────┘
```

---

# 66. Microphone Settings Wireframe

```text
┌────────────────────────────────────┐
│ Speaking attempts                  │
├────────────────────────────────────┤
│ Miozira briefly uses the           │
│ microphone to notice whether your  │
│ child tried to speak.              │
│                                    │
│ It does not:                       │
│ • grade pronunciation              │
│ • transcribe speech                │
│ • upload child audio               │
│ • save ordinary attempts           │
│                                    │
│ [ On / Off ]                       │
└────────────────────────────────────┘
```

---

# 67. Family Recording Wireframe

```text
┌────────────────────────────────────┐
│ Apple — Tamil                      │
│ ஆப்பிள்                            │
├────────────────────────────────────┤
│ Standard voice                     │
│ [Play]                             │
│                                    │
│ Family voice                       │
│ [Record]                           │
│                                    │
│ after recording:                   │
│ [Play] [Record again] [Delete]     │
└────────────────────────────────────┘
```

---

# 68. Real-World Observation Wireframe

```text
┌────────────────────────────────────┐
│ Apple — Tamil                      │
│ ஆப்பிள்                            │
├────────────────────────────────────┤
│ What did you notice?               │
│                                    │
│ [Recognized outside Miozira]       │
│                                    │
│ [Used outside Miozira]             │
│                                    │
│ [Cancel]                           │
└────────────────────────────────────┘
```

---

# 69. Parent Experience Acceptance Criteria

Before family testing:

- parent setup can be completed without account creation;
- parent understands the two-language prototype;
- microphone behavior is understandable;
- parent may decline microphone use;
- parent can intentionally enter parent mode;
- child cannot easily enter parent mode accidentally;
- current real-world suggestion is understandable;
- recognition/use can be saved quickly;
- family recording can be created/deleted;
- reset behavior is clear;
- no score/mastery language appears.

---

# 70. Family-Test Parent Questions

Ask after several days:

1. Was the parent area easy to enter?
2. Was it too easy for the child to discover?
3. Did microphone messaging feel clear?
4. Did the parent understand what data stayed local?
5. Were real-world suggestions useful?
6. Did suggestions feel like homework?
7. Did the parent actually use them?
8. Was logging recognition/use easy enough?
9. Did the parent remember to log observations?
10. Was family recording useful?
11. Did recent learning information feel meaningful?
12. Was anything missing that would have helped?
13. Did anything feel like unnecessary monitoring?

---

# 71. Parent Experience Warning Signs

Review the product if:

- parent feels obligated to log everything;
- parent asks for a “score” because current wording is unclear;
- microphone behavior feels suspicious or ambiguous;
- child regularly reaches parent controls;
- real-world suggestions feel like assignments;
- family recording is confusing;
- parent cannot tell whether canonical or family voice is playing;
- privacy controls are hard to find;
- reset consequences are unclear.

---

# 72. Parent Experience Invariants

### PE-INV-001
No account is required.

### PE-INV-002
No child score is shown.

### PE-INV-003
No pronunciation score is shown.

### PE-INV-004
Microphone use is optional.

### PE-INV-005
Microphone denial does not block child learning.

### PE-INV-006
Family recordings remain local.

### PE-INV-007
Parent observations remain parent-reported evidence.

### PE-INV-008
Real-world suggestions are optional.

### PE-INV-009
Parents are not required to mark suggestions complete.

### PE-INV-010
No daily quota or guilt messaging exists.

### PE-INV-011
No developmental comparison is shown.

### PE-INV-012
Destructive controls remain inside parent mode.

### PE-INV-013
Data reset requires explicit confirmation.

### PE-INV-014
Parent UI wording uses evidence rather than mastery claims.

---

# 73. Relationship to `ui-ux.md`

`ui-ux.md` defines:

- layout;
- navigation;
- components;
- visual hierarchy.

This document defines the parent workflow and information meaning.

---

# 74. Relationship to `real-world-transfer.md`

`real-world-transfer.md` defines:

- recognition;
- use;
- suggestion behavior.

This document defines how the parent sees and records those things.

---

# 75. Relationship to `microphone.md`

`microphone.md` must implement the privacy promises made here:

- short listening windows;
- no cloud processing;
- no transcription;
- no permanent raw child-audio storage;
- graceful denial behavior.

---

# 76. Relationship to `privacy.md`

`privacy.md` will formally document:

- local data categories;
- permissions;
- retention;
- deletion;
- third-party dependencies;
- data flows.

The parent experience should summarize those facts in plain language.

---

# 77. Relationship to `database.md`

The database must support parent-facing functionality for:

- recent concept evidence;
- real-world observations;
- family audio metadata;
- language settings;
- microphone setting;
- reset behavior.

---

# 78. Decision Summary

Prototype 0.1 parent experience is intentionally small.

The parent should be able to:

1. understand Miozira quickly;
2. choose the starting language;
3. understand and optionally enable microphone detection;
4. optionally record family voice;
5. enter a protected parent area;
6. see one real-world suggestion;
7. view lightweight recent learning evidence;
8. mark recognition/use outside Miozira;
9. manage family recordings;
10. understand privacy;
11. reset local data.

The parent should **not** need to:

- grade;
- plan lessons;
- track daily targets;
- review complex charts;
- manage adaptation;
- force completion.

The parent experience should support the child quietly rather than turning family life into a learning-management workflow.
