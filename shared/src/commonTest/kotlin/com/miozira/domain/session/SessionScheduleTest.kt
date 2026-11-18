package com.miozira.domain.session

import com.miozira.content.LanguageId
import com.miozira.core.result.AppError
import com.miozira.core.result.AppResult
import com.miozira.core.time.Clock
import com.miozira.core.time.LocalDay
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

private const val DAY_MS = 86_400_000L
private val localDay = LocalDay { it / DAY_MS }
private val five = listOf("en", "ta", "ja", "ar", "es").map(::LanguageId)

private class FakeHistory(
    var count: AppResult<Int> = AppResult.Success(0),
    var lastCompleted: AppResult<Long?> = AppResult.Success(null),
) : SessionHistory {
    override suspend fun completedSessionCount(): AppResult<Int> = count
    override suspend fun lastCompletedAtMs(): AppResult<Long?> = lastCompleted
}

private class FakeClock(var now: Long) : Clock {
    override fun nowMs(): Long = now
}

class SessionScheduleTest {
    private val noon = 10 * DAY_MS + DAY_MS / 2

    private fun schedule(history: FakeHistory, now: Long) =
        SessionSchedule(history, localDay, FakeClock(now))

    @Test fun completedEarlierToday_isFinished() = runBlocking {
        val history = FakeHistory(lastCompleted = AppResult.Success(10 * DAY_MS + 1_000))
        assertTrue(schedule(history, noon).todayIsFinished())
    }

    @Test fun completedYesterday_isNotFinished() = runBlocking {
        val history = FakeHistory(lastCompleted = AppResult.Success(9 * DAY_MS + DAY_MS - 1))
        assertFalse(schedule(history, noon).todayIsFinished())
    }

    @Test fun neverCompleted_isNotFinished() = runBlocking {
        assertFalse(schedule(FakeHistory(), noon).todayIsFinished())
    }

    @Test fun lastCompletionInTheFuture_isNotToday() = runBlocking {
        val history = FakeHistory(lastCompleted = AppResult.Success(11 * DAY_MS + 1_000))
        assertFalse(schedule(history, noon).todayIsFinished())
    }

    @Test fun historyReadFails_isNotFinished() = runBlocking {
        val history = FakeHistory(
            lastCompleted = AppResult.Failure(AppError.Unexpected("boom")),
        )
        assertFalse(schedule(history, noon).todayIsFinished())
    }

    @Test fun languages_advanceWithCompletedSessions() = runBlocking {
        val history = FakeHistory(count = AppResult.Success(0))
        val first = schedule(history, noon).languagesForNextSession(five)
        assertEquals(LanguageId("en") to LanguageId("ta"), first)

        history.count = AppResult.Success(1)
        val second = schedule(history, noon).languagesForNextSession(five)
        assertEquals(LanguageId("ja") to LanguageId("ar"), second)
    }

    @Test fun languages_failedCountFallsBackToZero() = runBlocking {
        val history = FakeHistory(count = AppResult.Failure(AppError.Unexpected("boom")))
        assertEquals(
            LanguageId("en") to LanguageId("ta"),
            schedule(history, noon).languagesForNextSession(five),
        )
    }

    @Test fun languages_noReadyLanguages_isNull() = runBlocking {
        assertNull(schedule(FakeHistory(), noon).languagesForNextSession(emptyList()))
    }

    @Test fun isSameDay_comparesLocalDays() {
        val schedule = schedule(FakeHistory(), noon)
        assertTrue(schedule.isSameDay(10 * DAY_MS, 10 * DAY_MS + DAY_MS - 1))
        assertFalse(schedule.isSameDay(10 * DAY_MS - 1, 10 * DAY_MS))
    }
}
