package ru.stankin.compose.viewmodel.base

sealed class FieldState {

    object Ok: FieldState()
    data class Error(val message: String): FieldState()
}