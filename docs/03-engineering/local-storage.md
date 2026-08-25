# Miozira — Local Storage Specification

**Document:** `local-storage.md`  
**Version:** 0.1  
**Status:** Draft for Prototype 0.1  
**Product:** Miozira  
**Architecture:** Fully local / offline-first  
**Primary platform:** Android tablet  
**Future platform:** iPadOS  
**Last updated:** 2026-08-25

---

## 1. Purpose

This document defines how Miozira Prototype 0.1 stores data locally on the device.

It specifies:

- structured database storage;
- preference storage;
- bundled canonical content;
- family audio recordings;
- temporary files;
- caches;
- backup eligibility;
- cleanup rules;
- reset behavior;
- storage quotas;
- privacy boundaries;
- platform abstraction.

The objective is:

> **all required learning behavior works locally, with clear ownership and minimal data retention.**

---

# 2. Storage Principles

## LS-P01 — Local is authoritative

Prototype 0.1 has no cloud source of truth.

## LS-P02 — Store only what is needed

Do not retain data merely because it is technically possible.

## LS-P03 — Sensitive media stays private

Family recordings stay in app-private storage.

## LS-P04 — Child attempt audio is transient

Raw child PCM is never written to storage.

## LS-P05 — Bundled content is immutable at runtime

Canonical assets ship with the app and are not user-editable.

## LS-P06 — Reset is understandable

A parent-triggered reset should remove local user-learning data predictably.

---

# 3. Storage Categories

Prototype 0.1 uses five storage categories:

```text
1. Structured database
2. Preferences
3. Bundled app assets
4. Private user-generated files
5. Temporary/cache files
```

---

# 4. Structured Database

Primary store:

> **Room / SQLite**

Recommended database:

```text
miozira.db
```

Stores:

- concepts;
- languages;
- concept-language pairs;
- sessions;
- interactions;
- pair learning state;
- real-world observations;
- family recording metadata.

The detailed schema is defined in:

> `database.md`

---

# 5. Database Location

The database must live in:

> app-private internal storage

It must not be placed in:

- Downloads;
- Documents;
- shared external storage;
- public media directories.

---

# 6. Database Visibility

Ordinary users and other apps should not need direct filesystem access to `miozira.db`.

All normal access occurs through:

```text
repositories
→ Room
```

---

# 7. Database Size Expectations

Prototype 0.1 data volume is tiny.

Expected scale:

- 16 concept-language pairs;
- a small number of sessions per day;
- small interaction rows;
- occasional real-world observations.

Even over many months, structured data should remain modest.

There is no need for storage optimization such as:

- sharding;
- archival databases;
- compression;
- remote offloading.

---

# 8. Preference Storage

Small preferences should use:

> **DataStore or equivalent**

Examples:

```text
setup_complete
starting_language
speech_attempt_detection_enabled
```

Potential future preferences:

```text
parent_ui_language
reduced_optional_sound
```

---

# 9. Preference Boundary

Do not store structured learning history as preference keys.

Avoid:

```text
pair.apple.ta.exposure1
pair.apple.ta.exposure2
```

in DataStore.

That belongs in Room.

---

# 10. Bundled Canonical Assets

Prototype 0.1 must bundle all required canonical learning content.

Includes:

- 8 primary concept visuals;
- 16 canonical word recordings;
- required prompt audio;
- content manifest;
- curated real-world suggestions.

---

# 11. Bundled Asset Availability

Required child-learning assets must be available:

- at first launch;
- without account;
- without download;
- without network.

---

# 12. Bundled Asset Location

Implementation may package assets through normal application resources/assets.

Logical access occurs through:

```text
AssetProvider
ContentRepository
```

Higher layers should not depend on physical package paths.

---

# 13. Bundled Assets Are Read-Only

Runtime code must not overwrite canonical bundled assets.

A content update occurs through:

- application update;
- content-version migration;
- future explicitly designed content-pack mechanism.

Prototype 0.1 has no runtime remote content update.

---

# 14. Canonical Content Version

Bundled content should expose:

```text
content_version
```

Example:

```text
prototype-0.1-content-v1
```

This value should be stored with sessions where needed for historical interpretation.

---

# 15. Family Recording Storage

Family voice recordings are:

