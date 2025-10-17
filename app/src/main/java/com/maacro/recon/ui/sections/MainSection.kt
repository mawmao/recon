package com.maacro.recon.ui.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.util.fastFirstOrNull
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.maacro.recon.core.common.navRoute
import com.maacro.recon.navigation.main.ReconMainNavHost
import com.maacro.recon.navigation.main.tabs.MainSectionTab
import com.maacro.recon.navigation.main.tabs.NavigationTab
import com.maacro.recon.ui.ReconAppState
import com.maacro.recon.ui.common.main.ReconTabNavigationBar
import com.maacro.recon.ui.components.ReconScaffold

@Composable
fun MainSection(
    appState: ReconAppState,
    mainSectionState: MainSectionState = rememberMainSectionState()
) {
    ReconScaffold(
        bottomBar = {
            ReconTabNavigationBar(
                tabs = mainSectionState.tabs,
                currentTab = mainSectionState.currentTab,
                onNavigateToTab = mainSectionState::navigateToTab
            )
        }
    ) {
        ReconMainNavHost(
            appState = appState,
            mainSectionState = mainSectionState
        )
    }
}

@Composable
fun rememberMainSectionState(
    mainNavController: NavHostController = rememberNavController()
) = remember(mainNavController) {
    MainSectionState(navController = mainNavController)
}

@Stable
class MainSectionState(
    val navController: NavHostController
) {

    val tabs: List<NavigationTab> = MainSectionTab.entries

    val previousTab = mutableStateOf<NavDestination?>(null)
    val currentTab: NavigationTab?
        @Composable get() {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val navDestination = navBackStackEntry?.destination.also {
                if (it != null) previousTab.value = it
            } ?: previousTab.value
            return tabs.fastFirstOrNull { tab ->
                navDestination?.route == navRoute(tab.route)
            }
        }

    //    val isTopLevelTabRoute: Boolean @Composable get() = tabs.contains(currentTab)

    fun navigateToTab(tab: NavigationTab) {
        navController.navigate(tab.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}

