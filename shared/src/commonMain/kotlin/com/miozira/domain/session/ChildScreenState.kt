package com.miozira.domain.session

import com.miozira.content.ConceptLanguagePair
import com.miozira.core.model.InteractionId

sealed interface ChildScreenState {
    data object Loading : ChildScreenState
    data class Word(
        val pair: ConceptLanguagePair,
        val interactionId: InteractionId,   // changes with every interaction, even for the same pair
        val phase: InteractionPhase,
        val wordVisible: Boolean,   // false until the word has started playing in this interaction
        val ringVisible: Boolean,   // true exactly while phase == LISTENING
        val nod: Boolean,           // true for nodMs after ATTEMPT_DETECTED
    ) : ChildScreenState
    data object Breath : ChildScreenState
    data object Quiet : ChildScreenState
}
