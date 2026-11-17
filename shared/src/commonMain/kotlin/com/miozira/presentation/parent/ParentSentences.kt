package com.miozira.presentation.parent

import com.miozira.content.LanguageId
import com.miozira.content.PrototypeContentRepository
import com.miozira.core.model.PairId

/** The most recent session in which at least one word was heard, grouped for the parent. */
data class SessionSummary(
    val languages: List<LanguageId>,
    val tried: List<PairId>,
    val listened: List<PairId>,
    val heardUnmeasured: List<PairId>,
)

/** "apple" for the base language, "water (Tamil)" otherwise. */
fun wordLabel(pairId: PairId, languageNames: Map<LanguageId, String>): String {
    val pair = PrototypeContentRepository.allPairs().firstOrNull { it.pairId == pairId }
        ?: return ""
    val word = pair.conceptId.value
    return if (pair.languageId == PrototypeContentRepository.baseLanguageId) {
        word
    } else {
        "$word (${languageNames[pair.languageId].orEmpty()})"
    }
}

fun describeSession(summary: SessionSummary?, languageNames: Map<LanguageId, String>): List<String> {
    if (summary == null) return listOf("Miozira will describe his sessions here.")
    fun line(prefix: String, pairs: List<PairId>) =
        pairs.takeIf { it.isNotEmpty() }?.joinToString(", ", "$prefix: ") { wordLabel(it, languageNames) }
    val languages = summary.languages.mapNotNull { languageNames[it] }
    return listOfNotNull(
        languages.takeIf { it.isNotEmpty() }?.joinToString(" and ", "Most recent session: "),
        line("Tried saying", summary.tried),
        line("Listened without speaking", summary.listened),
        line("Heard", summary.heardUnmeasured),
    )
}
