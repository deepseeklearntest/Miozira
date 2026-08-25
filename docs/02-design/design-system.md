# Miozira — Design System

**Document:** `design-system.md`  
**Version:** 0.2  
**Status:** Draft for Prototype 0.1 — Palette & Wellbeing Update  
**Product:** Miozira  
**Prototype:** Android-first, 8 concepts × English + Tamil  
**Primary modes:** Child + Parent  
**Supported orientations:** Portrait + Landscape  
**Last updated:** 2026-08-25

---

## 1. Purpose

This document defines the reusable visual and interaction foundations for Miozira Prototype 0.1.

It translates the requirements from:

- `ui-ux.md`
- `child-experience.md`
- `parent-experience.md`
- `accessibility.md`
- `session-design.md`
- `interaction-states.md`

into a small, consistent design system.

The design system covers:

- layout spacing;
- touch-target sizes;
- typography;
- color roles;
- contrast;
- component shapes;
- motion;
- child concept presentation;
- listening and response indicators;
- parent buttons and cards;
- responsive behavior;
- accessibility behavior.

The system should remain intentionally small.

Prototype 0.1 does not need a large enterprise component library.

---

# 2. Design System Goals

The design system should make Miozira:

- calm;
- consistent;
- easy to understand;
- development-friendly;
- accessible;
- suitable for preschool touch;
- visually coherent across portrait and landscape;
- easy to extend without redesigning every screen.

---

# 3. Design Principles

## DS-P01 — Calm before decorative

Every visual decision should support:

- concept clarity;
- interaction clarity;
- readability;
- comfort.

Decorative richness is secondary.

---

## DS-P02 — Child UI and parent UI are related but not identical

Child mode emphasizes:

- large visual focus;
- minimal chrome;
- very large touch area;
- minimal text.

Parent mode emphasizes:

- readable text;
- clear controls;
- compact information hierarchy;
- conventional accessibility semantics.

---

## DS-P03 — Few tokens, used consistently

Prototype 0.1 should avoid dozens of arbitrary values.

Prefer a small reusable scale.

---

## DS-P04 — Accessibility is built into tokens

Minimum touch size, contrast, text scaling, and motion alternatives should be part of the design system rather than patched later.

---

# 4. Base Unit

Recommended base spacing unit:

> **4 dp**

Most spacing values should be multiples of 4.

This keeps layout predictable while allowing enough granularity.

---

# 5. Spacing Scale

Recommended token set:

```text
space.0   = 0 dp
space.1   = 4 dp
space.2   = 8 dp
space.3   = 12 dp
space.4   = 16 dp
space.5   = 20 dp
space.6   = 24 dp
space.8   = 32 dp
space.10  = 40 dp
space.12  = 48 dp
space.16  = 64 dp
```

Avoid inventing one-off values unless a specific layout requires it.

---

# 6. Child Layout Spacing

Recommended child-mode spacing:

```text
screen horizontal safe padding:
  portrait: 24–32 dp
  landscape: 32–48 dp

screen vertical safe padding:
  24–40 dp
```

The exact value may adapt to screen size.

The child screen should prefer whitespace over filling the screen.

---

# 7. Parent Layout Spacing

Recommended parent-mode spacing:

```text
screen horizontal padding:
  compact width: 16–20 dp
  tablet width: 24–32 dp

card internal padding:
  16–20 dp

section gap:
  24–32 dp

control gap:
  8–12 dp
```

---

# 8. Touch Target Scale

## Adult baseline

```text
touch.adult.minimum = 48 dp
```

Parent-facing buttons, icons, and toggles should meet at least this effective size.

---

## Child secondary target

If a child-facing secondary target exists:

```text
touch.child.secondary.minimum = 72 dp
```

Prefer larger when space permits.

---

## Child primary concept target

The primary child concept should not be treated as a conventional button.

Recommended effective target:

```text
touch.child.primary = large central region
```

Target guidance:

- visual occupies approximately 55–75% of the shorter usable screen dimension;
- effective hit region may extend beyond visual bounds;
- no nearby competing controls.

---

# 9. Corner Radius Scale

Recommended:

```text
radius.none   = 0 dp
radius.small  = 8 dp
radius.medium = 16 dp
radius.large  = 24 dp
radius.pill   = 999 dp
```

Usage:

