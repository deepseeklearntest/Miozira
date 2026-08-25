# Miozira — Product Requirements

**Document:** `requirements.md`  
**Version:** 0.1  
**Status:** Draft for Prototype 0.1  
**Product:** Miozira  
**Primary platform:** Android tablet  
**Prototype target age:** approximately 4–5 years  
**Languages:** English + Tamil  
**Concepts:** 8  
**Last updated:** 2026-08-25

---

## 1. Purpose

This document defines the functional product requirements for Miozira Prototype 0.1.

Each requirement has a stable identifier so later specifications can reference it from `ui-ux.md`, `learning-model.md`, `adaptive-engine.md`, `architecture.md`, `database.md`, `api.md`, `audio.md`, `microphone.md`, `privacy.md`, `testing.md`, and `family-test-protocol.md`.

This document defines **what Miozira must do**. Technical implementation choices belong in later engineering documents.

---

## 2. Scope Baseline

Miozira Prototype 0.1 is:

> **Android-first → 8 familiar concepts → English + Tamil → adaptive audio loop → optional speech-attempt detection → fully offline → parent feedback → real-world transfer**

Prototype 0.1 does not include a backend, accounts, cloud sync, pronunciation grading, speech transcription, an AI tutor, ads, subscriptions, scores, streaks, coins, or reading-dependent child UI.

---

# 3. Child Experience

### FR-CHILD-001 — Direct child entry — P0
Miozira shall provide a child-facing entry point that reaches the learning experience without requiring ordinary adult-menu navigation, text comprehension, account creation, or login.

### FR-CHILD-002 — One dominant learning target — P0
During the main learning interaction, one concept shall be the visually dominant interactive object. Parent controls shall not be visible in the normal child flow.

### FR-CHILD-003 — Tap as primary gesture — P0
A single tap shall be the primary child gesture. Swipe, drag, pinch, rotate, double-tap, or long-press shall not be required to complete the core learning loop.

### FR-CHILD-004 — Large forgiving touch target — P0
The primary concept shall have a large touchable area suitable for preschool motor accuracy. The active hit region may safely exceed visible bounds.

### FR-CHILD-005 — Immediate touch feedback — P0
After an accepted tap, Miozira shall provide immediate visual and/or auditory feedback so the child can tell the interaction registered.

### FR-CHILD-006 — No reading requirement — P0
The child shall not need to read text to understand or complete the learning interaction. Essential instructions shall be conveyed through audio, visuals, or learned interaction patterns.

### FR-CHILD-007 — No performance score — P0
The child interface shall not display numerical or symbolic performance scores such as percentages, stars, grades, levels, or pronunciation scores.

### FR-CHILD-008 — No gamified retention mechanics — P0
Prototype 0.1 shall not contain child-facing streaks, coins, lives, leaderboards, badges, daily quests, reward economies, or forced unlock progression.

### FR-CHILD-009 — No “wrong” state — P0
Miozira shall not label a child's spoken attempt as wrong, incorrect, or failed. Red-X feedback, failure buzzers, negative correction language, or loss of progress due to pronunciation are prohibited.

### FR-CHILD-010 — Child may stop at any time — P0
The child shall be able to stop interacting at any point without losing previously persisted learning history and without receiving a penalty.

---

# 4. Concept and Language Requirements

### FR-CONTENT-001 — Eight canonical concepts — P0
Prototype 0.1 shall include exactly these concepts:

1. apple
2. ball
3. cup
4. hand
5. nose
6. cat
7. car
8. water

### FR-CONTENT-002 — Stable concept identity — P0
Each concept shall have an internal stable identifier independent of language, such as `concept.apple`.

### FR-CONTENT-003 — Two languages — P0
Prototype 0.1 shall support English and Tamil.

### FR-CONTENT-004 — Sixteen canonical concept-language pairs — P0
Every canonical concept shall have one canonical entry in each supported language, for 8 × 2 = 16 concept-language pairs.

### FR-CONTENT-005 — Concept-first model — P0
The product shall model `concept → language-specific spoken form` rather than treating English as the mandatory source representation for Tamil.

### FR-CONTENT-006 — Local canonical audio — P0
Every concept-language pair shall have reviewed canonical audio available locally.

---

# 5. Core Learning Loop

### FR-LOOP-001 — Tap-to-hear — P0
When the child taps the active concept, Miozira shall play the canonical spoken word for the active language.

