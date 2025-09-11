package dev.dr10.ping.domain.usesCases

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import dev.dr10.ping.data.models.UserProfileData
import dev.dr10.ping.domain.exceptions.ImageProcessingException
import dev.dr10.ping.domain.repositories.AuthRepository
import dev.dr10.ping.domain.repositories.StorageRepository
import dev.dr10.ping.domain.repositories.UsersRepository
import dev.dr10.ping.domain.utils.AuthDataValidator
import dev.dr10.ping.domain.utils.ErrorType
import dev.dr10.ping.domain.utils.ImageUtils
import dev.dr10.ping.domain.utils.Result
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.tasks.await

class ProfileSetupUseCase(
    private val context: Context,
    private val usersRepository: UsersRepository,
    private val authRepository: AuthRepository,
    private val storageRepository: StorageRepository
) {
    suspend operator fun invoke(
        profileImageUri: Uri?,
        username: String,
        bio: String
    ): Result<Boolean, ErrorType> {
        // Checks if the profile setup data is valid
        AuthDataValidator.isValidProfileSetupData(
            profileImageUri = profileImageUri,
            username = username,
            bio = bio
        ).takeIf { it is Result.Error }?.let { return it }

        return try {
            // Retrieves the current user session
            val currentSession: UserSession? = authRepository.getCurrentSession()
            // Checks if the user is authenticated if the session is null returns an error
            if (currentSession == null) return Result.Error(ErrorType.USER_NOT_AUTHENTICATED)

            // Converts the profile image URI to a byte array
            val profileImageByte: ByteArray = ImageUtils.uriToByteArray(context, profileImageUri!!)
            // Creates a unique image name using the user ID and current timestamp
            val imageName = "${currentSession.user!!.id}_${System.currentTimeMillis()}.png"

            // Uploads the profile image and saves it to the local storage
            val localImagePath = storageRepository.uploadAndSaveProfileImage(profileImageByte, imageName)

            // Retrieves the current FCM token
            val currentFCMToken = FirebaseMessaging.getInstance().token.await()

            // Saves the user profile data on Supabase and in local storage
            val userData = UserProfileData(
                userId = currentSession.user!!.id,
                username = username,
                bio = bio,
                profileImageName = imageName,
                profileImagePath = localImagePath,
                fcmToken = currentFCMToken
            )
            authRepository.localSaveProfileData(userData)
            usersRepository.saveUserData(userData)

            Result.Success(true)
        } catch (e: ImageProcessingException) {
            Log.e(this::class.java.simpleName, e.message.toString())
            Result.Error(e.errorType)
        }
        catch (e: Exception) {
            Log.e(this::class.java.simpleName, e.message.toString())
            Result.Error(ErrorType.UNKNOWN_ERROR)
        }
    }
}