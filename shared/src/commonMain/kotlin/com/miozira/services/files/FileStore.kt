package com.miozira.services.files

import com.miozira.core.result.AppResult

@JvmInline
value class PrivateFileId(val value: String)

interface FileStore {
    suspend fun createTemporary(recordingKey: String): AppResult<PrivateFileId>
    suspend fun promoteTemporary(fileId: PrivateFileId, recordingKey: String): AppResult<PrivateFileId>
    suspend fun delete(fileId: PrivateFileId): AppResult<Unit>
    suspend fun exists(fileId: PrivateFileId): AppResult<Boolean>
    suspend fun deleteOrphanTemporaryFiles(): AppResult<Int>
}
