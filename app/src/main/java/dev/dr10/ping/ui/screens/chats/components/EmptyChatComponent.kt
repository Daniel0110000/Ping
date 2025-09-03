package dev.dr10.ping.ui.screens.chats.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import dev.dr10.ping.domain.models.UserProfileModel
import dev.dr10.ping.ui.theme.AppTheme
import network.chaintech.sdpcomposemultiplatform.sdp
import network.chaintech.sdpcomposemultiplatform.ssp

@Composable
fun EmptyChatComponent(
    receiverData: UserProfileModel
) = Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
) {
    AsyncImage(
        model = receiverData.profileImageUrl,
        contentDescription = receiverData.username,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(75.sdp)
            .clip(RoundedCornerShape(8.sdp))
    )

    Spacer(Modifier.height(4.sdp))

    Text(
        text = receiverData.username,
        fontFamily = AppTheme.robotoFont,
        color = AppTheme.colors.text,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.ssp
    )

    Text(
        text = receiverData.bio,
        fontFamily = AppTheme.robotoFont,
        color = AppTheme.colors.textSecondary,
        fontWeight = FontWeight.Normal,
        fontSize = 12.ssp,
        textAlign = TextAlign.Center
    )

}