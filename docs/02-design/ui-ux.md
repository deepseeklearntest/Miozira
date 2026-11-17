# Miozira — UI/UX Specification

**Document:** `ui-ux.md`  
**Version:** 0.2  
**Status:** Draft for Prototype 0.1 — Palette & Wellbeing Update  
**Product:** Miozira  
**Prototype:** Android-first, 8 concepts × five languages (English, Tamil, Japanese, Arabic, Spanish)  
**Target child:** approximately 4–5 years old  
**Supported orientations:** Landscape only (locked)  
**Last updated:** 2026-11-17

---

## Revision — 2026-11-17

Revised to match the approved UI/UX design, `docs/superpowers/specs/2026-11-16-ui-ux-design.md`, which is the source of truth
for screens and wins wherever the two differ.

Changed in place: five languages; landscape only; one written word under the
picture; "Your turn" in a parent's Malayalam voice after every word; no language
cue; voices as the only sounds; the two-step parent gate; a parent home written
in sentences with four words for today; the Voices screen.

Added by reference (sections 89–91): "say it first", "Which one?", the thinking
puzzles, the scene, and the picture rules.

---

## 1. Purpose

This document defines the user interface and interaction rules for Miozira Prototype 0.1.

It translates the decisions in:

- `product-vision.md`
- `requirements.md`
- `non-functional-requirements.md`
- `learning-model.md`
- `adaptive-engine.md`
- `session-design.md`
- `content-spec.md`
- `real-world-transfer.md`

into a concrete screen and interaction model.

The UI/UX has two clearly separated experiences:

1. **Child Experience**
2. **Parent Experience**

The child experience must remain extremely simple.

The parent experience may use normal adult UI patterns.

---

# 2. UX North Star

The child should understand Miozira primarily by interacting with it, not by reading instructions.

The central interaction is:

> **See → Tap → Hear → Try if wanted → Receive a gentle response → Continue**

The interface should feel:

- calm;
- obvious;
- forgiving;
- non-competitive;
- safe;
- low-stimulation;
- predictable.

---

# 3. UI Principles

## UX-P01 — One primary action

Each child-facing screen should have one dominant intended interaction.

## UX-P02 — One dominant concept

One concept should visually own the screen.

## UX-P03 — No reading dependency

Text must never be required for child operation. One written word is shown under the picture in the language's own script; it is there for familiarity only and is never a tap target.

## UX-P04 — No precision interaction

The child should not need to accurately hit small controls.

## UX-P05 — No performance judgment

The UI must not visually communicate:

- right/wrong;
- score;
- rank;
- percentage;
- stars;
- levels;
- failure.

## UX-P06 — Calm motion

Motion supports comprehension.

Motion does not exist merely to keep attention.

## UX-P07 — Predictable repetition

The same action should produce the same kind of outcome.

## UX-P08 — Child and adult controls remain separate

Parent controls should not leak into the ordinary child flow.

---

# 4. Information Architecture

The structure stays extremely shallow.

```text
App Launch
   |
   +-- Child Mode
   |     |
   |     +-- Session (word screen, "Which one?", one puzzle, ending)
   |     |
   |     +-- Quiet screen
   |
   +-- Parent Gate (hidden corner + reading step)
         |
         +-- Parent Home (four words for today, most recent session, noticed at home)
         +-- How each language is going
         +-- How the puzzles are going
         +-- Voices (guide phrases, Tamil words)
         +-- Start an extra session
         +-- Microphone and privacy (including erase)
```

There is no child-facing navigation hierarchy.

---

# 5. App Launch

## UX-001 — Child-first launch

Normal launch should prioritize entering the child learning experience quickly.

Avoid a conventional app home containing:

- tabs;
- lesson lists;
- profile selectors;
- settings icons;
- course cards.

---

## UX-002 — Branding restraint

A Miozira logo or brand mark may appear briefly.

It should not delay access through a long animated intro.

---

# 6. Child Learning Screen

The word screen is the primary product screen. It contains:

1. the plain background;
2. one dominant concept picture, which is the tap target;
3. one written word beneath the picture, in the language's own script;
4. a temporary "his turn" ring around the picture;
5. the hidden parent corner (nothing is drawn there).

No other child controls exist. Full behaviour: UI/UX design §5.2 and §6.

