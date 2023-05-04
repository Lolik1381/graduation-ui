package ru.stankin.compose.presentation.profile

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.stankin.compose.R
import ru.stankin.compose.core.ext.navigate
import ru.stankin.compose.core.util.Route
import ru.stankin.compose.presentation.component.ErrorScreen
import ru.stankin.compose.presentation.component.LoadingScreen
import ru.stankin.compose.presentation.component.PasswordTextFieldComponent
import ru.stankin.compose.presentation.component.TextFieldComponent
import ru.stankin.compose.viewmodel.ChangePasswordViewModel
import ru.stankin.compose.viewmodel.event.ChangePasswordEvent
import ru.stankin.compose.viewmodel.state.ChangePasswordState
import ru.stankin.compose.viewmodel.state.ChangePasswordViewState

@Composable
fun ChangePassword(navController: NavController, changePasswordViewModel: ChangePasswordViewModel) {
    when (val changePasswordState = changePasswordViewModel.changePasswordState) {
        is ChangePasswordState.Loading -> LoadingScreen()
        is ChangePasswordState.Loaded -> ChangePasswordLoaded(changePasswordViewModel, changePasswordViewModel.changePasswordViewState as ChangePasswordViewState.Initialized)
        is ChangePasswordState.Processing -> ChangePasswordProcessing(changePasswordViewModel, changePasswordViewModel.changePasswordViewState as ChangePasswordViewState.Initialized)
        is ChangePasswordState.Completed -> {
            Toast.makeText(LocalContext.current, "Пароль успешно изменен", Toast.LENGTH_SHORT).show()
            navController.navigate(Route.AUTH)
        }
        is ChangePasswordState.Error -> ErrorScreen(
            onRefresh = { changePasswordViewModel.obtainEvent(ChangePasswordEvent.Reload) },
            message = changePasswordState.message
        )
    }

    LaunchedEffect(key1 = Unit, block = { changePasswordViewModel.obtainEvent(ChangePasswordEvent.LoadingComplete) })
}

@Composable
fun ChangePasswordLoaded(changePasswordViewModel: ChangePasswordViewModel, changePasswordViewState: ChangePasswordViewState.Initialized) {
    ChangePasswordScreen(changePasswordViewModel = changePasswordViewModel, changePasswordViewState = changePasswordViewState) {
        Text(text = stringResource(id = R.string.auth_change_password))
    }
}

@Composable
fun ChangePasswordProcessing(changePasswordViewModel: ChangePasswordViewModel, changePasswordViewState: ChangePasswordViewState.Initialized) {
    ChangePasswordScreen(changePasswordViewModel = changePasswordViewModel, changePasswordViewState = changePasswordViewState) {
        CircularProgressIndicator(
            color = Color.White,
            modifier = Modifier.height(20.dp)
        )
    }
}

@Composable
fun ChangePasswordScreen(
    changePasswordViewModel: ChangePasswordViewModel,
    changePasswordViewState: ChangePasswordViewState.Initialized,
    content: @Composable RowScope.() -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextFieldComponent(
            value = changePasswordViewState.login,
            onValueChange = { changePasswordViewModel.obtainEvent(ChangePasswordEvent.ChangeLogin(it)) },
            placeholderId = R.string.auth_login,
            fieldState = changePasswordViewState.loginValidatedError
        )

        PasswordTextFieldComponent(
            value = changePasswordViewState.oldPassword,
            onValueChange = { changePasswordViewModel.obtainEvent(ChangePasswordEvent.ChangeOldPassword(it)) },
            placeholderId = R.string.auth_old_password,
            fieldState = changePasswordViewState.oldPasswordValidatedError
        )

        PasswordTextFieldComponent(
            value = changePasswordViewState.newPassword,
            onValueChange = { changePasswordViewModel.obtainEvent(ChangePasswordEvent.ChangeNewPassword(it)) },
            placeholderId = R.string.auth_new_password,
            fieldState = changePasswordViewState.newPasswordValidatedError
        )

        Button(
            modifier = Modifier
                .padding(5.dp)
                .height(45.dp),
            onClick = { changePasswordViewModel.obtainEvent(ChangePasswordEvent.ChangePasswordClick) },
            content = content
        )
    }
}