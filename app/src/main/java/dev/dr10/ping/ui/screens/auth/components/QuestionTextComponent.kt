package dev.dr10.ping.ui.screens.auth.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dev.dr10.ping.ui.theme.AppTheme

@Composable
fun QuestionTextComponent(
    text: String
) = Text(
    text = text,
    color = AppTheme.colors.text,
    fontFamily = AppTheme.robotoFont,
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp
)