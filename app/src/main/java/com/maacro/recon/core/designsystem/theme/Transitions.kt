package com.maacro.recon.core.designsystem.theme

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavBackStackEntry

object Transitions {

    private const val ANIM_DURATION = 400
    private val ANIMATION_SPEC = tween<IntOffset>(
        durationMillis = ANIM_DURATION,
        easing = FastOutSlowInEasing
    )


    val BarEnter: EnterTransition =
        fadeIn() + slideInVertically(animationSpec = tween(ANIM_DURATION)) { it }

    val BarExit: ExitTransition =
        fadeOut() + slideOutVertically(animationSpec = tween(ANIM_DURATION)) { it }

    val enter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        slideInHorizontally(
            initialOffsetX = { it },
            animationSpec = ANIMATION_SPEC
        ) + fadeIn()
    }

    val exit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        slideOutHorizontally(
            targetOffsetX = { -it / 4 },
            animationSpec = ANIMATION_SPEC
        ) + fadeOut()
    }

    val popEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        slideInHorizontally(
            initialOffsetX = { -it / 4 },
            animationSpec = ANIMATION_SPEC
        ) + fadeIn()
    }

    val popExit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        slideOutHorizontally(
            targetOffsetX = { it },
            animationSpec = ANIMATION_SPEC
        ) + fadeOut()
    }

}
