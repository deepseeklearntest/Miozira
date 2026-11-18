package com.miozira.presentation.parent

import com.miozira.content.LanguageId
import com.miozira.content.PrototypeContentRepository
import com.miozira.content.dailyMoments
import com.miozira.core.model.AttemptState
import com.miozira.core.model.PairId

/** The most recent session in which at least one word was heard, grouped for the parent. */
data class SessionSummary(
    val languages: List<LanguageId>,
    val tried: List<PairId>,
    val listened: List<PairId>,
    val heardUnmeasured: List<PairId>,
)

/**
 * Groups heard rows (pairId to attemptState, in session order) for the parent.
 * Any ATTEMPT_DETECTED is Tried; else any NO_ATTEMPT_DETECTED is Listened; else Heard.
 */
internal fun summarize(rows: List<Pair<String, String>>, languageOf: Map<String, LanguageId>): SessionSummary? {
    val byPair = rows.filter { it.first in languageOf }.groupBy({ it.first }, { it.second })
    if (byPair.isEmpty()) return null
    val tried = byPair.filterValues { AttemptState.ATTEMPT_DETECTED.name in it }.keys
    val listened = byPair.filterValues {
        AttemptState.ATTEMPT_DETECTED.name !in it && AttemptState.NO_ATTEMPT_DETECTED.name in it
    }.keys
    val unmeasured = byPair.keys - tried - listened
    return SessionSummary(
        languages = byPair.keys.map { languageOf.getValue(it) }.distinct(),
        tried = tried.map(::PairId),
        listened = listened.map(::PairId),
        heardUnmeasured = unmeasured.map(::PairId),
    )
}

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

/** Up to [max] lines "<moment>: <word>", tried first, then listened, then heard; one per concept. */
fun wordsForToday(summary: SessionSummary?, languageNames: Map<LanguageId, String>, max: Int = 4): List<String> {
    if (summary == null) return emptyList()
    val pairs = PrototypeContentRepository.allPairs().associateBy { it.pairId }
    return (summary.tried + summary.listened + summary.heardUnmeasured)
        .mapNotNull { id -> pairs[id]?.let { pair -> dailyMoments[pair.conceptId]?.let { pair.conceptId to "$it: ${wordLabel(id, languageNames)}" } } }
        .distinctBy { it.first }
        .take(max)
        .map { it.second }
}

/**
 * What the child has done with one pair: heard it (valid exposure), tried saying it (attempt detected),
 * and whether listening was ever measured for it (an attempt or no-attempt result exists).
 */
data class PairEvidence(val pairId: PairId, val heard: Boolean, val tried: Boolean, val measured: Boolean)

/** Languages in the order the content lists them. */
val languageOrder: List<LanguageId> = PrototypeContentRepository.allPairs().map { it.languageId }.distinct()

/** One paragraph about one language, from that language's pairs only. No counts: "most"/"some" stand in. */
fun describeLanguage(
    languageId: LanguageId,
    evidence: List<PairEvidence>,
    languageNames: Map<LanguageId, String>,
): String {
    val name = languageNames[languageId].orEmpty()
    val pairs = PrototypeContentRepository.allPairs().filter { it.languageId == languageId }.associateBy { it.pairId }
    val heard = evidence.filter { it.heard && it.pairId in pairs }
    if (heard.isEmpty()) return "$name — not met yet."
    // Listening off leaves pairs unmeasured: say nothing about trying for them.
    val measured = heard.filter { it.measured || it.tried }
    if (measured.isEmpty()) return "$name — he has been hearing his $name words."
    val tried = measured.count { it.tried }
    val head = when {
        tried == 0 -> "$name — he has not tried saying his $name words yet."
        tried * 3 >= measured.size * 2 -> "$name — he tries most of his $name words."
        else -> "$name — he tries some of his $name words."
    }
    val base = PrototypeContentRepository.baseLanguageId
    val listening = measured.filter { !it.tried }.map { e ->
        val concept = pairs.getValue(e.pairId).conceptId
        PrototypeContentRepository.allPairs().firstOrNull { it.conceptId == concept && it.languageId == base }?.spokenForm
            ?: concept.value
    }
    return if (listening.isEmpty()) head else "$head Still just listening: ${listening.joinToString(", ")}."
}
