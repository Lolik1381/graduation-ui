package ru.stankin.compose.presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.stankin.compose.R
import ru.stankin.compose.core.manager.JwtTokenManager
import ru.stankin.compose.core.manager.RoleManager
import ru.stankin.compose.core.util.Route
import ru.stankin.compose.core.util.content
import ru.stankin.compose.core.util.onFailure
import ru.stankin.compose.core.util.onSuccess
import ru.stankin.compose.datasource.Repositories
import ru.stankin.compose.model.RequestLoginDto
import ru.stankin.compose.model.TextFieldStateDto
import ru.stankin.compose.presentation.component.ButtonComponent
import ru.stankin.compose.presentation.component.PasswordTextFieldComponent
import ru.stankin.compose.presentation.component.TextComponent
import ru.stankin.compose.presentation.component.TextFieldComponent

@Composable
fun Auth(
    navigationController: NavController,
//    authViewModel: AuthViewModel = viewModel()
) {
    var login by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    var loginFieldState by rememberSaveable { mutableStateOf(TextFieldStateDto()) }
    var passwordFieldState by rememberSaveable { mutableStateOf(TextFieldStateDto()) }

    var errorMessage by rememberSaveable { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextFieldComponent(
            value = login,
            onValueChange = { login = it },
            placeholderId = R.string.auth_login,
            textFieldState = loginFieldState
        )

        PasswordTextFieldComponent(
            value = password,
            onValueChange = { password = it },
            placeholderId = R.string.auth_password,
            textFieldState = passwordFieldState
        )

        ButtonComponent(textId = R.string.auth_sign_in) {
            loginFieldState = checkState(login)
            passwordFieldState = checkState(password)

            if (login.isNotBlank() && password.isNotBlank()) {
                coroutineScope.launch {
                    runBlocking {
                        Repositories.userRepository.login(RequestLoginDto(login, password))
                            .onSuccess { JwtTokenManager.put(it.content().orEmpty()) }
                    }

                    runBlocking {
                        Repositories.userRepository.userRoles()
                            .onSuccess {
                                RoleManager.put(it.content().orEmpty())

                                navigationController.navigate(
                                    Route.values().first { route -> route.defaultRoute && route.role.name in it.content().orEmpty() }.path
                                )
                            }
                            .onFailure {
                                errorMessage = "Не верный логин или пароль"
                            }
                    }
                }
            }
        }

        TextComponent(text = errorMessage, color = Color.Red)
    }
}

private fun checkState(field: String): TextFieldStateDto {
    if (field.isBlank()) {
        return TextFieldStateDto(isError = true, errorMessageId = R.string.required_field)
    }

    return TextFieldStateDto()
}