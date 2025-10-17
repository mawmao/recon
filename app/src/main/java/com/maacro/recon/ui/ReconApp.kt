package com.maacro.recon.ui

import androidx.compose.runtime.Composable
import com.maacro.recon.navigation.ReconRootNavigation

@Composable
fun ReconApp(appState: ReconAppState = rememberReconAppState()) {
    ReconRootNavigation(appState = appState)
}

