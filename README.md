# Miozira

> **An audio-first multilingual learning environment for young pre-readers that helps language move from the tablet into real family life.**

---

## 1. What is Miozira?

Miozira is an offline-first, tablet-native language learning application designed for young pre-readers (ages ~4–5). Unlike conventional gamified learning apps that rely on hyper-stimulating feedback loops, streaks, leaderboards, and artificial pressure, Miozira is built on a quiet, low-stimulation philosophy:

$$\text{SEE} \longrightarrow \text{HEAR} \longrightarrow \text{IMITATE IF THE CHILD WANTS} \longrightarrow \text{ENCOUNTER AGAIN LATER} \longrightarrow \text{USE WITH A HUMAN}$$

### Key Principles
- **Quiet Interface, Vivid Reality:** The concept illustration itself is the most colorful element on screen. No flashing animations, neon backgrounds, confetti, or gamified tokens.
- **Audio-First & Natural:** Tap a picture to hear canonical native pronunciation with natural warmth.
- **No-Pressure Attempt Detection:** The microphone detects only whether a speech-like sound was attempted. It performs **no transcription, no pronunciation scoring, and no storage of raw child speech**.
- **Real-World Transfer:** The goal is not screen time; it is transferring learned vocabulary into daily family routines through one calm, context-aware suggestion for parents after a session.
- **100% Offline & Private:** Zero network calls, zero tracking/telemetry, zero cloud storage, zero ads, zero subscriptions.

---

## 2. What is Prototype 0.1?

Prototype 0.1 is a focused vertical slice built to validate the core learning loop in a **7-day family test**:

- **Target Audience:** Children approximately 4–5 years old.
- **Primary Platform:** Android tablet (landscape + portrait support).
- **Languages:** English + Tamil (dual-language pairing).
- **Curriculum:** 8 core concepts (16 concept-language pairs):
  1. `apple` (`ஆப்பிள்`)
  2. `ball` (`பந்து`)
  3. `cup` (`கப்` / `கோப்பை`)
  4. `hand` (`கை`)
  5. `nose` (`மூக்கு`)
  6. `cat` (`பூனை`)
  7. `car` (`கார்`)
  8. `water` (`தண்ணீர்`)
- **Adaptive Repetition:** Local spaced repetition engine that weights exposure intervals based on child engagement and parent real-world observation.
- **Protected Parent Area:** Behind a simple parent gate, adults can mark if a word was recognized/used in real life, optionally record custom family voice pronunciations, or reset all data.

---

## 3. Current Project Status

- [x] **Comprehensive Documentation Suite Completed:** 39 canonical specifications covering foundation, pedagogy, design, engineering, privacy, and quality are locked in `/docs`.
- [x] **Architecture Decision Records (ADRs):** [ADR-0001](docs/decisions/ADR-0001-technology-stack.md) accepted (Kotlin Multiplatform + Compose Multiplatform with native Android fallback).
- [x] **Repository Structure Initialized:** Base multiplatform directories and build targets structured.
- [ ] **Next Phase:** Execute the **Mandatory Technical Spike** to validate tap-to-audio latency, memory-only microphone attempt detection, and local Room persistence on target Android hardware before commencing full feature implementation.

---

## 4. Documentation Hierarchy

All product and engineering specifications live under [`docs/`](docs/README.md):

```text
docs/
├── 00-foundation/        # Vision, prototype scope, functional/non-functional requirements, research
├── 01-learning/          # Learning model, adaptive engine, content spec, session design, transfer loop
├── 02-design/            # Design system, color palette, UI/UX flows, child/parent experience, accessibility
├── 03-engineering/       # Architecture, database schema, API contracts, audio/mic pipelines, performance
├── 04-privacy-safety/    # Offline guarantees, child safety invariants, threat model, data inventory
├── 05-quality/           # Acceptance criteria, test plans, hardware matrix, family test protocol, release
├── decisions/            # Architecture Decision Records (ADRs)
└── OPEN-QUESTIONS.md     # Documented tensions, lexical decisions, and spike validation items
```

See the [Documentation Index](docs/README.md) for direct links to all specifications.

---

## 5. How Implementation Should Proceed

Implementation follows a strict, step-by-step validation discipline:

1. **Step 1: Technical Spike**
   - Build a minimal vertical slice to validate:
     1. Full-screen concept presentation (Compose Multiplatform on Android).
     2. Bundled canonical audio playback ($\le 250\text{ ms}$ audio latency, $\le 100\text{ ms}$ tap acknowledgement).
     3. 2–3 second memory-only speech attempt detector.
     4. Local Room database persistence of exposure/attempt records.
     5. Screen rotation and lifecycle persistence without duplicated evidence.
2. **Step 2: Core Domain & Data Layer**
   - Implement the offline repository, SQLite/Room schema, and adaptive repetition engine with pure unit test coverage.
3. **Step 3: Audio & Microphone Engine**
   - Implement platform audio playback and safe in-memory voice activity detection.
4. **Step 4: Presentation & UI Flows**
   - Implement the calm child session interface and the protected parent portal according to the locked design system.
5. **Step 5: Content Assets & Integration**
   - Bundle canonical illustrations and English/Tamil audio recordings for all 8 concepts.
6. **Step 6: Quality Verification & Family Test Deployment**
   - Execute test suites against the [Device Test Matrix](docs/05-quality/device-test-matrix.md) and prepare side-load builds for the 7-day family trial.

---

## 6. Operating Rules for Contributors & AI Agents

Operating rules, design invariants, and development guidelines are strictly governed by [`AGENTS.md`](AGENTS.md). 
Contributors and AI agents must never introduce gamification, cloud backends, analytics SDKs, network permissions, or unapproved product features.
