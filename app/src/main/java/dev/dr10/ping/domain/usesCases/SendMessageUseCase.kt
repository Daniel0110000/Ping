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
    private val conversationsRepository: ConversationsRepository,
    private val sendNotificationUseCase: SendNotificationUseCase
) {

    suspend operator fun invoke(receiverUserData: UserProfileModel?, content: String, isOnline: Boolean): Result<Boolean, ErrorType> {
        return try {
            // Check if the content and receiver ID are not empty
            if (content.isBlank() || receiverUserData == null) return Result.Error(ErrorType.UNKNOWN_ERROR)

            // Fetch the current user profile data
            val currentProfile = authRepository.getProfileData()!!

            // Generate a unique conversation ID based on the sender and receiver IDs
            val conversationId = MessageUtils.generateConversationId(currentProfile.userId, receiverUserData.userId)

            // Send the message
            repository.sendMessage(MessageData(
                conversation_id = conversationId,
                sender_id = currentProfile.userId,
                receiver_id = receiverUserData.userId,
                content = content
            ))

            // Upsert the recent conversation associated with the current user
            conversationsRepository.upsertRecentConversation(
                RecentConversationData(
                    conversation_id = conversationId,
                    sender_id = currentProfile.userId,
                    receiver_id = receiverUserData.userId
                )
            )

            // Send a notification if the receiver is offline
            if (!isOnline) {
                sendNotificationUseCase(
                    userProfileData = currentProfile,
                    receiverUserId = receiverUserData.userId,
                    content = content
                )
            }

            Result.Success(true)
        } catch (e: Exception) {
            Log.e(this.javaClass.simpleName, e.message.toString())
            Result.Error(ErrorType.UNKNOWN_ERROR)
        }
    }

}