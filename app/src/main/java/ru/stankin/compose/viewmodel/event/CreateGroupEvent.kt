package ru.stankin.compose.viewmodel.event

sealed class CreateGroupEvent {

    object LoadingComplete: CreateGroupEvent()
    object Reload: CreateGroupEvent()
    data class ChangeGroupName(val name: String): CreateGroupEvent()
}