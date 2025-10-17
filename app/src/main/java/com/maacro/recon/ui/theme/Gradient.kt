package com.maacro.recon.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class Gradient(
    val top: Pair<Float, Color>,
    val bottom: Pair<Float, Color>
)