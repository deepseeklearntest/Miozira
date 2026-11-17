package com.miozira.presentation.child

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import com.miozira.domain.session.ChildScreenState
import com.miozira.domain.session.InteractionPhase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val RingColor = Color(0xFF607C8A)
private val WordSpokenColor = Color(0xFF4F746B)
private val WordRestColor = Color(0xFF26312E)

private const val PICTURE_HEIGHT_FRACTION = 0.60f
private const val GAP_HEIGHT_FRACTION = 0.04f
private const val WORD_SIZE_FRACTION = 0.10f
private val RING_STROKE = 3.dp
private const val RING_ALPHA_VISIBLE = 0.55f
private const val RING_ALPHA_FADE_MS = 800
private const val RING_SCALE_HIDDEN = 0.97f
private const val RING_SCALE_MS = 1600
private const val WORD_FADE_IN_MS = 600
private const val WORD_COLOUR_MS = 600
private const val PAIR_CROSSFADE_MS = 400
private const val TAP_SCALE = 1.02f
private const val TAP_SCALE_MS = 100
private const val NOD_SCALE = 1.03f
private const val NOD_SCALE_MS = 400
private const val IDLE_PULSE_AFTER_MS = 8_000L
private const val IDLE_PULSE_MS = 400

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun WordScreen(state: ChildScreenState.Word, onTap: () -> Unit) {
    AnimatedContent(
        targetState = state,
        contentKey = { it.pair.pairId },
        transitionSpec = {
            fadeIn(tween(PAIR_CROSSFADE_MS)) togetherWith fadeOut(tween(PAIR_CROSSFADE_MS))
        },
        modifier = Modifier.fillMaxSize(),
    ) { word -> WordContent(word, onTap) }
}

@Composable
private fun WordContent(state: ChildScreenState.Word, onTap: () -> Unit) {
    var tapped by remember(state.pair.pairId) { mutableStateOf(false) }
    var pulsed by remember(state.pair.pairId) { mutableStateOf(false) }
    val pictureScale = remember(state.pair.pairId) { Animatable(1f) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(state.pair.pairId, state.phase == InteractionPhase.READY, tapped) {
        if (state.phase == InteractionPhase.READY && !tapped && !pulsed) {
            delay(IDLE_PULSE_AFTER_MS)
            pulsed = true
            pictureScale.animateTo(NOD_SCALE, tween(IDLE_PULSE_MS))
            pictureScale.animateTo(1f, tween(IDLE_PULSE_MS))
        }
    }
    LaunchedEffect(state.nod) {
        if (state.nod) {
            pictureScale.snapTo(NOD_SCALE)
            pictureScale.animateTo(1f, tween(NOD_SCALE_MS))
        }
    }

    val ringAlpha by animateFloatAsState(
        if (state.ringVisible) RING_ALPHA_VISIBLE else 0f, tween(RING_ALPHA_FADE_MS),
    )
    val ringScale by animateFloatAsState(
        if (state.ringVisible) 1f else RING_SCALE_HIDDEN, tween(RING_SCALE_MS),
    )
    val wordAlpha by animateFloatAsState(
        if (state.wordVisible) 1f else 0f, tween(WORD_FADE_IN_MS),
    )
    val wordColour by animateColorAsState(
        if (state.phase == InteractionPhase.PLAYING_WORD) WordSpokenColor else WordRestColor,
        tween(WORD_COLOUR_MS),
    )

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val available = maxHeight
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            val pictureSize = available * PICTURE_HEIGHT_FRACTION
            Box(
                Modifier
                    .size(pictureSize)
                    .semantics(mergeDescendants = true) {
                        contentDescription = "${state.pair.spokenForm}. Tap to hear the word."
                    }
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        role = Role.Button,
                    ) {
                        tapped = true
                        scope.launch {
                            pictureScale.animateTo(TAP_SCALE, tween(TAP_SCALE_MS))
                            pictureScale.animateTo(1f, tween(TAP_SCALE_MS))
                        }
                        onTap()
                    },
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(state.pair.visual.toDrawableResource()),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize().graphicsLayer {
                        scaleX = pictureScale.value
                        scaleY = pictureScale.value
                    },
                )
                Canvas(
                    Modifier.fillMaxSize().graphicsLayer {
                        alpha = ringAlpha
                        scaleX = ringScale
                        scaleY = ringScale
                    },
                ) {
                    val stroke = RING_STROKE.toPx()
                    drawCircle(
                        color = RingColor,
                        radius = size.minDimension / 2f - stroke / 2f,
                        center = Offset(size.width / 2f, size.height / 2f),
                        style = Stroke(width = stroke),
                    )
                }
            }
            Spacer(Modifier.height(available * GAP_HEIGHT_FRACTION))
            // The word's height is always reserved, so the picture never moves when it appears.
            Text(
                text = state.pair.spokenForm,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Visible,
                textAlign = TextAlign.Center,
                color = wordColour,
                // dp -> sp so the system font scale does not change the size.
                fontSize = with(LocalDensity.current) { (available * WORD_SIZE_FRACTION).toSp() },
                style = TextStyle(
                    localeList = LocaleList(state.pair.languageId.value),
                    textDirection = TextDirection.Content,
                ),
                modifier = Modifier.fillMaxWidth().alpha(wordAlpha),
            )
        }
    }
}
