package com.aprouxdev.arcencielplanning.data.models

import com.aprouxdev.arcencielplanning.data.enums.Teams
import java.util.*


class Event(
    val id: String,
    val date: Date? = null,
    val time: String? = null,
    val team: Teams,
    val users: List<String> = emptyList(),
    val title: String,
    val description: String,
    val comments: List<String>
)