---

# 7. Child Screen Layout — Portrait (retired)

Retired 2026-11-17. The app is locked to landscape; there is no portrait layout.

---

# 8. Child Screen Layout — Landscape

The only layout.

```text
┌──────────────────────────────────┐
│            ╭ ─ ─ ─ ─ ╮           │
│           ¦  CONCEPT  ¦          │
│            ╰ ─ ─ ─ ─ ╯           │
│               word               │
└──────────────────────────────────┘
```

The picture is centred and about 60% of the screen's height. The written word is
centred beneath it. The ring belongs to the picture; the word always sits below
the ring with a clear gap, and the two never touch or overlap.

---

# 9. Layout Rules

## UX-003 — Preserve hierarchy

Every child screen preserves:

1. concept dominance;
2. large touch region;
3. uncluttered background;
4. picture first, written word beneath it.

---

## UX-004 — No hardcoded pixel placement

Layout should use adaptive constraints rather than fixed coordinates for one tablet model.

---

## UX-005 — Landscape lock

The screen is locked to landscape and does not turn.

If the screen is ever rebuilt for another reason:

- active concept stays the same;
- active language stays the same;
- session remains in the same state;
- no duplicate exposure is recorded;
- nothing is replayed.

---

# 10. Primary Concept Visual

## UX-006 — Large visual

The concept visual should typically occupy approximately:

> **55–75% of the shorter usable screen dimension**

depending on the asset and orientation.

This is a design starting point, not an immutable rule.

---

## UX-007 — Dominant object only

The primary concept must remain visually more prominent than:

- prompts;
- status indicators;
- decorative elements;
- parent-entry affordances.

---

## UX-008 — No small play button required

The child taps the concept itself.

The concept visual is the play control.

---

# 11. Touch Target

## UX-009 — Forgiving hit area

The active tap target should cover at least the visible concept and preferably include safe surrounding space.

Prototype target:

> the effective tap region should be substantially larger than the minimum adult Android touch target.

---

## UX-010 — Avoid accidental neighboring targets

Ordinary child mode should not place another small interactive target immediately beside the concept.

---

## UX-011 — Touch debounce

Rapid repeated touches must not create:

- overlapping audio;
- duplicate exposure records;
- broken animation state.

The UI may debounce or serialize repeated touches.

---

# 12. Child Interaction States

The UI should represent these internal states:

```text
PRESENTING
READY
PLAYING_AUDIO
INVITING
LISTENING
RESPONDING
TRANSITIONING
IDLE
```

The child does not see these labels.

---

# 13. PRESENTING State

When a new concept enters:

- image appears;
- no instructional text is required;
- brief entry motion may occur;
- touch becomes available promptly.

Recommended entry motion:

- soft fade;
- slight scale-in.

Avoid:

- spinning;
- bouncing repeatedly;
- flying across the screen;
- confetti.

---

# 14. READY State

The concept is stable and tappable.

If the child does nothing for several seconds, Miozira may provide one subtle hint.

Possible hint:

- gentle pulse;
- soft halo;
- slight scale movement.

Do not use:

- flashing;
- shaking;
- urgent sound;
- repeated “Tap me!”

---

# 15. PLAYING_AUDIO State

After tap:

- concept gives immediate visual acknowledgement;
- canonical word begins;
- listening prompt is not shown until the target audio finishes or reaches its intended end.

Recommended visual feedback:

- soft highlight;
- tiny scale pulse;
- brief glow.

---

# 16. INVITING State

After every word, "Your turn" is spoken once in Malayalam in a parent's recorded voice.

It is always the same short, warm phrase. It is never repeated, never louder and never followed by another prompt.

(Decided 2026-11-17: to this child the phrase is company, not pressure. This replaces the earlier advice to minimise spoken prompts.)

For familiar words the order is reversed ("say it first"): the picture appears with no written word, the ring shows in silence for about 3 seconds, then the app says the word. See UI/UX design §5.3.

---

# 17. His-Turn Cue

## UX-012 — One calm ring

During his turn one calm ring (`#607C8A`) breathes slowly around the picture for about 4 seconds.

It means "you may speak", not "you are being recorded". It shows whether or not the microphone is on.

It must not display:

