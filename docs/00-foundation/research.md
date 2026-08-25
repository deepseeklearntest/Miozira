# Miozira — Research Foundation

**Document:** `research.md`  
**Version:** 0.2  
**Status:** Research baseline for Prototype 0.1  
**Product:** Miozira  
**Research date:** 2026-08-25  
**Prototype:** Android-first, 8 concepts × 2 languages (English + Tamil)  
**Target child:** approximately 4–5 years old

---

## 1. Purpose

This document records the research foundation behind Miozira Prototype 0.1.

It exists to prevent three common mistakes:

1. treating product intuition as established evidence;
2. turning one research result into a universal rule; and
3. allowing later technical specifications to make assumptions that contradict what is already known.

This is not a legal opinion, medical advice, or a complete academic literature review.

It is a product-development research synthesis focused on questions that materially affect Miozira's first family-testable prototype.

---

## 2. Research Questions

The research was organized around the following questions.

### Learning

1. Is multilingual exposure itself harmful or confusing to preschool children?
2. How much do exposure quantity and input quality matter?
3. Should Miozira present the same concept in two languages?
4. Is repeated exposure enough, or should retrieval be spaced?
5. How important is human interaction outside the app?
6. What should Miozira consider meaningful progress?

### Child interaction

7. What touchscreen interactions are appropriate for a 4–5-year-old?
8. How should instructions and feedback be presented?
9. What kinds of stimulation should be avoided?
10. Can a child reasonably use the app independently?

### Speech

11. Can current automatic speech recognition reliably judge preschool pronunciation?
12. Is speech-attempt detection a safer first step?
13. Should child audio be recorded or uploaded?

### Technical architecture

14. What does current Android guidance recommend for adaptive tablet apps?
15. What architecture best supports offline-first behaviour?
16. Is Kotlin Multiplatform mature enough to preserve a future iPadOS path?
17. Is a relational local database justified for such a small prototype?
18. What Android platform requirements must a 2026 app plan for?

### Privacy and distribution

19. What does Google Play currently require for child-directed applications?
20. What is special about microphone access?
21. What third-party dependencies should be avoided?

### Market

22. Do similar products already exist?
23. What would make Miozira meaningfully different?

---

## 3. Research Method

Sources were prioritised in this order:

### Tier A — highest weight

- peer-reviewed review papers;
- systematic or multi-study research;
- official platform documentation;
- official policy documentation;
- professional medical/pediatric guidance.

### Tier B — strong supporting evidence

- individual peer-reviewed studies;
- major conference publications;
- published child-computer-interaction studies.

### Tier C — implementation and market evidence

- official product documentation;
- current competitor product pages;
- vendor device specifications.

Competitor marketing claims are **not** treated as scientific evidence.

Technical vendor documentation is treated as authoritative for platform behaviour, but not as neutral evidence when a vendor compares its technology against alternatives.

---

## 4. Executive Findings

The strongest conclusions for Miozira Prototype 0.1 are:

1. **Exposure to more than one language does not justify a “one language only” prototype.** Multilingual development is normal, but ability in each language is strongly shaped by the amount, context, and quality of exposure. [R1–R5]

2. **Eight concepts in two languages is a defensible prototype size, but not a meaningful claim of language acquisition.** It is enough to test interaction and memory behaviour, not fluency. [R1–R5]

3. **The app should build concept-to-sound associations rather than constantly translating one language into another.** Cross-language switching is not inherently harmful, but how languages are combined can affect learning. [R4]

4. **Spaced retrieval is better supported than massed drilling.** Preschool studies repeatedly show stronger retention when a word returns after intervening items rather than being immediately repeated over and over. [R7–R9]

5. **Human social interaction is not an optional “bonus.”** Socially contingent interaction and caregiver involvement support language learning; digital media should lead back to real people and real situations. [R6, R13]

6. **A 4–5-year-old can use simple touchscreen interactions, but the UI must be unusually forgiving.** Tap should be the main gesture; targets should be large; small edge controls, hierarchy, double-tap, dense backgrounds, and complicated gesture systems should be avoided. [R10–R12]

7. **Audio prompts are reasonable at this age, but should be short and visually supported.** Research shows children around age four can follow in-app audio/animated prompting far better than younger toddlers. [R10, R11]

8. **Current child ASR is not reliable enough to act as the authority on whether a preschool pronunciation is correct.** Performance varies heavily with age, noise, dataset, language, and model adaptation. [R15–R17]

9. **Speech-attempt detection is therefore a better Prototype 0.1 requirement than pronunciation grading.** The app only needs to know whether a speaking attempt probably happened.

10. **Offline-first is both technically straightforward and strategically valuable.** It reduces dependency, improves responsiveness, and dramatically simplifies the privacy posture of a child-directed app. [R18, R19, R24]

11. **Kotlin Multiplatform is now a credible architecture candidate rather than an experimental future option.** Google describes KMP as stable and production-ready for Android/iOS logic sharing; Compose Multiplatform is stable on Android and iOS. [R20, R21]

12. **The two candidate tablets are comfortably above what Miozira 0.1 should require.** Miozira should intentionally target a lower hardware baseline than either device. [R26, R27]

13. **Miozira is not entering an empty market.** Offline multilingual tap-and-hear apps, parent voice recording, calm design, and toddler vocabulary products already exist. Differentiation must therefore come from the full system: adaptive exposure + child-safe voice interaction + real-world transfer + family evidence, not from “multilingual flashcards” alone. [R28–R31]

---

# PART I — CHILD LANGUAGE LEARNING

## 5. Multilingual Exposure

### 5.1 What the evidence says

Young children are capable of learning multiple languages.

Research does not support a simple claim that exposure to two languages inherently “confuses” a normally developing child.

However, multilingual children divide their exposure across languages. As a result, measuring only one language can make development appear weaker than it is when total or conceptual vocabulary is ignored. [R1, R2]

A 2025 study of 4–5-year-old multilingual children found that single-language vocabulary may be lower than monolingual peers while conceptual vocabulary across languages can be comparable or stronger. It also found that language environment structure matters; multilingual experience cannot be reduced to a yes/no variable. [R2]

