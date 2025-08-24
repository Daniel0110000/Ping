package dev.dr10.ping.ui.screens.chats.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import dev.dr10.ping.domain.models.MessageDataModel
import dev.dr10.ping.ui.theme.AppTheme
import network.chaintech.sdpcomposemultiplatform.sdp
import network.chaintech.sdpcomposemultiplatform.ssp

@Composable
fun SenderMessageItemList(
    messageData: MessageDataModel
) = Row (Modifier.fillMaxWidth()) {
    Spacer(Modifier.weight(1f))

    Column(Modifier.weight(3.5f), horizontalAlignment = Alignment.End) {
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .background(
                    color = AppTheme.colors.onBackground,
                    shape = RoundedCornerShape(topStart = 7.sdp, topEnd = 7.sdp, bottomStart = 7.sdp)
                )
        ) {
            Text(
                text = messageData.message,
                color = AppTheme.colors.text,
                fontFamily = AppTheme.robotoFont,
                fontWeight = FontWeight.Normal,
                fontSize = 10.ssp,
                modifier = Modifier.padding(4.sdp)
            )
        }

        Spacer(Modifier.height(3.sdp))

        Text(
            text = messageData.date,
            color = AppTheme.colors.textSecondary,
            fontFamily = AppTheme.robotoFont,
            fontWeight = FontWeight.Normal,
            fontSize = 8.ssp,
        )
    }
}