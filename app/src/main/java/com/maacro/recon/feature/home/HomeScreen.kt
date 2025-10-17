package com.maacro.recon.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maacro.recon.core.common.ReconScreenEvents
import com.maacro.recon.feature.form.model.FormType
import com.maacro.recon.ui.common.ReconLogo
import com.maacro.recon.ui.components.ReconDialog
import com.maacro.recon.ui.components.ReconDialogButton
import com.maacro.recon.ui.components.ReconIconButton
import com.maacro.recon.ui.components.ReconTopAppBar
import com.maacro.recon.ui.theme.ScoutTypography
import com.maacro.recon.ui.util.debug
import com.maacro.recon.ui.util.safePadding

private const val DEBUG = false;


@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onSignOut: () -> Unit,
    onFormClick: (String) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val errors by viewModel.errors.collectAsStateWithLifecycle()
    val events = viewModel.events

    ReconScreenEvents(
        events = events,
        errors = errors,
        onErrorDismiss = viewModel::clearError,
    ) { }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safePadding(),
        verticalArrangement = Arrangement.Top,
    ) {
        SettingsDialog(
            isVisible = state.isSettingsShown,
            onDismissRequest = viewModel::hideSettings,
            onSignOut = onSignOut
        )
        HomeScreenTopBar(onSettingsClick = { viewModel.onAction(HomeAction.SettingsClick) })
        TemporaryFormGrid(onFormClick = onFormClick)
    }
}

@Composable
fun TemporaryFormGrid(onFormClick: (String) -> Unit) {

    val coreItems = FormType.coreTypes
    val optionalItems = FormType.optionalTypes

    Column(modifier = Modifier.fillMaxSize().padding(bottom = 16.dp)) {
        Text("Core", modifier = Modifier.padding(bottom = 8.dp))

        Column(modifier = Modifier.weight(2f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            for (row in 0 until 2) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    for (col in 0 until 2) {
                        val index = row * 2 + col
                        TemporaryFormPlaceholder(coreItems[index], modifier = Modifier.weight(1f)) {
                            onFormClick(coreItems[index])
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text("Optional", modifier = Modifier.padding(vertical = 8.dp))
        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            for (item in optionalItems) {
                TemporaryFormPlaceholder(item, modifier = Modifier.weight(1f)) {
                    onFormClick(item)
                }
            }
        }
    }
}

@Composable
fun TemporaryFormPlaceholder(
    item: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape = RoundedCornerShape(16.dp))
            .background(color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6F))
            .clickable(onClick = onClick)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = item, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
    }
}

@Composable
fun SettingsDialog(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    onDismissRequest: () -> Unit,
    onSignOut: () -> Unit
) {
    // Track whether the user requested sign out
    var signOutRequested by remember { mutableStateOf(false) }

    ReconDialog(
        isVisible = isVisible,
        onDismiss = {
            onDismissRequest()
        },
        onFinished = {
            if (signOutRequested) {
                onSignOut()
                signOutRequested = false // reset
            }
        },
        content = {
            Column {
                Box(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Settings",
                        style = ScoutTypography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                HorizontalDivider()
                Column(
                    modifier = modifier
                        .heightIn(min = 200.dp)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box {
                        Text(
                            text = "TODO: \nAuto Sync, \nRemove sign out from button, \n...",
                            style = ScoutTypography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                HorizontalDivider()
                ReconDialogButton(
                    text = "Sign out",
                    onClick = {
                        signOutRequested = true // mark sign out
                        onDismissRequest() // close the dialog
                    }
                )
            }
        }
    )
}


@Composable
fun HomeScreenTopBar(
    modifier: Modifier = Modifier,
    onSettingsClick: () -> Unit
) {
    ReconTopAppBar(
        modifier = modifier.debug(enable = DEBUG),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ReconLogo(size = 40.dp)
                Text(
                    text = "Scout",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        },
        actions = {
            ReconIconButton(
                onClick = {}, // TODO
                imageVector = Icons.Filled.Sync,
                contentDescription = "Sync Icon Button"
            )
            ReconIconButton(
                onClick = onSettingsClick,
                imageVector = Icons.Filled.Settings,
                contentDescription = "Settings Icon Button"
            )
        }
    )
}