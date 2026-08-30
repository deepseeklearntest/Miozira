package com.miozira.services.files

import android.content.Context
import com.miozira.core.result.AppError
import com.miozira.core.result.AppResult
import java.io.File
import java.util.UUID

class AndroidPrivateFileStore(
    context: Context,
) : FileStore {
    private val root = File(context.filesDir, "family-recordings")
    private val temporary = File(root, "temp")
    private val saved = File(root, "saved")

    override suspend fun createTemporary(recordingKey: String): AppResult<PrivateFileId> = try {
        temporary.mkdirs()
        val id = PrivateFileId("tmp-${UUID.randomUUID()}.m4a")
        File(temporary, id.value).createNewFile()
        AppResult.Success(id)
    } catch (error: Exception) {
        AppResult.Failure(AppError.Unavailable("Could not start private recording"))
    }

    override suspend fun promoteTemporary(fileId: PrivateFileId, recordingKey: String): AppResult<PrivateFileId> {
        return try {
            val source = safeChild(temporary, fileId.value) ?: return failure()
            if (!source.isFile) return failure()
            saved.mkdirs()
            val promoted = PrivateFileId("rec-${UUID.randomUUID()}.m4a")
            val destination = safeChild(saved, promoted.value) ?: return failure()
            check(source.renameTo(destination)) { "Could not promote private recording" }
            if (source.exists() && !source.delete()) {
                destination.delete()
                return failure()
            }
            AppResult.Success(promoted)
        } catch (error: Exception) {
            AppResult.Failure(AppError.Unavailable("Could not save private recording"))
        }
    }

    override suspend fun delete(fileId: PrivateFileId): AppResult<Unit> {
        return try {
            val candidates = listOf(safeChild(temporary, fileId.value), safeChild(saved, fileId.value))
            if (candidates.all { it == null }) return failure()
            val file = candidates.firstOrNull { it?.exists() == true }
            if (file != null && !file.delete()) return failure()
            AppResult.Success(Unit)
        } catch (error: Exception) {
            AppResult.Failure(AppError.Unavailable("Could not delete private recording"))
        }
    }

    override suspend fun exists(fileId: PrivateFileId): AppResult<Boolean> = try {
        AppResult.Success(
            (safeChild(temporary, fileId.value)?.isFile == true) || (safeChild(saved, fileId.value)?.isFile == true),
        )
    } catch (error: Exception) {
        AppResult.Failure(AppError.Unavailable("Could not inspect private recording"))
    }

    override suspend fun deleteOrphanTemporaryFiles(): AppResult<Int> = try {
        val files = temporary.listFiles().orEmpty()
        var deleted = 0
        files.forEach { if (it.isFile && it.delete()) deleted++ }
        AppResult.Success(deleted)
    } catch (error: Exception) {
        AppResult.Failure(AppError.Unavailable("Could not clean temporary recordings"))
    }

    internal fun temporaryFile(fileId: PrivateFileId): File? = safeChild(temporary, fileId.value)

    private fun safeChild(directory: File, name: String): File? =
        File(directory, name).canonicalFile.takeIf { it.parentFile == directory.canonicalFile }

    private fun <T> failure(): AppResult<T> = AppResult.Failure(AppError.Unavailable("Private recording file is unavailable"))
}
