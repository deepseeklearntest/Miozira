# Miozira — Prototype 0.1 Acceptance Criteria

**Document:** `acceptance-criteria.md`  
**Version:** 0.1  
**Status:** Draft for Prototype 0.1  
**Product:** Miozira  
**Last updated:** 2026-08-25

---

## 1. Purpose

This document defines the pass/fail criteria for declaring Miozira Prototype 0.1 ready for the seven-day family test.

Prototype 0.1 is accepted only if the product can demonstrate the intended vertical slice reliably enough that family observations reflect the Miozira idea rather than avoidable software defects.

---

# 2. Prototype Definition

Prototype 0.1 must contain:

- 8 concepts;
- 2 languages: English and Tamil;
- tap picture → hear canonical word;
- optional child repeat;
- gentle non-judgmental response;
- simple adaptive repetition;
- fully local learning state;
- tiny protected parent area;
- parent recognition/use observations;
- one short real-world suggestion after meaningful sessions;
- portrait + landscape support;
- no scores, streaks, XP, lives, quizzes, or mandatory reading.

---

# 3. Release Decision

A build is **FAMILY-TEST READY** only when:

```text
all BLOCKER criteria pass
all HIGH criteria pass
content is frozen
adaptive config is frozen
microphone config is frozen
primary physical tablet passes
known MEDIUM/LOW issues are explicitly reviewed
```

---

# 4. BLOCKER Acceptance Criteria

## AC-B01 — App launches

- [ ] Installs on primary family Android tablet.
- [ ] Cold launch succeeds.
- [ ] Warm launch succeeds.
- [ ] No crash loop.

## AC-B02 — Core loop works

For every active concept-language pair:

```text
picture shown
→ child tap accepted
→ canonical word plays
→ optional speaking opportunity
→ gentle response
→ next interaction
```

- [ ] No dead end.
- [ ] No mandatory reading.
- [ ] No correctness grading.

## AC-B03 — All 16 pairs are available

- [ ] 8 concepts exist.
- [ ] English exists for all 8.
- [ ] Tamil exists for all 8.
- [ ] No active pair is missing canonical audio or visual.

## AC-B04 — Exposure integrity

- [ ] Exposure is not committed at playback start.
- [ ] Exposure commits only after meaningful playback threshold/completion.
- [ ] Duplicate callbacks cannot duplicate exposure.
- [ ] Replay does not create a new scheduled exposure.
- [ ] Rotation does not duplicate exposure.
- [ ] Process recreation does not fabricate exposure.

## AC-B05 — Child microphone privacy

- [ ] Child attempt PCM is never written to disk.
- [ ] No transcript is produced.
- [ ] No pronunciation score is produced.
- [ ] No child voice file is created.
- [ ] No child audio is uploaded.

## AC-B06 — Background microphone safety

- [ ] Child microphone stops on background.
- [ ] Child microphone stops on screen lock.
- [ ] Parent recorder stops/cancels safely on background.
- [ ] No continuous microphone use exists.

## AC-B07 — Offline operation

- [ ] Full child session works in airplane mode.
- [ ] Canonical assets require no download.
- [ ] No login/authentication required.
- [ ] No cloud ASR required.
- [ ] No backend required.

## AC-B08 — Parent reset

- [ ] Reset removes sessions.
- [ ] Reset removes interactions.
- [ ] Reset removes adaptive state.
- [ ] Reset removes real-world observations.
- [ ] Reset removes family recording metadata.
- [ ] Reset removes family recording files.
- [ ] Reset does not remove canonical bundled content.
- [ ] Reset is idempotent.

## AC-B09 — Family recording privacy

- [ ] Family audio stored only in app-private storage.
- [ ] Family audio does not appear in media gallery.
- [ ] Family audio can be replaced.
- [ ] Family audio can be deleted.
- [ ] Canonical audio remains available after family-audio failure.

## AC-B10 — No prohibited product mechanics

The app contains none of:

```text
scores
streaks
XP
lives
leaderboards
badges
ads
IAP
subscriptions
push-notification pressure
mandatory lesson completion
pronunciation grading
```

---

# 5. HIGH Acceptance Criteria

## AC-H01 — Portrait and landscape

- [ ] Child loop works in portrait.
- [ ] Child loop works in landscape.
- [ ] Parent area works in portrait.
- [ ] Parent area works in landscape.
- [ ] Rotation preserves interaction identity.

## AC-H02 — Tap responsiveness

- [ ] Child tap acknowledgement normally ≤100 ms on primary device.

## AC-H03 — Audio responsiveness

- [ ] Canonical word normally begins ≤250 ms after accepted tap on built-in speaker.

