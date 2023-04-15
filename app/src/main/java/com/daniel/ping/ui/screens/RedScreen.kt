package com.daniel.ping.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.daniel.ping.R
import com.daniel.ping.ui.theme.Onyx
import com.daniel.ping.ui.theme.SilverFoil
import com.daniel.ping.ui.theme.UltramarineBlue
import com.daniel.ping.ui.theme.White

@Composable
fun RedScreen(
    navController: NavController
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {

        val (backScreen, titleScreen, searchInput, lazyUsers) = createRefs()

        IconButton(
            onClick = { },
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

        TextField(
            value = "",
            onValueChange = {},
            textStyle = TextStyle(color = White, fontSize = 12.sp),
            modifier = Modifier
                .height(45.dp)
                .constrainAs(searchInput) {
                    width = Dimension.fillToConstraints
                    top.linkTo(backScreen.bottom, margin = 13.dp)
                    start.linkTo(parent.start, margin = 10.dp)
                    end.linkTo(parent.end, margin = 10.dp)
                },
            placeholder = { Text(
                text = stringResource(id = R.string.search),
                color = SilverFoil,
                fontSize = 12.sp
            ) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            shape = RoundedCornerShape(15.dp),
            singleLine = true,
            maxLines = 1,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Onyx,
                cursorColor = UltramarineBlue,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            leadingIcon = { Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Search icon",
                tint = White,
                modifier = Modifier.size(18.dp)
            ) }
        )

        LazyColumn(
            modifier = Modifier
                .padding(top = 10.dp)
                .constrainAs(lazyUsers) {
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                    top.linkTo(searchInput.bottom)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ){}

    }
}

@Composable
fun LazyUsersItem(
    username: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_app),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape)
        )

        Column(
            modifier = Modifier.padding(start = 10.dp)
        ){
            Text(
                text = username,
                fontSize = 15.sp,
                fontFamily = FontFamily(Font(R.font.roboto)),
                fontWeight = FontWeight.Bold,
                color = White,
                maxLines = 1
            )

            Text(
                text = description,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = SilverFoil,
            )
        }

    }
}