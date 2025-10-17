package com.maacro.recon.core.domain

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

interface VMComponent

class VMState<State>(initialState: State): VMComponent {
    private val _state = MutableStateFlow(initialState)
    val flow: StateFlow<State> = _state.asStateFlow()
    val value = _state.value

    fun update(updater: (State) -> State) = _state.update(updater)
}

class VMEvents<Event> : VMComponent {
    private val _events = Channel<Event>(Channel.BUFFERED)
    val events: Flow<Event> = _events.receiveAsFlow()

    suspend fun sendEvent(event: Event) = _events.send(event)
}

class VMErrors<E>(initial: E? = null) : VMComponent {
    private val _errors = MutableStateFlow<E?>(initial)
    val errors = _errors.asStateFlow()

    fun setError(error: E?) = _errors.update { error }
    fun clearError() = _errors.update { null }
}

interface VMActions<Action> : VMComponent {
    fun onAction(action: Action)
}
