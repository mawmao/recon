package com.maacro.recon.core.domain

import android.util.Log
import androidx.lifecycle.ViewModel
import com.maacro.recon.BuildConfig
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

@Deprecated(
    message = "Use composition with VMState, VMEvent, and VMErrors instead of inheritance.",
    replaceWith = ReplaceWith("ViewModel() with VMState(), VMEvent(), VMErrors()"),
    level = DeprecationLevel.WARNING
)
abstract class ReconViewModel<State, Event, Action>(initialState: State) : ViewModel() {

    @Suppress("PropertyName")
    protected val _state = MutableStateFlow(initialState)
    val state: StateFlow<State> = _state.asStateFlow()

    // TODO: could put errors as generic when requirements change
    private val internalErrors: MutableStateFlow<String?> = MutableStateFlow(null)
    val errors: StateFlow<String?> = internalErrors.asStateFlow()

    private val _events = Channel<Event>(Channel.BUFFERED)
    val events: Flow<Event> = _events.receiveAsFlow()

    protected fun setState(updater: (State) -> State) = _state.update(updater)

    protected fun setError(error: String?) = internalErrors.update { error }
    fun clearError() = internalErrors.update { null }

    protected suspend fun sendEvent(event: Event) = _events.send(event)


    abstract fun onAction(action: Action)
    protected fun doLoggedAction(desc: String, block: () -> Unit) {
        if (BuildConfig.DEBUG) Log.d("recon:view-model-action", desc)
        block()
    }
}