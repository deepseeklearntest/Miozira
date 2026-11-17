# Parent Area Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Put the parent area behind a hidden two-step gate and make it describe the child's sessions in sentences, with four words for the day, a per-language page, a microphone switch and a way to start an extra session.

**Architecture:** Everything that decides something is a pure function or a small state holder tested in `commonTest`: the hold reducer (exists), the number-word challenge, the access state, and the functions that turn stored evidence into sentences. Compose screens only render those results. One new read query feeds the session summary; there is no schema change.

**Tech Stack:** Kotlin Multiplatform, Compose Multiplatform, Room, DataStore; `kotlin.test` in `shared/src/commonTest`, Room tests in `shared/src/androidInstrumentedTest`.

**Spec:** `docs/superpowers/specs/2026-11-16-ui-ux-design.md` (§10) and `docs/04-privacy-safety/security.md` (§13–15). This is product step 2, parent side. The child side is `2026-11-17-child-session-shape.md`.

## What exists today (read before starting)

- **The gate is bypassed.** `ChildSessionScreen` opens the parent area from Compose's default long-press (about half a second) on a 48 dp corner and never goes through `ParentGate.reduce`. The tested 2-second reducer is unused. An automated security review of the pushed code flagged this (authorization bypass, medium). Tasks 1–3 fix it and can be done before the child-session plan.
- `ParentSummaryScreen` shows `"Heard ${exposureCount} times"` and raw pair IDs such as `apple:ta`.
- `RealWorldTransferService.chooseSuggestion` is not called from production code.
- Speech-attempt detection has a stored setting (`isSpeechAttemptDetectionEnabled`, default off) and no screen that changes it.

## Global Constraints

- `android.permission.INTERNET` stays absent. No new dependency. No reminders or notifications.
- No child screen shows any icon, link or progress for the parent corner. A short tap there does nothing.
- The parent area shows **no numbers, dates or durations** anywhere, and never the words "missed", "failed", "wrong", "learned" or "mastered".
- Allowed phrases for evidence: "Tried saying", "Listened without speaking", "He understood it", "He said it"; "most", "some", "not yet" stand in for counts.
- The gate is a friction boundary, not authentication; do not describe it as secure anywhere in the UI.
- Erasing data keeps its own confirmation inside the parent area.
- UI never calls DAOs.
- Parent-area text is English.
- Do not touch `tools/audio/` or the bundled clips.
- Work on a branch named `feat/parent-area`. The working tree has unrelated uncommitted changes, so stage only the files each task names.
- Unit tests: `./gradlew :shared:testDebugUnitTest`. Room tests: `./gradlew :shared:connectedDebugAndroidTest`.

## Decisions made in this plan (not in the spec)

- **The gate opens when the finger lifts** after a hold of at least 3 seconds, as the existing reducer already works. Nothing changes on screen while holding.
- **The hidden corner is 72 dp square** at the top right.
- **The number is 10–99**, and the two typed digits are checked as soon as the second one is entered; there is no backspace and no "wrong" message.
- **"Said before the app did"** and the puzzle line of the session summary are left out until "say it first" and the puzzles exist. **Voices** and **How the puzzles are going** are built with the guide-voice plan.
- **When listening was off**, the summary says "Heard", not "Listened without speaking", because the app cannot know whether he spoke.
- **The moment for each of the eight words** is a fixed table (Task 5).

## Review Focus

1. A child mashing the corner or the number pad: wrong digits and the timeout both return silently, the number is different next time, and nothing rewards the attempt. Pinned in Tasks 2 and 3.
2. The parent area left open when the tablet is handed back or the screen turns off: it must be locked the next time the app is seen. Pinned in Task 3 (`lock_fromAnyState_returnsToLocked`) and its wiring to `onStop`.
3. Listening was off or the permission was refused: the summary must not claim he "listened without speaking". Pinned in Task 4 (`unmeasuredWords_areDescribedAsHeard`).
4. First use, or straight after erasing everything: the home shows the empty sentence and no four-words card, and nothing crashes. Pinned in Tasks 4 and 5.
5. A number leaking into parent text through a count, a pair ID or a date: pinned in Task 4 (`noParentSentenceContainsADigit`), which every later sentence-producing task extends.