### FR-LOOP-002 — Audio replay — P0
The child shall be able to hear the active spoken word again using a simple child-safe interaction.

### FR-LOOP-003 — Imitation invitation — P0
Miozira shall provide a brief, child-friendly invitation to imitate the spoken word. No text, countdown, or pressure shall be required.

### FR-LOOP-004 — Speaking is optional — P0
The session shall continue if the child remains silent. Silence shall not block progress or create a failure state.

### FR-LOOP-005 — Gentle response — P0
After the speaking opportunity, Miozira shall provide a neutral-to-positive response, such as acknowledgement, a replay of the canonical model, a small animation, or progression.

### FR-LOOP-006 — Re-model after uncertainty — P1
If no speaking attempt is detected, Miozira may replay/model the word before moving on. This shall be presented as another example, not as correction.

### FR-LOOP-007 — Automatic continuation — P0
The child shall not need to navigate a menu to reach the next learning interaction.

---

# 6. Session Requirements

### FR-SESSION-001 — Typical 5–10 minute design target — P0
A normal session should naturally fit within approximately 5–10 minutes. This is not a child-facing completion target.

### FR-SESSION-002 — No minimum duration — P0
Miozira shall not require a minimum session duration.

### FR-SESSION-003 — Early ending persists progress — P0
When a session ends early, completed meaningful interactions shall remain stored.

### FR-SESSION-004 — Familiar opening — P1
When learning history exists, sessions should normally begin with at least one relatively familiar concept-language pair.

### FR-SESSION-005 — Mixed difficulty — P1
Sessions should include a mixture of familiar, due, less-familiar, and newly introduced concept-language pairs.

### FR-SESSION-006 — Conservative novelty — P1
A session shall introduce only a small number of unfamiliar concept-language pairs. Exact limits will be defined in `adaptive-engine.md`.

### FR-SESSION-007 — Easy ending — P1
Where practical, a session should finish with a familiar, easy, or enjoyable interaction.

### FR-SESSION-008 — No child-facing timer — P0
The child interface shall not display a countdown or remaining-session meter.

### FR-SESSION-009 — No completion pressure — P0
Miozira shall not pressure the child with messages such as “finish today's lesson,” “only two left,” or “don't break your streak.”

---

# 7. Adaptive Learning Requirements

### FR-ADAPT-001 — Local adaptation — P0
Future concept exposure shall adapt using locally stored learning evidence only.

### FR-ADAPT-002 — Explainable rules — P0
Prototype 0.1 adaptation shall use deterministic or otherwise understandable rules, not an opaque remote ML ranking system.

### FR-ADAPT-003 — Exposure history — P0
The system shall record when a concept-language pair is meaningfully presented.

### FR-ADAPT-004 — Last exposure — P0
The system shall track the most recent meaningful exposure for each concept-language pair.

### FR-ADAPT-005 — Exposure count — P0
The system shall count meaningful exposures per concept-language pair.

### FR-ADAPT-006 — Replay evidence — P1
The system shall be capable of recording meaningful audio replay activity.

### FR-ADAPT-007 — Speaking-attempt evidence — P1
Where microphone support is enabled, the system shall record whether speech-like activity was detected.

### FR-ADAPT-008 — Parent real-world evidence — P0
The learning model shall be able to incorporate parent-reported recognition/use outside Miozira.

### FR-ADAPT-009 — Spaced resurfacing — P0
The same concept-language pair should not normally repeat immediately without intervening content.

### FR-ADAPT-010 — Intervening items — P1
When a concept returns during one session, other concept-language pairs should normally appear between exposures.

### FR-ADAPT-011 — Temporary rest — P1
The system shall support temporarily reducing exposure to repeatedly low-interaction concept-language pairs.

### FR-ADAPT-012 — Reintroduction after rest — P1
Rested concept-language pairs shall remain eligible for later reintroduction.

### FR-ADAPT-013 — No mastery percentage — P0
The adaptive engine shall not expose a percentage-based mastery score.

### FR-ADAPT-014 — No irreversible failure — P0
Silence, mispronunciation, disengagement, or skipped sessions shall never permanently mark a concept as failed.

---

# 8. Language Switching

### FR-LANG-001 — Independent playback — P0
Each concept shall be independently playable in English and Tamil.