- parent buttons: `medium`
- parent cards: `large`
- child listening ring: circular/pill
- child concept visual: depends on asset, not necessarily card-framed

Avoid applying rounded cards everywhere in child mode.

---

# 10. Elevation

Prototype 0.1 should use minimal elevation.

Recommended:

```text
elevation.none   = 0
elevation.low    = subtle
elevation.medium = parent modal/dialog only
```

Child mode should avoid stacked card layers.

Visual separation should come mainly from:

- spacing;
- contrast;
- scale.

---

# 11. Typography Philosophy

Child mode should contain very little visible text.

Parent mode should use a conventional readable hierarchy.

The design system should support:

- English;
- Tamil Unicode;
- dynamic text scaling.

---

# 12. Parent Typography Scale

Recommended semantic scale:

```text
type.display
type.title.large
type.title.medium
type.body.large
type.body
type.label
type.caption
```

Suggested starting sizes:

```text
display       32 sp
title.large   24 sp
title.medium  20 sp
body.large    18 sp
body          16 sp
label         14–16 sp
caption       13–14 sp
```

These values should be tested with Tamil script and Android font scaling.

---

# 13. Child Typography

Child text should be exceptional rather than default.

If a child-visible label is used:

```text
type.child.large = 28–36 sp
```

It must:

- remain supplementary;
- have strong contrast;
- not be required for interaction.

---

# 14. Font Requirements

The selected font stack must:

- render Tamil correctly;
- support English;
- preserve legibility at larger text sizes;
- avoid clipped Tamil glyphs;
- behave well under Android font scaling.

Prototype 0.1 should prefer robust system-supported fonts over decorative branding fonts that reduce readability.

---

# 15. Font Weight

Recommended parent hierarchy:

```text
regular  = body
medium   = controls/labels
semibold = titles
```

Avoid very light weights.

Child mode should use weight mainly for clarity, not styling.

---

# 16. Color System Philosophy — LOCKED FOR PROTOTYPE 0.1

Prototype 0.1 uses a **calm warm-neutral palette**.

The design intentionally rejects the common “children's app = maximum saturation” convention.

The governing visual idea is:

> **The learning object carries the visual interest. The interface provides a quiet stage around it.**

The palette is designed to:

- keep large screen areas low-stimulation;
- maintain clear figure-ground separation;
- avoid visual urgency;
- leave room for natural object colors;
- provide accessible contrast;
- avoid reward/correctness color coding.

Color is a functional attention tool, not an engagement mechanism.

---

# 17. Locked Semantic Color Tokens

```text
color.background.child          = #F2EEE7
color.background.parent         = #F6F3EE

color.surface.primary           = #E8E2D8
color.surface.secondary         = #DDD6CA

color.content.primary           = #26312E
color.content.secondary         = #53605B
color.content.muted             = #6F7772

color.action.primary            = #4F746B
color.action.onPrimary          = #FFFFFF

color.action.secondary          = #D6E2DE
color.action.onSecondary        = #2E4943

color.border                    = #B8B2A8
color.focus                     = #476D68

color.listening                 = #607C8A
color.response.neutral          = #6B756F

color.warning.parent            = #94604E
color.error.parent              = #875148

color.scrim                     = #26312E
```

These values are frozen for Prototype 0.1 unless real-device testing reveals a concrete readability/accessibility problem.

---

# 18. Palette Character

The palette can be described as:

```text
warm stone
soft linen
deep green-charcoal
muted eucalyptus/teal
soft slate-blue
muted clay
```

It should feel:

- calm;
- warm;
- trustworthy;
- non-clinical;
- non-game-like;
- contemporary;
- suitable for extended quiet viewing.

It should not feel:

- neon;
- candy-colored;
- arcade-like;
- celebratory;
- urgent;
- reward-driven.

---

# 19. Child Color Rules

Child mode should normally contain:

1. the warm neutral background `#F2EEE7`;
2. the natural-color concept illustration;
3. at most one temporary semantic accent at a time.

The concept illustration should usually be the most saturated element on screen.

Examples:

- apple may remain naturally red;
- car may use a realistic body color;
- water may remain naturally blue/transparent;
- cat may retain natural fur colors.

Do **not** desaturate the learning object merely to match the interface.

The child learns about real objects, so realistic color can carry semantic value.

---

# 20. Accent Budget

