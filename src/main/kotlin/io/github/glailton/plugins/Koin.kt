package io.github.glailton.plugins

import io.github.glailton.di.koinModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin(){
    install(Koin){
        slf4jLogger(level = org.koin.core.logger.Level.ERROR)
        modules(koinModule)
    }
}