package com.miozira

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.miozira.content.PrototypeContentRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PrototypeContentAssetTest {
    @Test
    fun allDraftContentAssets_arePackagedForOfflineUse() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val pairs = PrototypeContentRepository.allPairs()

        assertEquals(16, pairs.size)
        pairs.forEach { pair ->
            context.assets.openFd(resourceAssetPath(pair.audio.assetPath)).use { descriptor ->
                assertTrue(descriptor.length > 0)
            }
        }
        pairs.map { it.visual.resourcePath }.toSet().forEach { visualPath ->
            context.assets.open(resourceAssetPath(visualPath)).use { stream ->
                assertTrue(stream.read() >= 0)
            }
        }
    }

    private fun resourceAssetPath(path: String) =
        "composeResources/miozira.shared.generated.resources/$path"
}
