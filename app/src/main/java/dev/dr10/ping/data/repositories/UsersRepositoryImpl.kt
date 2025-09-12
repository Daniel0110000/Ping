package dev.dr10.ping.data.repositories

import dev.dr10.ping.data.mappers.toProfileData
import dev.dr10.ping.data.mappers.toUserData
import dev.dr10.ping.data.models.SimpleUserData
import dev.dr10.ping.data.models.UserData
import dev.dr10.ping.data.models.UserProfileData
import dev.dr10.ping.domain.repositories.UsersRepository
import dev.dr10.ping.domain.utils.Constants
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class UsersRepositoryImpl(
    private val supabaseService: SupabaseClient
): UsersRepository {

    /**
     * Save user profile data to the Supabase database
     *
     * @param data The user profile data to be saved
     */
    override suspend fun saveUserData(data: UserProfileData) {
        supabaseService.from(Constants.USERS_TABLE).insert(data.toUserData())
    }

    /**
     * Fetch user profile data from the Supabase database by user ID
     *
     * @param userId The ID of the user whose profile data is to be fetched
     * @return The user profile data if found, null otherwise
     */
    override suspend fun fetchUserData(userId: String): UserProfileData? = supabaseService.from(Constants.USERS_TABLE).select {
        filter { UserData::user_id eq userId }
    }.decodeSingleOrNull<UserData>()?.toProfileData()

    /**
     * Fetch suggested users for the given user
     *
     * @param userId The ID of the current user
     * @return A list of suggested user profiles
     */
    override suspend fun fetchSuggestedUsers(userId: String): List<UserProfileData> =
        supabaseService.postgrest.rpc(
            function = Constants.RPC_FUNCTION_NAME,
            parameters = buildJsonObject { put(Constants.RPC_FUNCTION_PARAM, userId) }
        ).decodeList<UserData>().map { it.toProfileData() }

    /**
     * Fetch users by username, excluding the current user
     *
     * @param id The ID of the current user
     * @param query The username query to search for
     * @return A list of user profiles matching the query
     */
    override suspend fun fetchUsersByUsername(id: String, query: String): List<UserProfileData> = supabaseService.from(
        Constants.USERS_TABLE).select {
            filter {
                UserData::user_id neq id
                UserData::username ilike "%$query%"
            }
        }
        .decodeList<UserData>()
        .map { it.toProfileData() }

    /**
     * Fetch the columns [Constants.USERNAME_COLUMN] and [Constants.PROFILE_IMAGE_COLUMN] from the [Constants.USERS_TABLE]
     *
     * @param userId The ID of the user whose data is to be fetched
     * @return The simple user data
     */
    override suspend fun fetchSimpleUserData(userId: String): SimpleUserData =
        supabaseService.from(Constants.USERS_TABLE).select(
            columns = Columns.list(Constants.USERNAME_COLUMN, Constants.PROFILE_IMAGE_COLUMN)
        ) { filter { UserData::user_id eq userId } }.decodeList<SimpleUserData>().single()

    /**
     * Update the FCM token for the current user
     *
     * @param fcmToken The new FCM token to be updated
     * @param currentUserId The ID of the current user
     */
    override suspend fun updateFcmToken(fcmToken: String, currentUserId: String) {
        supabaseService.from(Constants.USERS_TABLE).update(
            { set(Constants.FCM_TOKEN_COLUMN, fcmToken) }
        ) { filter { UserData::user_id eq currentUserId } }
    }

    /**
     * Fetch the FCM token for a given user ID
     *
     * @param userId The ID of the user whose FCM token is to be fetched
     * @return The FCM token if found, null otherwise
     */
    override suspend fun fetchFcmToken(userId: String): String? =
        supabaseService.from(Constants.USERS_TABLE).select(
            columns = Columns.list(Constants.FCM_TOKEN_COLUMN)
        ) { filter { UserData::user_id eq userId } }.decodeList<Map<String, String?>>().single()[Constants.FCM_TOKEN_COLUMN]
}