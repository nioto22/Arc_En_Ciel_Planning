package com.aprouxdev.arcencielplanning.data.repository

import com.aprouxdev.arcencielplanning.data.local.AlertLocalDataSource
import com.aprouxdev.arcencielplanning.database.ArcEnCielDatabase
import com.aprouxdev.arcencielplanning.domain.enums.AlertType
import com.aprouxdev.arcencielplanning.domain.models.Alert
import com.aprouxdev.arcencielplanning.domain.repository.AlertRepository
import kotlinx.coroutines.flow.Flow

/**
 * Implementation of AlertRepository using offline-first approach
 */
class AlertRepositoryImpl(
    private val localDataSource: AlertLocalDataSource
) : AlertRepository {

    override fun getAllAlerts(): Flow<List<Alert>> {
        return localDataSource.getAllAlerts()
    }

    override fun getActiveAlerts(): Flow<List<Alert>> {
        return localDataSource.getActiveAlerts()
    }

    override fun getAlertsByType(type: AlertType): Flow<List<Alert>> {
        return localDataSource.getAlertsByType(type)
    }

    override suspend fun getAlertById(id: String): Alert? {
        return try {
            localDataSource.getAlertById(id)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun saveAlert(alert: Alert): Result<Unit> {
        return try {
            localDataSource.insertOrReplace(alert)
            // TODO: Sync with remote server
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteAlert(id: String): Result<Unit> {
        return try {
            localDataSource.deleteById(id)
            // TODO: Sync deletion with remote server
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun syncAlerts(): Result<Unit> {
        // TODO: Implement sync with remote server
        return Result.success(Unit)
    }

    companion object {
        fun create(database: ArcEnCielDatabase): AlertRepositoryImpl {
            return AlertRepositoryImpl(
                localDataSource = AlertLocalDataSource(database)
            )
        }
    }
}
