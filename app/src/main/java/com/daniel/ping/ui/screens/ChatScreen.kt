package com.daniel.ping.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.daniel.ping.R
import com.daniel.ping.domain.models.Chat
import com.daniel.ping.domain.models.User
import com.daniel.ping.domain.utilities.ImageConverter
import com.daniel.ping.ui.components.lazyComponents.ReceivedMessageItem
import com.daniel.ping.ui.components.lazyComponents.SentMessageItem
import com.daniel.ping.ui.theme.LimeGreen
import com.daniel.ping.ui.theme.Onyx
import com.daniel.ping.ui.theme.SilverFoil
import com.daniel.ping.ui.theme.UltramarineBlue
import com.daniel.ping.ui.theme.White
import com.daniel.ping.ui.viewModels.ChatViewModel
import kotlinx.coroutines.launch

/**
 * Composable function that displays the chat screen UI
 * @param navController the navController object used for navigation
 * @param userDetails the user details of the chat recipient
 * @param viewModel that ChatViewModel object used to retrieve and message chat data
 */
@Composable
fun ChatScreen(
    navController: NavController,
    userDetails: User,
    viewModel: ChatViewModel = hiltViewModel()
) {

    // Maintain a list of chats
    val chats = remember { mutableStateListOf<Chat>() }

    // Obtain the lifecycle owner
    val lifecycleOwner = LocalLifecycleOwner.current
    val updateLifecycleOwner = rememberUpdatedState(lifecycleOwner)

    // Maintain the state of the lazy list
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // Observe the list of all messages and update the UI accordingly
    DisposableEffect(viewModel.allMessages) {
        val observer = Observer<List<Chat>> { messages ->
            if (messages.isNotEmpty()) {
                chats.addAll(messages)
                chats.sortWith { obj1, obj2 -> obj1.dateObject.compareTo(obj2.dateObject) }

                // Scroll to the last message
                scope.launch {
                    lazyListState.scrollToItem(chats.lastIndex)
                }

            }
        }

        viewModel.allMessages.observe(updateLifecycleOwner.value, observer)

        onDispose {
            viewModel.allMessages.removeObserver(observer)
        }
    }

    // Set the receiver user details and listen to messages
    LaunchedEffect(true) {
        viewModel.setReceiverUser(userDetails)
        viewModel.availabilityUser()
        viewModel.listenerMessages()
    }

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {

        val (backScreen, profileImage, username, onlineIndicator, lazyMessages, inputMessage, sendMessage) = createRefs()

        var message by remember { mutableStateOf("") }
        val isOnline by viewModel.isOnline.observeAsState()
        val isLoading by viewModel.isLoading.observeAsState()

        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .size(22.dp)
                .constrainAs(backScreen) {
                    start.linkTo(parent.start, margin = 10.dp)
                    top.linkTo(profileImage.top)
                    bottom.linkTo(profileImage.bottom)
                }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back Screen",
                tint = White
            )
        }

        Image(
            bitmap = ImageConverter.decodeFromString(userDetails.profileImage).asImageBitmap(),
            contentDescription = userDetails.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(35.dp)
                .clip(CircleShape)
                .constrainAs(profileImage) {
                    top.linkTo(parent.top, margin = 10.dp)
                    start.linkTo(backScreen.end, margin = 5.dp)
                }
        )

        Text(
            text = userDetails.name,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.roboto)),
            maxLines = 1,
            color = White,
            modifier = Modifier
                .constrainAs(username) {
                    width = Dimension.fillToConstraints
                    top.linkTo(profileImage.top)
                    bottom.linkTo(profileImage.bottom)
                    start.linkTo(profileImage.end, margin = 6.dp)
                    end.linkTo(onlineIndicator.start, margin = 5.dp)
                }
        )

        // Design to display if the user is online
        AnimatedVisibility(
            visible = isOnline!!,
            modifier = Modifier
                .constrainAs(onlineIndicator) {
                    top.linkTo(parent.top, margin = 15.dp)
                    end.linkTo(parent.end, margin = 15.dp)
                }
        ) {
            Box(modifier = Modifier
                .clip(CircleShape)
                .size(10.dp)
                .background(LimeGreen)
            )
        }

        // Modifier to position the lazy column that displays the chat messages
        val contentModifier = Modifier
            .constrainAs(lazyMessages) {
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
                top.linkTo(profileImage.bottom, margin = 15.dp)
                bottom.linkTo(inputMessage.top, margin = 10.dp)
                start.linkTo(parent.start, margin = 5.dp)
                end.linkTo(parent.end, margin = 5.dp)
            }

        // Displays a loading spinner while the chat messages are being retrieved
        // Once the messages are loaded, displays the LazyColumn with the messages
        AnimatedVisibility(visible = isLoading!!, modifier = contentModifier) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            ) {
                CircularProgressIndicator(
                    color = UltramarineBlue,
                    modifier = Modifier.size(25.dp),
                    strokeWidth = 3.dp
                )
            }
        }

        AnimatedVisibility(visible = !isLoading!!, modifier = contentModifier) {
            LazyColumn(state = lazyListState) {
                items(chats) { message ->
                    if (message.senderId == viewModel.userId) {
                        SentMessageItem(
                            message = message.message,
                            date = message.dateTime
                        )
                    } else {
                        ReceivedMessageItem(
                            profileImage = ImageConverter.decodeFromString(userDetails.profileImage),
                            message = message.message,
                            date = message.dateTime
                        )
                    }
                }
            }
        }

        TextField(
            value = message,
            onValueChange = { v -> message = v },
            textStyle = TextStyle(color = White, fontSize = 11.sp),
            modifier = Modifier
                .heightIn(45.dp)
                .constrainAs(inputMessage) {
                    width = Dimension.fillToConstraints
                    bottom.linkTo(parent.bottom, margin = 10.dp)
                    start.linkTo(parent.start, margin = 10.dp)
                    end.linkTo(sendMessage.start, margin = 10.dp)
                },
            placeholder = {
                Text(
                    text = stringResource(id = R.string.message),
                    color = SilverFoil,
                    fontSize = 11.sp
                )
            },
            maxLines = 5,
            visualTransformation = VisualTransformation.None,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            shape = RoundedCornerShape(15.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Onyx,
                cursorColor = UltramarineBlue,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Card(
            modifier = Modifier
                .size(45.dp)
                .clickable {
                    viewModel.setMessageText(message)
                    viewModel.sendMessage()
                    message = ""
                }
                .constrainAs(sendMessage) {
                    bottom.linkTo(parent.bottom, margin = 10.dp)
                    end.linkTo(parent.end, margin = 10.dp)
                },
            shape = RoundedCornerShape(10.dp),
            backgroundColor = Onyx,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_send),
                contentDescription = "Send message",
                tint = UltramarineBlue,
                modifier = Modifier.scale(0.6f)
            )
        }

    }
}