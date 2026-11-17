package com.miozira.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.miozira.content.PrototypeContentRepository
import com.miozira.core.model.SessionId
import com.miozira.core.result.AppResult.Success
import com.miozira.core.time.Clock
import com.miozira.data.db.ContentSeedService
import com.miozira.data.db.DatabaseFactory
import com.miozira.domain.session.SessionEndReason
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlinx.coroutines.runBlocking
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SessionEndTest {
    @Test
    fun endingTwiceIsRecordedOnce_andRecoveredSessionsAreNotCompleted() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val databaseName = "session-end-test.db"
        context.deleteDatabase(databaseName)
        val database = DatabaseFactory.create(context, databaseName)
        try {
            ContentSeedService(database.contentDao()) { 1_725_000_000_000L }
                .seedContent(PrototypeContentRepository.contentVersion)
            val sessions = RoomSessionRepository(database.sessionDao())
            val completed = SessionId("completed-session")
            val interrupted = SessionId("interrupted-session")
            val firstEnd = 1_725_000_010_000L
            val secondEnd = 1_725_000_020_000L

            sessions.createSession(completed, 1_725_000_000_000L, PrototypeContentRepository.contentVersion, "prototype-0.1-v1")
            assertIs<Success<Unit>>(sessions.endSession(completed, SessionEndReason.COMPLETED, firstEnd))
            assertIs<Success<Unit>>(sessions.endSession(completed, SessionEndReason.COMPLETED, secondEnd))

            sessions.createSession(interrupted, 1_725_000_030_000L, PrototypeContentRepository.contentVersion, "prototype-0.1-v1")
            RoomRecoveryService(database.recoveryDao(), Clock { 1_725_000_040_000L }).recoverInterruptedSessions()

            assertEquals(1, assertIs<Success<Int>>(sessions.completedSessionCount()).value)
            assertEquals(firstEnd, assertIs<Success<Long?>>(sessions.lastCompletedAtMs()).value)
        } finally {
            database.close()
            context.deleteDatabase(databaseName)
        }
        Unit
    }
}
