package com.miozira.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.miozira.content.PrototypeContentRepository
import com.miozira.core.model.AttemptState
import com.miozira.core.model.InteractionId
import com.miozira.core.model.PairId
import com.miozira.core.model.SessionId
import com.miozira.core.result.AppResult.Success
import com.miozira.data.db.ContentSeedService
import com.miozira.data.db.DatabaseFactory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlinx.coroutines.runBlocking
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PersistenceRestartTest {
    @Test
    fun committedEvidence_survivesDatabaseReopen_andRebuildsPairSummary() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val databaseName = "persistence-restart-test.db"
        context.deleteDatabase(databaseName)
        val pairId = PairId("water:ta")
        val interactionId = InteractionId("restart-interaction")

        val firstOpen = DatabaseFactory.create(context, databaseName)
        try {
            val database = firstOpen
            ContentSeedService(database.contentDao()) { 1_725_000_000_000L }
                .seedContent(PrototypeContentRepository.contentVersion)
            val sessions = RoomSessionRepository(database.sessionDao())
            val learning = RoomLearningRepository(database.interactionDao())
            val sessionId = SessionId("restart-session")
            sessions.createSession(
                sessionId,
                1_725_000_004_000L,
                PrototypeContentRepository.contentVersion,
                "prototype-0.1-v1",
            )
            sessions.createInteraction(
                interactionId,
                sessionId,
                pairId,
                0,
                "NEW_INTRODUCTION",
                1_725_000_004_100L,
            )
            learning.commitExposure(interactionId, 1_725_000_004_200L)
            learning.incrementReplay(interactionId)
            learning.recordAttempt(interactionId, AttemptState.ATTEMPT_DETECTED, 1_725_000_004_300L)
        } finally {
            firstOpen.close()
        }

        val reopened = DatabaseFactory.create(context, databaseName)
        try {
            val learning = RoomLearningRepository(reopened.interactionDao())
            assertIs<Success<Unit>>(learning.rebuildPairState(pairId, 1_725_000_005_000L))
            val summary = assertIs<Success<PairEvidenceSummary>>(learning.getPairEvidence(pairId)).value

            assertEquals(1, summary.validExposureCount)
            assertEquals(1, summary.replayCountTotal)
            assertEquals(1, summary.attemptDetectedCount)
            assertEquals(1_725_000_005_000L, summary.updatedAtMs)
        } finally {
            reopened.close()
            context.deleteDatabase(databaseName)
        }
        Unit
    }
}
