package ru.stankin.compose.presentation.task.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import ru.stankin.compose.R
import ru.stankin.compose.presentation.component.DateTimePickerComponent
import ru.stankin.compose.presentation.component.OutlinedDropdownTextField
import java.time.ZoneId
import java.time.ZonedDateTime

@Composable
fun TaskCreate(
    navController: NavController = rememberNavController(),
    taskTemplateId: String? = null,
    taskCRUDViewModel: TaskCRUDViewModel = viewModel()
) {
    val context = LocalContext.current
    var userIdOrGroupId by rememberSaveable { mutableStateOf("") }
    var userIdOrGroupName by rememberSaveable { mutableStateOf("") }
    val dialogState = rememberMaterialDialogState()

    LaunchedEffect(key1 = Unit) {
        taskCRUDViewModel.init(taskTemplateId)

        taskCRUDViewModel.taskDto.expireDate = ZonedDateTime.now().plusDays(1L)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (!taskCRUDViewModel.errorMessage.isNullOrBlank()) {
            Text(
                text = taskCRUDViewModel.errorMessage!!,
                color = Color.Red
            )
        }

        Text(
            text = stringResource(id = R.string.task_create, taskCRUDViewModel.taskTemplate?.header.orEmpty()),
            modifier = Modifier
                .padding(top = 15.dp, bottom = 5.dp),
            fontSize = 4.em,
            fontWeight = FontWeight.W500
        )

        val usersAndGroups = mutableMapOf<String, String>().apply {
            putAll(taskCRUDViewModel.userList.associate { it.id!! to "${it.firstName} ${it.lastName} (${it.email})" })
            putAll(taskCRUDViewModel.groupList.associate { it.id!! to it.name!! })
        }

        OutlinedDropdownTextField(
            items = usersAndGroups,
            enableSearch = true,
            label = "Пользователь или группа",
            defaultValue = userIdOrGroupName,
            onClick = { key, value ->
                userIdOrGroupId = key
                userIdOrGroupName = value
            },
            onUpdatedValues = { taskCRUDViewModel.updateUserInfo(it) }
        )

        Column(
            modifier = Modifier
                .clickable { dialogState.show() }
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Дата и время окончания чек-листа:",
                modifier = Modifier.padding(all = 2.dp)
            )
            Text(
                text = taskCRUDViewModel.taskDto.expireDate?.toString().orEmpty(),
                modifier = Modifier.padding(all = 2.dp)
            )
        }

        OutlinedButton(
            onClick = {
                taskCRUDViewModel.create(context, navController, userIdOrGroupId)
            }
        ) {
            Text(text = "Создать")
        }

        DateTimePickerComponent(
            dateTimePickerDialogState = dialogState,
            positiveOnClick = { date, time ->
                taskCRUDViewModel.taskDto.expireDate = ZonedDateTime.of(date, time, ZoneId.systemDefault())
            }
        )
    }
}