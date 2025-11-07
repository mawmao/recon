package com.maacro.recon.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maacro.recon.BuildConfig
import com.maacro.recon.core.domain.VMActions
import com.maacro.recon.core.domain.VMErrors
import com.maacro.recon.core.domain.VMEvents
import com.maacro.recon.core.domain.VMState
import com.maacro.recon.feature.auth.data.AuthRepository
import com.maacro.recon.feature.auth.data.mapSignOutErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel(), VMActions<HomeAction> {

    private val _state = VMState(initialState = HomeScreenState())
    val state = _state.flow

    private val _events = VMEvents<HomeEvent>()
    val events = _events.events

    private val _errors = VMErrors<String>()
    val errors = _errors.errors

    override fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.SettingsClick -> onSettingsClick()
            is HomeAction.ClearError -> _errors.clearError()
            is HomeAction.SignOutClick -> {
                viewModelScope.launch {
                    val result = authRepository.signOut()
                    result.onSuccess {
                        _events.sendEvent(HomeEvent.SignOutSuccess)
                    }.onFailure { error ->
                        if (BuildConfig.DEBUG) {
                            Log.e("recon:home-vm", "Sign out failed", error)
                        }
                        _errors.setError(mapSignOutErrors(error))
                    }
                }
            }
        }
    }

    fun hideSettings() = _state.update { state -> state.copy(isSettingsShown = false) }
    fun onSettingsClick() = _state.update { state -> state.copy(isSettingsShown = true) }
}


data class HomeScreenState(
    val isSettingsShown: Boolean = false,
    val isLoading: Boolean = false,
)

sealed class HomeEvent {
    object SignOutSuccess : HomeEvent()
}

sealed class HomeAction {
    object SettingsClick : HomeAction()
    object SignOutClick: HomeAction()
    object ClearError : HomeAction()
}

