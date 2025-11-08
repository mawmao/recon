package com.maacro.recon.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
    icon: ImageVector,
    selectedIcon: ImageVector,
    onClick: () -> Unit,
    enabled: Boolean = true,
    label: String,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = {
            Icon(
                imageVector = if (selected) selectedIcon else icon,
                contentDescription = label
            )
        },
        modifier = modifier,
        enabled = enabled,
        label = { Text(label) },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.onBackground,
            selectedTextColor = MaterialTheme.colorScheme.onBackground,
            indicatorColor = Color.Transparent,
        )
    )
}