### 5.2 What this means for Miozira

Miozira does **not** need to protect the child from seeing the same concept in English and Tamil.

However:

- two-language exposure should be structured;
- one session should not become a constant “English word → Tamil word → English word → Tamil word” translation drill;
- each spoken word should remain tied strongly to the underlying concept;
- language switching should be predictable enough that the child can understand the interaction.

### 5.3 What the evidence does not justify

The evidence does not justify claims such as:

- “Miozira makes children bilingual”;
- “10 minutes per day is enough for fluency”;
- “four languages at once is optimal”;
- “balanced exposure is always superior”;
- “a child must use one language per person.”

Miozira should make no fluency promise during Prototype 0.1.

---

## 6. Quantity and Quality of Language Input

Research repeatedly finds that language outcomes are associated with how much children hear and how rich or proficient the input is. [R1, R3, R5]

The relationship is not simply:

> more minutes = guaranteed learning.

Relevant factors include:

- amount of exposure;
- speaker proficiency;
- vocabulary richness;
- interaction context;
- number and identity of communication partners;
- whether the child participates;
- whether language appears meaningfully in daily life.

### Miozira implication

The app should not attempt to replace normal language exposure.

Its role is better described as:

> **a structured ignition point for language that family interaction can reinforce.**

This strongly supports the “real-world suggestion” feature.

---

## 7. Native Audio, Family Audio, and Speaker Quality

Evidence on non-native input is nuanced.

Some studies find associations between higher-proficiency/native input and stronger language outcomes, while other work suggests that proficiency and richness matter more than the native/non-native label by itself. [R3]

### Prototype implication

For target-language word models:

- use carefully reviewed, high-quality pronunciation;
- do not automatically assume a parent's recording should replace the canonical model if the parent does not speak that language confidently.

For family or heritage languages:

- parent/family recordings can be valuable because Miozira is not only providing phonetic input; it is also supporting familiar family interaction.

### Practical rule for later specs

Miozira should distinguish:

1. **canonical language audio** — reviewed target pronunciation; and
2. **family voice audio** — optional familiar-person recording.

They may both exist for the same concept.

---

## 8. Cross-Language Presentation

Experimental research suggests that the way two languages are combined can influence learning and that abrupt switches may temporarily impose processing costs. It does **not** support a blanket requirement to isolate each language completely. [R4]

### Prototype implication

Normal interaction should usually be:

> picture → one target-language word

rather than:

> picture → English → Tamil → English → Tamil

Miozira may occasionally provide an intentional comparison experience, but this should be a specific mode or event rather than the default teaching loop.

### Design hypothesis to test

During the family test, observe whether the child:

- enjoys switching languages;
- becomes confused by language switching;
- develops a strong preference;
- expects one particular language after a particular visual or cue.

This remains an empirical Miozira question.

---

## 9. Retrieval and Repetition

Repeated exposure matters, but multiple preschool studies show a stronger pattern:

> **retrieval separated by intervening items tends to produce better long-term retention than immediate repeated retrieval.** [R7–R9]

The effective pattern often includes:

1. exposure;
2. an attempt to retrieve;
3. feedback/model;
4. other items;
5. return to the original item.

### Miozira implication

Avoid:

> apple → apple → apple → apple

Prefer:

> apple → ball → cup → apple

The exact schedule should remain simple in Prototype 0.1.

### Important caution

Research protocols are controlled learning experiments.

Miozira should not copy experimental trial counts mechanically.

The product must also respect:

- voluntary attention;
- fatigue;
- mood;
- interest;
- session abandonment.

---

## 10. Retrieval Is Not the Same as Testing

The retrieval literature often asks children to produce or identify a learned word.

For Miozira, retrieval should not become a high-pressure test.

A better product interpretation is:

- show a familiar concept;
- create a short opportunity for recall;
- accept an attempt or silence;
- replay the model;
- move on.

### Consequence

The system should never create a child-facing failure condition because retrieval did not occur.

---

# PART II — SOCIAL INTERACTION AND REAL-WORLD TRANSFER

## 11. Social Contingency

Controlled research has shown that young children learn language more successfully in socially contingent interactions than from equivalent non-contingent video exposure. [R6]

The exact age and paradigm differ from Miozira's target, but the principle is relevant:

> responsiveness and human interaction matter.

### Miozira implication

The tablet should not be treated as a substitute speaker.

The experience should be designed to generate later interactions with parents.

This supports:

- parent word suggestions;
- family voice;
- real-world-use observations;
- physical prompts;
- stopping the app rather than extending screen time.

---

## 12. Caregiver Co-Use and Media Quality

The American Academy of Pediatrics' 2026 technical report moves away from treating raw “screen time” as the only meaningful variable.

It highlights:

- content quality;
- caregiver behaviour;
- co-viewing;
- communication;
- whether media crowds out sleep, play, relationships, or physical activity. [R13]

It also notes that educational media can support learning in children older than two, but direct device-free interaction remains especially valuable. [R13]

### Miozira implication

The product should not optimise for maximum session duration.

The 5–10 minute target is a product design constraint, not a medical threshold.

Miozira should prefer:

> meaningful short interaction → child returns to play/family life

over:

> longer in-app engagement.

---

## 13. “Did the Word Escape the App?”

The research does not define this phrase; it is a Miozira product principle derived from the social and learning evidence.

A high-value signal is:

> language encountered in Miozira later appears in a different context.

Examples:

- child points to an apple after hearing the Tamil word;
- child responds when a parent uses the word;
- child says the word spontaneously;
- child uses a learned action phrase during play.

### Why this is stronger than “lesson completed”

It demonstrates some combination of:

- memory;
- concept mapping;
- contextual transfer;
- spontaneous retrieval;
- real social use.

Prototype 0.1 should therefore capture parent-reported real-world evidence.

---

# PART III — TOUCHSCREEN AND UX RESEARCH

## 14. Preschool Touch Behaviour

