package com.maacro.recon.core.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ReconDefaultNavigationTopBar(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    onClose: () -> Unit,
) {
    ReconTopAppBar(
        modifier = modifier,
        onNavigateBack = onNavigateBack,
        actions = {
            ReconIconButton(
                onClick = onClose,
                imageVector = Icons.Outlined.Close,
                contentDescription = "Close Icon Button"
            )
        }
    )
}


@Composable
fun ReconTopAppBar(
    modifier: Modifier = Modifier,
    onNavigateBack: (() -> Unit)? = null,
    title: (@Composable () -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onNavigateBack != null) {
            ReconIconButton(
                onClick = onNavigateBack,
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = "Navigate Back Icon Button"
            )
        }

        if (title != null) {
            Box(
                Modifier.weight(1f),
                contentAlignment = if (onNavigateBack != null) Alignment.Center else Alignment.CenterStart
            ) {
                title()
            }
        }

        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            content = actions
        )
    }
}
