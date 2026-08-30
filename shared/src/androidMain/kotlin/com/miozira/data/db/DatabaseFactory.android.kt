package com.miozira.data.db

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

object DatabaseFactory {
    fun create(context: Context, name: String): MioziraDatabase = Room.databaseBuilder(
        context = context,
        klass = MioziraDatabase::class.java,
        name = name,
    ).setDriver(BundledSQLiteDriver()).build()
}