Research on young children's touchscreen performance consistently finds meaningful differences from adults. [R10–R12]

Performance improves rapidly from ages 3 to 6, but young children:

- miss smaller targets more often;
- benefit from forgiving hit areas;
- can struggle with complex gestures;
- are more affected by interface complexity;
- need clearer interaction cues.

### Prototype implication

Miozira's child experience should use:

- tap as the main gesture;
- very large interactive areas;
- generous spacing;
- forgiving touch bounds;
- few simultaneous targets.

---

## 15. Target Size

Child-touch research suggests that simply meeting ordinary adult minimum target sizes may not be enough.

Studies cited in the child-HCI literature show children miss relatively small targets substantially more often than adults, and researchers have used expanded hit areas well beyond the visible target. [R10, R12]

### Miozira design requirement

Later `ui-ux.md` should define:

- a large visible concept;
- an even larger invisible hit region where appropriate;
- no tiny child-facing controls;
- safe distance from device edges;
- adequate spacing between interactive elements.

Exact dp/mm values should be set in the UI specification after device testing.

---

## 16. Gestures

Evidence supports using simple gestures for preschool interfaces.

For Miozira:

### Suitable as core

- single tap.

### Potentially acceptable but not necessary

- basic horizontal swipe, if later testing shows a reason.

### Avoid as core interaction

- double-tap;
- pinch;
- rotate;
- multi-finger gestures;
- precision drag-and-drop;
- long-press.

A long-press may still be appropriate for an adult-only hidden parent gate, but it should not be part of the child's learning loop.

---

## 17. Instructions and Prompts

Research with 2–5-year-olds found that in-app audio prompts become substantially more usable around age four, while younger children often require an adult model. [R11]

The broader TIDRC research recommends:

- audio/visual rather than text prompts;
- visual support with audio;
- child-friendly language;
- immediate feedback;
- explicit scaffolding;
- avoiding hierarchy-heavy navigation. [R10]

### Miozira implication

A prompt like:

> “Your turn.”

with a simple microphone/visual cue is preferable to an explanatory sentence.

If the child does nothing, Miozira should model rather than lecture.

---

## 18. Visual Complexity

The evidence-based TIDRC framework recommends reducing visually complex backgrounds and making foreground learning elements clear and salient. [R10]

### Miozira implication

The child screen should normally contain:

- one primary concept;
- minimal background detail;
- no decorative clutter;
- no competing call-to-action;
- no banner;
- no carousel of unrelated content;
- no visible parent navigation.

---

## 19. Sound Design

The TIDRC review recommends purposeful sound and specifically advises against background music for children aged five and under in learning/video contexts because it can compete with content. [R10]

### Miozira implication

Prototype 0.1 should use:

- spoken language;
- intentional cue sounds;
- brief interaction feedback.

It should avoid:

- continuous background music;
- reward jingles;
- constant ambient effects;
- overlapping audio.

Every sound should have a reason.

---

# PART IV — CHILD SPEECH TECHNOLOGY

## 20. Why Child ASR Is Hard

Automatic speech recognition systems perform worse on children's speech than on adult speech for several reasons:

- higher and changing pitch;
- shorter vocal tract;
- developmental pronunciation;
- inconsistent articulation;
- disfluencies;
- shorter utterances;
- spontaneous speech;
- background noise;
- age variability;
- smaller child-specific training corpora.

Recent research continues to describe child ASR as an unresolved domain rather than a solved commodity problem. [R15–R17]

---

## 21. Current Model Performance Is Context-Dependent

The key lesson from current ASR research is **not** one particular word-error-rate number.

The important observation is how dramatically performance changes between datasets.

For example, a 2026 Dutch study evaluating nine ASR systems reported excellent performance for a fine-tuned model on one relatively controlled child dataset and extremely poor performance on a noisier child dataset. [R16]

That means an ASR system can look “solved” in one benchmark and fail badly in a real family environment.

### Miozira implication

Do not make child-facing correctness depend on ASR output.

---

## 22. Pronunciation Scoring Is a Different Problem From Speech Recognition

Recognizing the intended standard word is not the same as determining how the child pronounced it.

General-purpose ASR often normalizes imperfect speech into the expected word.

Clinical pronunciation research therefore uses specialized phonetic models and child-specific datasets. [R15 and related child-ASR literature]

### Miozira implication

A future pronunciation system would require:

- explicit pronunciation objectives;
- appropriate Tamil and English child-speech data;
- age-specific evaluation;
- clear tolerance policy;
- false-negative analysis;
- human validation.

It cannot responsibly be added as a checkbox around a generic transcription API.

---

## 23. Prototype 0.1 Speech Decision

The research strongly supports the existing scope choice:

> **Prototype 0.1 should detect participation, not pronunciation quality.**

Possible states:

- speech-like activity detected;
- no speech-like activity detected;
- microphone unavailable.

This is closer to voice activity detection / speech presence than full ASR.

### Child response

All valid states should preserve dignity and flow.

If speech is detected:

> acknowledge and continue.

If speech is not detected:

> gently replay/model or continue.

No “wrong.”

---

## 24. Child Audio Retention

There is no learning need in Prototype 0.1 that requires permanent storage of the child's raw speech.

### Research-informed default

- process microphone input transiently;
- discard it immediately after attempt detection;
- do not save raw child audio;
- do not upload raw child audio.

If future research requires speech recordings, that must be a new explicit privacy decision.

---

# PART V — PRIVACY, CHILD POLICY, AND ANDROID PERMISSIONS

## 25. Google Play Families Policy

Google Play considers microphone and camera sensor data among personal/sensitive information in child-directed applications. [R24]

Apps that include children in their target audience have additional restrictions on:

- identifiers;
- location;
- SDKs;
- ads;
- data disclosure;
- legal compliance.

### Miozira implication

The easiest compliant architecture is not merely to disclose large-scale child data collection.

It is to avoid collecting it.

---

## 26. Third-Party SDK Risk

Google explicitly states that developers remain responsible for data collected by embedded SDKs. [R24]

For Miozira 0.1 this supports a strict dependency rule:

