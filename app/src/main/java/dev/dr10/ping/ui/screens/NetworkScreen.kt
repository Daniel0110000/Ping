package dev.dr10.ping.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.dr10.ping.R
import dev.dr10.ping.domain.models.UserProfileModel
import dev.dr10.ping.ui.screens.components.IconButtonComponent
import dev.dr10.ping.ui.screens.components.PlaceholderComponent
import dev.dr10.ping.ui.screens.components.TextFieldComponent
import dev.dr10.ping.ui.screens.components.UserListItem
import dev.dr10.ping.ui.theme.AppTheme
import dev.dr10.ping.ui.viewmodels.NetworkViewModel
import network.chaintech.sdpcomposemultiplatform.sdp
import network.chaintech.sdpcomposemultiplatform.ssp
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NetworkScreen(
    viewModel: NetworkViewModel = koinViewModel(),
    onBack: () -> Unit,
    onNavigateToChat: (UserProfileModel) -> Unit,
    onErrorMessage: (Int) -> Unit
) {
    val state = viewModel.state.collectAsState().value

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            onErrorMessage(it)
            viewModel.clearErrorMessage()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .padding(vertical = 7.sdp, horizontal = 5.sdp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButtonComponent(
                iconId = R.drawable.ic_back,
                contentDescription = stringResource(R.string.back),
                background = AppTheme.colors.onBackground,
                size = 30.sdp,
                iconSize = 15.sdp
            ) { onBack() }

            Spacer(Modifier.width(5.sdp))

            Text(
                text = stringResource(R.string.network),
                fontFamily = AppTheme.robotoFont,
                fontWeight = FontWeight.Medium,
                color = AppTheme.colors.text,
                fontSize = 15.ssp
            )

        }

        Spacer(Modifier.height(7.sdp))

        TextFieldComponent(
            value = state.search,
            onValueChange = { viewModel.setSearchText(it) },
            placeholder = stringResource(R.string.search_friend),
            capitalization = true,
            height = 33.sdp,
            background = AppTheme.colors.onBackground,
            iconId = R.drawable.ic_search,
            horizontalPadding = 0.dp,
            isSearch = true
        )

        Spacer(Modifier.height(8.sdp))

        when {
            state.users != null && state.isSearchUserLoading -> {
                CircularProgressIndicator(
                    strokeWidth = (1.7).sdp,
                    modifier = Modifier.size(17.sdp).align(Alignment.CenterHorizontally),
                    color = AppTheme.colors.complementary,
                    trackColor = AppTheme.colors.background,
                )
            }
            state.users != null && state.users.isNotEmpty() -> {
                Text(
                    text = stringResource(R.string.search_results),
                    fontFamily = AppTheme.robotoFont,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.colors.text,
                    fontSize = (12.5).ssp,
                )

                LazyColumn(Modifier.fillMaxSize()) {
                    items(state.users) {
                        Spacer(Modifier.height(7.sdp))
                        UserListItem(it) { onNavigateToChat(it) }
                    }
                }
            }
            state.users != null -> {
                PlaceholderComponent(
                    iconId = R.drawable.ic_not_found,
                    label = stringResource(R.string.friend_not_found),
                    iconSize = 120.sdp,
                    fontSize = 22.ssp,
                    marginTop = (-16).sdp
                )
            }
            else -> {
                Text(
                    text = stringResource(R.string.suggestions),
                    fontFamily = AppTheme.robotoFont,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.colors.text,
                    fontSize = (12.5).ssp,
                )

                if (state.isSuggestedUsersLoading) {
                    Spacer(Modifier.height(7.sdp))
                    CircularProgressIndicator(
                        strokeWidth = (1.7).sdp,
                        modifier = Modifier.size(17.sdp).align(Alignment.CenterHorizontally),
                        color = AppTheme.colors.complementary,
                        trackColor = AppTheme.colors.background,
                    )
                } else {
                    LazyColumn(Modifier.fillMaxSize()) {
                        items(state.userSuggestions) {
                            Spacer(Modifier.height(7.sdp))
                            UserListItem(it) { onNavigateToChat(it) }
                        }
                    }
                }
            }
        }
    }
}