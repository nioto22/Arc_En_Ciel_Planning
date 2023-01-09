package com.aprouxdev.arcencielplanning.data.services.local

import android.content.Context
import com.aprouxdev.arcencielplanning.ArcEnCielDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver


class DatabaseDriverFactory(private val context: Context) {

    fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(ArcEnCielDatabase.Schema, context, "arcencieldatabase.db")
    }


}