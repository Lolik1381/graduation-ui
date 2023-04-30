package ru.stankin.compose.presentation.tasktemplate.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.stankin.compose.R
import ru.stankin.compose.model.TaskTemplateCheckDto
import ru.stankin.compose.presentation.component.OutlinedDropdownTextField
import ru.stankin.compose.presentation.component.TextComponent
import ru.stankin.compose.presentation.tasktemplate.admin.viewmodel.TaskTemplateCRUDViewModel

@Composable
fun TaskTemplateCheckCardComponent(
    taskTemplateStateCheck: TaskTemplateCRUDViewModel.TaskTemplateStateCheck,
    onDelete: () -> Unit = {  },
    onUp: () -> Unit = {},
    onDown: () -> Unit = {},
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
                    .height(110.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextComponent(
                    text = stringResource(id = R.string.task_template_check_constructor),
                    modifier = Modifier
                        .padding(top = 15.dp, bottom = 5.dp)
                        .fillMaxWidth(0.5f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h6
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextComponent(text = "Порядок: ${taskTemplateStateCheck.order}", style = MaterialTheme.typography.caption)
            }

            OutlinedTextField(
                value = taskTemplateStateCheck.name,
                onValueChange = { value -> taskTemplateStateCheck.name = value },
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 5.dp)
                    .fillMaxWidth(),
                label = { Text(text = "${stringResource(id = R.string.task_template_constructor_check_name)} *") },
                placeholder = { Text(text = "${stringResource(id = R.string.task_template_constructor_check_name)} *") },
                enabled = updateAvailable
            )

            OutlinedTextField(
                value = taskTemplateStateCheck.description,
                onValueChange = { value -> taskTemplateStateCheck.description = value },
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
                        value = taskTemplateStateCheck.requiredPhoto,
                        onValueChange = { value -> taskTemplateStateCheck.requiredPhoto = value },
                        enabled = updateAvailable
                    )
            ) {
                Switch(checked = taskTemplateStateCheck.requiredPhoto, onCheckedChange = null)
                Spacer(Modifier.width(8.dp))

                Text(text = stringResource(id = R.string.task_template_constructor_check_required_photo))
            }

            Row(
                Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .toggleable(
                        role = Role.Switch,
                        value = taskTemplateStateCheck.requiredComment,
                        onValueChange = { value -> taskTemplateStateCheck.requiredComment = value },
                        enabled = updateAvailable
                    )
            ) {
                Switch(checked = taskTemplateStateCheck.requiredComment, onCheckedChange = null)
                Spacer(Modifier.width(8.dp))

                Text(text = stringResource(id = R.string.task_template_constructor_check_required_comment))
            }

            Row(
                Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .toggleable(
                        role = Role.Switch,
                        value = taskTemplateStateCheck.requiredControlValue,
                        onValueChange = { value -> taskTemplateStateCheck.requiredControlValue = value },
                        enabled = updateAvailable
                    )
            ) {
                Switch(checked = taskTemplateStateCheck.requiredControlValue, onCheckedChange = null)
                Spacer(Modifier.width(8.dp))

                Text(text = stringResource(id = R.string.task_template_constructor_check_required_control_value))
            }

            OutlinedDropdownTextField(
                items = TaskTemplateCheckDto.PermissionControlValueType.values().associateWith { it.russianName },
                onClick = { key, _ -> taskTemplateStateCheck.controlValueType = key },
                defaultValue = taskTemplateStateCheck.controlValueType?.russianName.orEmpty(),
                label = "${stringResource(id = R.string.task_template_constructor_check_control_value_type)} *",
                visible = taskTemplateStateCheck.requiredControlValue
            )
        }
    }
}