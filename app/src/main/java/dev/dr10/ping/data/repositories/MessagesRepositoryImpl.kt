package dev.dr10.ping.data.repositories

import dev.dr10.ping.data.models.MessageData
import dev.dr10.ping.domain.repositories.MessagesRepository
import dev.dr10.ping.domain.utils.Constants
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

class MessagesRepositoryImpl(
    private val supabaseService: SupabaseClient
): MessagesRepository {

    /**
     * Save a message to the [Constants.MESSAGES_TABLE] table using the provided message data
     *
     * @param message The message data to be saved
     */
    override suspend fun sendMessage(message: MessageData) {
        supabaseService.from(Constants.MESSAGES_TABLE).insert(message)
    }


}