Avoid SDKs whose purpose is:

- advertising;
- behavioural analytics;
- attribution;
- user profiling;
- remote session replay;
- fingerprinting;
- unnecessary crash telemetry;
- unnecessary cloud AI.

Every dependency should later appear in the data inventory.

---

## 27. Microphone Permission

Android treats microphone access as a runtime/dangerous permission and recommends requesting sensitive permissions in context, as late as practical. [R23]

### Miozira implication

The parent should understand why microphone access is requested before the child reaches the speaking interaction.

The app must still work when permission is denied.

The permission is therefore:

> **optional enhancement, not core access gate.**

---

## 28. Google Play Timing Relevant to This Project

As of the research date, 2026-08-25:

- a Google Play Families policy update is scheduled to become effective **2026-08-26**; the highlighted 2026 change principally concerns anonymous chat apps, but the existing child data and SDK rules remain important to Miozira. [R24]
- Google Play states that starting **2026-08-31**, new apps and app updates must target **Android 16 / API level 36**. [R25]

### Architecture implication

Miozira should be designed from the beginning against modern Android adaptive and permission behaviour rather than targeting an old API for convenience.

---

# PART VI — TECHNICAL ARCHITECTURE RESEARCH

## 29. Current Android Architecture Guidance

Current Android guidance recommends:

- clear UI and data layers;
- repositories as data boundaries;
- data-driven UI;
- unidirectional data flow;
- persistent models where appropriate;
- ViewModel/state-holder patterns;
- adaptive UI rather than assuming fixed screen orientation. [R18]

### Miozira implication

Even though Prototype 0.1 is small, it should not place session logic directly inside UI components.

At minimum, separate:

- child UI;
- parent UI;
- learning/session logic;
- local persistence;
- audio service;
- microphone service.

---

## 30. Offline-First Architecture

Android's offline-first guidance recommends the local data source as the canonical source of truth for offline-first applications. [R19]

Miozira is simpler than a typical “offline-first” cloud application because Prototype 0.1 has **no remote source at all**.

That means:

> local storage is not a cache; it is the actual product data source.

### Consequence

Prototype 0.1 does not need:

- synchronization queues;
- WorkManager sync jobs;
- conflict resolution;
- server identifiers;
- remote timestamps.

Those should not appear in the first database design.

---

## 31. Relational Database vs Simple Preferences

Miozira has only eight concepts, but the dynamic data is relational:

- concept;
- language;
- concept-language pairing;
- exposure;
- session;
- speaking attempt;
- parent observation;
- adaptive state.

This is a better fit for a small structured SQLite/Room schema than a growing collection of unrelated preference keys.

Simple settings can still use DataStore or an equivalent preferences mechanism.

---

## 32. Kotlin Multiplatform Status in 2026

Google currently describes Kotlin Multiplatform as:

> stable and production-ready for sharing business logic between Android and iOS. [R20]

Kotlin/JetBrains documentation also marks the core Android and iOS targets as stable, and Compose Multiplatform UI as stable for both. [R21]

### Why this matters for Miozira

Miozira is:

- Android-first;
- likely to have a meaningful shared learning engine;
- likely to have a shared data model;
- likely to have shared adaptive rules;
- likely to have shared content;
- expected to retain an iPadOS path.

That is exactly the type of application where selective sharing has value.

### Research conclusion

**Kotlin Multiplatform is a strong architecture candidate.**

This is **not yet the final technology-stack decision**.

That belongs in the architecture ADR.

---

## 33. Compose Multiplatform vs Native UI

The evidence does not force Miozira to share the UI.

KMP allows at least two sensible strategies:

### Option A

Share:

- domain logic;
- database;
- content model;
- adaptive engine.

Use:

- Jetpack Compose on Android;
- SwiftUI on iOS later.

### Option B

Share most of the above plus UI using Compose Multiplatform.

### Research interpretation

Because Miozira's child UI is highly custom and conceptually similar across platforms, shared UI is plausible.

However, microphone/audio integration and parent-area platform conventions may still need platform-specific code.

This decision should be made in `ADR-0001-technology-stack.md`, not in this research file.

---

## 34. Room and KMP

Android documentation supports Room in Kotlin Multiplatform and documents platform-specific database creation with shared entities/DAOs. [R22]

Room is therefore a credible way to preserve a common database model across Android and future iOS.

### Important caveat

Not every Android-specific Room feature is available in common KMP code.

Prototype 0.1 should use only simple, portable database features unless an Android-only need is demonstrated.

---

## 35. Adaptive Tablet Layouts

Modern Android guidance explicitly discourages assuming a permanently fixed portrait or landscape experience.

Large-screen applications should adapt to:

- orientation;
- window size;
- resizing;
- configuration changes. [R18]

### Miozira implication

Do not design:

> “portrait screen” and “landscape screen” as two unrelated applications.

Design:

> one responsive child interaction that rearranges safely as available dimensions change.

The active session state must survive orientation changes.

---

# PART VII — DEVICE RESEARCH

## 36. Candidate Device Capability

### REDMI Pad 2 Pro

Official Xiaomi specifications describe:

- 12.1-inch 2.5K display;
- Snapdragon 7s Gen 4;
- 120 Hz display;
- configurations including 6 GB / 8 GB RAM depending on market;
- large battery. [R26]

### OPPO Pad 5

Official OPPO specifications describe:

- 12.1-inch display;
- Dimensity 7300-Ultra on current listed variants;
- 8 GB RAM / 256 GB storage on referenced model;
- 120 Hz display. [R27]

### Conclusion

Neither tablet should define Miozira's minimum hardware requirement.

Prototype 0.1 tasks are modest:

- static imagery;
- short local audio playback;
- lightweight microphone processing;
- local database reads/writes;
- simple animation.

### Engineering principle

If Miozira needs flagship-class processing to show eight objects and detect a speech attempt, the architecture is wrong.

---

# PART VIII — COMPETITIVE LANDSCAPE

## 37. Market Reality

The concept of a simple multilingual app for young children is not novel by itself.

