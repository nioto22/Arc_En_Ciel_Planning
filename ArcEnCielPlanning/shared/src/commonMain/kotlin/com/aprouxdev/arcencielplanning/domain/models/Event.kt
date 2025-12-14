package com.aprouxdev.arcencielplanning.domain.models

import com.aprouxdev.arcencielplanning.domain.enums.Teams
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
data class Event(
    val id: String = generateUuid(),
    val date: Instant,
    val time: String, // Format: "HH:mm"
    val team: Teams,
    val users: List<String> = emptyList(),
    val title: String? = null,
    val description: String? = null,
    val comments: List<String> = emptyList() // List of comment IDs
) {
    /**
     * Check if this event occurs on the same date as the given instant
     */
    fun hasSameDate(selectedDate: Instant): Boolean {
        val eventLocalDate = date.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val selectedLocalDate = selectedDate.toLocalDateTime(TimeZone.currentSystemDefault()).date

        return eventLocalDate == selectedLocalDate
    }

    companion object {
        /**
         * Simple UUID generator for KMP
         * In production, consider using a proper UUID library
         */
        private fun generateUuid(): String {
            val chars = "0123456789abcdef"
            return buildString(36) {
                for (i in 0 until 36) {
                    if (i == 8 || i == 13 || i == 18 || i == 23) {
                        append('-')
                    } else {
                        append(chars[Random.nextInt(chars.length)])
                    }
                }
            }
        }
    }
}
