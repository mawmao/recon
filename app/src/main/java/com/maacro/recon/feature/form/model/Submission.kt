package com.maacro.recon.feature.form.model

import com.mawmao.recon.forms.model.Form
import com.mawmao.recon.forms.model.Repeatable
import com.mawmao.recon.forms.model.Section
import kotlinx.serialization.Serializable

@Serializable
sealed interface SubmissionResult {
    @Serializable
    data class Single(val ungrouped: Map<String, FieldValue>) : SubmissionResult

    @Serializable
    data class Dynamic(
        val fixedSections: Map<String, FieldValue>,
        val dynamicSections: Map<String, List<Map<String, FieldValue>>>
    ) : SubmissionResult
}


fun buildSubmission(form: Form, fieldValues: Map<String, FieldValue>): SubmissionResult {
    val fixedSections = mutableMapOf<String, FieldValue>()
    val dynamicSections = mutableMapOf<String, MutableList<Map<String, FieldValue>>>()

    for (el in form.elements) {
        when (el) {
            is Section -> {
                for (f in el.fields) {
                    fieldValues[f.key]?.let { fixedSections[f.key] = it }
                }
            }

            is Repeatable -> {
                val instancesMap = mutableMapOf<Int, MutableMap<String, FieldValue>>()
                val regex =
                    Regex("(.+?)(?:_(\\d+))?$") // capture base key and optional numeric suffix

                for (section in el.sections) {
                    for (f in section.fields) {
                        fieldValues.entries.forEach { (key, value) ->
                            val match = regex.matchEntire(key) ?: return@forEach
                            val baseKey = match.groupValues[1]
                            val suffix =
                                match.groupValues[2].takeIf { it.isNotEmpty() }?.toInt() ?: 0

                            if (baseKey == f.key) {
                                val instance = instancesMap.getOrPut(suffix) { mutableMapOf() }
                                instance[f.key] = value
                            }
                        }
                    }
                }

                val instances = instancesMap.toSortedMap().values.map { it.toMap() }.toMutableList()
                dynamicSections[el.groupId] = instances
            }
        }
    }

    return if (dynamicSections.isEmpty()) SubmissionResult.Single(fixedSections)
    else SubmissionResult.Dynamic(fixedSections, dynamicSections)
}
