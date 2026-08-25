# Miozira — Privacy Specification

**Document:** `privacy.md`  
**Version:** 0.1  
**Status:** Draft for Prototype 0.1  
**Product:** Miozira  
**Audience:** Product, engineering, QA, policy/compliance, parent-facing copy  
**Primary platform:** Android tablet  
**Last updated:** 2026-08-25

---

## 1. Purpose

This document defines Miozira Prototype 0.1 privacy requirements.

It specifies:

- what data Miozira stores;
- what data Miozira does not collect;
- how child microphone input is handled;
- how family recordings are handled;
- whether data leaves the app;
- account and identity policy;
- analytics policy;
- backup considerations;
- retention;
- deletion/reset;
- permission/privacy wording;
- Google Play Families/Data Safety alignment;
- implementation invariants.

This is both:

1. a product/privacy design specification; and
2. an engineering constraint.

Privacy claims must be true because of architecture, not merely because of marketing copy.

---

# 2. Privacy North Star

Miozira Prototype 0.1 should be designed around:

> **The child should be able to learn without creating a cloud profile, behavioral advertising record, or stored voice history.**

---

# 3. Privacy Principles

## PRIV-P01 — Data minimization

Store only data necessary for:

- local learning adaptation;
- parent observations;
- optional family recordings;
- app operation.

---

## PRIV-P02 — Local-first by architecture

Core learning data stays local to the device/app sandbox.

---

## PRIV-P03 — No child account

Prototype 0.1 does not require:

- child name;
- email;
- username;
- password;
- social profile.

---

## PRIV-P04 — No behavioral advertising

No ads.

No advertising SDK.

No advertising identifier use.

---

## PRIV-P05 — No analytics profile

No remote product analytics or child-behavior telemetry.

---

## PRIV-P06 — Child speech is not retained

Ordinary child speaking attempts are processed transiently and discarded.

---

## PRIV-P07 — Parent-created recordings are explicit

Family recordings are created only after adult action and remain local.

---

## PRIV-P08 — Parent can delete local data

A parent can reset local Miozira data.

---

# 4. Data Inventory — Prototype 0.1

Miozira may persist the following categories locally:

```text
1. Canonical content metadata
2. Session records
3. Interaction records
4. Learning/adaptive state
5. Parent real-world observations
6. Small settings/preferences
7. Family recording metadata
8. Family recording files
```

---

# 5. Canonical Content Metadata

Examples:

- concept ID;
- language ID;
- target word;
- canonical audio asset ID;
- content version.

This is app content, not personal child data.

---

# 6. Session Records

May include:

```text
session_id
started_at
ended_at
completion_reason
content_version
adaptive_config_version
```

No child name is required.

---

# 7. Interaction Records

May include:

```text
interaction_id
concept-language pair
selection reason
exposure committed
replay count
speaking-attempt state
timestamps
```

This is local learning evidence.

---

# 8. Learning/Adaptive State

May include:

- `NEW`;
- `EMERGING`;
- `FAMILIAR`;
- `RESTING`;
- due time;
- rest-until time;
- exposure counts;
- local observation counts.

This data is used only to choose future local content.

---

# 9. Real-World Parent Observations

Parent may record:

```text
RECOGNIZED_OUTSIDE_APP
USED_OUTSIDE_APP
```

associated with a concept-language pair.

Prototype 0.1 should not require:

- free-text notes;
- location;
- photo;
- date details beyond local timestamp.

---

# 10. Settings

May include:

```text
setup_complete
starting_language
speech_attempt_detection_enabled
```

No personal identity is needed.

---

# 11. Family Recording Metadata

May include:

```text
recording_id
pair_id
logical_file_id
duration
format
created_at
updated_at
```

No identity field is necessary.

---

# 12. Family Recording Files

Optional adult-created audio containing a family member saying a target word.

This is potentially identifiable voice data.

Therefore it requires stronger handling than ordinary interaction metadata.

---

# 13. Data That Must Not Exist

Prototype 0.1 must not create or retain:

