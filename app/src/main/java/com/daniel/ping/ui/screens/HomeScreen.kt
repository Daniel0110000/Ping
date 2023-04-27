package com.daniel.ping.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.daniel.ping.R
import com.daniel.ping.ui.theme.SilverFoil
import com.daniel.ping.ui.theme.White

@Composable
fun HomeScreen() {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) { paddingValues ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            val (appDescriptionContainer) = createRefs()

            Column(
                modifier = Modifier
                    .constrainAs(appDescriptionContainer){
                        width = Dimension.fillToConstraints
                        top.linkTo(parent.top, margin = 15.dp)
                        bottom.linkTo(parent.bottom, margin = 15.dp)
                        start.linkTo(parent.start, margin = 15.dp)
                        end.linkTo(parent.end, margin = 15.dp)
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(id = R.drawable.ic_app),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier.size(80.dp)
                )

                Text(
                    text = stringResource(id = R.string.app_name),
                    color = White,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.roboto)),
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = stringResource(id = R.string.app_description),
                    color = SilverFoil,
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.roboto)),
                    textAlign = TextAlign.Center
                )

            }


        }
    }
}