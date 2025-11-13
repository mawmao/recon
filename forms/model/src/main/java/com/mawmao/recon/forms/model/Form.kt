package com.mawmao.recon.forms.model

import kotlinx.serialization.Serializable

sealed interface FormElement

data class Form(
    val label: String,
    val elements: List<FormElement>
)

data class Section(
    val key: String,
    val title: String,
    val groupId: String = "",
    val description: String,
    val fields: List<Field>
) : FormElement

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

data class Field(
    val key: String,
    val label: String,
    val type: FieldType,
    val required: Boolean = true,
    val options: FieldOptions? = null,
    val valueProvider: FieldDataSource? = null,
    val dependsOn: String = ""
)

sealed class FieldOptions {
    data class Static(val options: List<String>) : FieldOptions()
    data class Dynamic<T : Any>(val provider: suspend (dependsOnValue: String?) -> T) :
        FieldOptions()
}

sealed class FieldDataSource {
    data class Static(val options: List<String>) : FieldDataSource()
    data class Dynamic<T>(val source: suspend (parentValue: String?) -> T) : FieldDataSource()
    data class Computed<T>(val source: suspend () -> T) : FieldDataSource()
}

@Serializable
enum class FieldType {
    TEXT,
    NUMBER,
    DATE,
    DROPDOWN,
    SEARCHABLE_DROPDOWN,
    CHECKBOX,
    GPS,
    // CAMERA/IMAGE
}


