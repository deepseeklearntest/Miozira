package com.miozira.domain.session

import com.miozira.content.ConceptLanguagePair

sealed interface ChildScreenState {
    data object Loading : ChildScreenState
    data class Word(
        val pair: ConceptLanguagePair,
        val phase: InteractionPhase,
        val wordVisible: Boolean,   // false until the word has started playing in this interaction
        val ringVisible: Boolean,   // true exactly while phase == LISTENING
        val nod: Boolean,           // true for nodMs after ATTEMPT_DETECTED
    ) : ChildScreenState
    data object Breath : ChildScreenState
    data object Quiet : ChildScreenState
}
