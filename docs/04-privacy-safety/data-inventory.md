# Miozira — Data Inventory

**Document:** `data-inventory.md`  
**Version:** 0.1  
**Status:** Draft for Prototype 0.1  
**Product:** Miozira  
**Primary platform:** Android tablet  
**Architecture:** Offline-first, local-only learning data  
**Last updated:** 2026-08-25

---

## 1. Purpose

This document inventories every data element Miozira Prototype 0.1 is expected to create, read, derive, store, or process.

For each data element it records:

- what it is;
- why it exists;
- who/what creates it;
- whether it is persistent or transient;
- where it is stored;
- its sensitivity;
- whether it is backup-eligible;
- retention;
- deletion path;
- whether it may leave the device.

This document is the implementation-level companion to:

- `privacy.md`;
- `security.md`;
- `database.md`;
- `local-storage.md`.

The goal is:

> **Miozira should be able to explain why every piece of data exists and how to remove it.**

---

# 2. Data Classification

Prototype 0.1 uses four practical sensitivity levels.

## D0 — Public / bundled product data

Examples:

- concept IDs;
- canonical word text;
- bundled audio/visual asset IDs.

No user-specific sensitivity.

---

## D1 — Local app-operational data

Examples:

- settings;
- content version;
- session IDs.

Low sensitivity, but still app-private.

---

## D2 — Local child-learning evidence

Examples:

- exposure history;
- speaking-attempt state;
- real-world observations.

Potentially sensitive because it reflects a child's learning activity.

---

## D3 — Identifiable family media

Example:

- family voice recording file.

Higher sensitivity because voice can identify a person.

---

# 3. External Transmission Classification

Each item is assigned one of:

```text
NEVER
OS_CONTROLLED_ONLY
FUTURE_REVIEW_REQUIRED
```

### NEVER
Miozira itself must not transmit it externally.

### OS_CONTROLLED_ONLY
The app does not transmit it, but OS backup/migration behavior may create a copy depending on platform configuration.

### FUTURE_REVIEW_REQUIRED
Not used in Prototype 0.1; any future transmission requires a new privacy/security review.

---

# 4. Canonical Content Data

## 4.1 Concept ID

Example:

```text
apple
ball
cup
```

**Purpose:** stable concept identity.  
**Source:** bundled Miozira content.  
**Persistence:** persistent.  
**Storage:** bundled manifest and/or Room seed table.  
**Sensitivity:** D0.  
**Backup:** irrelevant / rebuildable from app bundle.  
**Retention:** lifetime of installed content version.  
**Deletion:** removed only by app/content update.  
**External transmission:** NEVER required.

---

## 4.2 Concept display/semantic metadata

Examples:

- category;
- sort/order metadata;
- enabled flag.

**Purpose:** content organization and runtime selection.  
**Source:** bundled content.  
**Persistence:** persistent.  
**Storage:** bundle / Room seed.  
**Sensitivity:** D0.  
**Backup:** rebuildable.  
**Retention:** content lifetime.  
**Deletion:** content migration/update.  
**External transmission:** NEVER.

---

## 4.3 Language ID

Prototype values:

```text
en
ta
```

**Purpose:** language identity.  
**Source:** bundled content.  
**Persistence:** persistent.  
**Storage:** bundle / Room.  
**Sensitivity:** D0.  
**Backup:** rebuildable.  
**Retention:** content lifetime.  
**Deletion:** content migration/update.  
**External transmission:** NEVER.

---

## 4.4 Concept-Language Pair ID

Example:

```text
pair.apple.en
pair.apple.ta
```

**Purpose:** stable learning unit identity.  
**Source:** bundled content.  
**Persistence:** persistent.  
**Storage:** Room seed / manifest.  
**Sensitivity:** D0.  
**Backup:** rebuildable.  
**Retention:** content lifetime.  
**Deletion:** content migration/update.  
**External transmission:** NEVER.

---

## 4.5 Canonical spoken form

Examples:

```text
apple
ஆப்பிள்
```

**Purpose:** content review/reference.  
**Source:** curated content.  
**Persistence:** persistent.  
**Storage:** manifest / Room.  
**Sensitivity:** D0.  
**Backup:** rebuildable.  
**Retention:** content version lifetime.  
**Deletion:** content update.  
**External transmission:** NEVER required.

---

## 4.6 Canonical visual asset ID

Example:

```text
visual.apple.v1
```

**Purpose:** resolve concept image.  
**Source:** bundled content.  
**Persistence:** persistent.  
**Storage:** manifest / asset bundle.  
**Sensitivity:** D0.  
**Backup:** not needed.  
**Retention:** content version lifetime.  
**Deletion:** app update.  
**External transmission:** NEVER.

