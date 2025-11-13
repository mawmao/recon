package com.maacro.recon.navigation.util

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.maacro.recon.core.common.navRoute
import com.maacro.recon.navigation.auth.AuthSection
import com.maacro.recon.navigation.main.MainSection
import com.maacro.recon.ui.transitions.TransitionSpec
import com.maacro.recon.ui.transitions.TransitionTable
import com.maacro.recon.ui.transitions.Transitions
import com.maacro.recon.ui.transitions.buildTransitionTable

inline fun <reified T : Any> NavGraphBuilder.transitionNavigationGraph(
    startDestination: Any,
    defaultTransition: TransitionSpec = Transitions.Horizontal,
    transitionTable: TransitionTable? = null,
    noinline builder: NavGraphBuilder.() -> Unit
) {
    val transition = transitionTable?.resolveTransition() ?: defaultTransition
    navigation<T>(
        startDestination = startDestination,
        enterTransition = transition.enterTransition,
        exitTransition = transition.exitTransition,
        popEnterTransition = transition.popEnterTransition,
        popExitTransition = transition.popExitTransition,
        builder = builder
    )
}

inline fun <reified T : Any> NavGraphBuilder.transitionComposable(
    defaultTransition: TransitionSpec = Transitions.Horizontal,
    transitionTable: TransitionTable? = null,
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    val transition = transitionTable?.resolveTransition() ?: defaultTransition
    internalComposable<T>(transition = transition, content = content)
}

inline fun <reified T : Any> NavGraphBuilder.navigationBarComposable(
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {

    val transition = buildTransitionTable {
        with(navRoute<AuthSection.Login>(), Transitions.VerticalReversed)
        indexed(navRoute<MainSection.Home>(), navRoute<MainSection.History>())
    }.resolveTransition()

    internalComposable<T>(transition = transition, content = content)
}

inline fun <reified T : Any> NavGraphBuilder.internalComposable(
    transition: TransitionSpec = Transitions.Horizontal,
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable<T>(
        enterTransition = transition.enterTransition,
        exitTransition = transition.exitTransition,
        popEnterTransition = transition.popEnterTransition,
        popExitTransition = transition.popExitTransition,
        content = content
    )
}
