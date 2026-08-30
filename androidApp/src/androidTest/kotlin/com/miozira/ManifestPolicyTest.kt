package com.miozira

import android.Manifest
import android.content.pm.PackageManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ManifestPolicyTest {
    @Test
    fun mergedPermissionSet_isMicrophoneOnly() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val packageInfo = context.packageManager.getPackageInfo(
            context.packageName,
            PackageManager.GET_PERMISSIONS,
        )
        val platformPermissions = packageInfo.requestedPermissions.orEmpty()
            .filter { it.startsWith("android.permission.") }
            .toSet()

        assertEquals(setOf(Manifest.permission.RECORD_AUDIO), platformPermissions)
        assertFalse(Manifest.permission.INTERNET in platformPermissions)
    }
}