The child screen has an explicit **accent budget**.

At rest:

> **zero persistent attention-seeking accents are required.**

During interaction, one accent may temporarily communicate:

- tappability/focus;
- listening;
- neutral acknowledgement.

Do not show several competing accent colors simultaneously.

---

# 21. Listening Color

The listening state uses:

```text
color.listening = #607C8A
```

This muted slate-blue is a neutral state cue.

It does **not** mean:

- correct;
- incorrect;
- success;
- recording quality;
- speech confidence.

Listening state must also be communicated through shape/state, not color alone.

---

# 22. Primary Action Color

Parent and occasional secondary interaction emphasis may use:

```text
#4F746B
```

This muted teal/eucalyptus is intentionally less saturated than conventional children's-app primary colors.

It should not be repeatedly pulsed, flashed, or used as a reward.

---

# 23. Warning / Error Color

Muted clay/red-brown is restricted primarily to parent-facing caution/destructive contexts:

```text
warning = #94604E
error   = #875148
```

Child mode should not use red-like hues to signal failure because Prototype 0.1 has no failure state.

---

# 24. Contrast Requirements and Baseline Checks

Prototype 0.1 baseline contrast checks:

```text
#26312E on #F2EEE7 ≈ 11.6:1
#53605B on #F2EEE7 ≈ 5.7:1
#4F746B on #FFFFFF ≈ 5.2:1
#476D68 on #F2EEE7 ≈ 5.0:1
#607C8A on #F2EEE7 ≈ 3.8:1
#2E4943 on #D6E2DE ≈ 7.3:1
#94604E on #F6F3EE ≈ 4.7:1
#875148 on #F6F3EE ≈ 5.8:1
```

Parent normal text:

> target at least **4.5:1**

Large text and important graphical UI:

> target at least **3:1**

Final device rendering must still be checked on real hardware.

---

# 25. Light / Dark Strategy — REVISED

Prototype 0.1 child mode is **not dark-mode-first**.

The default child environment is the fixed warm-light neutral:

```text
#F2EEE7
```

Reason:

- the evidence supports controlled contrast and salience more strongly than any claim that dark mode is inherently healthier for preschoolers;
- natural-color illustrations read clearly against the warm neutral field;
- a fixed child environment prevents unpredictable theme changes from altering the learning scene.

Parent mode may later respect system appearance if implementation remains visually coherent, but Prototype 0.1 may use the same warm-light family for simplicity.

A future dark/dim mode requires separate real-device testing.

---

# 26. System Appearance

For Prototype 0.1:

- child mode remains visually controlled rather than automatically changing with system theme;
- parent mode may remain warm-light for consistency;
- do not change theme during an active session;
- brightness itself remains controlled by the device/parent, not forced by Miozira.

---

# 27. Color Anti-Patterns

Do not use:

- rainbow gradients;
- neon accents;
- highly saturated full-screen backgrounds;
- multicolor button rows;
- glowing reward borders;
- green/red correctness;
- color cycling;
- animated gradient backgrounds;
- “attention red” for ordinary child interaction;
- celebratory gold/yellow as a completion reward.

---

# 28. Color Decision Rule

Before adding a color ask:

> **Does this color help the child identify the concept, understand the state, or read the interface?**

If the answer is only:

> “It makes the screen more exciting,”

do not add it.

---

# 23. Primary Child Component — ConceptStage

Recommended logical component:

```text
ConceptStage
```

Responsibilities:

- render one concept visual;
- provide the primary tap target;
- show temporary state cues;
- adapt to portrait/landscape;
- expose accessible semantics;
- avoid decorative chrome.

---

# 24. ConceptStage States

The same component should support:

```text
presenting
ready
playingAudio
inviting
listening
responding
transitioning
idle
```

State changes should not create unrelated layout jumps.

---

# 25. Concept Visual Sizing

Recommended approach:

```text
max width/height constrained by:
- available viewport
- safe padding
- asset aspect ratio
- listening/temporary cue space
```

Do not stretch images non-uniformly.

---

# 26. Visual Containment

If the concept asset needs containment:

- use transparent background where possible;
- otherwise use a simple neutral frame.

Avoid placing every concept inside an obvious button-shaped card if it makes the experience feel like an adult app.

---

# 27. Primary Tap Feedback

Recommended tap feedback:

