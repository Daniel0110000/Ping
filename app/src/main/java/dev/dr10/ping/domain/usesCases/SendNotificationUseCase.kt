package dev.dr10.ping.domain.usesCases

import android.util.Log
import dev.dr10.ping.data.models.NotificationRequestBody
import dev.dr10.ping.data.models.UserProfileData
import dev.dr10.ping.domain.extensions.toProfileImageUrl
import dev.dr10.ping.domain.repositories.NotificationsRepository
import dev.dr10.ping.domain.repositories.UsersRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SendNotificationUseCase(
    private val repository: NotificationsRepository,
    private val usersRepository: UsersRepository
) {

    operator fun invoke(
        userProfileData: UserProfileData,
        receiverUserId: String,
        content: String
    ) = CoroutineScope(Dispatchers.IO).launch {
        try {
            // Fetch the FCM token of the receiver user
            usersRepository.fetchFcmToken(receiverUserId)?.let {
                // Send one notification to the receiver user
                repository.sendNotification(
                    NotificationRequestBody(
                        to = it,
                        content = content,
                        senderId = userProfileData.userId,
                        username = userProfileData.username,
                        profileImageUrl = userProfileData.profileImageName.toProfileImageUrl()
                    )
                )
            }
        } catch (e: Exception) { Log.e(this.javaClass.simpleName, e.message.toString()) }
    }

}