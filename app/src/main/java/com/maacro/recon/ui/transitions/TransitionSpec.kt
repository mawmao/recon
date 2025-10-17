package com.maacro.recon.ui.transitions

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Immutable
import androidx.navigation.NavBackStackEntry

typealias EnterTransitionBlock =
        AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?

typealias ExitTransitionBlock =
        AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?

@Immutable
data class TransitionSpec(
    val enterTransition: (EnterTransitionBlock)? = null,
    val exitTransition: (ExitTransitionBlock)? = null,
    val popEnterTransition: (EnterTransitionBlock)? = enterTransition,
    val popExitTransition: (ExitTransitionBlock)? = exitTransition,
)
