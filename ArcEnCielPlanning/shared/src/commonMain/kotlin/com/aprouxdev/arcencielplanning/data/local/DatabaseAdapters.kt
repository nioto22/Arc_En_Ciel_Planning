package com.aprouxdev.arcencielplanning.data.local

import app.cash.sqldelight.ColumnAdapter
import com.aprouxdev.arcencielplanning.domain.enums.AlertType
import com.aprouxdev.arcencielplanning.domain.enums.Teams
import kotlinx.datetime.Instant
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Adapter for kotlinx.datetime.Instant to Long (timestamp in milliseconds)
 */
object InstantAdapter : ColumnAdapter<Instant, Long> {
    override fun decode(databaseValue: Long): Instant {
        return Instant.fromEpochMilliseconds(databaseValue)
    }

    override fun encode(value: Instant): Long {
        return value.toEpochMilliseconds()
    }
}

/**
 * Adapter for Teams enum to String
 */
object TeamsAdapter : ColumnAdapter<Teams, String> {
    override fun decode(databaseValue: String): Teams {
        return try {
            Teams.valueOf(databaseValue)
        } catch (e: IllegalArgumentException) {
            Teams.Other
        }
    }

    override fun encode(value: Teams): String {
        return value.name
    }
}

/**
 * Adapter for AlertType enum to String
 */
object AlertTypeAdapter : ColumnAdapter<AlertType, String> {
    override fun decode(databaseValue: String): AlertType {
        return try {
            AlertType.valueOf(databaseValue)
        } catch (e: IllegalArgumentException) {
            AlertType.General
        }
    }

    override fun encode(value: AlertType): String {
        return value.name
    }
}

/**
 * Adapter for List<String> to JSON String
 */
object StringListAdapter : ColumnAdapter<List<String>, String> {
    private val json = Json { ignoreUnknownKeys = true }

    override fun decode(databaseValue: String): List<String> {
        return if (databaseValue.isEmpty()) {
            emptyList()
        } else {
            try {
                json.decodeFromString<List<String>>(databaseValue)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    override fun encode(value: List<String>): String {
        return json.encodeToString(value)
    }
}

/**
 * Adapter for List<Teams> to JSON String
 */
object TeamsListAdapter : ColumnAdapter<List<Teams>, String> {
    private val json = Json { ignoreUnknownKeys = true }

    override fun decode(databaseValue: String): List<Teams> {
        return if (databaseValue.isEmpty()) {
            emptyList()
        } else {
            try {
                json.decodeFromString<List<Teams>>(databaseValue)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    override fun encode(value: List<Teams>): String {
        return json.encodeToString(value)
    }
}

/**
 * Adapter for Boolean to Integer (SQLite doesn't have boolean type)
 */
object BooleanAdapter : ColumnAdapter<Boolean, Long> {
    override fun decode(databaseValue: Long): Boolean {
        return databaseValue == 1L
    }

    override fun encode(value: Boolean): Long {
        return if (value) 1L else 0L
    }
}
