package com.aprouxdev.arcencielplanning.data.services.local

import com.aprouxdev.arcencielplanning.data.models.Alert
import java.time.LocalDate
import java.util.*


class AlertService {

    companion object {
        val instance = AlertService()
    }

    fun getAllAlerts(date: Date): List<Alert> {
        val allAlerts = database.alertDbQueries.getAllAlerts().executeAsList()
        return allAlerts.filter { it.endDate != null && it.endDate > date }.map { it.toAlert() }
    }
}