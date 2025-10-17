package com.maacro.recon.navigation.main

import androidx.navigation.NavGraphBuilder
import com.maacro.recon.core.common.ReconRoute
import com.maacro.recon.core.common.navRoute
import com.maacro.recon.navigation.RootSection
import com.maacro.recon.navigation.form.FormSection
import com.maacro.recon.navigation.util.transitionComposable
import com.maacro.recon.ui.ReconAppState
import com.maacro.recon.ui.sections.MainSection
import com.maacro.recon.ui.transitions.Transitions
import com.maacro.recon.ui.transitions.buildTransitionTable
import kotlinx.serialization.Serializable

@Serializable
sealed class MainSection : ReconRoute {

    @Serializable
    data object Home : ReconRoute

    @Serializable
    data object History : ReconRoute
}

fun NavGraphBuilder.mainNavigation(
    appState: ReconAppState,
) {
    transitionComposable<RootSection.Main>(
        transitionTable = buildTransitionTable {
            with(navRoute<FormSection.Scan>(), Transitions.Horizontal)
            with(navRoute<RootSection.Auth>(), Transitions.VerticalReversed)
        }
    ) {
        MainSection(appState = appState)
    }
}

