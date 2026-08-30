package com.miozira

import kotlin.test.Test
import kotlin.test.assertEquals

class AppSmokeTest {
    @Test
    fun appIdentity_isStable() {
        assertEquals("Miozira", appDisplayName())
    }
}