- audio waveform;
- decibel meter;
- microphone icon;
- recording countdown;
- pronunciation score;
- red/green accuracy bar.

---

## UX-013 — The ring is temporary

The ring fades when the turn ends. It never touches or overlaps the written word.

---

# 18. RESPONDING State

The child receives a small acknowledgement.

The only visual response is the small nod described in §19: the picture grows very slightly and settles, once. No halo, glow or added decoration.

Avoid turning the concept itself into a mascot unless separately approved.

---

# 19. Response for Detected Attempt

The picture gives one small warm nod: a slight grow-and-settle. No sound and no colour.

The nod is the same whatever he said. The app can only tell that he made a sound; it never judges how a word was said, now or later.

Avoid:

```text
✓ Correct
100%
Perfect!
```

---

# 20. Response for No Attempt

The ring fades and, about a second later, the next picture appears. Nothing else happens.

Do not show:

```text
X
Try again
You didn't answer
```

---

# 21. TRANSITIONING State

The current concept leaves calmly and the next appears.

Recommended motion:

- crossfade;
- short slide of only a few pixels;
- soft scale transition.

Transition target:

> approximately 300–700 ms.

---

# 22. IDLE State

If the child stops interacting:

- UI remains calm;
- animation should not intensify;
- microphone is not continuously active;
- no nagging sequence begins.

After sustained inactivity the app may end the session naturally.

---

# 23. Replay Interaction

The child taps the concept again.

The system should:

- replay the current spoken form;
- remain on the same concept;
- not reveal a separate replay screen;
- not create a score or replay counter.

---

# 24. Audio Playback Visual State

The visual may make it clear that audio is playing.

Recommended:

- gentle pulse synchronized roughly to playback;
- temporary highlight.

Avoid animated lip-sync characters unless separately designed and tested.

---

# 25. Language Switching UX

Settled 2026-11-17: there is no child-facing language cue of any kind.

Each session has two of the five languages in separate blocks. The app simply switches; the script of the written word is the only visible difference. He hears the word and links it to the picture.

The earlier options B (visual cue) and C (spoken cue) are dropped.

---

# 26. Do Not Use Flags as Primary Language Identity

National flags should not be the main way Miozira represents language.

Reasons:

- languages are not equivalent to one country;
- all five languages span many regional/cultural contexts;
- future languages may have multiple country variants.

Parent UI may use written language names.

---

# 27. Movement Interaction UX

When a fitting word is in the day's set, the puzzle part opens with one movement prompt in a parent's Malayalam voice beside the picture.

Example:

```text
[nose picture]
"Touch your nose."
```

The app waits about 5 seconds and moves on. Nothing is checked and no score screen appears.

---

# 28. Natural Session End

A session is a planned set of items, not a length of time. It ends when the set is done. Nothing counts down and no word is ever cut off.

The end is:

1. one familiar favourite (later, the scene);
2. one closing phrase in a parent's voice, such as "all done, off you go" — never "see you tomorrow";
3. a slow fade into the quiet screen.

It never ends with:

- stars;
- confetti;
- “Lesson complete!”;
- progress wheel;
- daily streak.

---

# 29. Quiet Screen

Plain `#F2EEE7` with nothing on it. Taps do nothing and make no sound. After about 30 seconds the tablet is allowed to dim and sleep.

It is shown:

- after a session ends;
- after about a minute without a touch mid-session (a tap later the same day carries on where he left off);
- whenever the app is opened again on a day whose session is already finished.

A further session on the same day starts only from the parent area. Nothing is ever said about stopping early or about days without a session.

---

# 30. Parent Entry Mechanism

Parent access takes two deliberate steps:

1. hold the top-right corner for 3 seconds. Nothing is drawn there, a short tap does nothing, and the hold shows no progress;
2. a plain adult screen asks the parent to type a two-digit number that is written as a word ("forty-seven"). The number changes every time.

A wrong answer, or 10 seconds of nothing, returns silently to exactly where the child was, with no sound or animation.

The corner works from every child screen, including the quiet screen. The parent area locks again when the parent leaves it or the screen turns off.

Once he can read number words, a parent-chosen PIN replaces step 2 (not built now).

---

# 31. Parent Gate Requirements

The parent gate should:

