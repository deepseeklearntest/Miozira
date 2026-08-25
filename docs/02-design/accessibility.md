# Miozira — Accessibility Specification

**Document:** `accessibility.md`  
**Version:** 0.2  
**Status:** Draft for Prototype 0.1 — Palette & Wellbeing Update  
**Product:** Miozira  
**Prototype:** Android-first, 8 concepts × English + Tamil  
**Target child:** approximately 4–5 years old  
**Last updated:** 2026-08-25

---

## 1. Purpose

This document defines accessibility requirements for Miozira Prototype 0.1.

Accessibility in Miozira has two distinct contexts:

1. **Child mode** — an audio-first, picture-first experience for a pre-reader.
2. **Parent mode** — a conventional adult interface containing text, controls, settings, recordings, privacy information, and learning evidence.

The specification covers:

- preschool motor accessibility;
- touch-target sizing;
- visual clarity and contrast;
- color use;
- audio accessibility;
- speech/microphone accessibility;
- motion and reduced-motion behavior;
- orientation and responsive layouts;
- Android accessibility semantics;
- screen-reader considerations;
- text scaling;
- cognitive accessibility;
- accessibility testing;
- known Prototype 0.1 limitations.

The goal is not to attach an “accessible” label to the product without evidence.

The goal is to deliberately remove avoidable barriers.

---

# 2. Accessibility Philosophy

Miozira's accessibility philosophy is:

> **Make the intended interaction easier to perceive, understand, and perform without turning accessibility into a separate mode.**

Accessibility should normally be built into:

- target size;
- spacing;
- contrast;
- audio clarity;
- interaction simplicity;
- optional speech;
- predictable behavior;
- system semantics.

---

# 3. Accessibility Is Broader Than Disability Compliance

For a 4–5-year-old child, accessible design also means accommodating ordinary developmental variability in:

- motor precision;
- attention;
- speech;
- reading ability;
- response time;
- language exposure;
- ability to understand abstract icons;
- ability to follow instructions.

Therefore many Miozira child-design decisions are accessibility decisions even when no diagnosed disability is involved.

---

# 4. Prototype 0.1 Accessibility Scope

Prototype 0.1 shall actively support:

- imprecise preschool touch;
- no-reading child interaction;
- optional/no speech response;
- portrait and landscape;
- high visual clarity;
- strong contrast;
- low visual clutter;
- reduced unnecessary motion;
- parent-area screen-reader semantics;
- parent-area text scaling;
- clear microphone alternatives.

Prototype 0.1 does **not** claim a fully equivalent learning experience for every child with:

- profound hearing loss;
- blindness;
- severe visual impairment;
- complex motor-access needs;
- switch-access-only interaction.

These are important future design areas, but the initial learning mechanic fundamentally depends on hearing a spoken form and perceiving a concept representation.

---

# 5. Accessibility Priorities

Accessibility requirements use:

- **P0** — required for family-test readiness;
- **P1** — strong requirement;
- **P2** — future-supporting requirement.

---

# 6. Motor Accessibility — Preschool Touch

## ACC-MOTOR-001 — Primary target is substantially oversized — P0

The main child concept shall be far larger than a conventional adult minimum touch target.

The visual itself should occupy a large central portion of the screen, and the effective hit area may safely extend beyond the visible object.

The child should not need fine fingertip precision.

---

## ACC-MOTOR-002 — Adult minimum baseline — P0

Any conventional adult-facing interactive target should meet at least the Android baseline of approximately **48 × 48 dp**.

Child-facing controls should normally exceed this baseline substantially.

---

## ACC-MOTOR-003 — Secondary child target guidance — P1

If Prototype 0.1 introduces any child-facing secondary action, its effective touch region should normally be at least:

> **72 × 72 dp**

and preferably larger when screen space permits.

The main concept target should be much larger than this.

This is a Miozira design target, not a universal Android standard.

---

## ACC-MOTOR-004 — Expanded hit regions — P0

A child target's tappable region may extend beyond its visible artwork when doing so does not overlap another action.

---

## ACC-MOTOR-005 — Target separation — P0

Two child-facing actions shall not be placed so closely that common preschool touch imprecision causes frequent accidental activation.

---

## ACC-MOTOR-006 — No precision gestures — P0