---

## 4.7 Canonical audio asset ID

Example:

```text
audio.word.apple.ta.v1
```

**Purpose:** resolve standard word recording.  
**Source:** bundled content.  
**Persistence:** persistent.  
**Storage:** manifest / bundle.  
**Sensitivity:** D0.  
**Backup:** not needed.  
**Retention:** content version lifetime.  
**Deletion:** app update.  
**External transmission:** NEVER.

---

## 4.8 Canonical audio bytes

**Purpose:** word playback.  
**Source:** bundled app asset.  
**Persistence:** persistent, immutable.  
**Storage:** application package/resources.  
**Sensitivity:** D0.  
**Backup:** app-store/app-package managed, not user backup data.  
**Retention:** installed app lifetime.  
**Deletion:** uninstall/update.  
**External transmission:** NEVER by runtime.

---

## 4.9 Canonical visual bytes

**Purpose:** child concept display.  
**Source:** bundled asset.  
**Persistence:** persistent, immutable.  
**Storage:** application package/resources.  
**Sensitivity:** D0.  
**Backup:** not user data.  
**Retention:** installed app lifetime.  
**Deletion:** uninstall/update.  
**External transmission:** NEVER.

---

## 4.10 Real-world suggestion content

Example:

```text
“Notice the car when you go outside.”
```

**Purpose:** parent-led transfer prompt.  
**Source:** curated content.  
**Persistence:** persistent.  
**Storage:** bundled content / Room.  
**Sensitivity:** D0.  
**Backup:** rebuildable.  
**Retention:** content lifetime.  
**Deletion:** content update.  
**External transmission:** NEVER.

---

# 5. Content / Build Version Data

## 5.1 Content version

Example:

```text
prototype-0.1-content-v1
```

**Purpose:** interpret historical sessions against content.  
**Source:** app build/content pack.  
**Persistence:** persistent.  
**Storage:** Room/session records + manifest.  
**Sensitivity:** D1.  
**Backup:** may follow DB backup policy.  
**Retention:** with historical sessions.  
**Deletion:** reset/uninstall for session copy; bundle copy persists.  
**External transmission:** NEVER.

---

## 5.2 Adaptive config version

Example:

```text
adaptive-v0.1
```

**Purpose:** identify scheduler rules used during a session.  
**Source:** app configuration.  
**Persistence:** persistent with session.  
**Storage:** Room.  
**Sensitivity:** D1.  
**Backup:** may follow DB policy.  
**Retention:** historical session lifetime.  
**Deletion:** reset/uninstall.  
**External transmission:** NEVER.

---

## 5.3 Database schema version

**Purpose:** migrations.  
**Source:** application.  
**Persistence:** persistent.  
**Storage:** SQLite metadata / Room schema.  
**Sensitivity:** D1.  
**Backup:** follows DB.  
**Retention:** database lifetime.  
**Deletion:** uninstall/reset may recreate.  
**External transmission:** NEVER.

---

# 6. Session Data

## 6.1 Session ID

Format:

```text
UUID string
```

**Purpose:** group interactions into one learning session.  
**Source:** app-generated.  
**Persistence:** persistent.  
**Storage:** Room.  
**Sensitivity:** D1.  
**Backup:** follows structured-data policy.  
**Retention:** until reset/uninstall.  
**Deletion:** parent reset or uninstall.  
**External transmission:** NEVER.

---

## 6.2 Session start timestamp

**Purpose:** scheduling/history.  
**Source:** device clock.  
**Persistence:** persistent.  
**Storage:** Room.  
**Sensitivity:** D2.  
**Backup:** follows DB policy.  
**Retention:** until reset/uninstall.  
**Deletion:** reset/uninstall.  
**External transmission:** NEVER.

---

## 6.3 Session end timestamp

**Purpose:** duration/history/recovery.  
**Source:** app/device clock.  
**Persistence:** persistent.  
**Storage:** Room.  
**Sensitivity:** D2.  
**Backup:** follows DB.  
**Retention:** until reset/uninstall.  
**Deletion:** reset/uninstall.  
**External transmission:** NEVER.

---

## 6.4 Session completion reason

Examples:

```text
PLANNED_END
INACTIVE
PARENT_STOP
INTERRUPTED
```

**Purpose:** understand session lifecycle.  
**Source:** session engine.  
**Persistence:** persistent.  
**Storage:** Room.  
**Sensitivity:** D2.  
**Backup:** follows DB.  
**Retention:** until reset/uninstall.  
**Deletion:** reset/uninstall.  
**External transmission:** NEVER.

