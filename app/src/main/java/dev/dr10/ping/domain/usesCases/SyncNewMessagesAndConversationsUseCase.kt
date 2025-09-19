package dev.dr10.ping.domain.usesCases

import android.util.Log
import dev.dr10.ping.domain.mappers.toRecentConversationEntity
import dev.dr10.ping.domain.repositories.AuthRepository
import dev.dr10.ping.domain.repositories.ConversationsRepository
import dev.dr10.ping.domain.repositories.MessagesRepository
import dev.dr10.ping.domain.repositories.UsersRepository
import dev.dr10.ping.domain.utils.ErrorType
import dev.dr10.ping.domain.utils.MessageUtils
import dev.dr10.ping.domain.utils.Result

class SyncNewMessagesAndConversationsUseCase(
    private val messagesRepository: MessagesRepository,
    private val conversationsRepository: ConversationsRepository,
    private val authRepository: AuthRepository,
    private val usersRepository: UsersRepository
) {

    suspend operator fun invoke(): Result<Boolean, ErrorType> {
        return try {
            // Get the current user id
            val currentUserId = authRepository.getCurrentUserId()!!

            // Get the last message id at saved in local database and if it's null return false success
            val lastMessageId = messagesRepository.getLastMessageId()
            if (lastMessageId == null) return Result.Success(false)

            // Get the last conversation updated at saved in local database if it's null return false success
            val lastConversationUpdatedAt = conversationsRepository.getLastConversationUpdatedAt()
            if (lastConversationUpdatedAt == null) return Result.Success(false)

            // Fetch new messages and recent conversations for the current user
            val newMessages = messagesRepository.fetchNewMessages(currentUserId, lastMessageId)
            // Save all new messages to the local database
            messagesRepository.saveAllMessages(newMessages)

            // Fetch new recent conversations for the current user
            val conversations = conversationsRepository.fetchNewRecentConversations(lastConversationUpdatedAt, currentUserId)
            // Map the recent conversations to [RecentConversationEntity]
            val recentConversations = conversations.map {
                val userId = MessageUtils.extractUserId(currentUserId, it.sender_id, it.receiver_id)
                val userData = usersRepository.fetchSimpleUserData(userId)
                it.toRecentConversationEntity(userId, userData)
            }
            // Save all new recent conversations to the local database
            conversationsRepository.localUpsertRecentConversations(recentConversations)

            Result.Success(true)
        } catch (e: Exception) {
            Log.d(this.javaClass.simpleName, e.message.toString())
            Result.Error(ErrorType.SYNC_MESSAGES_AND_CONVERSATIONS)
        }
    }

}