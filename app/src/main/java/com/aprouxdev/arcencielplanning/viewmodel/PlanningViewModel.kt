package com.aprouxdev.arcencielplanning.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aprouxdev.arcencielplanning.data.models.Event
import com.aprouxdev.arcencielplanning.data.services.local.database
import com.aprouxdev.arcencielplanning.data.services.local.toEvent
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import comaprouxdevarcencielplanning.EventDb
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*


class PlanningViewModel: ViewModel() {

    private val events: Flow<List<EventDb>> =
        database.eventDbQueries
            .getAllEventOverDate(Calendar.getInstance(Locale.getDefault()).time)
            .asFlow()
            .filterNotNull()
            .mapToList()
            .distinctUntilChanged()

    private var eventList: List<Event> = emptyList()
    private var mSelectedDate: Date? = Calendar.getInstance(Locale.getDefault()).time

    private val _allDatesWithItem: MutableStateFlow<List<Date>> = MutableStateFlow(emptyList())
    val allDatesWithItem: StateFlow<List<Date>> get() = _allDatesWithItem

    private val _selectedEvents: MutableStateFlow<List<Event>> = MutableStateFlow(emptyList())
    val selectedEvents: StateFlow<List<Event>> get() = _selectedEvents

    init {
        getAllEvent()
        getItemForDate()
    }

    fun getAllEvent() {
        eventList = database.eventDbQueries
                .getAllEventOverDate(Calendar.getInstance(Locale.getDefault()).time)
            .executeAsList().map { it.toEvent() }
        getAllDates()
    }

    private fun getAllDates() {
        _allDatesWithItem.value = eventList.mapNotNull { event -> event.date }
    }


    fun getItemForDate() {
        val selectedEvents = eventList.filter { it.hasSameDate(mSelectedDate) }
        Log.d("TAG_DEBUG", "selectEvents: size ${selectedEvents.size}")
        _selectedEvents.value = selectedEvents
    }

    fun selectDate(date: Date) {
        mSelectedDate = date
        getItemForDate()
    }

}