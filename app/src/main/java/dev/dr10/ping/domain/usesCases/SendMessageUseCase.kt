package dev.dr10.ping.domain.usesCases

import android.util.Log
import dev.dr10.ping.data.models.MessageData
import dev.dr10.ping.domain.repositories.AuthRepository
import dev.dr10.ping.domain.repositories.MessagesRepository
import dev.dr10.ping.domain.utils.ErrorType
import dev.dr10.ping.domain.utils.Result

class SendMessageUseCase(
    private val repository: MessagesRepository,
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(receiverId: String, content: String): Result<Boolean, ErrorType> {
        return try {
            // Check if the content and receiver ID are not empty
            if (content.isBlank() || receiverId.isBlank()) return Result.Error(ErrorType.UNKNOWN_ERROR)

            // Get the sender ID from the auth repository
            val senderId = authRepository.getProfileData()!!.userId
            // Send the message
            repository.sendMessage(MessageData(sender_id = senderId, receiver_id = receiverId, content = content))
            Result.Success(true)
        } catch (e: Exception) {
            Log.d(this.javaClass.simpleName, e.message.toString())
            Result.Error(ErrorType.UNKNOWN_ERROR)
        }
    }

}