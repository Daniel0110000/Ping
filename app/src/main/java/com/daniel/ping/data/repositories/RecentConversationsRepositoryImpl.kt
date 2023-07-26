package com.daniel.ping.data.repositories

import com.daniel.ping.data.local.room.RecentConversationsDao
import com.daniel.ping.data.mappers.RecentConversationsMapper.toRecentConversation
import com.daniel.ping.data.mappers.RecentConversationsMapper.toRecentConversations
import com.daniel.ping.data.remote.firebaseService.listenerRecentConversations
import com.daniel.ping.domain.models.RecentConversation
import com.daniel.ping.domain.repositories.RecentConversationsRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecentConversationsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val recentConversationsDao: RecentConversationsDao
): RecentConversationsRepository {

    /**
     * Retrieves a Flow of recent conversations for the given senderId
     * This function fetches recent conversations, saves them to Room database, and returns a Flow of RecentConversation objects
     * @param senderId The ID of the s ender whose recent conversations are to be listed
     * @return A Flow emitting a List of RecentConversation objects representing recent conversations of the sender
     */
    override suspend fun listerRecentConversations(senderId: String): Flow<List<RecentConversation>>{
        saveRecentMessagesToRoom(senderId)

        return recentConversationsDao.findAllRecentConversations().map { recentConversationsList ->
            recentConversationsList.map { it.toRecentConversation() }}
    }

    /**
     * Saves recent messages related to the given senderId to the local Room database
     * @param senderId The ID of the sender whose recent messages are to be saved
     */
    private fun saveRecentMessagesToRoom(senderId: String){
        firestore.listenerRecentConversations(senderId){ recentConversations ->
            recentConversations.forEach { recentConversation ->
                if(recentConversationsDao.existsByReceiverIdAndObjectData(recentConversation.receiverId))
                    recentConversationsDao.updateDataObject(recentConversation.receiverId, recentConversation.dateObject.toString())
                else recentConversationsDao.insertRecentConversation(recentConversation.toRecentConversations())
            }
        }
    }
}