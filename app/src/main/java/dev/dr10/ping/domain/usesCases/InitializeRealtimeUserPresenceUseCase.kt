package dev.dr10.ping.domain.usesCases

import android.util.Log
import dev.dr10.ping.domain.mappers.toModel
import dev.dr10.ping.domain.models.UserPresenceModel
import dev.dr10.ping.domain.repositories.PresencesRepository
import dev.dr10.ping.domain.utils.ErrorType
import dev.dr10.ping.domain.utils.Result
import kotlinx.coroutines.flow.map

class InitializeRealtimeUserPresenceUseCase(
    private val repository: PresencesRepository
) {

    suspend operator fun invoke(userId: String): Result<UserPresenceModel, ErrorType> = try {
        // Subscribe to the user presence channel and listen for updates
        val presence = repository.subscribeAndListenUserPresence(userId)
        Result.Success(
            UserPresenceModel(
                newStatus = presence.first.map { it.toModel() },
                channel = presence.second
            )
        )
    } catch (e: Exception) {
        Log.e(this::class.java.simpleName, e.message.toString())
        Result.Error(ErrorType.UNKNOWN_ERROR)
    }

}