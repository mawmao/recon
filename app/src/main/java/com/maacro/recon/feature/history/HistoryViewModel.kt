package com.maacro.recon.feature.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maacro.recon.core.database.model.FormEntryEntity
import com.maacro.recon.core.domain.VMActions
import com.maacro.recon.core.domain.VMEvents
import com.maacro.recon.core.domain.VMState
import com.maacro.recon.feature.form.data.FormRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val formRepository: FormRepository,
) : ViewModel(), VMActions<HistoryAction> {

    private val _state = VMState(initialState = HistoryScreenState())
    val state = _state.flow

    private val _events = VMEvents<HistoryEvent>()
    val events = _events.events


    init {
        formRepository.getAllEntries().onEach { entries ->
            _state.update { it.copy(entries = entries) }
        }.launchIn(viewModelScope)
    }

    override fun onAction(action: HistoryAction) {}

    fun clearDatabase() = viewModelScope.launch {
        formRepository.clearDatabase()
        _state.update { it.copy(entries = emptyList()) }
    }
}

data class HistoryScreenState(
    val isLoading: Boolean = false,
    val entries: List<FormEntryEntity> = emptyList()
)

sealed class HistoryEvent {
}

sealed class HistoryAction {
}
