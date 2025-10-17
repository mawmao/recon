package com.maacro.recon.feature.form.model

import androidx.compose.runtime.Immutable

@Immutable
data class Form(
    val type: String = FormType.UNKNOWN,
    val sections: List<Section>
)

@Immutable
data class Section(
    val id: String,
    val title: String,
    val description: String,
    val fields: List<FieldRow>
)

@Immutable
data class FieldRow(
    val fields: List<Field>
) {
    constructor(field: Field) : this(listOf(field))
}

@Immutable
data class Field(
    val key: String,
    val label: String,
    val type: FieldType,
    val placeholder: String,
    val required: Boolean = false,
    val options: List<String>? = null
)

enum class FieldType {
    TEXT, NUMBER, DATE, DROPDOWN, CHECKBOX
}