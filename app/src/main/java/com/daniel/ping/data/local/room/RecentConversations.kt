package com.daniel.ping.data.local.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "recentConversationTable")
data class RecentConversations(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "recentConversationId") val id: Int = 0,
    val senderId: String,
    val receiverId: String,
    val receiverProfileImageUrl: String,
    val receiverName: String,
    val receiverDescription: String,
    val receiverToken: String,
    val dateObject: String
)