- immediate small scale change of approximately 1–2%;
- or one brief muted-teal/neutral highlight;
- optional single soft haptic tick;
- duration approximately 80–120 ms.

The response confirms cause-and-effect.

It must not become:

- a reward;
- a collectible signal;
- a repeated vibration pattern;
- a bright flash;
- an escalating effect.

If haptics do not improve clarity on the family tablet, omit them.

---

# 28. ListeningIndicator Component

Logical component:

```text
ListeningIndicator
```

Possible visual:

- one ring;
- gentle expansion/contraction;
- small microphone glyph where useful.

---

# 29. ListeningIndicator Rules

Must not show:

- waveform;
- audio level;
- percentage;
- score;
- red/green accuracy;
- recording timer.

The state means only:

> Miozira is briefly listening for a speaking attempt.

---

# 30. ListeningIndicator Size

Recommended:

```text
visible diameter: 48–72 dp
```

It does not need to be tappable.

If it is tappable for any reason, effective target must follow child-target requirements.

---

# 31. ResponseCue Component

Logical component:

```text
ResponseCue
```

Possible forms:

- brief halo;
- soft pulse;
- short neutral acknowledgement sound;
- subtle concept animation.

Avoid:

- stars;
- confetti;
- celebratory badges;
- “correct” checkmark.

---

# 32. InactivityHint Component

A subtle temporary hint may appear after inactivity.

Recommended:

- one gentle pulse of the concept;
- optional small tap ripple cue.

Do not add persistent helper text.

---

# 33. Session Transition Component

Transitions should be:

- short;
- consistent;
- calm.

Recommended default:

```text
fade out current concept
fade/scale in next concept
```

Avoid complex page-navigation animations.

---

# 34. Parent Primary Button

Logical component:

```text
ParentPrimaryButton
```

Requirements:

- minimum 48 dp effective height;
- clear text label;
- strong contrast;
- proper semantics;
- visible disabled state if applicable;
- adequate horizontal padding.

---

# 35. Parent Secondary Button

Used for:

- Cancel;
- Not now;
- secondary actions.

Should remain clearly actionable but visually less dominant than primary action.

---

# 36. Parent Destructive Button

Used for:

- Delete family recording;
- Reset data.

Requirements:

- explicit text label;
- distinct destructive styling;
- confirmation before irreversible action where appropriate;
- not color-only.

---

# 37. Parent Card

Logical component:

```text
ParentCard
```

Use for:

- real-world suggestion;
- recent learning;
- microphone explanation summary;
- family recording item.

Recommended:

- radius `large`;
- padding 16–20 dp;
- minimal elevation;
- strong content hierarchy.

Avoid creating a dashboard grid of many small cards.

---

# 38. RealWorldSuggestionCard

Structure:

```text
eyebrow/heading
concept-language context
suggestion sentence
```

Optional:

- concept thumbnail.

No completion checkbox.

---

# 39. RecentLearningRow

Recommended structure:

```text
Concept name / language
Evidence summary
Quick observation actions
```

Example:

```text
Apple — Tamil
Heard recently · Tried saying it

[Recognized outside Miozira]
[Used outside Miozira]
```

---

# 40. Observation Action Component

Because labels are long, use full-width or roomy buttons rather than tiny chips.

Recommended:

```text
Recognized outside Miozira
Used outside Miozira
```

Avoid icon-only actions.

---

# 41. FamilyRecordingRow

Recommended structure:

```text
Concept + language
Standard voice [Play]
Family voice [Record / Play / Replace / Delete]
```

The row may expand rather than showing many small controls simultaneously.

---

# 42. Play Audio Control

Parent audio play controls should have:

- at least 48 dp effective target;
- accessible label;
- clear play/playing state.

Avoid a tiny standalone triangle icon without label/semantics.

---

# 43. Record Control

Parent recording control should clearly distinguish:

```text
idle
recording
recorded
```

Possible visual treatment:

- text label;
- icon;
- state indicator.

Do not rely on red color alone to communicate “recording.”

---

# 44. Toggle Component

Used for microphone attempt detection.

Requirements:

- label;
- description where needed;
- semantic on/off state;
- 48 dp effective target.

---

# 45. Parent App Bar

Prototype 0.1 may use a simple parent app bar:

```text
Back
Title
```

