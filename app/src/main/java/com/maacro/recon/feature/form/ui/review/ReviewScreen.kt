package com.maacro.recon.feature.form.ui.review

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maacro.recon.core.common.ReconConfirmEvent
import com.maacro.recon.core.common.ReconVMEvents
import com.maacro.recon.ui.components.ReconButton
import com.maacro.recon.ui.components.ReconIconButton
import com.maacro.recon.ui.components.ReconTopAppBar
import com.maacro.recon.ui.sections.FormSectionState
import com.maacro.recon.ui.util.safeHorizontalPadding
import com.maacro.recon.ui.util.safeVerticalPadding
import kotlinx.coroutines.flow.Flow


@Composable
fun ReviewScreen(
    vm: ReviewViewModel,
    formSectionState: FormSectionState,
    onNavigateToMain: () -> Unit,
    onNavigateBackToQuestions: () -> Unit,
    onSuccess: () -> Unit, // for logging
) {
    val state by vm.state.collectAsStateWithLifecycle()
    val events = vm.events
    val form = vm.form

    ReviewScreenEvents(
        state = state,
        events = events,
        onAction = vm::onAction,
        onNavigateToMain = onNavigateToMain,
        onNavigateBackToQuestions = onNavigateBackToQuestions,
        onSuccess = onSuccess,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeVerticalPadding()
    ) {
        ReconTopAppBar(
            modifier = Modifier.safeHorizontalPadding(),
            onBackTap = { vm.onAction(ReviewAction.BackClick) },
            actions = {
                ReconIconButton(
                    onClick = { vm.onAction(ReviewAction.ExitClick) },
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Close Icon Button"
                )
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .safeHorizontalPadding()
                .padding(bottom = 32.dp)
        ) {
            ReconButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Submit",
                onClick = { vm.onAction(ReviewAction.FormSubmit(answers = formSectionState.answers)) }
            )
        }
    }
}

@Composable
private fun ReviewScreenEvents(
    state: ReviewScreenState,
    events: Flow<ReviewEvent>,
    onAction: (ReviewAction) -> Unit,
    onNavigateToMain: () -> Unit,
    onNavigateBackToQuestions: () -> Unit,
    onSuccess: () -> Unit
) {
    ReconConfirmEvent(
        confirmVisible = state.isBackConfirmShown,
        confirmMessage = "Go back to form?",
        onConfirmDismiss = { onAction(ReviewAction.BackDismiss) },
        onConfirm = onNavigateBackToQuestions,
    )

    ReconConfirmEvent(
        confirmVisible = state.isExitConfirmShown,
        confirmMessage = "Go back to home page?",
        onConfirmDismiss = { onAction(ReviewAction.ExitDismiss) },
        onConfirm = onNavigateToMain,
    )

    ReconVMEvents(
        events = events,
        onBackGesture = { onAction(ReviewAction.BackClick) }
    ) { event ->
        when (event) {
            // NOTE: should show dialog first before navigating to main
            ReviewEvent.SubmitSuccess -> {
                onSuccess()
            }
        }
    }
}