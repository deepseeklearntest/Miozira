# Privacy and offline release audit

## Automated evidence

Verified from the current worktree on 2026-08-30:

- `./gradlew :shared:testDebugUnitTest` — PASS
- `./gradlew :androidApp:lintRelease` — PASS
- `./gradlew :androidApp:assembleRelease` — PASS
- Release manifest declares only `android.permission.RECORD_AUDIO` from the project allowlist.
- `./gradlew :androidApp:checkDependencyPolicy` — PASS; the resolved release component graph contains no denylisted analytics, advertising, or remote-crash SDK group.
- `./gradlew :androidApp:checkBackupPolicy` — PASS; both legacy and Android 12+ rules exclude family recordings, the local database, and shared preferences.
- `android.permission.INTERNET` is absent from the project manifest and forbidden by the manifest policy task.
- Android backup is disabled and explicit legacy/Android 12+ rules exclude `family-recordings/`, `miozira.db`, and shared preferences.
- Child attempt contracts expose semantic states only; no PCM path, transcript, pronunciation score, or child-audio persistence field is present in the detector API.
- Adult microphone request history is stored as one app-private boolean, so denied states can be distinguished across process death without storing capture data.
- Startup invokes private temporary-recording cleanup before content seeding and interrupted-session recovery; storage-boundary coverage verifies traversal rejection, orphan cleanup, and idempotent deletion.

## Still required before family-test release

- Inspect the merged release manifest and dependency report on the release candidate.
- Verify app-private/public storage and logcat on a physical tablet.
- Verify airplane-mode child, parent, reset, and recording flows.
- Review backup behavior on the primary device and record the exact build/checksum.
