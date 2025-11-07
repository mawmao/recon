package com.maacro.recon.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.ImeAction
import com.maacro.recon.ui.theme.ReconTokens

object ReconTextFieldDefaults {

    val KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Default)
    val CornerShape = ReconTokens.RoundedCornerShape

    @Composable
    fun colors(state: TextFieldState): TextFieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedBorderColor = if (state.text.isEmpty()) {
            MaterialTheme.colorScheme.onSurfaceVariant
        } else {
            MaterialTheme.colorScheme.onSurface
        },
        disabledBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45F),
        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45F),
        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45F)
    )

    @Composable
    fun colors(value: String): TextFieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedBorderColor = if (value.isEmpty()) {
            MaterialTheme.colorScheme.onSurfaceVariant
        } else {
            MaterialTheme.colorScheme.onSurface
        },
        disabledBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45F),

        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45F),
        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45F)
    )
}


@Composable
fun ReconTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    colors: TextFieldColors = ReconTextFieldDefaults.colors(state),
    label: String,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    keyboardOptions: KeyboardOptions = ReconTextFieldDefaults.KeyboardOptions,
    onKeyboardAction: KeyboardActionHandler? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    shape: Shape = ReconTextFieldDefaults.CornerShape
) {
    OutlinedTextField(
        modifier = modifier,
        state = state,
        enabled = enabled,
        lineLimits = TextFieldLineLimits.SingleLine,
        colors = colors,
        interactionSource = interactionSource,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardAction,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        shape = shape,
        label = { Text(text = label, style = MaterialTheme.typography.bodyMedium) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReconTextFieldSL(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    colors: TextFieldColors = ReconTextFieldDefaults.colors(value),
    label: String,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    keyboardOptions: KeyboardOptions = ReconTextFieldDefaults.KeyboardOptions,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    shape: Shape = ReconTextFieldDefaults.CornerShape,
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        readOnly = readOnly,
        enabled = enabled,
        singleLine = true,
        interactionSource = interactionSource,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        shape = shape,
        colors = colors,
        label = { Text(text = label, style = MaterialTheme.typography.bodyMedium) },
    )
}

@Composable
fun ReconSecureTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    colors: TextFieldColors = ReconTextFieldDefaults.colors(state),
    label: String,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = ReconTextFieldDefaults.KeyboardOptions,
    onKeyboardAction: KeyboardActionHandler? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    shape: Shape = ReconTextFieldDefaults.CornerShape
) {
    OutlinedSecureTextField(
        modifier = modifier,
        state = state,
        enabled = enabled,
        colors = colors,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardAction,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        shape = shape,
        label = { Text(text = label, style = MaterialTheme.typography.bodyMedium) }
    )
}

