package com.aprouxdev.arcencielplanning.data.services.local

import android.content.Context
import com.aprouxdev.arcencielplanning.ArcEnCielDatabase
import com.squareup.sqldelight.ColumnAdapter
import comaprouxdevarcencielplanning.EventDb
import comaprouxdevarcencielplanning.UserDb



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

        database = ArcEnCielDatabase(
            driver = databaseDriverFactory.createDriver(),
            UserDbAdapter = UserDb.Adapter(
                teamsAdapter = listOfStringsAdapter
            ),
            EventDbAdapter = EventDb.Adapter(
                commentsAdapter = listOfStringsAdapter
            )

            // Add here Adapter if needed
            /*
           UserAdapter = User.Adapter(
            couplesAdapter = listOfStringsAdapter,
            userStateAdapter = EnumColumnAdapter()
            )
             */
        )
    }