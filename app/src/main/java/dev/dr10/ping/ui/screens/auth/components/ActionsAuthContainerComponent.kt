package dev.dr10.ping.ui.screens.auth.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dev.dr10.ping.ui.theme.AppTheme
import network.chaintech.sdpcomposemultiplatform.sdp

@Composable
fun ActionsAuthContainerComponent(
    content: @Composable ColumnScope.() -> Unit
) = Column(
    modifier = Modifier
        .clip(RoundedCornerShape(15.sdp, 15.sdp, 0.dp, 0.dp))
        .background(AppTheme.colors.onBackground)
        .fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally
) { content() }