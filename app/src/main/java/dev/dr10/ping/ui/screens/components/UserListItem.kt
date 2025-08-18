package dev.dr10.ping.ui.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import dev.dr10.ping.domain.models.UserProfileModel
import dev.dr10.ping.ui.theme.AppTheme

@Composable
fun UserListItem(
    userData: UserProfileModel,
    onClick: () -> Unit
) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() },
    verticalAlignment = Alignment.CenterVertically
) {
    AsyncImage(
        model = userData.profileImageUrl,
        contentDescription = userData.username,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(45.dp)
            .clip(RoundedCornerShape(10.dp))
    )

    Spacer(Modifier.width(7.dp))
    
    Column(Modifier.fillMaxWidth()) {
        Text(
            text = userData.username,
            fontFamily = AppTheme.robotoFont,
            fontWeight = FontWeight.Medium,
            fontSize = 17.sp,
            color = AppTheme.colors.text
        )

        Text(
            text = userData.bio,
            fontFamily = AppTheme.robotoFont,
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp,
            color = AppTheme.colors.textSecondary,
            maxLines = 1
        )
    }

}