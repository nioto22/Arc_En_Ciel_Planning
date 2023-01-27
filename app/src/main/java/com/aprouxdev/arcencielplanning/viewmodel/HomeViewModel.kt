package com.aprouxdev.arcencielplanning.viewmodel

import androidx.lifecycle.ViewModel
import com.aprouxdev.arcencielplanning.data.models.Alert
import com.aprouxdev.arcencielplanning.data.models.Event
import com.aprouxdev.arcencielplanning.data.services.local.AlertService
import com.aprouxdev.arcencielplanning.data.services.local.EventService
import com.aprouxdev.arcencielplanning.data.services.local.database
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*


class HomeViewModel: ViewModel() {


    private val _alertList: MutableStateFlow<List<Alert>> = MutableStateFlow(emptyList())
    val alertList: StateFlow<List<Alert>> get() = _alertList

    private val _eventList: MutableStateFlow<List<Event>> = MutableStateFlow(emptyList())
    val eventList: StateFlow<List<Event>> get() = _eventList

    init {
        getUserId()
        getAllAlert()
        getAllUserEvents()
    }

    private var userId: String? = null
    set(value) {
        field = value
        getAllUserEvents()
    }


    private fun getUserId() {
       userId = database.userDbQueries.getAllUserDbs().executeAsOneOrNull()?.id
    }

    fun getAllAlert() {
        val alertList = AlertService.instance.getAllAlerts(Calendar.getInstance(Locale.getDefault()).time)
        _alertList.value = alertList
    }

    fun getAllUserEvents() {
        val allEvent = EventService.instance
            .getAllEvents(Calendar.getInstance(Locale.getDefault()).time)
            .filter { it.users?.contains(userId) == true }
        _eventList.value = allEvent
    }

}