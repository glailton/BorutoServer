package io.github.glailton.repository

import io.github.glailton.model.ApiResponse
import io.github.glailton.model.Hero

interface HeroRepositoryAlternative {
    val heroes: List<Hero>

    suspend fun getAllHeroes(page: Int = 1, limit: Int = 4): ApiResponse
    suspend fun searchHeroes(query: String?): ApiResponse
}