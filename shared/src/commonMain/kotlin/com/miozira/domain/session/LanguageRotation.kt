package com.miozira.domain.session

import com.miozira.content.LanguageId

object LanguageRotation {
    fun languagesFor(completedSessions: Int, available: List<LanguageId>): Pair<LanguageId, LanguageId>? {
        val n = available.size
        if (n == 0) return null
        if (n == 1) return available[0] to available[0]
        val shift = if (n % 2 == 0 && n > 2) completedSessions / (n / 2) else 0
        val i = 2 * completedSessions + shift
        return available[i.mod(n)] to available[(i + 1).mod(n)]
    }
}
