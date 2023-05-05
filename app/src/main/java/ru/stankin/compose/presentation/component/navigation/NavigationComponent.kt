package ru.stankin.compose.presentation.component.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.stankin.compose.core.util.Route
import ru.stankin.compose.presentation.auth.Auth
import ru.stankin.compose.presentation.profile.admin.AdminProfile
import ru.stankin.compose.presentation.profile.ChangePassword
import ru.stankin.compose.presentation.profile.UserProfile
import ru.stankin.compose.presentation.profile.admin.CreateGroup
import ru.stankin.compose.presentation.task.admin.TaskCreate
import ru.stankin.compose.presentation.task.user.task.UserTaskComponent
import ru.stankin.compose.presentation.task.user.tasklist.UserTaskListComponent
import ru.stankin.compose.presentation.taskscheduler.TaskSchedulerCreate
import ru.stankin.compose.presentation.tasktemplate.admin.TaskTemplateComponent
import ru.stankin.compose.presentation.tasktemplate.admin.TaskTemplateListComponent

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
        composable(Route.AUTH.path) {
            Auth(navigationController = navController, authViewModel = viewModel())
        }

        composable(Route.CHANGE_PASSWORD.path) {
            ChangePassword(navController = navController, changePasswordViewModel = viewModel())
        }

        composable(Route.TASK_INFO.path) {
            UserTaskListComponent(navController = navController)
        }

        composable(Route.TASK.path) {
            UserTaskComponent(it.arguments?.getString("taskId")!!, navController = navController)
        }

        composable(Route.USER_PROFILE.path) {
            UserProfile(navController = navController)
        }

        composable(Route.ADMIN_PROFILE.path) {
            AdminProfile(navController = navController, viewModel = viewModel())
        }

        composable(Route.TASK_TEMPLATE_LIST.path) {
            TaskTemplateListComponent(navController = navController, viewModel = viewModel())
        }

        composable(
            route = Route.TASK_TEMPLATE.path,
            arguments = listOf(
                navArgument("taskTemplateId") {
                    nullable = true
                    type = NavType.StringType
                }
            )
        ) {
            TaskTemplateComponent(taskTemplateId = it.arguments?.getString("taskTemplateId"), navController = navController, viewModel = viewModel())
        }

//        composable(Route.TASK_TEMPLATE_UPDATE.path) { TaskTemplateUpdateComponent(navController = navController, it.arguments?.getString("taskTemplateId")!!) }
        composable(Route.TASK_CREATE.path) {
            TaskCreate(navController = navController, taskTemplateId = it.arguments?.getString("taskTemplateId")!!)
        }

        composable(Route.TASK_SCHEDULER_CREATE.path) {
            TaskSchedulerCreate(navController = navController, taskTemplateId = it.arguments?.getString("taskTemplateId"))
        }

        composable(Route.CREATE_GROUP.path) {
            CreateGroup(
                navController = navController,
                viewModel = viewModel()
            )
        }
    }
}