```text
child account
email
phone number
address
GPS/location history
contacts
camera photos
face images
advertising ID
device fingerprint
cloud user ID
social graph
behavioral ad profile
child speech transcript
child pronunciation score
child raw speech files
voiceprint/biometric profile
remote analytics profile
```

---

# 14. Child Microphone Data Flow

Required flow:

```text
short microphone window
→ transient PCM in memory
→ local speech-like activity heuristic
→ ATTEMPT / NO_ATTEMPT / UNAVAILABLE result
→ raw PCM discarded
```

---

# 15. Child Audio Must Not Be Saved

Do not save ordinary child attempt audio to:

- database;
- temp file;
- cache file;
- log;
- crash report;
- analytics system.

---

# 16. Child Audio Must Not Be Uploaded

No child attempt audio may be sent to:

- Miozira server;
- cloud ASR;
- third-party speech service;
- analytics provider;
- advertising provider.

Prototype 0.1 has no such backend.

---

# 17. No Child Speech Transcription

Miozira does not convert child speech into text.

No:

```text
transcript
recognized word
phoneme output
```

---

# 18. No Pronunciation Grading

Miozira does not compute:

- pronunciation score;
- accent score;
- fluency score;
- phoneme accuracy;
- confidence percentage.

This is a product and privacy constraint.

---

# 19. Family Recording Consent Model

Family recording is:

- parent/adult initiated;
- optional;
- explicit;
- local;
- replaceable;
- deletable.

It must never be silently captured during child mode.

---

# 20. Family Recording Storage

Family recordings must be stored:

> in app-private local storage.

Do not store them in:

- shared media gallery;
- Downloads;
- public Documents;
- cloud drive;
- remote server.

---

# 21. Family Recording Backup

Recommended Prototype 0.1 policy:

> exclude family recordings from automatic cloud/device backup by default where practical.

Reason:

- voice can be personally identifying;
- cross-device recovery is not a Prototype 0.1 promise;
- minimizing copies better matches the privacy posture.

---

# 22. Structured Learning Backup

Structured learning data backup is a separate decision.

If eligible for OS backup:

- this must be documented accurately;
- marketing must not imply data can never leave the physical device.

If backup is disabled:

- document that fact as well.

---

# 23. Local-Only Wording

Avoid overly broad wording such as:

> “Your data never leaves your device under any circumstances.”

This may be inaccurate if:

- OS backup;
- device migration;
- system diagnostics

create copies outside the app's runtime control.

Preferred wording:

> **Miozira does not send your child's learning activity or ordinary speaking attempts to Miozira servers.**

And, if verified:

> **Family recordings are stored only in Miozira's private app storage and are excluded from automatic backup.**

---

# 24. No Account

Prototype 0.1 requires no:

- sign-up;
- login;
- email verification;
- Google account;
- Apple account;
- profile name.

---

# 25. No Child Identifier

Local learning state should not need a child name.

Prototype 0.1 assumes one local learner context.

---

# 26. Future Multiple Learners

If future versions support multiple children, privacy design must be revisited.

Do not prematurely add:

```text
child_profile
child_name
birthday
avatar
```

to Prototype 0.1.

---

# 27. No Analytics SDK

Prototype 0.1 must not include:

- Firebase Analytics;
- Mixpanel;
- Amplitude;
- Meta/Facebook SDK;
- advertising analytics;
- third-party behavior tracking.

---

# 28. No Crash SDK by Default

Remote crash-reporting SDKs can collect device/app metadata.

Prototype 0.1 should begin without a remote crash SDK unless a deliberate privacy review approves one.

Local development crash diagnostics are sufficient for the initial family test.

---

# 29. If Remote Diagnostics Are Added Later

Any future remote diagnostics must be reviewed for:

- data collected;
- identifiers;
- child-directed policy;
- network permissions;
- retention;
- vendor terms.

It is outside Prototype 0.1.

---

# 30. No Advertising

No:

- banner ads;
- interstitials;
- rewarded ads;
- personalized ads;
- contextual ad SDK.

No ad identifiers.

---

# 31. No Third-Party Tracking

Dependencies must not silently initialize tracking.

