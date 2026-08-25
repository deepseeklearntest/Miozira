# Miozira — Prototype Release Process

**Document:** `release.md`  
**Version:** 0.1  
**Status:** Draft for Prototype 0.1  
**Product:** Miozira  
**Last updated:** 2026-08-25

---

## 1. Purpose

This document defines how a Miozira Prototype 0.1 build becomes an approved family-test release.

This is intentionally lightweight.

The goal is:

> **produce one traceable, tested, frozen build that the family can use for seven days without ambiguity about what changed.**

---

# 2. Release Types

Prototype 0.1 uses:

```text
DEV
FAMILY_TEST_CANDIDATE
FAMILY_TEST_RELEASE
```

No public production release is required yet.

---

# 3. Versioning

Recommended app version:

```text
0.1.0
```

Build number increments for candidate rebuilds.

Example:

```text
0.1.0 (101)
0.1.0 (102)
```

---

# 4. Release Identity

Every family-test release must record:

```text
App version
Build number
Git commit
Content version
Adaptive config version
Microphone config version
Build date
Primary test device
```

---

# 5. Branch / Source Rule

The family-test build should come from a known Git commit.

Do not build from:

- uncommitted local changes;
- unknown IDE state;
- manually patched binary.

---

# 6. Pre-Release Gate

Before creating candidate:

- [ ] code compiles;
- [ ] unit tests pass;
- [ ] content validation passes;
- [ ] lint/static checks pass;
- [ ] database migration/schema checks pass;
- [ ] permission manifest reviewed.

---

# 7. Candidate Build

Create:

```text
FAMILY_TEST_CANDIDATE
```

Install on primary physical tablet.

Run:

- `qa-checklist.md`;
- `device-test-matrix.md`;
- `acceptance-criteria.md`.

---

# 8. Release Blockers

Do not approve release with:

```text
BLOCKER defects > 0
HIGH defects > 0
```

Mandatory blockers include:

- child audio persisted/uploaded;
- background microphone;
- broken reset;
- duplicate exposure;
- missing canonical content;
- mic denial dead-end;
- severe lifecycle crash;
- unexplained tracking/network dependency.

---

# 9. Content Freeze

Before final release:

- all 16 spoken forms approved;
- all canonical audio frozen;
- all visuals frozen;
- real-world suggestions frozen.

No content edits during seven-day test unless required for safety/correctness.

---

# 10. Adaptive Freeze

Record and freeze:

```text
new-item cap
spacing rules
rest rules
same-session spacing
selection priority
```

Do not tune during test.

---

# 11. Microphone Freeze

Record and freeze:

```text
window duration
frame size
noise-floor method
threshold margin
minimum active duration
```

Do not retune during test unless detector is materially broken.

---

# 12. Permission Freeze

Final merged manifest must be archived/recorded for the family-test build.

Expected sensitive permission:

```text
RECORD_AUDIO
```

Any additional dangerous permission requires review.

---

# 13. Privacy/Security Freeze

Confirm:

- no analytics SDK;
- no ad SDK;
- no cloud speech;
- child PCM not persisted;
- family audio private;
- backup rules reviewed;
- no unexplained INTERNET permission.

---

# 14. Primary Device Sign-Off

Primary family tablet must pass:

```text
portrait
landscape
all 16 audio pairs
mic granted
mic denied
airplane mode
reset
background/foreground
screen lock
rotation
```

---

# 15. Release Notes

Keep release notes short.

Template:

```text
Miozira Prototype 0.1
Build:
Date:

Includes:
- 8 concepts
- English + Tamil
- local adaptive repetition
- optional speaking-attempt detection
- parent recognition/use observations
- family voice
- offline operation

Known issues:
- ...

Family-test status:
APPROVED / NOT APPROVED
```

---

# 16. Distribution

Preferred Prototype 0.1 options:

```text
local developer installation
internal testing
private Play testing track
```

Choose the simplest reliable method.

Do not make public store launch a prerequisite for family testing.

---

# 17. Signing

Use a consistent trusted signing setup.

Signing key:

- not committed to repository;
- stored securely;
- associated with release process.

---

# 18. Device Install Verification

After installation:

- [ ] version correct;
- [ ] build correct;
- [ ] app launches;
- [ ] existing test data reset if required;
- [ ] microphone state understood;
- [ ] content version correct.

---

# 19. Clean Test State

Before Day 1:

Recommended:

> reset Miozira local learning data to clean initial state.

Family recordings may be created after reset if they are part of the intended test setup.

Record whether any family recordings exist at Day 1.

---

# 20. Seven-Day Freeze

During test:

Do not change:

- app binary;
- content;
- adaptive config;
- microphone config.

Exception:

> material safety/privacy/blocking defect.

If changed:

- increment build;
- record date;
- treat results before/after separately.

---

# 21. Defect During Family Test

If LOW/MEDIUM:

- record;
- continue if safe and does not invalidate test.

If HIGH/BLOCKER:

- stop using affected build;
- fix;
- rerun release gate;
- restart/segment family test.

---

# 22. Release Rollback

Because there is no backend/database migration complexity across many users, rollback is simple during prototype.

If candidate is bad:

- stop distribution;
- reinstall previous known-good build if compatible;
- reset local data if necessary and explicitly recorded.

---

# 23. Post-Test Release Review

After seven days:

1. close family-test build;
2. preserve final observation summary;
3. classify findings;
4. decide Prototype 0.2 scope;
5. do not silently modify Prototype 0.1 conclusions after changes.

---

# 24. Public Release Is Separate

A future public launch requires additional review of:

- Google Play Families;
- Data Safety;
- privacy policy;
- store listing;
- jurisdictional legal needs;
- wider device matrix;
- support/contact;
- signing/distribution operations.

Prototype 0.1 family release does not imply public-launch readiness.

---

# 25. Release Record Template

```text
Release:
App version:
Build:
Commit:
Date:
Content version:
Adaptive config:
Mic config:
Primary device:
Android version:

Unit tests: PASS/FAIL
QA checklist: PASS/FAIL
Device matrix: PASS/FAIL
Acceptance criteria: PASS/FAIL

Blockers:
High defects:
Accepted medium defects:
Accepted low defects:

Approved for family test: YES / NO
Approved by:
```

---

# 26. Release Invariants

### REL-INV-001
Every family-test build maps to a known Git commit.

### REL-INV-002
Every family-test build has recorded content/adaptive/mic versions.

### REL-INV-003
No blocker/high defect remains open.

### REL-INV-004
Primary physical tablet passes mandatory matrix.

### REL-INV-005
Content/adaptive/mic configuration remains frozen during test.

### REL-INV-006
A changed build creates a new test phase.

### REL-INV-007
Public-store launch is not required for family testing.

### REL-INV-008
Public release requires a separate compliance/release review.

---

# 27. Governing Rule

> **A Miozira release is not “whatever is currently on the tablet”; it is a specific, traceable, tested build whose behavior is intentionally frozen.**
