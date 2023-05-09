package com.daniel.ping.domain.repositories

import android.net.Uri
import com.daniel.ping.data.models.PushNotification
import com.daniel.ping.domain.models.Chat
import com.daniel.ping.domain.models.RecentConversation
import com.daniel.ping.domain.utilities.Resource
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

interface ChatRepository {

    suspend fun sendMessage(message: HashMap<String, Any>, messageImage: Uri? = null)

    fun listenerMessages(userId: String, receiverUserId: String, callback: (ArrayList<Chat>) -> Unit)

    fun listenerAvailabilityOfReceiver(receiverUserId: String, listener: (Int) -> Unit)

    suspend fun sendNotification(notification: PushNotification)

    suspend fun checkForConversation(senderId: String, receiverId: String): Resource<Task<QuerySnapshot>>

    suspend fun addConversations(conversation: HashMap<String, Any>): Resource<String>

    suspend fun updateConversation(documentId: String)

    fun listerRecentConversations(senderId: String, listener: (ArrayList<RecentConversation>) -> Unit)



}