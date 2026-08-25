# Changelog

All notable changes to the Miozira project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [Unreleased]

### Planned - Technical Spike & Milestone 1
- **Technical Spike:** Minimal vertical slice validating low-latency audio playback, memory-only speech attempt detection, and Room persistence across orientation changes on Android tablet.
- **Core Domain & Repositories:** Kotlin Multiplatform repository abstractions, local Room SQLite tables, and spaced repetition engine.
- **Child Presentation Layer:** Compose Multiplatform interactive card with locked low-stimulation color palette (`#F2EEE7`).
- **Protected Parent Portal:** Discreet hold parent gate, local real-world transfer logger, and custom audio recording interface.

---

## [0.1.0-init] - 2026-08-25

### Added
- **Canonical Documentation Suite:** Organized 39 product, pedagogical, architectural, privacy, design, and testing specifications into canonical `/docs` hierarchy (`00-foundation`, `01-learning`, `02-design`, `03-engineering`, `04-privacy-safety`, `05-quality`, `decisions`).
- **Documentation Index:** Created [`docs/README.md`](docs/README.md) linking all specifications.
- **Architecture Decision Record:** Initialized [`docs/decisions/ADR-0001-technology-stack.md`](docs/decisions/ADR-0001-technology-stack.md) documenting Kotlin Multiplatform + Compose Multiplatform selection with native Android Compose fallback.
- **Open Questions Log:** Created [`docs/OPEN-QUESTIONS.md`](docs/OPEN-QUESTIONS.md) documenting lexical choices (Tamil words for *cup*, *water*), parent gate interaction mechanics, and spike validation parameters.
- **Operating Instructions:** Formulated comprehensive [`AGENTS.md`](AGENTS.md) and [`CLAUDE.md`](CLAUDE.md) developer operating guidelines.
- **Repository Structure:** Scaffolded multiplatform base source directory hierarchy for `:shared`, `:androidApp`, `:iosApp`, and `content/concepts`.
- **Project Documentation:** Created root [`README.md`](README.md) and [`.gitignore`](.gitignore).
