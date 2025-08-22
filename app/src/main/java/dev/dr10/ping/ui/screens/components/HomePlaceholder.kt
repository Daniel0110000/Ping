package dev.dr10.ping.ui.screens.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.dr10.ping.R

@Composable
fun HomePlaceholder() = PlaceholderComponent(
    iconId = R.drawable.ic_app,
    label = stringResource(R.string.app_name)
)