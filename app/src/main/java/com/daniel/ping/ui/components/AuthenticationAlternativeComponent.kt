package com.daniel.ping.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniel.ping.R
import com.daniel.ping.ui.theme.SilverFoil
import com.daniel.ping.ui.theme.White

@Composable
fun AuthenticationAlternativeComponent(
    onGoogleClickListener: () -> Unit,
    onFacebookClickListener: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(SilverFoil))

            Spacer(modifier = Modifier.size(10.dp))

            Text(
                text = "O",
                fontSize = 13.sp,
                color = White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.size(10.dp))

            Box(modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(SilverFoil))
        }

        Spacer(modifier = Modifier.size(15.dp))

        Row {
            Card(
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .clickable { onGoogleClickListener() }
                    .clip(RoundedCornerShape(10.dp)),
                backgroundColor = White,
                elevation = 10.dp
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Google Icon",
                    modifier = Modifier.scale(0.5f)
                )
            }

            Spacer(modifier = Modifier.size(15.dp))

            Card(
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .clickable { onFacebookClickListener() }
                    .clip(RoundedCornerShape(10.dp)),
                backgroundColor = Color(0xFF1A75E4),
                elevation = 10.dp
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_facebook),
                    contentDescription = "Facebook Icon",
                    modifier = Modifier.scale(0.6f)
                )
            }

        }
    }
}