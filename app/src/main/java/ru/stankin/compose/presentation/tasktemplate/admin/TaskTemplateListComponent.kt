package ru.stankin.compose.presentation.tasktemplate.admin

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ru.stankin.compose.presentation.component.ExceptionComponent
import ru.stankin.compose.presentation.tasktemplate.admin.viewmodel.TaskTemplateViewModel

@Preview(showBackground = true)
@Composable
fun TaskTemplateListComponent(
    navController: NavController = rememberNavController(),
    taskTemplateViewModel: TaskTemplateViewModel = viewModel()
) {
    LaunchedEffect(key1 = Unit) {
        taskTemplateViewModel.init()
    }

    if (taskTemplateViewModel.errorMessage.isBlank()) {
        LazyColumn {
            items(items = taskTemplateViewModel.taskTemplateList) {
                TaskTemplateListCardComponent(navController, it)
            }
        }
    } else {
        ExceptionComponent()
    }
}