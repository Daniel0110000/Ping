package dev.dr10.ping.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.constraintlayout.compose.ConstraintLayout
import dev.dr10.ping.ui.theme.AppTheme
import network.chaintech.sdpcomposemultiplatform.sdp
import network.chaintech.sdpcomposemultiplatform.ssp

@Composable
fun PlaceholderComponent(
    iconId: Int,
    label: String,
    iconSize: Dp = 100.sdp,
    fontSize: TextUnit = 30.ssp,
    marginTop: Dp = (-20).sdp
) = Box(
    modifier = Modifier
        .fillMaxSize()
        .background(AppTheme.colors.background),
    contentAlignment = Alignment.Center
) {
    ConstraintLayout {
        val (iconApp, nameApp) = createRefs()

        Icon(
            painter = painterResource(iconId),
            contentDescription = label,
            tint = AppTheme.colors.onBackground,
            modifier = Modifier
                .size(iconSize)
                .constrainAs(iconApp) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Text(
            text = label,
            fontFamily = AppTheme.robotoFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = fontSize,
            color = AppTheme.colors.onBackground,
            modifier = Modifier.constrainAs(nameApp) {
                top.linkTo(iconApp.bottom, marginTop)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

    }
}