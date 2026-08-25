# Miozira Prototype 0.1 — Product Requirements Document (PRD)

**Document:** `docs/00-foundation/PRD-prototype-0.1.md`  
**Version:** 0.1.0  
**Status:** Draft — Reconciled with Canonical Specifications  
**Product:** Miozira  
**Target Platform:** Android Tablet (Landscape + Portrait)  
**Target Milestone:** 7-Day Family Test  

---

## 1. Product Summary

Miozira is an offline-first, tablet-native language learning environment designed for young pre-readers (approximately 4–5 years old). Unlike conventional gamified learning apps that utilize high-stimulation dopamine loops, streaks, leaderboards, and grading, Miozira implements a calm, audio-first pedagogical loop:

$$\text{SEE} \longrightarrow \text{HEAR} \longrightarrow \text{IMITATE IF THE CHILD WANTS} \longrightarrow \text{ENCOUNTER AGAIN LATER} \longrightarrow \text{USE WITH A HUMAN}$$

The objective of Miozira is not to maximize tablet screen time, but to facilitate language transfer from the tablet into real family routines and human conversations.

---

## 2. Problem

1. **Digital Overstimulation:** Most commercial early-childhood language applications rely on rapid animations, cartoon sirens, neon palettes, XP, and streaks that encourage compulsive tapping rather than acoustic and semantic processing.
2. **Artificial High-Pressure Feedback:** Conventional apps force speech recognition or pronunciation scoring on young children, producing anxiety, false error states, and discouragement when normal developmental phonetic approximations are marked "incorrect".
3. **Screen Isolation:** Existing apps trap vocabulary inside the device; they do not bridge learned words into family interactions or daily domestic routines where real language acquisition occurs.
4. **Privacy Invasions:** Many children's apps upload voice recordings and usage telemetry to cloud servers, introducing privacy, consent, and regulatory risks.

*Reference:* See [`docs/00-foundation/research.md`](research.md) and [`docs/00-foundation/product-vision.md`](product-vision.md).

---

## 3. Target Users

- **Primary User (Learner):** One young child (approx. 4–5 years old), typically pre-literate, with variable attention span, emergent motor skills, and developing phonological articulation. The child must never be required to read text or interpret complex navigation.
- **Secondary User (Caregiver/Parent):** One parent or caregiver who manages initial setup, views high-level engagement, optionally records family custom pronunciations, and receives post-session real-world transfer suggestions.

*Reference:* See [`docs/00-foundation/prototype-0.1-scope.md`](prototype-0.1-scope.md) §5 and [`docs/02-design/child-experience.md`](../02-design/child-experience.md).

---

## 4. Product Hypothesis

> If a 4–5-year-old child interacts with a calm, responsive, audio-first tablet interface for 5–10 minutes daily without grading or time pressure, they will willingly engage with spoken native words and later recognize or use at least one target word during offline family routines.

---

## 5. Prototype 0.1 Scope

Prototype 0.1 is a strictly bounded vertical slice for a 7-day family test on a single Android tablet:
- **Curriculum:** Exactly 8 core concrete concepts across 2 languages (English + Tamil) = 16 concept-language pairs.
- **Delivery Mode:** 100% offline standalone client. No backend, no accounts, no sync, no telemetry.
- **Orientation:** Android tablet landscape (primary) and portrait (secondary).
- **Core Loop:** Fullscreen visual card → tap to play canonical native audio → optional 2–3s memory-only speech attempt detection → gentle neutral feedback → adaptive scheduling.
- **Parent Surface:** Protected parent gate → lightweight summary + real-world transfer marking + optional local family voice recording.

*Reference:* See [`docs/00-foundation/prototype-0.1-scope.md`](prototype-0.1-scope.md).

---

## 6. Core Child Experience

- **Single Active Focus:** Exactly one concept card presented at a time.
- **Tap Interaction:** Tapping anywhere on the concept illustration triggers the canonical spoken word.
- **Audio Response:** Immediate tap acknowledgement ($\le 100\text{ ms}$) and low-latency native audio start ($\le 250\text{ ms}$).
- **Optional Speaking Window:** Following playback, a calm slate-blue indicator signals that the app is listening for 2–3 seconds.
- **Zero Judgment:** 
  - If the child speaks: subtle, non-evaluative visual confirmation (soft ripple/glow).
  - If the child stays silent: app peacefully advances or allows replay.
  - No buzzer, no red cross, no "Try Again!", no stars, no point rewards.
