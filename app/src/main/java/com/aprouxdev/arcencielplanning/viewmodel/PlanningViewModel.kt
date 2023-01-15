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
import java.util.*


class PlanningViewModel: ViewModel() {

    private val events: Flow<List<EventDb>> =
        database.eventDbQueries
            .getAllEventOverDate(Calendar.getInstance(Locale.getDefault()).time)
            .asFlow()
            .filterNotNull()
            .mapToList()
            .distinctUntilChanged()

    private var eventList: List<EventDb> = emptyList()
    private var mSelectedDate: Date? = Calendar.getInstance(Locale.getDefault()).time

    private val _allDatesWithItem: MutableStateFlow<List<Date>> = MutableStateFlow(emptyList())
    val allDatesWithItem: StateFlow<List<Date>> get() = _allDatesWithItem


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
        return eventList.filter { it.date == mSelectedDate }.map { it.toEvent() }
    }

    fun selectDate(date: Date) {
        mSelectedDate = date
    }

}