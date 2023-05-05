package ru.stankin.compose.viewmodel.state

sealed class CreateGroupState {

    object Loading: CreateGroupState()
    object Loaded: CreateGroupState()
    object Processing: CreateGroupState()
    data class Error(val message: String): CreateGroupState()
    object Completed: CreateGroupState()
}