- **Normal Child Behaviors Supported:** Replaying words indefinitely, wandering away, silence, mispronunciation, or stopping mid-session are all treated as valid interactions.

*Reference:* See [`docs/02-design/child-experience.md`](../02-design/child-experience.md) and [`docs/02-design/interaction-states.md`](../02-design/interaction-states.md).

---

## 7. Parent Experience

- **Protected Parent Gate:** Discreet hidden long-press gesture ($\approx 2\text{ s}$ hold on corner hotspot) designed to prevent accidental toddler navigation without turning access into an intriguing puzzle.
- **Session Insights:** High-level summary of active concepts and encounters without evaluative child grading.
- **Real-World Transfer Logging:** Simple checkboxes for parents to record:
  - *Recognized outside Miozira* (e.g. child pointed to an apple when hearing the word).
  - *Used outside Miozira* (e.g. child spoke the Tamil word for water at dinner).
- **Family Voice Recording:** Adult-controlled recording flow allowing a parent to record custom audio for any concept-language pair, stored locally on the device.
- **Reset Controls:** Complete local database and family audio wipe facility.

*Reference:* See [`docs/02-design/parent-experience.md`](../02-design/parent-experience.md) and [`docs/01-learning/real-world-transfer.md`](../01-learning/real-world-transfer.md).

---

## 8. Learning Model Summary

The learning model follows an evidence-based acquisition cycle:
1. **Perception (See & Hear):** Multi-sensory pairing of vivid illustration with clear canonical native audio.
2. **Production (Optional Imitation):** Low-affect, unpressured opportunity to imitate the word aloud.
3. **Retention (Adaptive Re-encounter):** Spaced re-presentation driven by local memory half-life decay.
4. **Transfer (Human-to-Human Use):** Transfer into real-world household context guided by post-session parent prompts.

*Reference:* See [`docs/01-learning/learning-model.md`](../01-learning/learning-model.md).

---

## 9. Adaptive Engine Summary

- **Local Spaced Repetition (SRS):** Runs entirely on local SQLite/Room.
- **Concept-Language Independence:** Each pair (e.g., `cat:en` vs `cat:ta`) maintains independent scheduling state.
- **Inputs to Scheduling:**
  - Exposure count and timestamp.
  - Child attempt occurrence (binary flag: `ATTEMPTED` vs `UNOBSERVED`).
  - Replay frequency within a session.
  - Parent real-world transfer observations (`RECOGNIZED`, `USED_SPONTANEOUSLY`).
- **Session Construction:** Each 5–10 minute session dynamically selects 4–8 items combining:
  1. Due review items (high priority based on memory decay).
  2. Active learning items.
  3. New concept introduction (max 1–2 new items per session).

*Reference:* See [`docs/01-learning/adaptive-engine.md`](../01-learning/adaptive-engine.md) for scheduling constants, intervals, decay formulas, and selection algorithms.

---

## 10. Content Scope

Prototype 0.1 contains exactly the **8 canonical concepts** across **2 languages** (16 pairs):

| Concept ID | English Form | Tamil Form | Role & Transfer Context |
|---|---|---|---|
| `concept.apple` | Apple | ஆப்பிள் (*aappiL*) | Familiar food, snack-time transfer |
| `concept.ball` | Ball | பந்து (*pandhu*) | Familiar toy, play routines |
| `concept.cup` | Cup | கப் (*kap*) / கோப்பை (*koappai*) | Mealtime object; conversational loan `கப்` preferred |
| `concept.hand` | Hand | கை (*kai*) | Body part, daily routines (washing hands) |
| `concept.nose` | Nose | மூக்கு (*mookku*) | Body part, physical self-awareness |
| `concept.cat` | Cat | பூனை (*poonai*) | Familiar domestic animal |
| `concept.car` | Car | கார் (*kaar*) | Familiar vehicle / toy |
| `concept.water` | Water | தண்ணீர் (*thanniir*) | Daily meal/drinking routine; natural spoken register |

Each concept requires:
1. One high-clarity SVG/PNG illustration (warm, hand-crafted aesthetic, not stock vector or clip-art).
2. Canonical studio-quality native audio recording meeting the performance and clarity standards in `docs/03-engineering/audio.md`.

*Reference:* See [`docs/01-learning/content-spec.md`](../01-learning/content-spec.md).

---

## 11. Design Philosophy

**Quiet interface. Vivid reality. Meaningful motion. Calm curiosity.**

