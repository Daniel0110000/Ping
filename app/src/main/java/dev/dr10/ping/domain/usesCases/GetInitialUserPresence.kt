package dev.dr10.ping.domain.usesCases

import android.util.Log
import dev.dr10.ping.domain.mappers.toModel
import dev.dr10.ping.domain.models.PresenceModel
import dev.dr10.ping.domain.repositories.PresencesRepository
import dev.dr10.ping.domain.utils.ErrorType
import dev.dr10.ping.domain.utils.Result

class GetInitialUserPresence(
    private val userPresencesRepository: PresencesRepository
) {

    suspend operator fun invoke(receiverId: String): Result<PresenceModel, ErrorType> {
        return try {
            // Fetch the initial presence data
            val presence = userPresencesRepository.fetchInitialUserPresence(receiverId)
            // If the presence data is not found, return an error
            if (presence == null) return Result.Error(ErrorType.UNKNOWN_ERROR)
            // Return the presence data as a success result
            Result.Success(presence.toModel())
        } catch (e: Exception) {
            Log.e(this.javaClass.simpleName, e.message.toString())
            Result.Error(ErrorType.UNKNOWN_ERROR)
        }
    }

}