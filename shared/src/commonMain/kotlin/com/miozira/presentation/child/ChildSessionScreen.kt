package com.miozira.presentation.child

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.miozira.content.ConceptLanguagePair
import com.miozira.content.VisualAssetRef
import com.miozira.core.result.AppResult
import com.miozira.services.audio.AudioAssetRef
import com.miozira.services.audio.AudioError
import com.miozira.services.audio.AudioPlaybackListener
import com.miozira.services.audio.AudioService
import com.miozira.services.audio.PlaybackHandle
import com.miozira.services.audio.WordPlaybackRequest
import kotlinx.coroutines.launch
import miozira.shared.generated.resources.Res
import miozira.shared.generated.resources.concept_apple_draft
import miozira.shared.generated.resources.concept_ball_draft
import miozira.shared.generated.resources.concept_car_draft
import miozira.shared.generated.resources.concept_cat_draft
import miozira.shared.generated.resources.concept_cup_draft
import miozira.shared.generated.resources.concept_hand_draft
import miozira.shared.generated.resources.concept_nose_draft
import miozira.shared.generated.resources.concept_water_draft
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun ChildSessionScreen(
    state: ChildSessionUiState,
    audioService: AudioService,
    isPlaying: Boolean,
    onPlaybackStateChanged: (Boolean) -> Unit,
    onExposureThreshold: suspend (PlaybackHandle) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val listener = remember {
        object : AudioPlaybackListener {
            override fun onStarted(handle: PlaybackHandle) = Unit
            override fun onExposureThresholdReached(handle: PlaybackHandle) {
                scope.launch { onExposureThreshold(handle) }
            }
            override fun onCompleted(handle: PlaybackHandle) = onPlaybackStateChanged(false)
            override fun onFailed(handle: PlaybackHandle?, error: AudioError) = onPlaybackStateChanged(false)
        }
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(
            modifier = Modifier.fillMaxSize().padding(48.dp).semantics {
                contentDescription = "${state.pair.spokenForm}. Tap to hear the word."
            },
            enabled = state.isReplayEnabled && !isPlaying,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF2EEE7)),
            onClick = {
                onPlaybackStateChanged(true)
                scope.launch {
                    val result = audioService.playCanonicalWord(state.pair.toPlaybackRequest(), listener)
                    if (result is AppResult.Failure) onPlaybackStateChanged(false)
                }
            },
        ) {
            Image(
                painter = painterResource(state.pair.visual.toDrawableResource()),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

private fun VisualAssetRef.toDrawableResource(): DrawableResource = when (resourcePath) {
    "drawable/concept_apple_draft.png" -> Res.drawable.concept_apple_draft
    "drawable/concept_ball_draft.png" -> Res.drawable.concept_ball_draft
    "drawable/concept_cup_draft.jpg" -> Res.drawable.concept_cup_draft
    "drawable/concept_hand_draft.jpg" -> Res.drawable.concept_hand_draft
    "drawable/concept_nose_draft.png" -> Res.drawable.concept_nose_draft
    "drawable/concept_cat_draft.png" -> Res.drawable.concept_cat_draft
    "drawable/concept_car_draft.png" -> Res.drawable.concept_car_draft
    "drawable/concept_water_draft.jpg" -> Res.drawable.concept_water_draft
    else -> error("No drawable resource for $resourcePath")
}

private fun ConceptLanguagePair.toPlaybackRequest() = WordPlaybackRequest(
    sessionId = com.miozira.core.model.SessionId("child-static-session"),
    interactionId = com.miozira.core.model.InteractionId("child-${pairId.value}"),
    pairId = pairId,
    assetRef = AudioAssetRef(
        logicalId = audio.logicalId,
        assetPath = "composeResources/miozira.shared.generated.resources/${audio.assetPath}",
    ),
)
