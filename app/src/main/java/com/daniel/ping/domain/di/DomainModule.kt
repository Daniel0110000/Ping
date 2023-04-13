package com.daniel.ping.domain.di

import com.daniel.ping.data.repositories.AuthenticationRepositoryImpl
import com.daniel.ping.domain.repositories.AuthenticationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    // Binds the AuthenticationRepositoryImpl instance to the AuthenticationRepository interface.
    @Binds
    @Singleton
    abstract fun providerUserDataRepository(authenticationRepositoryImpl: AuthenticationRepositoryImpl): AuthenticationRepository
}