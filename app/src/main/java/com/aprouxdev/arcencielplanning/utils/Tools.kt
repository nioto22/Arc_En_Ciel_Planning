package com.aprouxdev.arcencielplanning.utils

import java.util.*


fun getUuid(): String {
    val uuid = UUID.randomUUID().toString()
    return if (uuid.length > 20) uuid.substring(IntRange(0, 20)) else uuid
}