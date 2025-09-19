package dev.dr10.ping.domain.usesCases

import android.util.Log
import dev.dr10.ping.domain.repositories.AuthRepository
import dev.dr10.ping.domain.repositories.PresencesRepository

class UpdateLastConnectedUseCase(
    private val presencesRepository: PresencesRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() = try {
        // Retrieve the current user ID
        val currentUserId = authRepository.getCurrentUserId()!!
        // Update the last connected time
        presencesRepository.updateLastConnected(currentUserId)
    } catch (e: Exception) { Log.d(this.javaClass.simpleName, e.message.toString()) }
}