---

### Task 1: The hidden corner goes through the hold reducer

**Files:**
- Modify: `shared/src/commonMain/kotlin/com/miozira/presentation/parent/ParentGate.kt`
- Create: `shared/src/commonMain/kotlin/com/miozira/presentation/parent/ParentCorner.kt`
- Modify: `shared/src/commonMain/kotlin/com/miozira/presentation/child/ChildSessionScreen.kt`
- Test: `shared/src/commonTest/kotlin/com/miozira/presentation/parent/ParentGateTest.kt`

**Interfaces:**
- Produces: `ParentGate.REQUIRED_HOLD_MS = 3_000L`; `@Composable fun ParentCorner(nowMs: () -> Long, onHoldCompleted: () -> Unit, modifier: Modifier = Modifier)`.

`ParentCorner` is an undrawn 72 dp box. It reads raw pointer events (`awaitEachGesture`), sends `PressStarted`, `Moved`, `Released` and `Cancelled` to `ParentGate.reduce`, and calls `onHoldCompleted` only when the state becomes `UNLOCKED`, then sends `Consumed`. It has no semantics, no indication and no child content. `ChildSessionScreen` places it at `Alignment.TopEnd` on every child screen state and drops the `detectTapGestures(onLongPress = …)` block.

- [x] **Step 1: Change the reducer tests to three seconds and add one:**

```kotlin
@Test fun holdOfTwoPointNineSeconds_doesNotUnlock()   // PressStarted(0) + Released(2_999) -> IDLE
@Test fun exactThreeSecondStableHold_unlocks()        // replaces exactTwoSecondStableHold_unlocks
```

- [x] **Step 2: Run** `./gradlew :shared:testDebugUnitTest --tests '*ParentGateTest'` — expect FAIL.
- [x] **Step 3: Set `REQUIRED_HOLD_MS` to `3_000L`, write `ParentCorner`, and use it in `ChildSessionScreen`.**
- [x] **Step 4: Run** `./gradlew :shared:testDebugUnitTest :androidApp:assembleDebug` — expect PASS and BUILD SUCCESSFUL.
- [ ] **Step 5: Manual check on an emulator:** a tap and a one-second press on the corner do nothing; sliding the finger away during the hold does nothing; a three-second hold then lift triggers the callback. *(NOT VERIFIED: no device attached; R10)*
- [x] **Step 6: Commit** `fix: route the parent corner through the three-second hold reducer`.

---

### Task 2: The reading step

**Files:**
- Create: `shared/src/commonMain/kotlin/com/miozira/presentation/parent/ParentChallenge.kt`
- Test: `shared/src/commonTest/kotlin/com/miozira/presentation/parent/ParentChallengeTest.kt`

**Interfaces:**
- Produces:

```kotlin
fun numberWords(n: Int): String                       // 10..99, e.g. 47 -> "forty-seven"; throws IllegalArgumentException outside the range

data class ChallengeState(val number: Int, val typed: String = "", val outcome: Outcome? = null) {
    enum class Outcome { ACCEPTED, CANCELLED }
    val words: String get() = numberWords(number)
}
sealed interface ChallengeEvent {
    data class Digit(val digit: Int) : ChallengeEvent
    data object TimedOut : ChallengeEvent                 // 10 seconds without input
}
object ParentChallenge {
    const val TIMEOUT_MS = 10_000L
    fun start(previousNumber: Int?, random: (IntRange) -> Int): ChallengeState   // 10..99, never equal to previousNumber
    fun reduce(state: ChallengeState, event: ChallengeEvent): ChallengeState
}
```

`reduce`: a digit appends to `typed`; when `typed` has two digits, `outcome` is `ACCEPTED` if it equals the number, otherwise `CANCELLED`. `TimedOut` gives `CANCELLED`. Once `outcome` is set the state no longer changes.

- [x] **Step 1: Write the failing tests**

