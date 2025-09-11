package dev.dr10.ping.domain.usesCases

import dev.dr10.ping.domain.repositories.AuthRepository
import dev.dr10.ping.domain.repositories.UsersRepository

class UpdateFCMTokenUseCase(
    private val usersRepository: UsersRepository,
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(fcmToken: String) {
        // Get the current user's profile data and if it exists, update the FCM token
        authRepository.getProfileData()?.let {
            usersRepository.updateFcmToken(fcmToken, it.userId)
        }
    }

}