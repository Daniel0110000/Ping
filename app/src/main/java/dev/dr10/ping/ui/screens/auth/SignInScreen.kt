package dev.dr10.ping.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import dev.dr10.ping.R
import dev.dr10.ping.ui.screens.auth.components.ActionsAuthContainerComponent
import dev.dr10.ping.ui.screens.auth.components.ButtonAuthActionComponent
import dev.dr10.ping.ui.screens.auth.components.GoogleAuthActionComponent
import dev.dr10.ping.ui.screens.auth.components.HeaderInfoScreenComponent
import dev.dr10.ping.ui.screens.auth.components.OrTextComponent
import dev.dr10.ping.ui.screens.auth.components.QuestionTextComponent
import dev.dr10.ping.ui.screens.auth.components.TopIconAppComponent
import dev.dr10.ping.ui.screens.components.TextFieldComponent
import dev.dr10.ping.ui.theme.AppTheme
import dev.dr10.ping.ui.viewmodels.SignInViewModel
import network.chaintech.sdpcomposemultiplatform.sdp
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignInScreen(
    viewModel: SignInViewModel = koinViewModel(),
    onErrorMessage: (Int) -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToProfileSetup: () -> Unit
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsState().value

    LaunchedEffect(state.isSignInSuccessful) {
        if (state.isSignInSuccessful) onNavigateToHome()
    }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            onErrorMessage(it)
            viewModel.clearErrorMessage()
        }
    }

    LaunchedEffect(state.isIncompleteProfile) {
        if (state.isIncompleteProfile) onNavigateToProfileSetup()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
    ) {
        TopIconAppComponent()

        Spacer(Modifier.height(22.sdp))

        HeaderInfoScreenComponent(
            title = stringResource(R.string.sign_in),
            description = stringResource(R.string.sign_in_description)
        )

        Spacer(Modifier.height(36.sdp))

        ActionsAuthContainerComponent {

            Spacer(Modifier.height(25.sdp))

            TextFieldComponent(
                value = state.email,
                onValueChange = { viewModel.onEmailChanged(it) },
                placeholder = stringResource(R.string.email),
                isEmail = true,
                isNext = true
            )

            Spacer(Modifier.height(9.sdp))

            TextFieldComponent(
                value = state.password,
                onValueChange = { viewModel.onPasswordChanged(it) },
                placeholder = stringResource(R.string.password),
                isPassword = true,
                isDone = true
            )

            Spacer(Modifier.height(18.sdp))

            ButtonAuthActionComponent(
                label = stringResource(R.string.sign_in),
                isLoading = state.isSignInLoading
            ) { viewModel.onSignIn() }

            Spacer(Modifier.height(11.sdp))

            OrTextComponent()

            Spacer(Modifier.height(11.sdp))

            GoogleAuthActionComponent(isLoading = state.isGoogleSignInLoading) { viewModel.onGoogleSignIn(context) }

            Spacer(Modifier.height(20.sdp))

            QuestionTextComponent(stringResource(R.string.sign_in_question))

            Spacer(Modifier.height(8.sdp))

            ButtonAuthActionComponent(label = stringResource(R.string.sign_up)) { onNavigateToSignUp() }

        }

    }
}