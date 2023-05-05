package ru.stankin.compose.presentation.tasktemplate.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.navigation.NavController
import ru.stankin.compose.R
import ru.stankin.compose.core.ext.navigate
import ru.stankin.compose.core.util.Route
import ru.stankin.compose.model.TaskTemplateCheckDto
import ru.stankin.compose.presentation.component.ErrorScreen
import ru.stankin.compose.presentation.component.LoadingScreen
import ru.stankin.compose.presentation.component.OutlinedDropdownTextField
import ru.stankin.compose.presentation.component.TextComponent
import ru.stankin.compose.presentation.tasktemplate.admin.screen.TaskTemplateScreen
import ru.stankin.compose.viewmodel.TaskTemplateViewModel
import ru.stankin.compose.viewmodel.event.TaskTemplateEvent
import ru.stankin.compose.viewmodel.state.TaskTemplateState
import ru.stankin.compose.viewmodel.viewstate.TaskTemplateViewState

@Composable
fun TaskTemplateComponent(
    taskTemplateId: String? = null,
    navController: NavController,
    viewModel: TaskTemplateViewModel
) {
    when (val state = viewModel.state) {
        is TaskTemplateState.Loading -> LoadingScreen()
        is TaskTemplateState.Loaded -> TaskTemplateW(viewModel = viewModel, viewState = viewModel.viewState as TaskTemplateViewState.Initialized)
        is TaskTemplateState.Error -> ErrorScreen(onRefresh = { viewModel.obtainEvent(TaskTemplateEvent.Reload(taskTemplateId)) }, message = state.message)
        is TaskTemplateState.SavedProcess -> TaskTemplateW(viewModel = viewModel, viewState = viewModel.viewState as TaskTemplateViewState.Initialized)
        is TaskTemplateState.Success -> {
            navController.navigate(Route.TASK_TEMPLATE_LIST)
            viewModel.obtainEvent(TaskTemplateEvent.Completed)
        }
        is TaskTemplateState.NavigateToTaskCreate -> {
            navController.navigate(Route.TASK_CREATE, state.taskTemplateId)
            viewModel.obtainEvent(TaskTemplateEvent.Completed)
        }
        is TaskTemplateState.NavigateToTaskSchedulerCreate -> {
            navController.navigate(Route.TASK_SCHEDULER_CREATE, state.taskTemplateId)
            viewModel.obtainEvent(TaskTemplateEvent.Completed)
        }
        is TaskTemplateState.Completed -> {}
    }

    LaunchedEffect(
        key1 = Unit,
        block = { viewModel.obtainEvent(TaskTemplateEvent.LoadingComplete(taskTemplateId)) })
}

@Composable
fun TaskTemplateW(
    viewModel: TaskTemplateViewModel,
    viewState: TaskTemplateViewState.Initialized
) {
    TaskTemplateScreen(viewModel.make())
}

