package dev.dr10.ping.ui.di

import dev.dr10.ping.ui.viewmodels.ChatViewModel
import dev.dr10.ping.ui.viewmodels.HomeViewModel
import dev.dr10.ping.ui.viewmodels.MainViewModel
import dev.dr10.ping.ui.viewmodels.NetworkViewModel
import dev.dr10.ping.ui.viewmodels.ProfileSetupViewModel
import dev.dr10.ping.ui.viewmodels.SignInViewModel
import dev.dr10.ping.ui.viewmodels.SignUpViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { SignUpViewModel(get(), get(), get()) }
    viewModel { SignInViewModel(get(), get(), get()) }
    viewModel { ProfileSetupViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { NetworkViewModel(get(), get()) }
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { ChatViewModel(get(), get(), get()) }
}