Avoid excessive action icons.

---

# 46. Parent Gate Visual

The hidden parent-entry hotspot should not be part of the visible design system in ordinary child mode.

When the gate is activated, the confirmation UI should switch to adult-oriented styling.

---

# 47. Dialogs

Use dialogs sparingly.

Appropriate uses:

- data reset confirmation;
- delete family recording confirmation if needed;
- parent gate confirmation.

Dialogs must:

- scale with text;
- preserve readable action labels;
- not trap focus incorrectly.

---

# 48. Iconography

Prototype 0.1 should use a small set of conventional icons.

Possible parent icons:

- play;
- record;
- delete;
- settings;
- back;
- microphone.

Child mode should avoid icon-heavy interaction.

---

# 49. Icon Rules

Icons must:

- be visually simple;
- not be the sole source of meaning in parent mode where text is practical;
- have accessible labels when interactive;
- remain consistent in stroke/fill style.

---

# 50. Animation Tokens

Recommended starting tokens:

```text
motion.tapFeedback       = 100 ms
motion.conceptEnter      = 400 ms
motion.response          = 700 ms
motion.transition        = 500 ms
motion.listeningPulse    = 900–1200 ms, max 2 gentle cycles then static
motion.parentStandard    = 200–300 ms
```

These are starting points.

They should be tuned through device/family testing.

---

# 51. Reduced Motion Tokens

When reduced motion is preferred:

```text
motion.tapFeedback       = 0–80 ms
motion.conceptEnter      = 100–200 ms fade
motion.response          = 100–200 ms static highlight
motion.transition        = 100–200 ms fade
motion.listeningPulse    = static indicator
```

Meaning must remain intact.

---

# 52. Easing

Use simple, non-bouncy easing.

Prefer:

- standard ease-in-out;
- gentle deceleration.

Avoid springy/bouncy animations in child mode unless later testing proves a specific instructional benefit.

---

# 53. Sound Tokens

Logical non-word sound roles:

```text
sound.none
sound.tap.optional
sound.acknowledgement.neutral
sound.transition.none
```

The target spoken word remains the dominant audio.

Prototype 0.1 should keep non-word sound effects sparse.

---

# 54. No Background Music Token

There is intentionally no:

```text
sound.backgroundMusic
```

for the core learning session.

---

# 55. Layout Breakpoints

Do not build around one exact device.

A simple adaptive strategy may classify:

```text
compact width
medium width
expanded/tablet width
```

Exact Android window-size implementation belongs in engineering.

Prototype 0.1 is tablet-first, so component behavior should be designed for medium/expanded widths first.

---

# 56. Portrait Component Behavior

In portrait:

- concept remains centered;
- vertical space may hold temporary listening cue below concept;
- parent cards stack vertically;
- buttons are full-width where helpful.

---

# 57. Landscape Component Behavior

In landscape:

- concept remains dominant;
- listening cue may sit below or beside concept if balance improves;
- parent screens may use wider card width or two-column detail layouts cautiously.

Do not add extra child controls because more horizontal space exists.

---

# 58. Child Safe Area

Main concept should remain away from:

- navigation gesture region;
- cutouts;
- extreme corners.

Parent hotspot is the exception but should still be intentionally placed and tested.

---

# 59. Parent Maximum Content Width

On very wide tablets, parent reading content should not stretch edge-to-edge.

Recommended maximum content width:

```text
~720–840 dp
```

centered within the screen.

This keeps text readable.

---

# 60. Empty States

Parent empty states should use:

- short explanation;
- no guilt;
- optional relevant action.

Example:

> “Recent learning will appear here after a session.”

---

# 61. Loading States

Because core data is local, routine parent/child interactions should rarely show loading indicators.

If needed:

- use a restrained progress indicator;
- avoid skeleton dashboards;
- never block child flow for a normal local database read.

---

# 62. Error Components

Parent error component:

```text
short title
plain-language explanation
optional recovery action
```

Child mode should not expose ordinary technical error components.

---

# 63. Success Confirmation

Parent success confirmations should be restrained.

Example:

> “Saved.”

Avoid gamified success components.

---

# 64. Destructive Confirmation

Recommended hierarchy:

```text
Title
What will be removed
Cancel
Destructive action
```

Do not obscure the destructive effect behind vague wording.

---

# 65. Content Imagery Rules

