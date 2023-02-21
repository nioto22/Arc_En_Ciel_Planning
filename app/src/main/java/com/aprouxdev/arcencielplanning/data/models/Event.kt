package com.aprouxdev.arcencielplanning.data.models

import com.aprouxdev.arcencielplanning.data.enums.Teams
import com.aprouxdev.arcencielplanning.data.enums.getTeamByName
import com.aprouxdev.arcencielplanning.utils.getUuid
import java.io.Serializable
import java.time.Instant
import java.time.Year
import java.util.*


class Event(
    var id: String = getUuid(),
    var date: Date? = null,
    var time: String? = null,
    var team: String = "Général",
    val users: List<String>? = null,
    var title: String? = null,
    val description: String? = null,
    var comments: List<String>? = null
) : Serializable {
    fun getTeamByName(): Teams {
        return team.getTeamByName()
    }

    fun hasSameDate(mSelectedDate: Date?): Boolean {
       if (this.date == null) return false
        if (mSelectedDate == null) return false
        val eventCalendar = Calendar.getInstance().apply { time = this@Event.date!! }
        val selectedCalendar = Calendar.getInstance().apply { time = mSelectedDate }

        return eventCalendar.get(Calendar.YEAR) == selectedCalendar.get(Calendar.YEAR) &&
                eventCalendar.get(Calendar.MONTH) == selectedCalendar.get(Calendar.MONTH) &&
                eventCalendar.get(Calendar.DAY_OF_MONTH) == selectedCalendar.get(Calendar.DAY_OF_MONTH)
    }

    companion object
}