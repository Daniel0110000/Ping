package com.daniel.ping.data.remote

import android.app.Activity
import android.content.Intent
import com.daniel.ping.domain.useCases.AuthCredentialsUseCase
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FacebookAuthManager @Inject constructor(){

    // Create a callback manager to handle Facebook login callbacks
    private val callbackManager = CallbackManager.Factory.create()

    // Function to handle activity result for Facebook login callbacks
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    // Function to perform a Facebook sign-in flow
    suspend fun performSignIn(activity: Activity, authCredentialsUseCase: AuthCredentialsUseCase) : Task<AuthResult>? = suspendCoroutine { count ->
        // Request Facebook login with email permissions
        LoginManager.getInstance().logInWithReadPermissions(activity, listOf("email"))

        // Register a callback to handle the login result
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onCancel() {
                    // If the use cancels the login, resume with null
                    count.resume(null)
                }

                override fun onError(error: FacebookException) {
                    // If there's an error with the login, resume with null
                    count.resume(null)
                }

                override fun onSuccess(result: LoginResult) {
                    // If the login is success, retrieve the access token and use it to sign in with Firebase
                    result.let {
                        val credential = FacebookAuthProvider.getCredential(it.accessToken.token)
                        CoroutineScope(Dispatchers.IO).launch {
                            // Invoke the auth credentials use case with the Facebook credential and resume with the result
                            count.resume(authCredentialsUseCase.invoke(credential))
                        }
                    }
                }

            })
    }

}