package ru.stankin.compose.viewmodel.event

sealed class AuthEvent {

    object LoadingComplete : AuthEvent()

    data class ChangeLogin(
        val login: String
    ) : AuthEvent()

    data class ChangePassword(
        val password: String
    ) : AuthEvent()

    object LoginClick : AuthEvent()

    object Reload: AuthEvent()
}