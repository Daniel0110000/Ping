package dev.dr10.ping.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.rememberNavBackStack
import dev.dr10.ping.R
import dev.dr10.ping.ui.extensions.addIfNotExists
import dev.dr10.ping.ui.navigation.HomeNavDestination
import dev.dr10.ping.ui.navigation.HomeNavHost
import dev.dr10.ping.ui.screens.components.HomePlaceholder
import dev.dr10.ping.ui.screens.components.IconButtonComponent
import dev.dr10.ping.ui.screens.components.ProfileImageAndStatusComponent
import dev.dr10.ping.ui.theme.AppTheme

@Composable
fun HomeContainerScreen() {
    val backStack = rememberNavBackStack(HomeNavDestination.HomePlaceHolder)

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(75.dp)
                .background(
                    AppTheme.colors.onBackground,
                    shape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(15.dp))

            IconButtonComponent(
                iconId = R.drawable.ic_messages,
                isClickable = false
            )

            Spacer(Modifier.weight(1f))

            IconButtonComponent(
                iconId = R.drawable.ic_add,
                iconColor = AppTheme.colors.complementary
            ) { backStack.addIfNotExists(HomeNavDestination.Network) }

            Spacer(Modifier.height(8.dp))

            ProfileImageAndStatusComponent(
                image = painterResource(R.drawable.tmp_profile_image)
            )

            Spacer(Modifier.height(15.dp))

        }

        HomeNavHost(
            backStack = backStack,
            modifier = Modifier.fillMaxSize()
        )

        HomePlaceholder()

    }
}