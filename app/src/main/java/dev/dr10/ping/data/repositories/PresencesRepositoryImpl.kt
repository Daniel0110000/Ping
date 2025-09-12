package dev.dr10.ping.data.repositories

import dev.dr10.ping.data.mappers.toPresenceData
import dev.dr10.ping.data.models.PresenceData
import dev.dr10.ping.domain.repositories.PresencesRepository
import dev.dr10.ping.domain.utils.Constants
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.RealtimeChannel
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class PresencesRepositoryImpl(
    private val supabaseService: SupabaseClient
): PresencesRepository {

    /**
     * Create initial presence data for the new user
     *
     * @param data The presence data to be created
     */
    override suspend fun createInitialPresence(data: PresenceData) {
        supabaseService.from(Constants.USER_PRESENCES_TABLE).insert(data)
    }

    /**
     * Update the last connected time for the user
     *
     * @param userId The ID of the user to update the last connected time for
     */
    override suspend fun updateLastConnected(userId: String) {
        supabaseService.postgrest.rpc(
            function = Constants.RPC_UPDATE_LAST_CONNECTED,
            parameters = buildJsonObject {
                put(Constants.RPC_USER_ID_PARAM, userId)
            }
        )
    }

    /**
     * Update the user status (online/offline)
     *
     * @param userId The ID of the user to update the status for
     * @param isOnline The new status of the user (true for online, false for offline)
     */
    override suspend fun updateUserStatus(userId: String, isOnline: Boolean) {
        supabaseService.postgrest.rpc(
            function = Constants.RPC_UPDATE_USER_STATUS,
            parameters = buildJsonObject {
                put(Constants.RPC_USER_ID_PARAM, userId)
                put(Constants.RPC_IS_ONLINE_PARAM, isOnline)
            }
        )
    }

    /**
     * Fetch the initial presence data for the user
     *
     * @param userId The ID of the user to fetch the presence data for
     * @return The presence data if found, null otherwise
     */
    override suspend fun fetchInitialUserPresence(userId: String): PresenceData? =
        supabaseService.from(Constants.USER_PRESENCES_TABLE).select {
            filter { PresenceData::user_id eq userId}
        }.decodeSingleOrNull<PresenceData>()

    /**
     * Subscribe to the user presence channel and listen for updates
     *
     * @param userId The ID of the user to subscribe to the presence channel for
     * @return A pair containing the new presence flow and the realtime channel
     */
    override suspend fun subscribeAndListenUserPresence(userId: String): Pair<Flow<PresenceData>, RealtimeChannel> {
        val channel = supabaseService.channel("$userId:presence")
        val updatePresenceFlow = channel.postgresChangeFlow<PostgresAction.Update>(schema = Constants.TABLE_SCHEMA) {
            table = Constants.USER_PRESENCES_TABLE
            filter(Constants.USER_PROFILE_ID, FilterOperator.EQ, userId)
        }
        channel.subscribe()
        val presenceData = updatePresenceFlow.map { it.record.toPresenceData() }

        return Pair(presenceData, channel)
    }

}