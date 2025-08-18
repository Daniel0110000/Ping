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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import dev.dr10.ping.R
import dev.dr10.ping.ui.theme.AppTheme

@Composable
fun HomePlaceholder() = Box(
    modifier = Modifier
        .fillMaxSize()
        .background(AppTheme.colors.background),
    contentAlignment = Alignment.Center
) {
    ConstraintLayout {
        val (iconApp, nameApp) = createRefs()

        Icon(
            painter = painterResource(R.drawable.ic_app),
            contentDescription = stringResource(R.string.app_name),
            tint = AppTheme.colors.onBackground,
            modifier = Modifier
                .size(125.dp)
                .constrainAs(iconApp) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Text(
            text = stringResource(R.string.app_name),
            fontFamily = AppTheme.robotoFont,
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp,
            color = AppTheme.colors.onBackground,
            modifier = Modifier.constrainAs(nameApp) {
                top.linkTo(iconApp.bottom, (-25).dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

    }
}