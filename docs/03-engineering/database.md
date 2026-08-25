# Miozira — Database Specification

**Document:** `database.md`  
**Version:** 0.1  
**Status:** Draft for Prototype 0.1  
**Product:** Miozira  
**Architecture:** Local-first, Room/SQLite  
**Prototype:** 8 concepts × English + Tamil  
**Last updated:** 2026-08-25

---

## 1. Purpose

This document defines the local structured data model for Miozira Prototype 0.1.

It specifies:

- database entities;
- keys and relationships;
- session and interaction persistence;
- exposure/replay/speaking-attempt evidence;
- derived adaptive state;
- parent real-world observations;
- family recording metadata;
- content version references;
- indexes;
- uniqueness/idempotency rules;
- transactions;
- migrations;
- reset behavior.

This database is the local learning source of truth.

It is not:

- a cloud-sync database;
- an analytics warehouse;
- an account/profile database;
- a pronunciation corpus.

---

# 2. Database Goals

The database should be:

- small;
- deterministic;
- inspectable;
- privacy-preserving;
- resilient to app restarts;
- safe across orientation/lifecycle recreation;
- easy to migrate;
- suitable for Room/SQLite;
- portable through Kotlin Multiplatform where practical.

---

# 3. Data Categories

Prototype 0.1 has three persistent data categories.

## A. Bundled/static content identity

Examples:

- concept IDs;
- language IDs;
- content version;
- logical asset IDs.

## B. Dynamic learning data

Examples:

- sessions;
- scheduled interactions;
- exposures;
- replay count;
- speaking-attempt state;
- derived adaptive state;
- real-world observations.

## C. User-generated media metadata

Examples:

- family recording file reference;
- associated concept-language pair;
- created/updated timestamps.

Actual family audio bytes live in app-private file storage, not inside SQLite blobs.

---

# 4. Storage Boundary

Recommended persistent technologies:

```text
Room / SQLite
  → structured learning data

DataStore
  → small settings/preferences

App-private file storage
  → family audio files
```

Do not store large media blobs in the relational database.

---

# 5. Database Naming

Recommended database name:

```text
miozira.db
```

The name may change before release, but database identity should remain stable once migrations begin.

---

# 6. Primary Identifier Policy

Use stable string IDs for domain identity where values are globally meaningful.

Examples:

```text
concept.apple
language.en
language.ta
```

Use generated UUID/string identifiers for event-like records.

Examples:

```text
session_id
interaction_id
observation_id
recording_id
```

Avoid database-generated integer IDs as the only externally meaningful identity for domain records.

---

# 7. Time Storage

Store timestamps in a stable machine-readable form.

Recommended:

> UTC epoch milliseconds

Example field:

```text
created_at_ms INTEGER
```

Domain/UI can convert to local time for display.

Do not store locale-formatted timestamps such as:

```text
25/08/2026 11:10 AM
```

as the canonical database value.

---

# 8. Boolean Storage

SQLite-backed Room may represent booleans as integer-compatible values.

Logical schema should still treat them as booleans.

---

# 9. Enum Storage

Store semantic enums using stable string names when practical.

Examples:

```text
ATTEMPT_DETECTED
NO_ATTEMPT_DETECTED
MICROPHONE_UNAVAILABLE
NOT_MEASURED
```

Advantages:

- easier debugging;
- clearer migrations;
- less risk from enum ordinal changes.

Do not store Kotlin enum ordinal numbers as persistent meaning.

---

# 10. Core Entity Overview

Recommended Prototype 0.1 entities:

```text
ContentVersion
Concept
Language
ConceptLanguagePair
Session
Interaction
PairLearningState
RealWorldObservation
FamilyRecording
```

Optional separate event tables may be introduced later if evidence granularity requires them.

For Prototype 0.1, the interaction row can contain the core scheduled-interaction evidence while preserving append-oriented semantics.

---

# 11. Relationship Overview

```text
Concept
  └── ConceptLanguagePair
        ├── PairLearningState
        ├── Interaction
        ├── RealWorldObservation
        └── FamilyRecording

Language
  └── ConceptLanguagePair

Session
  └── Interaction
```

---

# 12. ContentVersion

Purpose:

- identify the bundled canonical content revision used by a session/build.

Recommended fields:

```text
version_id TEXT PRIMARY KEY
created_at_ms INTEGER NOT NULL
description TEXT NULL
```

Example:

```text
prototype-0.1-content-v1
```

Prototype 0.1 may also keep content version in the bundled manifest and store only the active version reference in session rows.

---

# 13. Concept Entity

Recommended table:

```text
concept
```

Fields:

```text
concept_id TEXT PRIMARY KEY
category TEXT NOT NULL
primary_visual_asset_id TEXT NOT NULL
movement_eligible INTEGER NOT NULL
content_version_id TEXT NOT NULL
is_enabled INTEGER NOT NULL
```

Prototype examples:

```text
concept.apple
concept.ball
concept.cup
concept.hand
concept.nose
concept.cat
concept.car
concept.water
```

---

# 14. Concept Notes

`Concept` is language-independent.

Do not store:

```text
english_word
```

as the primary concept identity.

The concept remains stable even if a Tamil lexical form changes.

---

# 15. Language Entity

Recommended table:

```text
language
```

Fields:

```text
language_id TEXT PRIMARY KEY
language_code TEXT NOT NULL UNIQUE
display_name TEXT NOT NULL
is_enabled INTEGER NOT NULL
```

Prototype rows:

```text
language.en | en | English
language.ta | ta | Tamil
```

---

# 16. ConceptLanguagePair Entity

Recommended table:

```text
concept_language_pair
```

Fields:

```text
pair_id TEXT PRIMARY KEY
concept_id TEXT NOT NULL
language_id TEXT NOT NULL
spoken_form TEXT NOT NULL
canonical_audio_asset_id TEXT NOT NULL
review_status TEXT NOT NULL
content_version_id TEXT NOT NULL
is_enabled INTEGER NOT NULL
```

Unique constraint:

```text
UNIQUE(concept_id, language_id)
```

Example:

```text
pair.apple.en
pair.apple.ta
```

---

# 17. Pair ID

Recommended stable logical format:

```text
pair.<concept-slug>.<language-code>
```

Examples:

```text
pair.apple.en
pair.apple.ta
pair.ball.en
pair.ball.ta
```

This is convenient but still should be treated as an opaque identifier by higher-level code.

---

# 18. Spoken Form Changes

If `spoken_form` changes:

- increment content version;
- update canonical audio reference;
- preserve historical session content version references.

Do not silently rewrite old family-test interpretation without version traceability.

---

# 19. Session Entity

Recommended table:

```text
session
```

Fields:

```text
session_id TEXT PRIMARY KEY
started_at_ms INTEGER NOT NULL
ended_at_ms INTEGER NULL
starting_language_id TEXT NULL
completion_reason TEXT NULL
content_version_id TEXT NOT NULL
meaningful_activity INTEGER NOT NULL DEFAULT 0
generated_suggestion_id TEXT NULL
```

---

# 20. Session Completion Reason

Stable values:

```text
PLANNED
CHILD_INACTIVE
PARENT_ENDED
APP_EXITED
LONG_INTERRUPTION
ERROR_RECOVERY
```

These are operational reasons, not child performance labels.

---

# 21. Meaningful Activity

`meaningful_activity` is a derived/recorded session flag used for decisions such as whether to create a real-world suggestion.

It should not become a child score.

The exact threshold is owned by `session-design.md`.

---

# 22. Interaction Entity

Recommended table:

```text
interaction
```

Purpose:

> one scheduled concept-language encounter within a session.

Fields:

```text
interaction_id TEXT PRIMARY KEY
session_id TEXT NOT NULL
pair_id TEXT NOT NULL
sequence_index INTEGER NOT NULL
selection_reason TEXT NOT NULL
started_at_ms INTEGER NOT NULL
completed_at_ms INTEGER NULL
completion_reason TEXT NULL

exposure_committed INTEGER NOT NULL DEFAULT 0
exposure_committed_at_ms INTEGER NULL

replay_count INTEGER NOT NULL DEFAULT 0

attempt_state TEXT NOT NULL DEFAULT 'NOT_MEASURED'
attempt_recorded_at_ms INTEGER NULL

planned INTEGER NOT NULL DEFAULT 1
```

---

# 23. Interaction Foreign Keys

```text
session_id
  REFERENCES session(session_id)

pair_id
  REFERENCES concept_language_pair(pair_id)
```

Recommended delete behavior:

- deleting/resetting a session should remove its interactions;
- normal app operation should not delete individual historical interactions casually.

---

# 24. Interaction Sequence

Within each session:

```text
sequence_index
```

represents scheduled order.

Unique constraint:

```text
UNIQUE(session_id, sequence_index)
```

This aids debugging and deterministic reconstruction.

---

# 25. Selection Reason

Stable values include:

```text
DUE_EMERGING
DUE_FAMILIAR
NEW_INTRODUCTION
EASY_OPENER
RECOVERY_ITEM
SESSION_CLOSER
LANGUAGE_BALANCE
REINTRODUCTION_AFTER_REST
INSUFFICIENT_ALTERNATIVES
```

Store the reason used at selection time.

