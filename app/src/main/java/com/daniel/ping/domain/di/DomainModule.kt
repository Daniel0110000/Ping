package com.daniel.ping.domain.di

import com.daniel.ping.data.repositories.AuthenticationRepositoryImpl
import com.daniel.ping.data.repositories.ChatRepositoryImpl
import com.daniel.ping.data.repositories.UserDataRepositoryImpl
import com.daniel.ping.domain.repositories.AuthenticationRepository
import com.daniel.ping.domain.repositories.ChatRepository
import com.daniel.ping.domain.repositories.UserDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * A Dagger module that provides the singleton instance of the AuthenticationRepository interface
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    /**
     * Binds the AuthenticationRepositoryImpl instance to the AuthenticationRepository
     * ... interface, ensuring that only a single instance of the repository is used throughout the app
     *
     * @param authenticationRepositoryImpl The AuthenticationRepositoryImpl instance
     * @return The singleton instance of the  AuthenticationRepository interface
     */
    @Binds
    @Singleton
    abstract fun providerAuthenticationRepository(authenticationRepositoryImpl: AuthenticationRepositoryImpl): AuthenticationRepository

    /**
     * Abstract method that binds a concrete implementation of UserDataRepository to the abstract UserDataRepository interface.
     * @param userDataRepositoryImpl the concrete implementation of UserDataRepository to be bound
     * @return a UserDataRepository object
     */
    @Binds
    @Singleton
    abstract fun providerUserDataRepository(userDataRepositoryImpl: UserDataRepositoryImpl): UserDataRepository

    /**
     * Binds the concrete implementation of ChatRepositoryImpl to the ChatRepository interface
     * @param chatRepositoryImpl an instance of ChatRepositoryImpl to be bound
     * @return ChatRepository instance
     */
    @Binds
    @Singleton
    abstract fun providerChatRepository(chatRepositoryImpl: ChatRepositoryImpl): ChatRepository
}