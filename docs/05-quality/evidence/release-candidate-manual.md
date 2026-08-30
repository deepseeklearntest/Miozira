# Prototype 0.1 manual release-candidate matrix

Status at 2026-08-30: PARTIAL — emulator smoke checks completed; primary-tablet and human-review rows remain open.

The emulator evidence below used the debug APK on `Pixel Tablet`, API 35. Run the remaining rows against the exact signed release APK and record device model/API/build in every row.

| Area | Required check | Result | Evidence |
|---|---|---|---|
| Offline | Airplane-mode child session and parent actions | PARTIAL — launch with Wi-Fi disabled; parent gate/reset/navigation smoke passed | `release-candidate-automated.md`; full airplane-mode flow remains open |
| Orientation | Portrait and landscape, no duplicate or lost evidence | PARTIAL — both launch settings resumed | `release-candidate-automated.md`; full interaction evidence remains open |
| Audio | Canonical word starts promptly and remains available after family-voice failure | NOT RUN | Requires listening review |
| Microphone | Adult opt-in; denial is calm; no background capture | NOT RUN | |
| Family voice | Record, preview, save, replace, delete, missing-file fallback | NOT RUN | |
| Accessibility | TalkBack, large text, 48dp parent targets, reduced motion | NOT RUN | |
| Privacy | App-private files, no public media entry, logcat contains no raw path/audio | PARTIAL — automated storage/privacy gates pass | Physical storage and logcat review remain open |
| Comfort | Volume, touch tolerance, visual calm, thermal/battery behavior | NOT RUN | |

No row should be changed to PASS from code inspection alone.