Review the final dependency graph and merged manifest.

---

# 32. No Remote Config

Prototype 0.1 does not require Firebase Remote Config or similar services.

Adaptive constants are local and versioned.

---

# 33. No Cloud Database

No:

- Firebase Firestore;
- Supabase;
- CloudKit sync;
- custom backend.

---

# 34. No Cloud Speech

No:

- Google Cloud Speech;
- Azure Speech;
- AWS Transcribe;
- pronunciation assessment APIs;
- remote ASR.

---

# 35. Network Permission

Prototype 0.1 should aim to omit Android:

```text
INTERNET
```

permission entirely.

This makes the privacy posture easier to verify.

---

# 36. Data Retention

Local structured learning data may remain until:

- parent reset;
- app uninstall;
- future explicit retention policy.

Data volume is small.

No automatic deletion window is required in Prototype 0.1.

---

# 37. Family Recording Retention

Family recording remains until:

- parent deletes/replaces it;
- parent reset;
- app uninstall.

---

# 38. Temporary File Retention

Temporary family recording files should be deleted on:

- cancel;
- recording failure;
- app recovery cleanup.

---

# 39. Child PCM Retention

Retention:

> effectively zero after in-memory processing.

No disk persistence.

---

# 40. Parent Data Reset

Parent can trigger reset from protected parent area.

Reset should delete:

```text
sessions
interactions
adaptive state
real-world observations
family recording metadata
family recording files
selected settings/preferences
temporary files
```

---

# 41. Reset Should Preserve

Do not delete:

```text
bundled canonical content
application files
schema
```

---

# 42. Reset Confirmation

Reset must require an explicit adult confirmation.

Example:

> “This will remove Miozira learning history, observations, and family recordings from this device.”

Actions:

```text
Cancel
Reset Miozira data
```

---

# 43. No Cloud Recovery Promise

After reset/uninstall, there may be no recovery.

Parent copy must not imply:

- restore from Miozira account;
- recover from cloud;
- retrieve prior family recording.

---

# 44. Privacy Settings

Prototype 0.1 parent privacy screen should explain:

- no account;
- no ads;
- no analytics;
- local learning history;
- optional microphone;
- ordinary speaking attempts not saved;
- family recordings local;
- reset/delete option.

---

# 45. Suggested Parent Privacy Copy

Draft:

> **Privacy in Miozira**
>
> Miozira works offline and does not require an account.
>
> Your child's learning history is stored locally on this device.
>
> If you enable the microphone, Miozira briefly checks whether your child tried to speak. Ordinary speaking attempts are not transcribed, graded, saved, or uploaded.
>
> Family voice recordings are optional and stored in Miozira's private app storage.
>
> Miozira has no ads or behavioral analytics.
>
> You can reset Miozira's local data from Parent settings.

Final wording must reflect actual backup behavior.

---

# 46. Microphone Privacy Copy

Draft:

> **Microphone**
>
> Miozira can briefly listen to see whether your child tried to speak. It does not grade pronunciation, turn speech into text, save ordinary child speaking attempts, or upload them.
>
> The microphone is also used if you choose to make a family voice recording.
>
> Miozira still works if you leave microphone access off.

---

# 47. Parent Observation Privacy

Real-world observations should remain minimal.

Prototype 0.1 should not ask:

- where it happened;
- who was present;
- free-text story;
- photo/video evidence.

This avoids unnecessary contextual personal data.

---

# 48. Time Data

Local timestamps are necessary for scheduling and history.

They should not be combined with location or identity to create a behavioral profile.

---

# 49. Data Portability

Prototype 0.1 does not require export/download.

Because data is local and no account exists, formal cloud export is not necessary for the first prototype.

Future export should be parent-controlled.

---

# 50. Privacy and Accessibility

Accessibility services may access UI semantics according to OS behavior.

Miozira should not place unnecessary personal data in semantic labels.

---

# 51. Privacy and Logs

Development logs must not include:

- raw audio;
- family recording content;
- child name;
- exact filesystem paths if avoidable.

Semantic debugging identifiers are acceptable:

```text
pair.apple.ta
interaction UUID
```

