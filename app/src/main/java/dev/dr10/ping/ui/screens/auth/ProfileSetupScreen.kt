package dev.dr10.ping.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.dr10.ping.R
import dev.dr10.ping.ui.screens.auth.components.ActionsAuthContainerComponent
import dev.dr10.ping.ui.screens.auth.components.ButtonAuthActionComponent
import dev.dr10.ping.ui.screens.auth.components.PickAndPreviewProfileImageComponent
import dev.dr10.ping.ui.screens.auth.components.TopIconAppComponent
import dev.dr10.ping.ui.screens.components.TextFieldComponent
import dev.dr10.ping.ui.theme.AppTheme

@Composable
fun ProfileSetupScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopIconAppComponent()

        Spacer(Modifier.height(30.dp))

        PickAndPreviewProfileImageComponent {  }

        Spacer(Modifier.height(50.dp))

        ActionsAuthContainerComponent {
            Spacer(Modifier.height(40.dp))

            TextFieldComponent(
                value = "",
                onValueChange = { },
                placeholder = stringResource(R.string.username),
                isEmail = true,
                isNext = true
            )

            Spacer(Modifier.height(15.dp))

            TextFieldComponent(
                value = "",
                onValueChange = {  },
                placeholder = stringResource(R.string.description),
                isPassword = true,
                isDone = true
            )

            Spacer(Modifier.height(25.dp))

            ButtonAuthActionComponent(label = stringResource(R.string.finish)) {}

        }
    }
}