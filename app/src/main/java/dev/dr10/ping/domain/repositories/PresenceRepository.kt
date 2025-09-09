package dev.dr10.ping.domain.repositories

import kotlinx.coroutines.flow.Flow

interface PresenceRepository {

    suspend fun subscribeToChannelAndSendTrack()

    suspend fun observeUserPresence(userId: String): Flow<Boolean>
}