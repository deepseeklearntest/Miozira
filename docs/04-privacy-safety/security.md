# Miozira — Security Specification

**Document:** `security.md`  
**Version:** 0.1  
**Status:** Draft for Prototype 0.1  
**Product:** Miozira  
**Primary platform:** Android tablet  
**Architecture:** Offline-first, local-only learning data  
**Last updated:** 2026-08-25

---

## 1. Purpose

This document defines security requirements for Miozira Prototype 0.1.

It covers:

- threat boundaries;
- app sandboxing;
- least-privilege permissions;
- local database/file protection;
- microphone security;
- family recording protection;
- dependency/supply-chain controls;
- build and release security;
- logging;
- backup exposure;
- parent-gate limitations;
- tampering assumptions;
- recovery.

The goal is:

> **protect the child's and family's local data, prevent accidental overreach, and keep the implementation simple enough to understand and audit.**

---

# 2. Security Scope

Prototype 0.1 is:

- single-device;
- offline-first;
- no account;
- no backend;
- no cloud sync;
- no payment;
- no social features.

This materially reduces the attack surface.

Security work should focus on the risks that actually exist.

---

# 3. Security Non-Goals

Prototype 0.1 does not need:

- enterprise IAM;
- OAuth;
- API gateways;
- server-side secrets;
- SIEM;
- remote attestation;
- certificate pinning;
- WAF;
- DDoS protection;
- cloud key management;
- multi-tenant authorization.

These do not exist in the architecture.

---

# 4. Security Principles

## SEC-P01 — Least privilege

Request only capabilities the app genuinely needs.

## SEC-P02 — Local containment

User-generated data remains inside the app sandbox.

## SEC-P03 — No secrets in client code

Prototype 0.1 should not contain production API secrets because it has no backend/API integration.

## SEC-P04 — Minimize dependencies

Every dependency expands attack/supply-chain surface.

## SEC-P05 — Fail safely

Corruption or service failure should not create undefined child behavior.

## SEC-P06 — Do not overstate protection

The parent gate is not strong authentication.

---

# 5. Threat Model Summary

The most relevant security risks are:

1. accidental permission overreach;
2. dependency introducing network/tracking behavior;
3. raw child audio being stored accidentally;
4. family recordings placed in public storage;
5. insecure backup configuration;
6. corrupted local database;
7. stale callbacks causing incorrect learning writes;
8. insecure logging;
9. malicious/tampered app build;
10. unauthorized access by someone who already has physical device access.

A fuller analysis belongs in `threat-model.md`.

---

# 6. Trust Boundaries

Conceptual boundaries:

```text
Child / Parent
     ↓
Miozira UI
     ↓
Domain logic
     ↓
Local database + private files
     ↓
Android OS sandbox
```

External components include:

- Android OS;
- device backup system;
- app-store distribution;
- third-party libraries.

There is no application backend trust boundary in Prototype 0.1.

---

# 7. Android App Sandbox

All dynamic Miozira data must reside inside Android app-private storage.

This includes:

- Room database;
- DataStore preferences;
- family recordings;
- temporary recording files.

The app should rely on standard Android sandboxing rather than public shared storage.

---

# 8. Shared Storage Prohibition

Do not store sensitive/user-created Miozira files in:

- Downloads;
- Documents;
- public Music;
- public media directories;
- removable shared storage.

---

# 9. File Permissions

App-created private files should use platform-default private access semantics.

Do not intentionally make family recording files world-readable.

---

# 10. Database Protection

The Room/SQLite database should remain app-private.

It may contain:

- learning history;
- interaction evidence;
- real-world observations.

No other app should have ordinary direct access.

---

# 11. Custom Database Encryption

Prototype 0.1 does not require custom database encryption by default.

Reason:

- no credentials;
- no financial data;
- no cloud tokens;
- minimal local learning data;
- platform sandbox already protects ordinary access.

If later threat analysis identifies a concrete requirement, revisit.

---

# 12. Device-Level Security Assumption

Miozira assumes normal device security.

If an attacker has:

- root access;
- physical unlocked device access;
- forensic access to backups;

the app sandbox may not provide absolute protection.

Prototype 0.1 does not attempt to defend against a fully compromised OS.

---

# 13. Parent Gate Is Not Authentication

The parent gate is designed to prevent casual child entry into adult controls.

It is not intended to protect against:

- another adult;
- a determined older child;
- device owner;
- attacker with physical access.

Do not describe it as:

> secure parental authentication.

---

# 14. Parent Gate Security Goal

The actual goal is:

> reduce accidental child activation of settings, reset, and recordings.

A hidden long-press + adult confirmation is sufficient for Prototype 0.1 if family testing validates it.

---

# 15. Sensitive Parent Actions

Actions needing stronger confirmation:

