package ru.stankin.compose.presentation.tasktemplate.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.stankin.compose.core.ext.navigate
import ru.stankin.compose.core.util.Route
import ru.stankin.compose.model.TaskTemplateDto
import ru.stankin.compose.presentation.component.EmptyScreen
import ru.stankin.compose.presentation.component.ErrorScreen
import ru.stankin.compose.presentation.component.LoadingScreen
import ru.stankin.compose.viewmodel.TaskTemplateListViewModel
import ru.stankin.compose.viewmodel.event.TaskTemplateListEvent
import ru.stankin.compose.viewmodel.state.TaskTemplateListState
import ru.stankin.compose.viewmodel.viewstate.TaskTemplateListViewState

@Composable
fun TaskTemplateListComponent(
    navController: NavController,
    viewModel: TaskTemplateListViewModel
) {
    when (val state = viewModel.state) {
        is TaskTemplateListState.Loading -> LoadingScreen()
        is TaskTemplateListState.Error -> ErrorScreen(onRefresh = { viewModel.obtainEvent(TaskTemplateListEvent.Reload) }, message = state.message)
        is TaskTemplateListState.Loaded -> TaskTemplateListScreen(viewModel, viewModel.viewState as TaskTemplateListViewState.Initialized)
        is TaskTemplateListState.Empty -> EmptyScreen(onRefresh = { viewModel.obtainEvent(TaskTemplateListEvent.Reload) }, message = state.message)
        is TaskTemplateListState.NavigateToTaskTemplateCardList -> {
            viewModel.obtainEvent(TaskTemplateListEvent.Completed)
            navController.navigate(Route.TASK_TEMPLATE, state.taskTemplateId)
        }
        is TaskTemplateListState.Completed -> {}
    }

    LaunchedEffect(key1 = Unit, block = { viewModel.obtainEvent(TaskTemplateListEvent.LoadingComplete) })
}

@Composable
fun TaskTemplateListScreen(
    viewModel: TaskTemplateListViewModel,
    viewState: TaskTemplateListViewState.Initialized
) {
    LazyColumn {
        items(items = viewState.taskTemplatesDto) {
            TaskTemplateListCardComponent(onClick = { viewModel.obtainEvent(TaskTemplateListEvent.TaskTemplateListCardClick(it.id)) }, taskTemplateDto = it)
        }
    }
}

@Composable
fun TaskTemplateListCardComponent(
    onClick: () -> Unit,
    taskTemplateDto: TaskTemplateDto
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = 2.dp,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(corner = CornerSize(8.dp))
    ) {
        Row {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 2.dp),
                    text = taskTemplateDto.header,
                    style = MaterialTheme.typography.h6
                )

                Text(
                    modifier = Modifier.padding(top = 2.dp),
                    text = taskTemplateDto.description,
                    style = MaterialTheme.typography.caption
                )

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Проверок: ${taskTemplateDto.taskTemplateChecks.size}",
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.caption,
                    color = Color.Gray
                )

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Статус: ${taskTemplateDto.status.description}",
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.caption,
                    color = Color.Gray
                )
            }
        }
    }
}