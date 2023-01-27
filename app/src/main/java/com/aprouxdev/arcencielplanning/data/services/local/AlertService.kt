package com.aprouxdev.arcencielplanning.data.services.local

import com.aprouxdev.arcencielplanning.data.models.Alert
import com.aprouxdev.arcencielplanning.data.models.Event
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

    fun removeAlert(alertId: String) {
        database.alertDbQueries.deleteAlertById(alertId)
    }
    fun addAlert(alert: Alert) {
        database.alertDbQueries.insertOrReplaceAlert(alert.toAlertDb())
    }
    fun updateAlert(alert: Alert) {
        database.alertDbQueries.insertOrReplaceAlert(alert.toAlertDb())
    }
}