package com.maacro.recon.feature.form.ui.question

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maacro.recon.core.common.ReconVMEvents
import com.maacro.recon.feature.form.model.FieldValue
import com.maacro.recon.ui.components.DebugArea
import com.maacro.recon.ui.components.DebugButton
import com.maacro.recon.ui.components.ReconButton
import com.maacro.recon.ui.components.ReconCheckbox
import com.maacro.recon.ui.components.ReconDateField
import com.maacro.recon.ui.components.ReconDropdownMenu
import com.maacro.recon.ui.components.ReconTextFieldSL
import com.maacro.recon.ui.components.ReconTopAppBar
import com.maacro.recon.ui.util.safeHorizontalPadding
import com.maacro.recon.ui.util.safeVerticalPadding
import com.mawmao.recon.forms.model.Field
import com.mawmao.recon.forms.model.FieldType
import com.mawmao.recon.forms.model.Section
import com.mawmao.recon.forms.model.sectionWrappers
import kotlinx.coroutines.flow.Flow
import timber.log.Timber


@Composable
fun QuestionScreen(
    vm: QuestionViewModel,
    onNavigateToMain: () -> Unit,
    onNavigateToReview: (barcode: String, answers: Map<String, FieldValue>) -> Unit,
) {

    val state by vm.state.collectAsStateWithLifecycle()
    val events = vm.events

    val hasNextPage = state.pager.currentPage < state.pager.pageCount - 1

    // could put this logic in route parameters for max flexibility
    LaunchedEffect(Unit) {
        state.pager.scrollToPage(0)
    }

    QuestionScreenEvents(
        state = state,
        events = events,
        onAction = vm::onAction,
        onNavigateToMain = onNavigateToMain,
        onNavigateOrExit = vm::navigateOrExit,
    )

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
            QuestionPage(
                section = state.currentForm.sections[page],
                fieldValues = state.fieldValues,
                onValueChange = { key, value ->
                    vm.onAction(
                        QuestionAction.FieldChange(key, value)
                    )
                }
            )
        }

        QuestionNavigationButton(
            hasNextPage = hasNextPage,
            onNext = { vm.onAction(QuestionAction.NextPage) },
            onReview = {
                onNavigateToReview(vm.mfid, state.fieldValues)
            }
        )
    }
}

@Composable
private fun QuestionScreenEvents(
    state: QuestionScreenState,
    events: Flow<QuestionEvent>,
    onAction: (QuestionAction) -> Unit,
    onNavigateToMain: () -> Unit,
    onNavigateOrExit: () -> Unit
) {
    ReconVMEvents(
        events = events,

        confirmVisible = state.isExitConfirmShown,
        confirmMessage = "Are you sure you want to exit?",
        onConfirmDismiss = { onAction(QuestionAction.ExitDismiss) },
        onConfirm = onNavigateToMain,
        onBackGesture = onNavigateOrExit

    ) { event ->
        when (event) {
            is QuestionEvent.MovePage -> {
                state.pager.animateScrollToPage(event.page)
            }
        }
    }
}

@Composable
private fun QuestionPage(
    section: Section,
    fieldValues: Map<String, FieldValue>,
    onValueChange: (String, FieldValue) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(section.title, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(4.dp))
        Text(
            section.description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(32.dp))

        section.fields.forEach { field ->
            QuestionField(
                field = field,
                value = fieldValues[field.key],
                onValueChange = { onValueChange(field.key, it) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun QuestionField(
    field: Field,
    value: FieldValue?,
    onValueChange: (FieldValue) -> Unit,
    modifier: Modifier = Modifier
) {
    when (field.type) {
        FieldType.TEXT -> {
            ReconTextFieldSL(
                modifier = modifier,
                label = field.label,
                value = (value as? FieldValue.Text)?.value.orEmpty(),
                onValueChange = { onValueChange(FieldValue.Text(it)) },
            )
        }

        FieldType.NUMBER -> {
            ReconTextFieldSL(
                modifier = modifier,
                label = field.label,
                value = (value as? FieldValue.Number)?.value.orEmpty(),
                onValueChange = { onValueChange(FieldValue.Number(it)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
        }

        FieldType.DROPDOWN -> {
            ReconDropdownMenu(
                modifier = modifier,
                label = field.label,
                options = field.options.orEmpty(),
                selectedOption = (value as? FieldValue.Dropdown)?.selected.orEmpty(),
                onOptionSelected = { onValueChange(FieldValue.Dropdown(it)) },
            )
        }

        FieldType.DATE -> {
            ReconDateField(
                value = (value as? FieldValue.Date)?.value,
                onValueChange = { onValueChange(FieldValue.Date(it)) },
                label = field.label
            )
        }

        FieldType.CHECKBOX -> {
            ReconCheckbox(
                modifier = modifier,
                checked = (value as? FieldValue.Checkbox)?.checked ?: false,
                onCheckedChange = { onValueChange(FieldValue.Checkbox(it)) }
            )
        }
    }
}

@Composable
fun QuestionNavigationButton(
    hasNextPage: Boolean,
    onNext: () -> Unit,
    onReview: () -> Unit
) {
    Box(
        modifier = Modifier
            .safeHorizontalPadding()
            .padding(bottom = 32.dp)
    ) {
        ReconButton(
            modifier = Modifier.fillMaxWidth(),
            text = if (hasNextPage) "Continue" else "Review",
            onClick = if (hasNextPage) onNext else onReview
        )
    }
}
