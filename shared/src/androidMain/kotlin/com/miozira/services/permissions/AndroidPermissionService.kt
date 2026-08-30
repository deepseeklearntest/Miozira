package com.miozira.services.permissions

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class AndroidPermissionService(
    private val activity: Activity,
    private val wasRequestedBefore: () -> Boolean,
    private val markRequested: () -> Unit = {},
) : PermissionService {
    private var pendingResult: ((MicrophonePermissionState) -> Unit)? = null

    override fun getMicrophonePermissionState(): MicrophonePermissionState {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            return MicrophonePermissionState.GRANTED
        }
        return if (wasRequestedBefore() && !ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO)) {
            MicrophonePermissionState.DENIED_DONT_ASK_AGAIN
        } else if (wasRequestedBefore()) {
            MicrophonePermissionState.DENIED
        } else {
            MicrophonePermissionState.NOT_DETERMINED
        }
    }

    override fun requestMicrophonePermission(onResult: (MicrophonePermissionState) -> Unit) {
        if (getMicrophonePermissionState() == MicrophonePermissionState.GRANTED) {
            onResult(MicrophonePermissionState.GRANTED)
            return
        }
        pendingResult = onResult
        markRequested()
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_CODE)
    }

    override fun openMicrophoneSettings() {
        activity.startActivity(
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:${activity.packageName}")
            },
        )
    }

    fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray): Boolean {
        if (requestCode != REQUEST_CODE) return false
        val result = if (grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
            MicrophonePermissionState.GRANTED
        } else {
            getMicrophonePermissionState()
        }
        pendingResult?.invoke(result)
        pendingResult = null
        return true
    }

    private companion object {
        const val REQUEST_CODE = 4101
    }
}
