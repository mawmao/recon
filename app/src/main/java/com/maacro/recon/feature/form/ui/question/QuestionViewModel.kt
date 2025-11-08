package com.maacro.recon.feature.form.ui.question

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maacro.recon.core.domain.VMActions
import com.maacro.recon.core.domain.VMState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch


@HiltViewModel
class QuestionViewModel @Inject constructor(
) : ViewModel(), VMActions<QuestionAction> {

    private val _state = VMState(initialState = QuestionScreenState(currentPage = 0))
    val state = _state.flow

    override fun onAction(action: QuestionAction) {
        when (action) {
            is QuestionAction.ExitRequest -> toggleExitDialog(true)
            is QuestionAction.ExitDismiss -> toggleExitDialog(false)
            is QuestionAction.PageRequest -> updatePage(action.page)
            is QuestionAction.NextRequest -> updatePage(_state.value.currentPage + 1)
            is QuestionAction.BackRequest -> {
                val currentPage = _state.value.currentPage
                if (currentPage == 0) toggleExitDialog(true)
                else updatePage(currentPage - 1)
            }
        }
    }

    private fun toggleExitDialog(show: Boolean) =
        _state.update { it.copy(isExitDialogShown = show) }

    private fun updatePage(page: Int) = viewModelScope.launch {
        _state.update { it.copy(currentPage = page) }
    }

    /**
     * Use this to sync page state only if UI page update also happens (gestures, swipes)
     *
     * `fun onUserPageUpdate(page: Int) = _state.update { it.copy(currentPage = page) }`
     */
}

data class QuestionScreenState(
    val currentPage: Int,
    val isExitDialogShown: Boolean = false,
)

sealed class QuestionAction {
    object ExitRequest : QuestionAction()
    object ExitDismiss : QuestionAction()
    object BackRequest : QuestionAction()
    object NextRequest : QuestionAction()
    data class PageRequest(val page: Int) : QuestionAction()
}


