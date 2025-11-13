package com.maacro.recon.navigation.form

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import com.maacro.recon.feature.form.model.CollectionStatus
import com.maacro.recon.feature.form.ui.confirm.ConfirmScreen
import com.maacro.recon.feature.form.ui.review.ReviewScreen
import com.maacro.recon.feature.form.ui.review.ReviewViewModel
import com.maacro.recon.feature.form.ui.scan.ScanScreen
import com.maacro.recon.navigation.util.sharedViewModel
import com.maacro.recon.navigation.util.transitionComposable
import com.maacro.recon.navigation.util.transitionNavigationGraph
import com.maacro.recon.ui.ReconAppState
import com.maacro.recon.ui.common.EmptyScreen
import com.maacro.recon.ui.components.Monitor
import com.maacro.recon.ui.components.ReconButton
import com.maacro.recon.ui.sections.FormSectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber


@HiltViewModel
class QuestionSharedViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(0)
    val state = _state.asStateFlow()

    fun increment() {
        _state.value++
    }

    fun decrement() {
        _state.value--
    }

    override fun onCleared() {
        super.onCleared()
        Timber.d("QuestionSharedViewModel cleared")
    }
}

@Composable
fun ReconFormNavHost(
    appState: ReconAppState,
    formSectionState: FormSectionState,
) {
    NavHost(
        navController = formSectionState.navController,
        startDestination = FormSection.Scan
    ) {
        transitionComposable<FormSection.Scan> { entry ->
            Monitor(onCompose = { CollectionStatus.Log.v(CollectionStatus.Scan.Start()) }) {
                ScanScreen(
                    formSectionState = formSectionState,
                    onNavigateBack = {
                        appState.navController.navigateUp()
                        CollectionStatus.Log.i(CollectionStatus.Scan.Cancel())
                    },
                    onBarcodeScan = {
                        formSectionState.navigateToConfirm()
                        CollectionStatus.Log.i(CollectionStatus.Scan.Success(formSectionState.mfid))
                    }
                )
            }
        }
        transitionComposable<FormSection.Confirm> { entry ->
            Monitor(onCompose = { CollectionStatus.Log.v(CollectionStatus.Confirm.Start()) }) {
                ConfirmScreen(
                    formSectionState = formSectionState,
                    onContinue = {
                        formSectionState.navigateToQuestions()
                        CollectionStatus.Log.i(CollectionStatus.Confirm.Success())
                    },
                    onNavigateBack = {
                        formSectionState.navigateBack()
                        CollectionStatus.Log.i(CollectionStatus.Confirm.Cancel())
                    },
                    onExit = {
                        appState.popToMain()
                        CollectionStatus.Log.i(CollectionStatus.Confirm.Cancel())
                    }
                )
            }
        }

        transitionNavigationGraph<FormSection.Question.Root>(
            // start destination should be a `when` block depending on the first section's type
            startDestination = FormSection.Question.Basic
        ) {
            transitionComposable<FormSection.Question.Basic> { entry ->
                val vm = entry.sharedViewModel<QuestionSharedViewModel>(formSectionState.navController)
                val state by vm.state.collectAsStateWithLifecycle()
                EmptyScreen(text = "Basic Form Question Page $state") {
                    ReconButton(
                        text = "Go to Async Page",
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            formSectionState.navController.navigate(FormSection.Question.Async)
                            vm.increment()
                        }
                    )
                }
            }
            transitionComposable<FormSection.Question.Async> { entry ->
                val vm = entry.sharedViewModel<QuestionSharedViewModel>(formSectionState.navController)
                val state by vm.state.collectAsStateWithLifecycle()
                BackHandler {
                    formSectionState.navController.navigateUp()
                    vm.decrement()
                }
                EmptyScreen(text = "Async Form Question Page $state")
            }
        }


//        transitionComposable<FormSection.Question> { entry ->
//            Monitor(onCompose = { CollectionStatus.Log.v(CollectionStatus.Question.Start()) }) {
//                QuestionScreen(
//                    formSectionState = formSectionState,
//                    onNavigateToMain = {
//                        appState.popToMain()
//                        CollectionStatus.Log.i(CollectionStatus.Question.Cancel())
//                    },
//                    onNavigateToReview = {
//                        formSectionState.navigateToReview()
//                        CollectionStatus.Log.v(CollectionStatus.Question.End())
//                    },
//                )
//            }
//        }

        transitionComposable<FormSection.Review> { entry ->
            Monitor(onCompose = { CollectionStatus.Log.v(CollectionStatus.Review.Start()) }) {
                ReviewScreen(
                    formSectionState = formSectionState,
                    onNavigateToMain = {
                        appState.popToMain()
                        CollectionStatus.Log.i(CollectionStatus.Review.Cancel())
                    },
                    onNavigateBackToQuestions = {
                        formSectionState.navigateBack()
                        CollectionStatus.Log.i(CollectionStatus.Review.Cancel())
                    },
                    onSuccess = {
                        appState.popToMain()
                        CollectionStatus.Log.i(CollectionStatus.Review.Success())
                        CollectionStatus.Log.i(CollectionStatus.Collect.Success(formSectionState.formType))
                    },
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
}