- **Color Palette:**
  - Child background: `#F2EEE7` (warm stone field, non-stimulating)
  - Primary text/icon content: `#26312E` (deep green-charcoal)
  - Primary interactive action: `#4F746B` (muted eucalyptus)
  - Listening indicator: `#607C8A` (calm slate blue)
  - Non-evaluative response: `#6B756F` (neutral warm gray)
- **Hierarchy:** The concept visual must always be the most saturated, colorful element on the screen.
- **Motion Rules:** Subtle physics-based transitions only when clarifying state or cause-and-effect. Never use confetti, particle bursts, or flashing reward animations.

*Reference:* See [`docs/02-design/design-system.md`](../02-design/design-system.md) and [`docs/02-design/color-palette.md`](../02-design/color-palette.md).

---

## 12. Privacy & Safety Requirements

1. **No Network Permission:** The `android.permission.INTERNET` permission is completely omitted from `AndroidManifest.xml`.
2. **Child Speech PCM Memory-Only:** Audio buffers captured from the microphone during child sessions must remain strictly in volatile RAM; raw child speech must never be written to flash storage or database.
3. **No ASR / Transcription:** The app never runs speech-to-text or transcription models on child voice.
4. **No Third-Party SDKs:** Zero analytics, zero ad SDKs, zero remote configuration, zero crash telemetry daemons.
5. **No Cloud Backup:** Explicitly configure `android:allowBackup="false"` to prevent operating system cloud leakage of learning history.
6. **Parental Audio Ownership:** Optional family voice recordings are stored solely in the app's sandboxed private storage (`context.filesDir`) and wiped completely upon reset.

*Reference:* See [`docs/04-privacy-safety/privacy.md`](../04-privacy-safety/privacy.md), [`docs/04-privacy-safety/child-safety.md`](../04-privacy-safety/child-safety.md), and [`docs/04-privacy-safety/data-inventory.md`](../04-privacy-safety/data-inventory.md).

---

## 13. Functional Requirements

- **FR-01 App Lifecycle & Setup:** Clean launch, initialization of default concept inventory, and immediate readiness for child play.
- **FR-02 Concept Presentation:** Render full-screen responsive concept card in portrait and landscape tablet aspect ratios.
- **FR-03 Canonical Playback:** Instantaneous native audio playback on visual tap with active touch debounce.
- **FR-04 Speaking Attempt Window:** 2–3 second listening window post-playback; classify audio into binary `ATTEMPT_DETECTED` vs `NO_ATTEMPT` state.
- **FR-05 Interaction State Machine:** Explicit state transitions (`IDLE` → `PLAYING` → `LISTENING` → `FEEDBACK` → `RESOLVED`).
- **FR-06 Spaced Repetition Engine:** Idempotent database logging of exposures and attempt states, computing review intervals locally.
- **FR-07 Protected Parent Gate:** Discreet long-press gate blocking toddler exit to settings.
- **FR-08 Real-World Transfer Prompt:** Render one context-relevant routine suggestion (e.g. for dinner, bedtime, or play) following session completion.
- **FR-09 Transfer Observation Logger:** Parent ability to flag real-world word recognition or spontaneous use.
- **FR-10 Family Voice Recording:** Parent recording, playback, saving, and deletion of custom family audio clips.
- **FR-11 Data Reset:** One-touch purge of all database tables and audio files in private storage.

*Reference:* See [`docs/00-foundation/requirements.md`](requirements.md) and [`docs/03-engineering/api.md`](../03-engineering/api.md).

---

## 14. Non-Functional Requirements

- **Tap Acknowledgement Latency:** $\le 100\text{ ms}$ (immediate visual feedback on touch).
- **Tap-to-Audio Playback Latency:** $\le 250\text{ ms}$ (audio start from touch).
- **Startup Time:** Cold launch $\le 4.0\text{ s}$; Warm launch $\approx 2.0\text{ s}$ on baseline midrange tablet.
- **Frame Rate:** Stable 60 fps during all Compose Multiplatform UI transitions and animations.
- **Memory Footprint:** Peak RSS $< 150\text{ MB}$; zero memory leaks on rapid concept replaying or orientation cycling.
- **Storage Footprint:** Total app package size $< 50\text{ MB}$ including all bundled assets.
- **Accessibility:** Minimum touch target size $\ge 64\text{ dp} \times 64\text{ dp}$; minimum text contrast ratio $\ge 4.5:1$.

*Reference:* See [`docs/00-foundation/non-functional-requirements.md`](non-functional-requirements.md) and [`docs/03-engineering/performance.md`](../03-engineering/performance.md).

