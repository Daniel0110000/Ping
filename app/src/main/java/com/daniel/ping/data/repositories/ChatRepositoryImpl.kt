package com.daniel.ping.data.repositories

import android.net.Uri
import com.daniel.ping.data.models.PushNotification
import com.daniel.ping.data.remote.firebaseService.addConversation
import com.daniel.ping.data.remote.firebaseService.checkForConversationRemotely
import com.daniel.ping.data.remote.firebaseService.listenerAvailabilityOfReceiver
import com.daniel.ping.data.remote.firebaseService.listenerMessage
import com.daniel.ping.data.remote.firebaseService.listenerRecentConversations
import com.daniel.ping.data.remote.firebaseService.sedMessage
import com.daniel.ping.data.remote.firebaseService.sendMessageWithFileOrMP3
import com.daniel.ping.data.remote.firebaseService.sendMessageWithImage
import com.daniel.ping.data.remote.firebaseService.updateConversation
import com.daniel.ping.data.remote.networkService.ApiService
import com.daniel.ping.domain.models.Chat
import com.daniel.ping.domain.models.RecentConversation
import com.daniel.ping.domain.repositories.ChatRepository
import com.daniel.ping.domain.utilities.CallHandler
import com.daniel.ping.domain.utilities.Constants
import com.daniel.ping.domain.utilities.Resource
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val apiService: ApiService,
    private val store: FirebaseStorage
) : ChatRepository {

    /**
     * Sends a message to the FireStore database
     * @param message A HashMap containing the message to be sent
     * @param messageImage The Uri of the image to be sent with the message
     * @param messageFile The uri of the file to be sent with the message
     * @param fileDetails The hashMap containing details of the file
     * @param messageMP3 The uri of the MP3 file to be sent with the message
     * @param mp3Details The hashMap containing details of the MP3 file.
     * @return Unit
     */
    override suspend fun sendMessage(
        message: HashMap<String, Any>,
        messageImage: Uri?,
        messageFile: Uri?,
        fileDetails: HashMap<String, String>,
        messageMP3: Uri?,
        mp3Details: HashMap<String, String>
    ) {
        message[Constants.KEY_TYPE_MESSAGE] = if(messageImage != null) Constants.MESSAGE_TYPE_IMAGE
                                              else if (messageFile != null) Constants.MESSAGE_TYPE_FILE
                                              else if (messageMP3 != null) Constants.MESSAGE_TYPE_MP3
                                              else Constants.MESSAGE_TYPE_TEXT
        message[Constants.KEY_IMAGE_URL] = messageImage?.let { store.sendMessageWithImage(it) } ?: ""
        messageFile?.let {
            fileDetails[Constants.KEY_FILE_URL] = store.sendMessageWithFileOrMP3(fileDetails[Constants.KEY_FILE_NAME].toString(), it)
        }
        messageMP3?.let {
            mp3Details[Constants.KEY_MP3_URL] = store.sendMessageWithFileOrMP3(mp3Details[Constants.KEY_MP3_NAME].toString(), it, true)
        }
        message[Constants.KEY_FILE_DETAILS] = fileDetails
        message[Constants.KEY_MP3_DETAILS] = mp3Details
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

    /**
     * A suspend function that checks for the existence of a conversation between a sender and a receiver
     * @param senderId String value representing the sender's ID
     * @param receiverId String value representing the receiver's ID
     * @return a Resource object containing a Task object that can be used to get the result of the query operation
     */
    override suspend fun checkForConversation(senderId: String, receiverId: String): Resource<Task<QuerySnapshot>> =
        CallHandler.callHandler { fireStore.checkForConversationRemotely(senderId, receiverId) }

    /**
     * Function to add a new conversation to the FireStore database using the addConversation function of the FireStore class
     * @param conversation HashMap object that contains the conversation data to be added
     * @return Resource object containing the result of the operation
     */
    override suspend fun addConversations(conversation: HashMap<String, Any>): Resource<String> =
        CallHandler.callHandler { fireStore.addConversation(conversation) }

    /**
     * Update a conversation's timestamp with the current data and time
     * @param documentId String containing the ID of the document to be updated
     */
    override suspend fun updateConversation(documentId: String) {
        fireStore.updateConversation(documentId)
    }
}