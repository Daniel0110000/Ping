package dev.dr10.ping.domain.models

import io.github.jan.supabase.realtime.RealtimeChannel
import kotlinx.coroutines.flow.Flow

data class UserPresenceModel(
    val newStatus: Flow<PresenceModel>,
    val channel: RealtimeChannel
)
