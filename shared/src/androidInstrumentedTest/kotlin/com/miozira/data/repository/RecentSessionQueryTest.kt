package com.miozira.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.miozira.content.PrototypeContentRepository
import com.miozira.core.model.InteractionId
import com.miozira.core.model.PairId
import com.miozira.core.model.SessionId
import com.miozira.core.result.AppResult
import com.miozira.data.db.ContentSeedService
import com.miozira.data.db.DatabaseFactory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlinx.coroutines.runBlocking
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecentSessionQueryTest {
    private suspend fun withDatabase(name: String, block: suspend (RoomSessionRepository, RoomLearningRepository, RoomParentDataSource) -> Unit) {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        context.deleteDatabase(name)
        val database = DatabaseFactory.create(context, name)
        try {
            ContentSeedService(database.contentDao()) { 1_725_000_000_000L }
                .seedContent(PrototypeContentRepository.contentVersion)
            block(
                RoomSessionRepository(database.sessionDao()),
                RoomLearningRepository(database.interactionDao()),
                RoomParentDataSource(database.interactionDao()),
            )
        } finally {
            database.close()
            context.deleteDatabase(name)
        }
    }

    private suspend fun RoomSessionRepository.session(id: String, startedAtMs: Long) {
        createSession(SessionId(id), startedAtMs, PrototypeContentRepository.contentVersion, "prototype-0.1-v1")
    }

    private suspend fun RoomSessionRepository.interaction(id: String, session: String, pair: String, index: Int, at: Long) {
        createInteraction(InteractionId(id), SessionId(session), PairId(pair), index, "NEW_INTRODUCTION", at)
    }

    @Test
    fun onlyTheLaterSessionsHeardInteractionsReturn() = runBlocking {
        withDatabase("recent-session-later.db") { sessions, learning, parent ->
            sessions.session("s1", 1_000L)
            sessions.interaction("s1-a", "s1", "apple:en", 0, 1_010L)
            learning.commitExposure(InteractionId("s1-a"), 1_020L)
            sessions.session("s2", 2_000L)
            sessions.interaction("s2-a", "s2", "ball:en", 0, 2_010L)
            sessions.interaction("s2-b", "s2", "cup:ta", 1, 2_020L)
            learning.commitExposure(InteractionId("s2-a"), 2_030L)

            val summary = (parent.mostRecentSession() as AppResult.Success).value
            assertEquals(listOf(PairId("ball:en")), summary!!.heardUnmeasured)
        }
    }

    @Test
    fun aLaterSessionWithNoHeardWordIsSkipped() = runBlocking {
        withDatabase("recent-session-skip.db") { sessions, learning, parent ->
            sessions.session("s1", 1_000L)
            sessions.interaction("s1-a", "s1", "apple:en", 0, 1_010L)
            learning.commitExposure(InteractionId("s1-a"), 1_020L)
            sessions.session("s2", 2_000L)
            sessions.interaction("s2-a", "s2", "ball:en", 0, 2_010L)

            val summary = (parent.mostRecentSession() as AppResult.Success).value
            assertEquals(listOf(PairId("apple:en")), summary!!.heardUnmeasured)
        }
    }

    @Test
    fun anEmptyDatabaseReturnsNull() = runBlocking {
        withDatabase("recent-session-empty.db") { _, _, parent ->
            assertNull((parent.mostRecentSession() as AppResult.Success).value)
        }
    }
}
