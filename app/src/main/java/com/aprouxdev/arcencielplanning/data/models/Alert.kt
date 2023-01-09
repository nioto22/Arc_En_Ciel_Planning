package com.aprouxdev.arcencielplanning.data.models

import com.aprouxdev.arcencielplanning.data.enums.AlertType
import java.io.Serializable
import java.time.format.DateTimeFormatter
import java.util.*

data class Alert(
    val id: String,
    val type: String,
    val title: String,
    val body: String,
    val endDate: Date
) : Serializable {

    fun getType(): AlertType {
        return when (type) {
            "General" -> AlertType.General
            "Team" -> AlertType.Team
            "Shop" -> AlertType.Shop
            "Urgency" -> AlertType.Urgency
            "Assiduity" -> AlertType.Assiduity
            else -> AlertType.General
        }
    }

}