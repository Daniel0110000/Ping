package dev.dr10.ping.android.notifications

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dev.dr10.ping.domain.usesCases.ProcessNotificationUseCase
import dev.dr10.ping.domain.usesCases.UpdateFCMTokenUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
class PushNotificationsService: FirebaseMessagingService() {
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onNewToken(token: String) {
        // When a new token is generated, update it in the backend
        scope.launch {
            try {
                val updateFCMTokenUseCase: UpdateFCMTokenUseCase by inject()
                updateFCMTokenUseCase(token)
            } catch (e: Exception) { Log.e(this.javaClass.simpleName, e.message.toString()) }
        }

    }

    override fun onMessageReceived(message: RemoteMessage) {
        // Inject the use case needed to process the notification
        val processNotificationUseCase: ProcessNotificationUseCase by inject()
        // Process the notification with the message received
        processNotificationUseCase(applicationContext, message)
    }

}