package ru.stankin.compose.presentation.tasktemplate.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ru.stankin.compose.core.ext.navigate
import ru.stankin.compose.core.util.Route
import ru.stankin.compose.model.TaskTemplateDto

@Preview(showBackground = true)
@Composable
fun TaskTemplateListCardComponent(
    navController: NavController = rememberNavController(),
    taskTemplateDto: TaskTemplateDto = TaskTemplateDto()
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate(Route.TASK_TEMPLATE_UPDATE, taskTemplateDto.id)
            },
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
                    text = "Наименование: ${taskTemplateDto.header.orEmpty()}",
                    style = MaterialTheme.typography.h6
                )

                Divider(startIndent = 4.dp, thickness = 1.dp, color = Color.Gray)

                Text(
                    modifier = Modifier.padding(top = 2.dp),
                    text = "Описание: ${taskTemplateDto.description.orEmpty()}",
                    style = MaterialTheme.typography.caption
                )

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Проверок: ${taskTemplateDto.taskTemplateChecks?.size ?: 0}",
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.caption,
                    color = Color.Gray
                )

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Статус: ${taskTemplateDto.status?.description}",
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.caption,
                    color = Color.Gray
                )
            }
        }
    }
}