```kotlin
@Test fun numberWords_coverTheIrregularAndRegularForms() {
    assertEquals("ten", numberWords(10)); assertEquals("thirteen", numberWords(13)); assertEquals("nineteen", numberWords(19))
    assertEquals("twenty", numberWords(20)); assertEquals("forty-seven", numberWords(47)); assertEquals("ninety-nine", numberWords(99))
}
@Test fun numberWords_neverContainADigit() = (10..99).forEach { n -> assertTrue(numberWords(n).none(Char::isDigit)) }
@Test fun numberWords_rejectOutOfRange() { assertFailsWith<IllegalArgumentException> { numberWords(9) }; assertFailsWith<IllegalArgumentException> { numberWords(100) } }
@Test fun typingTheNumber_isAccepted()                 // 47: Digit(4), Digit(7) -> ACCEPTED
@Test fun typingAnotherNumber_isCancelled_withoutAnyOtherChange()   // 47: Digit(4), Digit(8) -> CANCELLED
@Test fun oneDigitThenTimeout_isCancelled()
@Test fun afterAnOutcome_furtherEventsChangeNothing()
@Test fun start_neverRepeatsThePreviousNumber()        // random always returns 47, previous 47 -> result != 47 and within 10..99
```

- [x] **Step 2: Run** `--tests '*ParentChallengeTest'` — expect FAIL. **Step 3: Implement.** For `start`, if the drawn number equals the previous one, use the next number in range (wrapping 99 → 10). **Step 4: Run** — expect PASS.
- [x] **Step 5: Commit** `feat: add the number-word reading step for the parent gate`.

---

### Task 3: Locked, challenged, open — and locked again

**Files:**
- Create: `shared/src/commonMain/kotlin/com/miozira/presentation/parent/ParentAccessController.kt`
- Create: `shared/src/commonMain/kotlin/com/miozira/presentation/parent/ParentReadingStepScreen.kt`
- Modify: `shared/src/commonMain/kotlin/com/miozira/App.kt`
- Modify: `androidApp/src/main/kotlin/com/miozira/MainActivity.kt`
- Test: `shared/src/commonTest/kotlin/com/miozira/presentation/parent/ParentAccessControllerTest.kt`

**Interfaces:**
- Consumes: `ParentChallenge`, `ChallengeState`, `ChallengeEvent` (Task 2); `ParentCorner` (Task 1).
- Produces:

```kotlin
sealed interface ParentAccess {
    data object Locked : ParentAccess
    data class Challenge(val challenge: ChallengeState) : ParentAccess
    data object Open : ParentAccess
}
class ParentAccessController(random: (IntRange) -> Int = { it.random() }) {
    val state: StateFlow<ParentAccess>
    fun cornerHeld()                      // Locked -> Challenge(new number); ignored otherwise
    fun digit(digit: Int)                 // Challenge only
    fun timedOut()                        // Challenge only
    fun lock()                            // any state -> Locked
}
```

`Open` is reachable only from `Challenge` with outcome `ACCEPTED`. A `CANCELLED` outcome goes straight to `Locked`. The controller remembers the last number so the next challenge differs.

`ParentReadingStepScreen(challenge: ChallengeState, onDigit: (Int) -> Unit)`: parent background `#F6F3EE`, three lines of text ("Parents only", "Type this number:", `challenge.words`) and a 0–9 pad of ordinary buttons. It shows nothing about what was typed and has no cancel button. `MioziraApp` replaces `parentOpen` with the controller's state: `Locked` → child screen, `Challenge` → reading step plus a `LaunchedEffect(challenge.typed)` that waits `ParentChallenge.TIMEOUT_MS` and calls `timedOut()`, `Open` → parent area. "Back to child area" becomes "Lock and go back" and calls `lock()`. `MainActivity.onStop` calls `lock()`.

- [x] **Step 1: Write the failing tests**

