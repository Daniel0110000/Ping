package dev.dr10.ping.domain.usesCases

import android.util.Log
import dev.dr10.ping.data.models.MessageData
import dev.dr10.ping.data.models.RecentConversationData
import dev.dr10.ping.domain.models.UserProfileModel
import dev.dr10.ping.domain.repositories.AuthRepository
import dev.dr10.ping.domain.repositories.ConversationsRepository
import dev.dr10.ping.domain.repositories.MessagesRepository
import dev.dr10.ping.domain.utils.ErrorType
import dev.dr10.ping.domain.utils.MessageUtils
import dev.dr10.ping.domain.utils.Result

class SendMessageUseCase(
    private val repository: MessagesRepository,
    private val authRepository: AuthRepository,
    private val conversationsRepository: ConversationsRepository
) {

    suspend operator fun invoke(receiverUserData: UserProfileModel?, content: String): Result<Boolean, ErrorType> {
        return try {
            // Check if the content and receiver ID are not empty
            if (content.isBlank() || receiverUserData == null) return Result.Error(ErrorType.UNKNOWN_ERROR)

            // Get the sender ID from the auth repository
            val senderId = authRepository.getProfileData()!!.userId
            val conversationId = MessageUtils.generateConversationId(senderId, receiverUserData.userId)
            // Send the message
            repository.sendMessage(MessageData(
                conversation_id = conversationId,
                sender_id = senderId,
                receiver_id = receiverUserData.userId,
                content = content
            ))

            // Upsert the recent conversation associated with the current user
            conversationsRepository.upsertRecentConversation(
                RecentConversationData(
                    conversation_id = conversationId,
                    sender_id = senderId,
                    receiver_id = receiverUserData.userId
                )
            )

            Result.Success(true)
        } catch (e: Exception) {
            Log.d(this.javaClass.simpleName, e.message.toString())
            Result.Error(ErrorType.UNKNOWN_ERROR)
        }
    }

}