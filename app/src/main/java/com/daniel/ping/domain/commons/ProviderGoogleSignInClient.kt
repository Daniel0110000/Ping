package com.daniel.ping.domain.commons

import android.content.Context
import android.content.Intent
import com.daniel.ping.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class ProviderGoogleSignInClient {

    // This function create and return a Google Sign-In intent using the given context
    fun invoke(context: Context): Intent{
        // Configure the Google Sign-In options with the desired options, including requesting the user's email and an ID token
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Create a Google Sign-In client with the configured options
        val googleSignInClient = GoogleSignIn.getClient(context, gso)

        // Return a Google Sign-In intent for the client to start the authentication flow
        return googleSignInClient.signInIntent
    }
}