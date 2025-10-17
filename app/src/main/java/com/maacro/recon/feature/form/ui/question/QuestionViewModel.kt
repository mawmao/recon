package com.maacro.recon.feature.form.ui.question

import androidx.compose.foundation.pager.PagerState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maacro.recon.core.domain.VMActions
import com.maacro.recon.core.domain.VMEvents
import com.maacro.recon.core.domain.VMState
import com.maacro.recon.feature.form.data.FormTemplates
import com.maacro.recon.feature.form.model.Form
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = QuestionViewModel.Factory::class)
class QuestionViewModel @AssistedInject constructor(
    @Assisted val formType: String
) : ViewModel(), VMActions<QuestionAction> {

    private val _state = VMState(initialState = questionUiState(formType = formType))
    val state = _state.flow

    private val _events = VMEvents<QuestionEvent>()
    val events = _events.events

    override fun onAction(action: QuestionAction) {
        val pager = _state.value.pager
        when (action) {
            is QuestionAction.PrevPage -> goToPage(pager.currentPage - 1)
            is QuestionAction.NextPage -> goToPage(pager.currentPage + 1)
            is QuestionAction.ExitClick -> _state.update { it.copy(isExitConfirmShown = true) }
        }
    }

    fun navigateOrExit() {
        if (_state.value.pager.currentPage == 0) {
            onAction(QuestionAction.ExitClick)
        } else {
            onAction(QuestionAction.PrevPage)
        }
    }

    private fun goToPage(page: Int) = viewModelScope.launch {
        _events.sendEvent(QuestionEvent.MovePage(page))
    }

    fun hideExitConfirmation() = _state.update { it.copy(isExitConfirmShown = false) }

    @AssistedFactory
    interface Factory {
        fun create(type: String): QuestionViewModel
    }
}

private fun questionUiState(formType: String): QuestionScreenState {
    val form = FormTemplates.getFormByType(formType)!!

    return QuestionScreenState(
        currentForm = form,
        pager = PagerState(pageCount = { form.sections.size })
    )
}

data class QuestionScreenState(
    val currentForm: Form,
    val pager: PagerState,
    val isExitConfirmShown: Boolean = false,
)

sealed class QuestionEvent {
    data class MovePage(val page: Int) : QuestionEvent()
}

sealed class QuestionAction {
    object PrevPage : QuestionAction()
    object NextPage : QuestionAction()
    object ExitClick : QuestionAction()
}