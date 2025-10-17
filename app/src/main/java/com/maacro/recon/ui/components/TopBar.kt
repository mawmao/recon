package com.maacro.recon.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.maacro.recon.ui.util.debug

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
    actions: (@Composable RowScope.() -> Unit)? = null,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    paddingValues: PaddingValues = PaddingValues(vertical = 16.dp)
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(paddingValues = paddingValues),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onNavigateBack != null) {
            Box(
                Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
                    ReconIconButton(
                        modifier = Modifier.size(24.dp),
                        onClick = onNavigateBack,
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Navigate Back Icon Button",
                        tint = contentColor
                    )
                }
            }
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
            modifier = Modifier.then(if (title != null) Modifier.weight(1f) else Modifier),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            content = { actions?.invoke(this) }
        )
    }
}
