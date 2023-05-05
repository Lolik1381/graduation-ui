package ru.stankin.compose.presentation.task.user.task

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ru.stankin.compose.R
import ru.stankin.compose.model.TaskCheckDto
import ru.stankin.compose.presentation.component.CounterComponent
import ru.stankin.compose.presentation.component.DividerComponent
import ru.stankin.compose.presentation.component.TextComponent

@Preview
@Composable
fun UserTaskComponent(
    taskId: String? = null,
    navController: NavController = rememberNavController(),
    viewModel: UserTaskViewModel = viewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.init(taskId, context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        TextComponent(
            text = stringResource(id = R.string.user_task),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.fillMaxWidth()
        )

        DividerComponent()

        TextComponent(text = viewModel.task?.taskTemplate?.header.orEmpty(), style = MaterialTheme.typography.h6)
        TextComponent(text = viewModel.task?.taskTemplate?.description.orEmpty(), style = MaterialTheme.typography.caption)

        Row {
            DividerComponent(
                modifier = Modifier
                    .weight(0.8f)
                    .padding(0.dp, 10.dp)
            )
            CounterComponent(
                current = viewModel.getNumberCompleteChecks(),
                total = viewModel.getTotalChecks()
            )
        }

        LazyColumn {
            items(items = viewModel.taskTemplateChecks) {
                val taskCheckDto = viewModel.taskChecks.find { taskCheckDto -> taskCheckDto.taskCheck.taskTemplateCheckId == it.id }

                if (taskCheckDto?.taskCheck?.status == TaskCheckDto.TaskCheckStatus.FINISHED) {
                    UserTaskCheckCardPreviewComponent(
                        taskTemplateCheckDto = it,
                        taskCheckDto = taskCheckDto.taskCheck
                    )
                }

                if (taskCheckDto?.taskCheck?.status == TaskCheckDto.TaskCheckStatus.ACTIVE) {
                    UserTaskCheckCardComponent(
                        taskTemplateCheckDto = it,
                        viewModel = viewModel
                    )
                }
            }
        }
    }

    if (viewModel.errorMessage.isNotBlank()) {
        Toast.makeText(LocalContext.current, viewModel.errorMessage, Toast.LENGTH_SHORT).show()
    }
}