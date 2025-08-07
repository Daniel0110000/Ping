package dev.dr10.ping.di

import dev.dr10.ping.data.repositories.AuthRepositoryImpl
import dev.dr10.ping.domain.repositories.AuthRepository
import org.koin.dsl.module

val repositoriesModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
}