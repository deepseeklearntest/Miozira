package com.miozira

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ReleaseConfigTest {
    @Test
    fun prototypeConfig_isFrozenAndHasRequiredContentCardinality() {
        val config = prototypeReleaseConfig()

        assertEquals("0.1.0", config.appVersion)
        assertTrue(config.contentVersion.isNotBlank())
        assertTrue(config.adaptiveConfigVersion.isNotBlank())
        assertTrue(config.microphoneConfigVersion.isNotBlank())
        assertEquals(1, config.schemaVersion)
        assertEquals(8, config.conceptCount)
        assertEquals(2, config.languageCount)
        assertEquals(16, config.pairCount)
    }
}
