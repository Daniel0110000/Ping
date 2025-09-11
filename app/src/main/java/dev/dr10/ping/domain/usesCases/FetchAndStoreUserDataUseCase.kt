package dev.dr10.ping.domain.usesCases

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import dev.dr10.ping.domain.repositories.AuthRepository
import dev.dr10.ping.domain.repositories.StorageRepository
import dev.dr10.ping.domain.repositories.UsersRepository
import dev.dr10.ping.domain.utils.ErrorType
import dev.dr10.ping.domain.utils.Result
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.tasks.await

class FetchAndStoreUserDataUseCase(
    private val authRepository: AuthRepository,
    private val usersRepository: UsersRepository,
    private val storageRepository: StorageRepository
) {

    suspend operator fun invoke(): Result<Boolean, ErrorType> {
        return try {
            // Retrieves the current user session
            val currentSession: UserSession? = authRepository.getCurrentSession()
            // Checks if the user is authenticated; if the session is null, returns an error
            if (currentSession == null) return Result.Error(ErrorType.USER_NOT_AUTHENTICATED)

            val currentUserId = currentSession.user!!.id

            // Fetches user data from the repository
            val userData = usersRepository.fetchUserData(currentUserId)
            // If user data is null, returns an error indicating that user data was not found
            if (userData == null) return Result.Error(ErrorType.USER_DATA_NOT_FOUND)

            // Retrive the current FCM token and updates it in the backend
            val fcmToken = FirebaseMessaging.getInstance().token.await()
            usersRepository.updateFcmToken(fcmToken, currentUserId)

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