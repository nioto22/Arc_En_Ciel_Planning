package com.aprouxdev.arcencielplanning.data.models

import com.aprouxdev.arcencielplanning.data.enums.Teams
import com.aprouxdev.arcencielplanning.data.enums.getTeamByName
import com.aprouxdev.arcencielplanning.utils.getUuid
import java.io.Serializable
import java.util.*


class Event(
    val id: String = getUuid(),
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

    companion object
}