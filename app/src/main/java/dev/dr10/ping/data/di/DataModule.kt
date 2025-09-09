package dev.dr10.ping.data.di

import androidx.room.Room
import dev.dr10.ping.BuildConfig
import dev.dr10.ping.data.local.datastore.UserProfileStore
import dev.dr10.ping.data.local.room.AppDatabase
import dev.dr10.ping.data.local.storage.LocalImageStorageManager
import dev.dr10.ping.data.repositories.AuthRepositoryImpl
import dev.dr10.ping.data.repositories.ConversationsRepositoryImpl
import dev.dr10.ping.data.repositories.MessagesRepositoryImpl
import dev.dr10.ping.data.repositories.PresenceRepositoryImpl
import dev.dr10.ping.data.repositories.StorageRepositoryImpl
import dev.dr10.ping.data.repositories.UsersRepositoryImpl
import dev.dr10.ping.domain.repositories.AuthRepository
import dev.dr10.ping.domain.repositories.ConversationsRepository
import dev.dr10.ping.domain.repositories.MessagesRepository
import dev.dr10.ping.domain.repositories.PresenceRepository
import dev.dr10.ping.domain.repositories.StorageRepository
import dev.dr10.ping.domain.repositories.UsersRepository
import dev.dr10.ping.domain.utils.Constants
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.realtime.RealtimeChannel
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import org.koin.dsl.module

val dataModule = module {
    single<SupabaseClient> {
        createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_KEY
        ) {
            install(Postgrest)
            install(Auth)
            install(Storage)
            install(Realtime)
        }
    }
    single<Auth> { get<SupabaseClient>().auth }
    single<Storage> { get<SupabaseClient>().storage }
    single<RealtimeChannel> { get<SupabaseClient>().channel("global:presence") }

    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<StorageRepository> { StorageRepositoryImpl(get(), get()) }
    single<LocalImageStorageManager> { LocalImageStorageManager(get()) }
    single<UsersRepository> { UsersRepositoryImpl(get()) }
    single<MessagesRepository> { MessagesRepositoryImpl(get(), get()) }
    single<ConversationsRepository> { ConversationsRepositoryImpl(get(), get(), get(), get()) }
    single<PresenceRepository> { PresenceRepositoryImpl(get(), get()) }

    single { UserProfileStore(get()) }
    single<AppDatabase> {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            Constants.APP_DB
        ).build()
    }
}