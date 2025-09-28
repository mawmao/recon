package com.maacro.recon.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.maacro.recon.navigation.ReconTopLevelDestination
import com.maacro.recon.navigation.route
import com.maacro.recon.utils.orRemember

@Composable
fun rememberReconAppState(
    navController: NavHostController = rememberNavController()
) = remember(navController) {
    ReconAppState(navController = navController)
}

@Stable
class ReconAppState(
    val navController: NavHostController,
) {
    val destinations: List<ReconTopLevelDestination> = ReconTopLevelDestination.entries
    val previousDestination = mutableStateOf<NavDestination?>(null)

    val currentDestination: ReconTopLevelDestination?
        @Composable get() {
            val backStackEntry by navController.currentBackStackEntryAsState()
            val destination = backStackEntry?.destination.orRemember(previousDestination)
            return destinations.firstOrNull { destination?.hasRoute(it.route()) == true }
        }

    val isTopLevelDestination: Boolean
        @Composable get() {
            return destinations.contains(currentDestination)
        }

    fun navigateToTopLevel(destination: ReconTopLevelDestination) {
        navController.navigate(destination.route) {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }
}

