package ru.stankin.compose.presentation.tasktemplate.admin.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import ru.stankin.compose.R
import ru.stankin.compose.model.EquipmentDto
import ru.stankin.compose.presentation.component.OutlinedDropdownTextField
import ru.stankin.compose.presentation.tasktemplate.admin.TaskTemplateCheckCardComponent
import ru.stankin.compose.viewmodel.TaskTemplateViewModel
import ru.stankin.compose.viewmodel.event.TaskTemplateEvent
import ru.stankin.compose.viewmodel.viewstate.TaskTemplateViewState

@Composable
fun TaskTemplateCreateScreen(
    viewModel: TaskTemplateViewModel,
    viewState: TaskTemplateViewState.Initialized,
    buttonContent: @Composable RowScope.() -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.task_template_constructor),
                    modifier = Modifier
                        .padding(top = 15.dp, bottom = 5.dp)
                        .fillMaxWidth(0.9f),
                    fontSize = 4.em,
                    fontWeight = FontWeight.W500,
                    textAlign = TextAlign.Center
                )

                IconButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModel.obtainEvent(TaskTemplateEvent.Clear) }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Удалить",
                        tint = Color.Red
                    )
                }
            }

            OutlinedTextField(
                value = viewState.taskTemplateName,
                onValueChange = { viewModel.obtainEvent(TaskTemplateEvent.TaskTemplateNameChange(it)) },
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 5.dp)
                    .fillMaxWidth(),
                label = { Text(text = "${stringResource(id = R.string.task_template_constructor_name)} *") },
                placeholder = { Text(text = "${stringResource(id = R.string.task_template_constructor_name)} *") }
            )

            OutlinedTextField(
                value = viewState.taskTemplateDescription,
                onValueChange = { viewModel.obtainEvent(TaskTemplateEvent.TaskTemplateDescriptionChange(it)) },
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 5.dp)
                    .fillMaxWidth(),
                label = { Text(text = "${stringResource(id = R.string.task_template_constructor_description)} *") },
                placeholder = { Text(text = "${stringResource(id = R.string.task_template_constructor_description)} *") }
            )

            OutlinedDropdownTextField(
                items = viewState.equipmentsSearch,
                onClick = { key, value -> viewModel.obtainEvent(TaskTemplateEvent.TaskTemplateEquipmentClick(
                    EquipmentDto(key, value)
                )) },
                label = "${stringResource(id = R.string.task_template_constructor_equipment)} *",
                visible = true,
                expandedHandler = { viewModel.obtainEvent(TaskTemplateEvent.TaskTemplateEquipmentSearch(it)) },
                enableSearch = true
            )
        }

        items(items = viewState.taskTemplateChecks) {
            TaskTemplateCheckCardComponent(
                viewModel = viewModel,
                taskTemplateCheckDto = it,
                onDelete = { viewModel.obtainEvent(TaskTemplateEvent.TaskTemplateCheckDelete(it)) },
                onUp = { viewModel.obtainEvent(TaskTemplateEvent.TaskTemplateCheckUp(it)) },
                onDown = { viewModel.obtainEvent(TaskTemplateEvent.TaskTemplateCheckDown(it)) },
                updateAvailable = true
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedButton(
                    onClick = { viewModel.obtainEvent(TaskTemplateEvent.TaskTemplateCheckAdd) },
                    modifier = Modifier
                        .width(200.dp)
                        .height(55.dp)
                        .padding(vertical = 5.dp, horizontal = 5.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.task_template_constructor_add_check),
                        fontSize = 3.em,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }

                OutlinedButton(
                    onClick = { viewModel.obtainEvent(TaskTemplateEvent.SavedClick) },
                    modifier = Modifier
                        .width(60.dp)
                        .height(55.dp)
                        .padding(vertical = 5.dp, horizontal = 5.dp),
                    content = buttonContent
                )
            }
        }
    }
}