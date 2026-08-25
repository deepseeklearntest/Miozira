# Miozira — Product Vision

**Document:** `product-vision.md`  
**Version:** 0.1  
**Status:** Draft for Prototype 0.1  
**Product:** Miozira  
**Primary platform:** Android tablet  
**Future platform direction:** iPadOS  
**Prototype target age:** approximately 4–5 years  
**Last updated:** 2026-08-25

---

## 1. Purpose of This Document

This document defines what Miozira is, why it exists, who it is for, and the principles that must guide product and engineering decisions.

It is intentionally not a technical implementation document.

Later documents such as `architecture.md`, `database.md`, `api.md`, `ui-ux.md`, `learning-model.md`, and `privacy.md` must remain consistent with this vision.

If a future feature conflicts with this document, the feature should be reconsidered or this document should be explicitly revised first.

---

## 2. Product Vision

**Miozira is an audio-first multilingual learning environment for young pre-readers that helps language move from the tablet into real family life.**

Miozira should help a child:

1. see a familiar concept;
2. hear how people say it in another language;
3. voluntarily imitate the sound;
4. encounter it again later;
5. recognize or recall it over time; and
6. eventually use or understand it away from the screen.

The goal is not to make a preschool child “complete lessons.”

The goal is to make another language gradually feel familiar, meaningful, and usable.

---

## 3. The Problem Miozira Is Solving

Many language-learning products are designed around assumptions that work better for older children or adults:

- reading;
- menus;
- written instructions;
- explicit quizzes;
- scores;
- levels;
- streaks;
- rewards;
- long attention spans;
- deliberate study;
- pronunciation grading; and
- progress measured by completed lessons.

A young pre-reader interacts differently.

At approximately 4–5 years old, a child can learn through:

- hearing;
- imitation;
- repetition;
- pictures and familiar objects;
- physical movement;
- routines;
- social interaction;
- playful experimentation; and
- repeated exposure over time.

Miozira therefore starts from the child's developmental needs rather than shrinking an adult language-learning app onto a tablet.

---

## 4. Core Product Hypothesis

Miozira is based on the following hypothesis:

> A young pre-reader can begin building useful associations across languages when familiar concepts are presented through simple visual and spoken interactions, revisited adaptively over time, and reinforced naturally by family members away from the device.

Prototype 0.1 does **not** attempt to prove that Miozira can create fluency.

It attempts to prove something earlier and more fundamental:

> Can a child independently understand and enjoy Miozira's learning loop enough to voluntarily interact, imitate some words, remember some of them, and recognize or use at least one away from the tablet?

If this hypothesis fails, scaling the content library or building sophisticated speech recognition would not solve the underlying product problem.

---

## 5. Primary User

### 5.1 Child user

Prototype 0.1 is designed primarily for a child approximately **4–5 years old** who:

- may not yet read reliably;
- can use basic touchscreen interactions;
- has a short and variable attention span;
- learns strongly through spoken language and imitation;
- may pronounce words inconsistently;
- may choose not to speak when prompted;
- may stop using the app without warning; and
- should be able to use the core child experience with little or no adult instruction.

The child is not expected to understand settings, progress systems, language configuration, or technical concepts.

### 5.2 Parent or caregiver user

A parent or caregiver has a secondary role.

The adult should be able to:

- configure the experience;
- access a small protected parent area;
- review simple learning evidence;
- record family voice audio where appropriate;
- correct or supplement observations;
- mark that a child used or recognized a word outside Miozira; and
- receive a small suggestion for using language naturally during family life.

The parent interface must never turn the experience into pressure, testing, or performance management.

---

## 6. Product Principles

### 6.1 Audio first

Spoken language is the primary learning medium.

Text must never be required for the child to understand the learning interaction.

### 6.2 Meaning before translation

The central association should be:

**concept → spoken word**

rather than:

**English word → translated word**

The same concept may exist in multiple languages, but Miozira should avoid making constant translation chains the primary teaching method.

### 6.3 Interaction before instruction

The child should learn what to do through obvious interaction, visual cues, demonstration, and short spoken prompts.

Long explanations are a design failure.

### 6.4 Gentle responses, never judgment

Prototype 0.1 must not tell a child that their pronunciation is “wrong.”

A speaking attempt is treated as participation, not an exam answer.

Miozira may model the target pronunciation again without correcting or criticizing the child.

### 6.5 Repetition without drilling

Important concepts should return over time.

Repetition should be spaced and adaptive rather than forcing a child to repeat the same word continuously.

### 6.6 The child may stop at any time

There is no required session completion.

A 3-minute voluntary session may be more valuable than a forced 10-minute session.

Miozira should adapt to the child's attention rather than demanding that the child adapt to the app.

### 6.7 Real-world transfer matters more than screen completion

The strongest evidence of learning is not finishing a session.

It is language escaping the app.

