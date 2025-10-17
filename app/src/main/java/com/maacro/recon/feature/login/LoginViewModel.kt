package com.maacro.recon.feature.login

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.viewModelScope
import com.maacro.recon.core.domain.ReconViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor() :
    ReconViewModel<LoginScreenState, LoginEvent, LoginAction>(initialState = LoginScreenState()) {

    override fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.LoginClick -> doLoggedAction("Login") { onLoginClick() }
            is LoginAction.RememberClick -> doLoggedAction("Remember Me: ${action.shouldRemember}") {
                onRememberMeClick(action.shouldRemember)
            }
        }
    }

    private fun onRememberMeClick(shouldRemember: Boolean) =
        setState { it.copy(shouldRemember = shouldRemember) }

    private fun onLoginClick() {
        viewModelScope.launch {
            setState { state -> state.copy(isLoading = true) }
            delay(1000L)

            val email = _state.value.email.text
            val password = _state.value.password.text
//            val tempSuccess = email == "1" && password == "1"
            val tempSuccess = true

            setState { it.copy(isLoading = false) }

            if (tempSuccess) sendEvent(LoginEvent.LoginSuccess) else setError("Invalid Credential")
        }
    }
}

data class LoginScreenState(
    val email: TextFieldState = TextFieldState(),
    val password: TextFieldState = TextFieldState(),
    val shouldRemember: Boolean = false,
    val isLoading: Boolean = false,
)

sealed class LoginEvent {
    object LoginSuccess : LoginEvent()
}

sealed class LoginAction {
    object LoginClick : LoginAction()
    data class RememberClick(val shouldRemember: Boolean) : LoginAction()
}
