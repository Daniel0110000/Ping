package com.daniel.ping.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.daniel.ping.R
import com.daniel.ping.ui.theme.Onyx
import com.daniel.ping.ui.theme.White

@Composable
fun MoreOptionsComponent(
    modifier: Modifier,
    openGallery: () -> Unit
) {

    Column(modifier = modifier) {
        Card(
            modifier = Modifier
                .size(45.dp)
                .clickable { openGallery() },
            shape = RoundedCornerShape(10.dp),
            backgroundColor = Onyx,
        ) {
            Icon(
                painterResource(id = R.drawable.ic_gallery),
                contentDescription = "Open gallery",
                tint = White,
                modifier = Modifier.scale(0.5f)
            )
        }
    }

}