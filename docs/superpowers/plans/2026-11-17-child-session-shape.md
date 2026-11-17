# Child Session Shape Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Turn today's one-word child screen into a whole session that runs by itself: warm-up, two language blocks chosen by rotation, the written word and his-turn ring on every word, an ending, and a quiet screen for the rest of the day, in landscape only.

**Architecture:** The existing pure reducer (`InteractionState.kt`) keeps owning one word's phases. A pure step list (`sessionSteps`) and a pure language rotation decide what comes next; `LearningSessionEngine` picks pairs per language block and now ends sessions; `ChildSessionController` drives the reducer with injected pauses so every timing is testable without a clock. The Compose layer only renders `ChildScreenState`.

**Tech Stack:** Kotlin Multiplatform, Compose Multiplatform, Room, kotlinx-coroutines; tests are `kotlin.test` in `shared/src/commonTest` (run with `runBlocking` and `Dispatchers.Unconfined`, as the existing tests do) and Room tests in `shared/src/androidInstrumentedTest`.

**Spec:** `docs/superpowers/specs/2026-11-16-ui-ux-design.md` (§5.1, §5.2, §6, §7) and `docs/superpowers/specs/2026-11-16-five-languages-and-thinking-puzzles-design.md` (§7.1, §7.2). This is product step 2, child side. The parent side is `2026-11-17-parent-area.md`.

## What exists today (read before starting)

- `ChildSessionController.start()` begins **one** interaction and nothing ever begins a second. `acknowledgeResponse()` leaves the reducer in `TRANSITIONING` for good. There is no multi-word session yet.
- `speakingInviteEnabled` defaults to `false` and `MainActivity` never sets it; nothing in production calls `completeInvitation`. The invite and listening phases exist only in tests.
- `LearningSessionEngine` never reuses a pair (`usedPairIds`), so it runs dry after 16 words, and it cannot end a session.
- The manifest handles orientation changes but does not lock orientation.

## Global Constraints

- `android.permission.INTERNET` stays absent. No new dependency.
- Child microphone audio stays in memory. This plan adds no audio capture code; it only decides when the existing detector runs.
- Nothing is shown, coloured or sounded as right or wrong. No score, streak, timer, progress bar or count is visible to the child.
- The only sounds are voices. Add no effect sound and no music.
- Colours: background `#F2EEE7`, his-turn ring `#607C8A`, written word while spoken `#4F746B`, written word at rest `#26312E`.
- The ring belongs to the picture; the written word always sits below the ring and never touches it.
- Landscape only; the screen does not turn.
- No Japanese, Arabic or Spanish word may reach the child before a native speaker has checked it. This plan builds the gate; it adds no words in those languages.
- Exposure and evidence writes stay idempotent; audio and microphone callbacks are accepted only when their session and interaction IDs match the current interaction.
- UI never calls DAOs.
- Every timing lives in `SessionTimings` and every count in `SessionShape`; no magic numbers elsewhere.
- Do not touch `tools/audio/`, the bundled `.m4a` clips, or the Tamil wording in `ContentRepository.kt`; the clear-voices plan owns those.
- Work on a branch named `feat/child-session-shape`. The working tree has unrelated uncommitted changes, so stage only the files each task names.
- Unit tests: `./gradlew :shared:testDebugUnitTest`. Room tests (need an emulator or device): `./gradlew :shared:connectedDebugAndroidTest`.

## Decisions made in this plan (not in the spec)

- **Resume after the app is killed is not built.** An unfinished session resumes at the same word only while the app process is alive. If Android kills the process, the existing recovery closes that session and the next opening starts the same two languages again from the warm-up (the rotation has not advanced). Upgrade path: persist the step index on the session row.
- **System fonts, not bundled fonts.** The UI/UX design proposes carrying fonts in the app (a designer's call, not the parent's). This plan uses the tablet's fonts with the language set on the text, and Task 8 ends with a manual check on the Xiaomi Pad. Bundle Noto fonts only if a script renders wrongly there.
- **No screen wake lock.** The app never keeps the screen on, so there is nothing to release on the quiet screen; the tablet's own timeout dims it.
- **`sensorLandscape`.** The screen may flip between the two sideways directions but never goes upright.
- **Rotation uses languages that are ready.** With only English and Tamil ready, every session is English then Tamil.

## Review Focus

