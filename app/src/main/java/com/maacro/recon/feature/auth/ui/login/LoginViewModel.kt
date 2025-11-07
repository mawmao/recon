package com.maacro.recon.feature.auth.ui.login

import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maacro.recon.BuildConfig
import com.maacro.recon.core.domain.VMActions
import com.maacro.recon.core.domain.VMErrors
import com.maacro.recon.core.domain.VMEvents
import com.maacro.recon.core.domain.VMState
import com.maacro.recon.feature.auth.data.AuthRepository
import com.maacro.recon.feature.auth.data.mapSignInErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel(), VMActions<LoginAction> {

    private val _state = VMState(initialState = LoginScreenState())
    val state = _state.flow

    private val _events = VMEvents<LoginEvent>()
    val events = _events.events

    private val _errors = VMErrors<String>()
    val errors = _errors.errors

    override fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.LoginClick -> onLoginClick()
            is LoginAction.RememberClick -> onRememberMeClick(action.shouldRemember)
            is LoginAction.ClearError -> _errors.clearError()
        }
    }

    private fun onRememberMeClick(shouldRemember: Boolean) =
        _state.update { it.copy(shouldRemember = shouldRemember) }

    private fun onLoginClick() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val email = _state.value.email.text.toString()
            val password = _state.value.password.text.toString()

            val result = authRepository.signIn(email, password)

            _state.update { it.copy(isLoading = false) }

            result.onSuccess {
                _events.sendEvent(LoginEvent.LoginSuccess)
            }.onFailure { error ->
                if (BuildConfig.DEBUG) {
                    Log.e("recon:login-vm", "Login failed", error)
                }
                _errors.setError(mapSignInErrors(error))
            }
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
    object ClearError : LoginAction()
    data class RememberClick(val shouldRemember: Boolean) : LoginAction()
}
