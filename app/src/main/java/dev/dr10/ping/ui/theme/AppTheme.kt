package dev.dr10.ping.ui.theme

import androidx.compose.ui.graphics.Color


object AppTheme {

    val colors: Colors = Colors()

    class Colors(
        val background: Color = Color(0xFF171717),
        val onBackground: Color = Color(0xFF121313),
        val complementary: Color = Color(0xFF4561EE),
        val text: Color = Color(0xFFFFFFFF),
        val textSecondary: Color = Color(0xFFB5B5B5),
    )
}