# Miozira — Architecture Decision Records

**Document:** `decisions/README.md`  
**Version:** 0.1  
**Status:** Active  
**Product:** Miozira  
**Last updated:** 2026-08-25

---

## 1. Purpose

This directory contains Architecture Decision Records (ADRs) for Miozira.

An ADR records a technical/product-engineering decision that:

- has meaningful alternatives;
- affects future implementation;
- would be expensive/confusing to rediscover later.

ADRs are intentionally short.

---

# 2. Current ADRs

## ADR-0001 — Technology Stack

File:

```text
ADR-0001-technology-stack.md
```

Status:

> Accepted

Decision:

```text
Kotlin Multiplatform
+ Compose Multiplatform
+ Room/SQLite
+ DataStore
+ Coroutines
+ platform-specific audio/microphone adapters
```

Delivery priority:

> Android first, iPadOS later.

Fallback:

> native Android Compose if KMP materially blocks the family-test milestone.

---

# 3. When to Create an ADR

Create an ADR when deciding something like:

- changing database technology;
- changing KMP/CMP strategy;
- adding a backend;
- adding cloud speech;
- introducing remote analytics;
- changing persistence architecture;
- changing child audio/mic architecture;
- adding account/profile system;
- changing content-delivery model;
- adding encryption layer;
- choosing a substantially different cross-platform strategy.

---

# 4. When Not to Create an ADR

Do not create an ADR for:

- minor UI spacing;
- copy edits;
- bug fixes;
- routine dependency patch;
- individual content wording;
- implementation details already covered by a spec.

---

# 5. ADR Naming

Format:

```text
ADR-0001-technology-stack.md
ADR-0002-<decision-name>.md
ADR-0003-<decision-name>.md
```

Use sequential numbering.

Never reuse a number.

---

# 6. ADR Status Values

Use:

```text
Proposed
Accepted
Superseded
Rejected
Deprecated
```

---

# 7. ADR Template

```markdown
# ADR-XXXX — Title

**Status:** Proposed
**Date:** YYYY-MM-DD

## Context

What problem/decision are we facing?

## Decision Drivers

- ...
- ...

## Options Considered

### Option A
...

### Option B
...

## Decision

We will ...

## Consequences

### Positive
- ...

### Negative
- ...

## Validation / Exit Criteria

- ...

## Supersedes

None.

## Superseded By

None.
```

---

# 8. Decision Rule

An ADR should explain:

> **why**

not just:

> **what**

Someone reading it months later should understand why the chosen direction was rational at the time.

---

# 9. Superseding an ADR

Do not rewrite history.

If a major decision changes:

1. create a new ADR;
2. mark old ADR `Superseded`;
3. link both.

Example:

```text
ADR-0001 — KMP stack
superseded by
ADR-0005 — Native Android-only strategy
```

if that ever happens.

---

# 10. Prototype 0.1 ADR Discipline

Do not create dozens of speculative ADRs.

Prototype 0.1 needs ADRs only for decisions that materially affect implementation.

Current expected baseline:

```text
ADR-0001 technology stack
```

Additional ADRs should appear only when a genuine choice arises during implementation.

---

# 11. Candidate Future ADRs

Possible—not required now:

```text
ADR-0002 audio playback backend
ADR-0003 child attempt detection algorithm/library
ADR-0004 Android backup policy
ADR-0005 public content update strategy
```

Create only when alternatives are real and a decision is being made.

---

# 12. Relationship to Specs

ADRs answer:

> “Why did we choose this architecture?”

Specs answer:

> “How should the chosen architecture behave?”

Both are needed, but should not duplicate each other unnecessarily.

---

# 13. Governance

During Prototype 0.1:

- founder/product owner approves major decision changes;
- AI tools may draft ADRs;
- AI tools should not silently change accepted architecture;
- implementation that contradicts an accepted ADR should trigger review.

---

# 14. Governing Rule

> **Record decisions that would otherwise be forgotten; do not create paperwork for decisions that do not matter.**
