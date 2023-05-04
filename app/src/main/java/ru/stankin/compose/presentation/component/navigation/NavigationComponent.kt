package ru.stankin.compose.presentation.component.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.stankin.compose.core.util.Route
import ru.stankin.compose.presentation.auth.Auth
import ru.stankin.compose.presentation.profile.AdminProfile
import ru.stankin.compose.presentation.profile.ChangePassword
import ru.stankin.compose.presentation.profile.UserProfile
import ru.stankin.compose.presentation.task.admin.TaskCreate
import ru.stankin.compose.presentation.task.user.task.UserTaskComponent
import ru.stankin.compose.presentation.task.user.tasklist.UserTaskListComponent
import ru.stankin.compose.presentation.taskscheduler.TaskSchedulerCreate
import ru.stankin.compose.presentation.tasktemplate.admin.TaskTemplateCreateComponent
import ru.stankin.compose.presentation.tasktemplate.admin.TaskTemplateListComponent
import ru.stankin.compose.presentation.tasktemplate.admin.TaskTemplateUpdateComponent

@Composable
fun NavigationComponent(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = Route.AUTH.path,
        modifier = modifier
    ) {
        composable(Route.AUTH.path) { Auth(navigationController = navController, authViewModel = viewModel()) }
        composable(Route.CHANGE_PASSWORD.path) { ChangePassword(navController = navController, changePasswordViewModel = viewModel()) }
        composable(Route.TASK_INFO.path) { UserTaskListComponent(navController = navController) }
        composable(Route.TASK.path) { UserTaskComponent(it.arguments?.getString("taskId")!!, navController = navController) }
        composable(Route.USER_PROFILE.path) { UserProfile(navController = navController) }
        composable(Route.ADMIN_PROFILE.path) { AdminProfile(navController = navController) }
        composable(Route.TASK_TEMPLATE.path) { TaskTemplateListComponent(navController = navController) }
        composable(Route.TASK_TEMPLATE_CREATE.path) { TaskTemplateCreateComponent(navController = navController) }
        composable(Route.TASK_TEMPLATE_UPDATE.path) { TaskTemplateUpdateComponent(navController = navController, it.arguments?.getString("taskTemplateId")!!) }
//        composable(Route.CREATE_PAGE.path) { CreateAdminPage(navController = navController) }
        composable(Route.TASK_CREATE.path) { TaskCreate(navController = navController, taskTemplateId = it.arguments?.getString("taskTemplateId")!!) }
        composable(Route.TASK_SCHEDULER_CREATE.path) { TaskSchedulerCreate(navController = navController, taskTemplateId = it.arguments?.getString("taskTemplateId")) }
    }
}