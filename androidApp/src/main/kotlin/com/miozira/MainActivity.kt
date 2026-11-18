package com.miozira

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.miozira.MioziraApp
import com.miozira.content.PrototypeContentRepository
import com.miozira.data.db.ContentSeedService
import com.miozira.data.db.DatabaseFactory
import com.miozira.data.repository.RoomRecoveryService
import com.miozira.data.repository.RoomLearningRepository
import com.miozira.data.repository.RoomSessionPersistence
import com.miozira.data.repository.RoomSessionRepository
import com.miozira.data.repository.RoomParentDataSource
import com.miozira.data.repository.RoomRealWorldObservationRepository
import com.miozira.data.repository.RoomFamilyRecordingRepository
import com.miozira.data.repository.DataStoreSettingsRepository
import com.miozira.data.repository.FamilyRecordingCleanup
import com.miozira.data.repository.RoomResetCoordinator
import com.miozira.domain.session.ChildSessionController
import com.miozira.domain.session.LearningSessionEngine
import com.miozira.domain.session.SessionSchedule
import com.miozira.core.time.AndroidLocalDay
import com.miozira.services.permissions.decideAttemptMeasurement
import com.miozira.domain.adaptive.AdaptivePairPlanner
import com.miozira.domain.transfer.RealWorldTransferService
import com.miozira.presentation.parent.ParentAccessController
import com.miozira.presentation.parent.ParentController
import com.miozira.presentation.parent.FamilyRecordingController
import com.miozira.presentation.parent.ParentActionsController
import com.miozira.domain.reset.ResetService
import com.miozira.services.files.AndroidPrivateFileStore
import com.miozira.services.recording.AndroidFamilyRecordingService
import com.miozira.services.microphone.AndroidSpeechAttemptDetector
import com.miozira.domain.adaptive.LearningState
import com.miozira.domain.adaptive.PairLearningState
import com.miozira.domain.adaptive.ScheduledPairCandidate
import com.miozira.core.time.AndroidClock
import com.miozira.services.audio.AndroidAudioService
import com.miozira.services.permissions.AndroidPermissionService
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import java.util.UUID
import androidx.datastore.preferences.preferencesDataStore

private val android.content.Context.mioziraSettingsDataStore by preferencesDataStore(name = "miozira_settings")