---

## 15. Technical Architecture Summary

- **Architecture Pattern:** Clean Layered Architecture (`Presentation` → `Domain` → `Data` → `Platform`).
- **Language & Runtime:** Kotlin Multiplatform (KMP) + Compose Multiplatform (CMP) targeting Kotlin 2.x.
- **Platforms:** Android First (`minSdk 26+`, `targetSdk 34+`), with an isolated platform boundary allowing a future iOS shell.
- **Database & Persistence:** Android Room / SQLite KMP + AndroidX DataStore for key-value preferences.
- **Concurrency:** Kotlin Coroutines + `StateFlow` with structured lifecycle awareness.
- **Audio & Mic Pipeline:** Low-latency local playback and memory-only `AudioRecord` attempt detector. Exact runtime codec format and VAD thresholding are governed by the specialist specifications.

*Reference:* See [`docs/03-engineering/architecture.md`](../03-engineering/architecture.md), [`docs/03-engineering/audio.md`](../03-engineering/audio.md), and [`docs/decisions/ADR-0001-technology-stack.md`](../decisions/ADR-0001-technology-stack.md).

---

## 16. Explicit Non-Goals

The following are strictly outside the scope of Prototype 0.1:
- User registration, authentication, cloud sync, or multi-device accounts.
- Speech-to-text, phoneme grading, pronunciation scoring, or ASR evaluation.
- Gamification mechanisms: streaks, XP, coins, gems, leaderboards, timers, scores, badges.
- Remote content downloads, backend API servers, or over-the-air curriculum updates.
- Third or fourth language additions (beyond English and Tamil).
- Commercial app store monetization, subscriptions, in-app purchases, or ads.
- Multiple child profiles on a single device.

*Reference:* See [`docs/00-foundation/prototype-0.1-scope.md`](prototype-0.1-scope.md) §10.

---

## 17. Acceptance Criteria

Acceptance criteria are strictly defined in [`docs/05-quality/acceptance-criteria.md`](../05-quality/acceptance-criteria.md). A build is accepted for the family test only when all Blocker and High criteria pass:

### Blocker Criteria (AC-B)
- **AC-B01 (App Launches):** Cold and warm launch succeed on physical test tablet without crash loops.
- **AC-B02 (Core Loop Operable):** Touch $\to$ audio $\to$ optional mic window $\to$ gentle advance operates across all active pairs without dead ends.
- **AC-B03 (All 16 Pairs Available):** All 8 concepts in English and Tamil have approved visuals and audio.
- **AC-B04 (Exposure Integrity):** No duplicate database writes on orientation change, rapid re-tap, or process recreation.
- **AC-B05 (Child Microphone Privacy):** Child PCM is strictly volatile RAM; no disk writes, no transcripts, no pronunciation scores, no upload.
- **AC-B06 (Background Microphone Safety):** Microphone stops immediately when leaving interaction window, screen lock, or backgrounding app.
- **AC-B07 (Offline Operation):** Full child session works in airplane mode with zero network/cloud dependencies.
- **AC-B08 (Parent Reset):** Complete data reset cleanly purges sessions, adaptive state, real-world observations, and family audio files.
- **AC-B09 (Family Recording Privacy):** Adult recordings write to app-private storage only, excluded from media gallery and cloud backup.
- **AC-B10 (No Prohibited Product Mechanics):** Absolutely zero scores, streaks, XP, lives, leaderboards, badges, ads, paywalls, or grading.

### Key High Criteria (AC-H)
- **AC-H01 (Orientation):** Seamless portrait and landscape support preserving interaction state.
- **AC-H02 (Tap Responsiveness):** Touch acknowledgement $\le 100\text{ ms}$.
- **AC-H03 (Audio Responsiveness):** Canonical word audio playback begins $\le 250\text{ ms}$.
- **AC-H04 (Startup):** Cold launch $\le 4.0\text{ s}$, warm launch $\approx 2.0\text{ s}$.
- **AC-H07 (Parent Gate):** Unobtrusive $\approx 2\text{ s}$ long-press corner hotspot prevents accidental toddler access to settings.

*Reference:* For complete validation protocols and all HIGH/MEDIUM/LOW criteria, see [`docs/05-quality/acceptance-criteria.md`](../05-quality/acceptance-criteria.md) and [`docs/05-quality/qa-checklist.md`](../05-quality/qa-checklist.md).

---

## 18. Family-Test Success Criteria

