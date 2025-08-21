package dev.dr10.ping.domain.usesCases

import android.util.Log
import dev.dr10.ping.BuildConfig
import dev.dr10.ping.domain.mappers.toModel
import dev.dr10.ping.domain.models.UserProfileModel
import dev.dr10.ping.domain.repositories.AuthRepository
import dev.dr10.ping.domain.repositories.UsersRepository
import dev.dr10.ping.domain.utils.Constants
import dev.dr10.ping.domain.utils.ErrorType
import dev.dr10.ping.domain.utils.Result

class GetSuggestedUsersUseCase(
    private val authRepository: AuthRepository,
    private val usersRepository: UsersRepository
) {

    suspend operator fun invoke(): Result<List<UserProfileModel>, ErrorType> = try {
        // Get the current user ID from the session
        val currentUserId: String = authRepository.getCurrentSession()!!.user!!.id
        // Fetch the list of suggested users for the current user
        val suggestedUsers = usersRepository.fetchSuggestedUsers(currentUserId).map { fetch ->
            fetch.toModel().copy(profileImageUrl = "${BuildConfig.SUPABASE_URL}${Constants.PROFILE_IMAGES_BUCKET_PATH}${fetch.profileImageName}")
        }
        // Return the list of suggested users
        Result.Success(suggestedUsers)
    } catch (e: Exception) {
        Log.d(this::class.java.simpleName, e.message.toString())
        Result.Error(ErrorType.UNKNOWN_ERROR)
    }

}