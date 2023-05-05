package ru.stankin.compose.viewmodel.viewstate

import ru.stankin.compose.viewmodel.base.FieldState

sealed class AuthViewState {

    object Empty: AuthViewState()

    data class Initialized(
        val login: String = "",
        val loginValidatedError: FieldState = FieldState.Ok,
        val password: String = "",
        val passwordValidatedError: FieldState = FieldState.Ok
    ): AuthViewState()
}