Examples include:

- recognizing a Miozira word when a parent says it;
- identifying the corresponding real object;
- spontaneously saying the word later;
- using the word during play or routine;
- reacting correctly to a familiar phrase outside the app.

### 6.8 Family participation should feel natural

Miozira should help parents weave small amounts of language into:

- meals;
- play;
- dressing;
- bath time;
- travel;
- shopping;
- bedtime; and
- other ordinary routines.

Parents should not be told to quiz the child.

### 6.9 Privacy by architecture

Prototype 0.1 should function fully without an internet connection.

Child learning data, parent observations, settings, and audio should remain on the device.

Miozira should not require:

- an account;
- cloud storage;
- advertising identifiers;
- behavioural tracking;
- third-party analytics; or
- uploaded child speech.

### 6.10 Calm, low-stimulation design

Miozira should not compete for attention through artificial reward mechanics.

The experience should use deliberate visual and audio feedback without becoming noisy, frantic, or addictive.

### 6.11 The product serves learning, not engagement metrics

Miozira should never optimise for:

- daily active minutes;
- streak preservation;
- endless sessions;
- ad impressions;
- reward loops; or
- maximising screen time.

A successful session may end because the child leaves the tablet and uses the language with another person.

---

## 7. Prototype 0.1 Definition

Prototype 0.1 is a **family-testable vertical slice**.

### 7.1 Platform

- Android-first tablet application.
- Touch-first.
- Must support portrait and landscape.
- Must not depend on flagship-level hardware.
- Architecture should avoid unnecessary barriers to a future iPadOS version.

### 7.2 Content

Eight familiar concepts:

1. apple
2. ball
3. cup
4. hand
5. nose
6. cat
7. car
8. water

Two languages:

1. English
2. Tamil

The exact audio recordings, accent/dialect choices, and content metadata will be defined in the content and audio specifications.

### 7.3 Core child loop

The intended loop is:

**See → Tap → Hear → Imitate → Gentle response → Encounter again later**

A typical interaction:

1. Miozira displays one clear concept.
2. The child taps it.
3. Miozira plays the target word.
4. Miozira gently invites imitation.
5. The microphone may listen for evidence that speech occurred.
6. Miozira gives a warm acknowledgement or models the word again.
7. The session continues.
8. The concept may return later according to simple adaptive rules.

### 7.4 Microphone behaviour

Prototype 0.1 does **not** require child speech recognition.

The microphone may be used to detect that a speaking attempt occurred.

It must not claim to know whether the child's pronunciation was correct.

No numerical pronunciation score is allowed.

Detailed microphone behaviour will be defined in `microphone.md`.

### 7.5 Adaptation

Prototype 0.1 should perform simple local adaptation.

The system may consider signals such as:

- whether the concept was shown;
- whether audio was replayed;
- whether a speaking attempt occurred;
- recency of previous exposure;
- repeated lack of interaction;
- parent-marked real-world recognition/use; and
- whether the item is new, familiar, due, or temporarily resting.

The learning model must remain understandable and testable.

It should not use an opaque machine-learning recommendation system for Prototype 0.1.

### 7.6 Parent area

A small parent-only area should allow the adult to perform necessary actions without exposing menus or settings to the child.

At minimum, Prototype 0.1 should support:

- viewing basic learning history;
- marking “used/recognized outside Miozira”;
- reviewing or changing language settings where needed;
- managing parent/family recordings where supported; and
- viewing a short real-world suggestion.

The exact access mechanism and parent authentication/friction will be defined in `ui-ux.md` and `child-safety.md`.

### 7.7 Offline operation

Core functionality must remain available with:

- Wi-Fi disabled;
- mobile data unavailable; and
- no authenticated account.

This includes:

- concept images;
- word audio;
- session planning;
- learning history;
- parent feedback;
- parent recordings; and
- real-world suggestions.

---

## 8. Prototype 0.1 Success Criteria

The prototype exists to test behaviour, not content volume.

The primary question is:

> **Can a child use Miozira for roughly 5–10 minutes, understand the core interaction without substantial instruction, willingly imitate at least some words, and later recognize or use something learned away from the tablet?**

### 8.1 Positive behavioural signals

Miozira 0.1 is promising if repeated family testing shows several of the following:

- the child understands that tapping the concept produces spoken language;
- the child uses the experience with little adult instruction;
- the child voluntarily repeats at least some words;
- the child voluntarily replays audio;
- the child remains engaged for several minutes without being pressured;
- the child remembers a concept or word across sessions;
- the child recognizes a Miozira word in a real-world context;
- the child spontaneously uses a Miozira word away from the device;
- the child voluntarily returns to Miozira; or
- the child asks to use Miozira again.

### 8.2 Behaviours that are not failures

The following must **not** automatically be interpreted as product failure:

