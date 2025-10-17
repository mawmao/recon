package com.maacro.recon.ui.transitions

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

object TransitionToken {
    private const val ANIM_DURATION = 300
    private val ANIMATION_SPEC =
        tween<IntOffset>(durationMillis = ANIM_DURATION, easing = FastOutSlowInEasing)

    val slideInFromRight =
        slideInHorizontally(
            initialOffsetX = { it },
            animationSpec = ANIMATION_SPEC
        ) + fadeIn()

    val slideOutToLeft =
        slideOutHorizontally(
            targetOffsetX = { -it / 4 },
            animationSpec = ANIMATION_SPEC
        ) + fadeOut()

    val slideInFromLeft = slideInHorizontally(
        initialOffsetX = { -it },
        animationSpec = ANIMATION_SPEC
    ) + fadeIn()

    val slideOutToRight = slideOutHorizontally(
        targetOffsetX = { it },
        animationSpec = ANIMATION_SPEC
    ) + fadeOut()

    val slideInFromBottom = slideInVertically(
        initialOffsetY = { it / 4 },
        animationSpec = ANIMATION_SPEC
    ) + fadeIn(animationSpec = tween(ANIM_DURATION))

    val slideOutToBottom = slideOutVertically(
        targetOffsetY = { it / 4 },
        animationSpec = ANIMATION_SPEC
    ) + fadeOut(animationSpec = tween(ANIM_DURATION))

    val slideInFromTop = slideInVertically(
        initialOffsetY = { -it / 4 },
        animationSpec = ANIMATION_SPEC
    ) + fadeIn(animationSpec = tween(ANIM_DURATION))

    val slideOutToTop = slideOutVertically(
        targetOffsetY = { -it / 4 },
        animationSpec = ANIMATION_SPEC
    ) + fadeOut(animationSpec = tween(ANIM_DURATION))

    val barEnter: EnterTransition = fadeIn()
    val barExit: ExitTransition = fadeOut()
}
