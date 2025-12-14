package com.aprouxdev.arcencielplanning.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.aprouxdev.arcencielplanning.database.ArcEnCielDatabase

/**
 * iOS implementation of DatabaseDriverFactory
 */
actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = ArcEnCielDatabase.Schema,
            name = "arcenciel.db"
        )
    }
}
