package ru.stankin.compose.presentation.tasktemplate.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ru.stankin.compose.R
import ru.stankin.compose.presentation.tasktemplate.admin.viewmodel.TaskTemplateCRUDViewModel

@Preview(showBackground = true)
@Composable
fun TaskTemplateCreateComponent(
    navController: NavController = rememberNavController(),
    viewModel: TaskTemplateCRUDViewModel = viewModel()
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.task_template_constructor),
                    modifier = Modifier
                        .padding(top = 15.dp, bottom = 5.dp),
                    fontSize = 4.em,
                    fontWeight = FontWeight.W500
                )

                Spacer(Modifier.width(8.dp))

                IconButton(onClick = viewModel::clear) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Удалить",
                        tint = Color.Red
                    )
                }
            }

            OutlinedTextField(
                value = viewModel.taskTemplateName,
                onValueChange = { viewModel.taskTemplateName = it },
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 5.dp)
                    .fillMaxWidth(),
                label = { Text(text = "${stringResource(id = R.string.task_template_constructor_name)} *") },
                placeholder = { Text(text = "${stringResource(id = R.string.task_template_constructor_name)} *") }
            )

            OutlinedTextField(
                value = viewModel.taskTemplateDescription,
                onValueChange = { viewModel.taskTemplateDescription = it },
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 5.dp)
                    .fillMaxWidth(),
                label = { Text(text = "${stringResource(id = R.string.task_template_constructor_description)} *") },
                placeholder = { Text(text = "${stringResource(id = R.string.task_template_constructor_description)} *") }
            )
        }

        items(items = viewModel.taskTemplateChecks) {
            TaskTemplateCheckCardComponent(
                taskTemplateStateCheck = it,
                onDelete = { viewModel.removeTaskTemplateCheck(it) },
                onUp = { viewModel.orderUp(it) },
                onDown = { viewModel.orderDown(it) },
                updateAvailable = true
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedButton(
                    onClick = viewModel::addTaskTemplateCheck,
                    modifier = Modifier
                        .width(200.dp)
                        .height(50.dp)
                        .padding(vertical = 5.dp, horizontal = 5.dp)
                ) {
                    Text(text = stringResource(id = R.string.task_template_constructor_add_check))
                }

                OutlinedButton(
                    onClick = { viewModel.createTaskTemplate(navController) },
                    modifier = Modifier
                        .width(60.dp)
                        .height(50.dp)
                        .padding(vertical = 5.dp, horizontal = 5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Сохранить",
                        tint = Color.Green
                    )
                }
            }
        }
    }
}