---

## 6.5 Starting language ID

**Purpose:** reproduce/interpret session context.  
**Source:** parent preference/session engine.  
**Persistence:** persistent.  
**Storage:** Room session record.  
**Sensitivity:** D1.  
**Backup:** follows DB.  
**Retention:** session lifetime.  
**Deletion:** reset/uninstall.  
**External transmission:** NEVER.

---

## 6.6 Meaningful activity flag

**Purpose:** decide whether to offer a real-world suggestion.  
**Source:** session engine.  
**Persistence:** persistent or derivable.  
**Storage:** Room if persisted.  
**Sensitivity:** D2.  
**Backup:** follows DB.  
**Retention:** session lifetime.  
**Deletion:** reset/uninstall.  
**External transmission:** NEVER.

---

## 6.7 Generated suggestion ID

**Purpose:** avoid repetitive suggestion selection.  
**Source:** transfer service.  
**Persistence:** optional persistent.  
**Storage:** Room session record.  
**Sensitivity:** D1.  
**Backup:** follows DB.  
**Retention:** session lifetime.  
**Deletion:** reset/uninstall.  
**External transmission:** NEVER.

---

# 7. Interaction Data

## 7.1 Interaction ID

**Purpose:** stable identity for one scheduled encounter and idempotency.  
**Source:** app-generated UUID.  
**Persistence:** persistent.  
**Storage:** Room.  
**Sensitivity:** D1.  
**Backup:** follows DB.  
**Retention:** until reset/uninstall.  
**Deletion:** reset/uninstall.  
**External transmission:** NEVER.

---

## 7.2 Pair ID

**Purpose:** identify which concept-language pair was presented.  
**Source:** adaptive engine.  
**Persistence:** persistent in interaction record.  
**Storage:** Room.  
**Sensitivity:** D2 in context because it reflects child exposure.  
**Backup:** follows DB.  
**Retention:** until reset/uninstall.  
**Deletion:** reset/uninstall.  
**External transmission:** NEVER.

---

## 7.3 Sequence index

**Purpose:** interaction ordering within session.  
**Source:** session engine.  
**Persistence:** persistent.  
**Storage:** Room.  
**Sensitivity:** D1.  
**Backup:** follows DB.  
**Retention:** until reset/uninstall.  
**Deletion:** reset/uninstall.  
**External transmission:** NEVER.

---

## 7.4 Selection reason

Examples:

```text
DUE_EMERGING
NEW_INTRODUCTION
REINTRODUCTION_AFTER_REST
```

**Purpose:** explain scheduler decision.  
**Source:** adaptive engine.  
**Persistence:** persistent.  
**Storage:** Room.  
**Sensitivity:** D2.  
**Backup:** follows DB.  
**Retention:** until reset/uninstall.  
**Deletion:** reset/uninstall.  
**External transmission:** NEVER.

---

## 7.5 Interaction start/end timestamps

**Purpose:** lifecycle/recovery.  
**Source:** device clock.  
**Persistence:** persistent.  
**Storage:** Room.  
**Sensitivity:** D2.  
**Backup:** follows DB.  
**Retention:** until reset/uninstall.  
**Deletion:** reset/uninstall.  
**External transmission:** NEVER.

---

## 7.6 Interaction completion reason

Examples:

```text
COMPLETED
INTERRUPTED
SKIPPED_AUDIO_FAILURE
SESSION_ENDED
```

**Purpose:** integrity/recovery.  
**Source:** controller/session engine.  
**Persistence:** persistent.  
**Storage:** Room.  
**Sensitivity:** D2.  
**Backup:** follows DB.  
**Retention:** until reset/uninstall.  
**Deletion:** reset/uninstall.  
**External transmission:** NEVER.

---

# 8. Exposure Data

## 8.1 Exposure committed flag

**Purpose:** record whether enough canonical audio was presented.  
**Source:** audio threshold + repository commit.  
**Persistence:** persistent.  
**Storage:** interaction row in Room.  
**Sensitivity:** D2.  
**Backup:** follows DB.  
**Retention:** until reset/uninstall.  
**Deletion:** reset/uninstall.  
**External transmission:** NEVER.

---

## 8.2 Exposure committed timestamp

**Purpose:** scheduling/history.  
**Source:** device clock.  
**Persistence:** persistent.  
**Storage:** Room.  
**Sensitivity:** D2.  
**Backup:** follows DB.  
**Retention:** until reset/uninstall.  
**Deletion:** reset/uninstall.  
**External transmission:** NEVER.

---

# 9. Replay Data

## 9.1 Replay count per interaction

**Purpose:** weak evidence of voluntary engagement.  
**Source:** child replay action.  
**Persistence:** persistent.  
**Storage:** Room.  
**Sensitivity:** D2.  
**Backup:** follows DB.  
**Retention:** until reset/uninstall.  
**Deletion:** reset/uninstall.  
**External transmission:** NEVER.

---

## 9.2 Replay total per pair

**Purpose:** derived adaptive state.  
**Source:** interaction evidence.  
**Persistence:** derived projection, optionally persisted.  
**Storage:** PairLearningState.  
**Sensitivity:** D2.  
**Backup:** follows DB.  
**Retention:** current state / rebuildable.  
**Deletion:** reset/uninstall.  
**External transmission:** NEVER.

---

# 10. Child Speaking-Attempt Data

## 10.1 Attempt state

Allowed values:

```text
ATTEMPT_DETECTED
NO_ATTEMPT_DETECTED
MICROPHONE_UNAVAILABLE
NOT_MEASURED
```

**Purpose:** weak participation evidence.  
**Source:** child detector / permission state.  
**Persistence:** persistent.  
**Storage:** interaction row.  
**Sensitivity:** D2.  
**Backup:** follows DB.  
**Retention:** until reset/uninstall.  
**Deletion:** reset/uninstall.  
**External transmission:** NEVER.

---

## 10.2 Attempt recorded timestamp

**Purpose:** lifecycle/history.  
**Source:** device clock.  
**Persistence:** persistent.  
**Storage:** Room.  
**Sensitivity:** D2.  
**Backup:** follows DB.  
**Retention:** until reset/uninstall.  
**Deletion:** reset/uninstall.  
**External transmission:** NEVER.

---

## 10.3 Raw child microphone PCM

**Purpose:** transient signal analysis only.  
**Source:** device microphone during explicit 2–3 second window.  
**Persistence:** **transient only**.  
**Storage:** memory buffer only.  
**Sensitivity:** D3-like raw voice sensitivity, but not persisted.  
**Backup:** never.  
**Retention:** milliseconds/seconds during processing, then discarded.  
**Deletion:** release/overwrite buffer immediately after use.  
**External transmission:** **NEVER**.

---

## 10.4 Frame-level RMS / energy

**Purpose:** local attempt detection.  
**Source:** transient PCM processing.  
**Persistence:** normally transient only.  
**Storage:** memory.  
**Sensitivity:** D2.  
**Backup:** never.  
**Retention:** current detection window only.  
**Deletion:** discard after decision.  
**External transmission:** NEVER.

---

## 10.5 Noise-floor estimate

**Purpose:** adapt detection threshold to room/device.  
**Source:** microphone signal.  
**Persistence:** transient for window/session unless implementation proves need otherwise.  
**Storage:** memory.  
**Sensitivity:** D1/D2.  
**Backup:** never.  
**Retention:** current detection window or short-lived process state.  
**Deletion:** discard after use.  
**External transmission:** NEVER.

---

## 10.6 Child speech transcript

**Status:** **PROHIBITED DATA ELEMENT**

Miozira must not create it in Prototype 0.1.

---

## 10.7 Pronunciation score

**Status:** **PROHIBITED DATA ELEMENT**

Miozira must not create it.

---

## 10.8 Voiceprint / biometric embedding

**Status:** **PROHIBITED DATA ELEMENT**

Miozira must not create it.

---

# 11. Pair Learning State

## 11.1 Learning state enum

Values:

```text
NEW
EMERGING
FAMILIAR
RESTING
```

**Purpose:** local adaptive scheduling.  
**Source:** adaptive engine.  
**Persistence:** persistent projection.  
**Storage:** Room.  
**Sensitivity:** D2.  
**Backup:** follows DB.  
**Retention:** current state / rebuildable.  
**Deletion:** reset/uninstall.  
**External transmission:** NEVER.

---

## 11.2 Next due timestamp

**Purpose:** spaced re-encounter scheduling.  
**Source:** adaptive engine.  
**Persistence:** persistent projection.  
**Storage:** Room.  
**Sensitivity:** D2.  
**Backup:** follows DB.  
**Retention:** current state.  
**Deletion:** reset/uninstall.  
**External transmission:** NEVER.

---

## 11.3 Rest-until timestamp

**Purpose:** avoid over-repetition after low engagement.  
**Source:** adaptive engine.  
**Persistence:** persistent projection.  
**Storage:** Room.  
**Sensitivity:** D2.  
**Backup:** follows DB.  
**Retention:** current state.  
**Deletion:** reset/uninstall.  
**External transmission:** NEVER.

