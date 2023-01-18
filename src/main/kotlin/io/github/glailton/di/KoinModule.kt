package io.github.glailton.di

import io.github.glailton.repository.HeroRepository
import io.github.glailton.repository.HeroRepositoryAlternative
import io.github.glailton.repository.HeroRepositoryImpl
import io.github.glailton.repository.HeroRepositoryImplAlternative
import org.koin.dsl.module

val koinModule = module {
    single<HeroRepository> {
        HeroRepositoryImpl()
    }
    single<HeroRepositoryAlternative> {
        HeroRepositoryImplAlternative()
    }
}