> private user-generated files

They must live in app-private file storage.

---

# 16. Family Recording File Scope

A family recording belongs to:

```text
ConceptLanguagePair
```

Example:

```text
pair.apple.ta
```

At most one active family recording per pair is required in Prototype 0.1.

---

# 17. Family Recording Logical IDs

Database stores:

```text
logical_file_id
```

not absolute platform path.

Example:

```text
family_audio_8f3e...
```

`FileStore` resolves the actual path.

---

# 18. Family Recording Directory

Conceptual directory:

```text
/private/family-recordings/
```

Physical Android/iOS path is implementation-specific.

Do not expose platform path outside the file-storage layer.

---

# 19. Family Recording Filename

Recommended generated filename pattern:

```text
<recording-uuid>.<extension>
```

Example:

```text
8f3e2d...m4a
```

Do not derive filenames directly from:

- child name;
- family name;
- personal information.

---

# 20. Family Recording Replacement

Safe replacement sequence:

```text
record temporary file
→ preview
→ save/promote new file
→ update DB metadata
→ delete old file
```

Do not delete the old valid recording before the replacement is safely available.

---

# 21. Family Recording Delete

Delete should remove:

1. active family recording file;
2. active family recording metadata.

Canonical bundled audio remains.

---

# 22. Family Recording Recovery

If DB metadata exists but the file is missing:

- treat family recording as unavailable;
- preserve canonical fallback;
- allow parent to re-record;
- optionally clean stale metadata.

---

# 23. Orphan Family File

If a family audio file exists without DB metadata:

- development/recovery logic may identify it as orphaned;
- safe cleanup may delete it.

Do not expose orphan files in child mode.

---

# 24. Temporary Recording Storage

Parent recording should first use:

> private temporary storage

Conceptual location:

```text
/private/temp/
```

Temporary files should not be treated as saved family recordings.

---

# 25. Temporary File Lifecycle

Create on:

```text
record start
```

Promote on:

```text
parent confirms save
```

Delete on:

```text
cancel
recording failure
replacement failure cleanup
recovery startup cleanup
```

---

# 26. Temporary Child PCM

Child speaking-attempt PCM must **not** create a temporary file.

It remains:

```text
memory only
```

This is an explicit privacy invariant.

---

# 27. Cache Storage

Prototype 0.1 may use cache for:

- decoded/derived thumbnails;
- transient playback preparation;
- generated non-sensitive UI artifacts.

Cache must be:

- disposable;
- rebuildable;
- not source of truth.

---

# 28. Cache Must Not Hold Learning Truth

Do not store the only copy of:

- session history;
- observations;
- family recording metadata;
- settings

in cache.

---

# 29. Cache Must Not Persist Child Audio

Do not use disk cache for:

- child attempt PCM;
- child microphone buffers.

---

# 30. Cache Cleanup

Cache may be cleared:

- by OS;
- by app;
- during troubleshooting;
- during reset if convenient.

App correctness must not depend on cache survival.

---

# 31. Backup Policy

Prototype 0.1 must make an explicit decision about OS-level backup.

This is important because:

> “local-only” does not automatically mean “never copied by the operating system.”

---

# 32. Recommended Backup Direction

For Prototype 0.1:

### Structured learning database

Recommended:

> **eligible for normal device backup only if platform behavior is clearly documented and consistent with the privacy promise**

### Family recordings

Recommended:

> **exclude from automatic cloud/device backup by default**

Reason:

- they contain identifiable family voice;
- Prototype 0.1 does not promise cross-device restoration;
- minimizing unintended copies better matches the privacy posture.

---

# 33. Android Backup Review

Implementation must explicitly review Android backup behavior for:

- Room database;
- DataStore;
- family recording directory.

Do not rely on defaults without checking.

---

# 34. iOS Backup Review

Future iOS implementation must similarly decide:

- which app-support files are backup eligible;
- which family-media files should be excluded.

---

# 35. Privacy Wording and Backup

Parent-facing privacy copy must not say:

> “never leaves this physical device under any circumstance”

unless OS backup behavior truly guarantees that.

Safer Prototype 0.1 wording:

> “Miozira does not upload your child's learning data or family recordings to Miozira servers.”

