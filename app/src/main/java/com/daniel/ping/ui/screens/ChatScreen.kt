package com.daniel.ping.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
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
import androidx.compose.ui.platform.LocalContext
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
import androidx.compose.ui.text.style.TextAlign
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
import com.daniel.ping.ui.components.FilePreviewComponent
import com.daniel.ping.ui.components.MoreOptionsComponent
import com.daniel.ping.ui.components.lazyComponents.ReceivedMessageItem
import com.daniel.ping.ui.components.lazyComponents.ReceivedMessageWithImageItem
import com.daniel.ping.ui.components.lazyComponents.SentMessageItem
import com.daniel.ping.ui.components.lazyComponents.SentMessageWithImageItem
import com.daniel.ping.ui.theme.LimeGreen
import com.daniel.ping.ui.theme.Onyx
import com.daniel.ping.ui.theme.OnyxTransparent
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

    // Application context
    val context = LocalContext.current

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

                // Scroll to the last message
                scope.launch {
                    lazyListState.scrollToItem(chats.lastIndex)
                }

                if (!viewModel.hasCheckedForConversation) viewModel.checkForConversation()

            }
        }

        viewModel.allMessages.observe(updateLifecycleOwner.value, observer)

        onDispose {
            viewModel.allMessages.removeObserver(observer)
        }
    }

    // Set the receiver user details and listen to messages
    LaunchedEffect(Unit) {
        viewModel.setReceiverUser(userDetails)
        viewModel.availabilityUser()
        viewModel.listenerMessages()
    }

    // A mutable state variable that holds the Uri of the selected image for previewing
    var imagePreview by remember { mutableStateOf<Uri?>(null) }

    // A launcher for the "GetContent" ActivityResult contract. Launches the gallery app to allow the user to select an image
    // When the user selects an image, the URI of the image is saved in the "imagePreview" state variable
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { result: Uri? ->
            result?.let { imagePreview = result }
    }

    Scaffold { paddingValues ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            val (backScreen, profileImage, username, onlineIndicator, containerDescription,
                lazyMessages, buttonAddFiles, inputMessage, sendMessage, moreOptionsContainer,
                filePreviewContainer) = createRefs()

            var message by remember { mutableStateOf("") }
            val isOnline by viewModel.isOnline.observeAsState()
            val isLoading by viewModel.isLoading.observeAsState()
            val isLoadingImage by viewModel.isLoadingImage.observeAsState()
            var showMoreOptionsContainer by remember { mutableStateOf(false) }

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
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(10.dp)
                        .background(LimeGreen)
                )
            }

            Box(
                modifier = Modifier.constrainAs(lazyMessages) {
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                    top.linkTo(profileImage.bottom, margin = 15.dp)
                    bottom.linkTo(inputMessage.top, margin = 10.dp)
                    start.linkTo(parent.start, margin = 5.dp)
                    end.linkTo(parent.end, margin = 5.dp)
                }
            ) {
                if (isLoading == true) {
                    CircularProgressIndicator(
                        color = UltramarineBlue,
                        modifier = Modifier
                            .size(25.dp)
                            .align(Alignment.Center),
                        strokeWidth = 3.dp,
                    )
                } else {
                    if (chats.isNotEmpty()) {
                        val receivedProfileImage = ImageConverter.decodeFromString(userDetails.profileImage)
                        LazyColumn(state = lazyListState) {

                            chats.sortWith { obj1, obj2 -> obj1.dateObject.compareTo(obj2.dateObject) }

                            items(chats) { message ->
                                if (message.senderId == viewModel.userId) {
                                    if (message.messageType == "text") {
                                        SentMessageItem(
                                            message = message.message,
                                            date = message.dateTime
                                        )
                                    } else {
                                        SentMessageWithImageItem(
                                            imageUrl = message.imageUrl,
                                            message = message.message,
                                            date = message.dateTime
                                        )
                                    }
                                } else {
                                    if (message.messageType == "text") {
                                        ReceivedMessageItem(
                                            profileImage = receivedProfileImage,
                                            message = message.message,
                                            date = message.dateTime
                                        )
                                    } else {
                                        ReceivedMessageWithImageItem(
                                            imageUrl = message.imageUrl,
                                            profileImage = receivedProfileImage,
                                            message = message.message,
                                            date = message.dateTime
                                        )
                                    }
                                }

                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                bitmap = ImageConverter.decodeFromString(userDetails.profileImage).asImageBitmap(),
                                contentDescription = userDetails.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(95.dp)
                                    .clip(CircleShape)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = userDetails.name,
                                color = White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily(Font(R.font.roboto)),
                                textAlign = TextAlign.Center,
                                maxLines = 1
                            )

                            Text(
                                text = userDetails.description,
                                color = SilverFoil,
                                fontSize = 11.sp,
                                fontFamily = FontFamily(Font(R.font.roboto)),
                                textAlign = TextAlign.Center,
                                maxLines = 3
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(OnyxTransparent)
                    .constrainAs(containerDescription) {
                        top.linkTo(profileImage.bottom, margin = 10.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = userDetails.description,
                    textAlign = TextAlign.Center,
                    color = White,
                    fontSize = 13.sp,
                    maxLines = 2,
                    fontFamily = FontFamily(Font(R.font.roboto)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                )
            }

            if (showMoreOptionsContainer) {
                MoreOptionsComponent(
                    modifier = Modifier
                        .constrainAs(moreOptionsContainer){
                            start.linkTo(parent.start, margin = 10.dp)
                            bottom.linkTo(buttonAddFiles.top, margin = 10.dp)
                        },
                    openGallery = { galleryLauncher.launch("image/*") },
                )
            }

            if (imagePreview != null) {
                showMoreOptionsContainer = false
                FilePreviewComponent(
                    imagePreview = ImageConverter.uriToBitmap(context, imagePreview!!),
                    modifier = Modifier
                        .constrainAs(filePreviewContainer){
                            width = Dimension.fillToConstraints
                            start.linkTo(parent.start, margin = 10.dp)
                            end.linkTo(parent.end, margin = 10.dp)
                            bottom.linkTo(inputMessage.top, margin = 10.dp)
                        }
                ) { imagePreview = null }
            }

            Card(
                modifier = Modifier
                    .size(45.dp)
                    .clickable { showMoreOptionsContainer = !showMoreOptionsContainer }
                    .constrainAs(buttonAddFiles) {
                        bottom.linkTo(parent.bottom, margin = 10.dp)
                        start.linkTo(parent.start, margin = 10.dp)
                    },
                shape = RoundedCornerShape(10.dp),
                backgroundColor = Onyx
            ) {
                Icon(
                    imageVector = if (!showMoreOptionsContainer) Icons.Rounded.Add else Icons.Rounded.Close,
                    contentDescription = "Add file",
                    tint = UltramarineBlue,
                    modifier = Modifier.scale(0.5f)
                )
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
                        start.linkTo(buttonAddFiles.end, margin = 10.dp)
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
                        if (imagePreview != null) viewModel.setMessageImage(imagePreview)
                        viewModel.sendMessage()
                        message = ""
                        imagePreview = null
                    }
                    .constrainAs(sendMessage) {
                        bottom.linkTo(parent.bottom, margin = 10.dp)
                        end.linkTo(parent.end, margin = 10.dp)
                    },
                shape = RoundedCornerShape(10.dp),
                backgroundColor = Onyx,
            ) {
                if(isLoadingImage == true){
                    CircularProgressIndicator(
                        color = UltramarineBlue,
                        strokeWidth = 5.dp,
                        modifier = Modifier.scale(0.5f)
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_send),
                        contentDescription = "Send message",
                        tint = UltramarineBlue,
                        modifier = Modifier.scale(0.5f)
                    )
                }
            }

        }
    }
}