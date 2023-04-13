package com.daniel.ping.data.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * A Dagger module that provides Firebase authentication and Firebase instances as singleton object
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    /**
     * Provides the singleton instance of Firebase authentication
     *
     * @return The Firebase authentication instance
     */
    @Singleton
    @Provides
    fun providerFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Provides the singleton instance of Firebase FireStore
     *
     * @return The Firebase FireStore instance
     */
    @Singleton
    @Provides
    fun providerFirebaseFireStore(): FirebaseFirestore = FirebaseFirestore.getInstance()

}