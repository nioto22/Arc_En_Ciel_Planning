package com.aprouxdev.arcencielplanning

import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureHTTP()
    configureMonitoring()
    configureSecurity()
    configureSerialization()
    configureSockets()
    configureRouting()
}