---

## 11.4 Exposure count

**Purpose:** state derivation.  
**Source:** interaction evidence.  
**Persistence:** projection / derivable.  
**Storage:** Room.  
**Sensitivity:** D2.  
**Backup:** follows DB.  
**Retention:** current state.  
**Deletion:** reset/uninstall.  
**External transmission:** NEVER.

---

## 11.5 Attempt-detected count

**Purpose:** weak positive participation evidence.  
**Source:** interaction attempt states.  
**Persistence:** projection / derivable.  
**Storage:** Room.  
**Sensitivity:** D2.  
**Backup:** follows DB.  
**Retention:** current state.  
**Deletion:** reset/uninstall.  
**External transmission:** NEVER.

---

## 11.6 Low-interaction counter

**Purpose:** decide when to rest an item.  
**Source:** adaptive engine.  
**Persistence:** persistent projection.  
**Storage:** Room.  
**Sensitivity:** D2.  
**Backup:** follows DB.  
**Retention:** current state.  
**Deletion:** reset/uninstall.  
**External transmission:** NEVER.

---

## 11.7 Rest count

**Purpose:** extend later rest duration if needed.  
**Source:** adaptive engine.  
**Persistence:** persistent projection.  
**Storage:** Room.  
**Sensitivity:** D2.  
**Backup:** follows DB.  
**Retention:** current state.  
**Deletion:** reset/uninstall.  
**External transmission:** NEVER.

---

# 12. Real-World Observation Data

## 12.1 Observation ID

**Purpose:** idempotent observation identity.  
**Source:** app-generated UUID.  
**Persistence:** persistent.  
**Storage:** Room.  
**Sensitivity:** D1.  
**Backup:** follows DB.  
**Retention:** until reset/uninstall.  
**Deletion:** reset/uninstall.  
**External transmission:** NEVER.

---

## 12.2 Observation type

Values:

```text
RECOGNIZED_OUTSIDE_APP
USED_OUTSIDE_APP
```

**Purpose:** record transfer evidence.  
**Source:** parent.  
**Persistence:** persistent.  
**Storage:** Room.  
**Sensitivity:** D2.  
**Backup:** follows DB.  
**Retention:** until reset/uninstall.  
**Deletion:** reset/uninstall.  
**External transmission:** NEVER.

---

## 12.3 Observation pair ID

**Purpose:** associate observation with exact language form.  
**Source:** parent selection/context.  
**Persistence:** persistent.  
**Storage:** Room.  
**Sensitivity:** D2.  
**Backup:** follows DB.  
**Retention:** until reset/uninstall.  
**Deletion:** reset/uninstall.  
**External transmission:** NEVER.

---

## 12.4 Observation timestamp

**Purpose:** local history/adaptive weighting.  
**Source:** device clock.  
**Persistence:** persistent.  
**Storage:** Room.  
**Sensitivity:** D2.  
**Backup:** follows DB.  
**Retention:** until reset/uninstall.  
**Deletion:** reset/uninstall.  
**External transmission:** NEVER.

---

## 12.5 Observation provenance

Example:

```text
PARENT_REPORT
```

**Purpose:** distinguish evidence source.  
**Source:** application.  
**Persistence:** persistent.  
**Storage:** Room.  
**Sensitivity:** D1/D2.  
**Backup:** follows DB.  
**Retention:** until reset/uninstall.  
**Deletion:** reset/uninstall.  
**External transmission:** NEVER.

---

## 12.6 Free-text observation note

**Status:** NOT USED in Prototype 0.1.

Reason:

- unnecessary personal context;
- higher privacy burden.

---

## 12.7 Observation location

**Status:** PROHIBITED / NOT USED.

---

## 12.8 Observation photo/video

**Status:** PROHIBITED / NOT USED.

---

# 13. Settings Data

## 13.1 Setup complete

**Purpose:** first-run behavior.  
**Source:** parent onboarding.  
**Persistence:** persistent.  
**Storage:** DataStore.  
**Sensitivity:** D1.  
**Backup:** depends on settings backup policy.  
**Retention:** until reset/uninstall.  
**Deletion:** reset/uninstall.  
**External transmission:** NEVER.

---

## 13.2 Starting language preference

**Purpose:** choose session opener/language block.  
**Source:** parent.  
**Persistence:** persistent.  
**Storage:** DataStore.  
**Sensitivity:** D1.  
**Backup:** settings backup policy.  
**Retention:** until changed/reset/uninstall.  
**Deletion:** reset/uninstall.  
**External transmission:** NEVER.

