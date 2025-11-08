package com.mawmao.recon.forms.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface FormElement

@Serializable
data class Form(
    val label: String,
    val elements: List<FormElement>
)

fun Form.sections(): List<Section> = elements.flatMap { element ->
    when (element) {
        is Section -> listOf(element)
        is Repeatable -> element.sections
    }
}

@Serializable
data class Section(
    val key: String,
    val title: String,
    val groupId: String = "",
    val description: String,
    val fields: List<Field>
) : FormElement

@Serializable
data class Repeatable(
    val groupId: String,
    val title: String,
    val templateSections: List<Section>,
    val sections: List<Section> = templateSections
) : FormElement

data class RepeatableMetadata(
    val title: String,
    val instance: Int,
    val isLast: Boolean,
    val isRemovable: Boolean = true
)

@Serializable
data class Field(
    val key: String,
    val label: String,
    val type: FieldType,
    val required: Boolean = true,
    val options: List<String>? = null
)

@Serializable
enum class FieldType {
    TEXT, NUMBER, DATE, DROPDOWN, CHECKBOX
}

