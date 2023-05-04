package ru.stankin.compose.presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.stankin.compose.R
import ru.stankin.compose.core.ext.navigate
import ru.stankin.compose.core.manager.RoleManager
import ru.stankin.compose.core.util.Route
import ru.stankin.compose.presentation.component.ErrorScreen
import ru.stankin.compose.presentation.component.LoadingScreen
import ru.stankin.compose.presentation.component.PasswordTextFieldComponent
import ru.stankin.compose.presentation.component.TextFieldComponent
import ru.stankin.compose.viewmodel.AuthViewModel
import ru.stankin.compose.viewmodel.event.AuthEvent
import ru.stankin.compose.viewmodel.state.AuthState
import ru.stankin.compose.viewmodel.state.AuthViewState

@Composable
fun Auth(
    navigationController: NavController,
    authViewModel: AuthViewModel
) {
    when (val authState = authViewModel.authState) {
        is AuthState.Loading -> LoadingScreen()
        is AuthState.Loaded -> AuthLoaded(authViewModel = authViewModel, authViewState = authViewModel.authViewState as AuthViewState.Initialized)
        is AuthState.Processing -> AuthProcessing(authViewModel = authViewModel, authViewState = authViewModel.authViewState as AuthViewState.Initialized)
        is AuthState.Completed -> navigationController.navigate(Route.values().first { route -> route.defaultRoute && route.role.name in RoleManager.get() })
        is AuthState.TemporaryPassword -> navigationController.navigate(Route.CHANGE_PASSWORD)
        is AuthState.Error -> ErrorScreen(
            onRefresh = { authViewModel.obtainEvent(AuthEvent.Reload) },
            message = authState.message
        )
    }

    LaunchedEffect(key1 = Unit, block = { authViewModel.obtainEvent(AuthEvent.LoadingComplete) })
}

@Composable
fun AuthLoaded(
    authViewModel: AuthViewModel,
    authViewState: AuthViewState.Initialized
) {
    AuthScreen(authViewModel = authViewModel, authViewState = authViewState) {
        Text(text = stringResource(id = R.string.auth_sign_in))
    }
}

@Composable
fun AuthProcessing(
    authViewModel: AuthViewModel,
    authViewState: AuthViewState.Initialized
) {
    AuthScreen(authViewModel = authViewModel, authViewState = authViewState) {
        CircularProgressIndicator(
            color = Color.White,
            modifier = Modifier.height(20.dp)
        )
    }
}

@Composable
fun AuthScreen(
    authViewModel: AuthViewModel,
    authViewState: AuthViewState.Initialized,
    content: @Composable RowScope.() -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextFieldComponent(
            value = authViewState.login,
            onValueChange = { authViewModel.obtainEvent(AuthEvent.ChangeLogin(it)) },
            placeholderId = R.string.auth_login,
            fieldState = authViewState.loginValidatedError
        )

        PasswordTextFieldComponent(
            value = authViewState.password,
            onValueChange = { authViewModel.obtainEvent(AuthEvent.ChangePassword(it)) },
            placeholderId = R.string.auth_password,
            fieldState = authViewState.passwordValidatedError
        )

        Button(
            modifier = Modifier.padding(5.dp)
                .height(45.dp),
            onClick = { authViewModel.obtainEvent(AuthEvent.LoginClick) },
            content = content
        )
    }
}