package com.aprouxdev.arcencielplanning

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform