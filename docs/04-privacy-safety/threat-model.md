# Miozira — Threat Model

**Document:** `threat-model.md`  
**Version:** 0.1  
**Status:** Draft for Prototype 0.1  
**Product:** Miozira  
**Primary platform:** Android tablet  
**Architecture:** Offline-first, no backend, no account  
**Last updated:** 2026-08-25

---

## 1. Purpose

This document identifies plausible security, privacy, integrity, and child-safety threats for Miozira Prototype 0.1.

It defines:

- assets;
- actors;
- trust boundaries;
- attack/failure scenarios;
- likelihood;
- impact;
- mitigations;
- residual risk;
- release blockers.

The goal is not to model every theoretical attack.

The goal is:

> **understand the threats that matter for a small offline preschool language app and mitigate them proportionately.**

---

# 2. Threat-Model Scope

Prototype 0.1 has:

- no backend;
- no account;
- no payment;
- no social features;
- no ads;
- no analytics;
- no cloud speech;
- local Room database;
- local family audio;
- optional child microphone windows.

Therefore the dominant risks are:

```text
privacy mistakes
local data exposure
microphone misuse
dependency overreach
local integrity bugs
physical-device access
backup leakage
tampered/untrusted builds
```

---

# 3. Method

This threat model uses a lightweight risk framework.

Each threat receives:

## Likelihood

```text
LOW
MEDIUM
HIGH
```

## Impact

```text
LOW
MEDIUM
HIGH
CRITICAL
```

## Overall priority

Based on:

- probability;
- child/family harm;
- privacy consequence;
- learning-data integrity;
- recoverability.

This is not a formal CVSS score.

---

# 4. Security Assets

## A1 — Child learning history

Includes:

- sessions;
- interactions;
- exposure history;
- attempt state;
- adaptive state.

Classification:

> D2

---

## A2 — Parent real-world observations

Includes:

- recognized outside app;
- used outside app.

Classification:

> D2

---

## A3 — Family voice recordings

Persisted adult-created audio.

Classification:

> D3

Highest-sensitivity persistent asset in Prototype 0.1.

---

## A4 — Child microphone PCM

Transient speaking-attempt audio.

Classification:

> sensitive transient voice data

Must not persist.

---

## A5 — Parent settings

Includes:

- microphone enable state;
- language settings;
- setup state.

Classification:

> D1

---

## A6 — Canonical content

Includes:

- target words;
- audio;
- visuals;
- suggestions.

Classification:

> D0

Integrity matters more than privacy.

---

## A7 — Application signing/build integrity

Important because a malicious altered build could violate all other guarantees.

---

## A8 — Learning evidence integrity

Includes:

- correct exposure count;
- no duplicate attempt;
- no stale callback writes.

Incorrect state can undermine product validation even if no privacy breach occurs.

---

# 5. Trust Boundaries

Conceptual boundaries:

```text
Child / Parent
     │
     ▼
Miozira UI
     │
     ▼
Session / Domain Logic
     │
     ▼
Repositories / Services
     │
     ├── Room / SQLite
     ├── DataStore
     ├── Private FileStore
     ├── AudioService
     └── Microphone Services
     │
     ▼
Android OS Sandbox / Permission System
```

External boundaries:

```text
App Store / distribution
OS backup/migration
Third-party dependencies
Physical device owner
Rooted/compromised device
```

---

# 6. Threat Actors

## TACT-01 — Accidental child interaction

Not malicious.

May:

- enter parent area;
- trigger destructive controls;
- start/stop recording.

---

## TACT-02 — Adult with physical device access

May intentionally:

- inspect app;
- hear family recordings;
- reset data.

Prototype 0.1 does not strongly defend against this.

---

## TACT-03 — Malicious third-party app

Ordinary Android app without root.

Primary protection:

> Android sandbox.

---

## TACT-04 — Rooted/compromised device attacker

Can potentially bypass app-private storage.

Out of strong defensive scope.

---

## TACT-05 — Malicious or compromised dependency

Could:

- add network access;
- collect identifiers;
- leak data;
- add permissions.

This is a meaningful supply-chain threat.

---

## TACT-06 — Tampered/unofficial Miozira build

