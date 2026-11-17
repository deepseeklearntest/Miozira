package com.miozira.domain.adaptive

data class AdaptivePolicy(
    val version: String = "prototype-0.1",
    val emergingIntervalMs: Long = 24L * 60L * 60L * 1000L,
    val familiarIntervalMs: Long = 3L * 24L * 60L * 60L * 1000L,
    val familiarWithUseIntervalMs: Long = 5L * 24L * 60L * 60L * 1000L,
    val defaultRestMs: Long = 4L * 24L * 60L * 60L * 1000L,
    val extendedRestMs: Long = 7L * 24L * 60L * 60L * 1000L,
    val maxNewPairsPerBlock: Int = 3,
)