---

## 13.3 Speech-attempt detection enabled

**Purpose:** product-level microphone feature gate.  
**Source:** parent.  
**Persistence:** persistent.  
**Storage:** DataStore.  
**Sensitivity:** D1.  
**Backup:** settings backup policy.  
**Retention:** until changed/reset/uninstall.  
**Deletion:** reset/uninstall.  
**External transmission:** NEVER.

---

## 13.4 OS microphone permission state

**Purpose:** determine current capability.  
**Source:** Android OS.  
**Persistence:** owned by OS, not authoritative in Miozira storage.  
**Storage:** OS permission subsystem.  
**Sensitivity:** D1.  
**Backup:** OS-controlled.  
**Retention:** OS/app lifecycle.  
**Deletion:** user/OS/uninstall.  
**External transmission:** not a Miozira transmission.

---

# 14. Family Recording Data

## 14.1 Recording ID

**Purpose:** stable metadata identity.  
**Source:** app-generated UUID.  
**Persistence:** persistent.  
**Storage:** Room.  
**Sensitivity:** D1.  
**Backup:** metadata follows DB policy.  
**Retention:** until delete/reset/uninstall.  
**Deletion:** parent delete/reset/uninstall.  
**External transmission:** NEVER.

---

## 14.2 Pair ID for recording

**Purpose:** associate family voice with concept-language pair.  
**Source:** parent recording context.  
**Persistence:** persistent.  
**Storage:** Room.  
**Sensitivity:** D2.  
**Backup:** follows metadata DB policy.  
**Retention:** until delete/reset/uninstall.  
**Deletion:** delete/reset/uninstall.  
**External transmission:** NEVER.

---

## 14.3 Logical file ID

**Purpose:** reference private file without exposing path.  
**Source:** FileStore.  
**Persistence:** persistent.  
**Storage:** Room.  
**Sensitivity:** D1.  
**Backup:** follows DB policy.  
**Retention:** file lifetime.  
**Deletion:** delete/reset/uninstall.  
**External transmission:** NEVER.

---

## 14.4 Family recording audio bytes

**Purpose:** optional family voice playback.  
**Source:** adult microphone recording.  
**Persistence:** persistent.  
**Storage:** app-private file storage.  
**Sensitivity:** **D3**.  
**Backup:** recommended **excluded from automatic backup**.  
**Retention:** until parent deletes/replaces/resets/uninstalls.  
**Deletion:** individual delete, replacement, reset, uninstall.  
**External transmission:** **NEVER** by Miozira runtime.

---

## 14.5 Family recording duration

**Purpose:** validation/playback metadata.  
**Source:** recording service.  
**Persistence:** persistent.  
**Storage:** Room.  
**Sensitivity:** D1.  
**Backup:** follows DB policy.  
**Retention:** recording lifetime.  
**Deletion:** delete/reset/uninstall.  
**External transmission:** NEVER.

---

## 14.6 Family recording format/codec

**Purpose:** playback compatibility.  
**Source:** recording service.  
**Persistence:** persistent metadata or inferred.  
**Storage:** Room/file metadata.  
**Sensitivity:** D0/D1.  
**Backup:** follows metadata policy.  
**Retention:** recording lifetime.  
**Deletion:** delete/reset/uninstall.  
**External transmission:** NEVER.

---

## 14.7 Family recording created/updated timestamps

**Purpose:** replacement/recovery management.  
**Source:** device clock.  
**Persistence:** persistent.  
**Storage:** Room.  
**Sensitivity:** D2.  
**Backup:** follows DB.  
**Retention:** recording lifetime.  
**Deletion:** delete/reset/uninstall.  
**External transmission:** NEVER.

---

# 15. Temporary File Data

## 15.1 Temporary family recording file

**Purpose:** preview before save.  
**Source:** parent recording service.  
**Persistence:** temporary disk file.  
**Storage:** app-private temp directory.  
**Sensitivity:** D3.  
**Backup:** must be excluded / temp storage not backup source.  
**Retention:** until save, cancel, failure, or startup cleanup.  
**Deletion:** cancel/failure/promotion cleanup/recovery.  
**External transmission:** NEVER.

---

## 15.2 Temporary recording token

**Purpose:** correlate recording session/preview.  
**Source:** app-generated.  
**Persistence:** transient or short-lived.  
**Storage:** memory / temporary state.  
**Sensitivity:** D1.  
**Backup:** never.  
**Retention:** recording flow only.  
**Deletion:** completion/cancel/process death.  
**External transmission:** NEVER.

---

