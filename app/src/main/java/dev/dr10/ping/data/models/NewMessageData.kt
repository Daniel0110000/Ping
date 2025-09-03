package dev.dr10.ping.data.models

import io.github.jan.supabase.realtime.RealtimeChannel
import kotlinx.coroutines.flow.Flow

data class NewMessageData(
    val newMessage: Flow<MessageData>,
    val channel: RealtimeChannel
)
