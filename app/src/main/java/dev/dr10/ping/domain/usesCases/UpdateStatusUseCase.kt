package dev.dr10.ping.domain.usesCases

import android.util.Log
import dev.dr10.ping.domain.repositories.AuthRepository
import dev.dr10.ping.domain.repositories.PresencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateStatusUseCase(
    private val presencesRepository: PresencesRepository,
    private val authRepository: AuthRepository
) {

    operator fun invoke(isOnline: Boolean) = CoroutineScope(Dispatchers.IO).launch {
        try {
            // Retrieve the current user ID
            val currentUserId = authRepository.getProfileData()!!.userId
            // Update the user status
            presencesRepository.updateUserStatus(currentUserId, isOnline)
        } catch (e: Exception) { Log.e(this.javaClass.simpleName, e.message.toString()) }
    }

}