package dev.dr10.ping.ui.screens.auth.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import dev.dr10.ping.R
import dev.dr10.ping.ui.theme.AppTheme

@Composable
fun PickAndPreviewProfileImageComponent(
    profileImagePreview: Uri?,
    onPickImage: () -> Unit
) = ConstraintLayout {
    val (imageBoxContainer, pickImageBtn) = createRefs()

    Box(
        modifier = Modifier
            .size(130.dp)
            .background(AppTheme.colors.onBackground, shape = CircleShape)
            .constrainAs(imageBoxContainer) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
        contentAlignment = Alignment.Center
    ) {
        if (profileImagePreview != null) {
            AsyncImage(
                model = profileImagePreview,
                contentDescription = stringResource(R.string.profile_image),
                modifier = Modifier.fillMaxSize().clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Text(
                stringResource(R.string.profile_image),
                color = AppTheme.colors.textSecondary,
                fontFamily = AppTheme.robotoFont,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )
        }
    }

    Box(
        modifier = Modifier
            .size(35.dp)
            .background(AppTheme.colors.complementary, shape = CircleShape)
            .constrainAs(pickImageBtn) {
                bottom.linkTo(imageBoxContainer.bottom)
                end.linkTo(imageBoxContainer.end, margin = 5.dp)
            }
            .clickable { onPickImage() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_add_photo),
            contentDescription = stringResource(R.string.profile_image),
            tint = AppTheme.colors.background,
            modifier = Modifier.size(20.dp)
        )
    }

}