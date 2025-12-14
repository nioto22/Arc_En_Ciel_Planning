package com.aprouxdev.arcencielplanning.domain.models

import com.aprouxdev.arcencielplanning.domain.enums.AlertType
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Alert(
    val id: String,
    val type: AlertType,
    val title: String? = null,
    val body: String? = null,
    val endDate: Instant
)
