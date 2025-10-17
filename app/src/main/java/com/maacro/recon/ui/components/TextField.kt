package com.maacro.recon.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
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
    )
}

@Composable
fun ReconTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    colors: TextFieldColors = ReconTextFieldDefaults.colors(state),
    label: String,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = ReconTextFieldDefaults.KeyboardOptions,
    onKeyboardAction: KeyboardActionHandler? = null,
    shape: Shape = ReconTextFieldDefaults.CornerShape
) {
    OutlinedTextField(
        modifier = modifier,
        state = state,
        enabled = enabled,
        lineLimits = TextFieldLineLimits.SingleLine,
        colors = colors,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardAction,
        shape = shape,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )
        }
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
    shape: Shape = ReconTextFieldDefaults.CornerShape
) {
    OutlinedSecureTextField(
        modifier = modifier,
        state = state,
        enabled = enabled,
        colors = colors,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardAction,
        shape = shape,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    )
}
