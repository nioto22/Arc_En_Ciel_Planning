package com.aprouxdev.arcencielplanning.data.models

import com.aprouxdev.arcencielplanning.data.enums.AlertType
import java.io.Serializable
import java.util.*

data class Alert(
    val id: String = "dde",
    val type: String? = null,
    val title: String? = null,
    val body: String? = null,
    val endDate: Date? = null
) : Serializable {



    fun getTypeByName(): AlertType {
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