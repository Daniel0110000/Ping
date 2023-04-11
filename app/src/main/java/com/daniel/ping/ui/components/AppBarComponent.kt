package com.daniel.ping.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.daniel.ping.R
import com.daniel.ping.ui.theme.RangoonGreen

@Composable
fun AppBarComponent() {
    TopAppBar(
        title = {
            ConstraintLayout(
                modifier = Modifier.fillMaxSize()
            ) {

                val (iconApp) = createRefs()

                Icon(
                    painter = painterResource(id = R.drawable.ic_app),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier
                        .size(30.dp)
                        .constrainAs(iconApp){
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    tint = Color(0xFF4C4CFF)
                )

            }
        },
        backgroundColor = RangoonGreen
    )
}