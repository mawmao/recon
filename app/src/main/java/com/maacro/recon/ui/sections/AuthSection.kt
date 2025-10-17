package com.maacro.recon.ui.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.maacro.recon.navigation.auth.AuthSection
import com.maacro.recon.navigation.auth.ReconAuthNavHost
import com.maacro.recon.ui.ReconAppState
import com.maacro.recon.ui.components.ReconScaffold

@Composable
fun AuthSection(
    appState: ReconAppState,
    authSectionState: AuthSectionState = rememberAuthSectionState()
) {
    ReconScaffold {
        ReconAuthNavHost(
            appState = appState,
            authSectionState = authSectionState
        )
    }
}

@Composable
fun rememberAuthSectionState(
    authNavController: NavHostController = rememberNavController()
) = remember(authNavController) {
    AuthSectionState(navController = authNavController)
}

@Stable
class AuthSectionState(val navController: NavHostController) {
    fun navigateToForgotPassword() {
        navController.navigate(route = AuthSection.ForgotPassword) {
            launchSingleTop = true
        }
    }

    fun navigateBack() {
        navController.navigateUp() // testing; change back to popBackStack if problem arises
    }
}