The core child learning loop shall not require:

- dragging;
- pinch;
- tiny sliders;
- double-tap;
- edge swipes;
- multi-finger gestures;
- long-press.

A single tap remains sufficient.

---

## ACC-MOTOR-007 — No timed motor challenge — P0

The child shall not have to tap or speak within a narrow competitive time window to avoid failure.

---

# 7. Accidental Touch Tolerance

## ACC-MOTOR-008 — Debounce rapid taps — P0

Rapid repeated tapping must not:

- overlap audio;
- create duplicate exposures;
- create multiple speaking windows;
- skip multiple items.

---

## ACC-MOTOR-009 — Missed touch should be harmless — P0

A tap outside the main target should not create:

- error feedback;
- negative sound;
- loss of progress.

The screen should simply remain ready.

---

## ACC-MOTOR-010 — Parent controls separated — P0

Parent/destructive controls shall not occupy locations that are likely to be activated during ordinary child tapping.

---

# 8. Speech and Communication Accessibility

## ACC-SPEECH-001 — Speech is optional — P0

The child shall never be required to speak to:

- hear content;
- progress;
- finish an interaction;
- access another concept.

---

## ACC-SPEECH-002 — No pronunciation gate — P0

Speech intelligibility, articulation difference, developmental speech variation, stammering, or accent shall not prevent progression.

---

## ACC-SPEECH-003 — Microphone disabled path — P0

The entire child learning loop shall remain understandable when microphone attempt detection is:

- disabled;
- denied;
- unavailable;
- unreliable.

---

## ACC-SPEECH-004 — No negative interpretation of silence — P0

Silence shall never produce a child-facing failure state.

---

# 9. Visual Accessibility — Core Principles

## ACC-VIS-001 — One dominant focal object — P0

The child screen shall emphasize one concept and minimize unrelated visual competition.

---

## ACC-VIS-002 — Simple background — P0

Primary child content should appear against a quiet background with sufficient separation from the concept.

Busy photographic scenes should not be used as the normal child UI background.

---

## ACC-VIS-003 — Avoid low-contrast decorative treatment — P0

Essential concept boundaries and active cues must remain perceptible under typical tablet viewing conditions.

---

# 10. Contrast

## ACC-VIS-004 — Parent text contrast — P0

Parent-facing normal-size text should target at least:

> **4.5:1**

contrast against its background.

Large text and important non-text graphical controls should target at least:

> **3:1**

where applicable.

These values align with established WCAG/Android accessibility guidance and are used here as a practical design baseline.

---

## ACC-VIS-005 — Child state cues — P0

Temporary child-state indicators such as:

- listening ring;
- tap hint;
- focus outline

should remain clearly distinguishable from the background.

---

## ACC-VIS-006 — Contrast checked, not guessed — P1

Final design-system colors should be evaluated with a contrast-checking tool.

Visual review alone is insufficient.

---

# 11. Color

## ACC-COLOR-001 — No color-only meaning — P0

Miozira shall never rely solely on color to indicate:

- tappability;
- microphone state;
- saved state;
- warning;
- destructive action.

Use an additional:

- shape;
- icon;
- text;
- position;
- semantic label;
- motion cue

as appropriate.

---

## ACC-COLOR-002 — No red/green correctness pattern — P0

Child mode shall not use red and green to encode pronunciation correctness.

Prototype 0.1 has no correctness grading at all.

---

## ACC-COLOR-003 — Color-vision robustness — P1

The design system should remain understandable under common color-vision deficiencies.

This should be checked during design-system validation.


## ACC-COLOR-004 — Locked low-stimulation palette — P1

Prototype 0.1 uses these core values:

```text
child background   #F2EEE7
primary content    #26312E
secondary content  #53605B
primary action     #4F746B
listening          #607C8A
```

Baseline contrast checks:

```text
#26312E / #F2EEE7 ≈ 11.6:1
#53605B / #F2EEE7 ≈ 5.7:1
#4F746B / #FFFFFF ≈ 5.2:1
#607C8A / #F2EEE7 ≈ 3.8:1
```

The listening cue meets a non-text graphical contrast target against the child background, but still requires a shape/state cue so color is not the only signal.

## ACC-COLOR-005 — Saturation restraint — P1

Large UI surfaces should remain warm-neutral or muted.

High saturation should be reserved primarily for the natural visual properties of the learning concept itself.

This reduces competing salience without removing meaningful real-world color.

---

# 12. Images and Concept Recognition

## ACC-IMAGE-001 — Clear object silhouette — P0

Each concept visual should have a clear enough silhouette and foreground/background separation to remain identifiable at tablet viewing distance.

---

## ACC-IMAGE-002 — Avoid ambiguous crops — P0

A concept shall not be cropped so aggressively that its identity becomes unclear.

---

## ACC-IMAGE-003 — Multiple details do not compete — P0

Decorative or contextual details must not become stronger visual targets than the learning concept.

---

# 13. Parent Screen Reader Support

## ACC-SR-001 — Semantic controls — P1

Interactive parent controls should expose appropriate Android accessibility semantics.

Examples:

- role;
- label;
- state;
- action.

If Compose is selected, this should use the Compose semantics system rather than relying only on visual text.

---

## ACC-SR-002 — Icon-only actions need labels — P0

Any icon-only parent control must have an accessible label.

Examples:

- Play;
- Record;
- Delete;
- Back.

---

## ACC-SR-003 — Decorative images excluded — P1

Decorative visuals should not create noisy or meaningless screen-reader stops.

---

## ACC-SR-004 — Logical traversal order — P1

Parent-area accessibility focus order should follow the visual/task hierarchy.

For example:

```text
Suggestion heading
→ suggestion text
→ recent concept
→ recognition action
→ use action
```

---

# 14. Child Screen Reader Considerations

The child mode is intentionally visual/audio-first and is not designed around a reading workflow.

However, interactive child elements should still have meaningful semantics where feasible.

---

## ACC-SR-005 — Main concept semantics — P1

The main concept target should expose:

- an appropriate accessible label;
- tappable/clickable action semantics.

For example, an active Tamil apple item may expose a Tamil concept label and a “play word” action.

---

## ACC-SR-006 — Do not create semantic clutter — P1

Animation layers, decorative backgrounds, halos, and listening effects should not appear as separate accessibility nodes unless they convey meaningful information.

---

## ACC-SR-007 — Avoid contradictory duplicate speech — P1

If an accessibility service speaks the concept label, Miozira should avoid creating confusing simultaneous app speech.

Accessibility-service behavior should be included in device testing.

---

# 15. Important Limitation — Blind / Low-Vision Child Experience

The Prototype 0.1 learning model assumes the child can perceive the concept representation visually.

Adding a screen-reader label does not make the full picture-to-word learning experience equivalent for a blind child.

Therefore Miozira 0.1 should:

- preserve semantic accessibility where feasible;
- not claim full blind-child equivalence;
- document this limitation;
- leave room for future tactile/audio-context learning modes.

---

# 16. Audio Accessibility

## ACC-AUDIO-001 — Target word clarity — P0

The target spoken form shall be:

- clearly articulated;
- free from background music;
- free from competing speech;
- recorded at usable loudness.

---

## ACC-AUDIO-002 — Consistent loudness — P1

Canonical clips should have reasonably consistent perceived loudness across:

- concepts;
- English;
- Tamil.

---

## ACC-AUDIO-003 — No startle audio — P0

Miozira should avoid abrupt loud effects or large volume jumps.

---

## ACC-AUDIO-004 — No music masking — P0

Continuous background music shall not play underneath target-word learning audio.

---

## ACC-AUDIO-005 — Replay available — P0

The child can replay the target spoken form.

Replay is an important accessibility feature for:

- missed audio;
- processing time;
- environmental noise;
- individual listening needs.

---

# 17. Important Limitation — Deaf / Hard-of-Hearing Child Experience

Prototype 0.1 teaches spoken-word associations through sound.

A visual caption alone cannot provide an equivalent spoken-language experience for a pre-reader who cannot hear the target audio.

Therefore Prototype 0.1 should not claim equivalent accessibility for children with profound hearing loss.

Future work may explore:

- sign-language content;
- visual phonological supports;
- caregiver-mediated modes;
- haptic cues;
- captioning for adults;
- multimodal language pathways.

These are outside Prototype 0.1 scope.

---

# 18. Device Volume and Audio Route

## ACC-AUDIO-006 — Respect system audio — P0