- be hidden from normal child attention;
- not look like a child game;
- avoid simple one-tap entry;
- avoid puzzles children may enjoy solving repeatedly;
- not require account authentication for Prototype 0.1.

---

# 32. Parent Gate Candidate Options (settled)

Settled 2026-11-17 as the two-step gate in §30. The earlier candidates A–C are dropped.

---

# 33. Parent Home

Sections, in order:

```text
Four words for today
Most recent session
Noticed at home
How each language is going
How the puzzles are going
Voices
Start an extra session
Microphone and privacy
```

No charts, numbers, dates or durations anywhere.

---

# 34. Parent Home Layout

Parent mode may use conventional UI:

- top app bar;
- cards;
- lists;
- buttons;
- text labels.

The parent area should remain visually calm and compact.

---

# 35. Parent Home Priority

The most important parent information is:

1. four words for today;
2. the most recent session, in sentences;
3. noticed-at-home actions;
4. everything else.

Not:

- percentage mastery;
- counts of any kind;
- time spent or missed days;
- comparison charts.

---

# 36. Four Words for Today

Replaces the single real-world suggestion. Four words from the most recent session, each with the moment of the day to say it:

```text
Four words for today

At breakfast, when you pour: water (Tamil)
At snack time, handing a slice: apple (Tamil)
At bath time: nose (English)
At dinner, passing it: cup (English)
```

Avoid:

- checklist completion boxes;
- streak rewards;
- reminders or notifications. The card is seen only when a parent opens the parent area.

---

# 37. Real-World Observation UI

The parent records evidence with minimal friction.

```text
Recent concept row
   ↓
[He understood it]
[He said it]
```

One tap saves the observation. These labels replace "Recognized outside Miozira" and "Used outside Miozira".

---

# 38. Observation Confirmation

After saving:

Use a small confirmation such as:

> “Saved.”

Do not use:

> “Great! Your child mastered this word.”

---

# 39. Most Recent Session

Described in sentences only:

```text
Most recent session: English and Tamil
Tried saying: apple, ball, cup, water (Tamil)
Said before the app did: ball, cup (Tamil)
Listened without speaking: nose, cat
Puzzle, What's gone?: he chose before the answer came back.
```

No counts ("Heard 3 times"), dates or durations. Sessions are "most recent" and "before that".

---

# 40. Parent Activity Vocabulary

Preferred:

- Tried saying
- Said before the app did
- Listened without speaking
- He understood it (at home)
- He said it (at home)
- Coming back later
- "most", "some", "not yet" in place of counts

Avoid:

- Missed
- Failed
- Weak
- Incorrect
- Learned
- Mastered
- 37% complete
- Behind
- any number of times, days or minutes

---

# 41. Voices Screen

Two lists.

**Guide phrases in Malayalam.** One row per phrase with two slots, Mother and Father. Both parents record every phrase; the app uses one parent's voice for a whole session and the other's for the next. A prepared voice is used only until a recording exists.

**Tamil words.** One row per word, recorded by the parents.

In both lists the parent can play, record, replace and delete.

---

# 42. Voice Row

```text
Your turn
Mother   Recorded            [Play] [Record again]
Father   Not recorded yet    [Record]
```

Recording controls belong only in parent mode.

---

# 43. Family Recording Safety

The recording UI should explain:

> family recordings stay on this device.

It should not imply cloud backup.

---

# 44. Microphone Settings UX

The parent area should explain clearly:

> Miozira uses the microphone only to notice whether your child tried to speak. It does not grade pronunciation, transcribe speech, upload child audio, or save ordinary child speaking attempts.

Then offer:

```text
Microphone attempt detection
[On / Off]
```

If permission is missing:

```text
Enable microphone
```

---

# 45. Permissions UX

Permission requests should be preceded by Miozira's own explanation.

Avoid throwing the Android permission dialog at the child without context.

---

# 46. Settings

Parent settings stay minimal:

- microphone attempt detection on/off;
- the voices;
- start an extra session;
- erase everything on this tablet;
- privacy information.

There is no language setting: all five languages are always on and the rotation decides. Avoid settings that let the parent micromanage the adaptive engine.

---

# 47. Language Settings (retired)

Retired 2026-11-17. The five languages are always available, two per session, rotating per completed session.

---

