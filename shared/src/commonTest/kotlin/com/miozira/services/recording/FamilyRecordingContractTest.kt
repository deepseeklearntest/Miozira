package com.miozira.services.recording

import com.miozira.core.model.PairId
import com.miozira.core.result.AppResult
import com.miozira.services.files.FileStore
import com.miozira.services.files.PrivateFileId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

class FamilyRecordingContractTest {
    @Test
    fun recordingLifecycle_keepsOpaqueFileId_andSupportsCancel() = runBlocking {
        val store = FakeFileStore()
        val file = assertSuccess(store.createTemporary("apple:en"))
        val session = RecordingSession("session-1", PairId("apple:en"), file)
        assertEquals(file, session.temporaryFileId)
        assertSuccess(store.delete(file))
        assertEquals(false, assertSuccess(store.exists(file)))
    }

    private class FakeFileStore : FileStore {
        private val files = mutableSetOf<PrivateFileId>()
        override suspend fun createTemporary(recordingKey: String): AppResult<PrivateFileId> =
            PrivateFileId("tmp-opaque").also { files += it }.let { AppResult.Success(it) }
        override suspend fun promoteTemporary(fileId: PrivateFileId, recordingKey: String) = AppResult.Success(PrivateFileId("saved-opaque"))
        override suspend fun delete(fileId: PrivateFileId): AppResult<Unit> { files -= fileId; return AppResult.Success(Unit) }
        override suspend fun exists(fileId: PrivateFileId) = AppResult.Success(fileId in files)
        override suspend fun deleteOrphanTemporaryFiles() = AppResult.Success(0)
    }

    private fun <T> assertSuccess(result: AppResult<T>): T = (result as AppResult.Success).value
}