Do not recalculate historical selection reasons later.

---

# 26. Exposure Persistence

A scheduled interaction can have:

```text
exposure_committed = false
```

until the canonical audio reaches the required exposure threshold.

Once committed:

```text
exposure_committed = true
exposure_committed_at_ms = ...
```

This write must be idempotent.

---

# 27. Exposure Idempotency

Repository operation:

```text
commitExposure(interactionId)
```

must behave safely when called more than once.

Expected behavior:

```text
first call  → commit exposure
later call  → no duplicate learning event
```

The `interaction_id` is the idempotency boundary.

---

# 28. Planned But Unseen Interactions

An interaction may be created/planned but never exposed due to:

- early session end;
- app interruption;
- parent exit;
- error.

Such a row must remain:

```text
exposure_committed = false
```

and must not contribute to exposure counts.

---

# 29. Replay Count

`replay_count` represents child-initiated replays associated with the scheduled interaction.

It does not represent:

- adaptive scheduler repetition;
- a new scheduled item.

Replay update should be atomic:

```text
replay_count = replay_count + 1
```

---

# 30. Speaking Attempt State

Recommended persisted values:

```text
ATTEMPT_DETECTED
NO_ATTEMPT_DETECTED
MICROPHONE_UNAVAILABLE
NOT_MEASURED
```

Potential future value:

```text
INTERRUPTED
```

Use only if it adds meaningful distinction.

---

# 31. Important Attempt Rule

Do not write:

```text
NO_ATTEMPT_DETECTED
```

if the microphone window never actually ran.

Use:

```text
NOT_MEASURED
```

or:

```text
MICROPHONE_UNAVAILABLE
```

as appropriate.

---

# 32. No Raw Child Audio Columns

The schema must contain no fields such as:

```text
raw_audio_blob
audio_file_path
speech_transcript
pronunciation_score
```

for ordinary child speaking attempts.

Prototype 0.1 stores only the attempt result state.

---

# 33. PairLearningState Entity

Recommended table:

```text
pair_learning_state
```

Purpose:

> cached/derived scheduling state for each concept-language pair.

Fields:

```text
pair_id TEXT PRIMARY KEY
learning_state TEXT NOT NULL
next_due_at_ms INTEGER NULL
rest_until_ms INTEGER NULL

valid_exposure_count INTEGER NOT NULL DEFAULT 0
session_exposure_count INTEGER NOT NULL DEFAULT 0
attempt_detected_count INTEGER NOT NULL DEFAULT 0
replay_count_total INTEGER NOT NULL DEFAULT 0

real_world_recognition_count INTEGER NOT NULL DEFAULT 0
real_world_use_count INTEGER NOT NULL DEFAULT 0

last_exposed_at_ms INTEGER NULL
last_attempt_at_ms INTEGER NULL
last_real_world_event_at_ms INTEGER NULL

low_interaction_encounter_count INTEGER NOT NULL DEFAULT 0
rest_count INTEGER NOT NULL DEFAULT 0

updated_at_ms INTEGER NOT NULL
state_version INTEGER NOT NULL DEFAULT 1
```

---

# 34. Learning State Values

Stable values:

```text
NEW
EMERGING
FAMILIAR
RESTING
```

`Due` remains a time-derived property rather than a separate exclusive state.

---

# 35. PairLearningState Is Derived

This table is a performance/convenience projection.

The authoritative evidence remains:

- interaction history;
- real-world observations.

If a state row is inconsistent, it should be possible to rebuild or validate it from evidence.

Prototype 0.1 does not need a full event-sourcing framework.

---

# 36. Rebuild Capability

A development/debug routine should be able to recalculate:

```text
pair_learning_state
```

from relevant persisted evidence.

This is useful for:

- algorithm changes;
- bug repair;
- tests.

---

# 37. State Version

`state_version` identifies the adaptive derivation logic version.

Example:

```text
1
```

If future scheduling logic changes materially, the app can:

- recompute;
- migrate;
- compare.

---

# 38. Valid Exposure Count

Increment only when:

```text
interaction.exposure_committed = true
```

Do not count:

- planned-but-unseen items;
- failed audio startup;
- orientation recreation.

---

# 39. Session Exposure Count

Represents exposure across distinct sessions if the adaptive engine needs this value.

Alternatively this may be derived through queries rather than persisted.

If it proves redundant, remove it in a later schema revision.

Prototype 0.1 should favor correctness over denormalization.

---

# 40. RealWorldObservation Entity

Recommended table:

```text
real_world_observation
```

Fields:

