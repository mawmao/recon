package com.maacro.recon.feature.form.ui.question.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.maacro.recon.feature.form.model.FieldValue
import com.maacro.recon.ui.components.ReconCheckbox
import com.maacro.recon.ui.components.ReconDateField
import com.maacro.recon.ui.components.ReconDropdownMenu
import com.maacro.recon.ui.components.ReconTextFieldSL
import com.mawmao.recon.forms.model.Field
import com.mawmao.recon.forms.model.FieldType

@Composable
fun QuestionField(
    field: Field,
    value: FieldValue?,
    onValueChange: (FieldValue) -> Unit,
    modifier: Modifier = Modifier,
    options: List<String> = emptyList()
) {
    when (field.type) {
        FieldType.TEXT -> {
            ReconTextFieldSL(
                modifier = modifier,
                label = field.label,
                value = (value as? FieldValue.Text)?.value.orEmpty(),
                onValueChange = { onValueChange(FieldValue.Text(it)) },
            )
        }

        FieldType.NUMBER -> {
            ReconTextFieldSL(
                modifier = modifier,
                label = field.label,
                value = (value as? FieldValue.Number)?.value.orEmpty(),
                onValueChange = { onValueChange(FieldValue.Number(it)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
        }

        FieldType.DROPDOWN, FieldType.SEARCHABLE_DROPDOWN -> {
            ReconDropdownMenu(
                modifier = modifier,
                label = field.label,
                options = options,
                selectedOption = (value as? FieldValue.Dropdown)?.selected.orEmpty(),
                onOptionSelected = { onValueChange(FieldValue.Dropdown(it)) },
            )
        }

        FieldType.DATE -> {
            ReconDateField(
                value = (value as? FieldValue.Date)?.value,
                onValueChange = { onValueChange(FieldValue.Date(it)) },
                label = field.label
            )
        }

        FieldType.GPS -> {

        }

        FieldType.CHECKBOX -> {
            ReconCheckbox(
                modifier = modifier,
                checked = (value as? FieldValue.Checkbox)?.checked ?: false,
                onCheckedChange = { onValueChange(FieldValue.Checkbox(it)) }
            )
        }
    }
}
