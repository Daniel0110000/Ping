package com.daniel.ping.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.daniel.ping.ui.navigation.ScreenRoutes
import com.daniel.ping.ui.navigation.SetupNavGraph
import com.daniel.ping.ui.theme.Onyx
import com.daniel.ping.ui.theme.RangoonGreen
import com.daniel.ping.ui.theme.UltramarineBlue
import com.daniel.ping.ui.theme.White

@Composable
fun MainScreen(
    navController: NavHostController
) {
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

                val (fabNewConversation) = createRefs()

                FloatingActionButton(
                    onClick = {
                         navController.navigate(route = ScreenRoutes.NetworkUsers.route)
                    },
                    backgroundColor = UltramarineBlue,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .size(40.dp)
                        .constrainAs(fabNewConversation){
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