# Miozira — Content Specification

**Document:** `content-spec.md`  
**Version:** 0.1  
**Status:** Draft for Prototype 0.1  
**Product:** Miozira  
**Prototype:** 8 concepts × English + Tamil  
**Target child:** approximately 4–5 years old  
**Last updated:** 2026-08-25

---

## 1. Purpose

This document defines the content package required for every Miozira Prototype 0.1 concept.

It specifies:

- concept identity;
- language forms;
- canonical audio;
- family audio;
- visual assets;
- prompts;
- metadata;
- file naming;
- content validation;
- Tamil wording review;
- the initial eight-concept inventory.

This document defines **what content must exist and how it is represented**.

It does not define:

- database table structure;
- UI layout;
- playback implementation;
- microphone processing;
- adaptive scheduling.

Those belong in their respective technical documents.

---

# 2. Content Philosophy

Miozira content should support:

> **concept → sound → interaction → later real-world use**

Content must remain:

- concrete;
- familiar;
- simple;
- visually clear;
- naturally spoken;
- culturally sensible;
- suitable for a pre-reader;
- easy to connect to real life.

Prototype 0.1 is not a vocabulary catalog.

It is a test of whether a small set of carefully selected concepts can support the Miozira learning loop.

---

# 3. Canonical Content Unit

The canonical content unit is the:

> **Concept**

A concept has:

- one stable concept ID;
- one or more visual representations;
- one spoken form per supported language;
- one canonical audio asset per language;
- optional family recordings;
- optional action/context metadata;
- learning metadata.

Example:

```text
concept_id: concept.apple

concept:
    apple

languages:
    English → "apple"
    Tamil   → "ஆப்பிள்"
```

---

# 4. Stable Concept IDs

Concept IDs shall be:

- language-independent;
- stable across app releases;
- ASCII-safe;
- lowercase;
- human-readable;
- not based on translated words.

Recommended pattern:

```text
concept.apple
concept.ball
concept.cup
concept.hand
concept.nose
concept.cat
concept.car
concept.water
```

The ID shall not change if the preferred Tamil term changes later.

---

# 5. Language IDs

Prototype 0.1 uses:

```text
en
ta
```

Recommended canonical identifiers:

```text
language.english
language.tamil
```

or, in structured data:

```text
language_code: en
language_code: ta
```

The architecture may use BCP 47 language tags where useful.

Possible future expansion:

```text
en-IN
en-US
ta-IN
es-ES
ar
ja-JP
```

Prototype 0.1 does not require accent/region variants beyond reviewed English and Tamil canonical audio.

---

# 6. Concept-Language Pair

Every concept-language pair must contain:

```text
concept_id
language_code
spoken_form
canonical_audio_asset
review_status
```

Optional future fields may include:

```text
phonetic_note
regional_variant
usage_note
alternate_form
```

---

# 7. Required Prototype Content

Prototype 0.1 contains exactly these eight concepts:

1. apple
2. ball
3. cup
4. hand
5. nose
6. cat
7. car
8. water

Each concept requires:

- one primary visual;
- English spoken form;
- Tamil spoken form;
- English canonical audio;
- Tamil canonical audio;
- metadata;
- content review;
- real-world prompt eligibility.

---

# 8. Content Inventory

## CT-001 — Apple

```text
concept_id: concept.apple
English: apple
Tamil candidate: ஆப்பிள்
```

Content role:

- familiar food;
- easy visual referent;
- useful snack-time real-world transfer;
- concrete noun.

---

## CT-002 — Ball

```text
concept_id: concept.ball
English: ball
Tamil candidate: பந்து
```

Content role:

- familiar toy/object;
- movement-friendly;
- useful for “find/bring/give” future expansion.

---

## CT-003 — Cup

```text
concept_id: concept.cup
English: cup
Tamil candidate: TBD after family/native review
```

Potential Tamil forms include:

- கப்
- கோப்பை

