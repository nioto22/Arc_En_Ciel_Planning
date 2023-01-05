package com.aprouxdev.arcencielplanning.extensions

import java.text.SimpleDateFormat
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