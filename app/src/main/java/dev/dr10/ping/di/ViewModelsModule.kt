package dev.dr10.ping.di

import dev.dr10.ping.ui.viewmodels.SignUpViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { SignUpViewModel(get(), get()) }
}