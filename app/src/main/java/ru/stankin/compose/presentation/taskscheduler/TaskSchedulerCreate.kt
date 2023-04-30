package ru.stankin.compose.presentation.taskscheduler

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import ru.stankin.compose.R
import ru.stankin.compose.model.TaskSchedulerDto
import ru.stankin.compose.presentation.component.DateTimePickerComponent
import ru.stankin.compose.presentation.component.OutlinedDropdownTextField
import ru.stankin.compose.presentation.component.UserOrGroupSelectorComponent
import ru.stankin.compose.presentation.component.UserOrGroupState
import ru.stankin.compose.presentation.taskscheduler.viewmodel.TaskSchedulerCreateViewModel
import java.time.ZoneId
import java.time.ZonedDateTime

@Preview(showBackground = true)
@Composable
fun TaskSchedulerCreate(
    navController: NavController = rememberNavController(),
    componentViewModel: TaskSchedulerCreateViewModel = viewModel(),
    taskTemplateId: String? = null
) {
    val context = LocalContext.current
    val dialogState = rememberMaterialDialogState()
    var typeTextField by remember { mutableStateOf("") }

    var cron by remember { mutableStateOf("") }
    var triggerDate by remember { mutableStateOf<ZonedDateTime?>(null) }

    LaunchedEffect(key1 = Unit) {
        componentViewModel.init(navController, taskTemplateId)
    }

    if (componentViewModel.isError) {
        Toast.makeText(context, "Произошла ошибка - '${componentViewModel.errorMessage}'", Toast.LENGTH_LONG).show()
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.task_scheduler_create, componentViewModel.taskScheduler.taskTemplate?.header.orEmpty()),
            modifier = Modifier
                .padding(top = 15.dp, bottom = 5.dp),
            fontSize = 4.em,
            fontWeight = FontWeight.W500
        )

        Divider(startIndent = 4.dp, thickness = 1.dp, color = Color.Gray)

        OutlinedDropdownTextField(
            items = TaskSchedulerDto.TaskSchedulerTypeDto.values().associateWith { it.russianValue },
            label = "Тип планировщика",
            defaultValue = typeTextField,
            onClick = { type, russianValue ->
                componentViewModel.taskScheduler.type = type
                typeTextField = russianValue
            }
        )

        AnimatedVisibility(visible = componentViewModel.taskScheduler.type == TaskSchedulerDto.TaskSchedulerTypeDto.CRON) {
            OutlinedTextField(
                value = cron,
                onValueChange = {
                    cron = it
                    componentViewModel.taskScheduler.cron = it
                },
                label = { Text(text = "cron") },
                placeholder = { Text(text = "cron") }
            )
        }

        AnimatedVisibility(visible = componentViewModel.taskScheduler.type == TaskSchedulerDto.TaskSchedulerTypeDto.DATE) {
            OutlinedTextField(
                value = triggerDate?.toString() ?: "Не определено",
                onValueChange = {  },
                enabled = false,
                modifier = Modifier.clickable { dialogState.show() },
                label = { Text(text = "Время срабатывания") },
                placeholder = { Text(text = "Время срабатывания") }
            )
        }

        UserOrGroupSelectorComponent(
            onClick = { state, id ->
                when (state) {
                    UserOrGroupState.USER -> componentViewModel.taskScheduler.userId = id
                    UserOrGroupState.GROUP -> componentViewModel.taskScheduler.groupId = id
                    else -> {
                        componentViewModel.taskScheduler.userId = null
                        componentViewModel.taskScheduler.groupId = null
                    }
                }
            }
        )

        TaskSchedulerDelayPicker(
            daysDelayChangeValue = { componentViewModel.days = it.toLongOrNull() ?: 0 },
            minutesDelayChangeValue = { componentViewModel.minutes = it.toLongOrNull() ?: 0 }
        )

        OutlinedButton(
            onClick = componentViewModel::create
        ) {
            Text(text = "Создать")
        }
    }

    DateTimePickerComponent(
        dateTimePickerDialogState = dialogState,
        positiveOnClick = { date, time ->
            triggerDate = ZonedDateTime.of(date, time, ZoneId.systemDefault())
            componentViewModel.taskScheduler.triggerDate = triggerDate
        }
    )
}

@Preview(showBackground = true)
@Composable
fun TaskSchedulerDelayPicker(
    daysDelayChangeValue: (String) -> Unit = {  },
    minutesDelayChangeValue: (String) -> Unit = {  }
) {
    var days by remember { mutableStateOf("1") }
    var minutes by remember { mutableStateOf("0") }

    val width = 120.dp
    val height = 60.dp

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Какое время дается на выполнение задания?",
            modifier = Modifier.padding(10.dp, 10.dp)
        )

        Row(
            modifier = Modifier.padding(10.dp, 0.dp)
        ) {
            OutlinedTextField(
                value = days,
                onValueChange = {
                    var value = it
                    if (it.isNotBlank() && it.toInt() > 999) {
                        value = "999"
                    }

                    daysDelayChangeValue.invoke(value)
                    days = value
                },
                modifier = Modifier
                    .padding(horizontal = 5.dp, vertical = 5.dp)
                    .width(width)
                    .height(height),
                label = { Text(text = "Дней") },
                placeholder = { Text(text = "Дней") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = minutes,
                onValueChange = {
                    var value = it
                    if (it.isNotBlank() && it.toInt() > 59) {
                        value = "59"
                    }

                    minutesDelayChangeValue.invoke(value)
                    minutes = value
                },
                modifier = Modifier
                    .padding(horizontal = 5.dp, vertical = 5.dp)
                    .width(width)
                    .height(height),
                label = { Text(text = "Минут") },
                placeholder = { Text(text = "Минут") },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = true,
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )
        }
    }
}