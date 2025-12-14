package com.aprouxdev.arcencielplanning.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.aprouxdev.arcencielplanning.database.ArcEnCielDatabase
import com.aprouxdev.arcencielplanning.domain.enums.AlertType
import com.aprouxdev.arcencielplanning.domain.models.Alert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

/**
 * Local data source for Alert operations using SQLDelight
 */
class AlertLocalDataSource(private val database: ArcEnCielDatabase) {

    private val queries = database.alertDbQueries

    /**
     * Get all alerts as a Flow
     */
    fun getAllAlerts(): Flow<List<Alert>> {
        return queries.getAllAlerts()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { alertDbList ->
                alertDbList.map { it.toAlert() }
            }
    }

    /**
     * Get active alerts (endDate in the future)
     */
    fun getActiveAlerts(): Flow<List<Alert>> {
        val now = Clock.System.now()
        return queries.getActiveAlerts(now)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { alertDbList ->
                alertDbList.map { it.toAlert() }
            }
    }

    /**
     * Get alerts by type
     */
    fun getAlertsByType(type: AlertType): Flow<List<Alert>> {
        return queries.getAlertsByType(type)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { alertDbList ->
                alertDbList.map { it.toAlert() }
            }
    }

    /**
     * Get a single alert by ID
     */
    suspend fun getAlertById(id: String): Alert? {
        return queries.getAlertById(id).executeAsOneOrNull()?.toAlert()
    }

    /**
     * Insert or replace an alert
     */
    suspend fun insertOrReplace(alert: Alert) {
        queries.insertOrReplace(
            id = alert.id,
            type = alert.type,
            title = alert.title,
            body = alert.body,
            endDate = alert.endDate
        )
    }

    /**
     * Insert or replace multiple alerts
     */
    suspend fun insertOrReplaceAll(alerts: List<Alert>) {
        queries.transaction {
            alerts.forEach { alert ->
                queries.insertOrReplace(
                    id = alert.id,
                    type = alert.type,
                    title = alert.title,
                    body = alert.body,
                    endDate = alert.endDate
                )
            }
        }
    }

    /**
     * Delete an alert by ID
     */
    suspend fun deleteById(id: String) {
        queries.deleteById(id)
    }

    /**
     * Delete all alerts
     */
    suspend fun deleteAll() {
        queries.deleteAll()
    }

    /**
     * Count total alerts
     */
    suspend fun countAlerts(): Long {
        return queries.countAlerts().executeAsOne()
    }

    /**
     * Extension function to convert AlertDb to Alert
     */
    private fun com.aprouxdev.arcencielplanning.AlertDb.toAlert(): Alert {
        return Alert(
            id = this.id,
            type = this.type,
            title = this.title,
            body = this.body,
            endDate = this.endDate
        )
    }
}