Current products already demonstrate combinations of:

- tap-to-hear vocabulary;
- no-reading interfaces;
- offline operation;
- multiple languages;
- parent voice recording;
- calm visual design;
- parent gates;
- custom family content. [R28–R31]

Examples include:

- NidoVoix;
- Dinolingo;
- EveryLingo;
- KLAP;
- LumiNest;
- other toddler vocabulary products.

---

## 38. What Competitors Validate

Competitor existence is useful evidence that families value:

- heritage-language support;
- spoken-first content;
- familiar vocabulary;
- offline access;
- parent customization;
- ad-free child experiences.

This validates the **problem space**.

It does not validate Miozira's learning model.

---

## 39. Where Miozira Must Differentiate

Miozira should not position itself merely as:

> “an app where toddlers tap pictures and hear multiple languages.”

That space already exists.

The more defensible Miozira concept is:

> **a low-stimulation, audio-first learning loop that remembers each child's encounters, spaces future exposure, treats speech gently, and deliberately connects tablet learning to family use outside the app.**

The differentiated system is the combination of:

1. adaptive local memory;
2. cross-language concept model;
3. no pronunciation judgment;
4. parent real-world observations;
5. real-world usage prompts;
6. family audio;
7. offline/private operation;
8. no engagement-maximizing mechanics.

Prototype 0.1 needs to validate this combination.

---

# PART IX — RESEARCH-DERIVED REQUIREMENTS

## 40. Requirements Safe to Carry Forward

The following requirements have sufficient evidence and product rationale to be treated as strong defaults.

### Child UX

- Use tap as the primary child gesture.
- Use very large forgiving hit targets.
- Show one dominant learning object at a time.
- Avoid text dependence.
- Avoid hierarchical child menus.
- Pair spoken prompts with simple visual cues.
- Provide immediate touch feedback.
- Avoid continuous background music.
- Avoid small edge controls.
- Avoid gamified pressure.

### Learning

- Organize learning around concepts, not translation strings.
- Space repetitions.
- Mix familiar and less-familiar items.
- Do not require a spoken response.
- Re-model after uncertain or absent responses.
- Track evidence, not a single ability score.
- Include family-mediated real-world reinforcement.

### Speech

- No pronunciation grading in 0.1.
- No cloud speech recognition.
- No permanent raw child audio by default.
- Session remains usable without microphone permission.

### Data/privacy

- Local database is the source of truth.
- No account in 0.1.
- No ads.
- No behavioural analytics SDK.
- No unnecessary device identifiers.
- No network dependency for the child experience.

### Architecture

- Android-first.
- Adaptive portrait/landscape layout.
- Preserve session state across configuration changes.
- Separate UI, learning logic, data, audio, and microphone responsibilities.
- Evaluate KMP seriously before committing the stack.
- Design internal contracts without inventing a backend API.

---

# PART X — THINGS RESEARCH DOES NOT YET ANSWER

## 41. Open Product Questions

These should be tested rather than argued about indefinitely.

### 41.1 Photo vs illustration

Research supports clear real-world mapping, but Miozira still needs to test whether this child responds better to:

- real photographs;
- realistic illustrations;
- simple stylized illustrations.

### 41.2 Language switching pattern

We still need to test:

- alternating languages by concept;
- blocks by language;
- child-selected language;
- session-selected language;
- occasional cross-language comparison.

### 41.3 Parent voice frequency

We do not yet know whether family voice should be:

- canonical audio;
- optional replay;
- used for prompts only;
- used for selected concepts.

### 41.4 Microphone invitation

We need to observe whether the child:

- enjoys the pause to speak;
- understands the cue;
- feels interrupted by it;
- ignores it;
- becomes self-conscious.

### 41.5 Session length

5–10 minutes is a product target.

The correct duration should be inferred from behaviour during the seven-day test.

### 41.6 Adaptive thresholds

Research supports spacing, but not Miozira-specific rules such as:

- exact number of exposures;
- number of intervening concepts;
- how long an item rests;
- exact criteria for “familiar.”

These belong to `adaptive-engine.md` and should remain deliberately simple.

---

# PART XI — RESEARCH RISKS

## 42. Avoid Overgeneralising Laboratory Studies

Many word-learning studies:

- use novel nonsense words;
- involve researchers sitting with children;
- control exposure precisely;
- test over short periods;
- use small samples.

Miozira operates in a family environment.

Therefore, laboratory findings should guide design but not be treated as guaranteed product outcomes.

---

## 43. Avoid Treating One Child as Universal Evidence

Prototype 0.1 is intentionally a family test.

It can answer:

> “Does this interaction seem promising enough to continue?”

It cannot establish:

> “This method works for all preschool children.”

Broader claims require broader testing.

---

## 44. Avoid Technology-Led Scope

The availability of:

- speech AI;
- generative AI;
- cloud analytics;
- sophisticated databases;
- cross-device sync

does not mean these features should be added.

The research indicates that Miozira's highest-risk unknown remains the child learning loop.

---

# PART XII — RECOMMENDATION FOR THE NEXT DOCUMENTS

## 45. Research-to-Spec Sequence

This research supports the following next sequence:

1. `requirements.md`
2. `non-functional-requirements.md`
3. `learning-model.md`
4. `session-design.md`
5. `adaptive-engine.md`
6. `ui-ux.md`
7. `architecture.md`
8. `ADR-0001-technology-stack.md`
9. `database.md`
10. `api.md`
11. `audio.md`
12. `microphone.md`
13. privacy/security specifications
14. testing and family-validation specifications

Architecture should therefore be based on a defined learning model, rather than forcing the learning model to fit an arbitrary technical stack.

---

# PART XIII — SOURCE REGISTER

## 46. Academic / Learning Sources

**R1 — Sebastian-Galles & Santolin, “Bilingual Acquisition: The Early Steps,” Annual Review of Developmental Psychology (2020).**  
https://www.annualreviews.org/content/journals/10.1146/annurev-devpsych-013119-023724  
Use: broad bilingual development review, exposure, vocabulary interpretation.

