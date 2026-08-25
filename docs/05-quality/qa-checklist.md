# Miozira — QA Checklist

**Document:** `qa-checklist.md`  
**Version:** 0.1  
**Status:** Execution Checklist  
**Product:** Miozira  
**Last updated:** 2026-08-25

---

## 1. Purpose

This is the concise pre-release QA checklist for Prototype 0.1.

Use it after implementation and before creating the family-test build.

---

# 2. Build

- [ ] Correct release/family-test build variant.
- [ ] Commit hash recorded.
- [ ] Version/build number recorded.
- [ ] Release-like build is not unintentionally debuggable.
- [ ] No test/demo child UI exposed.
- [ ] Signing configuration understood.

---

# 3. Content

- [ ] 8 concepts present.
- [ ] English + Tamil present.
- [ ] Exactly 16 active pairs.
- [ ] No duplicate IDs.
- [ ] No `TBD` active content.
- [ ] Cup Tamil form approved.
- [ ] Water Tamil form approved.
- [ ] All canonical audio reviewed.
- [ ] All visuals reviewed.
- [ ] Content version recorded.

---

# 4. Child Core Loop

For several pairs in both languages:

- [ ] Concept appears clearly.
- [ ] Tap acknowledged immediately.
- [ ] Canonical word plays.
- [ ] No overlapping audio.
- [ ] Speaking invitation optional.
- [ ] Silence works.
- [ ] Gentle response works.
- [ ] Next item appears.
- [ ] Child can stop anytime.

---

# 5. Adaptive Engine

- [ ] Max 2 new pairs/session.
- [ ] Due items reappear.
- [ ] Same pair not hammered.
- [ ] RESTING behavior works.
- [ ] Replays not counted as scheduled exposures.
- [ ] Real-world observation affects correct language pair only.
- [ ] Adaptive config version recorded/frozen.

---

# 6. Audio

- [ ] All 16 clips decode.
- [ ] Loudness reasonably consistent.
- [ ] No clipping.
- [ ] No background music.
- [ ] Tap-to-audio acceptable.
- [ ] Exposure threshold works.
- [ ] Failure before threshold does not count exposure.
- [ ] Replay does not overlap.

---

# 7. Microphone

- [ ] Parent explanation shown before first request.
- [ ] Permission grant works.
- [ ] Permission denial works.
- [ ] Child loop works with mic off.
- [ ] No raw child audio file created.
- [ ] No transcript.
- [ ] No pronunciation score.
- [ ] Mic stops on background.
- [ ] Mic stops on screen lock.
- [ ] Microphone config version recorded/frozen.

---

# 8. Family Recording

- [ ] Adult-only flow.
- [ ] Start/stop works.
- [ ] Preview works.
- [ ] Save works.
- [ ] Replace works.
- [ ] Delete works.
- [ ] File remains private.
- [ ] File absent from media gallery.
- [ ] Canonical fallback works if family file missing.

---

# 9. Parent Area

- [ ] Parent gate works.
- [ ] Child cannot casually trigger destructive controls.
- [ ] Mark recognized works.
- [ ] Mark used works.
- [ ] Real-world suggestion appears.
- [ ] Mic setting works.
- [ ] Privacy explanation accessible.
- [ ] Reset requires confirmation.

---

# 10. Database / Persistence

- [ ] Sessions persist.
- [ ] Interactions persist.
- [ ] Exposure idempotent.
- [ ] Attempt state persists once.
- [ ] Pair state rebuild works.
- [ ] Incomplete session recovery works.
- [ ] Restart preserves state.
- [ ] Reset removes user data.
- [ ] Reset preserves canonical content.

---

# 11. Lifecycle

Rotate during:

- [ ] READY.
- [ ] PLAYING_WORD.
- [ ] LISTENING.
- [ ] RESPONDING.

Verify:

- [ ] no duplicate exposure;
- [ ] no duplicate mic window;
- [ ] no unexpected replay;
- [ ] no state corruption.

Also test:

- [ ] Home/background.
- [ ] Foreground return.
- [ ] Screen lock.
- [ ] Process kill/restart.

---

# 12. Layout

- [ ] Portrait child layout.
- [ ] Landscape child layout.
- [ ] Portrait parent layout.
- [ ] Landscape parent layout.
- [ ] Insets respected.
- [ ] Large concept remains dominant.
- [ ] No clipped controls.

---

# 13. Accessibility

- [ ] Large child touch region.
- [ ] Parent controls ≥48dp.
- [ ] Large parent text tested.
- [ ] TalkBack parent navigation sanity.
- [ ] Reduced-motion behavior.
- [ ] No color-only meaning.
- [ ] Tamil text renders correctly.

---

# 14. Privacy

- [ ] No account.
- [ ] No ads.
- [ ] No analytics.
- [ ] No cloud speech.
- [ ] No child audio persistence.
- [ ] Family recordings private.
- [ ] Backup policy reviewed.
- [ ] Privacy copy matches behavior.
- [ ] Data reset works.

---

# 15. Security

- [ ] Merged manifest reviewed.
- [ ] No unexplained permissions.
- [ ] `INTERNET` absent or explicitly justified.
- [ ] No secrets in repository.
- [ ] Dependencies reviewed.
- [ ] Family file paths hidden behind FileStore.
- [ ] Stale callbacks ignored.
- [ ] Reset partial-failure path tested.

---

# 16. Offline

- [ ] Launch in airplane mode.
- [ ] Start session in airplane mode.
- [ ] All canonical audio works.
- [ ] Parent observations save.
- [ ] Family recording works.
- [ ] Reset works.

---

# 17. Performance

- [ ] Cold launch near target.
- [ ] Warm launch near target.
- [ ] Tap feedback normally ≤100 ms.
- [ ] Audio normally ≤250 ms.
- [ ] No visible DB/UI freeze.
- [ ] No mic-induced jank.
- [ ] 50–100 interaction stress test stable.
- [ ] No obvious abnormal heating in ~10-minute session.

---

# 18. Child Safety

- [ ] No score/streak/XP/lives.
- [ ] No “wrong” state.
- [ ] No forced speaking.
- [ ] No forced retry.
- [ ] No forced completion.
- [ ] No push notifications.
- [ ] No ads/purchases.
- [ ] No flashing.
- [ ] No attention-grabbing nagging.
- [ ] Early stop treated as valid.

---

# 19. Device Gate

Primary family tablet:

- [ ] passes portrait;
- [ ] passes landscape;
- [ ] passes audio;
- [ ] passes mic grant;
- [ ] passes mic denial;
- [ ] passes offline;
- [ ] passes reset;
- [ ] passes lifecycle;
- [ ] passes all 16 pair playback.

---

# 20. Defects

```text
BLOCKER open:
HIGH open:
MEDIUM open:
LOW open:
```

Required before family test:

```text
BLOCKER = 0
HIGH = 0
```

---

# 21. Freeze

- [ ] Content frozen.
- [ ] Adaptive constants frozen.
- [ ] Mic thresholds frozen.
- [ ] Build ID frozen.
- [ ] Known issues documented.

---

# 22. Final QA Sign-Off

```text
Build:
Commit:
Date:
Primary device:
Tester:
Content version:
Adaptive config:
Mic config:
Open medium/low issues:
Result: PASS / FAIL
Family-test ready: YES / NO
```

---

# 23. Governing Rule

> **QA is complete when the family test can focus on the child and the learning idea instead of software defects.**
