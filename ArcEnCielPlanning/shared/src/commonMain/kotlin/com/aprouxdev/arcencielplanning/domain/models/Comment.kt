package com.aprouxdev.arcencielplanning.domain.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val id: String,
    val eventId: String,
    val userId: String,
    val user: User? = null,
    val text: String,
    val date: Instant
)