@Composable
fun TaskTemplateCheckCardComponent(
    viewModel: TaskTemplateViewModel,
    taskTemplateCheckDto: TaskTemplateCheckDto,
    onDelete: () -> Unit,
    onUp: () -> Unit,
    onDown: () -> Unit,
    updateAvailable: Boolean = false
) {

    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp),
        elevation = 2.dp,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(corner = CornerSize(8.dp))
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.task_template_check_constructor),
                    modifier = Modifier
                        .padding(top = 15.dp, bottom = 5.dp)
                        .fillMaxWidth(0.6f),
                    fontSize = 4.em,
                    fontWeight = FontWeight.W500,
                    textAlign = TextAlign.Center
                )

                IconButton(
                    onClick = onUp,
                    enabled = updateAvailable
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowUpward,
                        contentDescription = "Поднять",
                        tint = if (updateAvailable) Color.Black else Color.Gray
                    )
                }

                IconButton(
                    onClick = onDown,
                    enabled = updateAvailable
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowDownward,
                        contentDescription = "Опустить",
                        tint = if (updateAvailable) Color.Black else Color.Gray
                    )
                }

                IconButton(
                    onClick = onDelete,
                    enabled = updateAvailable
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Удалить",
                        tint = if (updateAvailable) Color.Red else Color.Gray
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextComponent(text = "Порядок: ${taskTemplateCheckDto.taskTemplateCheckOrder}", style = MaterialTheme.typography.caption)
            }

            OutlinedTextField(
                value = taskTemplateCheckDto.name,
                onValueChange = { viewModel.obtainEvent(TaskTemplateEvent.TaskTemplateCheckNameChange(taskTemplateCheckDto, it)) },
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 5.dp)
                    .fillMaxWidth(),
                label = { Text(text = "${stringResource(id = R.string.task_template_constructor_check_name)} *") },
                placeholder = { Text(text = "${stringResource(id = R.string.task_template_constructor_check_name)} *") },
                enabled = updateAvailable
            )

            OutlinedTextField(
                value = taskTemplateCheckDto.description,
                onValueChange = { viewModel.obtainEvent(TaskTemplateEvent.TaskTemplateCheckDescriptionChange(taskTemplateCheckDto, it)) },
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 5.dp)
                    .fillMaxWidth(),
                label = { Text(text = "${stringResource(id = R.string.task_template_constructor_check_description)} *") },
                placeholder = { Text(text = "${stringResource(id = R.string.task_template_constructor_check_description)} *") },
                enabled = updateAvailable
            )

            Row(
                Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .toggleable(
                        role = Role.Switch,
                        value = taskTemplateCheckDto.requiredPhoto,
                        onValueChange = {
                            viewModel.obtainEvent(
                                TaskTemplateEvent.TaskTemplateCheckRequiredPhotoChange(
                                    taskTemplateCheckDto,
                                    it
                                )
                            )
                        },
                        enabled = updateAvailable
                    )
            ) {
                Switch(checked = taskTemplateCheckDto.requiredPhoto, onCheckedChange = null)
                Spacer(Modifier.width(8.dp))

                Text(text = stringResource(id = R.string.task_template_constructor_check_required_photo))
            }

            Row(
                Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .toggleable(
                        role = Role.Switch,
                        value = taskTemplateCheckDto.requiredComment,
                        onValueChange = {
                            viewModel.obtainEvent(
                                TaskTemplateEvent.TaskTemplateCheckRequiredCommentChange(
                                    taskTemplateCheckDto,
                                    it
                                )
                            )
                        },
                        enabled = updateAvailable
                    )
            ) {
                Switch(checked = taskTemplateCheckDto.requiredComment, onCheckedChange = null)
                Spacer(Modifier.width(8.dp))

                Text(text = stringResource(id = R.string.task_template_constructor_check_required_comment))
            }

            Row(
                Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .toggleable(
                        role = Role.Switch,
                        value = taskTemplateCheckDto.requiredControlValue,
                        onValueChange = {
                            viewModel.obtainEvent(
                                TaskTemplateEvent.TaskTemplateCheckRequiredControlValueChange(
                                    taskTemplateCheckDto,
                                    it
                                )
                            )
                        },
                        enabled = updateAvailable
                    )
            ) {
                Switch(checked = taskTemplateCheckDto.requiredControlValue, onCheckedChange = null)
                Spacer(Modifier.width(8.dp))

                Text(text = stringResource(id = R.string.task_template_constructor_check_required_control_value))
            }

            OutlinedDropdownTextField(
                items = TaskTemplateCheckDto.PermissionControlValueType.values().associateWith { it.russianName },
                onClick = { key, _ -> viewModel.obtainEvent(TaskTemplateEvent.TaskTemplateCheckPermissionControlValueTypeChange(taskTemplateCheckDto, key)) },
                defaultValue = taskTemplateCheckDto.controlValueType.russianName,
                label = "${stringResource(id = R.string.task_template_constructor_check_control_value_type)} *",
                visible = taskTemplateCheckDto.requiredControlValue
            )
        }
    }
}