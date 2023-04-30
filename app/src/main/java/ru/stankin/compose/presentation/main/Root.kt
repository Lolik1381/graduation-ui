package ru.stankin.compose.presentation.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.stankin.compose.core.ext.navigate
import ru.stankin.compose.core.manager.JwtTokenManager
import ru.stankin.compose.core.manager.RoleManager
import ru.stankin.compose.core.util.Role
import ru.stankin.compose.core.util.Route
import ru.stankin.compose.presentation.component.navigation.BottomNavigationComponent
import ru.stankin.compose.presentation.component.navigation.NavigationComponent

@Composable
fun Root() {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()

    JwtTokenManager.init(LocalContext.current)
    RoleManager.init(LocalContext.current)

    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = {
            if (isVisible(navController)) {
                BottomNavigationComponent(navController)
            }
        },
        floatingActionButton = {
            if (isVisible(navController) && Role.ADMIN.name in RoleManager.get()) {
                FloatingActionButton(
                    onClick = { navController.navigate(Route.TASK_TEMPLATE_CREATE) }
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Добавить")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true
    ) { NavigationComponent(navController = navController, modifier = Modifier.padding(it)) }
}

@Composable
private fun isVisible(navController: NavController): Boolean {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    return !Route.values()
        .filter { !it.scaffoldVisible }
        .any { navBackStackEntry?.destination?.route == it.path }
}