```text
observation_id TEXT PRIMARY KEY
pair_id TEXT NOT NULL
observation_type TEXT NOT NULL
recorded_at_ms INTEGER NOT NULL
observed_at_ms INTEGER NULL
context TEXT NULL
source TEXT NOT NULL DEFAULT 'PARENT_REPORT'
```

---

# 41. Observation Type

Prototype 0.1 values:

```text
RECOGNIZED_OUTSIDE_APP
USED_OUTSIDE_APP
```

Potential future expansion:

```text
USED_AFTER_PROMPT
USED_SPONTANEOUSLY
```

Do not add future granularity unless the parent UX actually captures it.

---

# 42. Observation Source

Store:

```text
PARENT_REPORT
```

to make evidence provenance explicit.

This prevents later code from treating the event as objectively device-verified.

---

# 43. Observation Pair Specificity

Every observation references:

```text
pair_id
```

not just:

```text
concept_id
```

This ensures:

```text
car + Tamil
```

does not automatically become:

```text
car + English
```

evidence.

---

# 44. Observation Duplicate Protection

A parent may legitimately report multiple events for the same pair over time.

Therefore do **not** use:

```text
UNIQUE(pair_id, observation_type)
```

But rapid accidental duplicate taps should be prevented at command/UI level.

Optional repository-level short-window duplicate suppression may be added if testing shows need.

---

# 45. FamilyRecording Entity

Recommended table:

```text
family_recording
```

Fields:

```text
recording_id TEXT PRIMARY KEY
pair_id TEXT NOT NULL UNIQUE
logical_file_id TEXT NOT NULL UNIQUE
created_at_ms INTEGER NOT NULL
updated_at_ms INTEGER NOT NULL
duration_ms INTEGER NULL
format TEXT NULL
status TEXT NOT NULL
```

---

# 46. Family Recording Status

Recommended:

```text
ACTIVE
REPLACED
DELETED
```

Prototype 0.1 can physically delete replaced/deleted files while retaining minimal metadata only if needed.

A simpler first implementation may keep only one active row per pair and delete the row on deletion.

---

# 47. Family Recording File Reference

Store a logical file identifier, not a raw absolute path.

Example:

```text
family_audio_7e2f...
```

`FileStore` resolves this to the platform-specific app-private location.

This improves portability.

---

# 48. Family Recording Privacy

Do not store family recording files in:

- shared/public media storage;
- external downloads;
- cloud-backed shared folder.

Use app-private storage.

---

# 49. Temporary Recording Files

Temporary parent recordings should not be represented as permanent database records until saved.

Flow:

```text
record temp file
→ preview
→ save
→ create/update FamilyRecording row
```

On cancel:

```text
delete temp file
```

---

# 50. Settings Boundary

Settings such as:

```text
setupComplete
startingLanguage
microphoneAttemptDetectionEnabled
```

should live in DataStore or equivalent.

They do not need relational rows.

---

# 51. Why Settings Are Separate

Keeping small preferences separate avoids turning SQLite into a generic key-value store.

Structured learning history remains relational.

---

# 52. Content Manifest and Database

Canonical content may be supplied from a bundled manifest.

Recommended architecture:

```text
bundled content manifest
    ↓
ContentRepository
```

The database only needs content tables if doing so simplifies relational joins and validation.

For Prototype 0.1, two acceptable strategies exist.

---

# 53. Content Strategy A — Seed Content Tables

On first run / migration:

- insert Concept;
- insert Language;
- insert ConceptLanguagePair.

Advantages:

- strong foreign keys;
- easy joins;
- database self-contained for debugging.

Recommended for Prototype 0.1.

---

# 54. Content Strategy B — Manifest-Only Static Content

Store only stable pair IDs in learning rows and resolve metadata through manifest.

Advantages:

- less database duplication.

Disadvantages:

- weaker SQLite foreign-key enforcement against content identity.

Because Prototype 0.1 has only 16 pairs, Strategy A is preferred.

---

# 55. Seed Idempotency

Content seeding must be idempotent.

Running initialization twice must not duplicate:

- concepts;
- languages;
- pairs.

Use stable primary keys + upsert/migration logic.

---

# 56. Foreign Keys

Recommended foreign-key enforcement:

```text
concept_language_pair.concept_id → concept.concept_id
concept_language_pair.language_id → language.language_id
interaction.session_id → session.session_id
interaction.pair_id → concept_language_pair.pair_id
pair_learning_state.pair_id → concept_language_pair.pair_id
real_world_observation.pair_id → concept_language_pair.pair_id
family_recording.pair_id → concept_language_pair.pair_id
```

SQLite foreign-key enforcement should be enabled.

---

# 57. Delete Behavior

Normal product flow should not delete historical interactions individually.

Recommended cascade behavior for full reset:

```text
delete sessions
→ cascade interactions
```

