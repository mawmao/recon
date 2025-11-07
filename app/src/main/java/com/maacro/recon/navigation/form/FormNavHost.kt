package com.maacro.recon.navigation.form

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.toRoute
import com.maacro.recon.feature.form.model.FieldValue
import com.maacro.recon.feature.form.ui.confirm.ConfirmScreen
import com.maacro.recon.feature.form.ui.question.FormAnswers
import com.maacro.recon.feature.form.ui.question.QuestionScreen
import com.maacro.recon.feature.form.ui.question.QuestionViewModel
import com.maacro.recon.feature.form.ui.review.ReviewScreen
import com.maacro.recon.feature.form.ui.review.ReviewViewModel
import com.maacro.recon.feature.form.ui.scan.ScanScreen
import com.maacro.recon.navigation.util.transitionComposable
import com.maacro.recon.ui.ReconAppState
import com.maacro.recon.ui.sections.FormSectionState
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

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
            val routeArgs: FormSection.Confirm = entry.toRoute()

            ConfirmScreen(
                scannedBarcode = routeArgs.mfid,
                onContinue = formSectionState::navigateToQuestions,
                onNavigateBack = formSectionState::navigateBack,
                onExit = appState::popToMain
            )
        }
        transitionComposable<FormSection.Question> { entry ->
            val routeArgs: FormSection.Question = entry.toRoute()

            QuestionScreen(
                onNavigateToMain = appState::popToMain,
                onNavigateToReview = formSectionState::navigateToReview,
                vm = hiltViewModel<QuestionViewModel, QuestionViewModel.Factory>(
                    key = routeArgs.formTypeName
                ) { factory ->
                    factory.create(type = routeArgs.formTypeName, mfid = routeArgs.mfid)
                }
            )
        }

        transitionComposable<FormSection.Review> { entry ->
            val routeArgs: FormSection.Review = entry.toRoute()
            val answers = Json.decodeFromString<Map<String, FieldValue>>(routeArgs.answersJson)

            ReviewScreen(
                onNavigateToMain = appState::popToMain,
                onNavigateBackToQuestions = formSectionState::navigateBack,
                answers = answers,
                vm = hiltViewModel<ReviewViewModel, ReviewViewModel.Factory>(
                    key = routeArgs.formTypeName
                ) { factory ->
                    factory.create(type = routeArgs.formTypeName, mfid = routeArgs.mfid)
                }
            )
        }
    }
}

