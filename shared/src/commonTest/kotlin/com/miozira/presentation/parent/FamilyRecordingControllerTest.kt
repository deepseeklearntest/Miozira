package com.miozira.presentation.parent

import com.miozira.core.model.PairId
import com.miozira.core.result.AppError
import com.miozira.core.result.AppResult
import com.miozira.services.files.PrivateFileId
import com.miozira.services.permissions.MicrophonePermissionState
import com.miozira.services.permissions.PermissionService
import com.miozira.services.recording.FamilyRecordingMetadata
import com.miozira.services.recording.FamilyRecordingRepository
import com.miozira.services.recording.FamilyRecordingService
import com.miozira.services.recording.RecordingPreview
import com.miozira.services.recording.RecordingSession
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlinx.coroutines.runBlocking

class FamilyRecordingControllerTest {
    private val pair = PairId("apple:en")

    @Test
    fun save_replacesMetadata_thenDeletesOldFile(): Unit = runBlocking {
        val old = FamilyRecordingMetadata("old", pair, PrivateFileId("old-file"), 1, 2, 400)
        val repository = FakeRepository(old)
        val service = FakeService()
        val controller = FamilyRecordingController(GrantedPermission, service, repository, { 10L }, { "new" })

        controller.start(pair, replacing = true)
        controller.stop()
        controller.save()

        assertEquals(PrivateFileId("new-file"), repository.value?.fileId)
        assertEquals(listOf(PrivateFileId("old-file")), service.deleted)
        assertIs<FamilyRecordingState.Available>(controller.state.value)
    }

    @Test
    fun metadataFailure_deletesPromotedFile_andPreservesOldMetadata(): Unit = runBlocking {
        val old = FamilyRecordingMetadata("old", pair, PrivateFileId("old-file"), 1, 2, 400)
        val repository = FakeRepository(old, failSave = true)
        val service = FakeService()
        val controller = FamilyRecordingController(GrantedPermission, service, repository, { 10L }, { "new" })

        controller.start(pair, replacing = true)
        controller.stop()
        controller.save()

        assertEquals(old, repository.value)
        assertEquals(listOf(PrivateFileId("new-file")), service.deleted)
        assertIs<FamilyRecordingState.Error>(controller.state.value)
    }

    @Test
    fun background_cancels_recording_without_saving() = runBlocking {
        val repository = FakeRepository(null)
        val service = FakeService()
        val controller = FamilyRecordingController(GrantedPermission, service, repository, { 10L }, { "new" })

        controller.start(pair)
        controller.onBackground()

        assertEquals(1, service.cancelled)
        assertEquals(FamilyRecordingState.Idle, controller.state.value)
    }

    private object GrantedPermission : PermissionService {
        override fun getMicrophonePermissionState() = MicrophonePermissionState.GRANTED
        override fun requestMicrophonePermission(onResult: (MicrophonePermissionState) -> Unit) = Unit
        override fun openMicrophoneSettings() = Unit
    }

    private class FakeRepository(initial: FamilyRecordingMetadata?, private val failSave: Boolean = false) : FamilyRecordingRepository {
        var value = initial
        override suspend fun get(pairId: PairId) = AppResult.Success(value?.takeIf { it.pairId == pairId })
        override suspend fun save(metadata: FamilyRecordingMetadata): AppResult<Unit> = if (failSave) {
            AppResult.Failure(AppError.Unavailable("save failed"))
        } else {
            value = metadata
            AppResult.Success(Unit)
        }
        override suspend fun delete(pairId: PairId): AppResult<Unit> { value = null; return AppResult.Success(Unit) }
        override suspend fun getAll() = AppResult.Success(listOfNotNull(value))
    }

    private class FakeService : FamilyRecordingService {
        val deleted = mutableListOf<PrivateFileId>()
        var cancelled = 0
        private val session = RecordingSession("session", PairId("apple:en"), PrivateFileId("temp"))
        override suspend fun start(pairId: PairId) = AppResult.Success(session.copy(pairId = pairId))
        override suspend fun stop(session: RecordingSession) = AppResult.Success(RecordingPreview(session, 400))
        override suspend fun cancel(session: RecordingSession): AppResult<Unit> { cancelled++; return AppResult.Success(Unit) }
        override suspend fun save(preview: RecordingPreview) = AppResult.Success(PrivateFileId("new-file"))
        override suspend fun play(recordingId: PrivateFileId) = AppResult.Success(Unit)
        override suspend fun delete(recordingId: PrivateFileId): AppResult<Unit> { deleted += recordingId; return AppResult.Success(Unit) }
    }
}
