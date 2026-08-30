# Prototype 0.1 known issues

## Open release gates

- Physical-device validation and human review are still required for the manual matrix; API 35 emulator smoke and automated checks are recorded.
- Release signing is not configured in the repository; `assembleRelease` currently produces an unsigned artifact and must be signed through the local release process.
- Parent gate/reset/navigation composition flow has emulator smoke evidence; family-voice flow and full end-to-end parent review still require device review.

These are implementation/release gates, not child-facing product behavior. No new feature work should be added to resolve them.