Could claim Miozira behavior while adding:

- tracking;
- network;
- recording.

Distribution integrity matters.

---

## TACT-07 — Implementation bug

Likely threat source.

Examples:

- child PCM saved accidentally;
- stale callback double-writes;
- family file placed publicly;
- backup rules wrong.

---

# 7. Threat TM-001 — Raw Child Audio Accidentally Persisted

**Asset:** A4 child microphone PCM  
**Likelihood:** MEDIUM  
**Impact:** HIGH  
**Priority:** HIGH

### Scenario

Developer reuses a recording helper that writes microphone input to:

- temp file;
- cache;
- debug output.

This violates the product's strongest privacy promise.

### Mitigations

- child detector uses `AudioRecord`;
- no file path in detector API;
- no expected recording output;
- memory-only buffers;
- tests inspect app files after child speaking;
- code review for microphone changes;
- privacy invariant in `microphone.md`.

### Residual risk

LOW if architecture is enforced.

### Release blocker?

> YES

---

# 8. Threat TM-002 — Child Audio Uploaded by Dependency/SDK

**Assets:** A4, privacy posture  
**Likelihood:** LOW  
**Impact:** CRITICAL  
**Priority:** HIGH

### Scenario

A cloud speech or analytics SDK is introduced and receives child audio or metadata.

### Mitigations

- no cloud speech SDK;
- no analytics SDK;
- no backend;
- aim to omit INTERNET permission;
- dependency review;
- merged-manifest review;
- optional runtime network inspection.

### Residual risk

VERY LOW if INTERNET permission absent.

### Release blocker?

> YES

---

# 9. Threat TM-003 — Family Recording Saved in Public Storage

**Asset:** A3 family voice  
**Likelihood:** MEDIUM  
**Impact:** HIGH  
**Priority:** HIGH

### Scenario

Parent recording library saves to:

- Music;
- Downloads;
- MediaStore;
- shared external storage.

Other apps or users can access it.

### Mitigations

- FileStore owns app-private paths;
- no broad storage permission;
- logical file IDs;
- media-gallery test;
- local-storage invariant.

### Residual risk

LOW.

### Release blocker?

> YES

---

# 10. Threat TM-004 — Family Recording Included in Cloud Backup

**Asset:** A3 family voice  
**Likelihood:** MEDIUM  
**Impact:** MEDIUM/HIGH  
**Priority:** HIGH

### Scenario

Android backup default copies private family audio to device/cloud backup.

Public copy says “local,” creating a mismatch.

### Mitigations

- explicit backup rules;
- exclude family recording directory;
- verify on target Android behavior;
- avoid overclaiming privacy wording.

### Residual risk

LOW/MEDIUM depending on OS behavior.

### Release blocker?

> YES before public launch; SHOULD be resolved before family test.

---

# 11. Threat TM-005 — Unexpected INTERNET Permission

**Assets:** privacy posture, supply-chain trust  
**Likelihood:** MEDIUM  
**Impact:** MEDIUM/HIGH  
**Priority:** HIGH

### Scenario

A library manifest merges in:

```text
android.permission.INTERNET
```

even though Miozira does not require network.

### Mitigations

- final merged-manifest inspection;
- CI permission allowlist;
- dependency review;
- remove unnecessary library.

### Residual risk

LOW.

### Release blocker?

> YES if unexplained.

---

# 12. Threat TM-006 — Malicious/Tracking Dependency

**Assets:** A1–A5  
**Likelihood:** LOW/MEDIUM  
**Impact:** HIGH  
**Priority:** HIGH

### Scenario

Third-party library performs:

- analytics;
- identifier collection;
- unexpected network traffic.

### Mitigations

- dependency minimization;
- prefer official libraries;
- pin versions;
- license/security review;
- inspect transitive dependencies;
- runtime network sanity testing where feasible.

### Residual risk

LOW/MEDIUM.

### Release blocker?

> YES if behavior conflicts with privacy spec.

---

# 13. Threat TM-007 — Stale Audio Callback Commits Wrong Exposure

**Asset:** A8 learning integrity  
**Likelihood:** MEDIUM  
**Impact:** MEDIUM  
**Priority:** HIGH

### Scenario

