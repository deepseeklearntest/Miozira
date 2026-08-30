package com.miozira.presentation.child

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.miozira.content.ConceptLanguagePair
import com.miozira.services.audio.AudioService
import com.miozira.services.audio.PlaybackHandle

@Composable
fun ContentReviewScreen(
    pairs: List<ConceptLanguagePair>,
    audioService: AudioService,
    onExposureThreshold: suspend (PlaybackHandle) -> Unit,
) {
    var pairIndex by rememberSaveable { mutableIntStateOf(0) }
    var isPlaying by rememberSaveable { mutableStateOf(false) }

    Box(Modifier.fillMaxSize()) {
        ChildSessionScreen(
            state = ChildSessionUiState.ready(pairs[pairIndex]),
            audioService = audioService,
            isPlaying = isPlaying,
            onPlaybackStateChanged = { isPlaying = it },
            onExposureThreshold = onExposureThreshold,
        )
        Row(
            modifier = Modifier.align(Alignment.BottomCenter).padding(24.dp),
        ) {
            Button(
                modifier = Modifier.semantics { contentDescription = "Previous item" },
                enabled = isReviewNavigationEnabled(isPlaying),
                onClick = { pairIndex = previousContentIndex(pairIndex, pairs.size) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F746B)),
            ) {
                Text("‹")
            }
            Button(
                modifier = Modifier.semantics { contentDescription = "Next item" },
                enabled = isReviewNavigationEnabled(isPlaying),
                onClick = { pairIndex = nextContentIndex(pairIndex, pairs.size) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F746B)),
            ) {
                Text("›")
            }
        }
    }
}
