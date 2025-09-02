package dev.dr10.ping.ui.screens.chats.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import dev.dr10.ping.domain.models.MessageModel
import dev.dr10.ping.ui.theme.AppTheme
import network.chaintech.sdpcomposemultiplatform.sdp
import network.chaintech.sdpcomposemultiplatform.ssp

@Composable
fun ReceiverMessageItemList(
    profileImageUrl: String,
    messageModel: MessageModel
) = Row(Modifier.fillMaxWidth()) {
    AsyncImage(
        model = profileImageUrl,
        contentDescription = messageModel.receiverId,
        modifier = Modifier
            .size(20.sdp)
            .clip(RoundedCornerShape(4.sdp))
            .align(Alignment.Bottom),
        contentScale = ContentScale.Crop
    )

    Spacer(Modifier.width(5.sdp))

    Column(Modifier.weight(3.5f)) {
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .background(
                    color = AppTheme.colors.complementary,
                    shape = RoundedCornerShape(topStart = 7.sdp, topEnd = 7.sdp, bottomEnd = 7.sdp)
                )
        ) {
            Text(
                text = messageModel.content,
                color = AppTheme.colors.text,
                fontFamily = AppTheme.robotoFont,
                fontWeight = FontWeight.Normal,
                fontSize = 10.ssp,
                modifier = Modifier.padding(4.sdp)
            )
        }

        Spacer(Modifier.height(3.sdp))

        Text(
            text = messageModel.date,
            color = AppTheme.colors.textSecondary,
            fontFamily = AppTheme.robotoFont,
            fontWeight = FontWeight.Normal,
            fontSize = 8.ssp,
        )
    }

    Spacer(Modifier.weight(1f))
}