**R2 — “Diversity in monolingual and multilingual communicative environments and its relation to vocabulary in early childhood,” Bilingualism: Language and Cognition (2025).**  
https://www.cambridge.org/core/journals/bilingualism-language-and-cognition/article/diversity-in-monolingual-and-multilingual-communicative-environments-and-its-relation-to-vocabulary-in-early-childhood/2D83A1B8CD1778E2233E775299058F3F  
Use: 4–5-year-old multilingual vocabulary, conceptual vocabulary, language-environment complexity.

**R3 — Unsworth, “Predicting bilingual preschoolers’ patterns of language development: Degree of non-native input matters,” Applied Psycholinguistics (2019).**  
https://www.cambridge.org/core/journals/applied-psycholinguistics/article/predicting-bilingual-preschoolers-patterns-of-language-development-degree-of-nonnative-input-matters/499210BCDB47DA32E152AA4923998125  
Use: input quality/proficiency.

**R4 — Kaushanskaya, “Combining Languages in Bilingual Input: Using Experimental Evidence to Formulate Bilingual Exposure Strategies,” JSLHR (2023).**  
https://pubs.asha.org/doi/10.1044/2023_JSLHR-23-00181  
Use: distributed bilingual exposure and language-switching effects.

**R5 — Verhagen et al., “Relationships between bilingual exposure at ECEC and vocabulary growth in a linguistically diverse sample of preschoolers.”**  
https://pure.uva.nl/ws/files/217291658/1-s2.0-S0193397324000261-main.pdf  
Use: exposure and vocabulary growth in young children.

**R6 — Roseberry et al., “Skype Me! Socially Contingent Interactions Help Toddlers Learn Language,” Child Development (2014).**  
https://srcd.onlinelibrary.wiley.com/doi/10.1111/cdev.12166  
Use: social contingency in language learning.

**R7 — Haebig et al., “Retrieval-Based Word Learning in Young Typically Developing Children and Children With Developmental Language Disorder II,” JSLHR / PMC (2019).**  
https://pmc.ncbi.nlm.nih.gov/articles/PMC6802884/  
Use: spaced retrieval vs immediate retrieval.

**R8 — Leonard et al., “Retrieval Practice and Word Learning in Children With Specific Language Impairment and Their Typically Developing Peers,” JSLHR (2020).**  
https://pubs.asha.org/doi/full/10.1044/2020_JSLHR-20-00006  
Use: preschool retrieval practice and longer retention.

**R9 — “Retrieval Practice and Word Learning by Children With Developmental Language Disorder: Does Expanding Retrieval Provide Additional Benefit?” (2024).**  
https://pubmed.ncbi.nlm.nih.gov/38592972/  
Use: spacing schedules in 4–5-year-olds.

---

## 47. Child Interaction / Media Sources

**R10 — Soni et al., “A Framework of Touchscreen Interaction Design Recommendations for Children (TIDRC),” IDC (2019).**  
https://doi.org/10.1145/3311927.3323149  
Accessible copy:  
https://init.cise.ufl.edu/wp-content/uploads/sites/378/2019/04/TIDRC-Framework-soni-et-al-IDC19-final.pdf  
Use: evidence-based child touchscreen recommendations.

**R11 — Hiniker et al., “Touchscreen Prompts for Preschoolers” (2015).**  
https://faculty.washington.edu/alexisr/TouchscreenPrompts.pdf  
Use: prompt comprehension ages 2–5.

**R12 — Vatavu et al., “Touch interaction for children aged 3 to 6 years,” International Journal of Human-Computer Studies (2015).**  
https://dl.acm.org/doi/10.1016/j.ijhcs.2014.10.007  
Use: preschool touch accuracy and motor differences.

**R13 — American Academy of Pediatrics, “Digital Ecosystems, Children, and Adolescents: Technical Report” (2026).**  
https://publications.aap.org/pediatrics/article/157/2/e2025075321/206128/Digital-Ecosystems-Children-and-Adolescents  
Use: quality/context of media, caregiver co-use, engagement design.

**R14 — WHO, “Guidelines on physical activity, sedentary behaviour and sleep for children under 5 years of age.”**  
https://www.who.int/publications/i/item/9789241550536  
Use: broader under-5 sedentary/screen context.

---

## 48. Child Speech Technology Sources

**R15 — Attia et al., “Kid-Whisper: Towards Bridging the Performance Gap in Automatic Speech Recognition for Children VS. Adults,” AIES (2024).**  
https://ojs.aaai.org/index.php/AIES/article/view/31618  
Use: child-specific ASR adaptation and adult/child performance gap.

**R16 — “Transcribing Children’s Speech: ASR Performance and Obtaining Reliable Orthographic Transcriptions,” CLIN Journal (2026).**  
https://clinjournal.org/clinj/article/view/247  
Use: strong dataset-dependent variance in child ASR performance.

**R17 — Zhou et al., “ChildTalk: A Multi-Dialect Chinese Child Speech Corpus with Full-Length Child–Caregiver Conversations for Speech Recognition,” ACL Findings (2026).**  
https://aclanthology.org/2026.findings-acl.251/  
Use: current child-ASR challenges, developmental and dialect variability.

---

## 49. Android / Architecture Sources

**R18 — Android Developers, “Guide to app architecture.”**  
https://developer.android.com/topic/architecture  
Use: layered architecture, state, adaptive design.

**R19 — Android Developers, “Build an offline-first app.”**  
https://developer.android.com/topic/architecture/data-layer/offline-first  
Use: local source of truth and repository pattern.

**R20 — Android Developers, “Kotlin Multiplatform.”**  
https://developer.android.com/kotlin/multiplatform  
Use: official KMP Android/iOS support status.

**R21 — Kotlin Multiplatform Documentation, “Stability of supported platforms.”**  
https://kotlinlang.org/docs/multiplatform/supported-platforms.html  
Use: Android/iOS and Compose Multiplatform stability.

**R22 — Android Developers, “Set up Room Database for KMP.”**  
https://developer.android.com/kotlin/multiplatform/room  
Use: Room KMP support and SQLite drivers.

