package io.github.glailton

import io.github.glailton.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    configureKoin()
    configureSerialization()
    configureMonitoring()
    configureRouting()
    configureDefaultHeader()
    configureStatusPages()
}
