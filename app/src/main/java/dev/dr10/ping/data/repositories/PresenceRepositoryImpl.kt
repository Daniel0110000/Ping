package dev.dr10.ping.data.repositories

import dev.dr10.ping.data.mappers.toTrackData
import dev.dr10.ping.data.models.TrackData
import dev.dr10.ping.domain.repositories.AuthRepository
import dev.dr10.ping.domain.repositories.PresenceRepository
import io.github.jan.supabase.realtime.PresenceAction
import io.github.jan.supabase.realtime.RealtimeChannel
import io.github.jan.supabase.realtime.track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn

class PresenceRepositoryImpl(
    private val presenceChannel: RealtimeChannel,
    private val authRepository: AuthRepository
): PresenceRepository {

    /**
     * A shared flow that emits presence actions [PresenceAction.joins] and [PresenceAction.leaves] from the presence channel
     */
    private val sharedPresenceFlow: Flow<PresenceAction> by lazy {
        presenceChannel.presenceChangeFlow()
            .shareIn(
                scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
                started = SharingStarted.Eagerly,
                replay = 1
            )
    }

    /**
     * Subscribes to the presence channel and sends the current user's track data
     */
    override suspend fun subscribeToChannelAndSendTrack() {
        val userId = authRepository.getProfileData()!!.userId
        presenceChannel.apply {
            subscribe(blockUntilSubscribed = true)
            track(TrackData(user_id = userId))
        }
    }

    /**
     * Observes the presence of a user with the given [userId] and emits `true` when the user joins and `false` when the user leaves
     *
     * @param userId The ID of the user to observe
     * @return A flow when the user joins or leaves
     */
    override suspend fun observeUserPresence(userId: String): Flow<Boolean> {
        return sharedPresenceFlow
            .map { action ->
                val isJoin = action.joins.values.any { it.state.toTrackData().user_id == userId }
                if (isJoin) true

                val isLeave = action.leaves.values.any { it.state.toTrackData().user_id == userId }
                if (isLeave) false

                null
            }
            .filterNotNull()
            .distinctUntilChanged()
    }
}