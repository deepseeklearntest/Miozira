package com.miozira.data.repository

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.miozira.content.LanguageId
import com.miozira.content.PrototypeContentRepository
import com.miozira.core.model.InteractionCompletionReason
import com.miozira.core.model.InteractionId
import com.miozira.core.model.PairId
import com.miozira.core.model.SessionId
import com.miozira.core.result.AppResult.Success
import com.miozira.core.time.Clock
import com.miozira.data.db.ContentSeedService
import com.miozira.data.db.DatabaseFactory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.runBlocking
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecoveryTest {
    @Test
    fun settingsSurviveReopen_andRecoveryClosesInterruptedSessionWithoutInventingEvidence() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val settingsFileName = "recovery-settings-test.preferences_pb"
        context.preferencesDataStoreFile(settingsFileName).delete()
        val firstScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

        try {
            val firstSettings = DataStoreSettingsRepository(
                PreferenceDataStoreFactory.create(scope = firstScope) {
                    context.preferencesDataStoreFile(settingsFileName)
                },
            )
            firstSettings.setSetupComplete(true)
            firstSettings.setStartingLanguage(LanguageId("ta"))
            firstSettings.setSpeechAttemptDetectionEnabled(true)
            firstScope.cancel()

            val secondScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
            try {
                val reopenedSettings = DataStoreSettingsRepository(
                    PreferenceDataStoreFactory.create(scope = secondScope) {
                        context.preferencesDataStoreFile(settingsFileName)
                    },
                )
                assertEquals(true, assertIs<Success<Boolean>>(reopenedSettings.isSetupComplete()).value)
                assertEquals("ta", assertIs<Success<LanguageId?>>(reopenedSettings.getStartingLanguage()).value?.value)
                assertEquals(true, assertIs<Success<Boolean>>(reopenedSettings.isSpeechAttemptDetectionEnabled()).value)
            } finally {
                secondScope.cancel()
            }

            val databaseName = "recovery-test.db"
            context.deleteDatabase(databaseName)
            val database = DatabaseFactory.create(context, databaseName)
            try {
                ContentSeedService(database.contentDao()) { 1_725_000_000_000L }
                    .seedContent(PrototypeContentRepository.contentVersion)
                val sessions = RoomSessionRepository(database.sessionDao())
                val learning = RoomLearningRepository(database.interactionDao())
                val sessionId = SessionId("interrupted-session")
                val complete = InteractionId("complete-interaction")
                val incomplete = InteractionId("incomplete-interaction")
                sessions.createSession(
                    sessionId,
                    1_725_000_002_000L,
                    PrototypeContentRepository.contentVersion,
                    "prototype-0.1-v1",
                )
                sessions.createInteraction(complete, sessionId, PairId("apple:en"), 0, "NEW_INTRODUCTION", 1_725_000_002_100L)
                sessions.createInteraction(incomplete, sessionId, PairId("ball:en"), 1, "NEW_INTRODUCTION", 1_725_000_002_200L)
                learning.commitExposure(complete, 1_725_000_002_300L)
                learning.completeInteraction(complete, InteractionCompletionReason.NORMAL, 1_725_000_002_400L)

                val recovery = RoomRecoveryService(database.recoveryDao(), FixedClock(1_725_000_003_000L))
                assertEquals(1, assertIs<Success<RecoverySummary>>(recovery.recoverInterruptedSessions()).value.closedSessionCount)

                assertEquals("APP_EXITED", database.recoveryDao().findSession(sessionId.value)?.completionReason)
                assertEquals(
                    "PROCESS_INTERRUPTION",
                    database.interactionDao().findInteraction(incomplete.value)?.completionReason,
                )
                assertEquals(false, database.interactionDao().findInteraction(incomplete.value)?.exposureCommitted)
                assertEquals(1, assertIs<Success<PairEvidenceSummary>>(
                    learning.getPairEvidence(PairId("apple:en")),
                ).value.validExposureCount)
                assertEquals(0, assertIs<Success<PairEvidenceSummary>>(
                    learning.getPairEvidence(PairId("ball:en")),
                ).value.validExposureCount)
            } finally {
                database.close()
                context.deleteDatabase(databaseName)
            }
        } finally {
            firstScope.cancel()
            context.preferencesDataStoreFile(settingsFileName).delete()
        }
        Unit
    }
}

private class FixedClock(
    private val value: Long,
) : Clock {
    override fun nowMs(): Long = value
}
