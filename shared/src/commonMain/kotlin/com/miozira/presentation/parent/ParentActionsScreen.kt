package com.miozira.presentation.parent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ParentActionsScreen(
    state: ParentActionsState,
    onOpenMicrophoneSettings: () -> Unit,
    onRequestReset: () -> Unit,
    onCancelReset: () -> Unit,
    onConfirmReset: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Button(onClick = onOpenMicrophoneSettings) { Text("Microphone settings") }
        if (state.resetConfirmationRequired) {
            Text("Reset local learning, family recordings, and settings?")
            Button(onClick = onConfirmReset) { Text("Confirm reset") }
            Button(onClick = onCancelReset) { Text("Cancel reset") }
        } else {
            Button(onClick = onRequestReset) { Text("Reset local data") }
        }
        state.notice?.let { notice ->
            Text(if (notice == ParentActionsState.Notice.RESET_COMPLETE) "Local data reset." else "Reset could not finish safely.")
        }
    }
}
