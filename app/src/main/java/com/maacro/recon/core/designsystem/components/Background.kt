package com.maacro.recon.core.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import com.maacro.recon.core.designsystem.theme.Gradient

@Composable
fun ReconBackground(
    gradient: Gradient,
    content: @Composable BoxScope.() -> Unit,
) {

    Box(
        content = content,
        modifier = Modifier
            .fillMaxSize()
            .drawWithCache {
                val brush = Brush.linearGradient(
                    colorStops = arrayOf(
                        gradient.top, gradient.bottom
                    )
                )
                onDrawBehind {
                    drawRect(brush)
                }
            }
    )
}
