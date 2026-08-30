package com.miozira

import android.os.ParcelFileDescriptor
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlin.test.Test
import kotlin.test.assertTrue
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ReleasePerformanceTest {
    @Test
    fun launcherActivity_startsWithinFourSeconds() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val context = instrumentation.targetContext
        val launchOutput = instrumentation.uiAutomation.executeShellCommand(
            "am start -W -n ${context.packageName}/.MainActivity",
        ).use { descriptor ->
            ParcelFileDescriptor.AutoCloseInputStream(descriptor).bufferedReader().use { it.readText() }
        }
        val elapsedMs = Regex("TotalTime:\\s*(\\d+)").find(launchOutput)?.groupValues?.get(1)?.toLong()
            ?: error("Android launch timing was not reported: $launchOutput")
        assertTrue(
            elapsedMs <= 4_000L,
            "launcher activity exceeded the 4 second cold-start gate: ${elapsedMs}ms",
        )
    }

}