1. Every clip fails to play (missing or corrupt files): the session must not race silently to the end and be recorded as completed, which would lock him out for the day. Pinned in Task 7 (`threeAudioFailuresInARow_endSessionWithoutMarkingTodayFinished`).
2. The tablet's clock or time zone changes, or a session crosses midnight: the same-day rule must never throw or lock him out permanently. Pinned in Task 5 (`lastCompletionInTheFuture_isNotToday`).
3. Rapid repeated taps while a word plays or during his turn: never two voices at once, never a second exposure for the same interaction. Pinned in Tasks 6 and 7.
4. Only one language is ready, or none: the session runs both blocks in the one language, or shows the quiet screen, and never crashes. Pinned in Tasks 1 and 7.
5. The app is backgrounded while the microphone window is open: microphone and audio stop, no "no attempt" evidence is invented, and he returns to the same word. Pinned in Task 7 (`backgroundDuringTurn_stopsMicAndResumesSameWord`).

---

### Task 1: Language rotation

**Files:**
- Create: `shared/src/commonMain/kotlin/com/miozira/domain/session/LanguageRotation.kt`
- Test: `shared/src/commonTest/kotlin/com/miozira/domain/session/LanguageRotationTest.kt`

**Interfaces:**
- Produces: `object LanguageRotation { fun languagesFor(completedSessions: Int, available: List<LanguageId>): Pair<LanguageId, LanguageId>? }`

- [x] **Step 1: Write the failing tests**

```kotlin
private val five = listOf("en", "ta", "ja", "ar", "es").map(::LanguageId)
private fun ids(vararg v: String) = v.map(::LanguageId)

@Test fun fiveLanguages_followTheStream() {
    val got = (0..4).map { LanguageRotation.languagesFor(it, five)!! }
    assertEquals(listOf("en" to "ta", "ja" to "ar", "es" to "en", "ta" to "ja", "ar" to "es"),
        got.map { it.first.value to it.second.value })
}
@Test fun anyFiveSessionsInARow_useEachLanguageTwice() {
    for (start in 0..30) {
        val used = (start until start + 5).flatMap { LanguageRotation.languagesFor(it, five)!!.toList() }
        five.forEach { lang -> assertEquals(2, used.count { it == lang }) }
    }
}
@Test fun neverTheSameLanguageTwiceInOneSession_whenTwoOrMoreAreReady() {
    for (n in 2..5) for (k in 0..40) LanguageRotation.languagesFor(k, five.take(n))!!.let { assertNotEquals(it.first, it.second) }
}
@Test fun fourLanguages_doNotAlwaysKeepTheSamePartner() {
    val partnersOfFirst = (0..7).map { LanguageRotation.languagesFor(it, five.take(4))!! }
        .filter { five[0] in it.toList() }.map { (it.toList() - five[0]).single() }.toSet()
    assertTrue(partnersOfFirst.size >= 2)
}
@Test fun twoLanguages_areAlwaysEnglishThenTamil() =
    assertEquals(LanguageId("en") to LanguageId("ta"), LanguageRotation.languagesFor(7, ids("en", "ta")))
@Test fun oneLanguage_isUsedForBothBlocks() =
    assertEquals(LanguageId("en") to LanguageId("en"), LanguageRotation.languagesFor(3, ids("en")))
@Test fun noLanguages_givesNull() = assertNull(LanguageRotation.languagesFor(0, emptyList()))
```

- [x] **Step 2: Run** `./gradlew :shared:testDebugUnitTest --tests '*LanguageRotationTest'` — expect FAIL (unresolved `LanguageRotation`).

- [x] **Step 3: Implement `languagesFor`.** The rule (the tests do not fully determine it):

```kotlin
val n = available.size
if (n == 0) return null
if (n == 1) return available[0] to available[0]
val shift = if (n % 2 == 0 && n > 2) completedSessions / (n / 2) else 0
val i = 2 * completedSessions + shift
return available[i.mod(n)] to available[(i + 1).mod(n)]
```

- [x] **Step 4: Run the same command** — expect PASS.
- [x] **Step 5: Commit** `feat: add language rotation for two languages per session`.

---

### Task 2: Five languages in the content model, with a readiness gate

**Files:**
- Modify: `shared/src/commonMain/kotlin/com/miozira/content/ContentRepository.kt`
- Modify: `shared/src/commonMain/kotlin/com/miozira/data/db/ContentSeedService.kt` (the `languages` list)
- Test: `shared/src/commonTest/kotlin/com/miozira/content/LanguageReadinessTest.kt`
- Modify: `shared/src/androidInstrumentedTest/kotlin/com/miozira/data/db/SchemaAndSeedTest.kt` (language count)

**Interfaces:**
- Produces, top level in `ContentRepository.kt`:
  `val languageOrder: List<LanguageId>` = en, ta, ja, ar, es;
  `val languageNames: Map<LanguageId, String>` = English, Tamil, Japanese, Arabic, Spanish;
  `fun readyLanguages(pairs: List<ConceptLanguagePair>, conceptCount: Int = 8): List<LanguageId>`;
  on the object: `PrototypeContentRepository.readyLanguages(): List<LanguageId>` and `pairsFor(languageId: LanguageId): List<ConceptLanguagePair>`.

