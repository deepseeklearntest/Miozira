package com.miozira

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.miozira.content.PrototypeContentRepository
import com.miozira.presentation.child.ContentReviewScreen
import com.miozira.services.audio.AudioService
import com.miozira.services.audio.PlaybackHandle

fun appDisplayName(): String = "Miozira"

@Composable
fun MioziraApp(
    audioService: AudioService,
    onExposureThreshold: suspend (PlaybackHandle) -> Unit,
) {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFF2EEE7),
            contentColor = Color(0xFF26312E),
        ) {
            ContentReviewScreen(
                pairs = PrototypeContentRepository.allPairs(),
                audioService = audioService,
                onExposureThreshold = onExposureThreshold,
            )
        }
    }
}
