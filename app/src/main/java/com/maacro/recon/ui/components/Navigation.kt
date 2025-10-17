package com.maacro.recon.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ReconNavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    NavigationBar(
        modifier = modifier.padding(horizontal = 20.dp),
        tonalElevation = 0.dp,
        contentColor = Color.Transparent,
        containerColor = Color.Transparent,
        content = { content() }
    )
}

@Composable
fun NavigationBarContent(
    modifier: Modifier = Modifier,
    items: @Composable RowScope.() -> Unit
) {
    Row(modifier = modifier.fillMaxWidth()) {
        items()
    }
}

@Composable
fun RowScope.ReconNavigationBarItem(
    modifier: Modifier = Modifier,
    selected: Boolean,
    icon: @Composable () -> Unit,
    selectedIcon: @Composable () -> Unit,
    onClick: () -> Unit,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = if (selected) selectedIcon else icon,
        modifier = modifier,
        enabled = enabled,
        label = label,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.onBackground,
            selectedTextColor = MaterialTheme.colorScheme.onBackground,
            indicatorColor = Color.Transparent,
        )
    )
}
