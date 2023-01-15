package com.aprouxdev.arcencielplanning.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aprouxdev.arcencielplanning.data.enums.Teams
import com.aprouxdev.arcencielplanning.data.models.Event
import com.aprouxdev.arcencielplanning.data.services.local.EventService
import com.aprouxdev.arcencielplanning.data.services.online.EventOnlineService
import com.aprouxdev.arcencielplanning.data.services.online.OnlineQueryCallback
import com.aprouxdev.arcencielplanning.extensions.toTimeString
import com.aprouxdev.arcencielplanning.utils.getUuid
import com.aprouxdev.arcencielplanning.views.dialogfragments.Period
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.Serializable
import java.util.*

sealed class ProcessState : Serializable {
    object None : ProcessState()
    object IsLoading : ProcessState()
    object Close : ProcessState()
    data class IsSuccessful(val message: String?) : ProcessState()
    data class IsFailed(val message: String?) : ProcessState()
}

data class EventExistenceState(
    val eventId: String?,
    val teams: Teams?,
    val isMultipleFrequency: Boolean
)

class NewEventViewModel : ViewModel() {

    private val _processState: MutableStateFlow<ProcessState> = MutableStateFlow(ProcessState.None)
    val processState: StateFlow<ProcessState> get() = _processState

    private val _alreadyExist: MutableStateFlow<EventExistenceState?> = MutableStateFlow(null)
    val alreadyExist: StateFlow<EventExistenceState?> get() = _alreadyExist


    private var mSelectedTeam: Teams? = null
    private var mSelectedDate: Date = Calendar.getInstance().time
    private var mSelectedHour: Int = 8
    private var mSelectedMinute: Int = 0
    private var mFrequencyNumber: Int = 1
    private var mFrequencyPeriod: Period = Period.DAY
    private var mFrequency: Int = 1

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
                viewModelScope.launch {
                    delay(1500)
                    _processState.value = ProcessState.Close
                }
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
        if (initialDate == null) return null
        val datesList = arrayListOf(initialDate)
        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.time = initialDate
        var index = 0
        do {
            val periodValue = when (frequencyPeriod) {
                Period.DAY -> Calendar.DAY_OF_YEAR
                Period.WEEK -> Calendar.WEEK_OF_YEAR
                Period.MONTH -> Calendar.MONTH
                Period.YEAR -> Calendar.YEAR
            }
            calendar.add(periodValue, frequencyNumber)
            datesList.add(calendar.time)
            index++
        } while (index < repetition)
        return datesList
    }

    fun updateData(
        teams: Teams? = null,
        selectedDate: Date? = null,
        selectedHour: Int? = null,
        selectedMinute: Int? = null,
        frequencyNumber: Int? = null,
        frequencyPeriod: Period? = null,
        repetition: Int? = null
    ) {
        teams?.let { this.mSelectedTeam = it }
        selectedDate?.let { this.mSelectedDate = it }
        selectedHour?.let { this.mSelectedHour = it }
        selectedMinute?.let { this.mSelectedMinute = it }
        frequencyNumber?.let { this.mFrequencyNumber = it }
        frequencyPeriod?.let { this.mFrequencyPeriod = it }
        repetition?.let { this.mFrequency = it }

        checkEventAvailability()
    }


    /**
     * Set mSelectedTeam != null
     * Get List of date
     * For each date check if event already exists with same time and team
     * Update EventExistenceState value
     */
    private fun checkEventAvailability() {
        if (mSelectedTeam == null) {
            _alreadyExist.value = null
            return
        }
        val listOfDates =
            if (isMultipleSelection()) getListOfDates(
                mSelectedDate,
                frequencyNumber = mFrequencyNumber,
                frequencyPeriod = mFrequencyPeriod,
                repetition = mFrequency
            )
            else {
                listOf(mSelectedDate)
            }
        val eventService = EventService.instance
        val oldEvents = ArrayList<String>()
        listOfDates?.forEach {
            val existId = eventService.isEventExist(
                it,
                "${mSelectedHour.toTimeString()}:${mSelectedMinute.toTimeString()}",
                mSelectedTeam!!
            )
            existId?.let { id -> oldEvents.add(id) }
        }

        when (oldEvents.size) {
            0 -> _alreadyExist.value = null
            1 -> _alreadyExist.value =
                EventExistenceState(oldEvents[0], mSelectedTeam, isMultipleFrequency = false)
            else -> _alreadyExist.value =
                EventExistenceState(eventId = null, mSelectedTeam, isMultipleFrequency = true)
        }
    }

    private fun isMultipleSelection(): Boolean {
        return mFrequencyNumber > 1 || mFrequency > 1
    }


}