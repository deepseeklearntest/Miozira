package com.miozira

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.miozira.services.audio.SpikeAudio
import kotlin.test.Test
import kotlin.test.assertTrue
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SpikeAudioAssetTest {
    @Test
    fun bundledSpikeAudio_isReadableFromTheAndroidAssetPackage() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        context.assets.openFd(SpikeAudio.assetPath).use { descriptor ->
            assertTrue(descriptor.length > 0)
        }
    }
}
