package com.daniel.ping.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.daniel.ping.data.local.SharedPreferenceManager
import com.daniel.ping.domain.utilities.Constants
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

open class BaseActivity: ComponentActivity() {

    // Reference to the FireStore document for the current user
    private lateinit var documentReference: DocumentReference

    /**
     * Called when the activity is starting
     * It initializes the FireStore document reference for the current user
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Get the shared preference manager and FireStore database instance
        val preferenceManager = SharedPreferenceManager(this)
        val database = FirebaseFirestore.getInstance()
        // Get the reference to the FireStore document for the current user
        documentReference = database.collection(Constants.KEY_COLLECTION_USERS)
            .document(preferenceManager.getString(Constants.KEY_USER_ID))
    }

    /**
     * Called when the activity is going into the background, e.g., when another activity is launched
     * It sets the availability of the current user in FireStore to 0, indicating that they are not available
     */
    override fun onPause() {
        super.onPause()

        // Update the availability of the current user in FireStore to 0 (not available)
        documentReference.update(Constants.KEY_AVAILABILITY, 0)
    }

    /**
     * Called when the activity is becoming visible to the user, e.g., when the user returns to the activity
     * It sets the availability of the current user in FireStore to 1, indicating that they are available
     */
    override fun onResume() {
        super.onResume()
        // Update the availability of the current user in FireStore to 1 (available)
        documentReference.update(Constants.KEY_AVAILABILITY, 1)
    }

}