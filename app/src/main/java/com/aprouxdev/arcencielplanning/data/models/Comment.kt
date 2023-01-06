package com.aprouxdev.arcencielplanning.data.models

import java.io.Serializable
import java.util.*


data class Comment(
    val id: String,
    val userId: String,
    val user: String,
    val text: String,
    val date: Date?
) : Serializable