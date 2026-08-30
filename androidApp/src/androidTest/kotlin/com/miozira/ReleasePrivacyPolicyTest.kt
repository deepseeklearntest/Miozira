package com.miozira

import android.Manifest
import android.content.pm.PackageManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.runner.RunWith
import org.xmlpull.v1.XmlPullParser

@RunWith(AndroidJUnit4::class)
class ReleasePrivacyPolicyTest {
    @Test
    fun permissions_storageNames_andBackupRulesRemainPrivacyBounded() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val packageInfo = context.packageManager.getPackageInfo(
            context.packageName,
            PackageManager.GET_PERMISSIONS,
        )
        val permissions = packageInfo.requestedPermissions.orEmpty()
            .filter { it.startsWith("android.permission.") }
            .toSet()
        assertEquals(setOf(Manifest.permission.RECORD_AUDIO), permissions)
        assertFalse(Manifest.permission.INTERNET in permissions)

        val files = (context.filesDir.walkTopDown() + context.cacheDir.walkTopDown())
            .filter { it.isFile }
            .map { it.name.lowercase() }
            .toList()
        assertTrue(files.none { name -> name.contains("pcm") || name.contains("transcript") || name.contains("child-audio") })

        assertExcluded(context, R.xml.backup_rules)
        assertExcluded(context, R.xml.data_extraction_rules)
    }

    private fun assertExcluded(context: android.content.Context, resourceId: Int) {
        val parser = context.resources.getXml(resourceId)
        val exclusions = buildList {
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.eventType == XmlPullParser.START_TAG && parser.name == "exclude") {
                    add(parser.getAttributeValue(null, "path"))
                }
            }
        }
        parser.close()
        assertTrue("family-recordings/" in exclusions)
        assertTrue("miozira.db" in exclusions)
        assertTrue("." in exclusions)
    }
}
