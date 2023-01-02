package com.aprouxdev.arcencielplanning.data.models


data class User(
    val id: String,
    val name: String,
    val isAdmin : Boolean,
    val imageUrl: String?,
    val teams: List<Int>?
)