If family recordings are excluded from backup, that stronger local-storage fact may be stated separately.

---

# 36. Storage Encryption

Prototype 0.1 does not require custom application-layer encryption for the database by default.

The app relies on:

- OS app sandbox;
- device security;
- private app storage.

Reasons:

- no account credentials;
- no financial data;
- no child raw speech;
- small local learning dataset;
- custom key management would add complexity.

Threat modeling may revisit this.

---

# 37. Family Audio Encryption

Same default:

> no custom per-file encryption unless threat analysis identifies a concrete need.

App-private storage remains mandatory.

---

# 38. Shared/Public Storage Prohibition

Prototype 0.1 must not save family recordings into:

- Photos;
- Music;
- Downloads;
- Documents;
- shared media gallery;
- SD-card public directories.

---

# 39. Media Scanner

Family recordings should not be indexed into a public system media library.

They are application data.

---

# 40. File Export

Prototype 0.1 does not require parent export of family recordings.

No Share button is needed.

---

# 41. Data Export

Prototype 0.1 does not require:

- CSV;
- JSON export;
- cloud backup;
- email export.

Future parent-controlled export can be separately designed if needed.

---

# 42. Storage Quotas

Prototype 0.1 storage needs are small.

Suggested soft design budget:

```text
structured DB:         < 10 MB expected
family recordings:     < 10 MB expected
cache/temp:            < 20 MB expected
bundled media:         quality-dependent, likely modest
```

These are not hard limits.

---

# 43. Family Recording Quota

With:

```text
16 pairs
× one recording
× ≤5 seconds each
```

family audio storage should remain very small.

No complicated quota manager is required.

---

# 44. Recording Duration Limit

As defined in `audio.md`:

> approximately 5 seconds maximum per family-word recording

This naturally constrains storage.

---

# 45. Free-Space Failure

If device storage is too low to save a family recording:

Parent UI should say something like:

> “There isn't enough device storage to save this recording. The standard voice is still available.”

Child learning should continue.

---

# 46. Database Low-Space Failure

If SQLite cannot persist due to device storage failure:

- stop unsafe writes;
- surface a parent-facing recovery issue;
- do not silently pretend evidence was saved.

---

# 47. Storage Write Atomicity

Important file writes should use safe patterns.

For replacement:

```text
write new temp
→ verify
→ rename/promote atomically where possible
→ update metadata
→ delete old
```

Avoid half-written active files.

---

# 48. Temporary File Naming

Use random/generated IDs.

Example:

```text
tmp_recording_<uuid>.m4a
```

Avoid predictable names that can collide.

---

# 49. Startup Cleanup

On app launch, `RecoveryService` may clean:

- orphan temporary recording files;
- obsolete cache files;
- incomplete transient artifacts.

Do not delete active family recordings.

---

# 50. Cleanup Age

Temporary files older than a safe threshold may be deleted during recovery.

Example:

```text
older than 24 hours
```

Exact duration is an implementation choice.

The cleanup should err toward preserving potentially valid user files unless clearly temporary.

---

# 51. Session Data Retention

Prototype 0.1 may retain session/evidence history indefinitely on-device until:

- parent resets data;
- app is uninstalled;
- future retention policy changes.

The data volume is very small.

---

# 52. Real-World Observation Retention

Parent-reported observations may remain for the life of the local profile unless reset.

No automatic expiry is required.

---

# 53. Derived State Retention

`PairLearningState` remains current state.

It may be rebuilt at any time from evidence if needed.

---

# 54. Settings Retention

Settings remain until:

- user changes them;
- parent reset includes them;
- app uninstall.

---

# 55. App Uninstall

Uninstall normally removes app-private data according to platform behavior.

Miozira does not provide account-based restoration in Prototype 0.1.

---

# 56. Reinstall

After reinstall:

- bundled content returns;
- local learning history may be gone;
- family recordings may be gone;
- setup may run again.

Do not imply cloud recovery exists.

---

# 57. Reset Semantics

Parent reset should remove local user-generated learning data.

Reset scope should include:

```text
sessions
interactions
pair learning state
real-world observations
family recording metadata
family recording files
selected preferences
temporary files
```

---

# 58. Reset Should Preserve

Reset should preserve:

