package ru.stankin.compose.viewmodel.event

sealed class ChangePasswordEvent {

    object LoadingComplete: ChangePasswordEvent()
    data class ChangeLogin(val login: String): ChangePasswordEvent()
    data class ChangeOldPassword(val oldPassword: String): ChangePasswordEvent()
    data class ChangeNewPassword(val newPassword: String): ChangePasswordEvent()
    object ChangePasswordClick: ChangePasswordEvent()
    object Reload: ChangePasswordEvent()
}