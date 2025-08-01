package dev.dr10.ping.ui.screens.auth.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dev.dr10.ping.ui.theme.AppTheme

@Composable
fun OrTextComponent() = Text(
    text = "Or",
    color = AppTheme.colors.textSecondary,
    fontFamily = AppTheme.robotoFont,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp
)