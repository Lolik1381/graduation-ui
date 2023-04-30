package ru.stankin.compose.presentation.task.user.tasklist

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ru.stankin.compose.R
import ru.stankin.compose.presentation.component.DividerComponent
import ru.stankin.compose.presentation.component.TextComponent

@Preview
@Composable
fun UserTaskListComponent(
    navController: NavController = rememberNavController(),
    viewModel: UserTaskListViewModel = viewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.init()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            TextComponent(text = stringResource(id = R.string.user_task_list), style = MaterialTheme.typography.h5)
            DividerComponent()
        }

        items(items = viewModel.tasksInfo) {
            UserTaskListCardComponent(taskMetadataDto = it, navController = navController)
        }
    }

    if (viewModel.errorMessage.isNotBlank()) {
        Toast.makeText(LocalContext.current, viewModel.errorMessage, Toast.LENGTH_SHORT).show()
    }
}