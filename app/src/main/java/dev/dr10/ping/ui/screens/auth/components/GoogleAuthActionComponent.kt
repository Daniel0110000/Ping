package dev.dr10.ping.ui.screens.auth.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import dev.dr10.ping.R
import dev.dr10.ping.ui.theme.AppTheme
import network.chaintech.sdpcomposemultiplatform.sdp

@Composable
fun GoogleAuthActionComponent(
    height: Dp = 40.sdp,
    isLoading: Boolean = false,
    onClick: () -> Unit
) = Button(
    onClick = { if (!isLoading) onClick() },
    colors = ButtonDefaults.buttonColors().copy(
        containerColor = AppTheme.colors.text,
        contentColor = AppTheme.colors.complementary
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
            color = AppTheme.colors.text,
            trackColor = AppTheme.colors.complementary,
        )
    } else {
        Image(
            painter = painterResource(R.drawable.ic_google),
            contentDescription = "Google",
            modifier = Modifier.size((19.5).sdp)
        )
    }
}