Prototype 0.1 must not lock this item purely from dictionary preference.

The selected form should reflect:

- natural household usage;
- child familiarity;
- family register;
- pronunciation clarity;
- consistency with future content.

---

## CT-004 — Hand

```text
concept_id: concept.hand
English: hand
Tamil candidate: கை
```

Content role:

- body part;
- movement-friendly;
- supports future actions such as “show your hand” or “wash hands.”

---

## CT-005 — Nose

```text
concept_id: concept.nose
English: nose
Tamil candidate: மூக்கு
```

Content role:

- body part;
- supports physical interaction;
- easy real-world transfer.

---

## CT-006 — Cat

```text
concept_id: concept.cat
English: cat
Tamil candidate: பூனை
```

Content role:

- familiar animal;
- visually distinct;
- emotionally engaging without requiring reward mechanics.

---

## CT-007 — Car

```text
concept_id: concept.car
English: car
Tamil candidate: கார்
```

Content role:

- familiar vehicle;
- common household/play object;
- supports future phrases such as “red car” and “bring the car.”

---

## CT-008 — Water

```text
concept_id: concept.water
English: water
Tamil candidate: தண்ணீர்
```

Preferred Prototype 0.1 direction:

> use natural household Tamil rather than overly formal vocabulary.

`தண்ணீர்` is therefore the leading candidate over formal `நீர்`, subject to family/native review.

---

# 9. Tamil Content Review Policy

Tamil content must not be created by direct English translation alone.

Every Tamil spoken form must be reviewed for:

- natural household use;
- age appropriateness;
- pronunciation;
- regional/register suitability;
- ambiguity;
- unnecessary formality.

Prototype 0.1 should prefer the form a child is realistically likely to hear in family life.

---

# 10. Tamil Review Status

Each Tamil item should have one of:

```text
DRAFT
FAMILY_REVIEWED
NATIVE_REVIEWED
APPROVED
```

No Tamil canonical audio should be recorded as final until the spoken form reaches at least:

```text
FAMILY_REVIEWED
```

Preferred before family test:

```text
APPROVED
```

---

# 11. English Content Review Policy

English target words should use simple, common spoken forms.

Prototype 0.1 should avoid:

- unnecessary regional vocabulary;
- multi-word definitions;
- formal wording;
- synonyms that create concept ambiguity.

Example:

Prefer:

```text
car
```

not:

```text
automobile
```

---

# 12. Canonical Audio

Canonical audio is the primary reviewed pronunciation model for a concept-language pair.

Each pair shall have exactly one active canonical recording in Prototype 0.1.

---

## CT-AUDIO-001 — Human speech preferred

Prototype 0.1 should use human-recorded canonical speech where practical.

The goal is:

- natural rhythm;
- clear pronunciation;
- child-appropriate pacing;
- consistent voice quality.

Synthetic TTS is not required and should not be introduced merely for convenience if high-quality human recordings are available.

---

## CT-AUDIO-002 — Word-only canonical clip

The canonical word clip should normally contain only the target spoken form.

Example:

```text
"Apple."
```

not:

```text
"This is an apple. Can you say apple?"
```

Interaction prompts belong in separate audio assets.

---

## CT-AUDIO-003 — Clean recording

Canonical audio should avoid:

- music;
- room echo;
- strong background noise;
- clipping;
- excessive reverb;
- multiple speakers;
- unnecessary leading/trailing silence.

---

## CT-AUDIO-004 — Consistent loudness

Canonical recordings should have reasonably consistent perceived loudness.

The child should not experience one word as very quiet and another as startlingly loud.

---

## CT-AUDIO-005 — Natural pacing

Words should be spoken:

- clearly;
- naturally;
- without exaggerated syllable-by-syllable teaching unless intentionally required;
- without unnaturally slow pronunciation.

---

# 13. Audio Speaker Metadata

Each canonical recording should preserve metadata such as:

