package dev.dr10.ping.domain.mappers

import dev.dr10.ping.data.models.UserProfileData
import dev.dr10.ping.domain.models.RecentConversationModel
import dev.dr10.ping.domain.models.UserProfileModel
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

fun UserProfileData.toModel(): UserProfileModel = UserProfileModel(
    userId = this.userId,
    username = this.username,
    bio = this.bio
)

fun RecentConversationModel.toModel(): UserProfileModel = UserProfileModel(
    userId = this.userId,
    username = this.username,
    bio = "",
    profileImageUrl = this.profilePictureUrl
)

fun String.toProfileModel(): UserProfileModel {
    val decoded = URLDecoder.decode(this, StandardCharsets.UTF_8.toString())
    return Json.decodeFromString<UserProfileModel>(decoded)
}
fun UserProfileModel.encodeToString(): String = Json.encodeToString(UserProfileModel.serializer(), this)