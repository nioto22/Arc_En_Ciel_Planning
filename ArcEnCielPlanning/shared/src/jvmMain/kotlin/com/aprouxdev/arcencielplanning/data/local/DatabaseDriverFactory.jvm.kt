package com.aprouxdev.arcencielplanning.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.aprouxdev.arcencielplanning.database.ArcEnCielDatabase

/**
 * JVM implementation of DatabaseDriverFactory (for server/desktop)
 */
actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        ArcEnCielDatabase.Schema.create(driver)
        return driver
    }
}
