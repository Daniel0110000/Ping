package dev.dr10.ping.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.dr10.ping.R
import dev.dr10.ping.domain.models.UserProfileModel
import dev.dr10.ping.ui.screens.components.IconButtonComponent
import dev.dr10.ping.ui.screens.components.TextFieldComponent
import dev.dr10.ping.ui.screens.components.UserListItem
import dev.dr10.ping.ui.theme.AppTheme

@Composable
fun NetworkScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .padding(vertical = 15.dp, horizontal = 10.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButtonComponent(
                iconId = R.drawable.ic_back,
                contentDescription = "Back",
                background = AppTheme.colors.onBackground,
                size = 40.dp,
                iconSize = 18.dp
            ) {

            }

            Spacer(Modifier.width(8.dp))

            Text(
                text = "Network",
                fontFamily = AppTheme.robotoFont,
                fontWeight = FontWeight.Medium,
                color = AppTheme.colors.text,
                fontSize = 20.sp
            )

        }

        Spacer(Modifier.height(10.dp))

        TextFieldComponent(
            value = "",
            onValueChange = {},
            placeholder = "Search friend",
            capitalization = true,
            height = 50.dp,
            background = AppTheme.colors.onBackground,
            iconId = R.drawable.ic_search,
            horizontalPadding = 0.dp,
            isSearch = true
        )

        Spacer(Modifier.height(15.dp))
        
        Text(
            text = "Suggestions for you",
            fontFamily = AppTheme.robotoFont,
            fontWeight = FontWeight.Medium,
            color = AppTheme.colors.text,
            fontSize = 17.sp,
        )

        LazyColumn(Modifier.fillMaxSize()) {
            items(3) {
                Spacer(Modifier.height(10.dp))

                UserListItem(
                    UserProfileModel(
                        userId = "12345",
                        username = "Jhon Doe",
                        bio = "This is a test user.",
                        profileImageUrl = "https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/428c5ec9-de92-4956-85cb-4d4e41e41567/dcby6dy-a149403d-b644-4dfd-b578-836dca58e926.png?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7InBhdGgiOiJcL2ZcLzQyOGM1ZWM5LWRlOTItNDk1Ni04NWNiLTRkNGU0MWU0MTU2N1wvZGNieTZkeS1hMTQ5NDAzZC1iNjQ0LTRkZmQtYjU3OC04MzZkY2E1OGU5MjYucG5nIn1dXSwiYXVkIjpbInVybjpzZXJ2aWNlOmZpbGUuZG93bmxvYWQiXX0.AvFCy0bRuk11MUe16nYyxIYo5Lztd_ilG4FHmp52TAc"
                    )
                ) { }
            }
        }

    }
}