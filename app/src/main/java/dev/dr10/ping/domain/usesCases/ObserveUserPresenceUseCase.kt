package dev.dr10.ping.domain.usesCases

import android.util.Log
import dev.dr10.ping.domain.repositories.PresenceRepository
import dev.dr10.ping.domain.utils.ErrorType
import dev.dr10.ping.domain.utils.Result
import kotlinx.coroutines.flow.Flow

class ObserveUserPresenceUseCase(
    private val repository: PresenceRepository
) {
    suspend operator fun invoke(userId: String): Result<Flow<Boolean>, ErrorType> = try {
        Result.Success(repository.observeUserPresence(userId))
    } catch (e: Exception) {
        Log.e(this.javaClass.simpleName, e.message.toString())
        Result.Error(ErrorType.UNKNOWN_ERROR)
    }

}