```kotlin
@Test fun startsLocked()
@Test fun cornerHeld_showsAChallenge()
@Test fun correctDigits_open()
@Test fun wrongDigits_returnToLocked()
@Test fun timeout_returnsToLocked()
@Test fun digitsWhileLocked_doNothing()               // Open can never be reached without a challenge
@Test fun cornerHeldWhileOpen_isIgnored()
@Test fun lock_fromAnyState_returnsToLocked()
@Test fun aSecondChallenge_usesADifferentNumber()
```

- [x] **Step 2: Run** `--tests '*ParentAccessControllerTest'` — expect FAIL. **Step 3: Implement the controller, the screen and the wiring.** **Step 4: Run** `./gradlew :shared:testDebugUnitTest :androidApp:assembleDebug` — expect PASS.
- [ ] **Step 5: Manual check on an emulator:** a wrong number and ten seconds of nothing both return to the same child picture with no message or sound; pressing the home button with the parent area open and returning shows the child screen; two challenges in a row show different numbers.
- [x] **Step 6: Commit** `feat: two-step parent gate that locks itself again`.

---

### Task 4: The most recent session, in sentences

**Files:**
- Modify: `shared/src/commonMain/kotlin/com/miozira/data/db/dao/InteractionDao.kt`
- Modify: `shared/src/commonMain/kotlin/com/miozira/data/repository/RoomParentDataSource.kt`
- Modify: `shared/src/commonMain/kotlin/com/miozira/presentation/parent/ParentController.kt`
- Create: `shared/src/commonMain/kotlin/com/miozira/presentation/parent/ParentSentences.kt`
- Test: `shared/src/commonTest/kotlin/com/miozira/presentation/parent/ParentSentencesTest.kt`, existing `ParentControllerTest.kt`
- Test: `shared/src/androidInstrumentedTest/kotlin/com/miozira/data/repository/RecentSessionQueryTest.kt` (create)

**Interfaces:**
- Consumes: `languageNames` from the child-session plan's Task 2. If that plan has not landed, add the same `val languageNames: Map<LanguageId, String>` to `ContentRepository.kt` here.
- Produces:

```kotlin
data class SessionSummary(
    val languages: List<LanguageId>,      // in order of first appearance
    val tried: List<PairId>,              // any interaction with ATTEMPT_DETECTED
    val listened: List<PairId>,           // heard, measured, and never ATTEMPT_DETECTED
    val heardUnmeasured: List<PairId>,    // heard, only NOT_MEASURED or MICROPHONE_UNAVAILABLE
)
interface ParentDataSource {              // replaces recentActivities()
    suspend fun mostRecentSession(): AppResult<SessionSummary?>
}
fun wordLabel(pairId: PairId): String                       // "apple" for English, "water (Tamil)" otherwise
fun describeSession(summary: SessionSummary?): List<String>
```

Query (the most recent session in which at least one word was heard):

```sql
SELECT * FROM interaction WHERE session_id = (
  SELECT s.session_id FROM session s
  WHERE EXISTS (SELECT 1 FROM interaction x WHERE x.session_id = s.session_id AND x.exposure_committed = 1)
  ORDER BY s.started_at_ms DESC LIMIT 1
) AND exposure_committed = 1 ORDER BY sequence_index
```

`describeSession` output, each line present only when it has words:
- `null` → `["Miozira will describe his sessions here."]`
- `"Most recent session: English and Tamil"` (one language: `"Most recent session: English"`)
- `"Tried saying: apple, ball, water (Tamil)"`
- `"Listened without speaking: nose, cat"`
- `"Heard: cup (Tamil)"`

`ParentUiState` becomes `data class ParentUiState(val sessionLines: List<String>, val recentPairs: List<PairId>, val notice: Notice?)`; `recentPairs` is every pair in the summary and feeds the "Noticed at home" rows. `ParentPairActivity.exposureCount` and `lastExposedAtMs` are removed.

- [ ] **Step 1: Write the failing tests**

