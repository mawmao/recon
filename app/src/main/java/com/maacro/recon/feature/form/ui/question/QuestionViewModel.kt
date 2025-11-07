package com.maacro.recon.feature.form.ui.question

import androidx.compose.foundation.pager.PagerState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maacro.recon.core.domain.VMActions
import com.maacro.recon.core.domain.VMEvents
import com.maacro.recon.core.domain.VMState
import com.maacro.recon.feature.form.model.FieldValue
import com.maacro.recon.feature.form.model.FormType
import com.mawmao.recon.forms.model.Form
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class FormAnswers(
    val singleAnswers: Map<String, FieldValue>,
    val repeatedGroupAnswers: Map<String, List<Map<String, FieldValue>>>
)

//class FormPagerState(
//    var form: Form,
//    initialPage: Int = 0
//) : PagerState(currentPage = initialPage) {
//
//    private var allSections: List<Section> = computeAllSections(form)
//    private var groupIndexMap: Map<String, List<Int>> = computeGroupIndexMap(allSections)
//
//    private fun computeAllSections(form: Form): List<Section> =
//        form.elements.flatMap { element ->
//            when (element) {
//                is Section -> listOf(element)
//                is Repeatable -> element.instances.flatten().ifEmpty { element.sectionTemplates }
//            }
//        }
//
//    private fun computeGroupIndexMap(sections: List<Section>) =
//        sections.withIndex().groupBy({ it.value.groupId }) { it.index }
//
//    fun updateForm(newForm: Form) {
//        form = newForm
//        allSections = computeAllSections(newForm)
//        groupIndexMap = computeGroupIndexMap(allSections)
//    }
//
//    override val pageCount get() = allSections.size
//    fun getSection(page: Int) = allSections[page]
//
//    fun getSectionsByGroup(groupId: String): List<Section> =
//        groupIndexMap[groupId]?.map { allSections[it] } ?: emptyList()
//
//    suspend fun scrollToGroup(groupId: String) {
//        val firstIndex = groupIndexMap[groupId]?.firstOrNull() ?: return
//        scrollToPage(firstIndex)
//    }
//
//    val currentGroupId: String
//        get() = getSection(currentPage).groupId
//
//    val currentGroupFirstIndex: Int
//        get() = currentGroupId.takeIf { it.isNotEmpty() }?.let { groupIndexMap[it]?.first() } ?: -1
//
//    val currentGroupLastIndex: Int
//        get() = currentGroupId.takeIf { it.isNotEmpty() }?.let { groupIndexMap[it]?.last() } ?: -1
//
//    val currentGroupPageCount: Int
//        get() = currentGroupId.takeIf { it.isNotEmpty() }?.let { groupIndexMap[it]?.size } ?: 0
//
//    val currentIndexInGroup: Int
//        get() {
//            val group = currentGroupId.takeIf { it.isNotEmpty() } ?: return -1
//            val indices = groupIndexMap[group] ?: return -1
//            return indices.indexOf(currentPage)
//        }
//
//    val isFirstInGroup: Boolean
//        get() = currentIndexInGroup == 0
//
//    val isLastInGroup: Boolean
//        get() = currentIndexInGroup == currentGroupPageCount - 1
//
//}

@HiltViewModel(assistedFactory = QuestionViewModel.Factory::class)
class QuestionViewModel @AssistedInject constructor(
    @Assisted("formType") val formTypeName: String,
    @Assisted("mfid") val mfid: String
) : ViewModel(), VMActions<QuestionAction> {

    val currentForm = FormType.fromName(formTypeName).template

    private val _state = VMState(
        initialState = QuestionScreenState(
            currentForm = currentForm,
            pager = PagerState(
                pageCount = { currentForm.sections.size }
            )
        ))

    val state = _state.flow

    private val _events = VMEvents<QuestionEvent>()
    val events = _events.events

    override fun onAction(action: QuestionAction) {
        val pager = _state.value.pager
        when (action) {
            is QuestionAction.PrevPage -> goToPage(pager.currentPage - 1)
            is QuestionAction.NextPage -> goToPage(pager.currentPage + 1)
            is QuestionAction.ExitClick -> _state.update { it.copy(isExitConfirmShown = true) }
            is QuestionAction.ExitDismiss -> _state.update { it.copy(isExitConfirmShown = false) }
            is QuestionAction.FieldChange -> {
                _state.update {
                    it.copy(fieldValues = it.fieldValues + (action.fieldId to action.fieldValue))
                }
            }
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

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("formType") type: String,
            @Assisted("mfid") mfid: String
        ): QuestionViewModel
    }
}

data class QuestionScreenState(
    val currentForm: Form,
    val pager: PagerState,
    val isExitConfirmShown: Boolean = false,
    val fieldValues: Map<String, FieldValue> = emptyMap()
)

sealed class QuestionEvent {
    data class MovePage(val page: Int) : QuestionEvent()
}

sealed class QuestionAction {
    object PrevPage : QuestionAction()
    object NextPage : QuestionAction()
    object ExitClick : QuestionAction()
    object ExitDismiss : QuestionAction()
    data class FieldChange(val fieldId: String, val fieldValue: FieldValue) : QuestionAction()
}
