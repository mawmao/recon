package com.maacro.recon.feature.form.model

import kotlinx.serialization.Serializable

@Serializable
sealed class FieldValue {

    @Serializable
    data class Text(val value: String) : FieldValue()

    @Serializable
    data class Number(val value: String) : FieldValue()

    @Serializable
    data class Date(val value: String) : FieldValue()

    @Serializable
    data class Dropdown(val selected: String) : FieldValue()

    @Serializable
    data class Checkbox(val checked: Boolean) : FieldValue()
}

fun FieldValue?.toDisplay(): String = when (this) {
    is FieldValue.Text -> value
    is FieldValue.Number -> value
    is FieldValue.Dropdown -> selected
    is FieldValue.Date -> value
    is FieldValue.Checkbox -> if (checked) "Yes" else "No"
    null -> "No Answer"
}
