package com.aprouxdev.arcencielplanning.data.models

import com.aprouxdev.arcencielplanning.R
import com.aprouxdev.arcencielplanning.data.enums.AlertType

data class Alert(
    val id: String,
    val type: AlertType,
    val title: String,
    val body: String,
    val endDate: String
)