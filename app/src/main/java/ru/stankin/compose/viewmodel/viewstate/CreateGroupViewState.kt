package ru.stankin.compose.viewmodel.viewstate

import ru.stankin.compose.viewmodel.base.FieldState

sealed class CreateGroupViewState {

    object Empty: CreateGroupViewState()

    data class Initialized(
        val name: String = "",
        val nameValidatedError: FieldState = FieldState.Ok,
    ): CreateGroupViewState()
}