**R23 — Android Developers, “Permissions on Android.”**  
https://developer.android.com/guide/topics/permissions/overview  
Use: microphone permission, sensitive data, late/in-context permission requests.

---

## 50. Google Play Policy Sources

**R24 — Google Play, “Families Policies.”**  
https://support.google.com/googleplay/android-developer/answer/9893335  
Use: child data, microphone sensitivity, identifiers, SDK obligations, target audience.

**R25 — Google Play, “Target API level requirements for Google Play apps.”**  
https://support.google.com/googleplay/android-developer/answer/11926878  
Use: Android 16 / API 36 requirement starting 2026-08-31 for new apps and updates.

---

## 51. Device Sources

**R26 — Xiaomi, REDMI Pad 2 Pro official product specifications.**  
https://www.mi.com/uk/product/redmi-pad-2-pro/  
Use: candidate device capability.

**R27 — OPPO, OPPO Pad 5 official specifications.**  
https://www.oppo.com/in/accessories/oppo-pad-5/specs-5g/  
Use: candidate device capability.

---

## 52. Competitive Product Sources

These sources describe product claims and are included for market analysis, not scientific evidence.

**R28 — NidoVoix.**  
https://nidovoix.com/  
Use: offline multilingual toddler vocabulary, parent voice, privacy positioning.

**R29 — Dinolingo.**  
https://dinolingo.com/  
Use: pre-reader language product positioning and broad curriculum.

**R30 — EveryLingo.**  
https://everylingo.app/  
Use: multilingual vocabulary, family recordings, personalized content.

**R31 — KLAP.**  
https://klaptheapp.com/  
Use: offline, family voice, simple child interaction.

**R32 — LumiNest.**  
https://luminest.iterica.app/  
Use: calm design, real-world media, two-language presentation.

---

# PART XIV — RESEARCH BASELINE DECISION

## 53. Baseline

Based on the evidence reviewed, Miozira Prototype 0.1 should continue with the currently agreed direction:

> **Android-first, 8 familiar concepts, English + Tamil, audio-first tap interaction, simple spaced/adaptive repetition, optional speech-attempt detection without pronunciation grading, fully local data/audio, parent real-world observations, one real-world transfer suggestion, portrait + landscape, and no scores/streaks/coins/reading dependency.**

Nothing found in the research requires widening Prototype 0.1.

Several findings argue for keeping it intentionally small.

The next specifications should focus on making this loop precise, testable, private, and technically clean rather than adding features.

---

## 54. Research Maintenance Rule

This document should be updated when:

- a major product assumption changes;
- new family-test evidence contradicts an assumption;
- speech recognition becomes a planned feature;
- the product expands beyond Prototype 0.1;
- Google Play child policy changes materially;
- the Android/iOS technology stack is revisited;
- a meaningful new academic finding changes a design rule.

Each update should distinguish:

- new evidence;
- changed interpretation;
- changed product requirement.


---

# PART VIII — CHILD-CENTERED VISUAL DESIGN & WELLBEING UPDATE

## 32. Why this update exists

Prototype 0.1 originally kept the final palette open and leaned toward a dark-mode-first visual direction.

A focused follow-up research pass examined:

- developmental appropriateness of touchscreen interfaces;
- cognitive load and visual complexity;
- preschool touch interaction;
- symbolic UI comprehension;
- learning-science principles for educational apps;
- adult-child co-use;
- child digital wellbeing;
- persuasive/manipulative design;
- color preference versus visual comfort.

The research does **not** support treating “bright colors are good for children” or “dark mode is healthier” as reliable design rules.

The stronger conclusion is:

> **control salience, contrast, complexity, and engagement mechanics; then validate the visual system with actual children and real hardware.**

---

## 33. Evidence: Developmentally Appropriate Interface Simplicity

A synthesis of touchscreen HCI research identified dozens of recommendations addressing children's cognitive, physical, and socio-emotional needs. Young children's interfaces benefit from high visual clarity, reduced text dependence, simple interaction, and developmentally appropriate touch behavior.

Relevant source:

- Soni, Aloba, Morga, Wisniewski & Anthony, **A Framework of Touchscreen Interaction Design Recommendations for Children (TIDRC)**.
  https://stirlab.org/wp-content/uploads/A-Framework-of-Touchscreen-Interaction-Design-Recommendations-for-Children-TIDRC-Characterizing-the-Gap-between-Research-Evidence-and-Design-Practice.pdf

Miozira implication:

- one concept;
- one primary action;
- large forgiving target;
- minimal UI chrome;
- no unnecessary symbolic progress system.

---

## 34. Evidence: Visual Complexity and Symbolic Burden

Research with preschoolers shows that adults can underestimate how difficult abstract symbols and visually embellished interface elements are for young children.

Relevant source:

- Hiniker et al., **Hidden symbols: How informal symbolism in digital interfaces disrupts usability for preschoolers**.
  https://faculty.washington.edu/alexisr/HiddenSymbols.pdf

Miozira implication:

- do not decorate symbolic indicators heavily;
- avoid progress bars, stars, collections, or other abstract progress metaphors;
- prefer direct cause-and-effect.

---

## 35. Evidence: Touch Interaction

Children aged approximately 3–6 interact with touchscreens differently from adults, with touch performance changing significantly across these ages.

Relevant source:

- Vatavu, Cramariuc & Schipor, **Touch interaction for children aged 3 to 6 years**.
  https://dl.acm.org/doi/10.1016/j.ijhcs.2014.10.007

Miozira implication:

- large targets;
- forgiving hit areas;
- simple taps;
- no precision gestures;
- no double-tap requirement.

---

## 36. Evidence: Learning Requires Engagement, Not Distraction

The Four Pillars learning framework describes strong educational experiences as:

```text
active
engaged
meaningful
socially interactive
```

Importantly, “engaged” means engaged with the learning goal—not merely stimulated by screen effects. Extraneous animations, sound effects, and unrelated mini-games can distract from learning.

Relevant source:

