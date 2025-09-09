package dev.dr10.ping.domain.usesCases

import android.util.Log
import dev.dr10.ping.domain.mappers.toModel
import dev.dr10.ping.domain.models.UserStatusModel
import dev.dr10.ping.domain.repositories.UsersRepository
import dev.dr10.ping.domain.utils.ErrorType
import dev.dr10.ping.domain.utils.Result

class GetUserPresenceUseCase(
    private val usersRepository: UsersRepository
) {

    suspend operator fun invoke(receiverId: String): Result<UserStatusModel, ErrorType> = try {
        val statusResult = usersRepository.getUserPresence(receiverId).toModel()
        Result.Success(statusResult)
    } catch (e: Exception) {
        Log.e(this.javaClass.simpleName, e.message.toString())
        Result.Error(ErrorType.UNKNOWN_ERROR)
    }

}