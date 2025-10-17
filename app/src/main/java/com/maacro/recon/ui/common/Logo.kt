package com.maacro.recon.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.maacro.recon.R

@Composable
fun ReconLogo(
    modifier: Modifier = Modifier,
    size: Dp = 76.dp
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = modifier.size(size),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo"
        )
    }
}
