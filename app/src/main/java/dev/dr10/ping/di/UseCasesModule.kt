package dev.dr10.ping.di

import dev.dr10.ping.domain.usesCases.SignUpWithEmailAndPasswordUseCase
import org.koin.dsl.module

val useCasesModule = module {
    single { SignUpWithEmailAndPasswordUseCase(get()) }
}