A language is ready when it has exactly one pair for each of the `conceptCount` concepts and, for `ja`, `ar` and `es`, every one of those pairs has `audio.reviewStatus` of `NATIVE_REVIEWED` or `APPROVED`. English and Tamil are ready at any review status (the family judges those by ear). The result keeps `languageOrder`.

- [x] **Step 1: Write the failing tests** (build fake pairs with a small helper `fakePair(concept, language, status)`):

```kotlin
@Test fun bundledContent_hasEnglishAndTamilReady() =
    assertEquals(listOf(LanguageId("en"), LanguageId("ta")), PrototypeContentRepository.readyLanguages())
@Test fun japaneseInDraft_isNotReady() =
    assertFalse(LanguageId("ja") in readyLanguages(eightConcepts("en", DRAFT) + eightConcepts("ja", DRAFT)))
@Test fun japaneseNativeReviewed_isReady_andOrderIsKept() =
    assertEquals(listOf(LanguageId("en"), LanguageId("ja")),
        readyLanguages(eightConcepts("ja", NATIVE_REVIEWED) + eightConcepts("en", DRAFT)))
@Test fun oneUncheckedWord_keepsTheWholeLanguageOut() =
    assertFalse(LanguageId("ar") in readyLanguages(eightConcepts("ar", NATIVE_REVIEWED).drop(1) + fakePair("apple", "ar", DRAFT)))
@Test fun sevenOfEightWords_isNotReady() =
    assertFalse(LanguageId("es") in readyLanguages(eightConcepts("es", APPROVED).drop(1)))
```

- [x] **Step 2: Run** `./gradlew :shared:testDebugUnitTest --tests '*LanguageReadinessTest'` — expect FAIL.
- [x] **Step 3: Implement** the three top-level declarations and the two object functions. In `ContentSeedService`, seed all five `LanguageEntity` rows (`ja`/"Japanese", `ar`/"Arabic", `es`/"Spanish" added); inserts are already `IGNORE`, so existing databases gain the rows on next start and no migration is needed. Update the language-count assertion in `SchemaAndSeedTest` from 2 to 5.
- [x] **Step 4: Run** `./gradlew :shared:testDebugUnitTest` — expect PASS; `./gradlew :shared:compileDebugAndroidTestKotlin` — expect BUILD SUCCESSFUL.
- [x] **Step 5: Commit** `feat: model five languages and gate unchecked ones`.

---

### Task 3: The step list of one session

**Files:**
- Create: `shared/src/commonMain/kotlin/com/miozira/domain/session/SessionSteps.kt`
- Test: `shared/src/commonTest/kotlin/com/miozira/domain/session/SessionStepsTest.kt`

**Interfaces:**
- Produces:
  `data class SessionShape(val warmUpTurns: Int = 2, val blockTurns: Int = 10, val endingTurns: Int = 1)`;
  `enum class SessionPart { WARM_UP, FIRST_LANGUAGE, SECOND_LANGUAGE, ENDING }`;
  `sealed interface SessionStep { data class Word(val part: SessionPart, val language: LanguageId, val easy: Boolean) : SessionStep; data object Breath : SessionStep }`;
  `fun sessionSteps(shape: SessionShape, first: LanguageId, second: LanguageId): List<SessionStep>`.

Order: `warmUpTurns` easy words in `first`; `blockTurns` words in `first`; one `Breath`; `blockTurns` words in `second`; `endingTurns` easy words in `second`. The thinking puzzle (a later plan) goes between two breaths at the position of the single `Breath`.

- [x] **Step 1: Write the failing test**

```kotlin
@Test fun steps_followWarmUpFirstBreathSecondEnding() {
    val en = LanguageId("en"); val ta = LanguageId("ta")
    assertEquals(
        listOf(
            SessionStep.Word(SessionPart.WARM_UP, en, easy = true),
            SessionStep.Word(SessionPart.FIRST_LANGUAGE, en, easy = false),
            SessionStep.Word(SessionPart.FIRST_LANGUAGE, en, easy = false),
            SessionStep.Breath,
            SessionStep.Word(SessionPart.SECOND_LANGUAGE, ta, easy = false),
            SessionStep.Word(SessionPart.SECOND_LANGUAGE, ta, easy = false),
            SessionStep.Word(SessionPart.ENDING, ta, easy = true),
        ),
        sessionSteps(SessionShape(warmUpTurns = 1, blockTurns = 2, endingTurns = 1), en, ta),
    )
}
@Test fun defaultShape_isTwentyThreeWordsAndOneBreath() {
    val steps = sessionSteps(SessionShape(), LanguageId("en"), LanguageId("ta"))
    assertEquals(23, steps.count { it is SessionStep.Word }); assertEquals(1, steps.count { it is SessionStep.Breath })
}
```

