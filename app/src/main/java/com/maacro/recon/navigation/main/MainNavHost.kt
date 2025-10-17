package com.maacro.recon.navigation.main

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.maacro.recon.feature.history.HistoryScreen
import com.maacro.recon.feature.home.HomeScreen
import com.maacro.recon.navigation.util.navigationBarComposable
import com.maacro.recon.ui.ReconAppState
import com.maacro.recon.ui.sections.MainSectionState

@Composable
fun ReconMainNavHost(
    appState: ReconAppState,
    mainSectionState: MainSectionState,
) {
    NavHost(
        navController = mainSectionState.navController,
        startDestination = MainSection.Home
    ) {
        navigationBarComposable<MainSection.Home> {
            HomeScreen(
                onSignOut = appState::navigateToAuth,
                onFormClick = appState::navigateToForm
            )
        }
        navigationBarComposable<MainSection.History> {
            HistoryScreen()
        }
    }
}