All concept imagery should respect:

- clear silhouette;
- one dominant referent;
- simple background;
- consistent art direction;
- sufficient resolution.

The design system should provide a consistent presentation container, not redefine the content style per concept.

---

# 66. Concept Image Placeholder

During development, placeholders should be obviously non-production.

Do not accidentally ship generic debug icons as learning visuals.

---

# 67. Asset Aspect Ratios

The design system should support different natural concept aspect ratios without forcing them into one crop.

Examples:

- car: wide;
- hand: tall/irregular;
- apple: compact;
- cat: variable.

Use contain-fit behavior by default unless the final visual style says otherwise.

---

# 68. Focus Treatment — Parent Mode

Keyboard/switch/TalkBack focus should have a visible focus indication where platform behavior requires it.

Do not remove system focus styling without replacing it accessibly.

---

# 69. Child Accessibility Semantics

The main child concept target should expose one semantic node where practical.

Avoid separate semantic nodes for:

- decorative shadow;
- halo;
- animation layer;
- background.

---

# 70. Parent Accessibility Semantics

All major parent components should define:

- role;
- label;
- state;
- action.

This is particularly important for:

- record;
- play;
- toggle;
- delete;
- reset;
- observation buttons.

---

# 71. Text Scaling Rules

Parent components must:

- grow vertically;
- wrap labels;
- avoid fixed text boxes;
- preserve buttons at larger font sizes.

Do not truncate critical microphone/privacy/reset text.

---

# 72. Tamil Typography Rules

Tamil text requires:

- sufficient line height;
- no clipping;
- tested font fallback;
- Unicode storage/rendering;
- natural wrapping.

Design review must include real Tamil strings, not Latin placeholders.

---

# 73. Design Tokens — Proposed Summary

```text
BASE
space.unit = 4 dp

TOUCH
touch.adult.minimum = 48 dp
touch.child.secondary.minimum = 72 dp
touch.child.primary = large central region

RADIUS
small  = 8 dp
medium = 16 dp
large  = 24 dp

PARENT TYPE
display      = 32 sp
title.large  = 24 sp
title.medium = 20 sp
body.large   = 18 sp
body         = 16 sp
label        = 14–16 sp

MOTION
tapFeedback    = 100 ms
conceptEnter   = 400 ms
response       = 700 ms
transition     = 500 ms
parentStandard = 200–300 ms

CONTRAST
normal text = ≥4.5:1
large text / essential graphics = ≥3:1
```

The Prototype 0.1 palette is locked in this document. Typeface remains intentionally open pending Tamil/English rendering tests.

---

# 74. Component Inventory — Prototype 0.1

## Child

```text
ConceptStage
ListeningIndicator
ResponseCue
InactivityHint
SessionRestState
```

## Parent

```text
ParentAppBar
ParentCard
ParentPrimaryButton
ParentSecondaryButton
ParentDestructiveButton
RealWorldSuggestionCard
RecentLearningRow
ObservationAction
FamilyRecordingRow
AudioPlayControl
RecordControl
SettingToggle
ConfirmationDialog
PlainErrorMessage
```

This is enough for the vertical slice.

---

# 75. Components Explicitly Not Needed

Prototype 0.1 does not need:

- bottom navigation;
- lesson card grid;
- leaderboard row;
- progress ring;
- XP meter;
- badge;
- streak calendar;
- social avatar;
- chat bubble;
- subscription paywall;
- carousel of courses;
- score chip;
- achievement modal.

---

# 76. Design Review Checklist — Child

Before approving a child screen:

- [ ] Is one concept dominant?
- [ ] Is the whole concept easy to tap?
- [ ] Is there any competing small control?
- [ ] Is reading required?
- [ ] Is the background calm?
- [ ] Does motion serve a purpose?
- [ ] Is there any score/reward pattern?
- [ ] Does the state still make sense with microphone off?
- [ ] Does reduced motion preserve meaning?
- [ ] Does portrait work?
- [ ] Does landscape work?

---

# 77. Design Review Checklist — Parent

Before approving a parent screen:

- [ ] Are touch targets at least 48 dp?
- [ ] Is text readable and scalable?
- [ ] Is Tamil rendered correctly where present?
- [ ] Are interactive icons labeled?
- [ ] Does contrast meet target?
- [ ] Is color supplemented by text/icon/state?
- [ ] Is destructive behavior explicit?
- [ ] Is evidence wording non-judgmental?
- [ ] Is the screen compact rather than dashboard-heavy?
- [ ] Is the primary action obvious?