## AC-H04 — Startup

Targets on primary device:

```text
cold launch ~<=4 s
warm launch ~<=2 s
```

## AC-H05 — Adaptive behavior

- [ ] Due items can reappear.
- [ ] Max 2 new pairs/session.
- [ ] Immediate hammering is avoided.
- [ ] RESTING items are withheld.
- [ ] Real-world-use evidence affects only the exact language pair.

## AC-H06 — Mic denial path

- [ ] Setup can continue with microphone off.
- [ ] Child loop remains complete without microphone.
- [ ] No repeated system permission nagging.

## AC-H07 — Parent area

- [ ] Child does not casually enter parent controls during normal tapping.
- [ ] Parent can mark “recognized outside Miozira.”
- [ ] Parent can mark “used outside Miozira.”
- [ ] Parent can view one real-world suggestion.
- [ ] Parent can manage microphone setting.
- [ ] Parent can reset local data.

## AC-H08 — Child safety

- [ ] Silence is accepted.
- [ ] Early stopping is accepted.
- [ ] Technical errors never blame child.
- [ ] No forced retry.
- [ ] No flashing/high-stimulation feedback.
- [ ] No background music under target words.

## AC-H09 — Accessibility

- [ ] Core child action uses a large forgiving touch region.
- [ ] Parent controls meet ≥48dp target.
- [ ] Parent UI remains usable with large text.
- [ ] Meaning does not depend on red/green or color alone.
- [ ] Reduced-motion mode preserves meaning.

## AC-H10 — State integrity

- [ ] Stale audio callback ignored.
- [ ] Stale mic callback ignored.
- [ ] No duplicate microphone window on rotation.
- [ ] App backgrounding does not create false no-attempt evidence.

---

# 6. Content Acceptance Criteria

Before family test:

- [ ] `apple` approved in English/Tamil.
- [ ] `ball` approved in English/Tamil.
- [ ] `cup` Tamil lexical choice resolved.
- [ ] `hand` approved in English/Tamil.
- [ ] `nose` approved in English/Tamil.
- [ ] `cat` approved in English/Tamil.
- [ ] `car` approved in English/Tamil.
- [ ] `water` Tamil lexical choice resolved.
- [ ] All 16 canonical clips reviewed by fluent speaker.
- [ ] Tamil household/register review complete.
- [ ] English accent/voice consistency complete.
- [ ] Visuals contain no brand/product placement.
- [ ] No active content field remains `TBD`.

---

# 7. Performance Acceptance Criteria

- [ ] No visible UI freeze during DB writes.
- [ ] Adaptive selection creates no noticeable stall.
- [ ] 50–100 interaction stress test shows stable memory.
- [ ] Repeated mic windows do not leak resources.
- [ ] 10-minute session does not cause obvious abnormal heating.
- [ ] App performs no meaningful background workload after leaving active session.

---

# 8. Privacy/Security Acceptance Criteria

- [ ] No account exists.
- [ ] No analytics SDK exists.
- [ ] No ad SDK exists.
- [ ] No cloud speech SDK exists.
- [ ] No child audio file exists after speaking-attempt tests.
- [ ] Final merged manifest reviewed.
- [ ] Only justified permissions remain.
- [ ] `INTERNET` absent unless explicitly justified.
- [ ] Dependency list reviewed.
- [ ] Signing process understood.
- [ ] Family recording backup behavior reviewed.

---

# 9. Device Acceptance Criteria

Required environments:

```text
Primary family Android tablet
Android 16/API 36 emulator
Minimum-SDK emulator
Additional modest physical tablet if available
```

The primary physical tablet is mandatory.

---

# 10. Defect Gate

Before family test:

```text
BLOCKER defects = 0
HIGH defects = 0
MEDIUM defects = reviewed and accepted
LOW defects = acceptable if non-child-impacting
```

---

# 11. Family-Test Readiness Question

The final decision is:

> **Can the child use Miozira for 5–10 minutes, understand the interaction without instruction, hear reliable words, optionally repeat without pressure, stop naturally, and generate trustworthy evidence for whether language transfers outside the app?**

If the answer is not clearly yes, the build is not ready.

---

# 12. Acceptance Sign-Off

Record:

```text
Build:
Commit:
Date:
Primary device:
Android version:
Content version:
Adaptive config:
Microphone config:
Blockers:
High issues:
Known medium/low issues:
Decision: READY / NOT READY
Approved by:
```

---

# 13. Governing Rule

> **Prototype 0.1 is accepted when it is reliable enough to test the learning idea—not when every future Miozira feature exists.**
