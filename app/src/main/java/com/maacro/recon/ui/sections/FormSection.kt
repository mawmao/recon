package com.maacro.recon.ui.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.util.fastFlatMap
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.maacro.recon.core.common.measureTimeMicro
import com.maacro.recon.feature.form.model.FieldValue
import com.maacro.recon.feature.form.model.FormType
import com.maacro.recon.navigation.form.FormSection
import com.maacro.recon.navigation.form.ReconFormNavHost
import com.maacro.recon.ui.ReconAppState
import com.mawmao.recon.forms.model.Repeatable
import com.mawmao.recon.forms.model.RepeatableMetadata
import com.mawmao.recon.forms.model.Section

@Composable
fun FormSection(
    appState: ReconAppState,
    formSectionState: FormSectionState,
) {
    ReconFormNavHost(
        appState = appState,
        formSectionState = formSectionState,
    )
}

@Composable
fun rememberFormSectionState(
    formType: String,
    formNavController: NavHostController = rememberNavController()
) =
    remember(formNavController) {
        FormSectionState(navController = formNavController, formType = formType)
    }

@Stable
class FormSectionState(
    val navController: NavHostController,
    val formType: String,
) {

    var mfid: String? = null
        private set

    val form = mutableStateOf(FormType.fromName(formType).template)

    private val _answers = mutableStateMapOf<String, FieldValue>()
    val answers: Map<String, FieldValue> get() = _answers

    val isMfidSet: Boolean get() = mfid != null

    val mfidOrThrow: String
        get() = mfid ?: error("mfid not set yet")


    fun getSections(): List<Section> = measureTimeMicro("getSections") {
        form.value.elements.fastFlatMap { element ->
            when (element) {
                is Section -> listOf(element)
                is Repeatable -> element.sections
            }
        }
    }

    fun getRepeatableInfo(sections: List<Section>) =
        sections.mapIndexed { index, _ ->
            getRepeatableInstanceLabel(index).let { (title, instance) ->
                RepeatableMetadata(
                    title = title,
                    instance = instance,
                    isLast = isLastIndexOfARepeatable(index),
                    isRemovable = canRemoveRepeatable(index)
                )
            }
        }


    fun canRemoveRepeatable(sectionIndex: Int): Boolean {
        val elements = form.value.elements
        var count = 0

        for (el in elements) {
            when (el) {
                is Section -> count++
                is Repeatable -> {
                    val sectionCount = el.sections.size
                    val templateSize = el.templateSections.size
                    val end = count + sectionCount

                    if (sectionIndex in count until end) {
                        return el.sections.size > templateSize
                    }
                    count = end
                }
            }
        }
        return false
    }

    fun removeRepeatableInstance(groupId: String, sectionIndex: Int, onPageUpdate: (Int) -> Unit) {
        val formValue = form.value
        val elements = formValue.elements.toMutableList()

        val targetIndex = elements.indexOfFirst { it is Repeatable && it.groupId == groupId }
        if (targetIndex == -1) return

        val target = elements[targetIndex] as Repeatable
        val templateSize = target.templateSections.size
        if (target.sections.size <= templateSize) return // prevent removing the last instance

        var count = 0
        for (el in elements.take(targetIndex)) {
            count += when (el) {
                is Section -> 1
                is Repeatable -> el.sections.size
            }
        }

        val relativeIndex = sectionIndex - count
        val instanceIndex = relativeIndex / templateSize
        val start = instanceIndex * templateSize
        val end = start + templateSize

        val updatedSections = target.sections.toMutableList().apply {
            subList(start, end).clear()
        }

        val updatedRepeatable = target.copy(sections = updatedSections)
        elements[targetIndex] = updatedRepeatable
        form.value = formValue.copy(elements = elements)

        val newPage = maxOf(0, sectionIndex - templateSize)
        onPageUpdate(newPage)
    }

    fun getRepeatableInstanceLabel(sectionIndex: Int): Pair<String, Int> {
        val elements = form.value.elements
        var count = 0

        for (el in elements) {
            when (el) {
                is Section -> count++
                is Repeatable -> {
                    val sectionCount = el.sections.size
                    val templateSize = el.templateSections.size
                    val end = count + sectionCount

                    if (sectionIndex in count until end) {
                        val instanceIndex = (sectionIndex - count) / templateSize
                        return Pair(el.title, instanceIndex + 2)
                    }

                    count = end
                }
            }
        }

        return Pair("", -1)
    }

    fun isLastIndexOfARepeatable(sectionIndex: Int): Boolean {
        val elements = form.value.elements
        var count = 0
        for (el in elements) {
            when (el) {
                is Section -> {
                    if (count == sectionIndex) return false
                    count++
                }

                is Repeatable -> {
                    val end = count + el.sections.size
                    if (sectionIndex == end - 1) return true
                    count = end
                }
            }
        }
        return false
    }

    fun addRepeatableInstance(groupId: String, onPageUpdate: (Int) -> Unit) =
        measureTimeMicro("addRepeatableInstance") {
            val formValue = form.value
            val elements = formValue.elements

            val targetIndex = elements.indexOfFirst { it is Repeatable && it.groupId == groupId }
            if (targetIndex == -1) return

            val target = elements[targetIndex] as Repeatable

            val existingInstances = target.sections.size / target.templateSections.size
            val instanceIndex = existingInstances // 0-based

            val startIndex = elements.take(targetIndex).sumOf { el ->
                when (el) {
                    is Section -> 1
                    is Repeatable -> el.sections.size
                }
            } + target.sections.size

            val duplicated = target.templateSections.map { section ->
                section.copy(
                    key = "${section.key}_$instanceIndex",
                    fields = section.fields.map { f -> f.copy(key = "${f.key}_$instanceIndex") }
                )
            }

            val updatedRepeatable = target.copy(sections = target.sections + duplicated)

            val updatedElements = elements.mapIndexed { index, el ->
                if (index == targetIndex) updatedRepeatable else el
            }

            form.value = formValue.copy(elements = updatedElements)
            onPageUpdate(startIndex)
        }


    fun updateMfid(newMfid: String) {
        mfid = newMfid
    }

    fun updateAnswer(key: String, value: FieldValue) {
        ensureReady()
        _answers[key] = value
    }

    fun navigateToQuestions() = navController.navigate(FormSection.Question) {
        launchSingleTop = true
    }

    fun navigateToReview() = navController.navigate(FormSection.Review) {
        launchSingleTop = true
    }

    fun navigateToConfirm() = navController.navigate(FormSection.Confirm) {
        launchSingleTop = true
    }

    fun navigateBack() {
        navController.navigateUp()
    }

    private fun ensureReady() {
        check(isMfidSet) { "mfid not set before updating answers" }
    }
}
