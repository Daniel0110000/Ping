package com.daniel.ping.ui.components.lazyComponents

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.daniel.ping.R
import com.daniel.ping.ui.theme.UltramarineBlue
import com.daniel.ping.ui.theme.White

@Composable
fun RecentMessageItem(
    imageUrl: String,
    receivedMessageCount: Int = 0,
    onClickListener: () -> Unit
) {
    ConstraintLayout {
        val (userImage, receivedMessageCountContainer) = createRefs()

        AsyncImage(
            model = imageUrl,
            contentDescription = "",
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape)
                .clickable { onClickListener() }
                .constrainAs(userImage) {
                    top.linkTo(parent.top, margin = 10.dp)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end, margin = 3.dp)
                },
            contentScale = ContentScale.Crop
        )

        AnimatedVisibility(
            visible = receivedMessageCount > 0,
            modifier = Modifier
                .constrainAs(receivedMessageCountContainer) {
                    top.linkTo(parent.top, margin = 9.dp)
                    end.linkTo(parent.end)
                },
        ) {
            Box(
                modifier = Modifier
                    .width(30.dp)
                    .height(15.dp)
                    .background(UltramarineBlue, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = receivedMessageCount.toString(),
                    color = White,
                    fontSize = 11.sp,
                    fontFamily = FontFamily(Font(R.font.roboto))
                )
            }
        }

    }
}