---

# 52. Privacy and Screenshots

Miozira Prototype 0.1 does not need to block screenshots globally.

The app contains little personal visual data.

If future parent screens display sensitive personal information, revisit.

---

# 53. Privacy and Clipboard

Do not copy sensitive data to clipboard automatically.

Prototype 0.1 has no need.

---

# 54. Privacy and Notifications

No notifications are used.

Therefore no child learning content appears on lock-screen notifications.

---

# 55. Privacy and Background Activity

No microphone or data collection while app is backgrounded.

---

# 56. Privacy and Screen Lock

On screen lock:

- microphone stops;
- family recording stops/cancels safely;
- no background capture continues.

---

# 57. Third-Party Libraries

Every third-party dependency must be reviewed for:

- network access;
- analytics;
- identifiers;
- advertising;
- data collection;
- privacy policy.

Prefer official or simple local libraries.

---

# 58. Dependency Privacy Allowlist

Release QA should maintain an explicit list of approved dependencies.

New dependency additions require privacy review.

---

# 59. Google Play Families

Miozira is intended for young children/families.

Therefore Google Play Families policy requirements are relevant if distributed publicly.

Important privacy alignment includes:

- microphone use must be necessary and disclosed;
- data handling must match child-directed policy;
- third-party SDKs must be appropriate;
- Data Safety declarations must match actual collection/sharing.

Policy text may change, so release review must use current Google Play documentation.

---

# 60. Google Play Data Safety

At release time, complete Data Safety based on actual implementation.

Prototype 0.1 intended posture:

- no account data collection;
- no location;
- no contacts;
- no advertising;
- no remote analytics;
- no child raw speech collection/upload;
- local-only family recording files.

Do not mechanically copy these into the Play form without checking Google's definitions of “collected” and “shared” at submission time.

---

# 61. Microphone and Data Safety Classification

The fact that microphone audio is processed transiently on device may still need to be described according to Google Play's current definitions.

Do not assume:

> “not uploaded” automatically means “not relevant to disclosure.”

Verify the current form/policy during release.

---

# 62. COPPA / Child Privacy Posture

Prototype 0.1's privacy architecture materially reduces child-data exposure because it avoids:

- accounts;
- cloud profiles;
- tracking;
- advertising;
- persistent child voice;
- remote analytics.

This document does not claim legal compliance by itself.

Formal legal review may still be needed before public launch in relevant jurisdictions.

---

# 63. GDPR / UK GDPR Posture

Similar principle:

data minimization and local processing reduce exposure.

But legal obligations depend on:

- distribution;
- controller status;
- age-assurance/consent;
- jurisdiction.

Do not use this technical spec as legal advice.

---

# 64. India's DPDP Consideration

If Miozira is publicly distributed in India, the Digital Personal Data Protection framework and child-data obligations should be reviewed against the implementation and launch context.

Prototype 0.1 intentionally minimizes remote personal-data processing.

Formal launch review remains separate.

---

# 65. Public Website Privacy Policy

If Miozira gets a public website/app listing, privacy policy should accurately describe:

- app local processing;
- no account requirement;
- microphone use;
- family recordings;
- backups if relevant;
- parent deletion/reset;
- contact route for privacy questions.

---

# 66. No Marketing Overclaim

Do not say:

```text
100% anonymous
zero data
nothing is ever stored
```

because Miozira does store local learning history and optional family recordings.

Better:

> **No account, no ads, no behavioral analytics, and no cloud upload of ordinary child speaking attempts.**

---

# 67. “Zero Data” Terminology

Avoid calling the app “zero-data” if that could imply no local data exists.

Prefer:

- local-first;
- privacy-minimal;
- no-account;
- no-tracking;
- offline.

---

# 68. Privacy Threat Examples

Potential privacy failures include:

- accidental raw child audio file;
- analytics SDK added by dependency;
- family recordings backed up unintentionally;
- debug log containing paths/audio metrics;
- remote crash reporter enabled;
- shared storage usage;
- permission copy contradicting code.

These must be tested.

---

# 69. Privacy QA Checklist

