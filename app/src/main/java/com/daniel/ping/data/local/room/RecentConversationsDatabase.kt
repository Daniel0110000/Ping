package com.daniel.ping.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [RecentConversations::class],
    version = 1,
    exportSchema = false
)
abstract class RecentConversationsDatabase: RoomDatabase() {
    /**
     * Abstract method to get the RecentConversationsDao, allowing access to the database operations
     * @return An instance of the RecentConversationsDao
     */
    abstract fun getRecentConversationsDao(): RecentConversationsDao
}