```text
speaker_id
language
regional_variant
recording_version
review_status
```

Prototype 0.1 does not need to show speaker metadata to the child.

---

# 14. Family Audio

Family audio is optional user-generated audio associated with a concept-language pair.

It is distinct from canonical audio.

Example:

```text
concept.apple + ta
canonical_audio = reviewed Tamil speaker
family_audio = parent/grandparent recording
```

---

## CT-FAMILY-001 — Canonical preserved

Adding a family recording shall not delete canonical audio.

---

## CT-FAMILY-002 — Local storage

Family recordings remain local to the device in Prototype 0.1.

---

## CT-FAMILY-003 — Parent control

The parent may:

- record;
- listen;
- replace;
- delete.

---

## CT-FAMILY-004 — Same learning pair

Family audio does not create a separate vocabulary item.

Both canonical and family audio refer to the same concept-language pair.

---

# 15. Visual Asset Requirements

Every concept must have one primary visual asset.

---

## CT-VIS-001 — Clear referent

The image must make the intended concept easy to identify.

Avoid:

- visually ambiguous objects;
- cluttered scenes;
- multiple competing objects;
- decorative details that distract from the referent.

---

## CT-VIS-002 — One main object

The primary asset should normally show one dominant instance of the concept.

Example:

For `apple`:

Prefer:

> one clear apple

over:

> a fruit basket containing apples, bananas, grapes, plates, and people.

---

## CT-VIS-003 — Neutral background

The visual should use a simple or subdued background so the concept remains dominant.

---

## CT-VIS-004 — Consistent style

The eight primary visuals should feel like one product set.

Do not mix:

- realistic photo;
- clipart;
- 3D render;
- cartoon mascot;
- watercolor;

without an intentional design reason.

---

# 16. Photo vs Illustration

Prototype 0.1 has not yet permanently chosen between:

- real photographs;
- realistic illustrations;
- simplified illustrations.

The selected direction should be tested for:

- recognition;
- visual clarity;
- consistency;
- low stimulation;
- real-world transfer.

The content schema should not make future visual replacement difficult.

---

# 17. Visual Variants

Prototype 0.1 requires:

> one primary visual per concept.

A later prototype may support variants such as:

```text
apple_red
apple_green
car_toy
car_real
cat_white
cat_brown
```

Variants can help test category generalization.

They are not required for 0.1.

---

# 18. Visual Asset Technical Neutrality

This content specification does not lock:

- PNG vs WebP vs vector;
- resolution;
- compression;
- density buckets.

Those belong in `ui-ux.md`, `architecture.md`, and performance/asset implementation guidance.

However, source assets should be retained at sufficient quality to produce future platform-specific outputs.

---

# 19. Child-Facing Text

Prototype 0.1 does not require visible written target words in the child experience.

The content package may store written forms for:

- parent UI;
- internal validation;
- accessibility;
- development tooling.

Written text shall not become necessary for the child's core interaction.

---

# 20. Prompt Content Categories

Miozira content should separate:

### A. Target word

Example:

```text
"Ball."
```

### B. Interaction prompt

Example:

```text
"Your turn."
```

### C. Acknowledgement

Example:

```text
"Mm-hm."
```

### D. Movement prompt

Example:

```text
"Touch your nose."
```

### E. Parent real-world suggestion

Example:

```text
"During play, say the Tamil word for ball while handing it over."
```

These should not be embedded into one long audio clip.

---

# 21. Prompt Reuse

Common UI prompts may be reused across concepts.

Example:

```text
prompt.your_turn.en
prompt.your_turn.ta
```

This avoids duplicating identical prompt files for all eight concepts.

---

# 22. Prompt Language Quality

Prompts must be:

- short;
- naturally spoken;
- age-appropriate;
- non-judgmental;
- easy to understand.

Avoid:

- formal teaching language;
- exam language;
- technical language;
- long instructions.

---

# 23. Acknowledgement Content

