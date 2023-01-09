package com.aprouxdev.arcencielplanning.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aprouxdev.arcencielplanning.data.services.local.LoginState
import com.aprouxdev.arcencielplanning.data.services.local.UserService
import com.aprouxdev.arcencielplanning.data.services.local.database
import com.aprouxdev.arcencielplanning.data.services.online.toAlert
import com.aprouxdev.arcencielplanning.data.services.online.toEvent
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import comaprouxdevarcencielplanning.AlertDb
import comaprouxdevarcencielplanning.EventDb
import comaprouxdevarcencielplanning.UserDb
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

sealed class SetupState {
    object Loading: SetupState()
    object NoUser: SetupState()
    class Logged(val user: UserDb): SetupState()
}

class SetupViewModel: ViewModel() {

    private val onlineDb = Firebase.firestore
    private var allEventsLoaded = false
    private var allAlertsLoaded = false


    private var _setupState: MutableStateFlow<SetupState> = MutableStateFlow(SetupState.Loading)
    val setupState: StateFlow<SetupState> = _setupState

    private var _isLoadingComplete = MutableStateFlow<Boolean>(false)
    val isLoadingComplete: StateFlow<Boolean> = _isLoadingComplete


    init {
        viewModelScope.launch {
            UserService.instance.loadUser()
            getLocalUser()
        }
    }

    private suspend fun getLocalUser() {
        UserService.instance.loginState.collect {
          val state: SetupState = when(it) {
                LoginState.Loading -> SetupState.Loading
                is LoginState.Logged -> SetupState.Logged(it.user)
                LoginState.None -> SetupState.NoUser
            }
            _setupState.value = state
        }

    }

    fun refreshAllData() {
        getAllEventsFromFirestore()
        val today = LocalDate.now()
        getAllAlertFromFirestore(today)
    }

    enum class DataRefreshing {
        Events, Alerts
    }

    private fun getAllEventsFromFirestore() {
        onlineDb.collection("event")
                //.whereEqualTo("capital", true)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val event : EventDb? = document.toEvent()
                    event?.let { database.eventDbQueries.insertOrReplaceEventDB(it) }
                }
                updateLoader(DataRefreshing.Events)
            }
            .addOnFailureListener {
                Log.d("TAG_DEBUG", "getAllEventsFormFirestore: Failed : ${it.message}")
                updateLoader(DataRefreshing.Events)
            }
    }

    private fun getAllAlertFromFirestore(today: LocalDate) {
        onlineDb.collection("alert")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val alert: AlertDb? = document.toAlert()
                    alert?.let { database.alertDbQueries.insertOrReplaceAlert(it) }
                }
                updateLoader(DataRefreshing.Alerts)
            }
            .addOnFailureListener {
                Log.d("TAG_DEBUG", "getAllAlertFromFirestore: Failed : ${it.message}")
                updateLoader(DataRefreshing.Alerts)
            }
    }

    private fun updateLoader(data: DataRefreshing) {
        when(data) {
            DataRefreshing.Events -> allEventsLoaded = true
            DataRefreshing.Alerts -> allAlertsLoaded = true
        }
        if (allEventsLoaded && allAlertsLoaded) {
            _isLoadingComplete.value = true
        }
    }

}