package com.miozira.data.db

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.miozira.content.PrototypeContentRepository
import com.miozira.data.db.entity.ConceptLanguagePairEntity
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.runBlocking
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SchemaAndSeedTest {
    @Test
    fun seedContent_isIdempotent_andForeignKeysProtectPairs() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val databaseName = "schema-and-seed-test.db"
        context.deleteDatabase(databaseName)
        val database = DatabaseFactory.create(context, databaseName)

        try {
            val seedService = ContentSeedService(
                contentDao = database.contentDao(),
                nowMs = { 1_725_000_000_000L },
            )

            seedService.seedContent(PrototypeContentRepository.contentVersion)
            seedService.seedContent(PrototypeContentRepository.contentVersion)

            assertEquals(8, database.contentDao().conceptCount())
            assertEquals(2, database.contentDao().languageCount())
            assertEquals(16, database.contentDao().pairCount())
            assertEquals(16, database.contentDao().newPairStateCount())

            assertFailsWith<Exception> {
                database.contentDao().insertPairsIgnore(
                    listOf(
                        ConceptLanguagePairEntity(
                            pairId = "missing:en",
                            conceptId = "missing",
                            languageId = "en",
                            spokenForm = "missing",
                            canonicalAudioAssetId = "audio.word.missing.en",
                            reviewStatus = "DRAFT",
                            contentVersionId = PrototypeContentRepository.contentVersion,
                            isEnabled = true,
                        ),
                    ),
                )
            }
        } finally {
            database.close()
            context.deleteDatabase(databaseName)
        }
        Unit
    }
}
