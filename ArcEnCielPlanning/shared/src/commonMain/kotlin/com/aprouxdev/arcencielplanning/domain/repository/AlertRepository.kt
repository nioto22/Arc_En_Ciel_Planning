package com.aprouxdev.arcencielplanning.domain.repository

import com.aprouxdev.arcencielplanning.domain.enums.AlertType
import com.aprouxdev.arcencielplanning.domain.models.Alert
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Alert operations
 */
interface AlertRepository {

    /**
     * Get all alerts as a Flow
     */
    fun getAllAlerts(): Flow<List<Alert>>

    /**
     * Get active alerts (endDate in the future)
     */
    fun getActiveAlerts(): Flow<List<Alert>>

    /**
     * Get alerts by type
     */
    fun getAlertsByType(type: AlertType): Flow<List<Alert>>

    /**
     * Get a single alert by ID
     */
    suspend fun getAlertById(id: String): Alert?

    /**
     * Save or update an alert
     */
    suspend fun saveAlert(alert: Alert): Result<Unit>

    /**
     * Delete an alert by ID
     */
    suspend fun deleteAlert(id: String): Result<Unit>

    /**
     * Sync local alerts with remote server
     */
    suspend fun syncAlerts(): Result<Unit>
}
