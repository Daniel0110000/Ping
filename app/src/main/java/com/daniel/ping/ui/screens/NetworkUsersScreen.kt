package com.daniel.ping.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
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
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniel.ping.R
import com.daniel.ping.domain.utilities.ImageConverter
import com.daniel.ping.ui.components.MaterialSearchComponent
import com.daniel.ping.ui.components.lazyComponents.UserItem
import com.daniel.ping.ui.navigation.ScreenRoutes
import com.daniel.ping.ui.theme.UltramarineBlue
import com.daniel.ping.ui.theme.White
import com.daniel.ping.ui.viewModels.NetworkUsersViewModel

@Composable
fun NetworkUsersScreen(
    navController: NavController,
    viewModel: NetworkUsersViewModel = hiltViewModel()
) {

    // Collects the current state of the viewModel using kotlin coroutines
    val state by viewModel.state.collectAsState()

    // Remembers the state of the TextField
    val textState = remember { mutableStateOf(TextFieldValue("")) }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {

        val (backScreen, titleScreen, searchInput, lazyUsers) = createRefs()

        // Retrieves the text value of the search TextField
        val searchedText = textState.value.text

        IconButton(
            onClick = {
                navController.navigate(ScreenRoutes.Home.route) {
                    popUpTo(ScreenRoutes.Home.route) {
                        inclusive = true
                    }
                }
            },
            modifier = Modifier
                .size(23.dp)
                .constrainAs(backScreen) {
                    top.linkTo(parent.top, margin = 10.dp)
                    start.linkTo(parent.start, margin = 10.dp)
                },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back Screen",
                tint = White
            )
        }

        Text(
            text = stringResource(id = R.string.network),
            color = White,
            fontFamily = FontFamily(Font(R.font.roboto)),
            fontSize = 16.sp,
            modifier = Modifier
                .constrainAs(titleScreen) {
                    top.linkTo(parent.top, margin = 10.dp)
                    start.linkTo(backScreen.end, margin = 5.dp)
                }
        )

        MaterialSearchComponent(
            state = textState,
            modifier = Modifier
                .heightIn(45.dp)
                .constrainAs(searchInput) {
                    width = Dimension.fillToConstraints
                    top.linkTo(backScreen.bottom, margin = 13.dp)
                    start.linkTo(parent.start, margin = 10.dp)
                    end.linkTo(parent.end, margin = 10.dp)
                }
        )

        val contentModifier = Modifier
            .padding(top = 10.dp)
            .constrainAs(lazyUsers) {
                // Set constraints for the LazyColumn using ConstrainLayout
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
                top.linkTo(searchInput.bottom)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }

        // Show a progress indicator if data is currently being loaded
        AnimatedVisibility(visible = state.isLoading, modifier = contentModifier) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
            ) {
                CircularProgressIndicator(
                    color = UltramarineBlue,
                    modifier = Modifier.size(25.dp),
                    strokeWidth = 3.dp
                )
            }
        }

        // Show a list of users when data has been loaded
        AnimatedVisibility(visible = !state.isLoading, modifier = contentModifier) {
            LazyColumn {

                /**
                 * Filters the list of users based on the current value of the searched text field
                 * Only users whose names contain the searched text (case-insensitive) will be displayed in the LazyColumn
                 */
                items(items = state.allUsers.filter {
                    it.name.contains(
                        searchedText,
                        ignoreCase = true
                    )
                }) { user ->
                    // Use the UserItem composable to display information about each user
                    UserItem(
                        profileImage = ImageConverter.decodeFromString(user.profileImage),
                        username = user.name,
                        description = user.description
                    )
                }
            }
        }

    }
}