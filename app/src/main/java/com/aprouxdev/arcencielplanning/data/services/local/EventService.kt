package com.aprouxdev.arcencielplanning.data.services.local

import com.aprouxdev.arcencielplanning.data.enums.Teams
import com.aprouxdev.arcencielplanning.data.models.Event
import com.aprouxdev.arcencielplanning.extensions.formattedToString
import comaprouxdevarcencielplanning.EventDb
import kotlinx.coroutines.flow.Flow
import java.util.*


internal class EventService {

    companion object {
        val instance = EventService()
    }

    fun isEventExist(date: Date, time: String, teams: Teams): String? {
        val event = database.eventDbQueries.getAllEventByDate(date)
            .executeAsList()
        return event.filter { it.time == time && it.team == teams.getName()}.map { it.id }.firstOrNull()
    }

    fun getEventById(eventId: String): Event? {
        return database.eventDbQueries.getEventDBById(eventId).executeAsOneOrNull()?.toEvent()
    }

    fun getAllEvents(date: Date): List<Event> {
        return database.eventDbQueries
            .getAllEventOverDate(date)
            .executeAsList().map { it.toEvent() }

    }
}