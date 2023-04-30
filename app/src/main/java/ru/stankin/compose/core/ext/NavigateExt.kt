package ru.stankin.compose.core.ext

import androidx.navigation.NavController
import ru.stankin.compose.core.util.Route
import ru.stankin.compose.core.util.replacePathVariable

fun NavController.navigate(route: Route, pathVariable: String? = null) {
    val navController = this

    navController.navigate(route.replacePathVariable(pathVariable)) {
        navController.graph.startDestinationRoute?.let { screenRoute ->
            popUpTo(screenRoute) {
                saveState = true
            }
        }

        launchSingleTop = true
    }
}