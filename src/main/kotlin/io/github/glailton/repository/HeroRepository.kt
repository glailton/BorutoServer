package io.github.glailton.repository

import io.github.glailton.model.ApiResponse
import io.github.glailton.model.Hero

interface HeroRepository {
    val heroes: Map<Int, List<Hero>>
    val page1: List<Hero>
    val page2: List<Hero>
    val page3: List<Hero>
    val page4: List<Hero>
    val page5: List<Hero>

    suspend fun gelAllHeroes(page: Int = 1): ApiResponse
    suspend fun searchHeroes(query: String?): ApiResponse
}