```text
bundled canonical concepts
bundled canonical audio
bundled visuals
database schema
application binaries
```

---

# 59. Reset and Cache

Cache may be cleared during reset.

This is optional but safe because it is disposable.

---

# 60. Reset Transaction Boundary

Structured DB deletion should occur transactionally.

File deletion occurs through `FileStore`.

Recommended flow:

```text
1. validate parent confirmation
2. stop audio/mic/recording
3. delete structured user data transactionally
4. delete family files
5. reset preferences
6. clean temp/cache
7. reseed/rebuild pair state as NEW
```

---

# 61. Reset Failure

If reset partially fails:

- do not display “Reset complete”;
- report parent-facing error;
- allow recovery/retry.

---

# 62. Reset Idempotency

Calling reset again after partial/successful reset should be safe.

---

# 63. Post-Reset State

After successful reset:

- all 16 concept-language pairs exist;
- all learning states return to initial `NEW`;
- no sessions remain;
- no observations remain;
- no family recording remains;
- setup behavior follows reset policy.

---

# 64. Content Seeding

Canonical content seed logic must be idempotent.

On startup:

```text
ensure content version exists
ensure concepts exist
ensure languages exist
ensure 16 pairs exist
```

No duplicates.

---

# 65. Content Upgrade

If app update includes new canonical content version:

- perform explicit migration/seed update;
- preserve existing learning history where semantic pair IDs remain valid;
- do not silently remap evidence to different concepts.

---

# 66. Removed Content

If a future content version removes a concept:

- historical evidence should not be deleted automatically;
- pair may become inactive.

Prototype 0.1 does not remove any of the eight concepts during the family-test freeze.

---

# 67. Asset Replacement

If canonical visual/audio changes but pair identity stays the same:

- increment content version;
- retain historical session content version.

---

# 68. Storage Abstraction

Higher layers should use:

```text
Repositories
FileStore
AssetProvider
SettingsRepository
```

not direct filesystem APIs.

---

# 69. FileStore Contract

Conceptually:

```text
createTemporaryFamilyRecording()
promoteTemporaryFile(...)
delete(...)
exists(...)
```

No raw absolute path should escape this layer.

---

# 70. AssetProvider Contract

Conceptually:

```text
resolveVisualAsset(...)
resolveCanonicalAudioAsset(...)
resolvePromptAsset(...)
```

Bundled assets remain immutable.

---

# 71. SettingsRepository Contract

Conceptually:

```text
get/set setup complete
get/set starting language
get/set speech-attempt detection enabled
```

---

# 72. Storage Diagnostics

Development builds may inspect:

```text
database size
family audio total size
temp file count
cache size
orphan file count
```

No remote reporting required.

---

# 73. Parent Storage UI

Prototype 0.1 does not need a full storage-management screen.

Privacy/settings may simply explain:

- data is local;
- family recordings are local;
- reset removes local data.

---

# 74. Parent Recording Management

The family recording screen is the normal way to remove an individual family audio item.

No file browser.

---

# 75. No Child File Access

Child mode must not expose:

- filenames;
- storage locations;
- download/open actions.

---

# 76. Storage Permissions

Prototype 0.1 should not require broad storage permissions for app-private storage.

Avoid permissions for:

- read external storage;
- write external storage;
- photos/media

unless a future feature explicitly needs them.

---

# 77. Microphone Permission Is Separate

The microphone permission does not grant or imply external storage access.

Child attempt PCM never reaches disk.

---

# 78. No INTERNET Dependency

Storage behavior must remain fully functional when:

```text
airplane mode = on
```

---

# 79. Backup and Network Distinction

OS backup behavior, if enabled, is different from Miozira runtime networking.

The architecture should document both separately.

---

# 80. Storage Corruption

If family file metadata or database state becomes inconsistent:

Recovery should favor:

- preserving canonical app functionality;
- preventing crash loops;
- allowing parent reset if necessary.

---

# 81. Corrupt Cache

Delete/rebuild.

---

# 82. Corrupt Temporary File

Delete.

---

# 83. Corrupt Family Recording

Mark unavailable and let parent replace.

Canonical audio remains.

---

# 84. Corrupt Database

Do not silently destructive-reset immediately.

Use recovery flow defined in `database.md` / `architecture.md`.

---

