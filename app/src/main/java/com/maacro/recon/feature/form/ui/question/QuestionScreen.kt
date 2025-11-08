package com.maacro.recon.feature.form.ui.question

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maacro.recon.core.common.ReconConfirmEvent
import com.maacro.recon.feature.form.model.FieldValue
import com.maacro.recon.feature.form.ui.question.components.QuestionNavigationButtons
import com.maacro.recon.feature.form.ui.question.components.QuestionPage
import com.maacro.recon.ui.components.ReconTopAppBar
import com.maacro.recon.ui.sections.FormSectionState
import com.maacro.recon.ui.util.safeHorizontalPadding
import com.maacro.recon.ui.util.safeVerticalPadding
import com.mawmao.recon.forms.model.RepeatableMetadata
import com.mawmao.recon.forms.model.Section


@Composable
fun QuestionScreen(
    vm: QuestionViewModel = hiltViewModel(),
    formSectionState: FormSectionState,
    onNavigateToMain: () -> Unit,
    onNavigateToReview: () -> Unit,
) {
    val state by vm.state.collectAsStateWithLifecycle()
    val sections = formSectionState.getSections()
    val repeatableInfo by remember(formSectionState.form.value) {
        derivedStateOf { formSectionState.getRepeatableInfo(sections) }
    }
    val info = repeatableInfo[state.currentPage]

    val pagerController = rememberPagerController(
        page = { state.currentPage },
        pageCount = { sections.size },
    )

    QuestionScreenEvents(
        isExitDialogShown = state.isExitDialogShown,
        onAction = vm::onAction,
        onNavigateToMain = onNavigateToMain,
        onNavigateOrExit = { vm.onAction(QuestionAction.BackRequest) },
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeVerticalPadding()
    ) {
        ReconTopAppBar(
            modifier = Modifier.safeHorizontalPadding(),
            onBackTap = { vm.onAction(QuestionAction.BackRequest) },
            onBackLongTap = { vm.onAction(QuestionAction.ExitRequest) },
            actions = {
                Text(
                    text = "${state.currentPage + 1} of ${sections.size}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }
        )

        QuestionsPager(
            pagerState = pagerController.asPagerState(),
            getCurrentSection = { sections[it] },
            answers = formSectionState.answers,
            onAnswerChange = formSectionState::updateAnswer
        )

        QuestionNavigationButtons(
            hasNextPage = state.currentPage < sections.size - 1,
            repeatableTitle = info.title,
            instance = info.instance,
            onAddRepeatable =
                if (info.isLast) {
                    {
                        formSectionState.addRepeatableInstance(
                            groupId = sections[state.currentPage].groupId,
                            onPageUpdate = { vm.onAction(QuestionAction.PageRequest(it)) }
                        )
                    }
                } else null,
            onRemoveRepeatable =
                if (formSectionState.canRemoveRepeatable(state.currentPage)) {
                    {
                        formSectionState.removeRepeatableInstance(
                            groupId = sections[state.currentPage].groupId,
                            sectionIndex = state.currentPage,
                            onPageUpdate = { vm.onAction(QuestionAction.PageRequest(it)) }
                        )
                    }
                } else null,
            onNext = { vm.onAction(QuestionAction.PageRequest(state.currentPage + 1)) },
            onReview = onNavigateToReview
        )
    }
}


@Composable
private fun QuestionScreenEvents(
    isExitDialogShown: Boolean,
    onAction: (QuestionAction) -> Unit,
    onNavigateToMain: () -> Unit,
    onNavigateOrExit: () -> Unit,
) {
    BackHandler(onBack = onNavigateOrExit)
    ReconConfirmEvent(
        confirmVisible = isExitDialogShown,
        confirmMessage = "Are you sure you want to exit?",
        onConfirmDismiss = { onAction(QuestionAction.ExitDismiss) },
        onConfirm = onNavigateToMain,
    )
}

@Composable
private fun ColumnScope.QuestionsPager(
    pagerState: PagerState,
    getCurrentSection: (Int) -> Section,
    answers: Map<String, FieldValue>,
    onAnswerChange: (String, FieldValue) -> Unit
) {
    HorizontalPager(
        userScrollEnabled = false,
        contentPadding = PaddingValues(horizontal = 16.dp),
        beyondViewportPageCount = 1,
        pageSpacing = 16.dp,
        state = pagerState,
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
    ) { page ->
        QuestionPage(
            section = getCurrentSection(page),
            fieldValues = answers,
            onValueChange = onAnswerChange
        )
    }
}

