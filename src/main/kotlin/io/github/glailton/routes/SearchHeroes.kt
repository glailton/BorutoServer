package io.github.glailton.routes

import io.github.glailton.repository.HeroRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.searchHeroes(){
    val repository: HeroRepository by inject()

    get("/boruto/heroes/search") {
        val name = call.request.queryParameters["name"]

        val apiResponse = repository.searchHeroes(name)
        call.respond(
            message = apiResponse,
            status = HttpStatusCode.OK
        )
    }
}