Acknowledgements should confirm participation, not correctness.

Allowed direction:

- warm neutral phrase;
- soft affirmative sound;
- gentle non-verbal cue.

Avoid:

- “Correct!”
- “Perfect!”
- “Wrong!”
- “Try harder!”
- “Excellent pronunciation!”

unless future technology and product policy explicitly support such claims.

---

# 24. Movement Prompt Content

Prototype 0.1 may use movement prompts with body/object concepts.

Possible candidates:

```text
hand → show your hand
nose → touch your nose
ball → find the ball
car → bring the car
cup → find the cup
```

Movement prompts should be:

- optional;
- simple;
- concrete;
- not scored.

---

# 25. Real-World Suggestion Content

Each concept should have at least one candidate real-world context.

Examples:

| Concept | Possible natural context |
|---|---|
| apple | snack time |
| ball | play time |
| cup | drink/snack time |
| hand | washing/dressing |
| nose | bath/getting ready |
| cat | outdoors/book/toy |
| car | play/travel |
| water | meals/bath/drinking |

Suggestions should encourage natural use.

They should not instruct parents to quiz.

---

# 26. Real-World Suggestion Template

Recommended structure:

```text
[ordinary moment] + [natural use of target word]
```

Example:

> “At snack time, say the Tamil word for apple while handing over a slice.”

Avoid:

> “Ask your child what apple means in Tamil.”

---

# 27. Content Metadata

Recommended concept metadata:

```yaml
concept_id: concept.apple
category: food
prototype_enabled: true
primary_visual: concept_apple_primary
movement_eligible: false
real_world_contexts:
  - snack
  - kitchen
```

Recommended language metadata:

```yaml
language_code: ta
spoken_form: ஆப்பிள்
canonical_audio: audio_concept_apple_ta_v1
review_status: APPROVED
```

---

# 28. Suggested Concept Categories

Prototype 0.1 may categorize concepts internally:

```text
food:
  apple
  water

object/toy:
  ball
  cup
  car

body:
  hand
  nose

animal:
  cat
```

Categories are metadata only.

The child does not need to navigate category menus.

---

# 29. File Naming Convention

Recommended naming should be predictable and platform-neutral.

### Visual

```text
concept_apple_primary.webp
concept_ball_primary.webp
```

### Canonical word audio

```text
word_apple_en_v1.m4a
word_apple_ta_v1.m4a
```

### Common prompt audio

```text
prompt_your_turn_en_v1.m4a
prompt_your_turn_ta_v1.m4a
```

### Movement prompt

```text
movement_touch_nose_en_v1.m4a
movement_touch_nose_ta_v1.m4a
```

Actual codecs/extensions may change later without changing logical asset IDs.

---

# 30. Logical Asset IDs

Code and database records should prefer logical asset identifiers rather than hardcoded filenames.

Example:

```text
audio.word.apple.en.v1
audio.word.apple.ta.v1
visual.concept.apple.primary
```

This allows the underlying file format to change later.

---

# 31. Content Versioning

Each canonical content item should have a version.

Example:

```text
spoken_form_version: 1
audio_version: 1
visual_version: 1
```

A changed recording or word choice should not silently overwrite provenance.

---

# 32. Content Review Status

Recommended statuses:

```text
DRAFT
IN_REVIEW
APPROVED
REJECTED
REPLACED
```

Prototype builds intended for family testing should only use:

```text
APPROVED
```

canonical content.

---

# 33. Content Provenance

For each externally sourced or commissioned asset, record:

- creator/source;
- ownership/license;
- modification rights;
- attribution requirement;
- approval status.

Original family recordings are local user content and should not be redistributed.

---

# 34. Rights Requirement

Prototype 0.1 must not ship unlicensed:

- stock images;
- scraped web images;
- commercial audio;
- copyrighted character art;
- trademark-heavy promotional images.

Visual and audio assets should have clear usage rights.

---