# 16. Cache Data

## 16.1 Decoded image cache

**Purpose:** UI performance.  
**Source:** bundled image decode.  
**Persistence:** memory and/or disposable cache.  
**Storage:** cache.  
**Sensitivity:** D0.  
**Backup:** no.  
**Retention:** disposable.  
**Deletion:** OS/app cache cleanup.  
**External transmission:** NEVER.

---

## 16.2 Prepared audio cache

**Purpose:** playback latency.  
**Source:** canonical/family audio.  
**Persistence:** transient memory/decoder state.  
**Storage:** memory/cache.  
**Sensitivity:** D0 for canonical; D3 if family bytes temporarily decoded.  
**Backup:** no.  
**Retention:** short-lived.  
**Deletion:** player release/cache eviction.  
**External transmission:** NEVER.

---

## 16.3 Learning data cache

**Policy:** may exist only as a copy/projection.

It must never be the sole source of truth.

---

# 17. Development Diagnostics Data

## 17.1 Tap-to-audio latency

**Purpose:** engineering performance test.  
**Source:** local timestamps.  
**Persistence:** preferably temporary development log only.  
**Storage:** debug memory/log.  
**Sensitivity:** D1/D2.  
**Backup:** no.  
**Retention:** development session.  
**Deletion:** log cleanup/build replacement.  
**External transmission:** NEVER in Prototype 0.1.

---

## 17.2 Audio exposure-threshold timing

**Purpose:** validate playback semantics.  
**Source:** AudioService.  
**Persistence:** development-only transient/log.  
**Storage:** local debug log.  
**Sensitivity:** D1/D2.  
**Backup:** no.  
**Retention:** temporary.  
**Deletion:** debug cleanup.  
**External transmission:** NEVER.

---

## 17.3 Microphone noise-floor / RMS diagnostics

**Purpose:** tune detector.  
**Source:** transient microphone analysis.  
**Persistence:** development-only if enabled; should not become learning history.  
**Storage:** local debug output only.  
**Sensitivity:** D2.  
**Backup:** no.  
**Retention:** temporary.  
**Deletion:** debug cleanup.  
**External transmission:** NEVER.

---

## 17.4 Crash stack trace

**Purpose:** development debugging.  
**Source:** runtime error.  
**Persistence:** local tool/OS logs.  
**Storage:** development environment / OS logs.  
**Sensitivity:** D1, potentially higher if paths/data leak.  
**Backup:** not app-managed.  
**Retention:** temporary.  
**Deletion:** log rotation/tool cleanup.  
**External transmission:** no remote crash SDK in Prototype 0.1.

---

# 18. Prohibited Analytics Data

Prototype 0.1 must not create a remote analytics record for:

```text
daily active user
session duration
retention
funnel
child engagement score
language preference profile
device advertising ID
```

Local session data may exist for product operation, but it is not transmitted to an analytics service.

---

# 19. Prohibited Identity Data

Miozira Prototype 0.1 must not request/store:

```text
child name
child birthday
parent email
parent phone
home address
school
profile photo
social account
```

---

# 20. Prohibited Location Data

Do not create/store:

```text
GPS coordinates
coarse location
place names tied to observations
location history
```

---

# 21. Prohibited Device Tracking Data

Do not intentionally collect:

```text
advertising ID
Android ID for tracking
hardware serial
persistent fingerprint
IMEI
MAC address
```

---

# 22. Data Ownership by Storage Layer

## Room / SQLite

Owns:

- structured sessions;
- interactions;
- learning state;
- real-world observations;
- family recording metadata;
- canonical seed metadata if chosen.

---

## DataStore

Owns:

- small preferences.

---

## App bundle

Owns:

- canonical visuals;
- canonical audio;
- content manifest;
- suggestions.

---

## Private FileStore

Owns:

- saved family recordings;
- temporary family recording files.

---

## Memory only

Owns:

- child microphone PCM;
- signal-processing frames;
- short-lived controller state;
- active playback/mic handles.

---

# 23. Backup Matrix

Recommended Prototype 0.1 starting policy:

| Data category | Backup direction |
|---|---|
| Bundled canonical content | Not user backup; restored by app install |
| Room learning DB | Explicit decision required |
| DataStore settings | Explicit decision required |
| Family recording metadata | Follow DB decision |
| Family audio files | Exclude by default |
| Temporary files | Exclude |
| Cache | Exclude |
| Child PCM | Never stored, therefore never backed up |

---

# 24. Data Deletion Matrix