Before family testing:

- [ ] no account exists;
- [ ] no backend exists;
- [ ] no analytics SDK exists;
- [ ] no ad SDK exists;
- [ ] no INTERNET permission if feasible;
- [ ] child PCM not written to disk;
- [ ] child PCM not logged;
- [ ] child PCM not uploaded;
- [ ] family recordings stored privately;
- [ ] family recordings not in media gallery;
- [ ] reset deletes family files;
- [ ] permission copy matches behavior;
- [ ] backup configuration reviewed;
- [ ] dependency privacy review complete.

---

# 70. Privacy Device Test

On a test device:

1. run child session;
2. speak during microphone window;
3. inspect app-private files;
4. confirm no child audio file exists;
5. create family recording;
6. confirm family file exists privately;
7. delete it;
8. confirm removal;
9. reset app;
10. confirm learning data and family files removed.

---

# 71. Network Test

Run app in:

```text
airplane mode
```

Core behavior should be unchanged.

If build has no INTERNET permission, verify via manifest.

---

# 72. Dependency Network Test

If feasible during QA:

- inspect network activity during normal family-test use.

Expected:

> no Miozira runtime network traffic.

---

# 73. Privacy Invariants

### PRIV-INV-001
Prototype 0.1 requires no child or parent account.

### PRIV-INV-002
No behavioral advertising exists.

### PRIV-INV-003
No remote analytics exists.

### PRIV-INV-004
Ordinary child speaking-attempt audio is never stored.

### PRIV-INV-005
Ordinary child speaking-attempt audio is never uploaded.

### PRIV-INV-006
No child transcription or pronunciation score exists.

### PRIV-INV-007
Family recordings require explicit adult action.

### PRIV-INV-008
Family recordings remain in app-private storage.

### PRIV-INV-009
Family recordings can be deleted/reset.

### PRIV-INV-010
No location, contacts, camera, or advertising identifier is collected.

### PRIV-INV-011
Real-world observations are minimal and local.

### PRIV-INV-012
Privacy wording must match actual backup/runtime behavior.

### PRIV-INV-013
New dependencies require privacy review.

### PRIV-INV-014
No cloud service is required for the child learning loop.

---

# 74. Open Privacy Decisions

Before public distribution, confirm:

1. final Android OS backup configuration;
2. whether structured learning DB is backup-eligible;
3. family recording backup exclusion;
4. final Play Data Safety answers under then-current definitions;
5. final Google Play Families review;
6. jurisdiction-specific legal review needs;
7. final public privacy-policy wording;
8. whether any remote crash reporting is introduced later.

---

# 75. Relationship to `data-inventory.md`

`data-inventory.md` should enumerate every stored/transient field and its:

- purpose;
- location;
- retention;
- sensitivity;
- deletion path.

This document defines the policy baseline.

---

# 76. Relationship to `permissions.md`

`permissions.md` defines microphone access.

This document defines the privacy promise attached to that access.

---

# 77. Relationship to `microphone.md`

`microphone.md` must implement:

```text
transient local PCM
→ semantic attempt result
→ discard PCM
```

---

# 78. Relationship to `local-storage.md`

`local-storage.md` defines:

- app-private database/files;
- temporary files;
- backup handling;
- reset.

---

# 79. Relationship to `security.md`

`security.md` must protect the privacy properties defined here through:

- least privilege;
- app sandbox;
- dependency review;
- integrity;
- safe file handling.

---

# 80. Relationship to `child-safety.md`

Privacy is part of child safety.

The product must not turn participation into surveillance or persistent behavioral profiling.

---

# 81. Decision Summary

Miozira Prototype 0.1 privacy posture is:

```text
No account
No ads
No behavioral analytics
No backend
No cloud speech
No child voice storage
No transcription
No pronunciation grading
No location
No contacts
No camera
No ad identifier
Local learning state
Optional local family recordings
Parent-controlled reset
```

The strongest privacy property is architectural:

> **the app does not need a remote service to teach the child.**

The governing rule is:

> **Miozira should remember only what helps the child learn locally, and forget everything else by design.**
