package com.daniel.ping.ui.components.lazyComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.daniel.ping.ui.theme.Onyx
import com.daniel.ping.ui.theme.SilverFoil
import com.daniel.ping.ui.theme.White

@Composable
fun ReceivedMessageItem(
    profileImageUrl: String,
    message: String,
    date: String
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 10.dp, top = 8.dp, bottom = 8.dp)
    ) {

        val (profileImageReceived, textMessageContainerReceived, textDateTimeReceived) = createRefs()

        AsyncImage(
            model = profileImageUrl,
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

@Composable
fun ReceivedMessageWithImageItem(
    imageUrl: String,
    profileImageUrl: String,
    message: String = "",
    date: String
) {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 10.dp, top = 8.dp, bottom = 8.dp)
    ){

        val (profileImageReceived, messageContainerReceived, textDateTimeReceived) = createRefs()

        AsyncImage(
            model = profileImageUrl,
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
                .constrainAs(messageContainerReceived){
                    width = Dimension.fillToConstraints
                    top.linkTo(parent.top)
                    start.linkTo(profileImageReceived.end, margin = 12.dp)
                    end.linkTo(parent.end)
                }
        ) {

            Box(
                modifier = Modifier
                    .background(
                        Onyx,
                        RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp, bottomEnd = 15.dp)
                    )
                    .padding(5.dp)
            ){
                ConstraintLayout {

                    val (imageMessageSent, textMessageSent) = createRefs()

                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "",
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
        }

        Text(
            text = date,
            color = SilverFoil,
            fontSize = 8.sp,
            modifier = Modifier
                .constrainAs(textDateTimeReceived){
                    top.linkTo(messageContainerReceived.bottom, margin = 4.dp)
                    start.linkTo(messageContainerReceived.start)
                }
        )

    }

}

@Composable
fun ReceivedMessageWithFileItem(
    profileImageUrl: String,
    fileNameText: String,
    fileSizeText: String,
    message: String = "",
    date: String,
    downloadFileListener: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 80.dp, top = 8.dp, bottom = 8.dp)
    ) {

        val (profileImageReceived, messageContainerReceived, textDateTimeReceived) = createRefs()

        AsyncImage(
            model = profileImageUrl,
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
                .constrainAs(messageContainerReceived){
                    width = Dimension.fillToConstraints
                    top.linkTo(parent.top)
                    start.linkTo(profileImageReceived.end, margin = 12.dp)
                    end.linkTo(parent.end)
                }
        ){
            Box(
                modifier = Modifier
                    .background(
                        Onyx,
                        RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp, bottomEnd = 15.dp)
                    )
                    .padding(5.dp)
            ){
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(57.dp)
                            .background(
                                Color(0xFF161616),
                                RoundedCornerShape(10.dp)
                            )
                    ){

                        ConstraintLayout(modifier = Modifier.fillMaxSize()) {

                            val (downloadFile, fileIcon, fileName, fileSizeAndType) = createRefs()

                            Icon(
                                painterResource(id = R.drawable.ic_download),
                                contentDescription = "Download file",
                                tint = White,
                                modifier = Modifier
                                    .size(18.dp)
                                    .clickable { downloadFileListener() }
                                    .constrainAs(downloadFile) {
                                        top.linkTo(parent.top, margin = 5.dp)
                                        end.linkTo(parent.end, margin = 5.dp)
                                    }
                            )

                            Image(
                                painterResource(id = R.drawable.ic_file),
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
                                text = fileNameText,
                                color = White,
                                fontFamily = FontFamily(Font(R.font.roboto)),
                                fontSize = 15.sp,
                                maxLines = 1,
                                textAlign = TextAlign.Start,
                                modifier = Modifier.constrainAs(fileName){
                                    width = Dimension.fillToConstraints
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    start.linkTo(fileIcon.end, margin = 5.dp)
                                    end.linkTo(downloadFile.start, margin = 5.dp)
                                }
                            )

                            Text(
                                text = "$fileSizeText KB · ${
                                    fileNameText.substringAfterLast(".", "").uppercase()
                                }",
                                color = SilverFoil,
                                fontFamily = FontFamily(Font(R.font.roboto)),
                                fontSize = 10.sp,
                                modifier = Modifier
                                    .constrainAs(fileSizeAndType){
                                        top.linkTo(fileName.bottom, margin = 3.dp)
                                        bottom.linkTo(parent.bottom, margin = 5.dp)
                                        end.linkTo(parent.end, margin = 5.dp)
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
        }

        Text(
            text = date,
            color = SilverFoil,
            fontSize = 8.sp,
            modifier = Modifier
                .constrainAs(textDateTimeReceived){
                    top.linkTo(messageContainerReceived.bottom, margin = 4.dp)
                    start.linkTo(messageContainerReceived.start)
                }
        )

    }
}