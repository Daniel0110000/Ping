package dev.dr10.ping.ui.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import dev.dr10.ping.domain.mappers.toModel
import dev.dr10.ping.domain.models.RecentConversationModel
import dev.dr10.ping.domain.models.UserProfileModel
import network.chaintech.sdpcomposemultiplatform.sdp

@Composable
fun RecentConversationItemList(
    data: RecentConversationModel,
    onClick: (UserProfileModel) -> Unit
) = AsyncImage(
    model = data.profilePictureUrl,
    contentDescription = data.username,
    contentScale = ContentScale.Crop,
    modifier = Modifier
        .size(35.sdp)
        .clip(RoundedCornerShape(8.sdp))
        .clickable { onClick(data.toModel()) }
)