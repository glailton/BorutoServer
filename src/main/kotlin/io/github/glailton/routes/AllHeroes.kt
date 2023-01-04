package io.github.glailton.routes

import io.github.glailton.model.ApiResponse
import io.github.glailton.repository.HeroRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.lang.IllegalArgumentException
import java.lang.NumberFormatException

fun Route.getAllHeroes(){
    val repository: HeroRepository by inject()

    get("/boruto/heroes") {
        try {
            val page = call.request.queryParameters["page"]?.toInt() ?: 1
            require(page in 1..5)

            val apiResponse = repository.gelAllHeroes(page)

            call.respond(
                message = apiResponse,
                status = HttpStatusCode.OK
            )
        } catch (e: NumberFormatException){
            call.respond(
                message = ApiResponse(success = false, message = "Only numbers allowed."),
                status = HttpStatusCode.BadRequest
            )
        } catch (e: IllegalArgumentException){
            call.respond(
                message = ApiResponse(success = false, message = "Heroes not found."),
                status = HttpStatusCode.NotFound
            )
        }
    }
}