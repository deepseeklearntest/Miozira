package com.miozira.core.time

fun interface Clock {
    fun nowMs(): Long
}