### FR-LANG-002 — No mandatory translation chain — P0
Miozira shall not require every concept to play English and Tamil consecutively.

### FR-LANG-003 — Clear active language — P0
Each learning interaction shall have an unambiguous active language context.

### FR-LANG-004 — Structured switching test — P1
Prototype 0.1 shall support at least one controlled English/Tamil switching strategy for family testing.

### FR-LANG-005 — Optional comparison — P2
A limited cross-language comparison interaction may be supported for familiar concepts, but shall not be the default teaching pattern.

---

# 9. Audio Requirements

### FR-AUDIO-001 — Fully local learning audio — P0
Canonical learning audio shall be bundled or stored locally and playable without internet access.

### FR-AUDIO-002 — English audio — P0
All eight concepts shall have canonical English recordings.

### FR-AUDIO-003 — Tamil audio — P0
All eight concepts shall have canonical Tamil recordings.

### FR-AUDIO-004 — Pronunciation review — P0
Canonical target-language recordings shall be reviewed for pronunciation quality before the family test.

### FR-AUDIO-005 — No streaming dependency — P0
Core word playback shall not depend on streaming audio.

### FR-AUDIO-006 — No continuous background music — P0
The core learning interaction shall not contain continuous background music.

### FR-AUDIO-007 — Prevent overlapping speech — P0
Miozira shall prevent unintended overlap between spoken-word clips and instructional speech.

### FR-AUDIO-008 — Interruption recovery — P1
The app shall recover gracefully when audio playback is interrupted by the operating system or another audio event.

---

# 10. Family Voice Requirements

### FR-FAMILY-001 — Parent recording capability — P1
Prototype 0.1 should allow a parent to record family voice audio for supported concept-language pairs.

### FR-FAMILY-002 — Local-only family recordings — P0
Family recordings shall remain local to the device in Prototype 0.1.

### FR-FAMILY-003 — Preserve canonical audio — P1
A family recording shall not automatically delete or permanently replace the canonical language model.

### FR-FAMILY-004 — Re-record/delete — P1
The parent shall be able to replace or delete a family recording.

### FR-FAMILY-005 — Canonical fallback — P1
If a family recording is absent, canonical audio shall continue to work.

---

# 11. Microphone Requirements

### FR-MIC-001 — Microphone optional — P0
The learning experience shall remain functional without microphone permission.

### FR-MIC-002 — Parent-context permission request — P1
Microphone permission shall be requested with a parent-understandable explanation instead of appearing unexpectedly in the child's first speaking interaction.

### FR-MIC-003 — Speech-presence purpose only — P0
Prototype 0.1 shall use microphone input only to infer whether a speech-like attempt probably occurred.

### FR-MIC-004 — No transcription — P0
Prototype 0.1 shall not convert the child's speech into displayed or stored text.

### FR-MIC-005 — No pronunciation grading — P0
Prototype 0.1 shall not determine or display pronunciation correctness.

### FR-MIC-006 — No cloud processing — P0
Child microphone input shall not be sent to a remote server.

### FR-MIC-007 — No permanent raw child audio — P0
Ordinary child learning sessions shall not permanently save raw speech audio.

### FR-MIC-008 — Permission-denial fallback — P0
When microphone permission is denied, audio learning, voluntary repetition, and progression shall still work.

### FR-MIC-009 — Hardware/API failure fallback — P0
Microphone unavailability shall not block the learning loop.

---

# 12. Parent Area Requirements

### FR-PARENT-001 — Parent area — P1
Miozira shall provide a separate parent-facing area.

### FR-PARENT-002 — Child-resistant entry — P1
The parent area shall not be reachable through a single obvious child tap.

### FR-PARENT-003 — Adult UI allowed — P1
The parent area may use text, ordinary controls, lists, settings, and confirmation dialogs.

### FR-PARENT-004 — View concept activity — P1
The parent shall be able to view basic recent activity for the eight concepts.

### FR-PARENT-005 — Distinguish languages — P1
The parent shall be able to distinguish English and Tamil activity where relevant.

### FR-PARENT-006 — No misleading mastery claims — P0
The parent area shall not present unsupported claims such as “93% fluent,” “Tamil mastered,” or “pronunciation score 87%.”

### FR-PARENT-007 — Evidence-oriented wording — P1
Parent progress wording should use observations such as heard, attempted, replayed, recognized outside Miozira, and used outside Miozira.

