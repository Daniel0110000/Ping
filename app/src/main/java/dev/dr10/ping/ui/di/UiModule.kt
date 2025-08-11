package dev.dr10.ping.ui.di

import dev.dr10.ping.ui.viewmodels.ProfileSetupViewModel
import dev.dr10.ping.ui.viewmodels.SignUpViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { SignUpViewModel(get(), get()) }
    viewModel { ProfileSetupViewModel(get()) }
}