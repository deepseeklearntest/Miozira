package com.miozira

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.miozira.content.PrototypeContentRepository
import org.json.JSONObject
import org.junit.runner.RunWith
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class PrototypeContentManifestConsistencyTest {
    @Test
    fun packagedManifest_matchesTheContentUsedForSeedAndPresentation() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val manifest = context.assets
            .open("composeResources/miozira.shared.generated.resources/files/content/manifest-v1.json")
            .bufferedReader()
            .use { JSONObject(it.readText()) }
        val repositoryPairs = PrototypeContentRepository.allPairs().associateBy { it.pairId.value }

        val manifestConcepts = manifest.getJSONArray("concepts")
        val manifestVisuals = buildMap {
            for (index in 0 until manifestConcepts.length()) {
                val concept = manifestConcepts.getJSONObject(index)
                put(concept.getString("id"), concept.getString("visual"))
            }
        }
        val repositoryVisuals = PrototypeContentRepository.allPairs()
            .associate { it.conceptId.value to it.visual.resourcePath }

        assertEquals(PrototypeContentRepository.contentVersion, manifest.getString("contentVersion"))
        assertEquals(8, manifestConcepts.length())
        assertEquals(16, manifest.getJSONArray("pairs").length())
        assertEquals(repositoryVisuals, manifestVisuals)

        val manifestPairs = manifest.getJSONArray("pairs")
        for (index in 0 until manifestPairs.length()) {
            val pair = manifestPairs.getJSONObject(index)
            val repositoryPair = repositoryPairs.getValue(pair.getString("id"))

            assertEquals(repositoryPair.spokenForm, pair.getString("spokenForm"))
            assertEquals(repositoryPair.audio.assetPath, pair.getString("audio"))
            assertEquals(repositoryPair.audio.reviewStatus.name, pair.getString("reviewStatus"))
        }
    }
}