### FR-PARENT-008 — Reset prototype data — P1
The parent shall be able to intentionally reset locally stored prototype learning data.

### FR-PARENT-009 — Reset confirmation — P1
Destructive reset shall require explicit parent confirmation.

---

# 13. Real-World Transfer

### FR-REAL-001 — Mark recognition outside app — P0
The parent shall be able to record that the child recognized a concept/word outside Miozira.

### FR-REAL-002 — Mark use outside app — P0
The parent shall be able to record that the child used a concept/word outside Miozira.

### FR-REAL-003 — Link observation to concept/language — P0
A real-world observation shall be linked to the relevant concept and language where known.

### FR-REAL-004 — Parent-reported evidence — P0
Real-world observations shall be treated as parent-reported, not machine-verified.

### FR-REAL-005 — Observation timestamp — P1
The system shall store when the parent observation was recorded.

### FR-REAL-006 — One short suggestion — P1
After a meaningfully active session, Miozira shall provide one short real-world language suggestion to the parent.

### FR-REAL-007 — Suggestion uses recent learning — P1
The suggestion should reference a recently encountered concept-language pair.

### FR-REAL-008 — Suggestion includes a natural context — P1
The suggestion should identify an ordinary family moment such as snack time, dressing, play, bath, or travel.

### FR-REAL-009 — No quizzing instruction — P0
The suggestion shall not instruct the parent to test the child. It should encourage natural language use instead.

---

# 14. Offline and Persistence

### FR-OFFLINE-001 — Core app works offline — P0
After installation/setup, the Prototype 0.1 core experience shall work without internet connectivity.

### FR-OFFLINE-002 — Child session offline — P0
Child learning sessions shall not require a network connection.

### FR-OFFLINE-003 — Audio offline — P0
Canonical and family audio shall play offline.

### FR-OFFLINE-004 — Progress writes offline — P0
Learning events shall persist without internet access.

### FR-OFFLINE-005 — Parent area offline — P0
Parent progress and observation tools shall remain available offline.

### FR-OFFLINE-006 — Suggestions offline — P0
Real-world suggestions shall not require a network service.

### FR-DATA-001 — Local source of truth — P0
Prototype learning history shall use local persistent storage as the source of truth.

### FR-DATA-002 — Session persistence — P0
Meaningful session history shall survive app restarts.

### FR-DATA-003 — Concept evidence persistence — P0
Per concept-language learning evidence shall persist between sessions.

### FR-DATA-004 — Parent observations persistence — P0
Real-world observations shall persist locally.

### FR-DATA-005 — Settings persistence — P0
Required parent settings shall persist locally.

### FR-DATA-006 — Family audio persistence — P1
Family recordings shall persist locally until deleted or reset.

### FR-DATA-007 — No server identifiers — P0
Prototype 0.1 shall not require server-generated user, child, session, or device identifiers.

---

# 15. Privacy Requirements

### FR-PRIV-001 — No account — P0
Prototype 0.1 shall not require registration or sign-in.

### FR-PRIV-002 — No child legal name required — P0
The child's legal name shall not be required.

### FR-PRIV-003 — No email required — P0
An email address shall not be required for ordinary use.

### FR-PRIV-004 — No location permission — P0
Prototype 0.1 shall not request device location permission.

### FR-PRIV-005 — No contacts permission — P0
Prototype 0.1 shall not request contact-book access.

### FR-PRIV-006 — No advertising identifier — P0
Miozira shall not intentionally access the Android Advertising ID.

### FR-PRIV-007 — No ads — P0
Prototype 0.1 shall contain no advertising.

### FR-PRIV-008 — No behavioural analytics SDK — P0
Prototype 0.1 shall not include third-party behavioural analytics SDKs.

### FR-PRIV-009 — No remote session replay — P0
Prototype 0.1 shall not include remote session-recording or session-replay telemetry.

### FR-PRIV-010 — No child audio upload — P0
Prototype 0.1 shall not upload child microphone input.

### FR-PRIV-011 — Parent deletion/reset — P1
The parent shall have a practical way to delete/reset locally stored prototype data.

---

# 16. Platform and Layout

### FR-PLAT-001 — Android-first — P0
Prototype 0.1 shall be implemented and validated on Android tablets first.