- reset all local data;
- delete family recording.

The parent gate alone is not the only confirmation for destructive actions.

---

# 16. Reset Protection

Reset should require:

1. entry to parent mode;
2. explicit reset action;
3. clear confirmation.

Avoid single-tap destructive reset.

---

# 17. Family Recording Delete Protection

Individual deletion should be:

- explicit;
- reversible only by re-recording;
- not triggered accidentally by swipe unless well confirmed.

---

# 18. Microphone Security

Child microphone capture must occur only during:

```text
explicit 2–3 second response window
```

or adult family recording.

No continuous/background capture.

---

# 19. Background Microphone Prohibition

When app backgrounds or screen locks:

- stop child attempt detector;
- stop/cancel family recording safely.

---

# 20. Raw Child Audio Prohibition

Raw child attempt PCM must never be:

- persisted;
- cached;
- logged;
- uploaded;
- included in crash reports.

---

# 21. Family Recording Distinction

Family recordings are intentionally persisted.

Therefore they require:

- explicit adult action;
- private storage;
- deletion controls;
- backup review.

---

# 22. Microphone Ownership

Only one microphone owner at a time:

```text
NONE
CHILD_ATTEMPT
PARENT_RECORDING
```

This prevents accidental concurrent capture and ambiguous state.

---

# 23. Least-Privilege Manifest

Expected sensitive Android permission:

```text
RECORD_AUDIO
```

Do not add unnecessary:

- camera;
- location;
- contacts;
- storage;
- notifications;
- internet.

---

# 24. Manifest Review

Before family-test/release builds:

- inspect merged manifest;
- compare permissions against allowlist;
- investigate unexpected additions.

---

# 25. INTERNET Permission

Prototype 0.1 should aim to omit:

```text
android.permission.INTERNET
```

This is valuable security/privacy evidence.

If present due to dependency:

- identify why;
- remove dependency or justify explicitly.

---

# 26. Dependency Policy

Prefer dependencies that are:

1. official Android/Kotlin/Jetpack;
2. widely maintained;
3. small in scope;
4. clear about network/data behavior;
5. actively supported.

---

# 27. Dependency Minimization

Do not add a library for trivial functionality that can be implemented safely with a small amount of code.

This is especially true for:

- microphone;
- file helpers;
- analytics;
- networking.

---

# 28. Dependency Review Checklist

For every new dependency, check:

- maintainership;
- release recency;
- license;
- transitive dependencies;
- known vulnerabilities where practical;
- network behavior;
- added permissions;
- data collection;
- whether it executes native code.

---

# 29. Lock Dependency Versions

Use explicit version pinning through the dependency catalog/build system.

Avoid unbounded floating versions.

---

# 30. Build Reproducibility Direction

The build should be deterministic enough that:

- dependency versions are known;
- build configuration is version-controlled;
- release artifacts correspond to a specific commit.

Perfect bit-for-bit reproducibility is not required for Prototype 0.1.

---

# 31. Version Control

All production code/configuration should live in Git.

Do not make unreproducible manual edits only in IDE settings.

---

# 32. Secrets

Prototype 0.1 should have no runtime secrets.

Do not commit:

- passwords;
- signing private keys;
- personal tokens.

---

# 33. Signing Keys

Android signing credentials must be stored outside the repository.

Use secure OS/keychain/password-manager or platform-recommended handling.

---

# 34. Debug vs Release Builds

Debug builds may contain:

- extra logging;
- diagnostics.

Release/family-test builds should avoid:

- debug menus accessible to child;
- verbose sensitive logs;
- test files;
- unnecessary debug permissions.

---

# 35. Debuggable Flag

Public/release builds must not be distributed as debuggable unless intentionally required for internal testing.

---

# 36. Test Data

Do not include real child voice recordings in source control.

Use:

- synthetic signals;
- adult test recordings where needed and consented;
- generated/fake data.

---

# 37. Asset Integrity

All required canonical assets should be validated before release.

A missing/corrupt canonical audio file is a content integrity failure.

---

# 38. Content Manifest Integrity

Build/test validation should detect:

- duplicate IDs;
- missing pairs;
- missing audio;
- missing visual asset;
- invalid language mapping.

---

# 39. No Runtime Remote Content

Prototype 0.1 does not fetch content remotely.

This removes a major content-tampering boundary.

---

# 40. Stale Callback Security/Integrity

Audio/mic callbacks must be scoped to:

```text
sessionId
interactionId
handle
```

A stale callback cannot mutate a newer interaction.

This is a data-integrity security property.

---

# 41. Exposure Idempotency

Exposure writes must be idempotent.

Duplicate lifecycle callbacks must not inflate learning evidence.

---

# 42. Database Constraints

Use:

- primary keys;
- unique constraints;
- foreign keys;
- transactions