# 35. Cultural Neutrality

Prototype concept visuals should avoid unnecessary cultural assumptions.

Examples:

- `cup` should visually read as a normal cup, not a culturally niche ceremonial object;
- `car` should clearly read as a car;
- `water` should not require interpreting a complex branded bottle.

The goal is concept clarity.

---

# 36. Brand Avoidance

Primary concept visuals should avoid prominent:

- product logos;
- brand marks;
- packaging;
- character franchises.

Example:

For apple:

Prefer a plain apple, not branded supermarket packaging.

---

# 37. Content Ambiguity Check

Before approval, reviewers should ask:

> “Could a preschool child reasonably name this image as something else?”

Examples:

A visual intended as `cup` should not look equally like:

- mug;
- glass;
- bowl.

A `car` should not look like:

- van;
- bus;
- truck.

---

# 38. Singular Form

Prototype 0.1 should use singular base nouns.

Examples:

```text
apple
ball
cat
car
hand
nose
cup
water
```

Water is naturally mass/non-count.

Plural teaching is deferred.

---

# 39. Articles

Canonical word audio should normally omit English articles.

Prefer:

> “Apple.”

not:

> “An apple.”

This keeps the canonical unit focused on the spoken label.

Phrases may later introduce grammar in context.

---

# 40. Written Capitalization

Internal English written forms should normally use lowercase:

```text
apple
ball
cup
```

UI capitalization may vary by context.

Canonical concept identity remains lowercase.

---

# 41. Tamil Script Storage

Tamil text shall be stored as Unicode.

The implementation must avoid:

- transliteration-only storage;
- image-rendered text as the canonical data source;
- Latin-only schema assumptions.

---

# 42. Transliteration

Tamil transliteration may be useful internally for:

- developer reference;
- pronunciation review;
- tooling.

It should not replace Tamil script as canonical content.

Example internal-only metadata:

```text
spoken_form: தண்ணீர்
romanization_note: thanneer
```

Romanization convention must not be treated as linguistically authoritative unless standardized later.

---

# 43. Alternate Forms

If multiple natural spoken forms exist, content should support:

```text
canonical_form
alternate_forms[]
usage_note
```

Prototype 0.1 should select **one active canonical form** for the child experience to avoid unnecessary ambiguity.

---

# 44. Cup Decision

`cup` is the main known lexical decision requiring explicit review.

Candidate forms:

```text
கப்
கோப்பை
```

Decision criteria:

1. what the child naturally hears at home;
2. what Tamil-speaking families commonly use in comparable context;
3. whether the chosen visual matches the word;
4. whether future phrases sound natural;
5. whether the term is too formal or too English-borrowed for the desired register.

Decision must be documented before final Tamil audio is recorded.

---

# 45. Water Decision

Preferred Prototype 0.1 candidate:

```text
தண்ணீர்
```

Reason:

- natural household use;
- appropriate everyday register;
- directly relevant to family life.

Formal:

```text
நீர்
```

may be stored as an alternate/reference form but should not automatically replace the household term.

---

# 46. Content Package Example

A complete logical content package may resemble:

```yaml
concept:
  id: concept.apple
  category: food
  primary_visual: visual.concept.apple.primary
  movement_eligible: false

languages:
  - code: en
    spoken_form: apple
    canonical_audio: audio.word.apple.en.v1
    review_status: APPROVED

  - code: ta
    spoken_form: ஆப்பிள்
    canonical_audio: audio.word.apple.ta.v1
    review_status: APPROVED

real_world_contexts:
  - snack
  - kitchen
```

This is illustrative, not the final schema.

---

# 47. Content Validation Rules

Before a family-test build, validation should confirm:

### Concept completeness

- all 8 canonical concept IDs exist;
- no duplicates;
- no unexpected extra canonical concepts.

### Language completeness

- English entry exists for every concept;
- Tamil entry exists for every concept.

### Audio completeness

