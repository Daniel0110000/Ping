package com.daniel.ping.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.daniel.ping.R
import com.daniel.ping.ui.theme.*

@Composable
fun ChatScreen(
    navController: NavController
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {

        val (backScreen, profileImage, username, onlineIndicator, lazyMessages, inputMessage, sendMessage) = createRefs()

        IconButton(
            onClick = {  },
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
            painter = painterResource(id = R.drawable.ic_app),
            contentDescription = "",
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
            text = "",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.roboto)),
            maxLines = 1,
            color = White ,
            modifier = Modifier
                .constrainAs(username){
                    width = Dimension.fillToConstraints
                    top.linkTo(profileImage.top)
                    bottom.linkTo(profileImage.bottom)
                    start.linkTo(profileImage.end, margin = 6.dp)
                    end.linkTo(onlineIndicator.start, margin = 5.dp)
                }
        )

        Box(modifier = Modifier
            .clip(CircleShape)
            .size(10.dp)
            .background(LimeGreen)
            .constrainAs(onlineIndicator) {
                top.linkTo(parent.top, margin = 15.dp)
                end.linkTo(parent.end, margin = 15.dp)
            }
        )
        
        LazyColumn(
            modifier = Modifier
                .constrainAs(lazyMessages){
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                    top.linkTo(profileImage.bottom, margin = 15.dp)
                    bottom.linkTo(inputMessage.top, margin = 10.dp)
                    start.linkTo(parent.start, margin = 5.dp)
                    end.linkTo(parent.end, margin = 5.dp)
                }
        ){}
        TextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .heightIn(0.dp, 45.dp)
                .constrainAs(inputMessage) {
                    width = Dimension.fillToConstraints
                    bottom.linkTo(parent.bottom, margin = 10.dp)
                    start.linkTo(parent.start, margin = 10.dp)
                    end.linkTo(sendMessage.start, margin = 10.dp)
                },
            placeholder = { Text(
                text = stringResource(id = R.string.message),
                color = SilverFoil,
                fontSize = 11.sp
            ) },
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
                .clickable { }
                .constrainAs(sendMessage) {
                    bottom.linkTo(parent.bottom, margin = 10.dp)
                    end.linkTo(parent.end, margin = 10.dp)
                },
            shape = RoundedCornerShape(10.dp),
            backgroundColor = Onyx
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_send),
                contentDescription = "Send message",
                tint = UltramarineBlue,
                modifier = Modifier.scale(0.6f)
            )
        }

    }
}