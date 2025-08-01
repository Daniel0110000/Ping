package dev.dr10.ping.ui.screens.auth.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.dr10.ping.ui.theme.AppTheme

@Composable
fun HeaderInfoScreenComponent(
    title: String,
    description: String
) = Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Text(
        text = title,
        fontFamily = AppTheme.robotoFont,
        fontWeight = FontWeight.Medium,
        fontSize = 28.sp,
        color = AppTheme.colors.text
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = description,
        fontFamily = AppTheme.robotoFont,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        color = AppTheme.colors.textSecondary
    )
}