```kotlin
@Test fun noSession_givesTheEmptySentence()
@Test fun englishWordsAreBare_otherLanguagesAreNamed()        // wordLabel(PairId("apple:en")) == "apple"; PairId("water:ta") -> "water (Tamil)"
@Test fun triedAndListened_areSeparateLines()
@Test fun unmeasuredWords_areDescribedAsHeard()               // no "Listened without speaking" line for them
@Test fun aWordTriedOnceAndSilentOnce_countsAsTried()
@Test fun emptyGroups_produceNoLine()
@Test fun noParentSentenceContainsADigit()                    // every line from every case above: none { it.isDigit() }
@Test fun noParentSentenceUsesAForbiddenWord()                // missed, failed, wrong, learned, mastered (case-insensitive)
```

- [ ] **Step 2: Run** `--tests '*ParentSentencesTest'` — expect FAIL.
- [ ] **Step 3: Implement** `ParentSentences.kt`, the DAO query, `RoomParentDataSource.mostRecentSession()` (group the rows into a `SessionSummary`) and the `ParentController`/`ParentUiState` change; update `ParentControllerTest` to the new data source.
- [ ] **Step 4: Write the Room test** `RecentSessionQueryTest`: with two sessions, only the later one's heard interactions return; a later session with no heard word is skipped; an empty database returns `Success(null)`.
- [ ] **Step 5: Run** `./gradlew :shared:testDebugUnitTest` — expect PASS; `./gradlew :shared:connectedDebugAndroidTest` — expect PASS.
- [ ] **Step 6: Commit** `feat: describe the most recent session in sentences`.

---

### Task 5: Four words for today

**Files:**
- Create: `shared/src/commonMain/kotlin/com/miozira/content/DailyMoments.kt`
- Modify: `shared/src/commonMain/kotlin/com/miozira/domain/transfer/RealWorldTransferService.kt` (remove `chooseSuggestion`, `RealWorldSuggestion`, `SuggestionContext`; they have no production caller)
- Modify: `shared/src/commonMain/kotlin/com/miozira/presentation/parent/ParentSentences.kt`, `ParentController.kt`
- Test: `shared/src/commonTest/kotlin/com/miozira/presentation/parent/WordsForTodayTest.kt`; trim the suggestion cases from `RealWorldTransferServiceTest.kt`

**Interfaces:**
- Consumes: `SessionSummary`, `wordLabel` (Task 4).
- Produces: `val dailyMoments: Map<ConceptId, String>`; `fun wordsForToday(summary: SessionSummary?, max: Int = 4): List<String>`; `ParentUiState.wordsForToday: List<String>`.

The table (exact copy):

| Concept | Moment |
|---|---|
| apple | At snack time, handing a slice |
| ball | At play time, rolling it to him |
| cup | At dinner, passing it |
| hand | When washing hands |
| nose | At bath time |
| cat | When you see a cat |
| car | When getting into the car |
| water | At breakfast, when you pour |

Each line is `"<moment>: <wordLabel>"`, for example `"At breakfast, when you pour: water (Tamil)"`. Choose tried words first, then listened, then heard-unmeasured, in session order; at most one line per concept; at most `max` lines; a concept with no moment is skipped. `null` summary → empty list (the card is not shown).

- [ ] **Step 1: Write the failing tests**

```kotlin
@Test fun noSession_givesNoWords()
@Test fun atMostFourLines()
@Test fun triedWordsComeFirst()
@Test fun oneLinePerConcept_evenWhenHeardInBothLanguages()
@Test fun aLineJoinsTheMomentAndTheWord()                    // "At breakfast, when you pour: water (Tamil)"
@Test fun everyConceptInTheBundledContentHasAMoment()         // PrototypeContentRepository.allPairs() concepts ⊆ dailyMoments.keys
@Test fun noLineContainsADigit()
```

- [ ] **Step 2: Run** `--tests '*WordsForTodayTest'` — expect FAIL. **Step 3: Implement.** **Step 4: Run** `./gradlew :shared:testDebugUnitTest` — expect PASS.
- [ ] **Step 5: Commit** `feat: four words for today in place of the single suggestion`.

---

### Task 6: How each language is going