Interaction A ends.

Interaction B starts.

Late callback from A commits exposure to B or advances UI.

### Mitigations

Callbacks include:

```text
sessionId
interactionId
playbackHandle
```

Controller verifies:

- active IDs;
- expected state.

Repository exposure write is idempotent.

### Residual risk

LOW with automated tests.

### Release blocker?

> YES for family test integrity.

---

# 14. Threat TM-008 — Stale Microphone Callback Writes Wrong Attempt

**Asset:** A8  
**Likelihood:** MEDIUM  
**Impact:** MEDIUM  
**Priority:** HIGH

### Scenario

Late mic result from old interaction overwrites current interaction state.

### Mitigations

- `SpeechAttemptHandle`;
- active interaction validation;
- first terminal state wins;
- stale callbacks ignored.

### Residual risk

LOW.

### Release blocker?

> YES.

---

# 15. Threat TM-009 — Duplicate Exposure from Rotation/Recreation

**Asset:** A8  
**Likelihood:** MEDIUM  
**Impact:** MEDIUM  
**Priority:** HIGH

### Scenario

Screen rotation recreates UI and restarts audio/commit logic.

### Mitigations

- stable interaction ID;
- exposure commit idempotency;
- state survives UI recreation;
- orientation stress tests.

### Residual risk

LOW.

### Release blocker?

> YES.

---

# 16. Threat TM-010 — Parent Gate Accidentally Accessible to Child

**Assets:** A3, A5, destructive controls  
**Likelihood:** MEDIUM  
**Impact:** LOW/MEDIUM  
**Priority:** MEDIUM

### Scenario

Child discovers parent gate and:

- enters settings;
- starts recording;
- resets data.

### Mitigations

- hidden/less obvious gesture;
- adult confirmation;
- destructive-action confirmation;
- family-test observation.

### Residual risk

MEDIUM.

This is accepted because parent gate is not strong auth.

### Release blocker?

> Only if trivial accidental activation occurs.

---

# 17. Threat TM-011 — Another Adult Accesses Parent Area

**Assets:** A1–A3  
**Likelihood:** MEDIUM  
**Impact:** LOW/MEDIUM  
**Priority:** LOW/MEDIUM

### Scenario

Someone with unlocked tablet opens Miozira parent area.

### Mitigations

Prototype 0.1:

- no sensitive text notes;
- no account identity;
- no strong auth promised.

Potential future:

- optional parent PIN.

### Residual risk

MEDIUM and accepted.

### Release blocker?

> NO for prototype.

---

# 18. Threat TM-012 — Rooted Device Reads Private Data

**Assets:** A1–A3  
**Likelihood:** LOW  
**Impact:** HIGH  
**Priority:** LOW/MEDIUM

### Scenario

Rooted/forensic attacker bypasses sandbox.

### Mitigations

- no cloud/account secrets;
- data minimization;
- optional future encryption if threat model changes.

### Residual risk

HIGH against fully compromised OS.

Accepted as out of scope.

### Release blocker?

> NO.

---

# 19. Threat TM-013 — Database Corruption Causes Silent Bad Learning State

**Assets:** A1, A8  
**Likelihood:** LOW/MEDIUM  
**Impact:** MEDIUM  
**Priority:** MEDIUM

### Scenario

SQLite corruption/projection inconsistency causes:

- incorrect due dates;
- missing history;
- invalid session state.

### Mitigations

- Room constraints;
- transactions;
- derived-state rebuild;
- recovery service;
- no silent destructive reset.

### Residual risk

LOW/MEDIUM.

### Release blocker?

> YES if reproducible.

---

# 20. Threat TM-014 — Family File/DB Metadata Mismatch

**Asset:** A3  
**Likelihood:** MEDIUM  
**Impact:** LOW/MEDIUM  
**Priority:** MEDIUM

### Scenario

DB says recording exists but file is missing, or orphan file remains after DB change.

### Mitigations

- safe replace sequence;
- logical file IDs;
- startup orphan cleanup;
- canonical audio fallback.

### Residual risk

LOW.

### Release blocker?

> NO if graceful fallback works.

---

# 21. Threat TM-015 — Temporary Family File Survives Indefinitely

