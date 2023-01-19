package io.github.glailton

import io.github.glailton.model.ApiResponse
import io.ktor.http.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlin.test.*
import io.ktor.server.testing.*
import io.github.glailton.plugins.*
import io.github.glailton.repository.HeroRepositoryImpl
import io.github.glailton.repository.NEXT_PAGE_KEY
import io.github.glailton.repository.PREVIOUS_PAGE_KEY
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class ApplicationTest {

    @Test
    fun `access root endpoint, assert correct information`() =
        testApplication {
            application {
                configureRouting()
            }
            client.get("/").apply {
                assertEquals(HttpStatusCode.OK, status)
                assertEquals("Welcome to Boruto API!", bodyAsText())
            }
        }

    @Test
    fun `access all heroes endpoint, assert correct information`() =
        testApplication {
            application {
                configureRouting()
            }
            val heroRepository = HeroRepositoryImpl()
            val pages = 1..5
            val heroes = listOf(
                heroRepository.page1,
                heroRepository.page2,
                heroRepository.page3,
                heroRepository.page4,
                heroRepository.page5
            )
            pages.forEach { page ->
                val response = client.get("/boruto/heroes?page=$page").apply {
                    assertEquals(HttpStatusCode.OK, status)
                }

                val actual = Json.decodeFromString<ApiResponse>(response.bodyAsText())
                val expected = ApiResponse(
                    success = true,
                    message = "ok",
                    prevPage = calculatePage(page = page)["prevPage"],
                    nextPage = calculatePage(page = page)["nextPage"],
                    heroes = heroes[page - 1],
                    lastUpdated = actual.lastUpdated
                )
                assertEquals(
                    expected = expected,
                    actual = actual
                )
            }
        }

    @Test
    fun `access heroes endpoint when pass no page, assert correct information`() =
        testApplication {
            application {
                configureRouting()
            }
            val heroRepository = HeroRepositoryImpl()
            val heroes = listOf(
                heroRepository.page1
            )
            val response = client.get("/boruto/heroes").apply {
                assertEquals(HttpStatusCode.OK, status)
            }

            val actual = Json.decodeFromString<ApiResponse>(response.bodyAsText())
            val expected = ApiResponse(
                success = true,
                message = "ok",
                prevPage = calculatePage(page = 1)["prevPage"],
                nextPage = calculatePage(page = 1)["nextPage"],
                heroes = heroes[0],
                lastUpdated = actual.lastUpdated
            )
            assertEquals(
                expected = expected,
                actual = actual
            )
        }

    @Test
    fun `access all heroes endpoint, query non existing page number, assert error`() =
        testApplication {
            application {
                configureRouting()
            }

            val response = client.get("/boruto/heroes?page=7").apply {
                assertEquals(HttpStatusCode.NotFound, status)
            }
            assertEquals("Page not Found.", response.bodyAsText())
        }

    @Test
    fun `access all heroes endpoint, query invalid page number, assert error`() =
        testApplication {
            application {
                configureRouting()
            }

            val response = client.get("/boruto/heroes?page=invalid").apply {
                assertEquals(HttpStatusCode.BadRequest, status)
            }
            val actual = Json.decodeFromString<ApiResponse>(response.bodyAsText())
            val expected = ApiResponse(
                success = false,
                message = "Only Numbers Allowed."
            )

            assertEquals(
                expected = expected,
                actual = actual
            )
        }

    @Test
    fun `access search heroes endpoint, query hero name, assert single hero result`() =
        testApplication {
            val response = client.get("/boruto/heroes/search?name=sas").apply {
                assertEquals(HttpStatusCode.OK, status)
            }
            val actual = Json.decodeFromString<ApiResponse>(response.bodyAsText()).heroes.size
            assertEquals(expected = 1, actual = actual)
        }

    @Test
    fun `access search heroes endpoint, query hero name, assert multiple heroes result`() =
        testApplication {
            val response = client.get("/boruto/heroes/search?name=sa").apply {
                assertEquals(HttpStatusCode.OK, status)
            }
            val actual = Json.decodeFromString<ApiResponse>(response.bodyAsText()).heroes.size
            assertEquals(expected = 3, actual = actual)
        }

    @Test
    fun `access search heroes endpoint, query an empty text, assert empty list as a result`() =
        testApplication {
            val response = client.get("/boruto/heroes/search?name=").apply {
                assertEquals(HttpStatusCode.OK, status)
            }
            val actual = Json.decodeFromString<ApiResponse>(response.bodyAsText()).heroes.size
            assertEquals(expected = 0, actual = actual)
        }

    @Test
    fun `access search heroes endpoint, query an invalid text, assert empty list as a result`() =
        testApplication {
            val response = client.get("/boruto/heroes/search?name=test").apply {
                assertEquals(HttpStatusCode.OK, status)
            }
            val actual = Json.decodeFromString<ApiResponse>(response.bodyAsText()).heroes.size
            assertEquals(expected = 0, actual = actual)
        }

    @Test
    fun `access non existing endpoint,assert not found`() =
        testApplication {
            client.get("/invalid").apply {
                assertEquals(HttpStatusCode.NotFound, status)
                assertEquals("Page not Found.", bodyAsText())
            }
        }

    private fun calculatePage(page: Int): Map<String, Int?> {
        var prevPage: Int? = page
        var nextPage: Int? = page
        if (page in 1..4) {
            nextPage = nextPage?.plus(1)
        }
        if (page in 2..5) {
            prevPage = prevPage?.minus(1)
        }
        if (page == 1) {
            prevPage = null
        }
        if (page == 5) {
            nextPage = null
        }
        return mapOf(PREVIOUS_PAGE_KEY to prevPage, NEXT_PAGE_KEY to nextPage)
    }
}