to protect local integrity.

Do not rely solely on UI correctness.

---

# 43. Transaction Safety

Atomic operations include:

- observation + pair-state update;
- interaction completion + derived state;
- structured reset.

---

# 44. Backup Security

OS backup can create copies outside the immediate sandbox.

Therefore backup policy must be explicit.

Recommended:

> exclude family recordings from automatic backup.

Structured learning-data backup decision must be documented.

---

# 45. Backup Restore Integrity

If structured state is restored:

- content version must still resolve;
- system permission must be rechecked;
- learning state may be validated/rebuilt.

Do not trust restored preference as proof microphone permission is granted.

---

# 46. Logs

Production logs should not contain:

- raw audio;
- family recording contents;
- PII;
- full private file paths where unnecessary.

---

# 47. Semantic Logging

Safe examples:

```text
InteractionSelected(pair.apple.ta)
ExposureCommitted(interactionId)
MicUnavailable
FamilyRecordingSaved(pair.apple.ta)
```

---

# 48. Log Retention

Do not implement an indefinite application-level log archive in Prototype 0.1.

---

# 49. Remote Crash Reporting

Not required.

If added later, perform privacy/security review.

---

# 50. SQL Injection

Room uses parameterized queries/generated SQL patterns.

Avoid building raw SQL strings from untrusted UI input.

Prototype 0.1 has little free-text input.

---

# 51. Path Traversal

Higher layers use logical file IDs.

`FileStore` should generate/resolve paths internally.

Do not accept arbitrary user-controlled filesystem paths.

---

# 52. Filename Safety

Family recording filenames use generated IDs.

Do not derive filenames from arbitrary user strings.

---

# 53. Temporary File Security

Temporary family recording files remain private.

Delete after:

- cancel;
- failure;
- startup cleanup.

---

# 54. Corrupt Family Files

If corrupt:

- do not crash;
- fall back to canonical audio;
- parent may replace.

---

# 55. Corrupt Database

Do not immediately erase automatically.

Use recovery path:

1. stop unsafe writes;
2. attempt known safe recovery;
3. expose parent-level reset if necessary.

---

# 56. Fail-Closed vs Fail-Open

For child learning:

- microphone failure → fail open to learning without mic;
- family audio failure → use canonical audio;
- optional content failure → skip/fallback;
- core DB corruption → fail closed to unsafe learning writes.

---

# 57. Data Reset Integrity

Reset should be idempotent.

Repeated reset attempts should not crash or reintroduce deleted data.

---

# 58. Uninstall

App-private data is normally removed by OS on uninstall, subject to backup/restore behavior.

No server-side account data remains because no backend exists.

---

# 59. Physical Access Risk

Anyone with access to an unlocked device may potentially:

- open parent area;
- hear family recordings;
- reset data.

Prototype 0.1 does not provide PIN/password-protected parent mode.

If testing shows this is needed, revisit.

---

# 60. No Biometric Authentication

Do not add biometric auth in Prototype 0.1.

It would be disproportionate to the threat model.

---

# 61. Screenshot Security

No global screenshot blocking is required.

The UI contains minimal sensitive information.

---

# 62. Clipboard Security

Do not place sensitive data on clipboard automatically.

---

# 63. Screen Recording

Prototype 0.1 does not prevent OS-level screen recording.

No highly sensitive on-screen child data is shown.

---

# 64. Accessibility Services

Miozira supports accessibility semantics but does not control what a device-owner-enabled accessibility service can observe.

Do not put unnecessary sensitive information in accessibility labels.

---

# 65. Supply-Chain Risk

A malicious/compromised dependency could undermine:

- privacy;
- permissions;
- network guarantees.

Therefore dependency review is a primary security control.

---

# 66. Static Analysis

Use available Kotlin/Android lint/static checks.

Prototype 0.1 should at minimum run:

- compiler warnings;
- Android Lint;
- dependency/build checks;
- unit tests.

Additional security scanning may be added if lightweight.

---

# 67. Vulnerability Scanning

If GitHub Dependabot or equivalent is used:

- enable dependency alerts where practical;
- evaluate findings based on actual exploitability.

Do not blindly upgrade production dependencies during a frozen family test without testing.

---

# 68. Code Review

AI-generated code must still be reviewed for:

- permission use;
- network calls;
- file paths;
- logging;
- microphone behavior;
- persistence correctness.

---

# 69. AI-Assisted Development Risk

AI coding tools may suggest:

- cloud SDKs;
- analytics;
- broad permissions;
- unnecessary libraries.

Reject suggestions that violate the architecture, even if they simplify sample code.

---

# 70. Release Checklist — Security

Before family-test release:

