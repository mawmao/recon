package com.maacro.recon.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.maacro.recon.ui.theme.ReconColors.Gray100
import com.maacro.recon.ui.theme.ReconColors.Gray300
import com.maacro.recon.ui.theme.ReconColors.Gray500
import com.maacro.recon.ui.theme.ReconColors.Gray700
import com.maacro.recon.ui.theme.ReconColors.Gray900
import com.maacro.recon.ui.theme.ReconColors.Red100
import com.maacro.recon.ui.theme.ReconColors.Red500
import com.maacro.recon.ui.theme.ReconColors.White


private val LightColorScheme = lightColorScheme(
    background = Gray100,
    onBackground = Gray900,
    surface = White,
    onSurface = Gray700,
    onSurfaceVariant = Gray500,
    primary = Gray700,
    onPrimary = Gray500,
    secondary = Gray300,
    error = Red100,
    onError = Red500
)


@Composable
fun ReconTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = ScoutTypography,
        content = content
    )
}