### FR-PLAT-002 — Current target-API planning — P0
Implementation shall be compatible with the applicable modern Google Play target API requirement. From 2026-08-31, new apps and app updates must target Android 16 / API level 36.

### FR-PLAT-003 — Midrange hardware — P0
Core functionality shall not require flagship-class processing.

### FR-PLAT-004 — No AI accelerator dependency — P0
No NPU or dedicated AI accelerator shall be required for core behaviour.

### FR-LAYOUT-001 — Portrait support — P0
The child experience shall function in portrait orientation.

### FR-LAYOUT-002 — Landscape support — P0
The child experience shall function in landscape orientation.

### FR-LAYOUT-003 — Adaptive layout — P0
Portrait and landscape shall be responsive arrangements of the same experience, not separate product modes.

### FR-LAYOUT-004 — Preserve active state — P0
Orientation changes shall not unintentionally restart or discard the active learning interaction.

### FR-LAYOUT-005 — Parent area responsive — P1
The parent area shall remain usable in portrait and landscape.

---

# 17. Accessibility and Usability

### FR-ACCESS-001 — No colour-only meaning — P0
Essential state shall not be communicated using colour alone.

### FR-ACCESS-002 — Clear contrast — P0
Essential foreground elements shall maintain clear visual contrast.

### FR-ACCESS-003 — Low visual complexity — P0
The child learning screen shall avoid visually crowded backgrounds.

### FR-ACCESS-004 — Foreground emphasis — P0
The active concept shall be visually more prominent than decorative elements.

### FR-ACCESS-005 — Forgiving touch — P0
The child UI shall tolerate reasonable imprecision around intended touch targets.

### FR-ACCESS-006 — Adequate target spacing — P0
Simultaneous child-facing interactive targets shall be separated enough to reduce accidental activation.

### FR-ACCESS-007 — Avoid tiny edge controls — P0
Essential child controls shall not depend on small targets at screen edges.

### FR-ACCESS-008 — Clear spoken audio — P0
Canonical learning audio shall be intelligible at normal tablet listening volumes.

### FR-ACCESS-009 — No overlapping instructional speech — P0
Miozira shall avoid simultaneous spoken prompts that compete with the target word.

---

# 18. Error and Recovery

### FR-ERROR-001 — No technical errors in child mode — P0
The child shall never see raw exceptions, stack traces, database errors, or technical permission jargon.

### FR-ERROR-002 — Optional-audio fallback — P1
If family audio is missing, canonical audio shall be used where possible.

### FR-ERROR-003 — Valid restart state — P0
After the app is killed or closed, Miozira shall restart into a valid state.

### FR-ERROR-004 — Preserve persisted evidence — P0
A crash or forced close shall not erase previously persisted completed interactions.

### FR-ERROR-005 — Isolate optional-data failure — P1
Failure of optional local data should not make the whole child experience unusable where recovery is feasible.

---

# 19. Parent Gate

### FR-GATE-001 — Hide adult controls — P0
Ordinary settings and destructive controls shall not appear in the normal child learning interface.

### FR-GATE-002 — Deliberate adult action — P1
Entering the parent area shall require an interaction unlikely to occur accidentally during normal child use.

### FR-GATE-003 — Avoid child-oriented “puzzle gate” — P1
The gate should not present a child-attractive learning puzzle that encourages the child to solve it. Exact mechanism will be defined in `ui-ux.md`.

---

# 20. Family-Test Observability

### FR-TEST-001 — Record session boundaries — P0
The system shall identify when a meaningful session begins and ends.

### FR-TEST-002 — Record concept exposures — P0
The system shall retain enough evidence to reconstruct which concept-language pairs were encountered.

### FR-TEST-003 — Record replays — P1
The system should record meaningful replay activity.

### FR-TEST-004 — Record attempt/no-attempt — P1
Where microphone detection is enabled, the system shall record attempt/no-attempt without saving raw child audio.

### FR-TEST-005 — Record real-world observations — P0
The system shall retain parent-reported recognition/use events.

### FR-TEST-006 — No remote telemetry dependency — P0
The initial family test shall be analysable from local data and parent observation alone.

### FR-TEST-007 — Primary validation question — P0
Prototype evaluation shall be able to answer:

> Can the child use Miozira for roughly 5–10 minutes, understand what to do with little instruction, willingly interact with some spoken words, and later recognize or use at least one away from the tablet?