During the 7-day trial with a 4–5-year-old child and caregiver:
1. **Uninstructed Usability:** The child understands the tap-to-listen interaction without adult coaching.
2. **Voluntary Participation:** The child voluntarily repeats or attempts words during at least some sessions.
3. **Calm Demeanor:** The child experiences no frustration or stress from failure states or time limits.
4. **Real-World Transfer:** The child demonstrates recognition or spontaneous use of at least **1 target word** in daily offline family life.
5. **Low Caregiver Friction:** Post-session real-world suggestions feel actionable, calm, and manageable.

*Reference:* See [`docs/05-quality/family-test-protocol.md`](../05-quality/family-test-protocol.md).

---

## 19. Risks & Mitigations

| Risk | Impact | Mitigation Strategy |
|---|---|---|
| **Microphone False Triggers (Room Noise)** | High | Use energy thresholding with a minimum duration gate (250 ms) and calibrate against ambient noise; validate during Technical Spike. |
| **Child Tap Frustration / Latency** | High | Pre-load active audio assets into memory and debounce touch triggers to achieve $\le 100\text{ ms}$ visual and $\le 250\text{ ms}$ audio response. |
| **KMP / Compose Tablet Quirks** | Medium | Execute the mandatory Technical Spike first; maintain native Jetpack Compose fallback path per ADR-0001. |
| **Premature App Abandonment** | Medium | Keep sessions brief (5–10 minutes) with natural ending states; do not enforce rigid session lengths. |

*Reference:* See [`docs/04-privacy-safety/threat-model.md`](../04-privacy-safety/threat-model.md) and [`docs/03-engineering/performance.md`](../03-engineering/performance.md).

---

## 20. Open Decisions

Tracked in [`docs/OPEN-QUESTIONS.md`](../OPEN-QUESTIONS.md):
- **OQ-001 (Tamil Lexical Choice for "Cup"):** `கப்` (conversational loan) vs `கோப்பை` (formal). Standardize on `கப்` with parent voice override available.
- **OQ-002 (Tamil Lexical Choice for "Water"):** Standardize on `தண்ணீர்` spoken with natural, warm maternal tone.
- **OQ-004 (VAD Engine Calibration):** Validate simple RMS energy duration gating during the Technical Spike before evaluating native WebRTC VAD.
- **OQ-005 (Android `minSdk`):** Confirm `minSdk 26` vs `minSdk 28` compatibility during the Technical Spike.

---

## 21. Definition of Prototype 0.1 Done

Prototype 0.1 is **DONE** when:
1. All Blocker and High Acceptance Criteria pass on the primary physical Android test tablet.
2. The 16 canonical audio assets and illustrations for the 8 canonical concepts are frozen and bundled.
3. Spaced repetition engine, parent portal, real-world prompts, and family recording work reliably offline.
4. The application APK is packaged, signed with a debug/local key, and ready for deployment in the 7-day family test.

---

## Scope Guard

> **CRITICAL ENFORCEMENT:** An engineering agent or developer must **NEVER** implement any of the following items in Prototype 0.1.

- [ ] **NO Gamification:** No XP, points, levels, scores, coins, gems, or badges.
- [ ] **NO Pressure Mechanics:** No countdown timers, speed challenges, lives, hearts, or streaks.
- [ ] **NO Correctness Scoring:** No pronunciation grades, percentage accuracy, red error buzzers, or "Wrong!" cues.
- [ ] **NO Sensory Overload:** No confetti bursts, celebration overlays, neon rainbow themes, or mascot animations.
- [ ] **NO Speech Storage:** Never write child microphone audio to SQLite, flash, or disk.
- [ ] **NO Speech Transcription:** Never add cloud or local ASR/speech-to-text models for child voice.
- [ ] **NO Network Dependency:** Never add `android.permission.INTERNET` or HTTP client networking libraries.
- [ ] **NO Accounts / Cloud:** Never add Firebase, AWS, Cognito, Supabase, user login, or remote analytics.
- [ ] **NO Multi-Profile Complexity:** Never build multi-child profile switchers or classroom management tools.
- [ ] **NO Extra Languages:** Do not add third/fourth languages beyond English and Tamil in this milestone.
- [ ] **NO Extra Concepts:** Do not add concepts beyond the locked 8 canonical concepts (`apple`, `ball`, `cup`, `hand`, `nose`, `cat`, `car`, `water`).
- [ ] **NO Commercial Scaffolding:** No Stripe, Google Play Billing, paywalls, subscriptions, or advertisements.
