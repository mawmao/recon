package com.maacro.recon.utils

import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


fun Modifier.debug(color: Color = Color.Red) = this.border(1.dp, color)
