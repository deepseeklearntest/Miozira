package com.miozira.presentation.spike

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miozira.core.model.InteractionId
import com.miozira.core.model.PairId
import com.miozira.core.model.SessionId
import com.miozira.core.result.AppResult
import com.miozira.services.audio.AudioAssetRef
import com.miozira.services.audio.AudioError
import com.miozira.services.audio.AudioPlaybackListener
import com.miozira.services.audio.AudioService
import com.miozira.services.audio.PlaybackHandle
import com.miozira.services.audio.SpikeAudio
import com.miozira.services.audio.WordPlaybackRequest
import kotlinx.coroutines.launch

@Composable
fun SpikeScreen(
    audioService: AudioService,
    onExposureThreshold: suspend (PlaybackHandle) -> Unit,
) {
    var playbackRequested by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val listener = remember {
        object : AudioPlaybackListener {
            override fun onStarted(handle: PlaybackHandle) = Unit

            override fun onExposureThresholdReached(handle: PlaybackHandle) {
                scope.launch { onExposureThreshold(handle) }
            }

            override fun onCompleted(handle: PlaybackHandle) {
                playbackRequested = false
            }

            override fun onFailed(handle: PlaybackHandle?, error: AudioError) {
                playbackRequested = false
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Button(
            modifier = Modifier
                .fillMaxSize()
                .padding(48.dp)
                .semantics {
                    contentDescription = "Apple. Plays the temporary English technical-spike audio clip."
                },
            enabled = !playbackRequested,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4F746B),
                contentColor = Color(0xFFF2EEE7),
            ),
            onClick = {
                playbackRequested = true
                scope.launch {
                    val result = audioService.playCanonicalWord(
                        request = spikeWordPlaybackRequest(),
                        listener = listener,
                    )
                    if (result is AppResult.Failure) {
                        playbackRequested = false
                    }
                }
            },
        ) {
            Text(
                text = "🍎",
                fontSize = 160.sp,
                fontWeight = FontWeight.Normal,
            )
        }
    }
}

private fun spikeWordPlaybackRequest() = WordPlaybackRequest(
    sessionId = SessionId("spike-session"),
    interactionId = InteractionId("spike-apple-en"),
    pairId = PairId("apple:en"),
    assetRef = AudioAssetRef(
        logicalId = "audio.word.apple.en.spike",
        assetPath = SpikeAudio.assetPath,
    ),
)