- canonical English audio exists;
- canonical Tamil audio exists;
- referenced assets resolve.

### Visual completeness

- one primary visual exists per concept.

### Review completeness

- no active canonical item remains `DRAFT`;
- Tamil wording decisions are recorded.

### Rights completeness

- asset provenance/usage rights are known.

---

# 48. Content QA Checklist

For each concept ask:

- [ ] Is the concept visually obvious?
- [ ] Is only one main referent dominant?
- [ ] Does the English word match the visual?
- [ ] Does the Tamil word match the visual?
- [ ] Is Tamil wording natural for a child/family context?
- [ ] Is English pronunciation clear?
- [ ] Is Tamil pronunciation clear?
- [ ] Are audio levels consistent?
- [ ] Is background noise absent?
- [ ] Are asset rights clear?
- [ ] Is there at least one real-world-use context?
- [ ] Is the content free from unnecessary text dependency?
- [ ] Does the concept remain useful in both portrait and landscape presentation?

---

# 49. Content Test With Child

Before treating visual/audio content as final, observe:

- does the child identify the intended object?
- does the child tap the expected area?
- does the image attract attention without distracting detail?
- is the word audible and understandable?
- does the child replay it?
- does the child confuse the concept with another object?
- does either language pronunciation cause obvious confusion?

This is usability observation, not a formal vocabulary test.

---

# 50. Content Test With Adults

At least one fluent reviewer should validate each language pack.

For Tamil specifically, reviewers should evaluate:

- naturalness;
- family register;
- pronunciation;
- consistency;
- lexical choice.

For English:

- pronunciation;
- clarity;
- unnecessary accent ambiguity.

---

# 51. Family Voice Review

Family recordings do not need editorial approval before local use.

However, the parent UI should encourage:

- short recording;
- one clear word;
- minimal background noise;
- natural speech.

Miozira should not claim family audio is canonical pronunciation.

---

# 52. Future Phrase Expansion

The content structure should allow future growth from nouns into:

```text
concept
→ adjective + noun
→ action phrase
→ short functional phrase
```

Examples:

```text
apple
→ red apple
→ give me the apple

hand
→ my hand
→ show your hand
→ wash your hands

car
→ red car
→ find the car
→ bring the car
```

Prototype 0.1 does not require these phrase layers.

---

# 53. Future Content Types

The content model should be capable of later supporting:

- nouns;
- body parts;
- actions;
- verbs;
- adjectives;
- short phrases;
- movement instructions;
- contextual scenes.

Prototype 0.1 only needs the eight noun/concrete concept packages plus a very small number of interaction/movement prompts.

---

# 54. Content Must Not Become Curriculum Creep

Do not expand Prototype 0.1 with:

- alphabet;
- numbers;
- colours;
- shapes;
- stories;
- songs;
- spelling;
- grammar;
- reading;
- writing;

merely because content infrastructure exists.

Those require separate scope decisions.

---

# 55. Initial Content Deliverables

Before Prototype 0.1 can be considered content-complete, the project needs:

### Visuals

```text
8 approved primary concept visuals
```

### Canonical word audio

```text
8 English word recordings
8 Tamil word recordings
```

### Interaction prompts

A very small reviewed set in both languages where required.

### Movement prompts

Only those actually used in the Prototype 0.1 session design.

### Parent suggestions

At least one usable real-world suggestion template/context per concept.

---

# 56. Recommended Prototype Prompt Set

Keep the initial spoken UI vocabulary minimal.

Potential categories:

```text
your_turn
listen
again
gentle_acknowledgement
```

Not all need explicit speech in both languages if a non-verbal cue works better.

Content should be added only when the session flow proves it is needed.

---

# 57. Source-of-Truth Rule

There should be one authoritative structured content definition for:

- concept IDs;
- language forms;
- asset references;
- review status;
- content version.

Do not manually duplicate this information independently across:

- code;
- UI files;
- database seed logic;
- spreadsheets;
- audio folder names.

