# Miozira — UI/UX Specification

**Document:** `ui-ux.md`  
**Version:** 0.2  
**Status:** Draft for Prototype 0.1 — Palette & Wellbeing Update  
**Product:** Miozira  
**Prototype:** Android-first, 8 concepts × English + Tamil  
**Target child:** approximately 4–5 years old  
**Supported orientations:** Portrait + Landscape  
**Last updated:** 2026-08-25

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

Text may exist for adults or accessibility, but it must not be required for child operation.

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

Prototype 0.1 should remain extremely shallow.

Recommended structure:

```text
App Launch
   |
   +-- Child Mode
   |     |
   |     +-- Learning Session
   |     |
   |     +-- Session Rest/End State
   |
   +-- Parent Gate
         |
         +-- Parent Home
         +-- Recent Learning
         +-- Real-World Observation
         +-- Family Recordings
         +-- Language / Microphone Settings
         +-- Privacy / Data Reset
```

There should be no child-facing navigation hierarchy.

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

The child learning screen is the primary product screen.

It should contain:

1. background;
2. dominant concept visual;
3. optional subtle interaction cue;
4. temporary listening/response indicator;
5. hidden/inconspicuous parent-entry mechanism.

No other persistent child controls are required.

---

# 7. Child Screen Layout — Portrait

Recommended conceptual structure:

```text
┌──────────────────────┐
│                      │
│                      │
│      CONCEPT         │
│       VISUAL         │
│                      │
│                      │
│    subtle state      │
│       cue            │
│                      │
└──────────────────────┘
```

The concept should occupy most of the usable central area.

---

# 8. Child Screen Layout — Landscape

Recommended conceptual structure:

```text
┌──────────────────────────────────┐
│                                  │
│          CONCEPT VISUAL          │
│                                  │
│            state cue             │
│                                  │
└──────────────────────────────────┘
```

Landscape should not create a new navigation model.

It should simply give the concept more horizontal breathing room.

---

# 9. Responsive Layout Rules

## UX-003 — Preserve hierarchy

Portrait and landscape must preserve:

1. concept dominance;
2. large touch region;
3. uncluttered background;
4. simple listening/response cue.

---

## UX-004 — No hardcoded pixel placement

Layout should use adaptive constraints rather than fixed coordinates for one tablet model.

---

## UX-005 — Orientation continuity

When orientation changes:

- active concept stays the same;
- active language stays the same;
- session remains in the same state;
- no duplicate exposure is recorded;
- no automatic replay occurs solely because of rotation.

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

After the target word, Miozira may invite imitation.

The prompt may be:

- spoken;
- visual;
- both.

Prototype 0.1 should minimize dependence on spoken UI prompts because too much spoken interface can compete with the target word.

---

# 17. Listening Cue

## UX-012 — Simple listening indicator

When microphone detection is active, the screen may display:

- one calm ring;
- small microphone symbol;
- expanding/contracting shape.

It must not display:

- audio waveform;
- decibel meter;
- recording countdown;
- pronunciation score;
- red/green accuracy bar.

---

## UX-013 — Listening indicator is temporary

The cue disappears when the short listening window ends.

---

# 18. RESPONDING State

The child receives a small acknowledgement.

Possible visual response:

- concept gently moves;
- soft halo;
- brief smile-like motion in surrounding UI if non-character-based.

Avoid turning the concept itself into a mascot unless separately approved.

---

# 19. Response for Detected Attempt

The UI may use:

- warm neutral sound;
- slight positive visual change.

It must not imply verified correctness.

Avoid:

```text
✓ Correct
100%
Perfect!
```

---

# 20. Response for No Attempt

The interface should remain neutral.

Possible behavior:

- replay canonical word once;
- quiet pause;
- transition onward.

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

Language changes should feel intentional but subtle.

Possible options:

### Option A — No child-facing label

The app simply switches language according to session structure.

### Option B — Small non-reading visual cue

A subtle transition style distinguishes a language block.

### Option C — Short spoken cue

Used only if child testing shows the switch is confusing.

Prototype recommendation:

> start with **Option A**, adding cues only if testing shows confusion.

---

# 26. Do Not Use Flags as Primary Language Identity

National flags should not be the main way Miozira represents language.

Reasons:

- languages are not equivalent to one country;
- English and Tamil span regional/cultural contexts;
- future languages may have multiple country variants.

Parent UI may use written language names.

---

# 27. Movement Interaction UX

If a movement prompt is used:

- keep the same main screen;
- show the relevant concept;
- use a short spoken instruction;
- allow time for the child to respond.

Example:

```text
[nose visual]
"Touch your nose."
```

No new score screen appears.

---

# 28. Natural Session End

The child experience should not end with:

- stars;
- confetti;
- “Lesson complete!”;
- progress wheel;
- daily streak.

Recommended end:

1. familiar/easy final item;
2. soft visual transition;
3. neutral resting state or exit;
4. parent content available separately.

---

# 29. Resting Child Screen

After a planned session ends, Miozira may show a very simple resting state.

Possible elements:

- Miozira mark;
- calm background;
- no urgent button.

The exact state may be bypassed if the app simply returns to launch/child home.

---

# 30. Parent Entry Mechanism

Parent access must be intentional and unlikely to occur accidentally.

Recommended Prototype 0.1 candidate:

> **long-press on a small, visually unobtrusive corner hotspot for approximately 2 seconds**

followed by:

> **simple adult confirmation gate**

The final mechanism should be tested.

---

# 31. Parent Gate Requirements

The parent gate should:

- be hidden from normal child attention;
- not look like a child game;
- avoid simple one-tap entry;
- avoid puzzles children may enjoy solving repeatedly;
- not require account authentication for Prototype 0.1.

---

# 32. Parent Gate Candidate Options

Potential candidates:

### Candidate A — Long press + hold confirmation

Example:

- hold corner;
- continue holding until ring completes.

### Candidate B — Long press + simple text confirmation

Example:

- “Parent area — continue?”

### Candidate C — Two-step adult gesture

Example:

- long press;
- tap a small adult-oriented confirmation target.

Recommended starting candidate:

> **A or B**

because they are simple and fully offline.

---

# 33. Parent Home

Recommended parent home sections:

```text
Recent learning
Try this outside Miozira
Recognized / Used outside Miozira
Family recordings
Settings & privacy
```

Avoid a dashboard full of charts.

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

The most important parent information should be:

1. current real-world suggestion;
2. quick observation actions;
3. recent activity;
4. family recordings/settings.

Not:

- percentage mastery;
- engagement statistics;
- comparison charts.

---

# 36. Real-World Suggestion Card

Recommended structure:

```text
Try this outside Miozira

At snack time, say the Tamil word for apple while handing over a slice.
```

Optional:

- concept thumbnail;
- target-language label.

Avoid:

- checklist completion box;
- streak reward;
- push-reminder prompt.

---

# 37. Real-World Observation UI

The parent should be able to record evidence with minimal friction.

Recommended flow:

```text
Recent concept row
   ↓
[Recognized outside Miozira]
[Used outside Miozira]
```

One tap on the appropriate action should save the observation.

---

# 38. Observation Confirmation

After saving:

Use a small confirmation such as:

> “Saved.”

Do not use:

> “Great! Your child mastered this word.”

---

# 39. Recent Learning Screen

Parent-facing recent activity may show:

```text
Apple — Tamil
Heard recently
Tried saying it
```

or:

```text
Ball — English
Replayed
Recognized outside Miozira
```

Avoid numeric scoring.

---

# 40. Parent Activity Vocabulary

Preferred:

- Heard
- Tried saying it
- Replayed
- Recognized outside Miozira
- Used outside Miozira
- Coming back later

Avoid:

- Failed
- Weak
- Incorrect
- Mastered
- 37% complete
- Behind

---

# 41. Family Recordings Screen

The parent should be able to:

- select concept;
- select language;
- hear canonical audio;
- record family voice;
- listen to family recording;
- replace;
- delete.

---

# 42. Family Recording UI

Recommended flow:

