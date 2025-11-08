package com.maacro.recon.navigation.form

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import com.maacro.recon.feature.form.ui.confirm.ConfirmScreen
import com.maacro.recon.feature.form.ui.question.QuestionScreen
import com.maacro.recon.feature.form.ui.review.ReviewScreen
import com.maacro.recon.feature.form.ui.review.ReviewViewModel
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
                formSectionState = formSectionState,
                onNavigateBack = appState.navController::navigateUp,
                onBarcodeScan = formSectionState::navigateToConfirm

            )
        }
        transitionComposable<FormSection.Confirm> { entry ->
            ConfirmScreen(
                formSectionState = formSectionState,
                onContinue = formSectionState::navigateToQuestions,
                onNavigateBack = formSectionState::navigateBack,
                onExit = appState::popToMain
            )
        }
        transitionComposable<FormSection.Question> { entry ->
            QuestionScreen(
                formSectionState = formSectionState,
                onNavigateToMain = appState::popToMain,
                onNavigateToReview = formSectionState::navigateToReview,
            )
        }

        transitionComposable<FormSection.Review> { entry ->
            ReviewScreen(
                formSectionState = formSectionState,
                onNavigateToMain = appState::popToMain,
                onNavigateBackToQuestions = formSectionState::navigateBack,
                vm = hiltViewModel<ReviewViewModel, ReviewViewModel.Factory>(
                    key = formSectionState.formType
                ) { factory ->
                    factory.create(
                        type = formSectionState.formType,
                        mfid = formSectionState.mfidOrThrow
                    )
                }
            )
        }
    }
}

