# Miozira — Prototype 0.1 Scope

**Document:** `prototype-0.1-scope.md`  
**Version:** 0.1  
**Status:** Draft for Prototype 0.1  
**Product:** Miozira  
**Primary platform:** Android tablet  
**Prototype target age:** approximately 4–5 years  
**Languages:** English + Tamil  
**Concept count:** 8  
**Last updated:** 2026-08-25

---

## 1. Purpose

This document defines the exact scope of Miozira Prototype 0.1.

Its purpose is to prevent scope drift.

Prototype 0.1 is not intended to be a polished commercial launch. It is a family-testable vertical slice designed to validate whether the core Miozira interaction and learning loop is understandable, enjoyable, and potentially useful for a young child.

Everything in this document should be considered either:

- **In scope**
- **Out of scope**
- **Deferred**
- **Required for validation**

No implementation work should add major features outside this boundary without an explicit scope revision.

---

## 2. Prototype Objective

The primary objective is to test this question:

> **Can a 4–5-year-old child use Miozira for roughly 5–10 minutes, understand what to do with little or no instruction, willingly interact with spoken words, and later recognize or use at least one of those words away from the tablet?**

Prototype 0.1 is successful if it gives us reliable evidence about this question.

It does not need to prove:

- fluency;
- long-term curriculum effectiveness;
- advanced personalization;
- speech-recognition accuracy;
- commercial scalability;
- store-readiness;
- multi-device synchronization; or
- four-language learning.

---

## 3. Core Prototype Definition

Prototype 0.1 is:

> **Android-first → 8 concepts → 2 languages → adaptive audio loop → offline → parent feedback → real-world transfer**

This is the canonical summary for this release.

---

## 4. Supported Platform

### 4.1 Required

Prototype 0.1 must:

- run on Android tablets;
- support touch interaction;
- support portrait orientation;
- support landscape orientation;
- preserve the active session during orientation changes where practical;
- remain responsive on midrange tablet hardware;
- work without internet connectivity after installation and setup.

### 4.2 Target device philosophy

The app should be designed against a practical lower common denominator rather than tuned only for flagship tablets.

The prototype should avoid requiring:

- high-end GPU performance;
- large amounts of RAM;
- neural processing hardware;
- constant background processing;
- cloud inference;
- high-resolution video rendering.

### 4.3 Future portability

Prototype 0.1 is Android-first.

However, architecture decisions should avoid unnecessary Android-only coupling where there is a reasonable alternative.

Future iPadOS support is a portability goal, not a Prototype 0.1 delivery requirement.

---

## 5. Target Users

### 5.1 Primary user

One child approximately 4–5 years old.

The child may:

- be pre-literate;
- have variable attention;
- pronounce words inconsistently;
- ignore prompts;
- stop sessions early;
- prefer one language;
- prefer certain concepts;
- interact unpredictably.

The child experience must accommodate this.

### 5.2 Secondary user

One parent or caregiver.

The adult is responsible for:

- initial setup;
- parent-area access;
- optional family voice recordings;
- marking real-world recognition/use;
- reviewing lightweight progress;
- using post-session real-world suggestions;
- helping interpret prototype outcomes.

Prototype 0.1 does not require multi-parent accounts or remote caregiver access.

---

## 6. Languages

Prototype 0.1 supports exactly two languages:

1. English
2. Tamil

The prototype should support language-aware content internally so a third or fourth language can be added later without redesigning the whole app.

### 6.1 Out of scope for 0.1

The following are not required:

- Spanish;
- Arabic;
- Japanese;
- language downloads;
- dynamic language packs;
- automatic language detection;
- dialect switching;
- transliteration;
- written vocabulary for the child.

---

## 7. Concepts

Prototype 0.1 includes exactly eight familiar concepts:

1. apple
2. ball
3. cup
4. hand
5. nose
6. cat
7. car
8. water

Each concept requires:

- one primary visual asset;
- English spoken-word audio;
- Tamil spoken-word audio;
- concept metadata;
- learning-history support;
- scheduling/adaptation support.

Optional additional visual variants may be used during internal testing, but the product requirement is one dependable primary visual per concept.

---

## 8. Child Experience Scope

### 8.1 Core interaction

The basic child loop is:

1. show one concept;
2. child taps the concept;
3. play the spoken word;
4. visually or audibly invite imitation;
5. briefly listen for speech activity if microphone permission is available;
6. give a warm response;
7. continue to the next interaction;
8. reintroduce concepts later according to simple adaptive rules.