---

# 78. Design Quality Anti-Patterns

Reject designs that introduce:

- confetti;
- coins;
- stars;
- XP;
- animated mascots that dominate concept attention;
- child tabs;
- tiny controls;
- hidden critical text;
- red/green correctness;
- score rings;
- persistent progress meters;
- dense parent charts;
- unnecessary gradients/visual effects;
- excessive cards in child mode.

---

# 79. Implementation Guidance

The design system should eventually be represented as reusable code tokens/components.

Possible structure:

```text
design/
  tokens/
    Spacing
    Typography
    Shape
    Motion
    ColorRoles
  child/
    ConceptStage
    ListeningIndicator
    ResponseCue
  parent/
    ParentButton
    ParentCard
    ObservationAction
    RecordingControl
```

Exact code architecture belongs in `architecture.md`.

---

# 80. Naming Convention

Component names should describe their semantic role.

Prefer:

```text
ConceptStage
RealWorldSuggestionCard
ObservationAction
```

Avoid:

```text
BlueRoundedBox
BigButton2
ScreenWidgetNew
```

---

# 81. Theme Independence

Domain logic must not depend on:

- color values;
- font size;
- animation duration;
- dark/light theme.

The design system is presentation infrastructure only.

---

# 82. Family-Test Tuning

The following design values should remain easy to adjust after real-device observation:

- concept size;
- child horizontal padding;
- inactivity hint timing;
- listening indicator size;
- animation duration;
- parent card spacing;
- parent type sizes;
- language-switch cue visibility.

---

# 83. Design Freeze Before Family Test

Before the seven-day family test begins, freeze:

- primary child layout;
- core component behavior;
- concept sizing;
- parent gate;
- listening cue;
- motion values;
- primary parent flows.

Only bug-level corrections should be made during the test unless the design is causing clear distress or preventing use.

---

# 84. Relationship to `ui-ux.md`

`ui-ux.md` defines screen structure and behavior.

This document standardizes the reusable visual/interaction building blocks used to implement those screens.

---

# 85. Relationship to `accessibility.md`

`accessibility.md` is authoritative for:

- minimum touch accessibility;
- contrast;
- reduced motion;
- semantics;
- text scaling.

The design system must not override those constraints for aesthetics.

---

# 86. Relationship to `content-spec.md`

`content-spec.md` defines the concept media.

The design system defines how those assets are presented consistently.

---

# 87. Relationship to `architecture.md`

`architecture.md` should decide:

- where tokens live;
- how themes are implemented;
- whether Compose Multiplatform shares components;
- how platform accessibility semantics are applied.

---

# 88. Decision Summary

Miozira Prototype 0.1 uses a deliberately small design system built around:

1. a 4 dp spacing base;
2. very large child touch areas;
3. 48 dp minimum adult controls;
4. 72 dp minimum guidance for any secondary child control;
5. minimal child typography;
6. readable/scalable parent typography;
7. semantic color roles rather than arbitrary colors;
8. strong contrast;
9. calm motion with reduced-motion alternatives;
10. one reusable `ConceptStage` for the child;
11. a small set of conventional parent components;
12. consistent portrait/landscape behavior;
13. no reward/gamification components.

The system should make Miozira easier to build consistently without making the prototype visually or technically heavy.


---

# v0.2 Design Decision — Engagement Without Stimulation

Prototype 0.1 formally distinguishes:

```text
meaningful engagement
≠
sensory stimulation
```

Permitted micro-interactions must satisfy at least one of:

1. clarify cause-and-effect;
2. illustrate the meaning of the concept;
3. identify a temporary system state;
4. reduce uncertainty after a touch.

Examples:

```text
apple: 1–2% press response
car: one short meaningful roll when concept calls for motion
water: one short pour animation when teaching an action/context later
hand: one gentle open/close movement
listening: restrained ring change
```

Not permitted:

```text
idle bouncing
sparkles
confetti
character cheering
random movement
looping animation
reward vibrations
progress celebrations
```

The child should remain interested because the object, sound, action, prediction, repetition, and real-world connection are meaningful.