Miozira shall use normal platform audio routing and volume behavior.

It shall not override user/device volume to force a specific loudness.

---

## ACC-AUDIO-007 — Headphone/Bluetooth changes — P1

Audio route changes should not crash the learning interaction.

If the route changes, Miozira should recover gracefully.

---

# 19. Cognitive Accessibility

## ACC-COG-001 — One primary task — P0

Each child interaction shall contain one obvious primary action.

---

## ACC-COG-002 — Consistent cause and effect — P0

The same main action should predictably produce the same kind of outcome:

```text
tap concept → hear active word
```

---

## ACC-COG-003 — Minimal instruction load — P0

The child should not need to remember a sequence of written or multi-step instructions.

---

## ACC-COG-004 — No nested child navigation — P0

Child mode shall not require navigating:

- tabs;
- menus;
- lesson trees;
- settings;
- categories.

---

## ACC-COG-005 — Low information density — P0

Only information needed for the current child interaction should remain prominent.

---

## ACC-COG-006 — No performance anxiety cues — P0

No timer, score, failure symbol, leaderboard, or streak shall add cognitive/emotional pressure.

---

# 20. Processing Time

## ACC-COG-007 — Allow response time — P0

The child shall receive a meaningful pause after hearing the word.

The interface should not race immediately to the next concept.

---

## ACC-COG-008 — No escalating prompts — P0

If the child hesitates, prompts shall remain sparse and gentle.

---

## ACC-COG-009 — Replay does not penalize pace — P0

Using replay shall not cause the child to fall behind or miss an artificial deadline.

---

# 21. Motion Accessibility

## ACC-MOTION-001 — Motion has a purpose — P0

Animation should support:

- tap acknowledgement;
- item transition;
- listening state;
- attention cue.

It should not exist merely to stimulate.

---

## ACC-MOTION-002 — No continuous decorative animation — P0

Prototype 0.1 shall avoid:

- endless bouncing;
- floating particles;
- looping character movement;
- flashing;
- rapid parallax;
- animated gradients;
- idle “attention capture” movement.

A listening animation may use at most a small number of gentle cycles before becoming static.

---

## ACC-MOTION-003 — Reduced-motion preference — P1

Where Android/system accessibility settings indicate reduced animation or disabled animator duration, Miozira should reduce or remove non-essential motion.

The experience must remain fully understandable without decorative animation.

---

## ACC-MOTION-004 — No motion-dependent instruction — P0

A child must not need to interpret a complex animation in order to know the essential interaction.

---

## ACC-MOTION-005 — No flashing hazard — P0

Miozira shall not intentionally use rapid flashing effects.

---

# 22. Reduced-Motion Behavior

When reduced motion is preferred:

Replace:

```text
scale pulse + slide + fade
```

with:

```text
static highlight + short fade
```

or an equivalent low-motion cue.

Do not remove:

- state meaning;
- tap acknowledgement;
- listening indication.

---

# 23. Orientation Accessibility

## ACC-LAYOUT-001 — Portrait and landscape — P0

Core child and parent functionality shall work in portrait and landscape.

---

## ACC-LAYOUT-002 — Orientation does not alter task — P0

Rotating the device shall not create a different interaction model.

---

## ACC-LAYOUT-003 — State preservation — P0

Rotation shall not:

- restart the word;
- restart listening;
- create an exposure;
- lose the concept;
- duplicate a parent action.

---

# 24. Responsive Layout

## ACC-LAYOUT-004 — No single-resolution dependency — P0

The interface shall adapt to different supported tablet sizes.

---

## ACC-LAYOUT-005 — Large-screen space is not filled with clutter — P0

A larger tablet does not justify adding more child-facing controls or decorative elements.

Use whitespace.

---

## ACC-LAYOUT-006 — Parent text remains reflowable — P1

Parent screens should reflow when:

- orientation changes;
- font size increases;
- available width changes.

---

# 25. Text Accessibility — Parent Mode

## ACC-TEXT-001 — Text scaling — P1

Parent screens should remain usable with substantially increased Android font scaling.

A practical target is to remain functional up to approximately **200% text scaling** where platform behavior permits.

---

## ACC-TEXT-002 — Do not clip important text — P0

Important parent text such as:

- microphone explanation;
- privacy statement;
- reset warning

