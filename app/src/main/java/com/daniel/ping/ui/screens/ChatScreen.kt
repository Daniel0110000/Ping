package com.daniel.ping.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
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
import androidx.documentfile.provider.DocumentFile
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daniel.ping.R
import com.daniel.ping.domain.models.Chat
import com.daniel.ping.domain.models.User
import com.daniel.ping.domain.utilities.Constants
import com.daniel.ping.domain.utilities.ImageConverter
import com.daniel.ping.domain.utilities.downloadFile
import com.daniel.ping.ui.components.FileOrMP3PreviewComponent
import com.daniel.ping.ui.components.ImagePreviewComponent
import com.daniel.ping.ui.components.MoreOptionsComponent
import com.daniel.ping.ui.components.lazyComponents.ReceivedMessageItem
import com.daniel.ping.ui.components.lazyComponents.ReceivedMessageWithFileOrMP3Item
import com.daniel.ping.ui.components.lazyComponents.ReceivedMessageWithImageItem
import com.daniel.ping.ui.components.lazyComponents.SentMessageItem
import com.daniel.ping.ui.components.lazyComponents.SentMessageWithFileOrMP3Item
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
    var filePreview by remember { mutableStateOf<Uri?>(null) }
    var mp3Preview by remember { mutableStateOf<Uri?>(null) }
    var fileName by remember { mutableStateOf("") }
    var fileSize by remember { mutableStateOf("") }
    var mp3Name by remember { mutableStateOf("") }
    var mp3Size by remember { mutableStateOf("") }

    // A launcher for the "GetContent" ActivityResult contract. Launches the gallery app to allow the user to select an image
    // When the user selects an image, the URI of the image is saved in the "imagePreview" state variable
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { result: Uri? ->
            result?.let { imagePreview = result }
    }

    val fileLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()){ file: Uri? ->
        file?.let { fileUri ->
            filePreview = fileUri
            val documentFile = DocumentFile.fromSingleUri(context, file)
            documentFile?.let { document ->
                fileName = document.name.toString()
                fileSize = (document.length() / 1024).toString()
            }
        }
    }
    
    val mp3Launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()){ result: Uri? ->
        result?.let { audioUri ->
            mp3Preview = audioUri
            val documentFile = DocumentFile.fromSingleUri(context, result)
            documentFile?.let { audio ->
                mp3Name = audio.name.toString()
                mp3Size = (audio.length() / 1024).toString()
            }
        }
    }

    Scaffold { paddingValues ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            val (backScreen, profileImage, username, onlineIndicator, containerDescription,
                lazyMessages, buttonAddFiles, inputMessage, sendMessage, moreOptionsContainer,
                imagePreviewContainer, filePreviewContainer) = createRefs()

            var message by remember { mutableStateOf("") }
            val isOnline by viewModel.isOnline.observeAsState()
            val isLoading by viewModel.isLoading.observeAsState()
            val isLoadingUploadFile by viewModel.isLoadingUploadFile.observeAsState()
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

            AsyncImage(
                model = userDetails.profileImageUrl,
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
                        val receivedProfileImageUrl = userDetails.profileImageUrl
                        LazyColumn(state = lazyListState) {

                            chats.sortWith { obj1, obj2 -> obj1.dateObject.compareTo(obj2.dateObject) }

                            items(chats) { message ->
                                if (message.senderId == viewModel.userId) {
                                    when (message.messageType) {
                                        Constants.MESSAGE_TYPE_TEXT -> {
                                            SentMessageItem(
                                                message = message.message,
                                                date = message.dateTime
                                            )
                                        }
                                        Constants.MESSAGE_TYPE_IMAGE -> {
                                            SentMessageWithImageItem(
                                                imageUrl = message.imageUrl,
                                                message = message.message,
                                                date = message.dateTime
                                            )
                                        }
                                        Constants.MESSAGE_TYPE_FILE -> {
                                            SentMessageWithFileOrMP3Item(
                                                FMNameText = message.fileDetails[Constants.KEY_FILE_NAME].toString(),
                                                FMSizeText = message.fileDetails[Constants.KEY_FILE_SIZE].toString(),
                                                message = message.message,
                                                date = message.dateTime
                                            )
                                        }
                                        Constants.MESSAGE_TYPE_MP3 -> {
                                            SentMessageWithFileOrMP3Item(
                                                FMNameText = message.mp3Details[Constants.KEY_MP3_NAME].toString(),
                                                FMSizeText = message.mp3Details[Constants.KEY_MP3_SIZE].toString(),
                                                message = message.message,
                                                isMp3 = true,
                                                date = message.dateTime
                                            )
                                        }
                                    }
                                } else {
                                    when (message.messageType) {
                                        Constants.MESSAGE_TYPE_TEXT -> {
                                            ReceivedMessageItem(
                                                profileImageUrl = receivedProfileImageUrl,
                                                message = message.message,
                                                date = message.dateTime
                                            )
                                        }
                                        Constants.MESSAGE_TYPE_IMAGE -> {
                                            ReceivedMessageWithImageItem(
                                                imageUrl = message.imageUrl,
                                                profileImageUrl = receivedProfileImageUrl,
                                                message = message.message,
                                                date = message.dateTime
                                            )
                                        }
                                        Constants.MESSAGE_TYPE_FILE -> {
                                            ReceivedMessageWithFileOrMP3Item(
                                                profileImageUrl = receivedProfileImageUrl,
                                                FMNameText = message.fileDetails[Constants.KEY_FILE_NAME].toString(),
                                                FMSizeText = message.fileDetails[Constants.KEY_FILE_SIZE].toString(),
                                                message = message.message,
                                                date = message.dateTime,
                                                downloadListener = {
                                                    downloadFile(
                                                        urlFile = message.fileDetails[Constants.KEY_FILE_URL].toString(),
                                                        fileName = message.fileDetails[Constants.KEY_FILE_NAME].toString(),
                                                        fileSize = message.fileDetails[Constants.KEY_FILE_SIZE].toString(),
                                                        context = context
                                                    )
                                                }
                                            )
                                        }
                                        Constants.MESSAGE_TYPE_MP3 -> {
                                            ReceivedMessageWithFileOrMP3Item(
                                                profileImageUrl = receivedProfileImageUrl,
                                                FMNameText = message.mp3Details[Constants.KEY_MP3_NAME].toString(),
                                                FMSizeText = message.mp3Details[Constants.KEY_MP3_SIZE].toString(),
                                                message = message.message,
                                                date = message.dateTime,
                                                downloadListener = {
                                                }
                                            )
                                        }
                                    }
                                }

                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(
                                model = userDetails.profileImageUrl,
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
                    openFiles = {
                        val excludedMimeTypes = arrayOf("image/*", "video/*")
                        val intent = Intent(Intent.ACTION_GET_CONTENT)
                        intent.type = "*/*"
                        intent.putExtra(Intent.EXTRA_MIME_TYPES, excludedMimeTypes)
                        fileLauncher.launch("application/*")
                    },
                    openFilesForChooseMusic = { mp3Launcher.launch("audio/mpeg") }
                )
            }

            if (imagePreview != null) {
                showMoreOptionsContainer = false
                ImagePreviewComponent(
                    imagePreview = ImageConverter.uriToBitmap(context, imagePreview!!),
                    modifier = Modifier
                        .constrainAs(imagePreviewContainer){
                            width = Dimension.fillToConstraints
                            start.linkTo(parent.start, margin = 10.dp)
                            end.linkTo(parent.end, margin = 10.dp)
                            bottom.linkTo(inputMessage.top, margin = 10.dp)
                        }
                ) { imagePreview = null }
            }

            if(filePreview != null){
                showMoreOptionsContainer = false
                FileOrMP3PreviewComponent(
                    modifier = Modifier.constrainAs(filePreviewContainer){
                        start.linkTo(parent.start, margin = 10.dp)
                        bottom.linkTo(inputMessage.top, margin = 10.dp)
                    },
                    FMName = fileName,
                    FMSize = fileSize
                ) { filePreview = null }
            }

            if (mp3Preview != null){
                showMoreOptionsContainer = false
                FileOrMP3PreviewComponent(
                    modifier = Modifier.constrainAs(filePreviewContainer){
                        start.linkTo(parent.start, margin = 10.dp)
                        bottom.linkTo(inputMessage.top, margin = 10.dp)
                    },
                    FMName = mp3Name,
                    FMSize = mp3Size,
                    isMP3 = true
                ) { mp3Preview = null }
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
                        if (imagePreview != null) {
                            viewModel.setMessageImage(imagePreview)
                            imagePreview = null
                        }
                        if (filePreview != null) {
                            viewModel.setMessageFile(filePreview)
                            viewModel.setFileDetails(fileName, fileSize)
                            filePreview = null
                        }
                        if(mp3Preview != null) {
                            viewModel.setMessageMP3(mp3Preview)
                            viewModel.setMP3Details(mp3Name, mp3Size)
                            mp3Preview = null
                        }
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
                if(isLoadingUploadFile == true){
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