package com.miozira.domain.session

data class SessionTimings(
    val invitePauseMs: Long = 600,     // stand-in for the spoken "your turn" (guide-voice plan replaces it)
    val turnMs: Long = 4_000,
    val nodMs: Long = 700,
    val afterTurnMs: Long = 1_000,
    val breathMs: Long = 1_000,
    val idleQuietMs: Long = 60_000,
    val maxClipFailuresInARow: Int = 3,
)
