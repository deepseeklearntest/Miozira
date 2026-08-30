package com.miozira.security

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.miozira.core.result.AppResult
import com.miozira.services.files.AndroidPrivateFileStore
import com.miozira.services.files.PrivateFileId
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StorageBoundaryTest {
    @Test
    fun logicalIds_cannotEscapePrivateStorage_andDeleteIsIdempotent() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val store = AndroidPrivateFileStore(context)

        assertFalse(assertSuccess(store.exists(PrivateFileId("../outside.m4a"))))
        assertTrue(store.delete(PrivateFileId("../outside.m4a")) is AppResult.Failure)

        val temporary = assertSuccess(store.createTemporary("apple:en"))
        assertTrue(temporary.value.matches(Regex("tmp-[0-9a-f-]+\\.m4a")))
        assertTrue(File(context.filesDir, "family-recordings/temp/${temporary.value}").isFile)

        val saved = assertSuccess(store.promoteTemporary(temporary, "apple:en"))
        assertTrue(File(context.filesDir, "family-recordings/saved/${saved.value}").isFile)
        val orphan = assertSuccess(store.createTemporary("orphan"))
        assertTrue(File(context.filesDir, "family-recordings/temp/${orphan.value}").isFile)
        assertEquals(1, assertSuccess(store.deleteOrphanTemporaryFiles()))
        assertFalse(File(context.filesDir, "family-recordings/temp/${orphan.value}").exists())
        assertSuccess(store.delete(saved))
        assertSuccess(store.delete(saved))
        val remaining = assertSuccess(store.exists(saved))
        assertFalse(
            remaining,
            "Unexpected private recording remains: " +
                File(context.filesDir, "family-recordings").walkTopDown().filter { it.isFile }.map { it.path }.toList(),
        )
    }

    private fun <T> assertSuccess(result: AppResult<T>): T = assertIs<AppResult.Success<T>>(result).value
}
