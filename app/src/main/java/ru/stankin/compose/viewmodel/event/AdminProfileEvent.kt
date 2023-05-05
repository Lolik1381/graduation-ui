package ru.stankin.compose.viewmodel.event

sealed class AdminProfileEvent {

    object LoadingComplete: AdminProfileEvent()
    object Reload: AdminProfileEvent()
    object Exit: AdminProfileEvent()
    object Completed: AdminProfileEvent()
    object CreateUser: AdminProfileEvent()
    object ChangeUser: AdminProfileEvent()
    object CreateGroup: AdminProfileEvent()
    object ChangeGroup: AdminProfileEvent()
    object CreateEquipment: AdminProfileEvent()
    object ChangeEquipment: AdminProfileEvent()
}