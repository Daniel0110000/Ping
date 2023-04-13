package com.daniel.ping.data.local

import android.content.Context
import android.content.SharedPreferences
import com.daniel.ping.domain.utilities.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

// The SharedPreferences class is responsible for managing data storage in Shared Preferences
class SharedPreferenceManager @Inject constructor(@ApplicationContext context: Context) {

    // Variable that contains the instance of the SharedPreferences
    private var sharedPreferences: SharedPreferences

    // The constructor initializes the instance of SharedPreferences
    init {
        sharedPreferences = context.getSharedPreferences(Constants.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    // Method to save a boolean value in SharedPreferences
    fun putBoolean(key: String, value: Boolean){
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    // Method to get a boolean value from SharedPreferences
    fun getBoolean(key: String): Boolean{
        return sharedPreferences.getBoolean(key, false)
    }

    // Method to save a String value in SharedPreferences
    fun putString(key: String, value: String){
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    // Method to get a String value from SharedPreferences
    fun getString(key: String): String{
        return sharedPreferences.getString(key, null).toString()
    }

    // Method to clear all the SharedPreferences
    fun clean(){
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

}