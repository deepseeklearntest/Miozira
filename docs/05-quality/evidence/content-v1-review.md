# Prototype 0.1 content review

**Status:** DRAFT — not approved for a family test.

## Present draft package

- Eight concepts and sixteen English/Tamil pairs are locally packaged.
- All audio references use local `.m4a` files; no network content is used.
- Tamil `cup` is recorded as `கப்`; Tamil `water` is recorded as `தண்ணீர்`.
- English and Tamil clips were generated locally as development drafts and must not be represented as reviewed canonical recordings.
- The supplied Cat, Cup, Hand, and Water visuals were retained as drafts. Their source licence/provenance must be recorded before a family-test build.

## Required review before content approval

- A fluent Tamil reviewer verifies household wording, age suitability, and pronunciation of all eight Tamil clips.
- An English reviewer verifies natural spoken forms and clip clarity.
- A reviewer checks loudness and every visual/audio pairing on the physical family tablet in portrait and landscape.
- Every accepted pair is promoted from `DRAFT` to the appropriate review status; only approved assets may be frozen for the family test.

## Engineering validation

- Structural content tests confirm exactly eight concepts, two languages, and sixteen unique pair IDs.
- Android emulator packaging test opens every local audio asset and every unique visual from the installed app.
- All sixteen local draft clips decode as AAC/M4A files (0.56–0.76 seconds each).
- Pixel Tablet emulator renders the Apple and Ball cards, switches English Apple → Tamil Apple → English Ball, and accepts a Ball playback tap without a crash.
- The emulator reports portrait through its automation API while Android WindowManager remains landscape (`2560×1600`, rotation 0). Treat portrait visual verification as a physical-tablet gate; this is not evidence that the app passed portrait on hardware.
