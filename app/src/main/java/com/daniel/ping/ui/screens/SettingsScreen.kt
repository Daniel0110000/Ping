package com.daniel.ping.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.daniel.ping.R
import com.daniel.ping.ui.theme.LimeGreen
import com.daniel.ping.ui.theme.OnyxTransparent
import com.daniel.ping.ui.theme.SilverFoil
import com.daniel.ping.ui.theme.White
import com.daniel.ping.ui.viewModels.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {

    // Collects the current state of the viewModel using kotlin coroutines
    val state = viewModel.state.collectAsState()

    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        ConstraintLayout(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            val (profileImage, filterImage, backScreen, titleScreen, userDetailsContainer) = createRefs()

            AsyncImage(
                model = state.value.profileImage,
                contentDescription = "Profile Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(110.dp)
                    .clip(RoundedCornerShape(bottomEnd = 15.dp, bottomStart = 15.dp))
                    .constrainAs(profileImage) {
                        width = Dimension.fillToConstraints
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    }
            )

            Box(
                modifier = Modifier
                    .background(OnyxTransparent, RoundedCornerShape(bottomEnd = 15.dp, bottomStart = 15.dp))
                    .constrainAs(filterImage){
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                        top.linkTo(profileImage.top)
                        bottom.linkTo(profileImage.bottom)
                        start.linkTo(profileImage.start)
                        end.linkTo(profileImage.end)
                    }
            )

            IconButton(
                onClick = {},
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
                stringResource(id = R.string.settings),
                color = White,
                fontFamily = FontFamily(Font(R.font.roboto)),
                fontSize = 16.sp,
                modifier = Modifier
                    .constrainAs(titleScreen) {
                        top.linkTo(parent.top, margin = 10.dp)
                        start.linkTo(backScreen.end, margin = 5.dp)
                    }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .constrainAs(userDetailsContainer) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(profileImage.bottom)
                    }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(LimeGreen, CircleShape)
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Text(
                        text = state.value.name,
                        color = White,
                        fontFamily = FontFamily(Font(R.font.roboto)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Text(
                    text = state.value.description,
                    color = SilverFoil,
                    fontFamily = FontFamily(Font(R.font.roboto)),
                    fontSize = 14.sp
                )

            }

        }
    }
}