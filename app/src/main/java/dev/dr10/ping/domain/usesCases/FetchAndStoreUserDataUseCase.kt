package dev.dr10.ping.domain.usesCases

import android.util.Log
import dev.dr10.ping.domain.repositories.AuthRepository
import dev.dr10.ping.domain.repositories.StorageRepository
import dev.dr10.ping.domain.repositories.UsersRepository
import dev.dr10.ping.domain.utils.ErrorType
import dev.dr10.ping.domain.utils.Result
import io.github.jan.supabase.auth.user.UserSession

class FetchAndStoreUserDataUseCase(
    private val authRepository: AuthRepository,
    private val usersRepository: UsersRepository,
    private val storageRepository: StorageRepository,
) {

    suspend operator fun invoke(): Result<Boolean, ErrorType> {
        return try {
            // Retrieves the current user session
            val currentSession: UserSession? = authRepository.getCurrentSession()
            // Checks if the user is authenticated; if the session is null, returns an error
            if (currentSession == null) return Result.Error(ErrorType.USER_NOT_AUTHENTICATED)

            // Fetches user data from the repository
            val userData = usersRepository.fetchUserData(currentSession.user!!.id)
            // If user data is null, returns an error indicating that user data was not found
            if (userData == null) return Result.Error(ErrorType.USER_DATA_NOT_FOUND)

            // Downloads and saves the profile image, then updates the user data with the local path
            val profileImagePath = storageRepository.downloadAndSaveProfileImage(userData.profileImageName)
            authRepository.localSaveProfileData(userData.copy(profileImagePath = profileImagePath))

            Result.Success(true)
        } catch (e: Exception) {
            Log.e(this::class.java.simpleName, e.message.toString())
            return Result.Error(ErrorType.UNKNOWN_ERROR)
        }
    }

}