**Asset:** A3  
**Likelihood:** MEDIUM  
**Impact:** MEDIUM  
**Priority:** MEDIUM

### Scenario

App crashes during parent recording and leaves temp voice file indefinitely.

### Mitigations

- private temp directory;
- startup cleanup;
- age-based cleanup;
- temp excluded from backup.

### Residual risk

LOW.

---

# 22. Threat TM-016 — Microphone Continues in Background

**Asset:** A4 / child safety  
**Likelihood:** LOW/MEDIUM  
**Impact:** HIGH  
**Priority:** HIGH

### Scenario

Lifecycle bug leaves `AudioRecord` active after:

- Home gesture;
- screen lock;
- parent gate.

### Mitigations

- `stopAll()` on lifecycle events;
- single mic owner;
- background tests;
- platform indicator observation.

### Residual risk

LOW if tested.

### Release blocker?

> YES.

---

# 23. Threat TM-017 — Parent Recording Continues After Screen Lock

**Asset:** A3  
**Likelihood:** LOW/MEDIUM  
**Impact:** HIGH  
**Priority:** HIGH

### Scenario

Adult recording remains active unintentionally in background.

### Mitigations

- stop/cancel on background/lock;
- short max duration;
- lifecycle tests.

### Residual risk

LOW.

### Release blocker?

> YES.

---

# 24. Threat TM-018 — Child Hears Startling/Loud Audio

**Asset:** child safety  
**Likelihood:** MEDIUM  
**Impact:** MEDIUM  
**Priority:** MEDIUM/HIGH

### Scenario

One asset is normalized far louder than others.

### Mitigations

- audio QC;
- loudness consistency;
- no forced system volume;
- manual listening review.

### Residual risk

LOW.

---

# 25. Threat TM-019 — Inappropriate/Incorrect Canonical Content

**Assets:** A6, child safety, learning validity  
**Likelihood:** LOW/MEDIUM  
**Impact:** MEDIUM  
**Priority:** MEDIUM/HIGH

### Scenario

Wrong Tamil lexical form or inappropriate visual/audio ships.

### Mitigations

- native/family review;
- content manifest validation;
- curated content only;
- content freeze before family test.

### Residual risk

LOW/MEDIUM.

---

# 26. Threat TM-020 — Silent Canonical Audio Failure Counted as Exposure

**Asset:** A8  
**Likelihood:** MEDIUM  
**Impact:** MEDIUM  
**Priority:** HIGH

### Scenario

Playback call starts but actual word does not meaningfully play.

Exposure is incorrectly committed.

### Mitigations

- exposure-threshold callback;
- completion/failure semantics;
- idempotent repository;
- asset validation.

### Residual risk

LOW.

### Release blocker?

> YES.

---

# 27. Threat TM-021 — Parent Observation Misinterpreted as Objective Mastery

**Assets:** child safety, learning model  
**Likelihood:** MEDIUM  
**Impact:** LOW/MEDIUM  
**Priority:** MEDIUM

### Scenario

App treats one parent tap “used outside” as definitive mastery label.

### Mitigations

- observations are evidence only;
- no mastery UI;
- adaptive shortcut remains provisional;
- wording neutral.

### Residual risk

LOW.

---

# 28. Threat TM-022 — Manipulative Engagement Mechanics Added Later

**Asset:** child safety / product integrity  
**Likelihood:** MEDIUM over product evolution  
**Impact:** HIGH  
**Priority:** HIGH

### Scenario

Future growth pressure adds:

- streaks;
- badges;
- push reminders;
- engagement goals.

### Mitigations

- explicit anti-requirements;
- child-safety invariants;
- product vision;
- review against specs before feature approval.

### Residual risk

MEDIUM organizationally.

### Release blocker?

> YES if introduced into Prototype 0.1.

---

# 29. Threat TM-023 — Debug Logging Leaks Sensitive Information

**Assets:** A3/A4  
**Likelihood:** MEDIUM  
**Impact:** MEDIUM/HIGH  
**Priority:** HIGH

### Scenario

Developer logs:

- audio samples;
- family file path;
- detailed mic traces.

### Mitigations

- semantic logging only;
- no PCM logs;
- release logging review;
- diagnostics development-only.