# 48. Reset Data UX

Reset must:

1. live in parent mode;
2. explain what will be deleted;
3. require explicit confirmation;
4. not be reachable accidentally.

Recommended wording:

> “Reset Miozira data on this device?”

Then explain:

- learning history;
- parent observations;
- family recordings;
- local settings as applicable.

---

# 49. Visual Style

Miozira should use a low-stimulation visual language.

Recommended characteristics:

- large areas of calm background;
- one visual focal point;
- minimal persistent chrome;
- soft transitions;
- clear contrast;
- limited decorative elements.

---

# 50. Child Visual Environment — LOCKED

Prototype 0.1 uses a fixed warm-light neutral child background:

```text
#F2EEE7
```

The child environment is intentionally **not dark-mode-first** and not automatically theme-switching.

The concept illustration provides most of the screen's natural color.

The interface around it stays quiet.

This gives Miozira the intended visual hierarchy:

```text
quiet warm field
→ one real-world concept
→ one temporary state cue when needed
```

Do not add decorative color merely to make the screen look more “childlike.”

---

# 51. Colour Usage

Locked child/UI family:

```text
Child background: #F2EEE7
Primary text:     #26312E
Primary accent:   #4F746B
Listening:        #607C8A
Neutral response: #6B756F
```

Colour should support:

- focus;
- state distinction;
- accessibility;
- natural recognition of the object itself.

It should not encode:

- right = green;
- wrong = red;
- high score;
- streak status;
- urgency;
- reward.

On a normal child screen, the learning object should usually be the most visually colorful element.

---

# 52. Typography

Child mode shows exactly one piece of text: the written word under the picture.

- about a tenth of the screen's height, centred;
- in the language's own script: Latin; Tamil; Arabic with vowel marks, laid out right to left; Japanese as a children's book writes the word (hiragana for everyday words, katakana for borrowed ones);
- the whole word fades in `#4F746B` as it is spoken and settles to `#26312E`;
- never required, never a tap target.

There is no instructional text for the child.

Parent mode may use standard readable text hierarchy.

Exact fonts/typescale belong in `design-system.md`.

---

# 53. Animation & Haptic Rules

Animation is allowed only when it improves comprehension, causality, or state clarity.

Allowed:

- a very slight grow on tap;
- a slow fade;
- the small nod after an attempt;
- a picture "stepping forward" (growing a little) while it is named;
- the breathing ring;
- a missing picture fading back into its place;
- in the two later puzzles only: the cup tipping once, and the close-up widening once;
- one soft haptic acknowledgement when useful.

Avoid:

- continuous bouncing;
- particle systems;
- confetti;
- flashing;
- rapid motion;
- random idle animations;
- looping mascot movement;
- reward vibrations;
- animation whose only purpose is “engagement.”

The screen is allowed to become completely still.

---

# 54. Motion Duration Guidance

Starting targets:

```text
tap acknowledgement     ≤100 ms
concept entry           300–500 ms
listening cue start     immediate after invite
response animation      500–1200 ms
item transition         300–700 ms
```

Tune through real-device observation.

---

# 55. Sound Design Rules

The only sounds are voices: the word, and the guide phrases.

There is no music, no instrument sound and no effect sound of any kind (parent decision 2026-11-17).

---

# 56. Accessibility

## UX-ACC-001 — No colour-only meaning

All important states need another visual, positional, semantic, or audio cue.

## UX-ACC-002 — Large child interaction region

Tap target should be generous enough for preschool motor variability.

## UX-ACC-003 — Parent semantics

Parent controls should have proper accessible labels for Android screen readers where practical.

## UX-ACC-004 — Dynamic text

Parent-area text should tolerate system text scaling without becoming unusable.

## UX-ACC-005 — Contrast

Critical information must maintain strong contrast.

---

# 57. System Bars and Insets

The layout should respect:

- status/navigation bars;
- display cutouts;
- gesture insets.

Essential child interactions should not be placed directly against unsafe edges.

---

# 58. Full-Screen Behavior

Child mode may use immersive/full-screen presentation if it improves focus.

However:

- exiting should remain possible for the parent;
- system behavior must remain predictable;
- parent gate must remain reachable.

Final full-screen strategy belongs in engineering implementation.

---