```text
Concept + language
Canonical model [Play]

Family voice
[Record]

after recording:
[Play]
[Record again]
[Delete]
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

Prototype 0.1 parent settings should remain minimal.

Possible settings:

- enabled languages;
- preferred starting language;
- microphone attempt detection;
- family recordings;
- reset prototype data;
- privacy information.

Avoid settings that allow the parent to micromanage the adaptive engine.

---

# 47. Language Settings

Parent UI may display:

```text
Languages
English
Tamil
```

Prototype 0.1 should keep both available.

If one is disabled for testing, that should be an explicit parent choice.

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

Child mode should use little or no visible instructional text.

When text exists:

- large;
- clear;
- simple;
- high contrast.

Parent mode may use standard readable text hierarchy.

Exact fonts/typescale belong in `design-system.md`.

---

# 53. Animation & Haptic Rules

Animation is allowed only when it improves comprehension, causality, or state clarity.

Allowed examples:

- 1–2% press response;
- short fade;
- one gentle transition;
- a concept-specific meaningful motion;
- restrained listening indicator;
- one soft haptic acknowledgement when useful.

Meaningful motion examples:

```text
car → rolls briefly
hand → opens/closes once
water → pours once
```

These should be used only when the motion teaches or clarifies something.

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

Non-word sound effects should be:

- sparse;
- soft;
- short.

Avoid:

- loud reward sounds;
- arcade effects;
- continuous music;
- competing spoken audio.

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

When microphone attempt detection is disabled:

- do not show listening ring;
- maintain a short natural pause after target audio;
- move onward gently.

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
Used outside Miozira

Yesterday
Ball — English
Recognized outside Miozira
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

# 73. Initial Child Screen Count

Prototype 0.1 should aim for an extremely small child-screen count.

Potentially:

```text
1. Learning screen
2. Rest/end screen
```

Everything else should be state changes within the learning screen.

---

# 74. Initial Parent Screen Count

Likely minimal parent screens:

```text
1. Parent Home
2. Recent Learning
3. Real-World Observation
4. Family Recordings
5. Settings / Privacy
```

Some may be merged to reduce complexity.

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
- orientation does not reset the flow;
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
Orientation changes preserve the learning state.

### UX-INV-010
Microphone denial does not create a broken child screen.

### UX-INV-011
Parent real-world suggestions are not child quizzes.

### UX-INV-012
The parent UI uses evidence language, not mastery claims.

---

# 80. Prototype Wireframe — Child

```text
PORTRAIT

┌──────────────────────────────┐
│                              │
│                              │
│                              │
│        [ BIG APPLE ]         │
│                              │
│                              │
│                              │
│          ◌ listening         │
│          when active         │
│                              │
└──────────────────────────────┘
```

The concept itself is tappable.

---

# 81. Prototype Wireframe — Parent Home

```text
┌────────────────────────────────────┐
│ Miozira — Parent                   │
├────────────────────────────────────┤
│ Try this outside Miozira           │
│                                    │
│ At snack time, say the Tamil word  │
│ for apple while handing over a     │
│ slice.                              │
├────────────────────────────────────┤
│ Recent learning                    │
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

# 82. Prototype Wireframe — Family Recording

```text
┌────────────────────────────────────┐
│ Apple — Tamil                      │
├────────────────────────────────────┤
│ Standard voice                     │
│ [Play]                             │
│                                    │
│ Family voice                       │
│ [Record]                           │
│                                    │
│ after recording:                   │
│ [Play]  [Record again]  [Delete]   │
└────────────────────────────────────┘
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

The following should be tested rather than assumed:

1. photo vs illustration;
2. exact concept size;
3. dark vs light child background;
4. whether language switches need a cue;
5. whether explicit “Your turn” audio is necessary;
6. ideal listening indicator;
7. exact parent-gate gesture;
8. whether parent home needs a separate Recent Learning screen;
9. whether family voice should be exposed in Prototype 0.1 or deferred if it complicates testing;
10. whether movement prompts feel natural with the initial eight concepts.

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

Prototype 0.1 UI/UX is intentionally minimal:

- one primary child learning screen;
- one dominant concept;
- concept itself is the main tap target;
- tap plays the target word;
- listening/response state appears temporarily;
- no scores, streaks, text lessons, or child navigation;
- portrait and landscape use the same interaction model;
- parent mode is intentionally separated;
- parent home prioritizes real-world transfer over dashboards;
- family recordings and microphone controls live only in parent mode;
- visual and motion design remain calm;
- every child interaction remains optional and non-punitive.

The UX should be simple enough that the child can learn the app by using it.


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