class MainActivity : ComponentActivity() {
    private val serviceScope = MainScope()
    private val parentAccess = ParentAccessController()
    private lateinit var audioService: AndroidAudioService
    private lateinit var database: com.miozira.data.db.MioziraDatabase
    private lateinit var childSessionController: ChildSessionController
    private lateinit var permissionService: AndroidPermissionService
    private lateinit var parentController: ParentController
    private lateinit var familyRecordingController: FamilyRecordingController
    private lateinit var parentActionsController: ParentActionsController
    private var startupDone = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        audioService = AndroidAudioService(applicationContext)
        val permissionHistory = getSharedPreferences("permission_history", MODE_PRIVATE)
        permissionService = AndroidPermissionService(
            activity = this,
            wasRequestedBefore = { permissionHistory.getBoolean("record_audio_requested", false) },
            markRequested = {
                permissionHistory.edit().putBoolean("record_audio_requested", true).apply()
            },
        )
        try {
            database = DatabaseFactory.create(applicationContext, "miozira.db")
        } catch (_: Exception) {
            setContent { StorageRecoveryScreen() }
            return
        }
        val childDetector = AndroidSpeechAttemptDetector(applicationContext)
        val settings = DataStoreSettingsRepository(applicationContext.mioziraSettingsDataStore)
        val privateFileStore = AndroidPrivateFileStore(applicationContext)
        val familyRepository = RoomFamilyRecordingRepository(database.familyRecordingDao())
        val familyService = AndroidFamilyRecordingService(privateFileStore, privateFileStore, childDetector)
        familyRecordingController = FamilyRecordingController(
            permissionService = permissionService,
            recordingService = familyService,
            repository = familyRepository,
            nowMs = { AndroidClock.nowMs() },
            idGenerator = { UUID.randomUUID().toString() },
        )
        parentActionsController = ParentActionsController(
            ResetService(
                RoomResetCoordinator(
                    resetDao = database.resetDao(),
                    recordingCleanup = FamilyRecordingCleanup {
                        when (val result = familyRepository.getAll()) {
                            is com.miozira.core.result.AppResult.Failure -> result
                            is com.miozira.core.result.AppResult.Success -> {
                                var deleted = 0
                                for (metadata in result.value) {
                                    when (val deleteFile = familyService.delete(metadata.fileId)) {
                                        is com.miozira.core.result.AppResult.Success -> {
                                            when (val deleteMetadata = familyRepository.delete(metadata.pairId)) {
                                                is com.miozira.core.result.AppResult.Success -> deleted++
                                                is com.miozira.core.result.AppResult.Failure -> return@FamilyRecordingCleanup deleteMetadata
                                            }
                                        }
                                        is com.miozira.core.result.AppResult.Failure -> return@FamilyRecordingCleanup com.miozira.core.result.AppResult.Failure(
                                            com.miozira.core.result.AppError.Unavailable("Could not delete family recordings"),
                                        )
                                    }
                                }
                                privateFileStore.deleteOrphanTemporaryFiles().let { cleanup ->
                                    when (cleanup) {
                                        is com.miozira.core.result.AppResult.Failure -> cleanup
                                        is com.miozira.core.result.AppResult.Success -> com.miozira.core.result.AppResult.Success(deleted)
                                    }
                                }
                            }
                        }
                    },
                    settings = settings,
                    clock = AndroidClock,
                    onResetComplete = {
                        permissionHistory.edit().remove("record_audio_requested").apply()
                    },
                ),
            ),
            settings,
            permissionService,
        )
        parentController = ParentController(
            dataSource = RoomParentDataSource(database.interactionDao()),
            transferService = RealWorldTransferService(
                RoomRealWorldObservationRepository(database.realWorldObservationDao()),
            ),
            nowMs = { AndroidClock.nowMs() },
            idGenerator = { UUID.randomUUID().toString() },
        )
        val persistence = RoomSessionPersistence(
            sessions = RoomSessionRepository(database.sessionDao()),
            learning = RoomLearningRepository(database.interactionDao()),
            clock = AndroidClock,
        )
        childSessionController = ChildSessionController(
            engine = LearningSessionEngine(
                pairs = PrototypeContentRepository.allPairs(),
                persistence = persistence,
                clock = AndroidClock,
                planner = AdaptivePairPlanner(
                    states = {
                        database.contentDao().allPairLearningStates().mapNotNull { entity ->
                            val pair = PrototypeContentRepository.allPairs().firstOrNull { it.pairId.value == entity.pairId }
                                ?: return@mapNotNull null
                            ScheduledPairCandidate(
                                pair = pair,
                                state = PairLearningState(
                                    pairId = pair.pairId,
                                    state = runCatching { LearningState.valueOf(entity.learningState) }.getOrDefault(LearningState.NEW),
                                    nextDueAtMs = entity.nextDueAtMs,
                                    restUntilMs = entity.restUntilMs,
                                    validExposureCount = entity.validExposureCount,
                                    attemptDetectedCount = entity.attemptDetectedCount,
                                    replayCount = entity.replayCountTotal,
                                    recognitionCount = entity.realWorldRecognitionCount,
                                    useCount = entity.realWorldUseCount,
                                ),
                            )
                        }
                    },
                    nowMs = { AndroidClock.nowMs() },
                ),
            ),
            persistence = persistence,
            schedule = SessionSchedule(RoomSessionRepository(database.sessionDao()), AndroidLocalDay, AndroidClock),
            audioService = audioService,
            clock = AndroidClock,
            scope = serviceScope,
            readyLanguages = PrototypeContentRepository::readyLanguages,
            speechAttemptDetector = childDetector,
            measurement = {
                decideAttemptMeasurement(
                    settings.isSpeechAttemptDetectionEnabled() == com.miozira.core.result.AppResult.Success(true),
                    permissionService.getMicrophonePermissionState(),
                )
            },
        )
        serviceScope.launch {
            privateFileStore.deleteOrphanTemporaryFiles()
            ContentSeedService(
                contentDao = database.contentDao(),
                nowMs = System::currentTimeMillis,
            ).seedContent(PrototypeContentRepository.contentVersion)
            RoomRecoveryService(database.recoveryDao(), AndroidClock).recoverInterruptedSessions()
            childSessionController.open()
            startupDone = true
        }
        setContent {
            MioziraApp(
                childSessionController,
                parentAccess,
                parentController,
                familyRecordingController,
                parentActionsController,
                permissionService::openMicrophoneSettings,
            )
        }
    }

    override fun onStop() {
        parentAccess.lock()
        if (::childSessionController.isInitialized) childSessionController.onBackground()
        // security.md §19: stop family recording on background. UNDISPATCHED starts the cancel
        // inside onStop, so an onDestroy that cancels serviceScope cannot drop it.
        if (::familyRecordingController.isInitialized) {
            serviceScope.launch(start = CoroutineStart.UNDISPATCHED) { familyRecordingController.onBackground() }
        }
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        // Before startup finishes (seed and recovery), onCreate's own open() covers the first start.
        if (startupDone) serviceScope.launch { childSessionController.open() }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (!permissionService.onRequestPermissionsResult(requestCode, grantResults)) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onDestroy() {
        serviceScope.cancel()
        if (::database.isInitialized) database.close()
        super.onDestroy()
    }
}
