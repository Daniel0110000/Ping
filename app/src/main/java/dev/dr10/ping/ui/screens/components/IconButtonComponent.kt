package dev.dr10.ping.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import dev.dr10.ping.ui.theme.AppTheme
import network.chaintech.sdpcomposemultiplatform.sdp

@Composable
fun IconButtonComponent(
    iconId: Int,
    contentDescription: String? = null,
    size: Dp = 35.sdp,
    iconSize: Dp = 18.sdp,
    background: Color = AppTheme.colors.surfaceBackground,
    iconColor: Color = AppTheme.colors.textSecondary,
    borderRadius: Dp = 7.sdp,
    isClickable: Boolean = true,
    onClick: () -> Unit = {}
) = Box(
    modifier = Modifier
        .size(size)
        .background(background, shape = RoundedCornerShape(borderRadius))
        .clickable(isClickable) { onClick() },
    contentAlignment = Alignment.Center
) {
    Icon(
        painter = painterResource(iconId),
        contentDescription = contentDescription,
        tint = iconColor,
        modifier = Modifier.size(iconSize)
    )
}