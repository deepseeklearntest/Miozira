package com.miozira.presentation.parent

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

/**
 * Undrawn 72 dp hit area for the hidden parent area. No semantics, no indication, no child content.
 * Only a stable press held for [ParentGate.REQUIRED_HOLD_MS] and then lifted calls [onHoldCompleted].
 */
@Composable
fun ParentCorner(
    nowMs: () -> Long,
    onHoldCompleted: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val now by rememberUpdatedState(nowMs)
    val completed by rememberUpdatedState(onHoldCompleted)
    Box(
        modifier = modifier
            .size(72.dp)
            .pointerInput(Unit) {
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    var state = ParentGate.reduce(
                        ParentGateState(),
                        ParentGateEvent.PressStarted(now(), down.position.x, down.position.y),
                    )
                    while (state.phase == ParentGateState.Phase.HOLDING) {
                        val change = awaitPointerEvent(PointerEventPass.Main).changes.firstOrNull { it.id == down.id }
                        state = when {
                            change == null -> ParentGate.reduce(state, ParentGateEvent.Cancelled)
                            !change.pressed -> ParentGate.reduce(state, ParentGateEvent.Released(now()))
                            else -> ParentGate.reduce(state, ParentGateEvent.Moved(change.position.x, change.position.y))
                        }
                    }
                    if (state.phase == ParentGateState.Phase.UNLOCKED) {
                        completed()
                        ParentGate.reduce(state, ParentGateEvent.Consumed)
                    }
                }
            },
    )
}
