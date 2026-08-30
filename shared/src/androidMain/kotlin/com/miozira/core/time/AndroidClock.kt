package com.miozira.core.time

object AndroidClock : Clock {
    override fun nowMs(): Long = System.currentTimeMillis()
}
