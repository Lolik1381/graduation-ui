package ru.stankin.compose.presentation.profile.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.stankin.compose.core.ext.navigate
import ru.stankin.compose.core.util.Route
import ru.stankin.compose.presentation.component.ErrorScreen
import ru.stankin.compose.presentation.component.LoadingScreen
import ru.stankin.compose.viewmodel.AdminProfileViewModel
import ru.stankin.compose.viewmodel.event.AdminProfileEvent
import ru.stankin.compose.viewmodel.state.AdminProfileState

@Composable
fun AdminProfile(
    navController: NavController,
    viewModel: AdminProfileViewModel
) {
    when (val state = viewModel.state) {
        is AdminProfileState.Loaded -> LoadingScreen()
        is AdminProfileState.Loading, AdminProfileState.Completed -> AdminProfileScreen(viewModel)
        is AdminProfileState.Error -> ErrorScreen(onRefresh = { viewModel.obtainEvent(AdminProfileEvent.Reload) }, message = state.message)
        is AdminProfileState.NavigateToAuth -> {
            navController.navigate(Route.AUTH.path)
            viewModel.obtainEvent(AdminProfileEvent.Completed)
        }
        is AdminProfileState.NavigateToCreateUsage -> navController.navigate(Route.CREATE_USAGE)
        is AdminProfileState.NavigateToChangeUsage -> navController.navigate(Route.CHANGE_USAGE)
        is AdminProfileState.NavigateToCreateGroup -> navController.navigate(Route.CREATE_GROUP)
        is AdminProfileState.NavigateToChangeGroup -> navController.navigate(Route.CHANGE_GROUP)
        is AdminProfileState.NavigateToCreateEquipment -> navController.navigate(Route.CREATE_EQUIPMENT)
        is AdminProfileState.NavigateToChangeEquipment -> navController.navigate(Route.CHANGE_EQUIPMENT)
    }

    LaunchedEffect(
        key1 = Unit,
        block = { viewModel.obtainEvent(AdminProfileEvent.LoadingComplete) })
}

@Composable
fun AdminProfileScreen(
    viewModel: AdminProfileViewModel
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileComponent(description = "Добавить пользователя", icon = Icons.Filled.Add, onClick = { viewModel.obtainEvent(AdminProfileEvent.CreateUser) })
        ProfileComponent(description = "Изменить пользователя", icon = Icons.Filled.ChangeCircle, onClick = { viewModel.obtainEvent(AdminProfileEvent.ChangeUser) })
        ProfileComponent(description = "Добавить группу пользователей", icon = Icons.Filled.Add, onClick = { viewModel.obtainEvent(AdminProfileEvent.CreateGroup) })
        ProfileComponent(description = "Изменить группу пользователей", icon = Icons.Filled.ChangeCircle, onClick = { viewModel.obtainEvent(AdminProfileEvent.ChangeGroup) })
        ProfileComponent(description = "Добавить оборудование", icon = Icons.Filled.Add, onClick = { viewModel.obtainEvent(AdminProfileEvent.CreateEquipment) })
        ProfileComponent(description = "Изменить оборудование", icon = Icons.Filled.ChangeCircle, onClick = { viewModel.obtainEvent(AdminProfileEvent.ChangeEquipment) })

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(5.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Выход")

            IconButton(onClick = { viewModel.obtainEvent(AdminProfileEvent.Exit) }) {
                Icon(imageVector = Icons.Filled.ExitToApp, contentDescription = null)
            }
        }
    }
}

@Composable
fun ProfileComponent(
    icon: ImageVector,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(0.8f)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth(0.2f),
                onClick = onClick
            ) {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp),
                    imageVector = icon,
                    contentDescription = null
                )
            }

            Text(
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxWidth(),
                text = description
            )
        }
    }
}