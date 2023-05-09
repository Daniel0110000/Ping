package com.daniel.ping.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp

@Composable
fun FilePreviewComponent(
    imagePreview: Bitmap,
    modifier: Modifier,
    close: () -> Unit
) {
    Box(modifier = modifier.height(150.dp)) {
        Box {
            Image(
                bitmap = imagePreview.asImageBitmap(),
                contentDescription = "Image preview",
                modifier = Modifier.clip(RoundedCornerShape(10.dp))
            )

            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = "Close preview",
                modifier = Modifier
                    .size(28.dp)
                    .align(Alignment.TopEnd)
                    .padding(5.dp)
                    .clickable { close() }
            )

        }

    }
}