**Files:**
- Modify: `shared/src/commonMain/kotlin/com/miozira/presentation/parent/ParentSentences.kt`, `ParentController.kt`, `data/repository/RoomParentDataSource.kt`
- Test: `shared/src/commonTest/kotlin/com/miozira/presentation/parent/LanguageSentencesTest.kt`

**Interfaces:**
- Produces:

```kotlin
data class PairEvidence(val pairId: PairId, val heard: Boolean, val tried: Boolean)   // from pair_learning_state: validExposureCount > 0, attemptDetectedCount > 0
// new member of interface ParentDataSource:
suspend fun pairEvidence(): AppResult<List<PairEvidence>>
fun describeLanguage(languageId: LanguageId, evidence: List<PairEvidence>): String
```

One paragraph per language, built only from pairs of that language:
- none heard → `"Japanese — not met yet."`
- heard, none tried → `"Tamil — he has not tried saying his Tamil words yet."`
- tried at least two thirds of the heard words → `"Tamil — he tries most of his Tamil words."`
- otherwise → `"Tamil — he tries some of his Tamil words."`
- then, when any heard word is untried, append `" Still just listening: nose, cat."` using the concept name alone (the language is already named).

`ParentUiState.languageLines: List<String>` holds one line per language in `languageOrder`.

- [ ] **Step 1: Write the failing tests**

```kotlin
@Test fun languageNeverHeard_isNotMetYet()
@Test fun heardButNeverTried()
@Test fun twoThirdsTried_isMost()                 // 4 of 6 heard are tried
@Test fun fewerTried_isSome()
@Test fun untriedWordsAreListed_withoutTheLanguageName()
@Test fun noLineContainsADigit_orAForbiddenWord()
```

- [ ] **Step 2: Run** `--tests '*LanguageSentencesTest'` — expect FAIL. **Step 3: Implement.** **Step 4: Run** — expect PASS.
- [ ] **Step 5: Commit** `feat: describe how each language is going`.

---

### Task 7: Microphone switch and extra session

**Files:**
- Modify: `shared/src/commonMain/kotlin/com/miozira/presentation/parent/ParentActionsController.kt`
- Modify: `androidApp/src/main/kotlin/com/miozira/MainActivity.kt`
- Test: `shared/src/commonTest/kotlin/com/miozira/presentation/parent/ParentActionsControllerTest.kt`

**Interfaces:**
- Consumes: `SettingsRepository`, `PermissionService`; `ChildSessionController.startExtraSession()` and `ParentAccessController.lock()`. `startExtraSession` comes from the child-session plan; if that plan has not landed, leave the extra-session parts of this task until it has and say so in the commit.
- Produces: `ParentActionsController(resetService: ResetService, settings: SettingsRepository, permissions: PermissionService, startExtraSession: suspend () -> Unit, lock: () -> Unit)`; in `ParentActionsState`: `listeningOn: Boolean`, `microphoneBlocked: Boolean`, `extraSessionConfirmationRequired: Boolean`; methods `suspend fun load()`, `fun setListening(on: Boolean)`, `fun requestExtraSession()`, `fun cancelExtraSession()`, `suspend fun confirmExtraSession()`.

Behaviour:
- `setListening(true)` asks for the microphone permission only when its state is `NOT_DETERMINED`; the setting is saved as on only when the result is `GRANTED`. `DENIED` or `DENIED_DONT_ASK_AGAIN` leaves the setting off and sets `microphoneBlocked = true`, which shows the existing "open microphone settings" button.
- `setListening(false)` saves off and never touches the permission.
- `confirmExtraSession()` does nothing unless `extraSessionConfirmationRequired` is true; otherwise it calls `startExtraSession()` and then `lock()`.

- [ ] **Step 1: Write the failing tests**

```kotlin
@Test fun turningListeningOn_withPermissionGranted_savesOn()
@Test fun turningListeningOn_whenNotDetermined_asksOnce_andSavesOnlyIfGranted()
@Test fun turningListeningOn_whenDenied_staysOff_andReportsBlocked()
@Test fun turningListeningOff_neverAsksForPermission()
@Test fun extraSession_needsConfirmation_thenStartsAndLocks()
@Test fun confirmExtraSession_withoutARequest_doesNothing()
```

