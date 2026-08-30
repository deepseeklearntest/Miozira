package com.miozira.services.microphone

import kotlin.test.Test
import kotlin.test.assertEquals

class SpeechAttemptConfigTest {
    @Test
    fun defaultConfig_isVersioned_andUsesShortMemoryOnlyWindow() {
        val config = SpeechAttemptConfig()
        assertEquals("prototype-0.1-v1", config.version)
        assertEquals(2_500L, config.windowDurationMs)
        assertEquals(8, config.calibrationFrameCount)
    }
}
