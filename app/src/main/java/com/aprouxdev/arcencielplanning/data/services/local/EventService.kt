package com.aprouxdev.arcencielplanning.data.services.local

import com.aprouxdev.arcencielplanning.data.models.Event
import kotlinx.coroutines.flow.Flow
import java.util.*


interface EventService {

    suspend fun getEventById(id: String): Event?

    fun getAllEvents(): Flow<List<Event>>

    suspend fun deleteEventById(id: String)

    suspend fun insertEvent(event: Event)

    fun getAllEventsByDate(date: Date): Flow<List<Event>>
    fun getAllEventsByDate(startDate: Date, endDate: Date): Flow<List<Event>>
}