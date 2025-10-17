package com.maacro.recon.ui.transitions

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import com.maacro.recon.ui.transitions.Transitions.indexedHorizontal

object Transitions {

    val None = TransitionSpec(
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    )

    val Horizontal = TransitionSpec(
        enterTransition = { TransitionToken.slideInFromRight },
        exitTransition = { TransitionToken.slideOutToLeft },
        popEnterTransition = { TransitionToken.slideInFromLeft },
        popExitTransition = { TransitionToken.slideOutToRight }
    )

    val HorizontalReversed = TransitionSpec(
        enterTransition = { TransitionToken.slideInFromLeft },
        exitTransition = { TransitionToken.slideOutToRight },
        popEnterTransition = { TransitionToken.slideInFromRight },
        popExitTransition = { TransitionToken.slideOutToLeft }
    )

    val Vertical = TransitionSpec(
        enterTransition = { TransitionToken.slideInFromTop },
        exitTransition = { TransitionToken.slideOutToTop },
        popEnterTransition = { TransitionToken.slideInFromBottom },
        popExitTransition = { TransitionToken.slideOutToBottom }
    )

    val VerticalReversed = TransitionSpec(
        enterTransition = { TransitionToken.slideInFromBottom },
        exitTransition = { TransitionToken.slideOutToBottom },
        popEnterTransition = { TransitionToken.slideInFromTop },
        popExitTransition = { TransitionToken.slideOutToTop }
    )

    /**
     * **For the future Mel,**
     *
     * Each route has already **fixed** transitions up front.
     * There will be in no case a route with [indexedHorizontal] transition will change at runtime.
     */
    fun indexedHorizontal(routes: List<String>): TransitionSpec {
        fun index(route: String?) = routes.indexOf(route)
        return TransitionSpec(
            enterTransition = {
                val diff = index(initialState.destination.route) - index(targetState.destination.route)
                when {
                    diff < 0 -> TransitionToken.slideInFromRight
                    diff > 0 -> TransitionToken.slideInFromLeft
                    else -> EnterTransition.None
                }
            },
            exitTransition = {
                val diff = index(initialState.destination.route) - index(targetState.destination.route)
                when {
                    diff < 0 -> TransitionToken.slideOutToLeft
                    diff > 0 -> TransitionToken.slideOutToRight
                    else -> ExitTransition.None
                }
            },
            popEnterTransition = { TransitionToken.slideInFromLeft },
            popExitTransition = { TransitionToken.slideOutToRight }
        )
    }
}