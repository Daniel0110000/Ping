package com.daniel.ping.domain.di

import com.daniel.ping.data.repositories.AuthenticationRepositoryImpl
import com.daniel.ping.domain.repositories.AuthenticationRepository
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
    abstract fun providerUserDataRepository(authenticationRepositoryImpl: AuthenticationRepositoryImpl): AuthenticationRepository
}