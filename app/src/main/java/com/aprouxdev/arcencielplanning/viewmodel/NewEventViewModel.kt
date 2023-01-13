package com.aprouxdev.arcencielplanning.viewmodel

import androidx.lifecycle.ViewModel
import com.aprouxdev.arcencielplanning.data.models.Event
import com.aprouxdev.arcencielplanning.data.services.online.EventOnlineService
import com.aprouxdev.arcencielplanning.data.services.online.OnlineQueryCallback
import com.aprouxdev.arcencielplanning.utils.getUuid
import com.aprouxdev.arcencielplanning.views.dialogfragments.Period
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.Serializable
import java.util.*

sealed class ProcessState : Serializable {
    object None : ProcessState()
    object IsLoading : ProcessState()
    data class IsSuccessful(val message: String?) : ProcessState()
    data class IsFailed(val message: String?) : ProcessState()
}

class NewEventViewModel : ViewModel() {

    private val _processState: MutableStateFlow<ProcessState> = MutableStateFlow(ProcessState.None)
    val processState: StateFlow<ProcessState> get() = _processState

    fun addEvent(
        event: Event,
        frequencyNumber: Int = 1,
        frequencyPeriod: Period = Period.DAY,
        repetition: Int = 1
    ) {
        _processState.value = ProcessState.IsLoading
        if (event.date == null) {
            _processState.value = ProcessState.IsFailed("Erreur ! Aucune date n'a été sélectionnée")
            return
        }

        val isMultipleTransaction =
            frequencyNumber > 1 || frequencyPeriod != Period.DAY || repetition > 1
        val listener = object : OnlineQueryCallback {
            override fun onSuccessListener() {
                _processState.value = ProcessState.IsSuccessful("Evènement enregistré")
            }

            override fun onFailureListener(message: String?) {
                _processState.value = ProcessState.IsFailed(message)
            }

        }
        if (isMultipleTransaction) {
            val listOfDate = getListOfDates(
                initialDate = event.date,
                frequencyNumber,
                frequencyPeriod,
                repetition
            )
            val listOfEvent = listOfDate?.map {
                Event(
                    id = getUuid(),
                    date = it,
                    time = event.time,
                    team = event.team,
                    users = event.users,
                    title = event.title,
                    description = event.description,
                    comments = event.comments
                )
            }
            listOfEvent?.let { EventOnlineService.instance.addMultipleEvent(it, listener) }
        } else {
            EventOnlineService.instance.addEvent(event, listener)
        }
    }

    private fun getListOfDates(
        initialDate: Date?,
        frequencyNumber: Int,
        frequencyPeriod: Period,
        repetition: Int
    ): List<Date>? {
        initialDate?.let {
            return listOf(initialDate)
        } ?: kotlin.run { return null }
        // TODO

    }


}