shall not become inaccessible because of fixed-height containers.

---

## ACC-TEXT-003 — Avoid text embedded in images — P0

Functional parent text should be real text, not baked into raster images.

---

## ACC-TEXT-004 — Tamil rendering — P0

Tamil script must render correctly with:

- proper Unicode text;
- adequate line height;
- no clipped combining marks;
- no Latin-only font assumptions.

---

# 26. Typography

## ACC-TEXT-005 — Parent readability — P0

Parent typography shall prioritize:

- legibility;
- clear hierarchy;
- reasonable line length;
- adequate spacing.

---

## ACC-TEXT-006 — Child text is non-essential — P0

Any child-visible written word or label is supplementary unless a later product revision explicitly changes the pre-reader design.

---

# 27. Parent Interaction Accessibility

## ACC-PARENT-001 — Destructive action clarity — P0

Actions such as delete/reset shall:

- have clear labels;
- not rely on color alone;
- require explicit confirmation where appropriate.

---

## ACC-PARENT-002 — Record controls state — P1

Family recording controls should clearly expose whether they are:

- idle;
- recording;
- available to play;
- deleting.

Accessible semantics should reflect those states.

---

## ACC-PARENT-003 — Toggle semantics — P1

Settings such as microphone attempt detection should expose:

- control label;
- checked/on-off state;
- action.

---

# 28. Parent Gate Accessibility

The parent gate creates a tension:

- it should be discoverable/usable by an adult;
- it should not be obvious to the child.

---

## ACC-GATE-001 — Alternate accessible adult path — P1

If the hidden long-press hotspot is difficult to operate with assistive technology, Prototype 0.1 should provide an adult-accessible alternative where practical.

Potential options:

- accessible action exposed through semantics;
- system menu entry;
- explicit parent-mode route from setup/rest state.

The final mechanism will be tested.

---

## ACC-GATE-002 — Do not require precise long-press only — P1

The product should avoid making the hidden gesture the sole possible parent-access method if it proves inaccessible in testing.

---

# 29. Haptics

## ACC-HAPTIC-001 — Haptics optional and informational — P2

A single subtle haptic acknowledgement may be evaluated for:

- child tap cause-and-effect;
- parent control confirmation.

Haptics must remain optional and non-essential.

They must not encode:

- correctness;
- reward;
- urgency;
- progress.

Repeated or patterned child haptics are out of scope for Prototype 0.1.

It must not be essential to understanding the interaction.

---

## ACC-HAPTIC-002 — No strong repetitive vibration — P0

The child experience shall not use aggressive or repeated vibration effects.

---

# 30. Language Accessibility

## ACC-LANG-001 — Language explicitly identified in data — P0

English and Tamil content shall carry explicit language metadata.

---

## ACC-LANG-002 — Do not assume system locale equals learning language — P0

A tablet whose system UI is English may still play a Tamil learning item.

Learning-language behavior shall not be inferred solely from the device locale.

---

## ACC-LANG-003 — Tamil parent labels reviewed — P1

If Tamil parent UI text is added, it must be human-reviewed rather than mechanically translated and shipped without review.

---

# 31. Accessibility Semantics and Learning Language

Where accessibility labels refer to a target concept, they should use an appropriate language/localization context when technically feasible.

Example:

```text
active pair = concept.apple + Tamil
```

should not necessarily expose an English-only accessibility label if that changes the learning experience.

Exact locale handling belongs in engineering design.

---

# 32. System Accessibility Services

Prototype testing should include common Android accessibility configurations, including where practical:

- TalkBack;
- increased font size;
- display size changes;
- reduced/disabled animation;
- high-contrast/outline-text related settings where available;
- switch/keyboard navigation in parent mode.

---

# 33. Android 16 Considerations

Android 16 adds/improves accessibility APIs around semantic information and text contrast support.

Miozira should prefer standard platform/Compose accessibility semantics instead of custom-rendering critical controls in ways that bypass platform accessibility behavior.

---

# 34. Compose Semantics

If Compose or Compose Multiplatform is selected:

Use semantic properties for:

- labels;
- roles;
- states;
- actions;
- testing.

Do not assume visible UI automatically produces the desired accessibility tree.

---

# 35. Semantics and Automated Testing