# 59. Child Mode — Explicitly Prohibited UI

Do not include:

- back/next buttons as primary learning controls;
- bottom navigation;
- hamburger menu;
- settings gear;
- scores;
- hearts/lives;
- stars;
- streak counter;
- progress bar;
- lesson map;
- ads;
- purchase buttons;
- social sharing;
- leaderboard;
- text quiz;
- pronunciation meter.

---

# 60. Parent Mode — Explicitly Prohibited UI

Prototype 0.1 should avoid:

- competitive child comparison;
- percentile charts;
- daily productivity dashboards;
- guilt-based reminders;
- “Your child is behind” messaging;
- unsupported fluency scores;
- engagement-maximization alerts.

---

# 61. Child First-Session UX

The first session should teach the interface through interaction.

Recommended sequence:

```text
1. Familiar concept appears.
2. Concept subtly pulses.
3. Child taps.
4. Word plays.
5. Short imitation cue.
6. Next familiar concept appears.
7. Pattern repeats.
```

No onboarding carousel is needed.

---

# 62. Returning-Session UX

Once the child understands the pattern:

- reduce hint frequency;
- retain same interaction model;
- allow quieter pauses;
- maintain predictable state transitions.

---

# 63. Microphone Disabled UX

When microphone attempt detection is off or not permitted, everything looks the same, including the ring as a quiet space. Only the nod never happens.

The session must still feel complete.

---

# 64. Audio Failure UX

If canonical audio unexpectedly cannot play:

Child mode should:

- not show an error dialog;
- transition to another valid item if possible.

Parent/developer diagnostics may record the issue separately.

---

# 65. Missing Family Recording UX

If no family recording exists:

- canonical audio remains available;
- child mode does not show “missing recording.”

---

# 66. Empty Parent History

On first use, parent activity may say something simple such as:

> “Miozira will show recent learning here after a session.”

No empty-state pressure.

---

# 67. Parent Real-World Observation History

Prototype 0.1 may show a small chronological list:

```text
Today
Apple — Tamil
He said it

Yesterday
Ball — English
He understood it
```

No chart required.

---

# 68. Design for One Child

Prototype 0.1 does not need:

- profile avatars;
- child switching;
- child profile carousel.

The parent experience assumes one local learner state.

---

# 69. Error Messaging Style

Parent errors should be:

- plain-language;
- short;
- actionable.

Example:

> “The family recording couldn't be played. The standard word recording is still available.”

Avoid:

> `AudioTrackException code -19`.

---

# 70. Loading States

Because core data is local, loading states should be rare.

Do not show a spinner for routine:

- next-item selection;
- local database write;
- audio selection.

If a blocking operation occurs, keep it brief and parent-facing where possible.

---

# 71. UI State Ownership

The implementation should maintain a clear UI state representation.

Suggested child UI state:

```text
ChildSessionUiState
  activeConcept
  activeLanguage
  interactionState
  microphoneAvailable
  replayAvailable
  sessionActive
```

Suggested parent UI state:

```text
ParentUiState
  latestSuggestion
  recentActivity
  observationActions
  settings
```

Final architecture belongs in `architecture.md`.

---

# 72. Design Tokens

`design-system.md` should eventually define:

- spacing scale;
- corner radius;
- typography;
- background colours;
- concept frame;
- animation durations;
- touch target minimums;
- parent button styles.

`ui-ux.md` defines behavior first.

---

# 73. Child Screen Kinds

```text
1. Word screen (including "say it first")
2. Two-picture screen ("Which one?")
3. Puzzle screens
4. Quiet screen
5. Scene (later)
```

Everything else is a state change within one of these.

---

# 74. Parent Screens

```text
1. Parent gate (reading step)
2. Parent Home
3. How each language is going
4. How the puzzles are going
5. Voices
6. Microphone and privacy
```

"Start an extra session" is a confirmation, not a screen.

---

# 75. Prototype Navigation Recommendation

Recommended parent navigation:

> one parent home with stacked sections/cards and simple drill-in screens.

Avoid building a bottom-tab architecture unless testing demonstrates it is necessary.

---

# 76. UX Acceptance Criteria — Child

Before family testing:

