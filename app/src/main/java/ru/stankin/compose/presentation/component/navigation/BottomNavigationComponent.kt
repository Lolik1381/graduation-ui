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
import androidx.compose.ui.unit.TextUnit
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
                    icon = { Icon(item.icon, contentDescription = item.title) },
                    label = { Text(text = item.title, fontSize = item.fontSize) },
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
    val icon: ImageVector,
    val route: Route,
    val order: Int,
    val fontSize: TextUnit = 12.sp
) {
    companion object {
        fun items(): List<NavigationItems> = listOf(TaskItem, UserItem, AdminItem, TaskTemplateItem)
    }

    object TaskItem : NavigationItems(title = "Контрольные списки", icon = Icons.Default.Done, route = Route.TASK_INFO, order = 0)
    object UserItem : NavigationItems(title = "Профиль", icon = Icons.Default.Person, route = Route.USER_PROFILE, order = 1)

    object TaskTemplateItem : NavigationItems(title = "Шаблоны контрольных списков", icon = Icons.Default.Done, route = Route.TASK_TEMPLATE_LIST, order = 0, fontSize = 10.sp)
    object AdminItem : NavigationItems(title = "Профиль администратора", icon = Icons.Default.Person, route = Route.ADMIN_PROFILE, order = 1)
}