package ru.stankin.compose.presentation.task.user.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.stankin.compose.R
import ru.stankin.compose.model.TaskTemplateCheckDto
import ru.stankin.compose.presentation.component.TextComponent

@Composable
fun UserTaskCheckControlValueComponent(
    taskTemplateCheck: TaskTemplateCheckDto?,
    isError: Boolean = false,
    enabled: Boolean = true,
    controlValue: String,
    onControlValueUpdate: (String) -> Unit
) {
    val keyboardOptions = when (taskTemplateCheck?.controlValueType) {
        TaskTemplateCheckDto.PermissionControlValueType.INTEGER -> KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrect = true,
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Default
        )
        TaskTemplateCheckDto.PermissionControlValueType.STRING -> KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrect = true,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Default
        )
        else -> null
    }

    if (keyboardOptions != null) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = controlValue,
                onValueChange = onControlValueUpdate,
                label = { TextComponent(text = stringResource(id = R.string.text_field_control_value), required = taskTemplateCheck?.requiredControlValue) },
                placeholder = { TextComponent(text = stringResource(id = R.string.text_field_control_value), required = taskTemplateCheck?.requiredControlValue) },
                keyboardOptions = keyboardOptions,
                modifier = Modifier.fillMaxWidth(),
                isError = isError,
                enabled = enabled
            )
        }
    }
}

@Preview
@Composable
private fun UserTaskCardTextFieldComponentPreview() {
    var controlValue by remember { mutableStateOf("") }

    UserTaskCheckControlValueComponent(
        taskTemplateCheck = TaskTemplateCheckDto(
            controlValueType = TaskTemplateCheckDto.PermissionControlValueType.INTEGER
        ),
        controlValue = controlValue,
        onControlValueUpdate = { controlValue = it }
    )
}