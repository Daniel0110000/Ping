package com.daniel.ping.ui.components.lazyComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
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

@Composable
fun SentMessageWithFileOrMP3Item(
    FMNameText: String,
    FMSizeText: String,
    message: String = "",
    isMp3: Boolean = false,
    date: String
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 90.dp, top = 8.dp, bottom = 8.dp, end = 4.dp)
    ) {

        val (messageContainer, textDateTimeSent) = createRefs()

        Box(
            modifier = Modifier
                .background(
                    UltramarineBlue,
                    RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp, bottomStart = 15.dp)
                )
                .padding(3.dp)
                .constrainAs(messageContainer) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
        ){
            Column {
                Box(
                    modifier = Modifier
                        .height(57.dp)
                        .background(
                            Color(0xFF354DC7),
                            RoundedCornerShape(10.dp)
                        )
                ){

                    ConstraintLayout(modifier = Modifier.fillMaxSize()) {

                        val (fileIcon, fileName, fileSizeAndType) = createRefs()

                        Image(
                            painterResource(id = if(!isMp3) R.drawable.ic_file else R.drawable.ic_music),
                            contentDescription = "FileIcon",
                            modifier = Modifier
                                .size(25.dp)
                                .constrainAs(fileIcon) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    start.linkTo(parent.start, margin = 5.dp)
                                }
                        )

                        Text(
                            text = FMNameText,
                            color = White,
                            fontFamily = FontFamily(Font(R.font.roboto)),
                            fontSize = 15.sp,
                            maxLines = 2,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.constrainAs(fileName){
                                width = Dimension.fillToConstraints
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                start.linkTo(fileIcon.end, margin = 5.dp)
                                end.linkTo(parent.end, margin = 8.dp)
                            }
                        )

                        Text(
                            text = "$FMSizeText KB · ${
                                FMNameText.substringAfterLast(".", "").uppercase()
                            }",
                            color = SilverFoil,
                            fontFamily = FontFamily(Font(R.font.roboto)),
                            fontSize = 10.sp,
                            modifier = Modifier
                                .constrainAs(fileSizeAndType){
                                    top.linkTo(fileName.bottom, margin = 3.dp)
                                    bottom.linkTo(parent.bottom, margin = 6.dp)
                                    end.linkTo(parent.end, margin = 8.dp)
                                }
                        )

                    }
                }

                if(message.isNotEmpty()){
                    Text(
                        text = message,
                        color = White,
                        fontSize = 13.sp,
                        fontFamily = FontFamily(Font(R.font.roboto)),
                        modifier = Modifier
                            .padding(3.dp)
                            .fillMaxWidth()
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