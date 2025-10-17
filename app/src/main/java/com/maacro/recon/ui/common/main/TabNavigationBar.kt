package com.maacro.recon.ui.common.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.maacro.recon.navigation.main.tabs.NavigationTab
import com.maacro.recon.ui.components.NavigationBarContent
import com.maacro.recon.ui.components.ReconNavigationBar
import com.maacro.recon.ui.components.ReconNavigationBarItem

@Composable
fun ReconTabNavigationBar(
    currentTab: NavigationTab?,
    tabs: List<NavigationTab>,
    onNavigateToTab: (NavigationTab) -> Unit
) {
    ReconNavigationBar(modifier = Modifier.padding(horizontal = 64.dp)) {
        NavigationBarContent {
            tabs.fastForEach { tab ->
                ReconNavigationBarItem(
                    selected = currentTab == tab,
                    onClick = {
                        if (currentTab != tab) {
                            onNavigateToTab(tab)
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.label
                        )
                    },
                    selectedIcon = {
                        Icon(
                            imageVector = tab.selectedIcon,
                            contentDescription = tab.label
                        )
                    },
                    label = { Text(tab.label) }
                )
            }
        }
    }
}
