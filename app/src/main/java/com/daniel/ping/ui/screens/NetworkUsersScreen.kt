package com.daniel.ping.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniel.ping.R
import com.daniel.ping.domain.utilities.ImageConverter
import com.daniel.ping.ui.components.MaterialSearchComponent
import com.daniel.ping.ui.components.lazyComponents.UserItem
import com.daniel.ping.ui.navigation.ScreenRoutes
import com.daniel.ping.ui.navigation.navigateExt
import com.daniel.ping.ui.theme.RangoonGreen
import com.daniel.ping.ui.theme.UltramarineBlue
import com.daniel.ping.ui.theme.White
import com.daniel.ping.ui.viewModels.NetworkUsersViewModel

@Composable
fun NetworkUsersScreen(
    navController: NavController,
    viewModel: NetworkUsersViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    val textState = remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(modifier = Modifier.fillMaxHeight()) {
        Column(modifier = Modifier.padding(it)) {

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(RangoonGreen)
            ) {
                val (backScreen, titleScreen) = createRefs()

                IconButton(
                    onClick = {
                        navController.navigate(ScreenRoutes.Home.route) {
                            popUpTo(
                                ScreenRoutes.Home.route
                            )
                        }
                    },
                    modifier = Modifier
                        .size(23.dp)
                        .constrainAs(backScreen) {
                            top.linkTo(parent.top, margin = 10.dp)
                            start.linkTo(parent.start, margin = 10.dp)
                        }
                ) {
                    Icon(
                        painterResource(id = R.drawable.ic_back),
                        contentDescription = "Back Screen",
                        tint = White
                    )
                }

                Text(
                    stringResource(id = R.string.network),
                    color = White,
                    fontFamily = FontFamily(Font(R.font.roboto)),
                    fontSize = 16.sp,
                    modifier = Modifier
                        .constrainAs(titleScreen) {
                            top.linkTo(parent.top, margin = 10.dp)
                            start.linkTo(backScreen.end, margin = 5.dp)
                        }
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            MaterialSearchComponent(
                state = textState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
                    .padding(start = 10.dp, end = 10.dp)
            )

            val users = state.allUsers.filter { search ->
                search.name.contains(
                    textState.value.text,
                    ignoreCase = true
                )
            }

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(users) { user ->
                    UserItem(
                        profileImage = ImageConverter.decodeFromString(user.profileImage),
                        username = user.name,
                        description = user.description
                    ) {
                        navController.navigateExt(ScreenRoutes.Chat.route, "userDetails" to user)
                    }
                }
            }

            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        color = UltramarineBlue,
                        modifier = Modifier.size(25.dp),
                        strokeWidth = 3.dp
                    )
                }
            }
        }
    }
}