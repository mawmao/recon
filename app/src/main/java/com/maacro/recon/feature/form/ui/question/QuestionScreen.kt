package com.maacro.recon.feature.form.ui.question

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maacro.recon.core.common.ReconScreenEvents
import com.maacro.recon.feature.form.model.FieldType
import com.maacro.recon.ui.components.ReconButton
import com.maacro.recon.ui.components.ReconCheckbox
import com.maacro.recon.ui.components.ReconTextField
import com.maacro.recon.ui.components.ReconTopAppBar
import com.maacro.recon.ui.util.safeHorizontalPadding
import com.maacro.recon.ui.util.safeVerticalPadding

@Composable
fun ReconFlexField(
    modifier: Modifier = Modifier,
    type: FieldType,
    label: String,
) = when (type) {
    FieldType.TEXT -> {
        ReconTextField(
            modifier = modifier,
            state = rememberTextFieldState(),
            label = label
        )
    }

    FieldType.NUMBER -> {
        ReconTextField(
            modifier = modifier,
            state = rememberTextFieldState(),
            label = label
        )
    }

    FieldType.DATE -> {
        ReconTextField(
            modifier = modifier,
            state = rememberTextFieldState(),
            label = label
        )
    }

    FieldType.DROPDOWN -> {
        ReconTextField(
            modifier = modifier,
            state = rememberTextFieldState(),
            label = label
        )
    }

    FieldType.CHECKBOX -> {
        var checked by remember { mutableStateOf(false) }
        ReconCheckbox(
            modifier = modifier,
            checked = checked,
            onCheckedChange = { checked = !checked }
        )
    }
}

@Composable
fun QuestionScreen(
    vm: QuestionViewModel,
    onNavigateToMain: () -> Unit
) {

    val state by vm.state.collectAsStateWithLifecycle()
    val events = vm.events

    val hasNextPage = state.pager.currentPage < state.pager.pageCount - 1

    ReconScreenEvents(
        events = events,

        confirmVisible = state.isExitConfirmShown,
        confirmMessage = "Are you sure you want to exit?",
        onConfirmDismiss = vm::hideExitConfirmation,
        onConfirm = onNavigateToMain,

        onBackGesture = vm::navigateOrExit

    ) { event ->
        when (event) {
            is QuestionEvent.MovePage -> {
                state.pager.animateScrollToPage(event.page)
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeVerticalPadding()
    ) {
        ReconTopAppBar(
            modifier = Modifier.safeHorizontalPadding(),
            onNavigateBack = vm::navigateOrExit,
            actions = {
                Text(
                    text = "${state.pager.currentPage + 1} of ${state.pager.pageCount}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }
        )
        HorizontalPager(
            userScrollEnabled = false,
            contentPadding = PaddingValues(horizontal = 16.dp),
            pageSpacing = 16.dp,
            state = state.pager,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { page ->
            val section = state.currentForm.sections[page]
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.Top
            ) {

                Text(
                    text = section.title,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = section.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(32.dp))

                section.fields.forEach { fieldRow ->
                    if (fieldRow.fields.size == 1) {
                        val field = fieldRow.fields.first()
                        ReconFlexField(
                            type = field.type,
                            label = field.label,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            fieldRow.fields.forEach { field ->
                                ReconFlexField(
                                    type = field.type,
                                    label = field.label,
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                }
            }
        }

        Box(
            modifier = Modifier
                .safeHorizontalPadding()
                .padding(bottom = 32.dp)
        ) {
            if (hasNextPage) {
                ReconButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Continue",
                    onClick = { vm.onAction(QuestionAction.NextPage) }
                )
            } else {
                ReconButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Submit",
                    onClick = {}
                )
            }
        }
    }
}