### FR-TEST-008 — Independent-use observation — P0
The test protocol shall observe whether repeated adult instruction is necessary.

### FR-TEST-009 — Voluntary repetition observation — P0
The test protocol shall observe whether the child voluntarily attempts words.

### FR-TEST-010 — Replay observation — P1
The test protocol shall observe whether the child voluntarily replays audio.

### FR-TEST-011 — Return observation — P1
The test protocol shall observe whether the child voluntarily returns to or requests Miozira again.

### FR-TEST-012 — Real-world transfer observation — P0
The test protocol shall observe recognition or use outside the app.

---

# 21. Anti-Requirements

The following must **not** be introduced into Prototype 0.1 without formally revising scope.

| ID | Anti-requirement |
|---|---|
| AR-001 | No backend |
| AR-002 | No account/login system |
| AR-003 | No cloud database |
| AR-004 | No cloud ASR |
| AR-005 | No pronunciation scoring |
| AR-006 | No generative-AI dependency |
| AR-007 | No advertising |
| AR-008 | No subscription |
| AR-009 | No in-app-purchase flow |
| AR-010 | No social/messaging features |
| AR-011 | No push notifications for engagement |
| AR-012 | No streak mechanics |
| AR-013 | No child leaderboard |
| AR-014 | No forced daily target |
| AR-015 | No reading-based child quiz |

---

# 22. Priority Definitions

**P0 — Prototype blocker:** Without this requirement, Prototype 0.1 cannot validly test its core hypothesis.

**P1 — Strong prototype requirement:** Important to the intended Miozira experience, but a narrowly degraded fallback may still permit testing.

**P2 — Supporting requirement:** Useful but simplifiable without invalidating the initial family test.

---

# 23. P0 Baseline Summary

The Prototype 0.1 P0 baseline is:

- 8 concepts;
- English + Tamil;
- concept-first model;
- tap-to-hear;
- fully local canonical audio;
- no reading dependency;
- no scores/gamification;
- no negative pronunciation judgement;
- session and concept evidence persistence;
- explainable adaptive spacing;
- fully offline child experience;
- parent-reported real-world recognition/use;
- portrait + landscape;
- no backend;
- no cloud speech;
- no raw child-audio retention;
- family-test observability.

---

# 24. Traceability to Product Principles

| Product principle | Requirement groups |
|---|---|
| Audio first | FR-AUDIO, FR-LOOP |
| Meaning before translation | FR-CONTENT, FR-LANG |
| Interaction before instruction | FR-CHILD, FR-LOOP |
| Gentle responses | FR-CHILD-009, FR-LOOP |
| Repetition without drilling | FR-ADAPT |
| Child may stop | FR-CHILD-010, FR-SESSION |
| Real-world transfer | FR-REAL |
| Family participation | FR-FAMILY, FR-PARENT |
| Privacy by architecture | FR-PRIV, FR-OFFLINE |
| Calm design | FR-CHILD, FR-ACCESS |
| Learning over engagement | FR-SESSION, AR-011–AR-014 |

---

# 25. Research Traceability

The requirements implement findings recorded in `research.md`, including:

- multilingual exposure is valid but should be structured;
- input amount and quality matter;
- concept-to-word mapping should remain primary;
- spaced retrieval is preferable to massed drilling;
- caregiver/social interaction matters;
- preschool touchscreen interfaces should use simple, large, forgiving targets;
- child ASR remains too variable for pronunciation authority;
- microphone data is sensitive;
- local/offline operation materially simplifies the privacy posture;
- modern Android layouts should be adaptive;
- simple multilingual flashcards alone are not sufficient differentiation for Miozira.

---

# 26. Change Policy

A requirement may be changed when:

- family-test evidence contradicts it;
- a technical constraint makes it unsafe or impractical;
- research materially changes the rationale;
- Prototype 0.1 scope is formally revised.

Each change should record:

- requirement ID;
- previous wording;
- new wording;
- reason;
- downstream documents affected.

---

# 27. Definition of Ready for Detailed Technical Design

Prototype 0.1 is ready for detailed technical/system design when:

- this document is accepted as the functional baseline;
- non-functional requirements are documented;
- the learning states and semantics are defined;
- adaptive behaviour is specified;
- unresolved behaviour is explicitly listed rather than silently assumed.

Engineering documents must implement these requirements without silently widening Prototype 0.1.
