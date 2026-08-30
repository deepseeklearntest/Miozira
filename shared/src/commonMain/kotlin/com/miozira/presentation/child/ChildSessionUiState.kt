package com.miozira.presentation.child

import com.miozira.content.ConceptLanguagePair
import com.miozira.domain.session.InteractionPhase

data class ChildSessionUiState(
    val pair: ConceptLanguagePair,
    val isReplayEnabled: Boolean,
    val phase: InteractionPhase = InteractionPhase.READY,
) {
    companion object {
        fun ready(pair: ConceptLanguagePair): ChildSessionUiState =
            ChildSessionUiState(pair, isReplayEnabled = true)
    }
}
