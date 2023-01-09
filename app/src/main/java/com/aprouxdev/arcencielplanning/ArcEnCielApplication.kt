package com.aprouxdev.arcencielplanning

import android.app.Application
import com.aprouxdev.arcencielplanning.data.services.local.DatabaseDriverFactory
import com.aprouxdev.arcencielplanning.data.services.local.initializeDatabase


class ArcEnCielApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        DatabaseDriverFactory(applicationContext).createDriver()

        initializeDatabase(DatabaseDriverFactory(applicationContext))

    }
}