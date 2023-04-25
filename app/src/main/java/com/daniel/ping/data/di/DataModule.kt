package com.daniel.ping.data.di

import com.daniel.ping.data.remote.networkService.ApiService
import com.daniel.ping.domain.utilities.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * A Dagger module that provides Firebase authentication and Firebase instances as singleton object
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    /**
     * Provides the singleton instance of Firebase authentication
     * @return The Firebase authentication instance
     */
    @Singleton
    @Provides
    fun providerFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Provides the singleton instance of Firebase FireStore
     * @return The Firebase FireStore instance
     */
    @Singleton
    @Provides
    fun providerFirebaseFireStore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    /**
     * Provides an instance of the ApiService with the specified Retrofit settings
     * @return An instance of ApiService
     */
    @Singleton
    @Provides
    fun providerApiService(): ApiService = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

}