package com.aprouxdev.arcencielplanning.data.models

import com.aprouxdev.arcencielplanning.data.enums.Teams


data class User(
    val id: String,
    val name: String,
    val isAdmin: Boolean,
    val team: Teams,
    val imageUrl: String
)
