package dev.dr10.ping.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import dev.dr10.ping.R
import dev.dr10.ping.ui.screens.auth.components.TopIconAppComponent
import dev.dr10.ping.ui.theme.AppTheme
import dev.dr10.ping.ui.viewmodels.InitialSyncViewModel
import network.chaintech.sdpcomposemultiplatform.sdp
import network.chaintech.sdpcomposemultiplatform.ssp
import org.koin.androidx.compose.koinViewModel

@Composable
fun InitialSyncScreen(
    viewmodel: InitialSyncViewModel = koinViewModel(),
    onErrorMessage: (Int) -> Unit,
    onNavigateToHome: () -> Unit
) {
    val state = viewmodel.state.collectAsState().value

    LaunchedEffect(state.isFinished) {
        if (state.isFinished) onNavigateToHome()
    }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            onErrorMessage(it)
            viewmodel.clearErrorMessage()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background),
    ) {

        TopIconAppComponent()

        Spacer(Modifier.weight(1f))

        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.sdp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    strokeWidth = 3.sdp,
                    modifier = Modifier.size(85.sdp),
                    color = AppTheme.colors.complementary,
                    trackColor = AppTheme.colors.background,
                )

                state.profileImage?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "",
                        modifier = Modifier.size(75.sdp).clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(Modifier.height(10.sdp))

            Text(
                text = stringResource(R.string.initial_sync_title),
                fontFamily = AppTheme.robotoFont,
                fontWeight = FontWeight.Bold,
                fontSize = 13.ssp,
                color = AppTheme.colors.text,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(5.sdp))

            Text(
                text = stringResource(R.string.initial_sync_description),
                fontFamily = AppTheme.robotoFont,
                fontWeight = FontWeight.Normal,
                fontSize = 11.ssp,
                color = AppTheme.colors.textSecondary,
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.weight(1f))

    }
}