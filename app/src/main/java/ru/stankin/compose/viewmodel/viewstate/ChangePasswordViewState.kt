package ru.stankin.compose.viewmodel.viewstate

import ru.stankin.compose.viewmodel.base.FieldState

sealed class ChangePasswordViewState {

    object Empty: ChangePasswordViewState()

    data class Initialized(
        val login: String = "",
        val loginValidatedError: FieldState = FieldState.Ok,
        val oldPassword: String = "",
        val oldPasswordValidatedError: FieldState = FieldState.Ok,
        val newPassword: String = "",
        val newPasswordValidatedError: FieldState = FieldState.Ok,
    ): ChangePasswordViewState()
}