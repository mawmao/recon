package com.maacro.recon.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Immutable
object ReconTokens {
    val RoundedCornerShape: Shape = RoundedCornerShape(8.dp)

    object Padding {
        val Medium = 16.dp

        val Horizontal = PaddingValues(horizontal = Medium)
    }
}