For content tables:

- avoid cascading content deletion into evidence accidentally;
- content updates should use migrations/versioning.

---

# 58. Recommended Indexes

## Interaction indexes

```text
INDEX interaction_session_idx(session_id)
INDEX interaction_pair_idx(pair_id)
INDEX interaction_pair_exposed_idx(pair_id, exposure_committed)
INDEX interaction_completed_idx(completed_at_ms)
```

## Session indexes

```text
INDEX session_started_idx(started_at_ms)
```

## Observation indexes

```text
INDEX observation_pair_idx(pair_id)
INDEX observation_recorded_idx(recorded_at_ms)
```

## Learning state

Primary key on:

```text
pair_id
```

is sufficient for direct lookup.

---

# 59. Query Patterns to Optimize

The database should efficiently support:

```text
get current state for all 16 pairs
get recent interactions for one pair
get recent session interactions
get latest real-world observations
get latest meaningful session
get active family recording for a pair
```

There are only 16 prototype pairs, so avoid premature complex indexing.

---

# 60. Transaction — Commit Interaction Completion

When finalizing a scheduled interaction, transactionally:

1. update interaction completion fields;
2. ensure exposure state is correct;
3. update pair learning summary;
4. persist any attempt/replay totals required.

The exact split depends on repository implementation.

---

# 61. Transaction — Real-World Observation

When parent saves observation:

1. insert `real_world_observation`;
2. update/recompute `pair_learning_state`;
3. commit together where feasible.

This prevents saved evidence and adaptive state from diverging.

---

# 62. Transaction — Reset

Reset should transactionally clear structured learning data.

Candidate order:

```text
interaction
session
real_world_observation
pair_learning_state
family_recording metadata
```

Then separately delete family audio files through `FileStore`.

If file deletion partially fails, report the failure and retry/repair safely.

---

# 63. Reset Scope

Prototype 0.1 reset should remove:

- session history;
- interactions;
- derived adaptive state;
- parent real-world observations;
- family recording metadata;
- family recording files;
- selected DataStore preferences according to reset UX.

Keep:

- bundled canonical content;
- app binaries;
- schema migrations.

---

# 64. Reset and Content Tables

If Concept/Language/Pair rows are seeded canonical content:

Option A:

- retain them during reset.

Preferred.

This makes reset a learning/user-data reset, not a database reinstallation.

---

# 65. Database Migrations

Every schema change after initial persisted use must have an explicit migration.

Do not use destructive migration in family-test/release builds unless data loss is intentionally approved.

---

# 66. Migration Versioning

Room schema version:

```text
1
```

for Prototype 0.1 initial schema.

Future migrations:

```text
1 → 2
2 → 3
```

must be tested.

---

# 67. Schema Export

If using Room:

- export schema definitions into version control;
- review diffs;
- use them in migration testing.

This is especially useful with KMP/Room changes.

---

# 68. Migration Test Requirements

Tests should verify:

- old schema opens;
- migration completes;
- session/evidence rows survive;
- pair IDs remain valid;
- family recording metadata survives when intended;
- derived state is rebuilt if schema changes require it.

---

# 69. Data Corruption Recovery

If the database cannot be opened:

Prototype 0.1 should not silently delete it immediately.

Recommended:

1. detect failure;
2. enter parent-facing recovery path;
3. attempt known safe recovery if supported;
4. allow explicit reset as last resort.

---

# 70. Referential Integrity Check

Development builds may expose a diagnostic that verifies:

- every interaction pair exists;
- every observation pair exists;
- every family recording pair exists;
- every learning-state pair exists;
- no duplicate `(session_id, sequence_index)` values exist.

---

# 71. Derived State Consistency Check

Development/debug tooling may compare:

```text
stored pair_learning_state
```

against:

```text
state recomputed from evidence
```

and flag divergence.

This is valuable while tuning the adaptive engine.

---

# 72. Session Recovery

If process death occurs mid-session:

- committed interactions remain;
- incomplete interaction may remain with `completed_at_ms = NULL`;
- session may remain with `ended_at_ms = NULL`.

On next launch, recovery can mark such records:

```text
completion_reason = PROCESS_INTERRUPTION
```

or session:

```text
completion_reason = LONG_INTERRUPTION / APP_EXITED
```

according to recovery logic.

---

# 73. Incomplete Interaction Handling

Incomplete rows must not automatically count as exposures.

If:

```text
exposure_committed = true
```

then exposure remains valid even if interaction completion was interrupted afterward.

This preserves factual evidence.

---

# 74. Content Version on Session

Every session should retain:

```text
content_version_id
```

so later content changes do not obscure what the child actually encountered.

---