- [ ] **Step 2: Run** `--tests '*ParentActionsControllerTest'` — expect FAIL. **Step 3: Implement and wire in `MainActivity`** (share the one `DataStoreSettingsRepository` instance with the child controller's `measurement`). **Step 4: Run** `./gradlew :shared:testDebugUnitTest` — expect PASS.
- [ ] **Step 5: Commit** `feat: listening switch and extra session in the parent area`.

---

### Task 8: The parent screens

**Files:**
- Modify: `shared/src/commonMain/kotlin/com/miozira/presentation/parent/ParentSummaryScreen.kt` (becomes the parent home), `ParentActionsScreen.kt` (becomes "Microphone and privacy")
- Create: `shared/src/commonMain/kotlin/com/miozira/presentation/parent/LanguagesScreen.kt`
- Modify: `shared/src/commonMain/kotlin/com/miozira/App.kt`
- Modify: `docs/05-quality/acceptance-criteria.md` (tick the AC-H07 lines this plan satisfies), `CLAUDE.md` ("Current Repository State")

**Interfaces:**
- Consumes: `ParentUiState` (`wordsForToday`, `sessionLines`, `recentPairs`, `languageLines`, `notice`), `ParentActionsState`, `wordLabel`.

Parent home, top to bottom, on `#F6F3EE`:
1. Title "Miozira for parents" and the button "Lock and go back".
2. Card "Four words for today" with `wordsForToday`; the card is absent when the list is empty.
3. Card "Most recent session" with `sessionLines`.
4. Card "Noticed at home": one row per `recentPairs` entry showing `wordLabel`, with buttons "He understood it" and "He said it"; after a save the screen shows "Saved." or "Could not save this yet."
5. Rows: "How each language is going" (opens `LanguagesScreen`, which lists `languageLines`), "Start an extra session", "Microphone and privacy".

"Start an extra session" shows "Start another session now?" with "Start" and "Not now".

"Microphone and privacy" keeps the existing explanation of what the microphone is used for, adds a switch labelled "Listening for speaking attempts", and renames the reset controls: button "Erase everything on this tablet", question "Erase all learning history, voice recordings and settings on this tablet?", buttons "Erase" and "Keep everything".

No raw pair ID, count, date or duration appears on any of these screens. The existing per-pair "Family voice" button and `FamilyVoiceScreen` stay reachable from each "Noticed at home" row until the guide-voice plan replaces them with the Voices screen.

- [ ] **Step 1: Implement the three screens and the navigation in `MioziraApp`** (a small `enum class ParentPage { HOME, LANGUAGES, MICROPHONE }` held with `remember`, reset to `HOME` whenever access returns to `Locked`).
- [ ] **Step 2: Build** `./gradlew :shared:testDebugUnitTest :androidApp:assembleDebug :androidApp:checkManifestPolicy` — expect BUILD SUCCESSFUL.
- [ ] **Step 3: Manual check on an emulator**, noting each result in the commit body:
  - before any session: the home shows "Miozira will describe his sessions here." and no four-words card;
  - after a session: sentences appear, with no number, date, duration or pair ID anywhere on any parent screen;
  - "He understood it" answers "Saved.";
  - the listening switch asks for the microphone once and stays off if refused;
  - "Start an extra session" → "Start" returns to a waiting child picture and the parent area is locked;
  - "Erase everything on this tablet" still needs its own confirmation.
- [ ] **Step 4: Update the docs:** tick the AC-H07 items now true; in `CLAUDE.md` state that the parent area has the two-step gate and sentence-based home, and that Voices and the puzzle page are not built.
- [ ] **Step 5: Commit** `feat: parent home in sentences with languages and privacy pages`.

---

## Not in this plan

- The Voices screen (guide phrases by both parents, Tamil words) and "How the puzzles are going" — with the guide-voice and puzzles plan.
- The "Said before the app did" line — with "say it first".
- A parent-chosen PIN in place of the reading step — when he can read number words.
