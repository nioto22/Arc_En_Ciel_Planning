package com.aprouxdev.arcencielplanning.domain.models

import com.aprouxdev.arcencielplanning.domain.enums.Teams
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String,
    val isAdmin: Boolean = false,
    val imageUrl: String? = null,
    val teams: List<Teams> = emptyList()
)