# 75. Adaptive Config Version

Recommended addition to Session or PairLearningState:

```text
adaptive_config_version TEXT
```

This helps compare family-test behavior if algorithm constants change.

Prototype 0.1 should freeze config during the 7-day test.

---

# 76. Recommended Session Field Addition

Recommended final Session fields:

```text
adaptive_config_version TEXT NOT NULL
```

Example:

```text
adaptive-v0.1
```

---

# 77. Why Config Version Matters

If Prototype 0.2 changes:

- spacing intervals;
- rest thresholds;
- new-item cap;

historical sessions remain interpretable.

---

# 78. Real-World Suggestion Persistence

The session may store:

```text
generated_suggestion_id
```

This references a bundled suggestion definition.

Do not duplicate the full suggestion text in every row unless later requirements justify preserving historical copy.

---

# 79. Suggestion Versioning

Bundled suggestion logical IDs should be versioned/stable enough that:

```text
rw.apple.snack.ta.v1
```

can be resolved historically.

If suggestion text changes materially, use a new versioned ID.

---

# 80. Privacy-Minimal Schema

Prototype 0.1 does not need database fields for:

- child name;
- birthday;
- email;
- parent name;
- address;
- GPS;
- photo;
- device contacts;
- account ID.

One local learner context is assumed.

---

# 81. No Analytics Event Table

Do not create a generic table such as:

```text
analytics_event
```

for arbitrary UI telemetry.

Only persist data that serves:

- learning behavior;
- family transfer;
- app reliability;
- explicit local diagnostics where justified.

---

# 82. No Raw Error Log Table by Default

Technical logs should not automatically accumulate indefinitely in the learning database.

Development diagnostics can use local logging mechanisms with retention limits.

---

# 83. Example Initial Schema — Concept

```sql
CREATE TABLE concept (
    concept_id TEXT PRIMARY KEY NOT NULL,
    category TEXT NOT NULL,
    primary_visual_asset_id TEXT NOT NULL,
    movement_eligible INTEGER NOT NULL,
    content_version_id TEXT NOT NULL,
    is_enabled INTEGER NOT NULL
);
```

---

# 84. Example Initial Schema — Language

```sql
CREATE TABLE language (
    language_id TEXT PRIMARY KEY NOT NULL,
    language_code TEXT NOT NULL UNIQUE,
    display_name TEXT NOT NULL,
    is_enabled INTEGER NOT NULL
);
```

---

# 85. Example Initial Schema — ConceptLanguagePair

```sql
CREATE TABLE concept_language_pair (
    pair_id TEXT PRIMARY KEY NOT NULL,
    concept_id TEXT NOT NULL,
    language_id TEXT NOT NULL,
    spoken_form TEXT NOT NULL,
    canonical_audio_asset_id TEXT NOT NULL,
    review_status TEXT NOT NULL,
    content_version_id TEXT NOT NULL,
    is_enabled INTEGER NOT NULL,
    UNIQUE(concept_id, language_id),
    FOREIGN KEY(concept_id) REFERENCES concept(concept_id),
    FOREIGN KEY(language_id) REFERENCES language(language_id)
);
```

---

# 86. Example Initial Schema — Session

```sql
CREATE TABLE session (
    session_id TEXT PRIMARY KEY NOT NULL,
    started_at_ms INTEGER NOT NULL,
    ended_at_ms INTEGER,
    starting_language_id TEXT,
    completion_reason TEXT,
    content_version_id TEXT NOT NULL,
    adaptive_config_version TEXT NOT NULL,
    meaningful_activity INTEGER NOT NULL DEFAULT 0,
    generated_suggestion_id TEXT
);
```

---

# 87. Example Initial Schema — Interaction

```sql
CREATE TABLE interaction (
    interaction_id TEXT PRIMARY KEY NOT NULL,
    session_id TEXT NOT NULL,
    pair_id TEXT NOT NULL,
    sequence_index INTEGER NOT NULL,
    selection_reason TEXT NOT NULL,
    started_at_ms INTEGER NOT NULL,
    completed_at_ms INTEGER,
    completion_reason TEXT,
    exposure_committed INTEGER NOT NULL DEFAULT 0,
    exposure_committed_at_ms INTEGER,
    replay_count INTEGER NOT NULL DEFAULT 0,
    attempt_state TEXT NOT NULL DEFAULT 'NOT_MEASURED',
    attempt_recorded_at_ms INTEGER,
    planned INTEGER NOT NULL DEFAULT 1,
    UNIQUE(session_id, sequence_index),
    FOREIGN KEY(session_id) REFERENCES session(session_id) ON DELETE CASCADE,
    FOREIGN KEY(pair_id) REFERENCES concept_language_pair(pair_id)
);
```

