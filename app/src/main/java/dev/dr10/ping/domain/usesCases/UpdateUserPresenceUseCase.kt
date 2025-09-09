package dev.dr10.ping.domain.usesCases

import android.util.Log
import dev.dr10.ping.domain.repositories.AuthRepository
import dev.dr10.ping.domain.repositories.UsersRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateUserPresenceUseCase(
    private val usersRepository: UsersRepository,
    private val authRepository: AuthRepository
) {

    operator fun invoke(isOnline: Boolean) = try {
        CoroutineScope(Dispatchers.IO).launch {
            val currentUserId = authRepository.getProfileData()!!.userId
            usersRepository.updateUserPresence(isOnline, currentUserId)
        }
    } catch (e: Exception) {
        Log.e(this.javaClass.simpleName, e.message.toString())
    }
}