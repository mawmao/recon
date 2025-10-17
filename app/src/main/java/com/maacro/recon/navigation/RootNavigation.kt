package com.maacro.recon.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.maacro.recon.core.common.ReconRoute
import com.maacro.recon.feature.form.model.FormType
import com.maacro.recon.navigation.auth.authNavigation
import com.maacro.recon.navigation.form.formNavigation
import com.maacro.recon.navigation.main.mainNavigation
import com.maacro.recon.ui.ReconAppState
import kotlinx.serialization.Serializable

@Serializable
sealed class RootSection : ReconRoute {

    @Serializable
    object Auth : RootSection()

    @Serializable
    object Main : RootSection()

    @Serializable
    data class Form(val type: String) : RootSection()
}


@Composable
fun ReconRootNavigation(appState: ReconAppState) {

    NavHost(
        navController = appState.navController,
        startDestination = RootSection.Form(FormType.CULTURAL)
    ) {
        authNavigation(appState = appState)
        mainNavigation(appState = appState)
        formNavigation(appState = appState)
    }
}



