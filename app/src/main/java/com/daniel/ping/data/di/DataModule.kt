package com.daniel.ping.data.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    // This provides a singleton instance of FirebaseAuth
    @Singleton
    @Provides
    fun providerFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    // This provides a singleton instance of FirebaseFireStore
    @Singleton
    @Provides
    fun providerFirebaseFireStore(): FirebaseFirestore = FirebaseFirestore.getInstance()

}