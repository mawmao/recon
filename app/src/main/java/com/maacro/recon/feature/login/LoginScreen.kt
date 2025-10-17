package com.maacro.recon.feature.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maacro.recon.core.common.ReconScreenEvents
import com.maacro.recon.ui.common.ConnectivityIndicator
import com.maacro.recon.ui.common.ReconLogo
import com.maacro.recon.ui.common.ReconRegion
import com.maacro.recon.ui.components.ReconCheckbox
import com.maacro.recon.ui.components.ReconLoadingButton
import com.maacro.recon.ui.components.ReconSecureTextField
import com.maacro.recon.ui.components.ReconTextButton
import com.maacro.recon.ui.components.ReconTextField
import com.maacro.recon.ui.util.debug
import com.maacro.recon.ui.util.safePadding

private const val DEBUG = false;

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onAuthSuccess: () -> Unit,
    onForgotPassword: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val errors by viewModel.errors.collectAsStateWithLifecycle()
    val events = viewModel.events

    ReconScreenEvents(
        events = events,
        errors = errors,
        onErrorDismiss = viewModel::clearError,
    ) {
        when (it) {
            is LoginEvent.LoginSuccess -> onAuthSuccess()
        }
    }

    LoginContent(
        modifier = Modifier
            .fillMaxSize()
            .safePadding(),
        state = state,
        onRememberClick = { viewModel.onAction(LoginAction.RememberClick(it)) },
        onForgotPasswordClick = onForgotPassword,
        onLoginClick = { viewModel.onAction(LoginAction.LoginClick) }
    )
}

@Composable
private fun LoginContent(
    modifier: Modifier,
    state: LoginScreenState,
    onLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onRememberClick: (Boolean) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ReconRegion(
            modifier = Modifier
                .debug(enable = DEBUG)
                .padding(top = 24.dp)
        )
        ReconLogo(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
                .debug(enable = DEBUG),
        )
        LoginForm(
            modifier = Modifier
                .fillMaxWidth()
                .debug(enable = DEBUG),
            isLoading = state.isLoading,
            emailState = state.email,
            passwordState = state.password,
            shouldRemember = state.shouldRemember,
            onRememberClick = onRememberClick,
            onForgotPasswordClick = onForgotPasswordClick,
            onLoginClick = onLoginClick,
        )
        ConnectivityIndicator(
            isOffline = false,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .padding(bottom = 32.dp)
                .weight(1F),
        )
    }
}


@Composable
private fun LoginForm(
    modifier: Modifier,
    isLoading: Boolean,
    emailState: TextFieldState,
    passwordState: TextFieldState,
    shouldRemember: Boolean,
    onRememberClick: (Boolean) -> Unit,
    onForgotPasswordClick: () -> Unit,
    onLoginClick: () -> Unit
) {

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    Column(modifier = modifier) {
        ReconTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            state = emailState,
            label = "Email",
            enabled = !isLoading,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        )
        Spacer(modifier = Modifier.height(8.dp))
        ReconSecureTextField(
            modifier = Modifier.fillMaxWidth(),
            state = passwordState,
            label = "Password",
            enabled = !isLoading,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            onKeyboardAction = { focusManager.clearFocus() }
        )
        LoginOptions(
            shouldRemember = shouldRemember,
            onRememberClick = onRememberClick,
            onForgotPasswordClick = onForgotPasswordClick,
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .debug(enable = DEBUG)
        )
        Spacer(modifier = Modifier.height(32.dp))
        ReconLoadingButton(
            text = "Sign In",
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (emailState.text.isEmpty()) focusRequester.requestFocus()
                else onLoginClick()
            },
            isLoading = isLoading
        )
    }

}

@Composable
private fun LoginOptions(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    shouldRemember: Boolean,
    onRememberClick: (Boolean) -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ReconCheckbox(
            label = "Remember Me",
            checked = shouldRemember,
            onCheckedChange = onRememberClick,
            enabled = enabled,
        )
        ReconTextButton(text = "Forgot Password", onClick = onForgotPasswordClick)
    }
}

