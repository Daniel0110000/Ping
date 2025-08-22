package dev.dr10.ping.domain.usesCases

import android.util.Log
import dev.dr10.ping.domain.extensions.toProfileImageUrl
import dev.dr10.ping.domain.mappers.toModel
import dev.dr10.ping.domain.models.UserProfileModel
import dev.dr10.ping.domain.repositories.AuthRepository
import dev.dr10.ping.domain.repositories.UsersRepository
import dev.dr10.ping.domain.utils.ErrorType
import dev.dr10.ping.domain.utils.Result

class SearchUserUseCase(
    private val usersRepository: UsersRepository,
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(query: String): Result<List<UserProfileModel>, ErrorType> = try {
        // Get the current user ID from the session
        val currentUserId: String = authRepository.getCurrentSession()!!.user!!.id

        // Fetch users by username
        val users = usersRepository.fetchUsersByUsername(id = currentUserId, query = query).map { user ->
            user.toModel().copy(profileImageUrl = user.profileImageName.toProfileImageUrl())
        }

        Result.Success(users)
    } catch (e: Exception) {
        Log.e(this::class.java.simpleName, e.message.toString())
        Result.Error(ErrorType.UNKNOWN_ERROR)
    }

}