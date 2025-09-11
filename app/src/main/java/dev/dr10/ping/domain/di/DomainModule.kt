package dev.dr10.ping.domain.di

import dev.dr10.ping.android.notifications.AppNotificationsManager
import dev.dr10.ping.domain.usesCases.AuthWithGoogleUseCase
import dev.dr10.ping.domain.usesCases.FetchAndStoreUserDataUseCase
import dev.dr10.ping.domain.usesCases.GetMessagesUseCase
import dev.dr10.ping.domain.usesCases.GetProfileImageUseCase
import dev.dr10.ping.domain.usesCases.GetRecentConversationsUseCase
import dev.dr10.ping.domain.usesCases.GetSuggestedUsersUseCase
import dev.dr10.ping.domain.usesCases.GetUserPresenceUseCase
import dev.dr10.ping.domain.usesCases.InitializeRealtimeChatUseCase
import dev.dr10.ping.domain.usesCases.InitializeRealtimeRecentConversationsUseCase
import dev.dr10.ping.domain.usesCases.ObserveUserPresenceUseCase
import dev.dr10.ping.domain.usesCases.ProcessNotificationUseCase
import dev.dr10.ping.domain.usesCases.ProfileSetupUseCase
import dev.dr10.ping.domain.usesCases.SearchUserUseCase
import dev.dr10.ping.domain.usesCases.SendMessageUseCase
import dev.dr10.ping.domain.usesCases.SendNotificationUseCase
import dev.dr10.ping.domain.usesCases.SignInWithEmailAndPasswordUseCase
import dev.dr10.ping.domain.usesCases.SignUpWithEmailAndPasswordUseCase
import dev.dr10.ping.domain.usesCases.UpdateUserPresenceUseCase
import org.koin.dsl.module

val domainModule = module {
    single { SignUpWithEmailAndPasswordUseCase(get()) }
    single { SignInWithEmailAndPasswordUseCase(get()) }
    single { AuthWithGoogleUseCase(get()) }
    single { ProfileSetupUseCase(get(), get(), get(), get()) }
    single { FetchAndStoreUserDataUseCase(get(), get(), get()) }
    single { GetSuggestedUsersUseCase(get(), get()) }
    single { SearchUserUseCase(get(), get()) }
    single { GetProfileImageUseCase(get(), get()) }
    single { FetchAndStoreUserDataUseCase(get(), get(), get()) }
    single { SendMessageUseCase(get(), get(), get(), get()) }
    single { GetMessagesUseCase(get(), get()) }
    single { InitializeRealtimeChatUseCase(get(), get()) }
    single { InitializeRealtimeRecentConversationsUseCase(get()) }
    single { GetRecentConversationsUseCase(get()) }
    single { UpdateUserPresenceUseCase(get(), get()) }
    single { GetUserPresenceUseCase(get()) }
    single { ObserveUserPresenceUseCase(get()) }
    single { ProcessNotificationUseCase(get()) }
    single { SendNotificationUseCase(get(), get()) }

    single { AppNotificationsManager() }
}