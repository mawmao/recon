package com.maacro.recon.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.maacro.recon.ui.theme.ScoutTypography
import kotlinx.coroutines.delay

/**
 * TODO: factor out to small composables to keep the main composable lean)
 */




@Composable
fun ReconDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
) {
    Dialog(
        properties = DialogProperties(
            dismissOnClickOutside = true,
            dismissOnBackPress = true,
        ),
        onDismissRequest = onDismissRequest
    ) {
        Surface(modifier = modifier, shape = RoundedCornerShape(14.dp)) {
            content()
        }
    }
}

// has [onFinished] lambda
@Composable
fun ReconDialog(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onFinished: () -> Unit,
    content: @Composable () -> Unit,
) {
    var wasVisible by remember { mutableStateOf(false) }
    if (isVisible) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnClickOutside = true,
                dismissOnBackPress = true,
            ),
        ) {
            Surface(modifier = modifier, shape = RoundedCornerShape(14.dp)) {
                content()
            }
        }
    }

    LaunchedEffect(isVisible) {
        if (!isVisible && wasVisible) onFinished()
        wasVisible = isVisible
    }
}


@Composable
fun ReconConfirmDialog(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    icon: ImageVector = Icons.Outlined.QuestionMark,
    title: String = "Confirm",
    message: String,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    // Track whether the user requested sign out
    var confirmRequest by remember { mutableStateOf(false) }

    ReconDialog(
        isVisible = isVisible,
        onDismiss = {
            onDismissRequest()
        },
        onFinished = {
            if (confirmRequest) {
                onConfirm()
                confirmRequest = false // reset
            }
        },
        content = {
            Column {
                Icon(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .size(42.dp),
                    imageVector = icon,
                    contentDescription = "$title Icon",
                    tint = MaterialTheme.colorScheme.primary,
                )
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp),
                ) {
                    Text(
                        text = title,
                        style = ScoutTypography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = message,
                        style = ScoutTypography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider()
                Row(modifier = Modifier.fillMaxWidth()) {
                    ReconDialogButton(
                        modifier = Modifier.weight(1F),
                        text = "Cancel",
                        onClick = onDismissRequest
                    )
                    ReconDialogButton(modifier = Modifier.weight(1F), text = "OK", onClick = {
                        confirmRequest = true // mark sign out
                        onDismissRequest() // close the dialog
                    })
                }
            }
        }
    )
}


@Composable
fun ReconAlertDialog(
    modifier: Modifier = Modifier,
    icon: ImageVector, // change to composable if needed
    title: String,
    message: String,
    onDismissRequest: () -> Unit,
) {
    ReconDialog(modifier = modifier, onDismissRequest = onDismissRequest) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .size(42.dp),
                imageVector = icon,
                contentDescription = "$title Icon",
                tint = MaterialTheme.colorScheme.primary,
            )
            Column(
                modifier = Modifier.padding(horizontal = 24.dp),
            ) {
                Text(
                    text = title,
                    style = ScoutTypography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = message,
                    style = ScoutTypography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider()
            ReconDialogButton(text = "OK", onClick = onDismissRequest)
        }
    }
}


@Composable
fun ReconDialogButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val ripple = ripple(bounded = true)
    val interactionSource = remember { MutableInteractionSource() }
    var isDismissing by remember { mutableStateOf(false) }

    LaunchedEffect(isDismissing) {
        if (isDismissing) {
            delay(120L)
            onClick()
        }
    }

    Text(
        text = text,
        textAlign = TextAlign.Center,
        style = ScoutTypography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = ripple,
                enabled = !isDismissing,
                onClick = { if (!isDismissing) isDismissing = true }
            )
            .padding(vertical = 16.dp)
    )
}
