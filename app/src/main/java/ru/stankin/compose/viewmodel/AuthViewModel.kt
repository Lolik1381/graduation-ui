package ru.stankin.compose.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ru.stankin.compose.core.manager.JwtTokenManager
import ru.stankin.compose.core.manager.RoleManager
import ru.stankin.compose.core.util.nonNullContent
import ru.stankin.compose.core.util.onFailure
import ru.stankin.compose.core.util.onSuccess
import ru.stankin.compose.model.RequestLoginDto
import ru.stankin.compose.retrofit.repository.UserRepository
import ru.stankin.compose.viewmodel.base.EventHandler
import ru.stankin.compose.viewmodel.event.AuthEvent
import ru.stankin.compose.viewmodel.state.AuthState
import ru.stankin.compose.viewmodel.state.AuthViewState
import ru.stankin.compose.viewmodel.state.FieldState

class AuthViewModel(
    private val userRepository: UserRepository = UserRepository()
): ViewModel(), EventHandler<AuthEvent> {

    private val _authState = mutableStateOf<AuthState>(AuthState.Loading)
    private val _authViewState = mutableStateOf<AuthViewState>(AuthViewState.Empty)
    val authState by _authState
    val authViewState by _authViewState

    override fun obtainEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.LoadingComplete -> {
                _authState.value = AuthState.Loaded
                _authViewState.value = AuthViewState.Initialized()
            }
            is AuthEvent.ChangeLogin -> processingViewState(event, _authViewState.value as AuthViewState.Initialized)
            is AuthEvent.ChangePassword -> processingViewState(event, _authViewState.value as AuthViewState.Initialized)
            is AuthEvent.LoginClick -> login(_authViewState.value as AuthViewState.Initialized)
            is AuthEvent.Reload -> obtainEvent(AuthEvent.LoadingComplete)
        }
    }

    private fun processingViewState(authEvent: AuthEvent, currentViewState: AuthViewState.Initialized) {
        when (authEvent) {
            is AuthEvent.ChangeLogin -> _authViewState.value = currentViewState.copy(login = authEvent.login)
            is AuthEvent.ChangePassword -> _authViewState.value = currentViewState.copy(password = authEvent.password)
            else -> {}
        }
    }

    private fun login(currentViewState: AuthViewState.Initialized) {
        if (currentViewState.login.isBlank() || currentViewState.password.isBlank()) {
            _authViewState.value = currentViewState.copy(
                loginValidatedError = when {
                    currentViewState.login.isBlank() -> FieldState.Error("Поле обязательно для заполнения")
                    else -> FieldState.Ok
                },
                passwordValidatedError = when {
                    currentViewState.password.isBlank() -> FieldState.Error("Поле обязательно для заполнения")
                    else -> FieldState.Ok
                }
            )
            return
        }

        viewModelScope.launch {
            userRepository.login(RequestLoginDto(currentViewState.login, currentViewState.password))
                .onStart { _authState.value = AuthState.Processing }
                .catch { _authState.value = AuthState.Error(it.message.orEmpty()) }
                .collect { response ->
                    response
                        .onSuccess {
                            val content = response.nonNullContent()

                            if (!content.isTemporaryPassword) {
                                JwtTokenManager.put(content.token)
                                RoleManager.put(content.roles)

                                _authState.value = AuthState.Completed
                            } else {
                                _authState.value = AuthState.TemporaryPassword
                            }
                        }
                        .onFailure { _authState.value = AuthState.Error(response.body()?.message.orEmpty()) }
                }
        }
    }
}