- [x] **Step 2: Run** `--tests '*SessionStepsTest'` — expect FAIL. **Step 3: Implement.** **Step 4: Run** — expect PASS.
- [x] **Step 5: Commit** `feat: describe a session as a list of steps`.

---

### Task 4: The engine picks per language block and can end a session

**Files:**
- Modify: `shared/src/commonMain/kotlin/com/miozira/domain/session/LearningSessionEngine.kt`
- Modify: `shared/src/commonMain/kotlin/com/miozira/domain/adaptive/AdaptiveEngine.kt`, `AdaptivePolicy.kt`
- Modify: `shared/src/commonMain/kotlin/com/miozira/data/db/dao/SessionDao.kt`, `data/repository/RoomSessionRepository.kt`, `data/repository/RoomSessionPersistence.kt`
- Test: `shared/src/commonTest/kotlin/com/miozira/domain/session/LearningSessionEngineTest.kt` (create), existing `AdaptiveSelectionTest.kt`, `AdaptiveSessionIntegrationTest.kt`
- Test: `shared/src/androidInstrumentedTest/kotlin/com/miozira/data/repository/SessionEndTest.kt` (create)

**Interfaces:**
- Produces:
  `enum class SessionEndReason { COMPLETED, IDLE, CONTENT_ERROR }`;
  `data class PairRequest(val available: List<ConceptLanguagePair>, val recentPairIds: List<PairId>, val language: LanguageId?, val easy: Boolean, val newPairsInBlock: Int)`;
  `fun interface PairPlanner { suspend fun selectNext(request: PairRequest): PairSelection? }` (replaces the three-argument form);
  `LearningSessionEngine.beginInteraction(step: SessionStep.Word, allowSpeakingInvite: Boolean = true): AppResult<InteractionPlan>`;
  `LearningSessionEngine.endSession(reason: SessionEndReason): AppResult<Unit>` (after it, `startSession` may be called again);
  `SessionPersistence.endSession(sessionId: SessionId, reason: SessionEndReason, endedAtMs: Long): AppResult<Unit>`;
  `interface SessionHistory { suspend fun completedSessionCount(): AppResult<Int>; suspend fun lastCompletedAtMs(): AppResult<Long?> }`, implemented by `RoomSessionRepository`;
  `AdaptivePolicy.maxNewPairsPerBlock: Int = 3`.

Decisions:
- The engine keeps an ordered history of this session's pairs instead of the `usedPairIds` set. `recentPairIds` is the last two; a pair may come back later in the same session.
- `newPairsInBlock` counts selections in the current `SessionPart` whose reason is `NEW_INTRODUCTION`; it resets when the part changes.
- `AdaptivePairPlanner` restricts candidates to `request.language`, passes `needsEasyOpener = request.easy` and `newPairsSelected = request.newPairsInBlock`. `AdaptiveEngine` uses `policy.maxNewPairsPerBlock` where it now hard-codes `2`.
- With no planner, the fallback is the first pair in the requested language that is not in `recentPairIds`.
- SQL: `UPDATE session SET ended_at_ms = :endedAtMs, completion_reason = :reason WHERE session_id = :sessionId AND ended_at_ms IS NULL`; `SELECT COUNT(*) FROM session WHERE completion_reason = 'COMPLETED'`; `SELECT MAX(ended_at_ms) FROM session WHERE completion_reason = 'COMPLETED'`. No schema change, so the Room version stays 1.

- [x] **Step 1: Write the failing engine tests** (reuse the `RecordingPersistence` pattern from `ChildSessionControllerTest`, adding `ended: MutableList<Pair<SessionId, SessionEndReason>>`):

```kotlin
@Test fun wordsComeFromTheRequestedLanguage() // begin 3 Word(FIRST_LANGUAGE, ta) steps -> every plan.pair.languageId == ta
@Test fun aPairIsNotRepeatedWithinTwoTurns_butMayReturnLater() // 6 turns in "en" with 8 pairs: no pair equals either of the two before it
@Test fun engineNeverRunsDry() // 40 turns in "en" -> all Success
@Test fun endSession_isRecordedOnce_andANewSessionCanStart() // endSession(COMPLETED) -> ended.size == 1; startSession() is Success
@Test fun endSession_withoutAnActiveSession_isInvalidState()
```

