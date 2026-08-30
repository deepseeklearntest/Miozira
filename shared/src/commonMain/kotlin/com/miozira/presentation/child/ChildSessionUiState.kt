package com.miozira.presentation.child

import com.miozira.content.ConceptLanguagePair

data class ChildSessionUiState(
    val pair: ConceptLanguagePair,
    val isReplayEnabled: Boolean,
) {
    companion object {
        fun ready(pair: ConceptLanguagePair): ChildSessionUiState =
            ChildSessionUiState(pair, isReplayEnabled = true)
    }
}
