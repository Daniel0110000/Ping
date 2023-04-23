package com.daniel.ping.data.repositories

import com.daniel.ping.data.remote.listenerMessage
import com.daniel.ping.data.remote.sedMessage
import com.daniel.ping.domain.models.Chat
import com.daniel.ping.domain.repositories.ChatRepository
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore
) : ChatRepository {

    /**
     * Sends a message to the FireStore database
     * @param message a HashMap containing the message to be sent
     * @return Unit
     */
    override suspend fun sendMessage(message: HashMap<String, Any>) {
        fireStore.sedMessage(message)
    }

    /**
     * Sets up a listener for incoming messages from the FireStore database
     * @param userId the IF of the user sending the message
     * @param receiverUserId the ID of the receiving the message
     * @param callback a function to be called when new messages arrive
     * @return Unit
     */
    override fun listenerMessages(
        userId: String,
        receiverUserId: String,
        callback: (ArrayList<Chat>) -> Unit
    ) =
        fireStore.listenerMessage(userId, receiverUserId, callback)

}