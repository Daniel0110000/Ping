package com.daniel.ping.data.repositories

import com.daniel.ping.data.models.PushNotification
import com.daniel.ping.data.remote.listenerAvailabilityOfReceiver
import com.daniel.ping.data.remote.listenerMessage
import com.daniel.ping.data.remote.networkService.ApiService
import com.daniel.ping.data.remote.sedMessage
import com.daniel.ping.domain.models.Chat
import com.daniel.ping.domain.repositories.ChatRepository
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val apiService: ApiService
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
    override fun listenerMessages(userId: String, receiverUserId: String, callback: (ArrayList<Chat>) -> Unit) =
        fireStore.listenerMessage(userId, receiverUserId, callback)

    /**
     * Listen for the availability status of the specified user
     * @param receiverUserId the ID of the user to listen to
     * @param listener a lambda function that takes an integer representing the availability status
     */
    override fun listenerAvailabilityOfReceiver(receiverUserId: String, listener: (Int) -> Unit) =
        fireStore.listenerAvailabilityOfReceiver(receiverUserId, listener)

    /**
     * Function to send a push notification using the API service defined in the ApiService interface
     * @param notification Push Notification object that contains the information of the notification to be sent
     */
    override suspend fun sendNotification(notification: PushNotification) {
        try { apiService.sendNotification(notification) }
        catch (e: Exception){ e.printStackTrace() }
    }

}