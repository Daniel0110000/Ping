package com.daniel.ping.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.daniel.ping.R
import com.daniel.ping.ui.theme.Onyx
import com.daniel.ping.ui.theme.SilverFoil
import com.daniel.ping.ui.theme.UltramarineBlue
import com.daniel.ping.ui.theme.White

@Composable
fun FilePreviewComponent(
    modifier: Modifier,
    fileName: String,
    fileSize: String,
    close: () -> Unit
) {
    Card(
        modifier = modifier
            .height(50.dp)
            .width(160.dp),
        shape = RoundedCornerShape(10.dp),
        backgroundColor = Onyx,
        elevation = 10.dp
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxHeight()) {

            val (closePreview, fileIcon, fileNameText, fileSizeText) = createRefs()

            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = "Close preview",
                modifier = Modifier
                    .size(18.dp)
                    .clickable { close() }
                    .constrainAs(closePreview) {
                        top.linkTo(parent.top, margin = 5.dp)
                        end.linkTo(parent.end, margin = 5.dp)
                    },
                tint = UltramarineBlue
            )

            Image(
                painterResource(id = R.drawable.ic_file),
                contentDescription = "File icon",
                modifier = Modifier
                    .size(25.dp)
                    .constrainAs(fileIcon) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start, margin = 5.dp)
                    }
            )

            Text(
                text = fileName,
                color = White,
                fontFamily = FontFamily(Font(R.font.roboto)),
                fontSize = 12.sp,
                textAlign = TextAlign.Start,
                maxLines = 1,
                modifier = Modifier
                    .constrainAs(fileNameText){
                        width = Dimension.fillToConstraints
                        start.linkTo(fileIcon.end, margin = 5.dp)
                        end.linkTo(closePreview.start, margin = 5.dp)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            )

            Text(
                text = "$fileSize KB",
                color = SilverFoil,
                fontFamily = FontFamily(Font(R.font.roboto)),
                fontSize = 10.sp,
                modifier = Modifier.constrainAs(fileSizeText){
                    bottom.linkTo(parent.bottom, margin = 5.dp)
                    end.linkTo(parent.end, margin = 5.dp)
                }
            )

        }
    }
}