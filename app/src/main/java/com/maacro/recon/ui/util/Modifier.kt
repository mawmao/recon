package com.maacro.recon.ui.util

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.maacro.recon.ui.theme.ReconTokens


@Stable
fun Modifier.debug(
    color: Color = Color.Red,
    enable: Boolean = true
): Modifier = if (enable) this.border(1.dp, color) else this


@Stable
@Composable
fun Modifier.safePadding() =
    this
        .padding(ReconTokens.Padding.Horizontal)
        .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical))

@Stable
@Composable
fun Modifier.safeVerticalPadding() =
    this.windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical))

@Stable
fun Modifier.safeHorizontalPadding() =
    this.padding(ReconTokens.Padding.Horizontal)