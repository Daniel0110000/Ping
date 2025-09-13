package dev.dr10.ping.ui.screens.auth.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import dev.dr10.ping.ui.theme.AppTheme
import network.chaintech.sdpcomposemultiplatform.sdp
import network.chaintech.sdpcomposemultiplatform.ssp

@Composable
fun HeaderInfoScreenComponent(
    title: String,
    description: String
) = Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Text(
        text = title,
        fontFamily = AppTheme.robotoFont,
        fontWeight = FontWeight.Medium,
        fontSize = 21.ssp,
        color = AppTheme.colors.text
    )

    Spacer(modifier = Modifier.height(5.sdp))

    Text(
        text = description,
        fontFamily = AppTheme.robotoFont,
        fontWeight = FontWeight.Normal,
        fontSize = 11.ssp,
        color = AppTheme.colors.textSecondary
    )
}