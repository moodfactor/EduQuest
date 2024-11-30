package com.mood.eduquest.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<State>(
    private val initialState: State,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<State> = _state

    protected fun updateState(update: (State) -> State) {
        _state.value = update(_state.value)
    }

    protected fun launchDataLoad(block: suspend () -> Unit) {
        viewModelScope.launch(dispatcher) {
            try {
                block()
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    protected open fun handleError(exception: Exception) {
        // Centralized error handling
        println("ViewModel Error: ${exception.message}")
        // Could integrate with error tracking service
    }
}