The same semantic tree used by accessibility services can support UI testing.

Accessibility labels should therefore be:

- meaningful;
- stable enough for testing where appropriate;
- not populated with meaningless developer-only text.

---

# 36. Accessibility Does Not Mean More Child UI

Do not “solve” accessibility by adding:

- extra buttons;
- text everywhere;
- persistent helper labels;
- visual clutter.

The objective is to make the simple interaction more usable.

---

# 37. Environment Accessibility

Prototype 0.1 cannot control family environment, but design should account for:

- moderate background noise;
- different tablet viewing distances;
- varying room brightness;
- speaker quality.

This supports:

- clear audio;
- high visual separation;
- replay;
- large targets.

---

# 38. Error Accessibility

## ACC-ERR-001 — Child errors non-technical — P0

A child shall never need to understand a technical error message.

---

## ACC-ERR-002 — Parent errors readable and actionable — P0

Parent errors should use concise text and expose accessible semantics.

---

## ACC-ERR-003 — Error not encoded only by color — P0

Parent errors need text/icon/state information in addition to color.

---

# 39. Accessibility and Privacy

Accessibility features must not weaken Miozira's privacy rules.

For example:

- enabling TalkBack does not justify uploading content;
- accessibility logs must not contain child raw speech;
- accessibility labels should not expose unnecessary personal information.

---

# 40. Accessibility and Microphone

The microphone is not an accessibility requirement.

A child unable or unwilling to produce detectable speech still receives the complete content path.

This is an intentional accessibility safeguard.

---

# 41. Accessibility and Family Recordings

Family recordings may benefit:

- familiarity;
- accent/register matching;
- home-language continuity.

But family voice is optional.

Canonical content remains available.

---

# 42. Accessibility QA — Child Mode

Before family testing, manually verify:

- [ ] main concept can be tapped imprecisely;
- [ ] missed taps do not create negative feedback;
- [ ] no small control is required;
- [ ] child flow works without reading;
- [ ] child flow works without microphone;
- [ ] visual hierarchy is clear;
- [ ] target word is audible without background music;
- [ ] replay is easy;
- [ ] state cues are not color-only;
- [ ] reduced motion preserves meaning;
- [ ] portrait works;
- [ ] landscape works;
- [ ] rotation does not duplicate learning events.

---

# 43. Accessibility QA — Parent Mode

Before family testing, verify:

- [ ] controls meet adult touch-target baseline;
- [ ] text contrast meets target;
- [ ] icon-only controls have labels;
- [ ] TalkBack can identify important controls;
- [ ] traversal order is sensible;
- [ ] increased font scaling does not hide critical content;
- [ ] Tamil text renders correctly;
- [ ] reset warning is readable;
- [ ] microphone explanation is reachable/readable;
- [ ] family recording controls expose state;
- [ ] orientation changes do not break forms.

---

# 44. Accessibility Test Devices / Configurations

Prototype validation should include at least:

1. intended family tablet in portrait;
2. intended family tablet in landscape;
3. lower-capability Android tablet/emulator profile;
4. TalkBack enabled in parent mode;
5. large font/display scaling;
6. animation/reduced-motion configuration;
7. microphone denied;
8. speaker volume at a moderate level.

---

# 45. Accessibility Test With Child

Observe:

- frequency of missed taps;
- accidental activation;
- whether the concept is visually identifiable;
- whether the listening cue is understandable;
- whether motion distracts;
- whether replay is independently discovered;
- whether inactivity prompts are too subtle or too intrusive.

Do not treat difficulty as proof that the child is incapable.

First investigate the product.

---

# 46. Accessibility Test With Parent

Ask:

- Is anything too small?
- Is Tamil text readable?
- Is the parent gate operable?
- Is the microphone explanation understandable?
- Are recording controls clear?
- Can real-world use be logged without precision tapping?
- Does larger text break the layout?

---

# 47. Prototype Accessibility Metrics

Useful product-level measures may include:

```text
missed_tap_rate during observed test
accidental_parent_gate_entries
parent_task_completion
orientation_failures
TalkBack-labeled-control coverage
contrast-check failures
```

These are QA measures.

They are not child metrics.

---

# 48. Accessibility Invariants

### ACC-INV-001
The child can complete the core loop without reading.

