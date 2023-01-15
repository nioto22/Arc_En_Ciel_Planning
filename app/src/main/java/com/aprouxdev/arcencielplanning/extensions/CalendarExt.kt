package com.aprouxdev.arcencielplanning.extensions

import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
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

fun Int.toTimeString(): String {
    var st = this.toString()
    if (st.length < 2) st = "0$st"
    return st
}

fun LocalDate.toDate(): Date {
    return Date.from(this.atStartOfDay(ZoneId.systemDefault()).toInstant())
}
fun Date.toLocaleDate(): LocalDate {
    return this.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
}