---

# 88. Example Initial Schema — PairLearningState

```sql
CREATE TABLE pair_learning_state (
    pair_id TEXT PRIMARY KEY NOT NULL,
    learning_state TEXT NOT NULL,
    next_due_at_ms INTEGER,
    rest_until_ms INTEGER,
    valid_exposure_count INTEGER NOT NULL DEFAULT 0,
    session_exposure_count INTEGER NOT NULL DEFAULT 0,
    attempt_detected_count INTEGER NOT NULL DEFAULT 0,
    replay_count_total INTEGER NOT NULL DEFAULT 0,
    real_world_recognition_count INTEGER NOT NULL DEFAULT 0,
    real_world_use_count INTEGER NOT NULL DEFAULT 0,
    last_exposed_at_ms INTEGER,
    last_attempt_at_ms INTEGER,
    last_real_world_event_at_ms INTEGER,
    low_interaction_encounter_count INTEGER NOT NULL DEFAULT 0,
    rest_count INTEGER NOT NULL DEFAULT 0,
    updated_at_ms INTEGER NOT NULL,
    state_version INTEGER NOT NULL DEFAULT 1,
    FOREIGN KEY(pair_id) REFERENCES concept_language_pair(pair_id)
);
```

---

# 89. Example Initial Schema — RealWorldObservation

```sql
CREATE TABLE real_world_observation (
    observation_id TEXT PRIMARY KEY NOT NULL,
    pair_id TEXT NOT NULL,
    observation_type TEXT NOT NULL,
    recorded_at_ms INTEGER NOT NULL,
    observed_at_ms INTEGER,
    context TEXT,
    source TEXT NOT NULL DEFAULT 'PARENT_REPORT',
    FOREIGN KEY(pair_id) REFERENCES concept_language_pair(pair_id)
);
```

---

# 90. Example Initial Schema — FamilyRecording

```sql
CREATE TABLE family_recording (
    recording_id TEXT PRIMARY KEY NOT NULL,
    pair_id TEXT NOT NULL UNIQUE,
    logical_file_id TEXT NOT NULL UNIQUE,
    created_at_ms INTEGER NOT NULL,
    updated_at_ms INTEGER NOT NULL,
    duration_ms INTEGER,
    format TEXT,
    status TEXT NOT NULL,
    FOREIGN KEY(pair_id) REFERENCES concept_language_pair(pair_id)
);
```

---

# 91. Room Entity Mapping

If Room KMP is used:

- each table maps to a Room entity;
- DAO interfaces remain in data layer;
- domain layer receives mapped domain models;
- Room annotations must not leak into core domain classes unless a deliberate shared-model simplification is chosen.

Prefer separation where it improves clarity.

---

# 92. DAO Responsibilities

Suggested DAOs:

```text
ContentDao
SessionDao
InteractionDao
PairLearningStateDao
RealWorldObservationDao
FamilyRecordingDao
```

A single DAO per table is not mandatory.

Group by transactional behavior if that produces a cleaner repository.

---

# 93. Repository Mapping

Recommended:

```text
LearningRepository
  → InteractionDao + PairLearningStateDao

SessionRepository
  → SessionDao + InteractionDao

ContentRepository
  → ContentDao + bundled assets

RealWorldObservationRepository
  → RealWorldObservationDao + PairLearningStateDao

FamilyRecordingRepository
  → FamilyRecordingDao + FileStore
```

---

# 94. Write Discipline

All writes should go through repositories/use cases.

UI code must not call DAO methods directly.

---

# 95. Database Threading

Database work must not block the rendering/UI thread.

Room/coroutine integration should be used appropriately.

---

# 96. Database Encryption

Prototype 0.1 does not require custom encrypted SQLite by default.

Reasons:

- data is low-sensitivity learning evidence;
- Android/iOS app sandboxing already provides platform protection;
- encryption introduces complexity/key management.

However:

- family recordings remain app-private;
- no secrets are stored;
- threat model may revisit this if risk changes.

---

# 97. Backup Consideration

Prototype 0.1 should decide explicitly whether local app data participates in OS backup.

Because the product promise is local-only, OS backup behavior must be documented rather than assumed.

`privacy.md` and `platform-support.md` should decide whether:

- learning data may be device-backup eligible;
- family recordings are excluded from backup.

---

# 98. Export

Prototype 0.1 does not require:

- CSV export;
- cloud export;
- parent data download workflow.

A future parent-controlled export could be added if needed.

---

# 99. Database Acceptance Criteria

Before family testing:

