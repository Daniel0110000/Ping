package dev.dr10.ping.ui.screens

import android.Manifest
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import dev.dr10.ping.R
import dev.dr10.ping.android.notifications.AppNotificationsManager
import dev.dr10.ping.domain.mappers.encodeToString
import dev.dr10.ping.ui.navigation.Chat
import dev.dr10.ping.ui.navigation.HomeNavHost
import dev.dr10.ping.ui.navigation.Network
import dev.dr10.ping.ui.screens.components.HomePlaceholder
import dev.dr10.ping.ui.screens.components.IconButtonComponent
import dev.dr10.ping.ui.screens.components.RecentConversationItemList
import dev.dr10.ping.ui.theme.AppTheme
import dev.dr10.ping.ui.viewmodels.HomeViewModel
import network.chaintech.sdpcomposemultiplatform.sdp
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeContainerScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onErrorMessage: (Int) -> Unit
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsState().value
    val recentConversationsFlow = viewModel.recentConversations.collectAsState().value
    val recentConversationsState = recentConversationsFlow.collectAsLazyPagingItems()
    val navController = rememberNavController()
    val appNotificationsManager: AppNotificationsManager = koinInject()

    LaunchedEffect(Unit) { appNotificationsManager.clearAllDisplayedNotifications(context) }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val notificationPermissionState = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
        LaunchedEffect(Unit) {
            if (!notificationPermissionState.status.isGranted) {
                notificationPermissionState.launchPermissionRequest()
            }
        }
    }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            onErrorMessage(it)
            viewModel.clearErrorMessage()
        }
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(50.sdp)
                .background(
                    AppTheme.colors.onBackground,
                    shape = RoundedCornerShape(topEnd = 15.sdp, bottomEnd = 15.sdp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(9.sdp))

            IconButtonComponent(
                iconId = R.drawable.ic_messages,
                isClickable = false
            )

            Spacer(Modifier.height(7.sdp))

            LazyColumn(Modifier.weight(1f)) {
                items(recentConversationsState.itemCount) {
                    recentConversationsState[it]?.let { data ->
                        RecentConversationItemList(data) { userData ->
                            navController.navigate(Chat(userData.encodeToString()))
                        }

                        Spacer(Modifier.height(5.sdp))
                    }
                }
            }

            Spacer(Modifier.height(7.sdp))

            IconButtonComponent(
                iconId = R.drawable.ic_add,
                iconColor = AppTheme.colors.complementary
            ) { navController.navigate(Network) }

            Spacer(Modifier.height(6.sdp))

            state.profileImage?.let {
                Image(
                    painter = BitmapPainter(it.asImageBitmap()),
                    contentDescription = stringResource(R.string.profile_image),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(35.sdp)
                        .clip(RoundedCornerShape(8.sdp))
                )
            }

            Spacer(Modifier.height(9.sdp))
        }

        HomeNavHost(
            navHostController = navController,
            modifier = Modifier.fillMaxSize(),
            onBack = { navController.popBackStack() },
            onNavigateToChat = { navController.navigate(Chat(it.encodeToString())) },
            onErrorMessage = { onErrorMessage(it) }
        )

        HomePlaceholder()

    }
}