package com.maacro.recon.navigation.auth

import androidx.navigation.NavGraphBuilder
import com.maacro.recon.core.common.ReconRoute
import com.maacro.recon.navigation.RootSection
import com.maacro.recon.navigation.util.transitionComposable
import com.maacro.recon.ui.ReconAppState
import com.maacro.recon.ui.sections.AuthSection
import com.maacro.recon.ui.transitions.Transitions
import kotlinx.serialization.Serializable

@Serializable
sealed class AuthSection : ReconRoute {

    @Serializable
    data object Login : AuthSection()

    @Serializable
    data object ForgotPassword : AuthSection() {

        @Serializable
        data object Otp : ReconRoute
    }
}

fun NavGraphBuilder.authNavigation(appState: ReconAppState) {
    transitionComposable<RootSection.Auth>(
        defaultTransition = Transitions.Vertical,
    ) {
        AuthSection(appState = appState)
    }
}

