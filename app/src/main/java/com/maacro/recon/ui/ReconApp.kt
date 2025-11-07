package com.maacro.recon.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.maacro.recon.navigation.ReconRootNavigation
import com.maacro.recon.navigation.RootSection
import io.github.jan.supabase.auth.status.SessionStatus

@Composable
fun ReconApp(
    appState: ReconAppState = rememberReconAppState(),
    sessionStatus: SessionStatus
) {
    val initialRoute = remember(Unit) {
        when (sessionStatus) {
            is SessionStatus.Authenticated -> RootSection.Main
            else -> RootSection.Auth
        }
    }

    ReconRootNavigation(
        appState = appState,
        startDestination = initialRoute
    )
}

