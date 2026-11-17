package com.miozira.presentation.parent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ParentActionsScreen(
    state: ParentActionsState,
    onSetListening: (Boolean) -> Unit,
    switchEnabled: Boolean,
    onOpenMicrophoneSettings: () -> Unit,
    onRequestReset: () -> Unit,
    onCancelReset: () -> Unit,
    onConfirmReset: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Microphone and privacy", style = MaterialTheme.typography.headlineSmall)
        Text(
            "When listening is on, the microphone only notices that he is trying to say a word. " +
                "What he says is never saved, never sent anywhere and never judged. " +
                "It records a family voice only when you press Record. " +
                "Miozira works without a network connection.",
        )
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Listening for speaking attempts")
            Switch(checked = state.listeningOn, onCheckedChange = onSetListening, enabled = switchEnabled)
        }
        if (state.microphoneBlocked) {
            Button(onClick = onOpenMicrophoneSettings) { Text("Microphone settings") }
        }
        if (state.resetConfirmationRequired) {
            Text("Erase all learning history, voice recordings and settings on this tablet?")
            Button(onClick = onConfirmReset) { Text("Erase") }
            Button(onClick = onCancelReset) { Text("Keep everything") }
        } else {
            Button(onClick = onRequestReset) { Text("Erase everything on this tablet") }
        }
        state.notice?.let { notice ->
            Text(if (notice == ParentActionsState.Notice.RESET_COMPLETE) "Local data reset." else "Reset could not finish safely.")
        }
        TextButton(onClick = onBack) { Text("Back") }
    }
}
