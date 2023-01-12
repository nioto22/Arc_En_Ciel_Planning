package com.aprouxdev.arcencielplanning.viewmodel

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
import java.util.Date


class PlanningViewModel: ViewModel() {

    private val events: Flow<List<EventDb>> =
        database.eventDbQueries
            .getAllEventDBs()
            .asFlow()
            .filterNotNull()
            .mapToList()
            .distinctUntilChanged()

    private var eventList: List<EventDb> = emptyList()

    private val _allDatesWithItem: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val allDatesWithItem: StateFlow<List<String>> get() = _allDatesWithItem

    private val _selectedDate: MutableStateFlow<String?> = MutableStateFlow(null)
    val selectedDate: StateFlow<String?> get() = _selectedDate

    init {
        listenToEvent()
    }

    private fun listenToEvent() {
        this.viewModelScope.launch {
            events.collect {
                eventList = it
                _allDatesWithItem.value = it.mapNotNull { event -> event.date }
            }
        }
    }

    fun getItemForDate(): List<Event> {
        return eventList.filter { it.date == _selectedDate.value }.map { it.toEvent() }
    }

    fun selectDate(date: String) {
        _selectedDate.value = date
    }

}