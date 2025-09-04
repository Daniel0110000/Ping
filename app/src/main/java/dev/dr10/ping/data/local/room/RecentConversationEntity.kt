package dev.dr10.ping.data.local.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_conversations")
data class RecentConversationEntity(
    @PrimaryKey @ColumnInfo("conversation_id") val conversationId: String,
    @ColumnInfo("user_id") val userId: String,
    @ColumnInfo("username") val username: String,
    @ColumnInfo("profile_picture_url") val profilePictureUrl: String,
    @ColumnInfo(name = "updated_at") val updatedAt: String
)
