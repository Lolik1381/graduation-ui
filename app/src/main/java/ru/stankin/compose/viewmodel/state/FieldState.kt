package ru.stankin.compose.viewmodel.state

sealed class FieldState {

    object Ok: FieldState()
    data class Error(val message: String): FieldState()
}