- Hirsh-Pasek et al., **Putting Education in “Educational” Apps: Lessons From the Science of Learning**.
  https://www.psychologicalscience.org/journals/pspi/1529100615569721/

Miozira implication:

> learning-object interest > interface excitement.

---

## 37. Evidence: Wellbeing and Child-Centered Digital Design

The American Academy of Pediatrics' 2026 policy statement distinguishes child-centered design from engagement-based design. It highlights privacy, safety, meaningful experiences, and wellbeing, while warning that digital environments optimized around engagement/commercial incentives can promote prolonged use and displace healthy activities.

Relevant source:

- American Academy of Pediatrics, **Digital Ecosystems, Children, and Adolescents: Policy Statement** (2026).
  https://doi.org/10.1542/peds.2025-075320

Miozira implication:

- do not optimize for time-on-app;
- no autoplay marathon;
- no retention pressure;
- easy stopping;
- prioritize real-world family interaction.

---

## 38. Evidence: Manipulative Design Is Common in Children's Apps

A study of apps used by children aged 3–5 identified manipulative design patterns including gameplay pressure, time pressure, navigation constraints, attractive lures, and purchase pressure.

Relevant source:

- Radesky et al., **Prevalence and Characteristics of Manipulative Design in Mobile Applications Used by Children**, JAMA Network Open.
  https://jamanetwork.com/journals/jamanetworkopen/fullarticle/2793493

A 2025 experimental study also found that persuasive-design intensity can make digital disengagement harder for some 3–5-year-old children, particularly children with lower self-regulation.

Relevant source:

- Mallawaarachchi et al., **Effects of Persuasive App Design and Self-Regulation on Young Children's Digital Disengagement**.
  https://eprints.qut.edu.au/258323/

Miozira implication:

```text
no reward loops
no urgency
no parasocial pressure
no engagement lures
no completion pressure
no emotional exit friction
```

---

## 39. Evidence: Color Preference Is Not the Same as Wellbeing or Learning

Some studies report that young children are attracted to brighter or more saturated colors.

That does **not** establish that a highly saturated interface improves learning or wellbeing.

A 2023 eye-tracking study of children aged 4–7 found a more nuanced relationship: attraction and visual comfort depended on brightness, saturation, and contrast; moderate brightness/contrast supported comfort, while excessively low contrast impaired visual discrimination. The authors also caution that hue preferences are not conclusively established.

Relevant source:

- Cai et al., **Using head-mounted eye trackers to explore children's color preferences and perceptions of toys with different color gradients**, Frontiers in Psychology (2023).
  https://www.frontiersin.org/journals/psychology/articles/10.3389/fpsyg.2023.1205213/full

Miozira implication:

> do not design for maximum color preference.

Design instead for:

- concept clarity;
- comfortable contrast;
- deliberate salience;
- restrained UI saturation;
- natural object colors.

---

## 40. Evidence: Co-Use and Real-World Transfer

A 2024 meta-analysis of 17 studies involving 1,288 children aged 0–6 found a small positive association between adult-child co-use and children's learning from digital media, while acknowledging limitations in the evidence base.

Relevant source:

- Taylor et al., **Does adult-child co-use during digital media use improve children's learning aged 0–6 years?**
  https://discovery.ucl.ac.uk/id/eprint/10199543/1/Taylor%20et%20al%202024.pdf

Miozira implication:

> the app should create opportunities to leave the app.

This supports:

- one small parent real-world suggestion;
- family voice;
- “recognized outside Miozira”;
- “used outside Miozira.”

---

## 41. Evidence Strength Classification

### Strong / convergent enough to use as product constraints

- avoid manipulative engagement mechanics;
- reduce unnecessary visual complexity;
- avoid unrelated animations/sounds;
- use large forgiving touch targets;
- support child/adult co-use;
- prioritize meaningful real-world transfer;
- no pressure to remain in the app.

### Reasonable evidence, validate in prototype

- salient but restrained state cues improve usability;
- simple tap + immediate response is a good preschool interaction primitive;
- moderate visual contrast is preferable to weak figure-ground separation.

### Design hypothesis / aesthetic decision

The exact Miozira HEX palette.

Research can constrain:

- saturation;
- contrast;
- salience;
- complexity.

It cannot scientifically determine that `#F2EEE7` is uniquely optimal.

Therefore the exact palette below is an **evidence-informed design decision**, not a medical or psychological claim.

---

## 42. Miozira Prototype 0.1 Palette Decision

Locked palette:

```text
Child background          #F2EEE7  warm stone
Parent background         #F6F3EE  soft linen
Primary surface           #E8E2D8
Secondary surface         #DDD6CA

Primary content           #26312E  deep green-charcoal
Secondary content         #53605B
Muted content             #6F7772

Primary action            #4F746B  muted eucalyptus
On primary                #FFFFFF

Secondary action surface  #D6E2DE
On secondary              #2E4943

Border                    #B8B2A8
Focus                     #476D68
Listening                 #607C8A  muted slate-blue
Neutral response          #6B756F

Parent warning            #94604E  muted clay
Parent error              #875148
```

Design rule:

> **The concept should normally be more colorful than the interface.**

---

## 43. Dark-Mode Decision

Prototype 0.1 no longer uses “dark-mode-first” as a design requirement.

The child screen uses a controlled warm-light environment.

This decision should be revisited only if:

- real-device glare/comfort testing;
- accessibility testing;
- actual child observation

shows a meaningful problem.

---

## 44. Motion & Haptic Decision

Permitted:

- short press response;
- one short meaningful concept animation;
- restrained listening state;
- single subtle haptic acknowledgement.

Prohibited:

- looping decorative animation;
- confetti;
- reward animation;
- surprise effects;
- repeated reward haptics;
- sensory escalation.

Decision test:

> **Would this interaction still be worth having if it did not increase time-on-app?**

If no, it should probably not be in Miozira.

---

## 45. Updated Design North Star

Miozira's visual/interaction direction is now:

> **quiet interface, vivid reality, meaningful motion, calm curiosity.**

The app should not compete with the learning object for the child's attention.
