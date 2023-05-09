package com.daniel.ping.ui.components.lazyComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import com.daniel.ping.R
import com.daniel.ping.ui.theme.SilverFoil
import com.daniel.ping.ui.theme.UltramarineBlue
import com.daniel.ping.ui.theme.White

@Composable
fun SentMessageItem(
    message: String,
    date: String
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {

        val (textMessageSent, textDateTimeSent) = createRefs()

        Text(
            text = message,
            color = White,
            fontSize = 13.sp,
            fontFamily = FontFamily(Font(R.font.roboto)),
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .background(
                    UltramarineBlue,
                    RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp, bottomStart = 15.dp)
                )
                .padding(vertical = 8.dp, horizontal = 12.dp)
                .constrainAs(textMessageSent) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
        )

        Text(
            text = date,
            color = SilverFoil,
            fontSize = 8.sp,
            modifier = Modifier
                .constrainAs(textDateTimeSent){
                    top.linkTo(textMessageSent.bottom, margin = 4.dp)
                    end.linkTo(parent.end)
                }
        )

    }
}


@Composable
fun SentMessageWithImageItem(
    imageUrl: String,
    message: String = "",
    date: String
) {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {

        val (messageContainer, textDateTimeSent) = createRefs()

        Box(
            modifier = Modifier
                .background(
                    UltramarineBlue,
                    RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp, bottomStart = 15.dp)
                )
                .padding(5.dp)
                .constrainAs(messageContainer) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }

        ) {
            ConstraintLayout {

                val (imageMessageSent, textMessageSent) = createRefs()

                AsyncImage(
                    model = imageUrl,
                    contentDescription = message,
                    modifier = Modifier
                        .heightIn(0.dp, 160.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .constrainAs(imageMessageSent) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                )

                if(message.isNotEmpty()){
                    Text(
                        text = message,
                        color = White,
                        fontSize = 13.sp,
                        fontFamily = FontFamily(Font(R.font.roboto)),
                        modifier = Modifier
                            .constrainAs(textMessageSent){
                                width = Dimension.fillToConstraints
                                top.linkTo(imageMessageSent.bottom, margin = 5.dp)
                                start.linkTo(imageMessageSent.start)
                                end.linkTo(imageMessageSent.end)
                            }
                    )
                }

            }
        }

        Text(
            text = date,
            color = SilverFoil,
            fontSize = 8.sp,
            modifier = Modifier
                .constrainAs(textDateTimeSent){
                    top.linkTo(messageContainer.bottom, margin = 4.dp)
                    end.linkTo(parent.end)
                }
        )

    }

}