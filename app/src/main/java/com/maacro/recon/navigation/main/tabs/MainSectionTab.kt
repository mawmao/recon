package com.maacro.recon.navigation.main.tabs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.maacro.recon.core.common.ReconRoute
import com.maacro.recon.navigation.main.MainSection

interface NavigationTab {
    val route: ReconRoute
    val label: String
    val icon: ImageVector
    val selectedIcon: ImageVector
}

enum class MainSectionTab(
    override val route: ReconRoute,
    override val label: String,
    override val icon: ImageVector,
    override val selectedIcon: ImageVector
) : NavigationTab {
    HOME(
        route = MainSection.Home,
        label = "Home",
        icon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home
    ),
    HISTORY(
        route = MainSection.History,
        label = "History",
        icon = Icons.Outlined.History,
        selectedIcon = Icons.Filled.History
    ),
}