- [ ] merged manifest reviewed;
- [ ] permission allowlist matches spec;
- [ ] INTERNET permission absent or justified;
- [ ] no analytics/ad SDK;
- [ ] no secrets in repo;
- [ ] release signing secure;
- [ ] debug-only tools removed/hidden;
- [ ] child PCM not written to disk;
- [ ] family audio private;
- [ ] family recordings excluded from backup if configured;
- [ ] dependency list reviewed;
- [ ] local reset works;
- [ ] stale callback tests pass;
- [ ] database constraints enabled.

---

# 71. Device Security Test

On target device verify:

1. family recording not visible in media apps;
2. no broad storage permission;
3. no network requirement;
4. child mic stops in background;
5. parent recording stops/cancels on background;
6. app restart preserves only intended state;
7. reset removes user data.

---

# 72. Network Security Test

Run normal session while:

```text
airplane mode = on
```

Expected:

- identical learning behavior.

If tooling allows, inspect traffic:

> expected application network traffic = none.

---

# 73. Tampered Clock

Changing device time may affect scheduling.

This is not considered a security attack because there is:

- no score;
- no competitive reward;
- no entitlement.

Handle conservatively rather than implementing anti-cheat.

---

# 74. Tampered Local Database

A rooted/advanced user could edit app storage.

Prototype 0.1 does not defend against deliberate local database manipulation.

If state becomes invalid:

- validation/recovery should prevent crashes where possible.

---

# 75. No Licensing DRM

Prototype 0.1 does not need DRM.

Canonical content is bundled.

Rights protection should rely on licensing/legal controls, not intrusive runtime DRM.

---

# 76. No Remote Kill Switch

No remote config/backend exists.

A bad release is corrected through application update.

---

# 77. Security Incident Response — Prototype

If a security/privacy defect is discovered:

1. stop distribution if material;
2. reproduce and scope issue;
3. fix root cause;
4. verify no contradictory privacy wording remains;
5. release corrected build;
6. document incident in project records if significant.

---

# 78. Child Safety Relationship

Security protects child safety by preventing:

- hidden recording;
- unnecessary tracking;
- accidental public voice files;
- unauthorized destructive child actions.

---

# 79. Security Invariants

### SEC-INV-001
All dynamic user data is stored in app-private storage.

### SEC-INV-002
Raw child speaking-attempt audio is never persisted.

### SEC-INV-003
No background microphone capture occurs.

### SEC-INV-004
Parent family recording is explicit and private.

### SEC-INV-005
Prototype 0.1 uses least-privilege permissions.

### SEC-INV-006
Unexpected manifest permissions require review.

### SEC-INV-007
No production secrets are stored in source control.

### SEC-INV-008
No remote analytics/ad SDK exists.

### SEC-INV-009
Higher layers cannot supply arbitrary filesystem paths.

### SEC-INV-010
Exposure writes are idempotent.

### SEC-INV-011
Stale async callbacks cannot mutate newer interactions.

### SEC-INV-012
Core DB corruption does not silently continue unsafe writes.

### SEC-INV-013
The parent gate is not represented as strong authentication.

### SEC-INV-014
Dependencies are part of the security boundary.

### SEC-INV-015
Family recording backup behavior is explicitly reviewed.

---

# 80. Open Security Decisions

Before public launch, confirm:

1. final Android backup policy;
2. whether parent gate needs stronger protection than family test;
3. whether remote crash reporting will ever be added;
4. exact dependency vulnerability scanning workflow;
5. signing-key operational process;
6. whether app integrity/Play Integrity has a future justification;
7. final iOS sandbox/backup mapping.

---

# 81. Relationship to `privacy.md`

`privacy.md` defines what Miozira promises.

This document defines controls that help enforce those promises.

---

# 82. Relationship to `permissions.md`

`permissions.md` defines the least-privilege manifest/runtime strategy.

---

# 83. Relationship to `local-storage.md`

`local-storage.md` defines where files live.

This document defines the security expectations around those locations.

---

# 84. Relationship to `database.md`

`database.md` defines integrity constraints and transactions.

---

# 85. Relationship to `threat-model.md`

`threat-model.md` should expand this document into structured:

- assets;
- actors;
- threats;
- mitigations;
- residual risk.

---

# 86. Relationship to `release.md`

`release.md` should include the security checklist as a release gate.

---

# 87. Decision Summary

Miozira Prototype 0.1 uses a proportionate security model:

```text
Android app sandbox
least-privilege permissions
no backend
no production secrets
private Room database
private family audio files
no raw child audio persistence
no background recording
minimal dependencies
manifest/dependency review
safe local recovery
```

It explicitly does **not** pretend that:

- the parent gate is strong authentication;
- a rooted/unlocked device can be fully defended;
- enterprise infrastructure is necessary.

The governing rule is:

> **Use the operating system's strong defaults, keep the attack surface small, and never add a capability Miozira cannot justify.**
