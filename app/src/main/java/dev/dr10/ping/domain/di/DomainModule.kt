package dev.dr10.ping.domain.di

import dev.dr10.ping.domain.usesCases.AuthWithGoogleUseCase
import dev.dr10.ping.domain.usesCases.ProfileSetupUseCase
import dev.dr10.ping.domain.usesCases.SignUpWithEmailAndPasswordUseCase
import org.koin.dsl.module

val domainModule = module {
    single { SignUpWithEmailAndPasswordUseCase(get()) }
    single { AuthWithGoogleUseCase(get()) }
    single { ProfileSetupUseCase(get(), get(), get(), get()) }
}