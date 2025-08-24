package dev.dr10.ping.ui.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import dev.dr10.ping.R
import dev.dr10.ping.ui.theme.AppTheme
import network.chaintech.sdpcomposemultiplatform.sdp

@Composable
fun ProfileImageAndStatusComponent(
    image: Painter,
    isOnline: Boolean = true,
    size: Dp = 50.dp
) = ConstraintLayout(Modifier.size(60.dp)) {
    val (profileImage, statusIndicator) = createRefs()

    Image(
        painter = image,
        contentDescription = stringResource(R.string.profile_image),
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(6.sdp))
            .constrainAs(profileImage) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
    )

    if (isOnline) {
        Box(
            Modifier
                .size(10.dp)
                .background(AppTheme.colors.online, shape = CircleShape)
                .constrainAs(statusIndicator) {
                    top.linkTo(profileImage.bottom, (-10).dp)
                    start.linkTo(profileImage.end, (-8).dp)
                }
        )
    }

}