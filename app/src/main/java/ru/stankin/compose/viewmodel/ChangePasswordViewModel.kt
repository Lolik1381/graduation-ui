package ru.stankin.compose.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ru.stankin.compose.core.util.onFailure
import ru.stankin.compose.core.util.onSuccess
import ru.stankin.compose.model.RequestChangePasswordDto
import ru.stankin.compose.retrofit.repository.UserRepository
import ru.stankin.compose.viewmodel.base.EventHandler
import ru.stankin.compose.viewmodel.event.ChangePasswordEvent
import ru.stankin.compose.viewmodel.state.ChangePasswordState
import ru.stankin.compose.viewmodel.state.ChangePasswordViewState
import ru.stankin.compose.viewmodel.state.FieldState

class ChangePasswordViewModel(
    private val userRepository: UserRepository = UserRepository()
) : ViewModel(), EventHandler<ChangePasswordEvent> {

    private val _changePasswordState = mutableStateOf<ChangePasswordState>(ChangePasswordState.Loading)
    private val _changePasswordViewState = mutableStateOf<ChangePasswordViewState>(ChangePasswordViewState.Empty)
    val changePasswordState by _changePasswordState
    val changePasswordViewState by _changePasswordViewState

    override fun obtainEvent(event: ChangePasswordEvent) {
        when (event) {
            is ChangePasswordEvent.LoadingComplete -> {
                _changePasswordState.value = ChangePasswordState.Loaded
                _changePasswordViewState.value = ChangePasswordViewState.Initialized()
            }
            is ChangePasswordEvent.ChangeLogin -> processingViewState(event, _changePasswordViewState.value as ChangePasswordViewState.Initialized)
            is ChangePasswordEvent.ChangeOldPassword -> processingViewState(event, _changePasswordViewState.value as ChangePasswordViewState.Initialized)
            is ChangePasswordEvent.ChangeNewPassword -> processingViewState(event, _changePasswordViewState.value as ChangePasswordViewState.Initialized)
            is ChangePasswordEvent.ChangePasswordClick -> changePassword(_changePasswordViewState.value as ChangePasswordViewState.Initialized)
            is ChangePasswordEvent.Reload -> obtainEvent(ChangePasswordEvent.LoadingComplete)
        }
    }

    private fun processingViewState(authEvent: ChangePasswordEvent, currentViewState: ChangePasswordViewState.Initialized) {
        when (authEvent) {
            is ChangePasswordEvent.ChangeLogin -> _changePasswordViewState.value = currentViewState.copy(login = authEvent.login)
            is ChangePasswordEvent.ChangeOldPassword -> _changePasswordViewState.value = currentViewState.copy(oldPassword = authEvent.oldPassword)
            is ChangePasswordEvent.ChangeNewPassword -> _changePasswordViewState.value = currentViewState.copy(newPassword = authEvent.newPassword)
            else -> {}
        }
    }

    private fun changePassword(currentViewState: ChangePasswordViewState.Initialized) {
        if (currentViewState.login.isBlank() || currentViewState.newPassword.isBlank() || currentViewState.oldPassword.isBlank()) {
            _changePasswordViewState.value = currentViewState.copy(
                loginValidatedError = when {
                    currentViewState.login.isBlank() -> FieldState.Error("Поле обязательно для заполнения")
                    else -> FieldState.Ok
                },
                oldPasswordValidatedError = when {
                    currentViewState.oldPassword.isBlank() -> FieldState.Error("Поле обязательно для заполнения")
                    else -> FieldState.Ok
                },
                newPasswordValidatedError = when {
                    currentViewState.newPassword.isBlank() -> FieldState.Error("Поле обязательно для заполнения")
                    else -> FieldState.Ok
                }
            )
            return
        }

        viewModelScope.launch {
            userRepository.changePassword(RequestChangePasswordDto(currentViewState.login, currentViewState.oldPassword, currentViewState.newPassword))
                .onStart { _changePasswordState.value = ChangePasswordState.Processing }
                .catch { _changePasswordState.value = ChangePasswordState.Error(it.message.orEmpty()) }
                .collect { response ->
                    response
                        .onSuccess { _changePasswordState.value = ChangePasswordState.Completed }
                        .onFailure { _changePasswordState.value = ChangePasswordState.Error(response.body()?.message.orEmpty()) }
                }
        }
    }
}