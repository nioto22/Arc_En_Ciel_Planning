package com.aprouxdev.arcencielplanning.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aprouxdev.arcencielplanning.data.services.local.AlertService
import com.aprouxdev.arcencielplanning.data.services.local.EventService
import com.aprouxdev.arcencielplanning.data.services.local.toAlert
import com.aprouxdev.arcencielplanning.data.services.local.toEvent
import com.aprouxdev.arcencielplanning.data.services.online.AlertOnlineService
import com.aprouxdev.arcencielplanning.data.services.online.EventOnlineService
import com.aprouxdev.arcencielplanning.data.services.online.toAlert
import com.aprouxdev.arcencielplanning.data.services.online.toEvent
import com.google.firebase.firestore.DocumentChange
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*


class MainViewModel : ViewModel() {

    init {
        listenToOnlineEventChange()
        listenToOnlineAlertChange()
    }

    private val _updateEvents: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val updateEvents: StateFlow<Boolean> get() = _updateEvents

    private val _updateAlerts: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val updateAlerts: StateFlow<Boolean> get() = _updateAlerts

    private fun listenToOnlineEventChange() {
        viewModelScope.launch {
            val eventDbService = EventService.instance
            val now = Calendar.getInstance(Locale.getDefault()).time
            EventOnlineService.eventDatabase.whereGreaterThanOrEqualTo("date", now)
                .addSnapshotListener { snapshots, e ->
                    if (e != null) {
                        Log.e("TAG_DEBUG", "listenToOnlineEventChange: ", e)
                    }
                    for (dc in snapshots!!.documentChanges) {
                        when (dc.type) {
                            DocumentChange.Type.ADDED -> {
                                dc.document.toEvent()?.toEvent()
                                    ?.let { eventDbService.addEvent(it) }
                            }
                            DocumentChange.Type.MODIFIED -> {
                                dc.document.toEvent()?.toEvent()
                                    ?.let { eventDbService.updateEvent(it) }
                            }
                            DocumentChange.Type.REMOVED -> {
                                dc.document.toEvent()?.id?.let {
                                    eventDbService.removeEvent(it)
                                }
                            }
                        }
                    }
                }
        }
    }

    private fun listenToOnlineAlertChange() {
        viewModelScope.launch {
            val alertDbService = AlertService.instance
            val now = Calendar.getInstance(Locale.getDefault())
            AlertOnlineService.alertDatabase
                .whereGreaterThan("endDate", now)
                .addSnapshotListener { snapshots, e ->
                    if (e != null) {
                        Log.e("TAG_DEBUG", "listenToOnlineAlertChange: ", e)
                    }
                    for (dc in snapshots!!.documentChanges) {
                        when (dc.type) {
                            DocumentChange.Type.ADDED -> {
                                dc.document.toAlert()?.toAlert()
                                    ?.let { alertDbService.addAlert(it) }
                            }
                            DocumentChange.Type.MODIFIED -> {
                                dc.document.toAlert()?.toAlert()
                                    ?.let { alertDbService.updateAlert(it) }
                            }
                            DocumentChange.Type.REMOVED -> {
                                dc.document.toAlert()?.id?.let {
                                    alertDbService.removeAlert(it)
                                }
                            }
                        }
                    }
                }
        }
    }

    fun updateEventsCompleted() {
        _updateEvents.value = false
    }

    fun updateAlertsCompleted() {
        _updateAlerts.value = false
    }

}