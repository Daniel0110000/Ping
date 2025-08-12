package dev.dr10.ping.data.di

import dev.dr10.ping.BuildConfig
import dev.dr10.ping.data.local.datastore.UserProfileStore
import dev.dr10.ping.data.local.storage.LocalImageStorageManager
import dev.dr10.ping.data.repositories.AuthRepositoryImpl
import dev.dr10.ping.data.repositories.StorageRepositoryImpl
import dev.dr10.ping.data.repositories.UsersRepositoryImpl
import dev.dr10.ping.domain.repositories.AuthRepository
import dev.dr10.ping.domain.repositories.StorageRepository
import dev.dr10.ping.domain.repositories.UsersRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
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
        }
    }
    single<Auth> { get<SupabaseClient>().auth }
    single<Storage> { get<SupabaseClient>().storage }

    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<StorageRepository> { StorageRepositoryImpl(get(), get()) }
    single<LocalImageStorageManager> { LocalImageStorageManager(get()) }
    single<UsersRepository> { UsersRepositoryImpl(get()) }

    single { UserProfileStore(get()) }
}