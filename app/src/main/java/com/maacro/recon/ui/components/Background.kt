package com.maacro.recon.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.maacro.recon.ui.theme.Gradient

// TODO: should change

@Composable
fun ReconBackground(
    gradient: Gradient?,
    enabled: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    var displayedGradient by remember { mutableStateOf(gradient) }

    val targetAlpha = if (enabled && gradient != null) 1f else 0f
    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = 500)
    )

    LaunchedEffect(gradient) {
        if (gradient != null) displayedGradient = gradient
    }

    LaunchedEffect(alpha, gradient) {
        if (alpha <= 0f && (gradient == null || !enabled)) displayedGradient = null
    }

    val topColor = displayedGradient?.top?.second ?: Color.Transparent
    val bottomColor = displayedGradient?.bottom?.second ?: Color.Transparent
    val topPos = displayedGradient?.top?.first ?: 0f
    val bottomPos = displayedGradient?.bottom?.first ?: 1f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(
                if (alpha <= 0f && displayedGradient == null) Modifier
                else Modifier.drawWithCache {
                    val brush = Brush.linearGradient(
                        colorStops = arrayOf(topPos to topColor, bottomPos to bottomColor)
                    )
                    onDrawBehind { drawRect(brush = brush, alpha = alpha) }
                }
            ),
        content = content
    )
}