### Residual risk

LOW.

---

# 30. Threat TM-024 — Debug Build Accidentally Distributed

**Assets:** app integrity/privacy  
**Likelihood:** LOW/MEDIUM  
**Impact:** MEDIUM  
**Priority:** MEDIUM

### Scenario

Family/public build contains:

- debug menus;
- verbose logs;
- debug permissions.

### Mitigations

- release build checklist;
- distinguish build variants;
- final manifest inspection;
- signing verification.

### Residual risk

LOW.

---

# 31. Threat TM-025 — Signing Key Compromise

**Asset:** A7 distribution trust  
**Likelihood:** LOW  
**Impact:** CRITICAL  
**Priority:** HIGH

### Scenario

Attacker obtains signing key and ships malicious update.

### Mitigations

- key outside repo;
- secure password manager/keychain;
- Play App Signing if used;
- limited access.

### Residual risk

LOW if operationally managed.

---

# 32. Threat TM-026 — Fake/Tampered Miozira Build

**Assets:** all privacy/security promises  
**Likelihood:** LOW  
**Impact:** HIGH  
**Priority:** MEDIUM/HIGH

### Scenario

Unofficial APK claims to be Miozira but adds tracking.

### Mitigations

- distribute through trusted channels;
- signed builds;
- document official source;
- future website/store verification.

### Residual risk

LOW/MEDIUM.

---

# 33. Threat TM-027 — Insecure Dependency Update

**Assets:** build integrity  
**Likelihood:** MEDIUM  
**Impact:** HIGH  
**Priority:** HIGH

### Scenario

AI/developer updates dependency to compromised/problematic version without review.

### Mitigations

- pinned versions;
- changelog/release review;
- dependency alerts;
- test release manifest/network.

### Residual risk

LOW/MEDIUM.

---

# 34. Threat TM-028 — AI Coding Tool Suggests Incompatible Security Pattern

**Assets:** architecture/privacy posture  
**Likelihood:** HIGH during development  
**Impact:** MEDIUM/HIGH  
**Priority:** HIGH

### Scenario

AI tool introduces:

- Firebase;
- analytics;
- cloud speech;
- broad storage permission;
- secret in code.

### Mitigations

- canonical specs in repository;
- code review;
- architecture/permission checks;
- PR checklist.

### Residual risk

LOW/MEDIUM with disciplined review.

---

# 35. Threat TM-029 — Incorrect Reset Leaves Family Audio Behind

**Asset:** A3  
**Likelihood:** MEDIUM  
**Impact:** HIGH  
**Priority:** HIGH

### Scenario

DB reset succeeds but family file deletion fails.

UI says reset complete.

### Mitigations

- ResetService coordinates DB/files/settings;
- success only after all required deletion succeeds;
- idempotent retry;
- recovery cleanup.

### Residual risk

LOW.

### Release blocker?

> YES.

---

# 36. Threat TM-030 — Reset Accidentally Deletes Canonical Assets

**Asset:** A6 / app availability  
**Likelihood:** LOW  
**Impact:** MEDIUM  
**Priority:** MEDIUM

### Scenario

File cleanup recursively deletes bundled/runtime required assets.

### Mitigations

- canonical assets read-only in app bundle;
- separate FileStore namespace;
- reset tests.

### Residual risk

VERY LOW.

---

# 37. Threat TM-031 — OS Backup Restores Stale Permission Preference

**Assets:** A5 / microphone behavior  
**Likelihood:** MEDIUM  
**Impact:** MEDIUM  
**Priority:** MEDIUM

### Scenario

DataStore says mic enabled after restore, but Android permission not granted.

### Mitigations

- OS permission queried at use time;
- product preference never treated as authority.

### Residual risk

LOW.

---

# 38. Threat TM-032 — Time Manipulation Distorts Scheduling

**Asset:** A8  
**Likelihood:** LOW/MEDIUM  
**Impact:** LOW  
**Priority:** LOW

### Scenario

Device clock changes significantly.

### Mitigations

- use UTC epoch timestamps;
- conservative recalculation;
- no anti-cheat needed.

### Residual risk

Accepted.

---

