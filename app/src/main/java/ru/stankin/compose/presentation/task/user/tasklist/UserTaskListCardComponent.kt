package ru.stankin.compose.presentation.task.user.tasklist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ru.stankin.compose.R
import ru.stankin.compose.core.util.Route
import ru.stankin.compose.core.util.replacePathVariable
import ru.stankin.compose.model.TaskMetadataDto
import ru.stankin.compose.presentation.component.TextComponent

@Preview
@Composable
fun UserTaskListCardComponent(
    taskMetadataDto: TaskMetadataDto = TaskMetadataDto(header = "ewq", description = "ewq"),
    navController: NavController = rememberNavController()
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable { navController.navigate(Route.TASK.replacePathVariable(taskMetadataDto.id)) },
        elevation = 2.dp,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(corner = CornerSize(8.dp))
    ) {
        Row {
            Column(
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextComponent(text = stringResource(id = R.string.user_task_name), style = typography.h6)
                    TextComponent(text = taskMetadataDto.header.orEmpty(), style = typography.h6)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextComponent(text = stringResource(id = R.string.user_task_description), style = typography.caption)
                    TextComponent(text = taskMetadataDto.description.orEmpty(), style = typography.caption)
                }

                TextComponent(
                    text = "Статус: ${taskMetadataDto.status?.uiDescription.orEmpty()}",
                    style = typography.caption,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}