| Data category | Individual delete | Parent reset | Uninstall |
|---|---:|---:|---:|
| Canonical content | No | No | Yes |
| Sessions/interactions | No | Yes | Yes |
| Pair learning state | No | Yes | Yes |
| Real-world observations | Prototype: reset only | Yes | Yes |
| Settings | Via settings / reset | Yes | Yes |
| Family recording | Yes | Yes | Yes |
| Temp files | Automatic | Yes | Yes |
| Cache | Automatic | Optional | Yes |
| Child PCM | Automatic immediately | N/A | N/A |

---

# 25. Data Access Matrix

## Child UI may access indirectly

- current concept;
- current pair;
- canonical visual/audio;
- current session state.

Child UI must not directly inspect:

- DB rows;
- raw file paths;
- parent observations;
- reset state;
- family recording management metadata.

---

## Parent UI may access

- settings;
- recent learning summary;
- real-world observations;
- family recordings;
- reset controls.

---

## Domain layer may access

Through repositories only:

- learning evidence;
- adaptive state;
- content.

---

## Platform services may access

Only what they own:

- microphone PCM transiently;
- audio asset/file bytes;
- app-private file paths below FileStore boundary.

---

# 26. Third-Party Access

Prototype 0.1 should have no third-party runtime service that receives user data.

No:

- analytics;
- ad network;
- cloud speech;
- cloud database;
- social SDK.

---

# 27. OS Access

The operating system may necessarily mediate:

- app sandbox;
- microphone permission;
- audio routing;
- backup;
- app lifecycle.

This is different from Miozira intentionally transmitting data to a third-party service.

---

# 28. Data Inventory Acceptance Criteria

Before family test:

- [ ] every persistent DB field appears in this inventory;
- [ ] every saved file type appears here;
- [ ] child PCM is classified memory-only;
- [ ] no transcript/pronunciation field exists;
- [ ] family audio is D3;
- [ ] backup policy is defined for each storage class;
- [ ] reset path exists for all user data;
- [ ] prohibited identity/location/tracking fields are absent;
- [ ] no remote analytics data path exists.

---

# 29. Data Inventory Invariants

### DATA-INV-001
Every persistent user-related field must have a documented purpose.

### DATA-INV-002
Child raw speech is transient memory only.

### DATA-INV-003
No transcript, pronunciation score, or biometric voice representation exists.

### DATA-INV-004
Family voice recordings are the highest-sensitivity persisted data in Prototype 0.1.

### DATA-INV-005
Family voice recordings are parent-created and deletable.

### DATA-INV-006
Real-world observations contain no free text, location, photo, or video.

### DATA-INV-007
No identity/account data is required.

### DATA-INV-008
No advertising or device-tracking identifier is required.

### DATA-INV-009
No user data is intentionally transmitted to third-party runtime services.

### DATA-INV-010
Cache/temp data is never authoritative learning truth.

### DATA-INV-011
All user-generated persistent data is removable by reset/uninstall.

### DATA-INV-012
Backup eligibility is explicit rather than assumed.

---

# 30. Open Data-Inventory Decisions

Before public distribution, confirm:

1. final structured DB backup eligibility;
2. final DataStore backup eligibility;
3. final family recording backup exclusion;
4. exact family recording codec metadata;
5. whether individual real-world observation deletion is needed;
6. whether any local technical diagnostics remain enabled in family-test builds;
7. whether any future crash reporting changes the inventory.

---

# 31. Relationship to `database.md`

`database.md` defines the exact schema.

This document must be updated whenever a new persistent DB field is added.

---

# 32. Relationship to `privacy.md`

`privacy.md` defines the promise.

This inventory provides the evidence for that promise.

---

# 33. Relationship to `security.md`

`security.md` defines controls around each storage boundary.

---

# 34. Relationship to `local-storage.md`

`local-storage.md` defines physical/logical storage locations and backup/reset behavior.

---

# 35. Relationship to `threat-model.md`

`threat-model.md` should use the D2/D3 assets identified here as primary privacy/security assets.

---

# 36. Decision Summary

Prototype 0.1 stores only the data needed to support:

```text
local adaptive repetition
session integrity
parent real-world observations
small preferences
optional family voice
```

It deliberately does **not** create:

```text
accounts
identity profiles
location history
photos
contacts
advertising IDs
remote analytics
child transcripts
pronunciation scores
voiceprints
persistent child speech files
```

The most sensitive persisted data is:

> **optional family voice recordings**

The most sensitive transient data is:

> **child microphone PCM during a short speaking-attempt window**

which must be discarded immediately after local processing.

The governing rule is:

> **If a data element has no clear learning, safety, or operational purpose, it should not exist in Miozira.**