# 39. Threat TM-033 — Excessive Parent Dashboard Becomes Surveillance-Like

**Asset:** child safety/privacy  
**Likelihood:** MEDIUM over future growth  
**Impact:** MEDIUM  
**Priority:** MEDIUM

### Scenario

Product adds granular:

- timestamps;
- failure counts;
- performance rankings.

### Mitigations

- current parent experience spec;
- no performance score;
- recent summaries only;
- child-safety review for new metrics.

### Residual risk

MEDIUM organizationally.

---

# 40. Threat TM-034 — Unintended Network Traffic from Platform/Library

**Assets:** privacy posture  
**Likelihood:** LOW/MEDIUM  
**Impact:** MEDIUM  
**Priority:** MEDIUM/HIGH

### Scenario

A library performs telemetry despite no explicit app networking.

### Mitigations

- aim for no INTERNET permission;
- dependency review;
- release runtime network inspection where feasible.

### Residual risk

LOW if permission absent.

---

# 41. Threat TM-035 — Family Audio Exposed Through Share/Open Intent

**Asset:** A3  
**Likelihood:** LOW  
**Impact:** MEDIUM/HIGH  
**Priority:** MEDIUM

### Scenario

FileProvider/share feature accidentally exposes family audio.

### Mitigations

- no export/share feature;
- no public URI generation;
- private FileStore.

### Residual risk

VERY LOW.

---

# 42. Threat TM-036 — Parent Recording Replaces Good File Before New Save Completes

**Asset:** A3 availability/integrity  
**Likelihood:** MEDIUM  
**Impact:** LOW/MEDIUM  
**Priority:** MEDIUM

### Scenario

Old recording deleted first.

New recording save fails.

Parent loses both.

### Mitigations

Safe sequence:

```text
new temp
→ preview
→ promote
→ metadata update
→ delete old
```

### Residual risk

LOW.

---

# 43. Threat TM-037 — Child Attempt Detector Overinterprets Parent Voice

**Asset:** learning integrity  
**Likelihood:** HIGH  
**Impact:** LOW  
**Priority:** LOW/MEDIUM

### Scenario

Parent speaks during child window and detector logs ATTEMPT_DETECTED.

### Mitigations

- weak evidence only;
- no speaker identity claim;
- no pronunciation/correctness conclusion;
- family-test guidance.

### Residual risk

Accepted.

---

# 44. Threat TM-038 — Detector False Negative Discourages Child

**Asset:** child safety  
**Likelihood:** MEDIUM  
**Impact:** LOW/MEDIUM  
**Priority:** MEDIUM

### Scenario

Child speaks softly but app behaves as though no attempt occurred.

### Mitigations

- no negative UI;
- no forced retry;
- neutral response;
- no-attempt weak evidence only.

### Residual risk

LOW.

---

# 45. Threat TM-039 — Inappropriate External Link Reaches Child

**Asset:** child safety  
**Likelihood:** LOW  
**Impact:** HIGH  
**Priority:** MEDIUM

### Scenario

Child UI contains store/web link.

### Mitigations

- no child external links;
- parent-only links if any;
- UI review.

### Residual risk

VERY LOW.

---

# 46. Threat TM-040 — Device Media Volume Too High

**Asset:** child safety  
**Likelihood:** MEDIUM  
**Impact:** LOW/MEDIUM  
**Priority:** MEDIUM

### Scenario

System volume already high.

Word playback is uncomfortable.

### Mitigations

- consistent mastering;
- do not force volume;
- parent responsible for device volume;
- optional future parent hint.

### Residual risk

MEDIUM.

Accepted as normal device-use risk.

---

# 47. Risk Register Summary

| Threat | Likelihood | Impact | Priority |
|---|---|---|---|
| Raw child PCM persisted | Medium | High | High |
| Child audio uploaded | Low | Critical | High |
| Family audio public | Medium | High | High |
| Family audio backup leak | Medium | High | High |
| Unexpected INTERNET | Medium | High | High |
| Tracking dependency | Low/Med | High | High |
| Stale audio callback | Medium | Medium | High |
| Stale mic callback | Medium | Medium | High |
| Duplicate rotation exposure | Medium | Medium | High |
| Background mic | Low/Med | High | High |
| Reset leaves family audio | Medium | High | High |
| Signing-key compromise | Low | Critical | High |
| Unsafe dependency update | Medium | High | High |
| AI-generated architecture drift | High | Med/High | High |

