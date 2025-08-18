package dev.dr10.ping.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import dev.dr10.ping.R


object AppTheme {

    val colors: Colors = Colors()
    val robotoFont: FontFamily = FontFamily(
        Font(R.font.roboto, FontWeight.Normal),
        Font(R.font.roboto_medium, FontWeight.Medium),
        Font(R.font.roboto_bold, FontWeight.Bold),
    )

    class Colors(
        val background: Color = Color(0xFF171717),
        val onBackground: Color = Color(0xFF121313),
        val surfaceBackground: Color = Color(0xFF1C1C1C),
        val complementary: Color = Color(0xFF4561EE),
        val text: Color = Color(0xFFFFFFFF),
        val textSecondary: Color = Color(0xFFB5B5B5),

        val online: Color = Color(0xFF0CFF79)
    )
}