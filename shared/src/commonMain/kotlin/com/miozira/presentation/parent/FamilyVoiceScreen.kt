package com.miozira.presentation.parent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.miozira.content.PrototypeContentRepository
import com.miozira.core.model.PairId

@Composable
fun FamilyVoiceScreen(
    pairId: PairId,
    state: FamilyRecordingState,
    onRequestPermission: () -> Unit,
    onRecord: () -> Unit,
    onStop: () -> Unit,
    onPreview: () -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    onDelete: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Family voice", style = MaterialTheme.typography.headlineSmall)
        Text("Optional pronunciation for ${wordLabel(pairId, PrototypeContentRepository.languageNames)}. Canonical audio is always available.")
        when (state) {
            FamilyRecordingState.Idle -> {
                Button(onClick = onRequestPermission) { Text("Allow microphone") }
                Button(onClick = onRecord) { Text("Record") }
            }
            is FamilyRecordingState.Recording -> Button(onClick = onStop) { Text("Stop recording") }
            is FamilyRecordingState.Previewing -> {
                Button(onClick = onPreview) { Text("Preview") }
                Button(onClick = onSave) { Text("Save family voice") }
                Button(onClick = onCancel) { Text("Cancel") }
            }
            is FamilyRecordingState.Available -> {
                Button(onClick = onPreview) { Text("Play family voice") }
                Button(onClick = onRecord) { Text("Replace recording") }
                Button(onClick = onDelete) { Text("Delete family voice") }
            }
            is FamilyRecordingState.Error -> {
                Text(state.message, color = MaterialTheme.colorScheme.error)
                Button(onClick = onCancel) { Text("Back") }
            }
        }
    }
}