---

# 48. Prototype 0.1 Release Blockers

The following must block family-test/public release if unresolved:

1. raw child audio written to disk;
2. child audio/network upload;
3. unexplained tracking/analytics SDK;
4. unexplained dangerous permissions;
5. public/shared family recording storage;
6. microphone active in background;
7. reset claims success while family audio remains;
8. stale callback can corrupt current interaction;
9. duplicate exposure after rotation/process lifecycle;
10. canonical audio failure counted as exposure;
11. build signing/security process not understood.

---

# 49. Accepted Residual Risks

Prototype 0.1 explicitly accepts:

- parent gate is not strong authentication;
- unlocked-device adults may access parent area;
- rooted devices can defeat sandbox;
- parent voice may trigger child attempt detector;
- device clock changes may influence scheduling;
- very high system volume remains a device-level concern;
- no protection against sophisticated forensic extraction.

These are proportionate to the prototype.

---

# 50. Risks Deferred to Public Launch

Before broad public distribution, revisit:

- jurisdiction-specific child privacy law;
- Play Families/Data Safety;
- backup behavior on wider device set;
- signing/release operations;
- dependency vulnerability management;
- potential parent PIN;
- official distribution verification;
- iOS-specific threat model.

---

# 51. Verification Plan

Each high-priority threat should map to at least one verification method:

```text
automated test
manual device test
manifest inspection
dependency review
file-system inspection
network inspection
code review
```

---

# 52. Security/Privacy Tests Required

At minimum:

- child mic creates no file;
- child mic stops on background;
- family file remains private;
- family file deleted on reset;
- no unexpected permission;
- no network dependency;
- no duplicate exposure;
- stale callback ignored;
- reset idempotent;
- app works with mic denied.

---

# 53. Threat-Model Invariants

### THREAT-INV-001
The highest-priority privacy risk is unintended child/family voice persistence or transmission.

### THREAT-INV-002
The Android sandbox is the primary local confidentiality boundary.

### THREAT-INV-003
Dependencies are treated as part of the trust boundary.

### THREAT-INV-004
Lifecycle/event bugs are treated as integrity threats, not merely UI bugs.

### THREAT-INV-005
Parent gate is not assumed to provide strong authentication.

### THREAT-INV-006
Rooted/fully compromised devices are outside strong defensive scope.

### THREAT-INV-007
High-priority threats must have explicit verification.

### THREAT-INV-008
Prototype security controls remain proportionate to actual risk.

---

# 54. Open Threat-Model Questions

Before public release, revisit:

1. final family audio backup behavior;
2. whether structured DB backup is enabled;
3. whether official distribution needs integrity documentation;
4. whether parent PIN becomes necessary;
5. whether iPadOS adds new data-sharing surfaces;
6. whether any remote diagnostics are introduced;
7. whether multiple-child profiles change identity/privacy risk.

---

# 55. Relationship to `security.md`

`security.md` defines the security controls.

This document explains which threats those controls mitigate.

---

# 56. Relationship to `privacy.md`

`privacy.md` defines the privacy promises.

This document identifies ways those promises could fail.

---

# 57. Relationship to `data-inventory.md`

The D2/D3 data elements identified there are the main protected assets here.

---

# 58. Relationship to `testing.md`

`testing.md` should translate each release-blocking threat into executable tests.

---

# 59. Relationship to `release.md`

`release.md` must refuse release when a high-priority threat marked as blocker remains open.

---

# 60. Decision Summary

Miozira's real security/privacy risks are not internet-scale attacks.

They are primarily:

```text
accidental voice persistence
backup leakage
dependency overreach
lifecycle integrity bugs
unsafe file placement
tampered/untrusted builds
physical-device access
```

The architecture already removes many harder threat classes by having:

```text
no backend
no account
no payments
no ads
no analytics
no social system
no cloud speech
```

The governing rule is:

> **Protect the small amount of sensitive data Miozira has very well, rather than inventing infrastructure for threats the product does not create.**
