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
import ru.stankin.compose.viewmodel.base.FieldState
import ru.stankin.compose.viewmodel.event.ChangePasswordEvent
import ru.stankin.compose.viewmodel.state.ChangePasswordState
import ru.stankin.compose.viewmodel.viewstate.ChangePasswordViewState

class ChangePasswordViewModel(
    private val userRepository: UserRepository = UserRepository()
): ViewModel(), EventHandler<ChangePasswordEvent> {

    private val _state = mutableStateOf<ChangePasswordState>(ChangePasswordState.Loading)
    private val _viewState = mutableStateOf<ChangePasswordViewState>(ChangePasswordViewState.Empty)
    val state by _state
    val viewState by _viewState

    override fun obtainEvent(event: ChangePasswordEvent) {
        when (event) {
            is ChangePasswordEvent.LoadingComplete -> {
                _state.value = ChangePasswordState.Loaded
                _viewState.value = ChangePasswordViewState.Initialized()
            }
            is ChangePasswordEvent.ChangeLogin,
            is ChangePasswordEvent.ChangeOldPassword,
            is ChangePasswordEvent.ChangeNewPassword -> processingViewState(event, viewState as ChangePasswordViewState.Initialized)
            is ChangePasswordEvent.ChangePasswordClick -> changePassword(viewState as ChangePasswordViewState.Initialized)
            is ChangePasswordEvent.Reload -> obtainEvent(ChangePasswordEvent.LoadingComplete)
        }
    }

    private fun processingViewState(authEvent: ChangePasswordEvent, currentViewState: ChangePasswordViewState.Initialized) {
        when (authEvent) {
            is ChangePasswordEvent.ChangeLogin -> _viewState.value = currentViewState.copy(login = authEvent.login)
            is ChangePasswordEvent.ChangeOldPassword -> _viewState.value = currentViewState.copy(oldPassword = authEvent.oldPassword)
            is ChangePasswordEvent.ChangeNewPassword -> _viewState.value = currentViewState.copy(newPassword = authEvent.newPassword)
            else -> {}
        }
    }

    private fun changePassword(currentViewState: ChangePasswordViewState.Initialized) {
        if (currentViewState.login.isBlank() || currentViewState.newPassword.isBlank() || currentViewState.oldPassword.isBlank()) {
            _viewState.value = currentViewState.copy(
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
                .onStart { _state.value = ChangePasswordState.Processing }
                .catch { _state.value = ChangePasswordState.Error(it.message.orEmpty()) }
                .collect { response ->
                    response
                        .onSuccess { _state.value = ChangePasswordState.Completed }
                        .onFailure { _state.value = ChangePasswordState.Error(response.body()?.message.orEmpty()) }
                }
        }
    }
}