package com.aprouxdev.arcencielplanning.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aprouxdev.arcencielplanning.data.mock.MockData
import com.aprouxdev.arcencielplanning.data.models.Event
import com.aprouxdev.arcencielplanning.data.services.local.database
import com.aprouxdev.arcencielplanning.data.services.local.toEvent
import com.aprouxdev.arcencielplanning.data.services.local.toEventDb
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
        mockData()
        eventList = database.eventDbQueries
                .getAllEventOverDate(Calendar.getInstance(Locale.getDefault()).time)
            .executeAsList().map { it.toEvent() }
        getAllDates()
    }

    private fun mockData() {
        val mockEvents = listOf(MockData.event1, MockData.event2,
            MockData.event3, MockData.event4,
            MockData.event5, MockData.event6,
            MockData.event7, MockData.event8
        )
        mockEvents.forEachIndexed { index, event ->
            val now = Calendar.getInstance(Locale.getDefault())
            now.add(Calendar.DAY_OF_YEAR, index)
            event.date = now.time
            event.id = now.time.toString()
            database.eventDbQueries.insertOrReplaceEventDB(event.toEventDb())
        }
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