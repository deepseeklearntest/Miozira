# Prototype 0.1 family-test release record

Status: NOT RELEASED — awaiting Phase 10 device sign-off.

## Frozen identifiers

- App version: `0.1.0`
- Content version: `prototype-0.1-v1`
- Room schema: `1`
- Adaptive policy: `prototype-0.1`
- Microphone config: `prototype-0.1-v1`
- Content cardinality: 8 concepts / 2 languages / 16 pairs

## Candidate artifact

- APK path: to be recorded after final device-approved build
- SHA-256: the currently built artifact is `d9284d7dfebe5963c7a21c7221b2aa7c75e38a61864bb26e24c411d87b8f5fc4` (unsigned; not a family-test candidate)
- Signing identity: local debug/release signing identity to be recorded; secrets remain outside the repository
- Primary device: to be recorded

The current connected emulator is a Pixel Tablet, API 35. It passed ten debug instrumentation tests; this does not satisfy signed-release or primary-tablet acceptance.

## Signing process

The repository accepts signing inputs only through `MIOZIRA_RELEASE_STORE_FILE`, `MIOZIRA_RELEASE_STORE_PASSWORD`, `MIOZIRA_RELEASE_KEY_ALIAS`, and `MIOZIRA_RELEASE_KEY_PASSWORD`. Set `MIOZIRA_REQUIRE_SIGNED_RELEASE=true` and run `./gradlew :androidApp:verifyFamilyTestSigning :androidApp:assembleRelease` for the family candidate. Keystore files and passwords must remain outside the repository.

The signing gate was exercised without credentials on 2026-08-30 and failed as expected with `MIOZIRA_RELEASE_* signing variables are incomplete`.

Do not install this as the family-test candidate until the manual matrix is signed off.
