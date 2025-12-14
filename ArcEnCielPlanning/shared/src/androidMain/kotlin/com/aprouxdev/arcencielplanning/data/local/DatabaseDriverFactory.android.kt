package com.aprouxdev.arcencielplanning.data.local

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.aprouxdev.arcencielplanning.database.ArcEnCielDatabase

/**
 * Android implementation of DatabaseDriverFactory
 */
actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = ArcEnCielDatabase.Schema,
            context = context,
            name = "arcenciel.db"
        )
    }
}
