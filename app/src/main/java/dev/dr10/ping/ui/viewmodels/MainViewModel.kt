package dev.dr10.ping.ui.viewmodels

import androidx.lifecycle.ViewModel
import dev.dr10.ping.domain.repositories.AuthRepository
import kotlinx.coroutines.runBlocking

class MainViewModel(
    private val authRepository: AuthRepository
): ViewModel() {

    fun isUserLoggedIn(): Boolean = runBlocking { authRepository.isUserLoggedIn() }

    fun isProfileSetupCompleted(): Boolean = runBlocking { authRepository.isProfileSetupCompleted() }

    fun isInitialSyncCompleted(): Boolean = runBlocking { authRepository.isMessagesAndConversationsSynced() }

}