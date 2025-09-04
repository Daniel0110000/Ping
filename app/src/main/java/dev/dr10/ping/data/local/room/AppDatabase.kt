package dev.dr10.ping.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MessageEntity::class, RecentConversationEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun messagesDao(): MessagesDao
    abstract fun recentConversationsDao(): RecentConversationsDao
}