package dev.dr10.ping.domain.repositories

import dev.dr10.ping.data.models.PresenceData
import io.github.jan.supabase.realtime.RealtimeChannel
import kotlinx.coroutines.flow.Flow

interface PresencesRepository {

    suspend fun createInitialPresence(data: PresenceData)

    suspend fun updateLastConnected(userId: String)

    suspend fun updateUserStatus(userId: String, isOnline: Boolean)

    suspend fun fetchInitialUserPresence(userId: String): PresenceData?

    suspend fun subscribeAndListenUserPresence(userId: String): Pair<Flow<PresenceData>, RealtimeChannel>

}