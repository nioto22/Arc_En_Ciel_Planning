package com.aprouxdev.arcencielplanning.data.repository

import com.aprouxdev.arcencielplanning.data.local.EventLocalDataSource
import com.aprouxdev.arcencielplanning.database.ArcEnCielDatabase
import com.aprouxdev.arcencielplanning.domain.enums.Teams
import com.aprouxdev.arcencielplanning.domain.models.Event
import com.aprouxdev.arcencielplanning.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

/**
 * Implementation of EventRepository using offline-first approach
 * Currently uses only local data source; remote sync will be added later
 */
class EventRepositoryImpl(
    private val localDataSource: EventLocalDataSource
) : EventRepository {

    override fun getEvents(): Flow<List<Event>> {
        return localDataSource.getEvents()
    }

    override fun getEventsBetween(from: Instant, to: Instant?): Flow<List<Event>> {
        return localDataSource.getEventsBetween(from, to)
    }

    override fun getEventsByTeam(team: Teams): Flow<List<Event>> {
        return localDataSource.getEventsByTeam(team)
    }

    override suspend fun getEventById(id: String): Event? {
        return try {
            localDataSource.getEventById(id)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun saveEvent(event: Event): Result<Unit> {
        return try {
            localDataSource.insertOrReplace(event)
            // TODO: Sync with remote server when remote data source is implemented
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteEvent(id: String): Result<Unit> {
        return try {
            localDataSource.deleteById(id)
            // TODO: Sync deletion with remote server
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun syncEvents(): Result<Unit> {
        // TODO: Implement sync with remote server
        // For now, this is a placeholder
        return Result.success(Unit)
    }

    override suspend fun getDatesWithEvents(): List<Instant> {
        // TODO: Add query to SQLDelight to get distinct dates
        return emptyList()
    }

    companion object {
        /**
         * Factory method to create EventRepositoryImpl
         */
        fun create(database: ArcEnCielDatabase): EventRepositoryImpl {
            return EventRepositoryImpl(
                localDataSource = EventLocalDataSource(database)
            )
        }
    }
}
