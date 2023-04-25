package com.daniel.ping.data.remote

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

// Extension function for FirebaseAuth class to sign in in with a given credential
fun FirebaseAuth.withCredentials(credential: AuthCredential): Task<AuthResult> =
    signInWithCredential(credential)

// Extension function for FirebaseAuth class to sign in with email and password
fun FirebaseAuth.signInWithEmailAndPasswordM(email: String, password: String): Task<AuthResult> =
    signInWithEmailAndPassword(email, password)

// Extension function for FirebaseAuth class to sign up with email and password
fun FirebaseAuth.signUpWithEmailAndPassword(email: String, password: String): Task<AuthResult> =
    createUserWithEmailAndPassword(email, password)