# Technical spike — persistence evidence

**Date:** 2026-08-26
**Build:** local uncommitted debug APK, `com.miozira` 0.1.0
**Database:** Room 2.7.2 KMP with bundled SQLite 2.6.2 and KSP 2.0.21-1.0.28.

## Automated emulator check — PASS

Device: Android 15 Pixel Tablet emulator (`medium_tablet`).

`SpikePersistenceTest` passed against a real on-device Room database:

1. it committed one interaction twice using the same interaction ID;
2. it observed exactly one row;
3. it closed the database, reopened the same named database, and read back the original semantic fields.

The spike schema has only four fields: interaction ID, session ID, exposure committed, and attempt state. It has no raw audio, transcript, score, analytics, or file-path fields.

## Toolchain observation

Room 2.8.4 was rejected for the current Kotlin 2.0.21 setup: its KSP processor failed during schema export with a Kotlin serialization `AbstractMethodError`. Room 2.7.2 is the earlier stable KMP Room line and passed the same KSP compile and emulator round-trip.

## Physical-tablet restart check — PENDING

After the spike screen is wired to commit its actual exposure and attempt callbacks, complete an interaction on the primary tablet, force-stop, relaunch, and confirm its row remains exactly once.
