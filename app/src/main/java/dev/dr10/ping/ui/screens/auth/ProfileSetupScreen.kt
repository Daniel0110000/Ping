package dev.dr10.ping.ui.screens.auth

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.dr10.ping.R
import dev.dr10.ping.ui.screens.auth.components.ActionsAuthContainerComponent
import dev.dr10.ping.ui.screens.auth.components.ButtonAuthActionComponent
import dev.dr10.ping.ui.screens.auth.components.PickAndPreviewProfileImageComponent
import dev.dr10.ping.ui.screens.auth.components.TopIconAppComponent
import dev.dr10.ping.ui.screens.components.TextFieldComponent
import dev.dr10.ping.ui.theme.AppTheme
import dev.dr10.ping.ui.viewmodels.ProfileSetupViewModel
import network.chaintech.sdpcomposemultiplatform.sdp
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileSetupScreen(
    viewModel: ProfileSetupViewModel = koinViewModel(),
    onErrorMessage: (Int) -> Unit,
    onNavigateToHome: () -> Unit
) {
    val state = viewModel.state.collectAsState().value
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> viewModel.onProfileImageChanged(uri)  }

    LaunchedEffect(state.isProfileSetupSuccessful) {
        if (state.isProfileSetupSuccessful) onNavigateToHome()
    }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            onErrorMessage(it)
            viewModel.clearErrorMessage()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopIconAppComponent()

        Spacer(Modifier.height(22.sdp))

        PickAndPreviewProfileImageComponent(state.profileImage) {
            photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        Spacer(Modifier.height(36.sdp))

        ActionsAuthContainerComponent {
            Spacer(Modifier.height(25.sdp))

            TextFieldComponent(
                value = state.username,
                onValueChange = { viewModel.onUsernameChanged(it) },
                placeholder = stringResource(R.string.username),
                capitalization = true,
                isNext = true
            )

            Spacer(Modifier.height(9.sdp))

            TextFieldComponent(
                value = state.bio,
                onValueChange = { viewModel.onBioChanged(it) },
                placeholder = stringResource(R.string.bio),
                capitalization = true,
                isDone = true
            )

            Spacer(Modifier.height(18.sdp))

            ButtonAuthActionComponent(
                label = stringResource(R.string.finish),
                isLoading = state.isLoading,
            ) { viewModel.onFinish() }

        }
    }
}