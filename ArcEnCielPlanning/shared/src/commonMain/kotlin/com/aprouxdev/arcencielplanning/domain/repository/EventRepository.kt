package com.aprouxdev.arcencielplanning.domain.repository

import com.aprouxdev.arcencielplanning.domain.enums.Teams
import com.aprouxdev.arcencielplanning.domain.models.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

/**
 * Repository interface for Event operations
 * Provides abstraction over data sources (local and remote)
 */
interface EventRepository {

    /**
     * Get all events as a Flow
     */
    fun getEvents(): Flow<List<Event>>

    /**
     * Get events between two dates
     */
    fun getEventsBetween(from: Instant, to: Instant?): Flow<List<Event>>

    /**
     * Get events by team
     */
    fun getEventsByTeam(team: Teams): Flow<List<Event>>

    /**
     * Get a single event by ID
     */
    suspend fun getEventById(id: String): Event?

    /**
     * Create or update an event
     */
    suspend fun saveEvent(event: Event): Result<Unit>

    /**
     * Delete an event by ID
     */
    suspend fun deleteEvent(id: String): Result<Unit>

    /**
     * Sync local events with remote server
     */
    suspend fun syncEvents(): Result<Unit>

    /**
     * Get dates that have events (for calendar UI)
     */
    suspend fun getDatesWithEvents(): List<Instant>
}