- [ ] all 8 concepts seed correctly;
- [ ] both languages seed correctly;
- [ ] exactly 16 active pairs exist;
- [ ] session can be created;
- [ ] interaction can be created;
- [ ] exposure commit is idempotent;
- [ ] replay count updates correctly;
- [ ] attempt states persist;
- [ ] orientation does not duplicate rows;
- [ ] pair state updates transactionally;
- [ ] recognition/use observation persists;
- [ ] family recording metadata persists;
- [ ] restart preserves learning data;
- [ ] reset clears user learning data;
- [ ] canonical content survives reset;
- [ ] foreign keys remain valid;
- [ ] migration tests pass.

---

# 100. Database Test Cases

At minimum:

1. first-run seed;
2. seed executed twice;
3. create/complete session;
4. planned interaction without exposure;
5. commit exposure twice;
6. replay increment;
7. attempt detected;
8. no attempt;
9. mic unavailable;
10. session interrupted after exposure;
11. observation recognition;
12. observation use;
13. observation adaptive update;
14. family recording insert;
15. family recording replace;
16. family recording delete;
17. reset;
18. process-death recovery;
19. content-version migration;
20. schema migration.

---

# 101. Database Invariants

### DB-INV-001
Exactly one concept row exists per stable concept ID.

### DB-INV-002
Exactly one language row exists per supported language ID.

### DB-INV-003
At most one active pair exists per `(concept_id, language_id)`.

### DB-INV-004
Every interaction belongs to one session.

### DB-INV-005
Every interaction references one concept-language pair.

### DB-INV-006
A scheduled interaction commits exposure at most once.

### DB-INV-007
Replay count does not create new scheduled interactions.

### DB-INV-008
`NO_ATTEMPT_DETECTED` is stored only after a real measurement window.

### DB-INV-009
No raw child speech is stored in SQLite.

### DB-INV-010
Every real-world observation is pair-specific.

### DB-INV-011
Every real-world observation is explicitly parent-reported.

### DB-INV-012
At most one active family recording exists per pair.

### DB-INV-013
Family recording bytes are not stored as SQLite blobs.

### DB-INV-014
Derived learning state can be validated/rebuilt from evidence.

### DB-INV-015
Orientation/recomposition creates no database learning row by itself.

### DB-INV-016
Reset preserves canonical bundled content.

### DB-INV-017
Persistent enums are stored by stable semantic value, not ordinal.

---

# 102. Open Database Decisions

Before implementation completes, confirm:

1. exact Room KMP version;
2. exact UUID strategy;
3. whether content tables are seeded vs partially manifest-backed;
4. whether `session_exposure_count` is persisted or derived;
5. whether incomplete interactions are retained indefinitely;
6. OS backup behavior;
7. whether family recording deletion retains tombstone metadata;
8. exact reset preference scope.

None of these block initial schema implementation.

---

# 103. Relationship to `architecture.md`

`architecture.md` defines:

- repositories;
- local source of truth;
- service boundaries.

This document defines the concrete relational persistence behind those repositories.

---

# 104. Relationship to `adaptive-engine.md`

`adaptive-engine.md` owns the rules.

The database only stores:

- evidence;
- current derived state;
- next due/rest timing;
- config/state versions.

Do not embed adaptive formulas in SQL triggers.

---

# 105. Relationship to `interaction-states.md`

The interaction state machine determines:

- when exposure commits;
- when replay increments;
- when attempt state becomes final;
- when interaction/session completes.

Database writes must follow those boundaries.

---

# 106. Relationship to `api.md`

`api.md` should define repository/service methods such as:

```text
createSession()
createInteraction()
commitExposure()
incrementReplay()
recordAttempt()
completeInteraction()
recordRealWorldObservation()
getPairLearningState()
saveFamilyRecordingMetadata()
resetLearningData()
```

---

# 107. Relationship to `privacy.md`

`privacy.md` must describe every persistent category defined here.

There should be no undocumented hidden data table.

---

# 108. Decision Summary

Miozira Prototype 0.1 will use a small Room/SQLite relational database centered on:

```text
Concept
Language
ConceptLanguagePair
Session
Interaction
PairLearningState
RealWorldObservation
FamilyRecording
```

Core rules:

- interactions are the scheduled-learning evidence boundary;
- exposure is idempotent per interaction;
- replay is stored separately from scheduled exposure;
- speaking attempts store state only, never raw speech;
- real-world evidence is pair-specific and parent-reported;
- adaptive state is a rebuildable projection;
- family audio bytes remain in private files;
- small settings remain in DataStore;
- canonical content is seeded/versioned locally;
- schema migrations are explicit;
- reset removes user learning data while preserving canonical content.

This schema is intentionally small enough to reason about manually while still being robust enough for the seven-day family test.