- child can identify the primary tap target;
- child does not need written instructions;
- tap triggers word reliably;
- replay is understandable;
- microphone cue is not confusing;
- silence does not create failure feedback;
- child can stop at any time;
- the screen stays in landscape and never turns;
- parent controls are not accidentally activated.

---

# 77. UX Acceptance Criteria — Parent

Before family testing:

- parent can intentionally enter parent mode;
- parent can understand current real-world suggestion;
- parent can record recognition/use in a few taps;
- parent can manage family recording;
- parent understands microphone behavior;
- parent can reset data intentionally;
- no mastery/fluency claims appear.

---

# 78. Family-Test UX Questions

Observe:

1. Does the child know where to tap?
2. Does the child understand replay?
3. Does the child notice the listening cue?
4. Does the listening cue distract?
5. Does the child wait naturally after audio?
6. Does the child seem confused when language changes?
7. Are transitions too slow or too fast?
8. Does the concept visual dominate enough?
9. Are accidental taps frequent?
10. Does the parent gate stay hidden from ordinary child exploration?
11. Can the parent record real-world evidence quickly?
12. Does the parent understand the purpose of family recordings?
13. Does any part feel like homework or performance pressure?

---

# 79. UX Invariants

### UX-INV-001
The child never needs to read to complete the core loop.

### UX-INV-002
One dominant concept exists per child learning state.

### UX-INV-003
The primary child interaction is a tap.

### UX-INV-004
Child silence never produces failure UI.

### UX-INV-005
No child-facing score exists.

### UX-INV-006
No child-facing streak exists.

### UX-INV-007
No mandatory “Next” button exists.

### UX-INV-008
No ordinary child tap opens destructive parent controls.

### UX-INV-009
The screen is locked to landscape; any screen rebuild preserves the learning state.

### UX-INV-010
Microphone denial does not create a broken child screen.

### UX-INV-011
Parent real-world suggestions are not child quizzes.

### UX-INV-012
The parent UI uses evidence language, not mastery claims.

### UX-INV-013
After any child tap in "Which one?" or a puzzle, the same two things happen: what he touched is named, then the real answer plays out. Nothing is ever shown as wrong.

### UX-INV-014
Nothing in the app is earned, unlocked or announced as an achievement.

### UX-INV-015
The parent UI shows no numbers, dates or durations.

### UX-INV-016
No picture shows the face of a person or an animal.

### UX-INV-017
The only sounds are voices.

---

# 80. Prototype Wireframe — Child

```text
LANDSCAPE (the only orientation)

┌──────────────────────────────────┐
│            ╭ ─ ─ ─ ─ ╮           │
│           ¦ BIG APPLE ¦          │   ring only during his turn
│            ╰ ─ ─ ─ ─ ╯           │
│              apple               │   written word, below the ring
└──────────────────────────────────┘
```

The picture itself is tappable.

---

# 81. Prototype Wireframe — Parent Home

```text
┌──────────────────────────────────────────────┐
│ Miozira for parents         [Lock and go back]│
├──────────────────────────────────────────────┤
│ Four words for today                          │
│  At breakfast, when you pour: water (Tamil)   │
│  At snack time, handing a slice: apple (Tamil)│
│  At bath time: nose (English)                 │
│  At dinner, passing it: cup (English)         │
├──────────────────────────────────────────────┤
│ Most recent session: English and Tamil        │
│  Tried saying: apple, ball, cup, water (Tamil)│
│  Said before the app did: ball, cup (Tamil)   │
│  Listened without speaking: nose, cat         │
├──────────────────────────────────────────────┤
│ Noticed at home                               │
│  Cup, in Tamil [He understood it] [He said it]│
├──────────────────────────────────────────────┤
│ How each language is going                  › │
│ How the puzzles are going                   › │
│ Voices                                      › │
│ Start an extra session                      › │
│ Microphone and privacy                      › │
└──────────────────────────────────────────────┘
```

---

# 82. Prototype Wireframe — Voices

```text
┌──────────────────────────────────────────────┐
│ Guide phrases in Malayalam                    │
│ Until you record a phrase, a prepared voice   │
│ is used. Recordings stay on this tablet.      │
├──────────────────────────────────────────────┤
│ Your turn                                     │
│  Mother  Recorded          [Play][Record again]│
│  Father  Not recorded yet  [Record]           │
│ What's gone?                                  │
│  ...                                          │
└──────────────────────────────────────────────┘
```

