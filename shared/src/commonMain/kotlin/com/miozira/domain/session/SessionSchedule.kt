package com.miozira.domain.session

import com.miozira.content.LanguageId
import com.miozira.core.result.AppResult
import com.miozira.core.time.Clock
import com.miozira.core.time.LocalDay

class SessionSchedule(
    private val history: SessionHistory,
    private val localDay: LocalDay,
    private val clock: Clock,
) {
    // A failed read counts as not finished, so a read error never locks the child out.
    suspend fun todayIsFinished(): Boolean {
        val last = (history.lastCompletedAtMs() as? AppResult.Success)?.value ?: return false
        val now = clock.nowMs()
        // A completion stamped after now means the clock moved back; it is not today's session.
        if (last > now) return false
        return isSameDay(last, now)
    }

    suspend fun languagesForNextSession(ready: List<LanguageId>): Pair<LanguageId, LanguageId>? {
        val completed = (history.completedSessionCount() as? AppResult.Success)?.value ?: 0
        return LanguageRotation.languagesFor(completed, ready)
    }

    fun isSameDay(aMs: Long, bMs: Long): Boolean = localDay.epochDayOf(aMs) == localDay.epochDayOf(bMs)
}
