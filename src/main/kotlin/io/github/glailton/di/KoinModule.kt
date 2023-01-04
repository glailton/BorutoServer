package io.github.glailton.di

import io.github.glailton.repository.HeroRepository
import io.github.glailton.repository.HeroRepositoryImpl
import org.koin.dsl.module

val koinModule = module {
    single<HeroRepository> {
        HeroRepositoryImpl()
    }
}