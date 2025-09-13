package dev.dr10.ping.ui.screens.auth.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import dev.dr10.ping.ui.theme.AppTheme
import network.chaintech.sdpcomposemultiplatform.sdp
import network.chaintech.sdpcomposemultiplatform.ssp

@Composable
fun ButtonAuthActionComponent(
    label: String,
    height: Dp = 40.sdp,
    isLoading: Boolean = false,
    onClick: () -> Unit
) = Button(
    onClick = { if (!isLoading) onClick() },
    colors = ButtonDefaults.buttonColors().copy(
        containerColor = AppTheme.colors.complementary,
        contentColor = AppTheme.colors.text
    ),
    shape = RoundedCornerShape(7.sdp),
    modifier = Modifier
        .fillMaxWidth()
        .height(height)
        .padding(horizontal = 15.sdp)
) {
    if (isLoading) {
        CircularProgressIndicator(
            strokeWidth = (1.7).sdp,
            modifier = Modifier.size(17.sdp),
            color = AppTheme.colors.complementary,
            trackColor = AppTheme.colors.text,
        )
    } else {
        Text(
            text = label,
            fontFamily = AppTheme.robotoFont,
            fontWeight = FontWeight.Medium,
            fontSize = (13.5).ssp
        )
    }
}