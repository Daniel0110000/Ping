package com.daniel.ping.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.daniel.ping.ui.theme.RangoonGreen
import com.daniel.ping.ui.theme.White

@Composable
fun HomeScreen(
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(RangoonGreen),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = "Home Screen",
            color = White
        )
    }
}