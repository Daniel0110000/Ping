package com.daniel.ping.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniel.ping.R
import com.daniel.ping.ui.theme.SilverFoil
import com.daniel.ping.ui.theme.White

@Composable
fun DescriptionScreenComponent(
    title: String,
    description: String,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            color = White,
            fontFamily = FontFamily(Font(R.font.roboto)),
            fontWeight = FontWeight.Bold
        )

        Text(
            text = description,
            fontSize = 12.sp,
            color = SilverFoil,
            fontFamily = FontFamily(Font(R.font.roboto)),
            textAlign = TextAlign.Center
        )

    }
}