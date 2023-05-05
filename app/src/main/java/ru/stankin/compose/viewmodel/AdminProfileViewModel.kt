package ru.stankin.compose.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ru.stankin.compose.core.manager.JwtTokenManager
import ru.stankin.compose.core.manager.RoleManager
import ru.stankin.compose.viewmodel.base.EventHandler
import ru.stankin.compose.viewmodel.event.AdminProfileEvent
import ru.stankin.compose.viewmodel.state.AdminProfileState

class AdminProfileViewModel : ViewModel(), EventHandler<AdminProfileEvent> {

    private val _state = mutableStateOf<AdminProfileState>(AdminProfileState.Loading)
    val state by _state

    override fun obtainEvent(event: AdminProfileEvent) {
        when (event) {
            is AdminProfileEvent.LoadingComplete -> loading()
            is AdminProfileEvent.Exit -> exit()
            is AdminProfileEvent.Reload -> obtainEvent(AdminProfileEvent.LoadingComplete)
            is AdminProfileEvent.Completed -> process(AdminProfileState.Completed)
            is AdminProfileEvent.CreateUser -> process(AdminProfileState.NavigateToCreateUsage)
            is AdminProfileEvent.CreateGroup -> process(AdminProfileState.NavigateToCreateGroup)
            is AdminProfileEvent.CreateEquipment -> process(AdminProfileState.NavigateToCreateEquipment)
            is AdminProfileEvent.ChangeUser -> process(AdminProfileState.NavigateToChangeUsage)
            is AdminProfileEvent.ChangeGroup -> process(AdminProfileState.NavigateToChangeGroup)
            is AdminProfileEvent.ChangeEquipment -> process(AdminProfileState.NavigateToChangeEquipment)
        }
    }

    private fun loading() {
        _state.value = AdminProfileState.Loading
    }

    private fun exit() {
        JwtTokenManager.remove()
        RoleManager.remove()

        _state.value = AdminProfileState.NavigateToAuth
    }

    private fun process(state: AdminProfileState) {
        _state.value = state
    }
}