- stopping after only a few minutes;
- not speaking during a session;
- mispronouncing a word;
- preferring one language;
- preferring certain concepts;
- ignoring a prompt;
- replaying the same interesting sound;
- progress temporarily slowing;
- forgetting a previously encountered word.

These are observations that should inform future design.

---

## 9. Explicit Non-Goals for Prototype 0.1

Prototype 0.1 will not attempt to provide:

- fluency;
- full language curriculum;
- grammar instruction;
- reading instruction;
- writing instruction;
- 30 concepts;
- four languages;
- cloud accounts;
- cloud synchronization;
- social features;
- leaderboards;
- achievements;
- stars;
- coins;
- streaks;
- lives;
- levels;
- competitive scoring;
- mandatory daily targets;
- numerical child ability scores;
- pronunciation grading;
- open-ended speech transcription;
- AI conversation;
- generative AI;
- remote analytics;
- advertisements;
- in-app purchases;
- video lessons;
- complex games;
- complicated child navigation; or
- a full parent learning-management dashboard.

A feature being absent from Prototype 0.1 does not mean it is permanently rejected.

It means it is not necessary to answer the prototype's core hypothesis.

---

## 10. Product Boundaries

The following boundaries are considered strong defaults unless deliberately revised.

### Child-facing experience

**Allowed**

- large familiar pictures or illustrations;
- tapping;
- simple visual motion;
- short spoken prompts;
- word audio;
- optional replay;
- warm acknowledgement;
- physical-action prompts;
- calm transitions.

**Avoid**

- written instructions;
- dense screens;
- multiple competing buttons;
- small targets;
- complex gestures;
- abstract navigation icons without clear meaning;
- pop-up dialogs;
- timers;
- countdown pressure;
- reward explosions;
- background music during language-learning interactions;
- “correct” and “wrong” judgement.

### Parent-facing experience

The parent area may use ordinary adult UI patterns, including text, because it is not intended for independent child use.

However, the parent area should remain small and purposeful.

---

## 11. Learning Progress Philosophy

Miozira should represent progress as **evidence accumulated over time**, not as a single score.

A concept-language pair may gradually move through experiences such as:

**heard → familiar → imitates → recalls → recognizes in context → uses spontaneously → combines into phrases**

These are not mandatory linear levels.

A child may:

- skip stages;
- move backwards;
- stop using a word temporarily;
- understand without speaking;
- say a word without consistently recognizing it later.

The data model must preserve observations without pretending to measure a child's language ability with false precision.

---

## 12. Long-Term Direction

If Prototype 0.1 validates the learning loop, Miozira may later expand toward:

- more concepts;
- additional languages;
- verbs and actions;
- short phrases;
- family and heritage-language packs;
- native-speaker audio packs;
- richer real-world activities;
- improved adaptive scheduling;
- carefully evaluated child speech technology;
- multiple child profiles;
- iPadOS support; and
- broader parent customization.

Long-term expansion must preserve the product principles in this document.

Adding more capability must not turn Miozira into a conventional gamified language app.

---

## 13. Decision Filter

Every proposed feature should be tested against these questions:

1. Does this help the child connect spoken language with meaning?
2. Does it make the child experience easier to understand?
3. Does it improve useful repetition or recall?
4. Does it help language transfer into real life?
5. Does it respect the child's variable attention and autonomy?
6. Does it preserve privacy?
7. Can it work offline where required?
8. Does it avoid unnecessary stimulation or pressure?
9. Is it necessary for the current prototype?
10. Can we test whether it actually helps?

If the answer to most of these is no, the feature probably does not belong in Miozira.

---

## 14. Prototype 0.1 North-Star Statement

> **Miozira 0.1 succeeds when the tablet becomes a small doorway into spoken language—not the destination.**

The most meaningful outcome is not that a child spends more time inside Miozira.

It is that something first heard in Miozira later becomes meaningful between the child, family, and real world.

---

## 15. Document Relationships

This document governs the intent of the following future specifications:

- `prototype-0.1-scope.md`
- `research.md`
- `requirements.md`
- `non-functional-requirements.md`
- `learning-model.md`
- `adaptive-engine.md`
- `session-design.md`
- `content-spec.md`
- `ui-ux.md`
- `child-experience.md`
- `parent-experience.md`
- `architecture.md`
- `database.md`
- `api.md`
- `audio.md`
- `microphone.md`
- `privacy.md`
- `security.md`
- `testing.md`
- `family-test-protocol.md`
- architecture decision records under `decisions/`

When conflicts occur, the more specific document may define implementation detail, but it must not silently contradict the principles and scope established here.

---

## 16. Change Policy

Changes to the product vision should be intentional.

A revision should state:

- what changed;
- why it changed;
- what evidence caused the change; and
- which downstream specifications must be reviewed.

Prototype discoveries are expected to change Miozira.

Untracked scope drift is not.
