package com.daniel.ping.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Observer
import androidx.navigation.NavHostController
import com.daniel.ping.R
import com.daniel.ping.domain.models.RecentConversation
import com.daniel.ping.domain.models.User
import com.daniel.ping.domain.utilities.ImageConverter
import com.daniel.ping.ui.components.lazyComponents.RecentMessageItem
import com.daniel.ping.ui.navigation.ScreenRoutes
import com.daniel.ping.ui.navigation.SetupNavGraph
import com.daniel.ping.ui.navigation.navigateExt
import com.daniel.ping.ui.theme.Onyx
import com.daniel.ping.ui.theme.RangoonGreen
import com.daniel.ping.ui.theme.UltramarineBlue
import com.daniel.ping.ui.theme.White
import com.daniel.ping.ui.viewModels.MainViewModel

@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainViewModel = hiltViewModel()
) {

    // Maintain a list of recent conversations
    var recentConversations = remember { mutableStateListOf<RecentConversation>() }

    // Collects the current state of the viewModel using kotlin coroutines
    val state = viewModel.state.collectAsState()

    // Maintain the state of the lazy list
    val lifecycleOwner = LocalLifecycleOwner.current
    val updateLifecycleOwner = rememberUpdatedState(lifecycleOwner)

    DisposableEffect(viewModel.recentConversations){
        val observer = Observer<List<RecentConversation>> { conversations ->
            if(conversations.isNotEmpty()){
                // Adds all new conversations to the list of recent conversations
                recentConversations.addAll(conversations)
                // Sorts the list of recent conversations by date in descending order
                recentConversations.sortWith { obj1, obj2 -> obj2.dateObject.compareTo(obj1.dateObject) }
                // Removes any duplicate conversations from the list
                recentConversations = recentConversations.distinctBy { conversation -> conversation.senderId + conversation.receiverId}.toMutableStateList()
            }
        }

        // Observes changes to the list of recent conversations and updates the UI accordingly
        viewModel.recentConversations.observe(updateLifecycleOwner.value, observer)

        // Removes the observer when the composable is disposed to avoid leaks
        onDispose {
            viewModel.recentConversations.removeObserver(observer)
        }

    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(RangoonGreen)
    ) {

        val (leftContainer, screensContainer) = createRefs()

        Card(
            modifier = Modifier
                .width(120.dp)
                .fillMaxHeight()
                .constrainAs(leftContainer) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                },
            backgroundColor = Onyx,
            shape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp)
        ) {
            ConstraintLayout(modifier = Modifier.fillMaxSize()) {

                val (profileImage, myName, fabNewConversation, recentMessagesTitle, recentMessagesDivider, recentMessages) = createRefs()

                state.value.profileImage?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = state.value.name,
                        modifier = Modifier
                            .size(45.dp)
                            .clip(CircleShape)
                            .constrainAs(profileImage) {
                                top.linkTo(parent.top, margin = 15.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                        contentScale = ContentScale.Crop
                    )
                }

                Text(
                    text = state.value.name,
                    fontFamily = FontFamily(Font(R.font.roboto)),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    color = White,
                    modifier = Modifier
                        .constrainAs(myName){
                            width = Dimension.fillToConstraints
                            top.linkTo(profileImage.bottom, margin = 5.dp)
                            start.linkTo(parent.start, margin = 2.dp)
                            end.linkTo(parent.end, margin = 2.dp)
                        }

                )

                Text(
                    text = stringResource(id = R.string.message),
                    fontSize = 14.sp,
                    color = White,
                    fontFamily = FontFamily(Font(R.font.roboto)),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .constrainAs(recentMessagesTitle){
                            top.linkTo(myName.bottom, margin = 20.dp)
                            start.linkTo(parent.start, margin = 5.dp)
                        }
                )

                Box(
                    modifier = Modifier
                        .height(2.dp)
                        .background(RangoonGreen)
                        .constrainAs(recentMessagesDivider) {
                            width = Dimension.fillToConstraints
                            start.linkTo(parent.start, margin = 5.dp)
                            end.linkTo(parent.end, margin = 5.dp)
                            top.linkTo(recentMessagesTitle.bottom, margin = 3.dp)
                        }
                )

                LazyColumn(
                    modifier = Modifier
                        .constrainAs(recentMessages){
                            height = Dimension.fillToConstraints
                            top.linkTo(recentMessagesDivider.bottom)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                ){
                    items(recentConversations){ conversation ->
                        RecentMessageItem(image = ImageConverter.decodeFromString(conversation.profileImage)){
                            val userDetails = User(
                                id = conversation.receiverId,
                                profileImage = conversation.profileImage,
                                name = conversation.name,
                                description = conversation.description,
                                token = conversation.token
                            )

                            navController.navigateExt(ScreenRoutes.Chat.route, "userDetails" to userDetails)

                        }
                    }
                }

                FloatingActionButton(
                    onClick = {
                         navController.navigate(route = ScreenRoutes.NetworkUsers.route)
                    },
                    backgroundColor = UltramarineBlue,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .size(40.dp)
                        .constrainAs(fabNewConversation) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom, margin = 20.dp)
                        }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "New conversation",
                        tint = White
                    )
                }

            }
        }

        SetupNavGraph(
            navController = navController,
            modifier = Modifier
                .fillMaxHeight()
                .constrainAs(screensContainer) {
                    width = Dimension.fillToConstraints
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(leftContainer.end)
                    end.linkTo(parent.end)
                }
        )

    }
}