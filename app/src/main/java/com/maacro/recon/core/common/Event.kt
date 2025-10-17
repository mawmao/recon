package com.maacro.recon.core.common

import android.R.id.message
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.maacro.recon.ui.common.ReconErrorDialog
import com.maacro.recon.ui.components.ReconConfirmDialog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest


/**
 * TODO: Group [ReconConfirmDialog] state to a data class
 *
 */
@Composable
fun <E> ReconScreenEvents(
    events: Flow<E>,

    errors: String? = null,
    onErrorDismiss: (() -> Unit)? = null,

    confirmVisible: Boolean = false,
    confirmMessage: String = "Are you sure?",
    onConfirmDismiss: (() -> Unit)? = null,
    onConfirm: (() -> Unit)? = null,

    onBackGesture: (() -> Unit)? = null,
    onEvent: suspend (E) -> Unit,
) {
    if (onBackGesture != null) {
        BackHandler(onBack = onBackGesture)
    }

    LaunchedEffect(events) {
        events.collectLatest(onEvent)
    }


    if (onConfirmDismiss != null && onConfirm != null) {
        ReconConfirmDialog(
            isVisible = confirmVisible,
            message = confirmMessage,
            onDismissRequest = { onConfirmDismiss() },
            onConfirm = { onConfirm() }
        )
    }

    if (errors != null) {
        ReconErrorDialog(
            title = "Error!",
            message = errors,
            onDismissRequest = { onErrorDismiss?.invoke() }
        )
    }
}

