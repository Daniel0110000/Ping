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

class SyncInitialMessagesAndConversationsUseCase(
    private val messagesRepository: MessagesRepository,
    private val authRepository: AuthRepository,
    private val conversationsRepository: ConversationsRepository,
    private val usersRepository: UsersRepository
) {

    suspend operator fun invoke(): Result<Boolean, ErrorType> = try {
        // Get the current user id
        val currentUserId = authRepository.getCurrentUserId()!!

        // Fetch all messages and recent conversations for the current user
        val messages = messagesRepository.fetchAllMessages(currentUserId)
        val allRecentConversations = conversationsRepository.fetchAllRecentConversations(currentUserId)

        // Map the recent conversations to [RecentConversationEntity]
        val recentConversations = allRecentConversations.map {
            val userId = MessageUtils.extractUserId(currentUserId, it.sender_id, it.receiver_id)
            val userData = usersRepository.fetchSimpleUserData(userId)
            it.toRecentConversationEntity(userId, userData)
        }

        // Save all messages and recent conversations to the local database
        messagesRepository.saveAllMessages(messages)
        conversationsRepository.saveNewsRecentConversations(recentConversations)

        // Mark that the user's messages and conversations have been synced
        authRepository.messagesAndConversationsSynced()

        Result.Success(true)
    } catch (e: Exception) {
        Log.d(this.javaClass.simpleName, e.message.toString())
        Result.Error(ErrorType.SYNC_MESSAGES_AND_CONVERSATIONS)
    }
}