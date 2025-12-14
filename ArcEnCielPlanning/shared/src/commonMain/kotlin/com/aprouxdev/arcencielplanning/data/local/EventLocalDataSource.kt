package com.aprouxdev.arcencielplanning.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.aprouxdev.arcencielplanning.database.ArcEnCielDatabase
import com.aprouxdev.arcencielplanning.domain.enums.Teams
import com.aprouxdev.arcencielplanning.domain.models.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Local data source for Event operations using SQLDelight
 */
class EventLocalDataSource(private val database: ArcEnCielDatabase) {

    private val queries = database.eventDbQueries
    private val json = Json { ignoreUnknownKeys = true }

    /**
     * Get all events as a Flow
     */
    fun getEvents(): Flow<List<Event>> {
        return queries.getAllEvents()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { eventDbList ->
                eventDbList.map { it.toEvent() }
            }
    }

    /**
     * Get events between two dates
     */
    fun getEventsBetween(from: Instant, to: Instant?): Flow<List<Event>> {
        return if (to != null) {
            queries.getAllEventsByDateRange(from, to)
                .asFlow()
                .mapToList(Dispatchers.Default)
                .map { eventDbList ->
                    eventDbList.map { it.toEvent() }
                }
        } else {
            queries.getAllEventsAfterDate(from)
                .asFlow()
                .mapToList(Dispatchers.Default)
                .map { eventDbList ->
                    eventDbList.map { it.toEvent() }
                }
        }
    }

    /**
     * Get events by team
     */
    fun getEventsByTeam(team: Teams): Flow<List<Event>> {
        return queries.getAllEventsByTeam(team)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { eventDbList ->
                eventDbList.map { it.toEvent() }
            }
    }

    /**
     * Get a single event by ID
     */
    suspend fun getEventById(id: String): Event? {
        return queries.getEventById(id).executeAsOneOrNull()?.toEvent()
    }

    /**
     * Insert or replace an event
     */
    suspend fun insertOrReplace(event: Event) {
        queries.insertOrReplace(
            id = event.id,
            date = event.date,
            time = event.time,
            team = event.team,
            users = json.encodeToString(event.users),
            title = event.title,
            description = event.description,
            comments = json.encodeToString(event.comments)
        )
    }

    /**
     * Insert or replace multiple events
     */
    suspend fun insertOrReplaceAll(events: List<Event>) {
        queries.transaction {
            events.forEach { event ->
                queries.insertOrReplace(
                    id = event.id,
                    date = event.date,
                    time = event.time,
                    team = event.team,
                    users = json.encodeToString(event.users),
                    title = event.title,
                    description = event.description,
                    comments = json.encodeToString(event.comments)
                )
            }
        }
    }

    /**
     * Delete an event by ID
     */
    suspend fun deleteById(id: String) {
        queries.deleteById(id)
    }

    /**
     * Delete all events
     */
    suspend fun deleteAll() {
        queries.deleteAll()
    }

    /**
     * Count total events
     */
    suspend fun countEvents(): Long {
        return queries.countEvents().executeAsOne()
    }

    /**
     * Extension function to convert EventDb to Event
     */
    private fun com.aprouxdev.arcencielplanning.EventDb.toEvent(): Event {
        return Event(
            id = this.id,
            date = this.date,
            time = this.time,
            team = this.team,
            users = try {
                json.decodeFromString<List<String>>(this.users)
            } catch (e: Exception) {
                emptyList()
            },
            title = this.title,
            description = this.description,
            comments = try {
                json.decodeFromString<List<String>>(this.comments)
            } catch (e: Exception) {
                emptyList()
            }
        )
    }
}
