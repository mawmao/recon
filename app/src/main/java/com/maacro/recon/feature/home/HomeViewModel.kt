package com.maacro.recon.feature.home

import com.maacro.recon.core.domain.ReconViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() :
    ReconViewModel<HomeScreenState, HomeEvent, HomeAction>(initialState = HomeScreenState()) {

    override fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.SettingsClick -> doLoggedAction("Settings") { onSettingsClick() }
        }
    }

    fun hideSettings() = setState { state -> state.copy(isSettingsShown = false) }
    fun onSettingsClick() = setState { state -> state.copy(isSettingsShown = true) }
}


data class HomeScreenState(
    val isSettingsShown: Boolean = false,
    val isLoading: Boolean = false,
)

sealed class HomeEvent {}

sealed class HomeAction {
    object SettingsClick : HomeAction()
}