---

# 83. Prototype Wireframe — Microphone Explanation

```text
┌────────────────────────────────────┐
│ Speaking attempts                  │
│                                    │
│ Miozira can briefly use the        │
│ microphone to notice whether your  │
│ child tried to speak.              │
│                                    │
│ It does not:                       │
│ • grade pronunciation              │
│ • transcribe speech                │
│ • upload child audio               │
│ • save ordinary speaking attempts  │
│                                    │
│ [Enable microphone]                │
│ [Not now]                          │
└────────────────────────────────────┘
```

---

# 84. Open UX Questions

Answered on 2026-11-17:

1. photo vs illustration — painted realistic pictures;
3. dark vs light child background — light, locked;
4. language-switch cue — none;
5. explicit “Your turn” audio — yes, after every word;
6. listening indicator — one calm ring around the picture;
7. parent-gate gesture — 3-second corner hold plus a reading step;
8. separate Recent Learning screen — no; the home describes the most recent session;
9. family voice — in: guide phrases by both parents, and Tamil words;
10. movement prompts — one, opening the puzzle part, when a fitting word is in the set.

Still to be tested rather than assumed:

2. exact concept size.

---

# 85. Relationship to `child-experience.md`

`child-experience.md` should expand this document into the child's emotional and behavioral journey:

- first use;
- returning use;
- uncertainty;
- replay;
- no-response;
- interruption;
- stopping;
- recovery;
- child-safe language.

---

# 86. Relationship to `parent-experience.md`

`parent-experience.md` should define:

- parent onboarding;
- gate behavior;
- suggestion flow;
- observation flow;
- family recording management;
- settings;
- privacy messaging;
- destructive actions.

---

# 87. Relationship to `interaction-states.md`

`interaction-states.md` should make every child and parent state explicit, including:

- transitions;
- valid events;
- invalid events;
- timeout behavior;
- orientation behavior;
- audio/microphone fallback states.

---

# 88. Decision Summary

- landscape only; the screen does not turn;
- one dominant concept picture, which is the tap target;
- one written word beneath it, shown but never required;
- tap plays the word; "Your turn" follows in a parent's voice; a ring marks his turn;
- "say it first", "Which one?" and one thinking puzzle per session;
- after any tap the same thing happens; nothing is ever shown as wrong;
- no language cue; no scores, streaks, rewards or anything earned;
- the session is a set of items, ends by itself and goes quiet;
- the only sounds are voices;
- pictures are painted realistic, with no faces on people or animals;
- the parent area sits behind a hidden two-step gate and speaks in sentences, not numbers;
- every child interaction remains optional and non-punitive.

The UX should be simple enough that the child can learn the app by using it.

---

# 89. "Say It First", "Which One?" and the Thinking Puzzles

Defined in the UI/UX design §5.3–5.5 (`docs/superpowers/specs/2026-11-16-ui-ux-design.md`).

One rule covers all of them: after any tap, (1) what he touched is named, then (2) the real answer plays out calmly. If he taps nothing, step 2 happens after a short wait.

There is no tick, cross, colour, sound effect, shake or "try again".

---

# 90. The Scene

Defined in the UI/UX design §5.6. One calm painted place containing things he already knows; he taps what he likes and hears its word.

It is the ordinary ending of every session. It is never unlocked, earned or announced, it shows no empty outlines for things not yet met, and it has no pinch-zoom. It is built after the first three puzzles.

---

# 91. Pictures

Defined in the UI/UX design §8.

- realistic and softly painted; one thing per picture, cut out, directly on the warm background; no glow, frame or room behind it;
- animals and people never show a face; body parts are shown one at a time;
- people are modestly dressed; eating and drinking use the right hand;
- three pictures per word; a new word always uses the same first picture.

---

# v0.2 Interaction Philosophy — Calm Curiosity

The Prototype 0.1 UX target is:

> **calm curiosity, not maximum engagement.**

A child taking longer to look at an object is not a UX failure.

A quiet screen is not an empty screen.

The design should allow:

- looking;
- listening;
- processing;
- imitation;
- replay;
- silence;
- departure.

The app must never visually accelerate because the child is slow to respond.
