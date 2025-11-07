package com.maacro.recon.feature.form.ui.review

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maacro.recon.core.common.ReconConfirmEvent
import com.maacro.recon.core.common.ReconVMEvents
import com.maacro.recon.feature.form.model.FieldValue
import com.maacro.recon.feature.form.model.toDisplay
import com.maacro.recon.feature.form.ui.question.FormAnswers
import com.maacro.recon.ui.components.ReconButton
import com.maacro.recon.ui.components.ReconIconButton
import com.maacro.recon.ui.components.ReconTopAppBar
import com.maacro.recon.ui.components.formatDate
import com.maacro.recon.ui.util.safeHorizontalPadding
import com.maacro.recon.ui.util.safeVerticalPadding
import kotlinx.coroutines.flow.Flow


@Composable
fun ReviewScreen(
    vm: ReviewViewModel,
    onNavigateToMain: () -> Unit,
    onNavigateBackToQuestions: () -> Unit,
    answers: Map<String, FieldValue>
) {
    val state by vm.state.collectAsStateWithLifecycle()
    val events = vm.events
    val form = vm.currentForm

    ReviewScreenEvents(
        state = state,
        events = events,
        onAction = vm::onAction,
        onNavigateToMain = onNavigateToMain,
        onNavigateBackToQuestions = onNavigateBackToQuestions
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeVerticalPadding()
    ) {
        ReconTopAppBar(
            modifier = Modifier.safeHorizontalPadding(),
            onNavigateBack = { vm.onAction(ReviewAction.BackClick) },
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
                onClick = {
                    vm.onAction(ReviewAction.FormSubmit(answers = answers))
                }
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
                Log.d("recon:review-screen", "SubmitSuccess event received, navigating to main")
                onNavigateToMain()
            }
        }
    }
}