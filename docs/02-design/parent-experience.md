# Miozira — Parent Experience Specification

**Document:** `parent-experience.md`  
**Version:** 0.1  
**Status:** Draft for Prototype 0.1  
**Product:** Miozira  
**Prototype:** Android-first, 8 concepts × five languages (English, Tamil, Japanese, Arabic, Spanish)  
**Primary adult user:** parent/caregiver  
**Last updated:** 2026-11-17

---

## Revision — 2026-11-17

Revised to match the approved UI/UX design, `docs/superpowers/specs/2026-11-16-ui-ux-design.md`, which is the source of truth
for screens and wins wherever the two differ.

Changed in place: five languages with no language setup; the two-step parent
gate; a parent home in sentences with four words for today; no counts, dates or
durations; the Voices area. Wherever this document says "suggestion", read "the
four words for today"; wherever it says "family recording", read "voice
recording" (guide phrases by both parents, and Tamil words). New parent
functions are listed in the addendum at the end.

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

Miozira uses five languages: English, Tamil, Japanese, Arabic and Spanish, with equal standing.

There is nothing to choose. Each session has two of them, and the pairing rotates per completed session, so a skipped day never skips a language. Setup simply explains this.

---

# 8. Starting Language (retired)

Retired 2026-11-17. The rotation decides which two languages a session uses; there is no starting-language setting.

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

The parent area requires two intentional adult actions:

1. hold the top-right corner for 3 seconds. Nothing is drawn there and the hold shows no progress;
2. type a two-digit number that is written as a word, which changes every time.

```text
Parents only
Type this number:
forty-seven
[number pad]
```

A wrong answer, or 10 seconds of nothing, returns silently to the child screen. The area locks again when the parent leaves it or the screen turns off. A parent-chosen PIN replaces step 2 once the child can read number words.

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

```text
Parent Home
│
├── Four words for today
├── Most recent session
├── Noticed at home
├── How each language is going
├── How the puzzles are going
├── Voices
├── Start an extra session
└── Microphone and privacy
```

This is one vertically scrolling screen.

---

# 18. Parent Home Priority

Information order should reflect Miozira's philosophy.

Recommended priority:

1. four words for today;
2. the most recent session, in sentences;
3. noticed-at-home actions;
4. voices and settings.

Never:

1. time spent;
2. lessons completed;
3. percentage progress;
4. counts, dates or missed days.

---

# 19. Four Words for Today

Four words from the most recent session, each with the moment of the day to say it.

Example:

> **Four words for today**
>
> At breakfast, when you pour: water (Tamil)
> At snack time, handing a slice: apple (Tamil)
> At bath time: nose (English)
> At dinner, passing it: cup (English)

No completion checkbox. No reminders: the card is seen only when a parent opens the parent area.

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
- He understood it
- He said it
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

The parent sees sentences, never counts.

```text
Most recent session: English and Tamil
Tried saying: apple, ball, cup, water (Tamil)
Said before the app did: ball, cup (Tamil)
Listened without speaking: nose, cat
```

No numbers ("Heard 3 times"), dates ("Last heard yesterday") or durations appear anywhere. Words such as "most", "some" and "not yet" stand in for counts, and sessions are "most recent" and "before that".

A quiet turn is "listened without speaking", never "missed".

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
[He understood it]
[He said it]
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
He said it

Yesterday
Ball — English
He understood it
```

---

# 31. Prompted vs Spontaneous Use

The learning model distinguishes these concepts.

Prototype 0.1 parent UI may keep one simple button:

```text
He said it
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

# 33. Voices Area

Two lists.

```text
Guide phrases in Malayalam

Your turn
Mother   Recorded            [Play] [Record again]
Father   Not recorded yet    [Record]

What's gone?
...
```

```text
Tamil words

Apple    Recorded            [Play] [Record again] [Delete]
Ball     Not recorded yet    [Record]
...
```

Both parents record every guide phrase. The app uses one parent's voice for a whole session and the other's for the next; a prepared voice is used only until a recording exists.

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
Speaking-attempt detection
Voices
Start an extra session
Privacy
Erase everything on this tablet
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

Since 2026-11-17 the parent has no language controls: all five languages are always on and the rotation decides which two a session uses.

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

> “When you notice a word being understood or said at home, you can save it here.”

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

Child content is in five languages: English, Tamil, Japanese, Arabic and Spanish.

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
│ [He understood it]       │
│ [He said it]             │
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
│ [He understood it]       │
│                                    │
│ [He said it]             │
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
2. see that five languages rotate, with nothing to set up;
3. understand and optionally enable microphone detection;
4. record the guide phrases and Tamil words;
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

---

# Addendum 2026-11-17 — New Parent Functions

Defined in `docs/superpowers/specs/2026-11-16-ui-ux-design.md` §10.

- **Start an extra session.** One confirmation ("Start another session now?"); the parent area then locks and the child's first picture is waiting. Without this, the app stays on its quiet screen for the rest of a day whose session is finished.
- **How each language is going.** One short paragraph per language, for example: "Tamil — he tries most of his Tamil words. Said before the app did: cup, water. Still just listening: nose."
- **How the puzzles are going.** One sentence per puzzle kind he has met, for example: "What's gone? — usually joins in."
- **Noticed at home.** The two observation buttons are labelled "He understood it" and "He said it".
- **Words taught at home.** Feelings words and family words are not pictured in the app (they cannot be shown without faces); the parents teach them themselves.
