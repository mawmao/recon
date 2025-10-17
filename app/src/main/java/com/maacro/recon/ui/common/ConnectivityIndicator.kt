package com.maacro.recon.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maacro.recon.ui.theme.ReconColors.HumayGreen
import com.maacro.recon.ui.theme.ReconColors.Red500
import com.maacro.recon.ui.theme.ScoutTypography

@Composable
fun ConnectivityIndicator(
    isOffline: Boolean,
    modifier: Modifier = Modifier,
    hideLabel: Boolean = false,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(8.dp)
) {
    val statusColor = if (isOffline) Red500 else HumayGreen
    val statusText = if (isOffline) "(つ•̀ꞈ•̀)つ - offline" else "(つ˘▽˘)つ - online"

    Row(
        modifier = modifier,
        verticalAlignment = verticalAlignment,
        horizontalArrangement = horizontalArrangement
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color = statusColor, shape = CircleShape)
        )
        if (!hideLabel) {
            Text(
                text = statusText,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75F),
                style = ScoutTypography.labelLarge
            )
        }
    }
}
