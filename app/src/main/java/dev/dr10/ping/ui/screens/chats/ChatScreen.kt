package dev.dr10.ping.ui.screens.chats

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import dev.dr10.ping.R
import dev.dr10.ping.domain.models.UserProfileModel
import dev.dr10.ping.ui.screens.chats.components.EmptyChatComponent
import dev.dr10.ping.ui.screens.chats.components.ReceiverMessageItemList
import dev.dr10.ping.ui.screens.chats.components.SenderMessageItemList
import dev.dr10.ping.ui.screens.components.IconButtonComponent
import dev.dr10.ping.ui.screens.components.TextFieldComponent
import dev.dr10.ping.ui.theme.AppTheme
import dev.dr10.ping.ui.viewmodels.ChatViewModel
import network.chaintech.sdpcomposemultiplatform.sdp
import network.chaintech.sdpcomposemultiplatform.ssp
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChatScreen(
    viewModel: ChatViewModel = koinViewModel(),
    receiverData: UserProfileModel,
    onBack: () -> Unit,
    onErrorMessage: (Int) -> Unit
) {
    LaunchedEffect(Unit) { viewModel.initializeAndListenChatSession(receiverData) }
    val state = viewModel.state.collectAsState().value

    val messagesFlow = viewModel.messages.collectAsState().value
    val messagesState = messagesFlow.collectAsLazyPagingItems()
    val isLoadingMessages = messagesState.loadState.refresh is LoadState.Loading ||
            messagesState.loadState.append is LoadState.Loading

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            onErrorMessage(it)
            viewModel.clearErrorMessage()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.sdp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButtonComponent(
                iconId = R.drawable.ic_back,
                contentDescription = stringResource(R.string.back),
                background = AppTheme.colors.onBackground,
                size = 30.sdp,
                iconSize = 15.sdp
            ) {
                viewModel.stopListening()
                onBack()
            }

            Spacer(Modifier.width(4.sdp))

            AsyncImage(
                model = receiverData.profileImageUrl,
                contentDescription = receiverData.username,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(32.sdp)
                    .clip(RoundedCornerShape(6.sdp))
            )
            
            Spacer(Modifier.width(5.sdp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = receiverData.username,
                    fontFamily = AppTheme.robotoFont,
                    color = AppTheme.colors.text,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.ssp
                )

                if (state.isOnline || state.lastConnected.isNotEmpty()) {
                    Text(
                        text = if (state.isOnline) stringResource(R.string.online) else state.lastConnected,
                        fontFamily = AppTheme.robotoFont,
                        color = AppTheme.colors.textSecondary,
                        fontWeight = FontWeight.Normal,
                        fontSize = 9.ssp
                    )
                }
            }
        }

        Spacer(Modifier.height(5.sdp))

        Box(Modifier.weight(1f)) {
            if (messagesState.itemCount != 0) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    reverseLayout = true
                ) {
                    items(messagesState.itemCount) { index ->
                        Spacer(Modifier.height(20.dp))

                        messagesState[index]?.let {
                            if (it.senderId == receiverData.userId) {
                                ReceiverMessageItemList(
                                    profileImageUrl = receiverData.profileImageUrl,
                                    messageModel = it
                                )
                            } else {
                                SenderMessageItemList(it)
                            }
                        }
                    }
                }
            } else EmptyChatComponent(receiverData)

            if (isLoadingMessages) {
                CircularProgressIndicator(
                    strokeWidth = (1.8).sdp,
                    modifier = Modifier
                        .size(18.sdp)
                        .align(Alignment.TopCenter),
                    color = AppTheme.colors.complementary,
                    trackColor = AppTheme.colors.background,
                )
            }
        }

        Spacer(Modifier.height(5.sdp))

        Row(Modifier.fillMaxWidth()) {
            TextFieldComponent(
                value = state.message,
                onValueChange = { viewModel.setMessage(it) },
                placeholder = stringResource(R.string.message),
                height = 36.sdp,
                horizontalPadding = 0.dp,
                singleLine = false,
                background = AppTheme.colors.onBackground,
                modifier = Modifier.weight(1f),
                isSend = true,
                onSend = { viewModel.sendMessage() }
            )

            Spacer(Modifier.width(7.sdp))

            IconButtonComponent(
                iconId = R.drawable.ic_send,
                contentDescription = stringResource(R.string.send),
                background = AppTheme.colors.onBackground,
                iconColor = AppTheme.colors.complementary,
                size = 36.sdp,
                iconSize = 15.sdp
            ) { viewModel.sendMessage() }
        }


    }

    BackHandler {
        viewModel.stopListening()
        onBack()
    }
}