The architecture can decide the storage format later.

---

# 58. Content Change Policy

A canonical content change should record:

- concept ID;
- language;
- previous value;
- new value;
- reason;
- reviewer;
- version bump;
- whether audio re-recording is required;
- whether prior family-test evidence becomes harder to compare.

---

# 59. Changes Requiring Audio Re-Recording

Examples:

- Tamil lexical change;
- pronunciation correction;
- speaker replacement;
- noise correction affecting spoken clip;
- regional variant change.

A metadata-only edit should not necessarily require a new recording.

---

# 60. Prototype Content Freeze

Before the seven-day family test begins:

- the eight concept IDs should be frozen;
- canonical English/Tamil spoken forms should be frozen;
- primary visuals should be frozen;
- canonical audio should be frozen;
- prompt wording should be frozen where possible.

Only bug-level fixes should occur during the test.

This keeps observations comparable across days.

---

# 61. Prototype 0.1 Content Decisions

The current content baseline is:

| Concept | English | Tamil candidate | Status |
|---|---|---|---|
| apple | apple | ஆப்பிள் | review required |
| ball | ball | பந்து | review required |
| cup | cup | கப் / கோப்பை | decision required |
| hand | hand | கை | review required |
| nose | nose | மூக்கு | review required |
| cat | cat | பூனை | review required |
| car | car | கார் | review required |
| water | water | தண்ணீர் | preferred; review required |

No Tamil candidate becomes final solely because it appears in this table.

---

# 62. Content Invariants

### CT-INV-001
Every concept has a language-independent ID.

### CT-INV-002
English is not the source identity for Tamil content.

### CT-INV-003
Every active concept-language pair has canonical audio.

### CT-INV-004
Canonical and family audio are distinct.

### CT-INV-005
Family audio does not overwrite canonical content.

### CT-INV-006
Child learning does not require written target words.

### CT-INV-007
Primary visuals contain one dominant referent.

### CT-INV-008
Target word audio contains no score/judgment language.

### CT-INV-009
Tamil forms require human review.

### CT-INV-010
No unlicensed media enters the family-test build.

### CT-INV-011
One active canonical spoken form is used per concept-language pair in Prototype 0.1.

### CT-INV-012
Content changes are versioned rather than silently replaced.

---

# 63. Relationship to `database.md`

The database must be able to represent:

- concept;
- language;
- concept-language pair;
- canonical spoken form;
- asset references;
- content version;
- optional family audio references.

The database should not make filenames themselves the permanent semantic identity.

---

# 64. Relationship to `audio.md`

`audio.md` will define:

- recording format;
- encoding;
- sample rate;
- playback;
- loudness handling;
- asset loading;
- family recording limits;
- interruption behavior.

This document defines the linguistic/content role of those assets.

---

# 65. Relationship to `ui-ux.md`

`ui-ux.md` will define:

- visual presentation size;
- crop behavior;
- portrait/landscape layout;
- child tap area;
- animation;
- parent content-review screens if needed.

---

# 66. Relationship to `family-test-protocol.md`

Family testing should explicitly capture content-specific problems such as:

- child misidentifies image;
- child appears confused by Tamil lexical choice;
- audio sounds unnatural;
- concept feels too easy/boring;
- concept does not transfer to real life;
- family uses a different word naturally.

These observations may justify changing content before scaling beyond eight concepts.

---

# 67. Decision Summary

Prototype 0.1 content consists of:

- 8 stable, language-independent concepts;
- 16 canonical concept-language pairs;
- one clear primary visual per concept;
- one reviewed canonical English audio clip per concept;
- one reviewed canonical Tamil audio clip per concept;
- optional local family recordings;
- a minimal reusable prompt set;
- optional movement prompts;
- at least one natural real-world context per concept;
- versioned, reviewed, rights-cleared content.

The content system should be small enough that every item can be manually reviewed before the first family test.