and in `AdaptiveSelectionTest`: change the expectation for the new-pair cap from 2 to 3 and add `candidatesOutsideTheRequestedLanguage_areNeverChosen`.

- [x] **Step 2: Run** `./gradlew :shared:testDebugUnitTest` — expect FAIL (compile errors on the new signatures).
- [x] **Step 3: Implement** the engine, planner and policy changes; update the callers in `AdaptiveSessionIntegrationTest` and `MainActivity` to the new `PairPlanner` signature so the build compiles.
- [x] **Step 4: Write the Room test** `SessionEndTest`: create a session, end it twice with `COMPLETED` (second call changes nothing and still returns Success), assert `completedSessionCount() == 1` and `lastCompletedAtMs()` equals the first end time; a session closed by `RoomRecoveryService` must not be counted as completed.
- [ ] **Step 5: Run** `./gradlew :shared:testDebugUnitTest` — expect PASS; `./gradlew :shared:connectedDebugAndroidTest` — expect PASS (if no emulator is available, run `:shared:compileDebugAndroidTestKotlin` and say so in the commit body). — NOT DONE: connected Room run needs a device or emulator, none was attached; the unit run and instrumented compile are done.
- [x] **Step 6: Commit** `feat: pick words per language block and end sessions`.

---

### Task 5: Has today's session already finished?

**Files:**
- Create: `shared/src/commonMain/kotlin/com/miozira/core/time/LocalDay.kt`
- Create: `shared/src/androidMain/kotlin/com/miozira/core/time/AndroidLocalDay.kt`
- Create: `shared/src/commonMain/kotlin/com/miozira/domain/session/SessionSchedule.kt`
- Test: `shared/src/commonTest/kotlin/com/miozira/domain/session/SessionScheduleTest.kt`

**Interfaces:**
- Consumes: `SessionHistory` (Task 4), `LanguageRotation` (Task 1), `Clock`.
- Produces:
  `fun interface LocalDay { fun epochDayOf(epochMs: Long): Long }`; `object AndroidLocalDay : LocalDay` (system time zone, `java.time`);
  `class SessionSchedule(history: SessionHistory, localDay: LocalDay, clock: Clock)` with
  `suspend fun todayIsFinished(): Boolean`,
  `suspend fun languagesForNextSession(ready: List<LanguageId>): Pair<LanguageId, LanguageId>?`,
  `fun isSameDay(aMs: Long, bMs: Long): Boolean`.

`todayIsFinished` is true only when the last completed session ended on the same local day as now. A failed history read counts as not finished (never lock him out because of a read error).

- [x] **Step 1: Write the failing tests** with a fake `LocalDay { it / 86_400_000 }`:

```kotlin
@Test fun completedEarlierToday_isFinished()
@Test fun completedYesterday_isNotFinished()
@Test fun neverCompleted_isNotFinished()
@Test fun lastCompletionInTheFuture_isNotToday()      // clock moved back a day
@Test fun historyReadFails_isNotFinished()
@Test fun languages_advanceWithCompletedSessions()     // count 0 -> en,ta ; count 1 -> ja,ar with five ready
```

- [x] **Step 2: Run** `--tests '*SessionScheduleTest'` — expect FAIL. **Step 3: Implement.** **Step 4: Run** — expect PASS.
- [x] **Step 5: Commit** `feat: decide whether today's session is finished`.

---

### Task 6: Reducer — replay during his turn, and a failed clip skips the word

**Files:**
- Modify: `shared/src/commonMain/kotlin/com/miozira/domain/session/InteractionState.kt`
- Modify: `shared/src/commonMain/kotlin/com/miozira/domain/session/LearningSessionEngine.kt` (`SessionPersistence.recordReplay`), `data/repository/RoomSessionPersistence.kt`, and `RoomLearningRepository.kt` if it has no replay method (delegate to the existing `InteractionDao.incrementReplayAndRebuild`)
- Test: `shared/src/commonTest/kotlin/com/miozira/domain/session/InteractionReducerTest.kt`

**Interfaces:**
- Produces: `InteractionEffect.RecordReplay`; `SessionPersistence.recordReplay(interactionId: InteractionId): AppResult<Unit>`.
- Changed transitions:
  - `INVITING`, `LISTENING`, `RESPONDING` + `ReplayRequested` with matching IDs → `PLAYING_WORD`, `replayCount + 1`, effects `[StopListening, PlayCanonicalWord, RecordReplay]`. Non-matching IDs → unchanged with `DiagnosticInvalidEvent`.
  - `PLAYING_WORD` + `AudioPlaybackFailed` → `TRANSITIONING` with `completionReason = CONTENT_ERROR` and effect `CompleteInteraction` (was: end the session).

