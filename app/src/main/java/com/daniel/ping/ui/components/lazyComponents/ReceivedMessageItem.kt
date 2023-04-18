package com.daniel.ping.ui.components.lazyComponents

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.daniel.ping.R
import com.daniel.ping.ui.theme.Onyx
import com.daniel.ping.ui.theme.SilverFoil
import com.daniel.ping.ui.theme.White

@Composable
fun ReceivedMessageItem(
    profileImage: Bitmap,
    message: String,
    date: String
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 10.dp, top = 8.dp, bottom = 8.dp)
    ) {

        val (profileImageReceived, textMessageContainerReceived, textDateTimeReceived) = createRefs()

        Image(
            bitmap = profileImage.asImageBitmap(),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(25.dp)
                .clip(CircleShape)
                .constrainAs(profileImageReceived) {
                    start.linkTo(parent.start)
                    bottom.linkTo(profileImageReceived.bottom)
                }
        )

        Box(
            modifier = Modifier
                .constrainAs(textMessageContainerReceived){
                    width = Dimension.fillToConstraints
                    top.linkTo(parent.top)
                    start.linkTo(profileImageReceived.end, margin = 12.dp)
                    end.linkTo(parent.end)
                }
        ) {
            Text(
                text = message,
                color = White,
                fontSize = 13.sp,
                fontFamily = FontFamily(Font(R.font.roboto)),
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .background(
                        Onyx,
                        RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp, bottomEnd = 15.dp)
                    )
                    .padding(vertical = 8.dp, horizontal = 12.dp)
            )
        }

        Text(
            text = date,
            color = SilverFoil,
            fontSize = 8.sp,
            modifier = Modifier
                .constrainAs(textDateTimeReceived){
                    top.linkTo(textMessageContainerReceived.bottom, margin = 4.dp)
                    start.linkTo(textMessageContainerReceived.start)
                }
        )

    }
}