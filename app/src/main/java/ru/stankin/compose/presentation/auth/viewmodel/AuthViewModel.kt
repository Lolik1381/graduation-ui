package ru.stankin.compose.presentation.auth.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.stankin.compose.core.manager.JwtTokenManager
import ru.stankin.compose.core.manager.RoleManager
import ru.stankin.compose.datasource.Repositories
import ru.stankin.compose.model.RequestLoginDto

class AuthViewModel : ViewModel() {

    private val _isSuccessJwtToken = mutableStateOf(false)
    private val _isSuccessRoles = mutableStateOf(false)
    private val _errorMessage = mutableStateOf("")

    val isSuccessJwtToken: Boolean
        get() = _isSuccessJwtToken.value

    val isSuccessRoles: Boolean
        get() = _isSuccessRoles.value

    val errorMessage: String
        get() = _errorMessage.value

    fun login(login: String, password: String) {
//         viewModelScope.launch {
//             runBlocking {
//                 runCatching { Repositories.userRepository.login(RequestLoginDto(login, password)) }
//                     .onSuccess {
//                         _isSuccessJwtToken.value = true
//                         JwtTokenManager.put(it.notNullContent())
//                     }
//                     .onFailure {
//                         _isSuccessJwtToken.value = false
//                         _errorMessage.value = "Не верный логин или пароль"
//                     }
//             }
//
//             runBlocking {
//                 runCatching { Repositories.userRepository.userRoles() }
//                     .onSuccess {
//                         _isSuccessJwtToken.value = true
//                         RoleManager.put(it.notNullContent())
//                     }
//                     .onFailure {
//                         _isSuccessJwtToken.value = false
//                         _errorMessage.value = "Не удалось выполнить запрос на сервер"
//                     }
//             }
//         }
    }
}