### 8.2 Required child-facing capabilities

Prototype 0.1 must provide:

- a clear child-mode entry point;
- one primary concept at a time;
- large tap targets;
- immediate touch feedback;
- spoken word playback;
- replay ability;
- a gentle imitation invitation;
- microphone attempt detection where enabled;
- a non-punitive response;
- automatic progression;
- adaptive resurfacing of concepts;
- session stopping at any time;
- graceful restart after app closure.

### 8.3 Child-facing design restrictions

Prototype 0.1 must not require:

- reading;
- typing;
- menus;
- multi-step navigation;
- text instructions;
- small icons;
- complex gestures;
- drag-and-drop;
- pinch;
- double-tap;
- long-press as a core learning interaction;
- timer pressure;
- scores;
- streaks;
- stars;
- coins;
- levels;
- lives;
- badges;
- leaderboards;
- “correct” or “wrong” labels.

---

## 9. Session Scope

### 9.1 Intended duration

A normal session should naturally fit within approximately:

**5–10 minutes**

This is a design target, not a child-facing requirement.

The child may stop earlier.

There is no minimum session length.

### 9.2 Session composition

A session may include:

- familiar concepts;
- recently uncertain concepts;
- due concepts;
- a small number of new or less-familiar concept-language combinations;
- replay opportunities;
- one movement-oriented interaction where suitable;
- an easy or familiar ending.

### 9.3 Session completion

Prototype 0.1 must not:

- force completion;
- show a failure state for leaving early;
- punish inactivity;
- reset progress for skipped days;
- require daily usage.

---

## 10. Adaptive Learning Scope

Prototype 0.1 includes a simple local adaptive mechanism.

It should use understandable rules rather than machine learning.

### 10.1 Signals that may be used

The engine may consider:

- concept-language pair last shown;
- total exposures;
- audio replay count;
- speaking attempt detected;
- no speaking attempt;
- repeated low interaction;
- parent-marked real-world recognition;
- parent-marked spontaneous use;
- recency;
- whether the item is new;
- whether the item is due;
- whether the item is temporarily resting.

### 10.2 Allowed adaptation

The prototype may:

- show less-familiar items somewhat more often;
- space repeated exposures;
- reduce repeated exposure to well-familiar items;
- temporarily rest repeatedly difficult items;
- reintroduce rested items later;
- balance easy/familiar items with less-familiar ones.

### 10.3 Out of scope

Prototype 0.1 must not include:

- opaque AI ranking;
- cloud recommendation engines;
- reinforcement-learning systems;
- child skill scores;
- percentile comparisons;
- automatic pronunciation mastery scores;
- predictive language proficiency estimates.

---

## 11. Microphone Scope

### 11.1 Required behaviour

Microphone support is limited to detecting whether a speaking attempt probably occurred.

The app may classify interaction internally as something similar to:

- speech attempt detected;
- no speech attempt detected;
- microphone unavailable.

### 11.2 Prohibited behaviour

Prototype 0.1 must not:

- grade pronunciation;
- show pronunciation percentages;
- transcribe the child;
- claim a word was correct;
- claim a word was incorrect;
- send speech to a server;
- use cloud speech recognition;
- permanently store child speech by default.

### 11.3 Graceful fallback

If microphone permission is denied or unavailable:

- the learning session must still work;
- the child must not be blocked;
- the app should continue with audio modelling and interaction.

Microphone access is helpful, not mandatory.

---

## 12. Audio Scope

Prototype 0.1 requires fully local audio playback.

Required audio categories:

- English target-word audio;
- Tamil target-word audio;
- short child-directed interaction prompts;
- warm acknowledgement audio where used;
- optional parent-recorded audio.

Audio must work offline.

Streaming audio is out of scope.

---

## 13. Parent Area Scope

The parent area must be small and hidden from ordinary child interaction.

### 13.1 Required parent capabilities

At minimum, the parent should be able to:

- access the parent area intentionally;
- view the supported languages;
- view basic concept activity;
- view recent learning history;
- mark a concept as recognized outside Miozira;
- mark a concept as used outside Miozira;
- view one short real-world suggestion after a session;
- manage parent/family recordings if this feature is included in the build;
- access privacy-related information;
- reset prototype data if needed.

### 13.2 Parent area is not a dashboard product

Prototype 0.1 does not require:

- charts;
- achievement graphs;
- detailed analytics;
- comparisons between children;
- downloadable reports;
- cloud backup;
- remote access;
- teacher accounts;
- school administration tools.