- [x] **Step 1: Write the failing tests**

```kotlin
@Test fun replayDuringListening_replaysTheWord_andCountsTheReplay()
@Test fun afterAReplay_theExposureThresholdDoesNotCommitAgain() // exposureCommitted stays true -> no CommitExposure effect
@Test fun replayWithStaleIds_isIgnored()
@Test fun failedClip_completesTheInteractionAsContentError_andDoesNotEndTheSession()
```

- [x] **Step 2: Run** `--tests '*InteractionReducerTest'` — expect FAIL. **Step 3: Implement.** **Step 4: Run** `./gradlew :shared:testDebugUnitTest` — expect PASS (fix the one existing test that expected `SESSION_ENDING` after a failed clip).
- [x] **Step 5: Commit** `feat: allow replay during his turn and skip words whose clip fails`.

---

### Task 7: The controller runs a whole session

**Files:**
- Create: `shared/src/commonMain/kotlin/com/miozira/presentation/child/ChildScreenState.kt`
- Create: `shared/src/commonMain/kotlin/com/miozira/domain/session/SessionTimings.kt`
- Modify: `shared/src/commonMain/kotlin/com/miozira/domain/session/ChildSessionController.kt` (rewrite)
- Test: `shared/src/commonTest/kotlin/com/miozira/domain/session/ChildSessionControllerTest.kt` (rewrite, keeping the stale-callback tests)

**Interfaces:**
- Consumes: Tasks 2–6.
- Produces:

```kotlin
sealed interface ChildScreenState {
    data object Loading : ChildScreenState
    data class Word(
        val pair: ConceptLanguagePair,
        val phase: InteractionPhase,
        val wordVisible: Boolean,   // false until the word has started playing in this interaction
        val ringVisible: Boolean,   // true exactly while phase == LISTENING
        val nod: Boolean,           // true for nodMs after ATTEMPT_DETECTED
    ) : ChildScreenState
    data object Breath : ChildScreenState
    data object Quiet : ChildScreenState
}

data class SessionTimings(
    val invitePauseMs: Long = 600,     // stand-in for the spoken "your turn" (guide-voice plan replaces it)
    val turnMs: Long = 4_000,
    val nodMs: Long = 700,
    val afterTurnMs: Long = 1_000,
    val breathMs: Long = 1_000,
    val idleQuietMs: Long = 60_000,
    val maxClipFailuresInARow: Int = 3,
)

class ChildSessionController(
    engine: LearningSessionEngine,
    persistence: SessionPersistence,
    schedule: SessionSchedule,
    audioService: AudioService,
    clock: Clock,
    scope: CoroutineScope,
    readyLanguages: () -> List<LanguageId>,
    measurement: suspend () -> AttemptMeasurementDecision,
    speechAttemptDetector: SpeechAttemptDetector? = null,
    shape: SessionShape = SessionShape(),
    timings: SessionTimings = SessionTimings(),
    pause: suspend (Long) -> Unit = { kotlinx.coroutines.delay(it) },
) {
    val state: StateFlow<ChildScreenState>
    suspend fun open()                 // app start and every return to the foreground
    fun tap()
    suspend fun startExtraSession()    // parent area; ignores todayIsFinished
    fun onBackground()
}
```

Behaviour, in the order one word runs:
1. `open()`: if a session is paused in this process and it began today, show its current word again. Otherwise, if `schedule.todayIsFinished()` → `Quiet`. Otherwise start a session with `schedule.languagesForNextSession(readyLanguages())`; `null` → `Quiet`.
2. `Word` in `READY`: `tap()` plays the word. `wordVisible` becomes true.
3. Word finished → `INVITING` for `invitePauseMs` → `LISTENING` for at most `turnMs`. `measurement()` decides: `OPEN_WINDOW` starts the detector; the other two record that state at once but **still hold `LISTENING` for `turnMs`** so the ring looks the same with the microphone off.
4. `ATTEMPT_DETECTED` → `nod = true` for `nodMs`. Any other result → no visible change.
5. `afterTurnMs` later the interaction is completed and the next step begins. `Breath` shows for `breathMs`. After the last step: `endSession(COMPLETED)`, then `Quiet`.
6. `tap()` in `INVITING`, `LISTENING` or `RESPONDING` sends `ReplayRequested`, cancels the running turn timer and restarts from step 2 without a new exposure. `tap()` in `PLAYING_WORD`, `Breath`, `Loading`, or `Quiet` with nothing paused does nothing.
7. No tap for `idleQuietMs` while in `READY` → `Quiet`, session kept paused in memory. A later `tap()`: same local day as the session start → back to that word in `READY`; a different day → `endSession(IDLE)` and behave like `open()`.
8. A clip failure skips to the next step. `maxClipFailuresInARow` failures in a row → `endSession(CONTENT_ERROR)` and `Quiet`; this does not count as completed.
9. `onBackground()` stops audio and the detector and cancels timers; the word returns to `READY` on `open()`.

