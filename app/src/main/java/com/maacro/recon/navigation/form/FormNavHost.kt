package com.maacro.recon.navigation.form

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.toRoute
import com.maacro.recon.feature.form.ui.confirm.ConfirmScreen
import com.maacro.recon.feature.form.ui.question.QuestionScreen
import com.maacro.recon.feature.form.ui.question.QuestionViewModel
import com.maacro.recon.feature.form.ui.scan.ScanScreen
import com.maacro.recon.navigation.util.transitionComposable
import com.maacro.recon.ui.ReconAppState
import com.maacro.recon.ui.sections.FormSectionState

@Composable
fun ReconFormNavHost(
    appState: ReconAppState,
    formSectionState: FormSectionState
) {
    NavHost(
        navController = formSectionState.navController,
        startDestination = FormSection.Scan
    ) {
        transitionComposable<FormSection.Scan> { entry ->
            ScanScreen(
                onNavigateBack = appState.navController::navigateUp,
                onBarcodeScan = formSectionState::navigateToConfirm
            )
        }
        transitionComposable<FormSection.Confirm> { entry ->
            val confirm: FormSection.Confirm = entry.toRoute()
            ConfirmScreen(
                scannedBarcode = confirm.barcode,
                onContinue = formSectionState::navigateToQuestions,
                onNavigateBack = formSectionState::navigateBack,
                onExit = appState::popToMain
            )
        }
        transitionComposable<FormSection.Question> { entry ->
            val question: FormSection.Question = entry.toRoute()
            QuestionScreen(
                onNavigateToMain = appState::popToMain,
                vm = hiltViewModel<QuestionViewModel, QuestionViewModel.Factory>(
                    key = question.type
                ) { factory ->
                    factory.create(question.type)
                }
            )
        }
    }
}

