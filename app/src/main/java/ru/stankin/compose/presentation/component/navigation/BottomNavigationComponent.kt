package ru.stankin.compose.presentation.component.navigation

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.stankin.compose.core.ext.navigate
import ru.stankin.compose.core.manager.RoleManager
import ru.stankin.compose.core.util.Role
import ru.stankin.compose.core.util.Route

@Composable
fun BottomNavigationComponent(
    navController: NavHostController
) {
    val roles = RoleManager.get()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val navigationItems = NavigationItems.items()

    BottomNavigation {
        navigationItems
            .filter { it.route.role.name in roles || it.route.role == Role.ALL }
            .sortedBy { it.order }
            .forEach { item ->
                BottomNavigationItem(
                    icon = { Icon(item.iconVector, contentDescription = item.title) },
                    label = { Text(text = item.title, fontSize = 12.sp) },
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color.White.copy(0.4f),
                    alwaysShowLabel = true,
                    selected = currentRoute == item.route.path,
                    onClick = { navController.navigate(item.route) }
                )
            }
    }
}

sealed class NavigationItems(
    val title: String,
    val iconVector: ImageVector,
    val route: Route,
    val order: Int
) {
    companion object {
        fun items(): List<NavigationItems> = listOf(TaskItem, UserItem, AdminItem, TaskTemplateItem)
    }

    object TaskItem : NavigationItems("Задания", Icons.Default.Done, Route.TASK_INFO, 0)
    object UserItem : NavigationItems("Профиль", Icons.Default.Person, Route.USER_PROFILE, 1)

    object TaskTemplateItem : NavigationItems("Шаблоны заданий", Icons.Default.Done, Route.TASK_TEMPLATE, 0)
    object AdminItem : NavigationItems("Профиль", Icons.Default.Person, Route.ADMIN_PROFILE, 1)
}