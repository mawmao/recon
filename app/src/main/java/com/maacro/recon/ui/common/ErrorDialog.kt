package com.maacro.recon.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.maacro.recon.ui.components.ReconAlertDialog

@Composable
fun ReconErrorDialog(
    modifier: Modifier = Modifier,
    title: String,
    message: String,
    onDismissRequest: () -> Unit
) {
    ReconAlertDialog(
        modifier = modifier,
        icon = Icons.Outlined.ErrorOutline,
        title = title,
        message = message,
        onDismissRequest = onDismissRequest
    )
}