### ACC-INV-002
The child can complete the core loop without speaking.

### ACC-INV-003
The child does not need precision gestures.

### ACC-INV-004
The child primary target is substantially larger than the adult minimum.

### ACC-INV-005
No essential state uses color alone.

### ACC-INV-006
No pronunciation correctness is conveyed visually or audibly.

### ACC-INV-007
Reduced motion does not remove essential meaning.

### ACC-INV-008
Orientation does not change the task model.

### ACC-INV-009
Parent destructive controls have explicit labels/confirmation.

### ACC-INV-010
Critical parent text remains real text rather than text embedded in images.

### ACC-INV-011
Accessibility services do not create new learning evidence.

### ACC-INV-012
Screen recomposition/accessibility focus does not count as exposure.

### ACC-INV-013
Microphone accessibility fallback is always available.

### ACC-INV-014
Miozira does not claim accessibility equivalence it has not implemented or tested.

---

# 49. Known Prototype 0.1 Limitations

Prototype 0.1 has meaningful accessibility limitations.

## Visual dependency

The child learning loop depends strongly on seeing a visual concept representation.

## Auditory dependency

The language-learning objective depends strongly on hearing spoken forms.

## Limited alternative input

The core child interaction is touch-first; advanced switch/scanning interaction is not yet a validated child mode.

## Parent UI language

Parent UI may initially be English-only.

## Accessibility validation scale

The initial family test is too small to establish broad accessibility effectiveness.

These limitations should remain visible in product planning rather than being hidden.

---

# 50. Future Accessibility Research Areas

After Prototype 0.1, research may consider:

- blind/low-vision concept teaching;
- deaf/hard-of-hearing multilingual learning;
- sign-language support;
- AAC-compatible interaction;
- switch access;
- external keyboard access;
- haptic/tactile learning;
- neurodivergent sensory profiles;
- customizable motion/sound intensity;
- parent-mode Tamil localization;
- broader motor-access requirements.

These require dedicated research rather than superficial feature additions.

---

# 51. Relationship to `ui-ux.md`

`ui-ux.md` defines:

- layout;
- states;
- target placement;
- parent flows.

This document imposes accessibility constraints on those designs.

---

# 52. Relationship to `design-system.md`

`design-system.md` must define measurable tokens for:

- contrast-safe colors;
- text sizes;
- touch-target sizes;
- spacing;
- focus treatment;
- motion duration;
- reduced-motion variants.

---

# 53. Relationship to `interaction-states.md`

Accessibility-service events, focus changes, recomposition, orientation, and reduced-motion changes must not accidentally become learning/domain events.

---

# 54. Relationship to `audio.md`

`audio.md` must implement:

- clear canonical playback;
- predictable volume behavior;
- route recovery;
- no overlapping instructional speech;
- replay.

---

# 55. Relationship to `microphone.md`

`microphone.md` must preserve the speech-accessibility rule:

> microphone detection is optional and never a gate to learning.

---

# 56. Relationship to `testing.md`

`testing.md` should include:

- semantic/UI tests;
- touch-target checks;
- contrast checks;
- large-text tests;
- TalkBack manual tests;
- reduced-motion tests;
- orientation accessibility tests.

---

# 57. Standards and Platform Guidance Used

Prototype 0.1 uses the following as practical references rather than claiming formal certification:

- Android core app quality guidance, including **48 dp** touch targets and contrast expectations;
- Android/Compose semantics for accessibility and testing;
- WCAG 2.2 contrast principles as a useful parent-interface baseline;
- modern Android accessibility settings and APIs.

Miozira is a native/mobile child-learning product, so web standards are used as design guidance where relevant, not as a claim of WCAG conformance.

---

# 58. Decision Summary

Prototype 0.1 accessibility is based on:

1. a very large primary child target;
2. no reading requirement;
3. no speaking requirement;
4. no precision gesture requirement;
5. low visual complexity;
6. strong contrast;
7. no color-only state;
8. clear, replayable target audio;
9. minimal non-essential motion;
10. reduced-motion compatibility;
11. portrait and landscape support;
12. accessible parent semantics and text scaling;
13. explicit microphone fallback;
14. honest documentation of visual/hearing limitations.

The guiding principle is:

> **remove avoidable barriers without pretending the first prototype already solves every accessibility need.**
