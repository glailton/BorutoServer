package io.github.glailton.plugins

import io.github.glailton.routes.getAllHeroes
import io.github.glailton.routes.root
import io.github.glailton.routes.searchHeroes
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureRouting() {

    routing {
        root()
        getAllHeroes()
        searchHeroes()

        static("/images") {
            resources("images")
        }
    }
}
