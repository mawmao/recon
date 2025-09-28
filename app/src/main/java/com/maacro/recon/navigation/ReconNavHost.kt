package com.maacro.recon.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.maacro.recon.core.designsystem.theme.Transitions
import com.maacro.recon.feature.auth.AuthRoute
import com.maacro.recon.feature.auth.authGraph
import com.maacro.recon.feature.home.HomeRoute
import com.maacro.recon.feature.home.homeGraph
import com.maacro.recon.ui.ReconAppState

@Composable
fun ReconNavHost(
    appState: ReconAppState,
    scaffoldPadding: PaddingValues,
    modifier: Modifier
) {
    NavHost(
        modifier = modifier,
        navController = appState.navController,
        startDestination = AuthRoute,
        enterTransition = Transitions.enter,
        exitTransition = Transitions.exit,
        popEnterTransition = Transitions.popEnter,
        popExitTransition = Transitions.popExit
    ) {
        authGraph(
            modifier = Modifier,
            onAuthSuccess = {
                appState.navController.navigate(HomeRoute) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
        )
        homeGraph(
            modifier = Modifier.padding(scaffoldPadding),
            onSignOut = {
                appState.navController.navigate(AuthRoute) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
        )
    }
}