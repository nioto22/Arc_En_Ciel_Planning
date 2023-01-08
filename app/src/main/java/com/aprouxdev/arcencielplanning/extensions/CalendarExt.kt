package com.aprouxdev.arcencielplanning.extensions

import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.util.*

fun Date.formattedToString(pattern: String): String {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.formattedToString(pattern)
}

fun Calendar.formattedToString(pattern: String): String {
    val format1 = SimpleDateFormat(pattern, Locale.getDefault())
    return format1.format(this.time)
}

fun DayOfWeek.getLegendName(): String {
    return when(this.name) {
        "MONDAY" -> "Lun"
        "TUESDAY" -> "Mar"
        "WEDNESDAY" -> "Mer"
        "THURSDAY" -> "Jeu"
        "FRIDAY" -> "Ven"
        "SATURDAY" -> "Sam"
        "SUNDAY" -> "Dim"
        else -> "Err"
    }
}