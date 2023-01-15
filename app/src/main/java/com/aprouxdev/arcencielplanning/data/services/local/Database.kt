package com.aprouxdev.arcencielplanning.data.services.local

import android.content.Context
import com.aprouxdev.arcencielplanning.ArcEnCielDatabase
import com.squareup.sqldelight.ColumnAdapter
import comaprouxdevarcencielplanning.AlertDb
import comaprouxdevarcencielplanning.EventDb
import comaprouxdevarcencielplanning.UserDb
import java.time.Instant
import java.util.*


lateinit var database: ArcEnCielDatabase

    fun initializeDatabase(databaseDriverFactory: DatabaseDriverFactory) {
        // Adapter for a list of object
        val listOfStringsAdapter = object : ColumnAdapter<List<String>, String> {
            override fun decode(databaseValue: String) =
                if (databaseValue.isEmpty()) {
                    listOf()
                } else {
                    databaseValue.split(",")
                }
            override fun encode(value: List<String>) = value.joinToString(separator = ",")
        }
        val dateAdapter = object : ColumnAdapter<Date, String> {
            override fun decode(databaseValue: String): Date {
                val instant = Instant.parse(databaseValue)
                return Date.from(instant)
            }

            override fun encode(value: Date): String {
                val instant = value.toInstant()
                return instant.toString()
            }

        }

        database = ArcEnCielDatabase(
            driver = databaseDriverFactory.createDriver(),
            UserDbAdapter = UserDb.Adapter(
                teamsAdapter = listOfStringsAdapter
            ),
            EventDbAdapter = EventDb.Adapter(
                commentsAdapter = listOfStringsAdapter,
                dateAdapter = dateAdapter
            ),
            AlertDbAdapter = AlertDb.Adapter(
                endDateAdapter= dateAdapter
            )
        )
    }