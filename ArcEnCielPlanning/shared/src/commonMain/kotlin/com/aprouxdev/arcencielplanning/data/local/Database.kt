package com.aprouxdev.arcencielplanning.data.local

import app.cash.sqldelight.db.SqlDriver
import com.aprouxdev.arcencielplanning.AlertDb
import com.aprouxdev.arcencielplanning.CommentDb
import com.aprouxdev.arcencielplanning.EventDb
import com.aprouxdev.arcencielplanning.UserDb
import com.aprouxdev.arcencielplanning.database.ArcEnCielDatabase

/**
 * Expect declaration for platform-specific database driver
 */
expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}

/**
 * Creates and configures the SQLDelight database with custom adapters
 */
fun createDatabase(driverFactory: DatabaseDriverFactory): ArcEnCielDatabase {
    val driver = driverFactory.createDriver()

    return ArcEnCielDatabase(
        driver = driver,
        EventDbAdapter = EventDb.Adapter(
            dateAdapter = InstantAdapter,
            teamAdapter = TeamsAdapter
        ),
        AlertDbAdapter = AlertDb.Adapter(
            typeAdapter = AlertTypeAdapter,
            endDateAdapter = InstantAdapter
        ),
        CommentDbAdapter = CommentDb.Adapter(
            dateAdapter = InstantAdapter
        )
    )
}
