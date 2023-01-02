package com.aprouxdev.arcencielplanning.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Calendar.formattedToString(pattern: String): String {
    val format1 = SimpleDateFormat(pattern, Locale.getDefault())
    return format1.format(this.time)
}