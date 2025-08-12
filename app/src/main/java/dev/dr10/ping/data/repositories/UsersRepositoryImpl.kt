package dev.dr10.ping.data.repositories

import dev.dr10.ping.data.mappers.toProfileData
import dev.dr10.ping.data.mappers.toUserData
import dev.dr10.ping.data.models.UserData
import dev.dr10.ping.data.models.UserProfileData
import dev.dr10.ping.domain.repositories.UsersRepository
import dev.dr10.ping.domain.utils.Constants
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

class UsersRepositoryImpl(
    private val supabaseService: SupabaseClient
): UsersRepository {

    /**
     * Saves user profile data to the Supabase database
     *
     * @param data The user profile data to be saved
     */
    override suspend fun saveUserData(data: UserProfileData) {
        supabaseService.from(Constants.USERS_TABLE).insert(data.toUserData())
    }

    /**
     * Fetches user profile data from the Supabase database by user ID
     *
     * @param userId The ID of the user whose profile data is to be fetched
     * @return The user profile data if found, null otherwise
     */
    override suspend fun fetchUserData(userId: String): UserProfileData? = supabaseService.from(Constants.USERS_TABLE).select {
        filter { UserData::user_id eq userId }
    }.decodeSingleOrNull<UserData>()?.toProfileData()


}