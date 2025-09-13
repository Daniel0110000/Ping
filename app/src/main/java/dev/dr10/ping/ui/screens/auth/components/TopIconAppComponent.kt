package dev.dr10.ping.ui.screens.auth.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import dev.dr10.ping.R
import network.chaintech.sdpcomposemultiplatform.sdp

@Composable
fun TopIconAppComponent() = Box(
    modifier = Modifier.fillMaxWidth(),
    contentAlignment = Alignment.Center
) {
    Image(
        painter = painterResource(id = R.drawable.ic_app),
        contentDescription = stringResource(R.string.app_name),
        modifier = Modifier.size(35.sdp)
    )
}