- [x] **Step 1: Write the failing tests.** Use shape `SessionShape(1, 2, 1)`, an audio fake that completes on demand (`emitThreshold`/`emitCompleted`, as today), and a controllable pause:

```kotlin
class Pauses { val pending = ArrayDeque<Pair<Long, CompletableDeferred<Unit>>>()
    val pause: suspend (Long) -> Unit = { ms -> CompletableDeferred<Unit>().also { pending += ms to it }.await() }
    fun finish(ms: Long) = pending.first { it.first == ms && !it.second.isCompleted }.second.complete(Unit) }
```

```kotlin
@Test fun fullSession_walksEveryStep_thenGoesQuiet_andIsRecordedCompletedOnce()
    // 6 interactions; languages en,en,en,ta,ta,ta; a Breath state is seen between the 3rd and 4th; final state Quiet; ended == [COMPLETED]
@Test fun writtenWord_isHiddenUntilTheWordPlays()
@Test fun ring_showsOnlyDuringHisTurn()
@Test fun silentTurn_movesOn_withNoNod()
@Test fun detectedAttempt_nodsOnce_thenMovesOn()
@Test fun microphoneOff_ringStillShowsForTheFullTurn_detectorNeverStarts_noNod()
@Test fun tapDuringHisTurn_replaysTheWord_withoutASecondExposure()        // audio.requests.size == 2, exposures == 1
@Test fun tapWhileTheWordPlays_isIgnored()                                   // audio.requests.size == 1
@Test fun aFailedClip_skipsToTheNextWord()
@Test fun threeAudioFailuresInARow_endSessionWithoutMarkingTodayFinished()   // ended == [CONTENT_ERROR]
@Test fun idleForAMinute_goesQuiet_andATapReturnsToTheSameWord()
@Test fun tapAfterIdleOnAnotherDay_endsTheOldSessionAsIdle_andStartsANewOne()
@Test fun openWhenTodayIsFinished_showsQuiet_andTapsDoNothing()
@Test fun startExtraSession_startsEvenWhenTodayIsFinished()
@Test fun noReadyLanguages_showsQuiet()
@Test fun oneReadyLanguage_runsBothBlocksInIt()
@Test fun backgroundDuringTurn_stopsMicAndResumesSameWord()                  // detector.stopAll called; no attempt recorded; same pair after open()
@Test fun staleAudioCallbacks_doNotCommitExposureForCurrentInteraction()     // kept from the existing file
```

- [x] **Step 2: Run** `--tests '*ChildSessionControllerTest'` — expect FAIL.
- [x] **Step 3: Implement.** Keep every state change going through `reduce`; the controller only reacts to effects and timers. Each timer is one `scope.launch { pause(ms); ... }` whose `Job` is cancelled on replay, background and step change, and whose body re-checks the interaction ID before acting.
- [x] **Step 4: Run** `./gradlew :shared:testDebugUnitTest` — expect PASS.
- [x] **Step 5: Commit** `feat: run a whole child session from warm-up to quiet`.

---

### Task 8: The word screen, breath and quiet screen

**Files:**
- Create: `shared/src/commonMain/kotlin/com/miozira/presentation/child/WordScreen.kt`
- Modify: `shared/src/commonMain/kotlin/com/miozira/presentation/child/ChildSessionScreen.kt` (the controller overload only; leave the overload used by `ContentReviewScreen`)

**Interfaces:**
- Consumes: `ChildScreenState`, `ChildSessionController.tap()`.
- Produces: `@Composable fun WordScreen(state: ChildScreenState.Word, onTap: () -> Unit)`; `ChildSessionScreen(controller, onParentGateUnlocked)` renders `Loading` and `Breath` and `Quiet` as the plain background and `Word` as `WordScreen`, keeping the existing top-right parent hotspot unchanged (the parent-area plan replaces it).

