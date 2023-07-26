package com.daniel.ping.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentConversationsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecentConversation(recentConversations: RecentConversations)

    @Query("select * from recentConversationTable")
    fun findAllRecentConversations(): Flow<List<RecentConversations>>

    @Query("select exists (select 1 from recentConversationTable where receiverId= :id limit 1)")
    fun existsByReceiverIdAndObjectData(id: String): Boolean

    @Query("update recentConversationTable set dateObject = :dateObject where receiverId = :receiverId")
    fun updateDataObject(receiverId: String, dateObject: String)

}