# 85. Storage Test Matrix

Test:

1. first launch;
2. database created;
3. DataStore preferences persist;
4. family recording save;
5. family recording replace;
6. family recording delete;
7. temp recording cancel;
8. app killed during recording;
9. orphan temp cleanup;
10. low-storage recording failure;
11. reset;
12. reset called twice;
13. app restart after reset;
14. app upgrade with content seed;
15. database migration;
16. no external-storage permission;
17. airplane mode;
18. uninstall/reinstall expectations;
19. OS backup configuration validation;
20. family recording not visible in media gallery.

---

# 86. Storage Acceptance Criteria

Before family test:

- [ ] database lives in app-private storage;
- [ ] settings persist locally;
- [ ] all canonical assets are bundled;
- [ ] no child attempt audio file is created;
- [ ] family recordings are private files;
- [ ] replacing recording is safe;
- [ ] deleting recording preserves canonical audio;
- [ ] temporary files are cleaned;
- [ ] cache is disposable;
- [ ] reset clears user data;
- [ ] reset preserves canonical content;
- [ ] no broad storage permission is required;
- [ ] family recordings do not appear in public media apps;
- [ ] backup behavior is explicitly configured/documented.

---

# 87. Storage Invariants

### LS-INV-001
The learning database is app-private.

### LS-INV-002
Small preferences are separate from structured learning history.

### LS-INV-003
All required canonical content is bundled locally.

### LS-INV-004
Canonical bundled assets are read-only at runtime.

### LS-INV-005
Family recordings live in app-private storage.

### LS-INV-006
Family recording paths are hidden behind logical file IDs.

### LS-INV-007
Child attempt PCM is memory-only and never written to disk.

### LS-INV-008
Cache is never the source of truth.

### LS-INV-009
No broad shared-storage permission is required.

### LS-INV-010
Family recordings do not enter public media libraries.

### LS-INV-011
Reset removes user learning data and family recordings.

### LS-INV-012
Reset preserves canonical bundled content.

### LS-INV-013
Temporary family recording files are disposable.

### LS-INV-014
OS backup behavior is explicitly reviewed, not assumed.

### LS-INV-015
Storage functionality requires no network.

---

# 88. Open Storage Decisions

Before release/family-test freeze, confirm:

1. exact Android backup XML/rules;
2. whether structured learning DB participates in device backup;
3. family recording backup exclusion implementation;
4. final family recording file extension/codec;
5. temp cleanup age;
6. exact reset preference scope;
7. whether cache directory is needed at all for Prototype 0.1;
8. future iOS backup-exclusion mapping.

---

# 89. Relationship to `database.md`

`database.md` defines:

- structured entities;
- migrations;
- relational integrity.

This document defines where that database lives and how it relates to other storage categories.

---

# 90. Relationship to `audio.md`

`audio.md` defines family recording content/format behavior.

This document defines:

- file ownership;
- lifecycle;
- storage location;
- cleanup.

---

# 91. Relationship to `microphone.md`

`microphone.md` requires:

> child attempt PCM remains transient and is never stored.

This document enforces the storage side of that guarantee.

---

# 92. Relationship to `privacy.md`

`privacy.md` must accurately describe:

- database storage;
- family recording storage;
- backup behavior;
- reset/deletion behavior.

---

# 93. Relationship to `security.md`

`security.md` should review:

- app sandbox;
- backup exposure;
- file permissions;
- tampering;
- dependency access to local files.

---

# 94. Decision Summary

Miozira Prototype 0.1 stores data using a deliberately simple local model:

```text
Room / SQLite
    → structured learning state

DataStore
    → small preferences

Bundled app assets
    → canonical visuals/audio/content manifest

App-private files
    → family recordings

Temporary/cache storage
    → disposable transient artifacts
```

Core guarantees:

1. no network is required;
2. canonical content ships with the app;
3. child attempt audio is never written to disk;
4. family recordings remain private;
5. no shared-storage permission is needed;
6. temporary files are cleaned;
7. cache is disposable;
8. reset removes local user-learning data while preserving canonical content;
9. OS backup behavior is explicitly configured rather than assumed.

The governing rule is:

> **Miozira should know exactly why every local file exists, where it lives, and when it is safe to delete it.**
