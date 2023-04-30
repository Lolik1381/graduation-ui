package ru.stankin.compose.presentation.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import ru.stankin.compose.R
import ru.stankin.compose.core.manager.JwtTokenManager
import ru.stankin.compose.core.manager.RoleManager
import ru.stankin.compose.core.util.Route
import ru.stankin.compose.presentation.component.ButtonComponent

@Composable
fun UserProfile(
    navController: NavController
) {
    Column {
        Text(text = "Профиль пользователя")

        ButtonComponent(textId = R.string.auth_sign_out) {
            JwtTokenManager.remove()
            RoleManager.remove()
            navController.navigate(Route.AUTH.path)
        }
    }
}