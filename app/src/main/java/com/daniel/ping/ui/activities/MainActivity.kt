package com.daniel.ping.ui.activities

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.daniel.ping.domain.utilities.Constants
import com.daniel.ping.ui.screens.MainScreen
import com.daniel.ping.ui.theme.PingTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Annotated with @AndroidEntryPoint to enable Hilt dependency injection for this activity
 * MainActivity is the entry point for the app and sets up the navigation controller for the app
 */
@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PingTheme {
                navController = rememberNavController()
                MainScreen(navController)
                closeNotification()
            }
        }

    }


    /**
     * Closes the notification with the given ID, if it exists
     */
    private fun closeNotification(){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val activeNotification = notificationManager.activeNotifications
        val notification = activeNotification.find { it.id == Constants.NOTIFICATION_ID }
        if(notification != null)
            notificationManager.cancel(Constants.NOTIFICATION_ID)
    }

}
