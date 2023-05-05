package ru.stankin.compose.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ru.stankin.compose.viewmodel.base.EventHandler
import ru.stankin.compose.viewmodel.event.CreateGroupEvent
import ru.stankin.compose.viewmodel.state.CreateGroupState
import ru.stankin.compose.viewmodel.viewstate.CreateGroupViewState

class CreateGroupViewModel : ViewModel(), EventHandler<CreateGroupEvent> {

    private val _state = mutableStateOf<CreateGroupState>(CreateGroupState.Loading)
    private val _viewState = mutableStateOf<CreateGroupViewState>(CreateGroupViewState.Empty)
    val state by _state
    val viewState by _viewState

    override fun obtainEvent(event: CreateGroupEvent) {
        when (event) {
            is CreateGroupEvent.LoadingComplete -> loading()
            is CreateGroupEvent.Reload -> obtainEvent(CreateGroupEvent.LoadingComplete)
        }
    }

    private fun loading() {
        _state.value = CreateGroupState.Loaded
        _viewState.value = CreateGroupViewState.Initialized()
    }
}