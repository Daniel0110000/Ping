package com.daniel.ping.ui.components.lazyComponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun RecentMessageItem(
    imageUrl: String,
    onClickListener: () -> Unit
) {
    Column {

        Spacer(modifier = Modifier.height(10.dp))

        AsyncImage(
            model = imageUrl,
            contentDescription = "",
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape)
                .clickable { onClickListener() },
            contentScale = ContentScale.Crop
        )
    }
}