---

## 14. Real-World Transfer Scope

Prototype 0.1 must include a small bridge between app use and family life.

After a session, the parent should receive one short suggestion.

Example structure:

> “At snack time, say the Tamil word for apple when handing over a piece.”

The suggestion should:

- reference a recently encountered concept;
- identify a natural moment;
- avoid telling the parent to quiz the child;
- be simple enough to use immediately.

Prototype 0.1 requires one suggestion per completed or meaningfully active session.

---

## 15. Parent-Reported Real-World Evidence

The parent must be able to record evidence such as:

- recognized outside app;
- used after prompting;
- used spontaneously.

Prototype 0.1 may simplify this to two actions:

1. **Recognized outside Miozira**
2. **Used outside Miozira**

The exact model will be defined in `learning-model.md` and `database.md`.

This is parent-reported evidence, not automatically inferred behaviour.

---

## 16. Data Scope

Prototype 0.1 should store only what is necessary for the experience and the family test.

Likely data categories include:

- concept definitions;
- language definitions;
- audio asset references;
- session records;
- concept exposures;
- speaking-attempt events;
- replay events;
- parent observations;
- adaptive scheduling state;
- parent settings;
- parent recordings;
- prototype configuration.

Exact schema belongs in `database.md`.

---

## 17. Offline Requirement

Prototype 0.1 must support its core experience with the device offline.

The following must remain functional offline:

- app launch;
- child session;
- image display;
- word audio;
- replay;
- speaking-attempt detection;
- adaptive scheduling;
- progress updates;
- parent area;
- parent observations;
- family voice playback;
- real-world suggestions.

A network connection must not be necessary for ordinary use.

---

## 18. Privacy Scope

Prototype 0.1 should be designed as if it may eventually be distributed as a child-directed app.

Therefore, the prototype should avoid collecting unnecessary sensitive data from the start.

### 18.1 Required defaults

- no account;
- no sign-in;
- no advertising;
- no analytics SDK;
- no behavioural tracking SDK;
- no cloud audio;
- no cloud child profile;
- no location;
- no contacts;
- no device advertising identifier;
- no unnecessary device identifiers.

### 18.2 Local data

Learning history and parent recordings remain local to the device.

More detailed requirements will be defined in `privacy.md`, `security.md`, and `data-inventory.md`.

---

## 19. Accessibility and Usability Scope

Prototype 0.1 should include basic accessibility considerations from the beginning.

Required:

- large touch targets;
- strong visual clarity;
- sufficient contrast;
- no reliance on text for child interaction;
- no reliance on colour alone;
- clear audio;
- responsive touch feedback;
- no essential control placed too close to screen edges;
- child experience usable in both orientations.

Full formal accessibility conformance certification is not required for Prototype 0.1.

---

## 20. Performance Scope

The prototype should feel immediate.

Target behaviours:

- tapping a concept should produce prompt feedback quickly;
- local audio should start without noticeable loading delays;
- orientation changes should not lose learning state;
- session transitions should remain smooth;
- app startup should not depend on network availability.

Formal benchmark thresholds will be defined later in `performance.md`.

---

## 21. Error Handling Scope

Prototype 0.1 must gracefully handle:

- microphone permission denied;
- microphone temporarily unavailable;
- missing parent recording;
- app killed mid-session;
- orientation change;
- corrupted or missing optional local asset where recoverable;
- no prior learning history;
- partially completed session.

The child should not see technical error messages.

Technical failure details, if needed, belong only in the parent/developer path.

---

## 22. Testing Scope

Prototype 0.1 requires:

- core unit tests for adaptive/session logic;
- local persistence tests;
- orientation/state-preservation tests;
- microphone fallback tests;
- audio playback tests;
- child-flow UI tests;
- parent-area access tests;
- offline-mode tests;
- manual family testing;
- testing on at least one real Android tablet.

The exact test plan will be defined in `testing.md`.

---

## 23. Family Validation Scope

The intended initial evaluation period is approximately seven days.

The family test should observe:

- independent use;
- willingness to tap;
- replay behaviour;
- willingness to imitate;
- session duration;
- language preference;
- concept preference;
- repeated voluntary return;
- recognition away from the app;
- spontaneous use away from the app;
- parent burden;
- confusion points;
- microphone tolerance.

The detailed protocol will be defined in `family-test-protocol.md`.

---

## 24. Explicitly Out of Scope for Prototype 0.1

The following must not be added without a scope revision:

### Product

- more than 8 canonical concepts;
- more than 2 languages;
- reading curriculum;
- grammar curriculum;
- story mode;
- video lessons;
- multiplayer;
- social sharing;
- chat;
- AI tutor;
- generative AI;
- child avatar;
- virtual currency;
- daily quests.

### Speech technology

- cloud ASR;
- open-ended transcription;
- pronunciation scoring;
- speech-to-text;
- automatic fluency grading;
- conversational voice AI.

### Infrastructure

- backend server;
- REST API;
- GraphQL API;
- cloud database;
- account service;
- login;
- subscription infrastructure;
- remote feature flags;
- push notifications;
- cloud analytics;
- cross-device sync.

### Commercial

- payments;
- in-app purchases;
- ads;
- subscription;
- app-store monetization;
- referral systems.

### Administration

- teacher portal;
- school portal;
- classroom management;
- multi-child family management;
- web dashboard.

---

## 25. Deferred, Not Rejected

The following may be evaluated after Prototype 0.1:

- Spanish;
- Arabic;
- Japanese;
- 30+ concepts;
- phrases;
- verbs;
- sentence patterns;
- multiple visual examples per concept;
- stronger adaptive scheduling;
- child speech recognition;
- multiple child profiles;
- iPadOS;
- downloadable language packs;
- parent-created concepts;
- richer parent audio tools;
- optional encrypted backup;
- broader accessibility features;
- carefully designed progress summaries.

They are intentionally deferred so Prototype 0.1 can answer its main question quickly and cleanly.

---

## 26. Prototype Completion Definition

Prototype 0.1 is considered implementation-complete when all of the following are true:

### Child loop

- [ ] all 8 concepts can be shown;
- [ ] both languages work;
- [ ] each target word has local audio;
- [ ] tap-to-hear works;
- [ ] replay works;
- [ ] imitation invitation works;
- [ ] speaking-attempt detection works or gracefully falls back;
- [ ] warm response works;
- [ ] sessions can progress without adult navigation.

### Adaptation

- [ ] exposures are stored;
- [ ] speaking attempts are stored;
- [ ] replay behaviour can be stored;
- [ ] simple due/familiar/rest logic works;
- [ ] future sessions reflect prior interaction.

### Parent

- [ ] parent area is not easily entered accidentally;
- [ ] parent can view basic activity;
- [ ] parent can mark real-world recognition/use;
- [ ] real-world suggestion is shown;
- [ ] local data can be reset.

### Offline

- [ ] child session works in airplane/offline conditions;
- [ ] audio works offline;
- [ ] progress persists offline;
- [ ] parent area works offline.

### Device behaviour

- [ ] portrait works;
- [ ] landscape works;
- [ ] orientation change does not break the active session;
- [ ] app remains usable after process restart.

### Validation

- [ ] family test build can be installed on a real Android tablet;
- [ ] seven-day test protocol can begin without missing critical functionality.

---

## 27. Prototype Exit Decision

After the family test, one of four decisions should be made:

### A. Continue

Core interaction works and there are meaningful positive learning/engagement signals.

Proceed to Prototype 0.2.

### B. Iterate the child loop

The idea appears promising but there are usability or interaction problems.

Revise the loop before expanding content.

### C. Revise the learning model

The child can use the app, but repetition or language switching does not appear useful.

Change the learning strategy before scaling.

### D. Stop or rethink

The child does not understand, enjoy, or benefit from the core experience despite reasonable iteration.

Do not solve this by adding more content or engineering complexity.

---

## 28. Scope Change Rule

Any proposed addition must answer:

1. Is this necessary to test the Prototype 0.1 hypothesis?
2. Does the seven-day family test become invalid without it?
3. Can we test the same thing with a simpler implementation?
4. Does this add infrastructure that will not produce useful evidence yet?

If the feature is not necessary, it should be deferred.

---

## 29. Canonical Prototype Statement

For all planning, implementation, and AI-assisted coding work, Prototype 0.1 should be described as:

> **Miozira Prototype 0.1 is an Android-first, fully offline, family-testable language-learning vertical slice for a 4–5-year-old child. It contains 8 familiar concepts in English and Tamil, uses a simple audio-first tap-and-repeat interaction, treats microphone input only as evidence of a speaking attempt, adapts concept repetition locally, gives parents lightweight real-world feedback tools, and contains no scores, streaks, reading requirements, cloud backend, or pronunciation grading.**

This statement should remain unchanged unless the scope itself is formally revised.