Layout and motion decisions (all inside one `BoxWithConstraints`, a centred `Column`):
- Picture box: square, 60% of the available height. The whole box is the tap target; no ripple (`indication = null`). Keep the existing `contentDescription` ("<word>. Tap to hear the word.") and `Role.Button`.
- Ring: drawn inside the picture box's bounds as a 3 dp circle stroke in `#607C8A`. Alpha animates 0 → 0.55 in 800 ms and scale 0.97 → 1.0 in 1600 ms when `ringVisible`, back out when not.
- Gap of 4% of the height, then the written word, so the word can never meet the ring.
- Written word: `state.pair.spokenForm`, one line, centred, font size = 10% of the available height converted with `toSp()` from dp so the system font scale does not change it. `TextStyle(localeList = LocaleList(state.pair.languageId.value), textDirection = TextDirection.Content)`. Shown only when `wordVisible`; fades in over 600 ms; colour `#4F746B` while `phase == PLAYING_WORD`, animating to `#26312E` otherwise. Reserve its height when hidden so the picture does not move.
- Picture scale: 1.02 on tap for 100 ms; 1.03 → 1.0 once when `nod`; one pulse to 1.03 after 8 s in `READY` without a tap, once per interaction.
- Changing words: cross-fade of 400 ms keyed on the interaction's pair.

- [ ] **Step 1: Implement** `WordScreen` and the `when (state)` in `ChildSessionScreen`.
- [ ] **Step 2: Build** `./gradlew :shared:testDebugUnitTest :androidApp:assembleDebug` — expect BUILD SUCCESSFUL.
- [ ] **Step 3: Manual check on an emulator in landscape** (install the debug APK). Confirm each line and note the result in the commit body:
  - the picture waits with no word under it; a tap plays the word and the word fades in teal, then turns dark;
  - the ring appears around the picture only, never touching the word, for about four seconds, and fades;
  - a second tap during the ring repeats the word;
  - nothing on screen is tappable except the picture; the breath and quiet screens are plain;
  - the longest Tamil word (`மூக்கு`) is not clipped and stays on one line.
- [ ] **Step 4: Commit** `feat: word screen with written word and his-turn ring`.

---

### Task 9: Landscape lock and wiring

**Files:**
- Modify: `androidApp/src/main/AndroidManifest.xml`
- Modify: `androidApp/src/main/kotlin/com/miozira/MainActivity.kt`
- Modify: `androidApp/src/androidTest/kotlin/com/miozira/ManifestPolicyTest.kt`
- Modify: `CLAUDE.md` ("Current Repository State")

**Interfaces:**
- Consumes: everything above.

- [ ] **Step 1: Add the failing instrumented assertion** in `ManifestPolicyTest`: the launcher activity's `screenOrientation` equals `ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE`.
- [ ] **Step 2: Lock the manifest:** add `android:screenOrientation="sensorLandscape"` to `.MainActivity`. Keep `configChanges`.
- [ ] **Step 3: Wire `MainActivity`:** build `SessionSchedule(RoomSessionRepository(...), AndroidLocalDay, AndroidClock)`; pass `readyLanguages = PrototypeContentRepository::readyLanguages`, the detector, and `measurement = { decideAttemptMeasurement(settings.isSpeechAttemptDetectionEnabled() == Success(true), permissionService.getMicrophonePermissionState()) }` using one shared `DataStoreSettingsRepository`. Replace `childSessionController.start()` with `open()`, and call `open()` from `onStart` in place of `onForeground()`. (Detection stays off until a parent turns it on; the parent-area plan adds that switch.)
- [ ] **Step 4: Run the full check:**
  `./gradlew :shared:testDebugUnitTest :shared:connectedDebugAndroidTest :androidApp:connectedDebugAndroidTest :androidApp:assembleDebug :androidApp:checkManifestPolicy` — expect BUILD SUCCESSFUL with `RECORD_AUDIO` still the only permission.
- [ ] **Step 5: Manual check on the Xiaomi Pad** (physical tablet; emulator results are not device proof): a whole English-then-Tamil session runs to the quiet screen with no adult help; reopening the app the same day shows the quiet screen; the screen never goes upright; Tamil renders correctly in the tablet's font.
- [ ] **Step 6: Update `CLAUDE.md`:** in "Current Repository State", state that the child session now runs a whole session (warm-up, two language blocks, ending, quiet screen) in landscape, and that puzzles, the guide voice and the three unchecked languages are not built.
- [ ] **Step 7: Commit** `feat: lock landscape and wire the whole-session controller`.

---

## Not in this plan

- "Say it first", "Which one?", several pictures per word — product step 3.
- The Malayalam guide voice (the spoken "your turn", closing phrase), the thinking puzzles, movement prompts — product step 4. Until then the turn is silent and the session ends without a closing phrase.
- Japanese, Arabic and Spanish words and clips — added as data once each batch is checked; the gate in Task 2 lets them in automatically.
- Replacing the six draft pictures — artwork, not code.
- The parent gate and parent screens — `2026-11-17-parent-area.md`.
