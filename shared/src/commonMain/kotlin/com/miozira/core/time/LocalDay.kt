package com.miozira.core.time

fun interface LocalDay {
    fun epochDayOf(epochMs: Long): Long
}
