# Miozira — AI Agent & Developer Operating Instructions

You are helping me build **Miozira**, an offline-first multilingual language-learning tablet app for young pre-readers.

---

## Important Context

I am not a traditional software developer. I use AI coding tools to build software. Explain major decisions in plain English and do not silently make large product or architectural decisions for me.

The repository contains a substantial set of product, learning, design, privacy, safety, engineering, and QA specifications under [`docs/`](docs/README.md).

**These documents are the single source of truth.**

Before writing production code:

1. Read the documentation under `docs/`.
2. Build a concise understanding of the product.
3. Identify contradictions, missing implementation decisions, or anything that would prevent Prototype 0.1 from being built (logging them in [`docs/OPEN-QUESTIONS.md`](docs/OPEN-QUESTIONS.md)).
4. **Do NOT invent new product features.**
5. **Do NOT expand scope beyond Prototype 0.1.**
6. **Do NOT rewrite the learning philosophy into a conventional gamified educational app.**
7. Preserve privacy, child-safety, low-stimulation, offline-first, and no-pressure principles.

---

## Product North Star

Miozira is an audio-first multilingual learning environment for young pre-readers that helps language move from the tablet into real family life.

The core loop is:

$$\text{SEE} \longrightarrow \text{HEAR} \longrightarrow \text{IMITATE IF THE CHILD WANTS} \longrightarrow \text{ENCOUNTER AGAIN LATER} \longrightarrow \text{USE WITH A HUMAN}$$

### Prototype 0.1 Specification
- **Form factor:** Android tablet first (landscape + portrait).
- **Target age:** Approximately 4–5 years old.
- **Concepts:** 8 concepts (`apple`, `ball`, `cup`, `hand`, `nose`, `cat`, `car`, `water`).
- **Languages:** English + Tamil (16 concept-language pairs).
- **Connectivity:** 100% offline; omit `INTERNET` permission.
- **Core interaction:** Tap picture → hear canonical native word.
- **Speaking attempt:** Completely optional; microphone detects only whether speech-like activity occurred.
- **Zero Evaluation:** No transcription, no pronunciation grading, no red/green correctness cues, and **no raw child speech storage**.
- **Adaptive Spacing:** Local spaced repetition engine running on SQLite/Room.
- **Protected Parent Area:** Simple parent gate to view activity, record optional custom family pronunciations, or mark real-world transfer.
- **Real-World Transfer:** One quiet, context-appropriate prompt for parents after a meaningful session.
- **Zero Commercial Distractions:** No backend, no accounts, no analytics, no ads, no subscriptions, no IAP, no cloud speech, no remote content.

---

## Child Experience & Design Rules

The learning itself should be interesting. The interface must **never** use overstimulation to retain attention.

### Prohibited Elements (Never Introduce)
- Streaks, XP, points, levels, or scores
- Lives, timers, countdowns, or races
- Badges, medals, trophies, or leaderboards
- Confetti, burst animations, or celebration overlays
- Neon/bright rainbow color palettes
- Emotional mascot manipulation or "come back" pressure
- Push-notification engagement hooks
- Pronunciation correctness grading or red/green error states

### Normal Child Behaviors (Never Treat as Failures)
- Remaining silent
- Replaying the same word repeatedly
- Stopping early or wandering away
- Preferring one language over another
- Mispronouncing a word

---

## Design Philosophy

**Quiet interface. Vivid reality. Meaningful motion. Calm curiosity.**

- The concept illustration should normally be the most colorful element on screen.
- Palette and design tokens are locked in [`docs/02-design/design-system.md`](docs/02-design/design-system.md) and [`docs/02-design/color-palette.md`](docs/02-design/color-palette.md):
  - Child background: `#F2EEE7` (warm stone)
  - Primary content: `#26312E` (deep green-charcoal)
  - Primary action: `#4F746B` (muted eucalyptus)
- Small animations or haptics are allowed **only** when they clarify cause and effect, illustrate concept meaning, or clarify a temporary state. They must never exist to inflate screen time.

---

## Technical Direction

The accepted architecture direction is defined in [`docs/decisions/ADR-0001-technology-stack.md`](docs/decisions/ADR-0001-technology-stack.md):

- **Target Stack:** Kotlin Multiplatform (KMP) + Compose Multiplatform (CMP) + Room / SQLite + DataStore + Kotlin Coroutines + platform audio/microphone adapters.
- **Android First:** Primary delivery on Android tablet (`minSdk 26+`, `targetSdk 34+`). Minimal iOS shell for multiplatform boundary validation.
- **Fallback Stack:** Native Android + Jetpack Compose if the mandatory technical spike demonstrates that KMP materially delays Prototype 0.1.

### Architecture Principles

$$\text{Presentation} \longrightarrow \text{Domain} \longrightarrow \text{Data} \longrightarrow \text{Platform}$$

- Dependencies point inward toward the domain layer.
- UI components must never directly call DAOs.
- Learning evidence and exposure logs must be idempotent.
- Audio and microphone callbacks must carry stable session/interaction IDs to reject stale callbacks.
- Child microphone PCM must remain memory-only (never written to flash/disk).
- Family voice recording is an adult-controlled feature writing locally to app-private storage.
- No network required: omit `android.permission.INTERNET`.

---

## Operating Rules for Coding

When making changes:
- Prefer the smallest implementation that proves Prototype 0.1.
- Avoid speculative abstractions, premature multi-profile support, backend scaffolding, remote configs, and third-party analytics frameworks.
- For each meaningful implementation phase:
  1. State what you are about to change.
  2. Explain why in plain English.
  3. Implement it.
  4. Add/update tests.
  5. Run validation.
  6. Summarize what changed.
  7. List any decisions I need to make.
- Never mark something complete merely because it compiles.
- Use the QA and acceptance documents in [`docs/05-quality/`](docs/05-quality/) to determine whether functionality is complete.

The goal is: **Create a trustworthy Prototype 0.1 that can be used in a seven-day family test.**