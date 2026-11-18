package com.miozira.core.time

import java.time.Instant
import java.time.ZoneId

object AndroidLocalDay : LocalDay {
    override fun epochDayOf(epochMs: Long): Long =
        Instant.ofEpochMilli(epochMs).atZone(ZoneId.systemDefault()).toLocalDate().toEpochDay()
}
