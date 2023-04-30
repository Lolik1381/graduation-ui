package ru.stankin.compose.presentation.task.admin

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import ru.stankin.compose.core.exceprion.ValidationException
import ru.stankin.compose.core.ext.isAfterCurrentDate
import ru.stankin.compose.core.ext.requiredValue
import ru.stankin.compose.core.ext.update
import ru.stankin.compose.core.util.Route
import ru.stankin.compose.core.util.content
import ru.stankin.compose.core.util.errorMessage
import ru.stankin.compose.core.util.onFailure
import ru.stankin.compose.core.util.onSuccess
import ru.stankin.compose.datasource.Repositories
import ru.stankin.compose.model.GroupDto
import ru.stankin.compose.model.TaskDto
import ru.stankin.compose.model.TaskTemplateDto
import ru.stankin.compose.model.UserDto

class TaskCRUDViewModel(
    taskDto: TaskDto = TaskDto(
        taskTemplate = TaskTemplateDto()
    )
) : ViewModel() {

    private val _taskDto = mutableStateOf(taskDto)

    private val _taskTemplate = mutableStateOf<TaskTemplateDto?>(null)
    private val _userList = mutableStateListOf<UserDto>()
    private val _groupList = mutableStateListOf<GroupDto>()

    private val _errorMessage = mutableStateOf<String?>(null)

    val taskDto
        get() = _taskDto.value

    var taskTemplate
        get() = _taskTemplate.value
        set(value) {
            _taskTemplate.value = value
        }

    val userList
        get() = _userList.toList()

    val groupList
        get() = _groupList.toList()

    var errorMessage
        get() = _errorMessage.value
        set(value) {
            _errorMessage.value = value
        }

    fun init(taskTemplateId: String? = null) {
        viewModelScope.launch {
            taskTemplateId?.let {
                Repositories.adminTaskTemplateRepository.findById(taskTemplateId)
                    .onSuccess {
                        taskTemplate = it.content()
                        taskDto.taskTemplate?.id = taskTemplate?.id
                    }
                    .onFailure { errorMessage = it.errorMessage() }
            }

            Repositories.adminUserRepository.findAll(size = 5)
                .onSuccess { _userList.update(it.content()?.content) }
                .onFailure { errorMessage = it.errorMessage() }

            Repositories.adminUserRepository.findAllGroups(size = 5)
                .onSuccess { _groupList.update(it.content()?.content) }
                .onFailure { errorMessage = it.errorMessage() }
        }
    }

    fun updateUserInfo(searchText: String) {
        viewModelScope.launch {
            Repositories.adminUserRepository.findAll(searchText = searchText, size = 5)
                .onSuccess { _userList.update(it.content()?.content) }
                .onFailure { errorMessage = it.errorMessage() }
        }

        viewModelScope.launch {
            Repositories.adminUserRepository.findAllGroups(searchName = searchText, size = 5)
                .onSuccess { _groupList.update(it.content()?.content) }
                .onFailure { errorMessage = it.errorMessage() }
        }
    }

    fun create(context: Context, navController: NavController, userIdOrGroupId: String?) {
        try {
            taskDto.taskTemplate?.id.requiredValue("Шаблон задания")
            taskDto.expireDate.isAfterCurrentDate("Дата истечения срока задания")
            userIdOrGroupId.requiredValue("Пользователь или группа")

            taskDto.systemUserId = userList.find { it.id == userIdOrGroupId }?.id
            taskDto.groupId = groupList.find { it.id == userIdOrGroupId }?.id

            viewModelScope.launch {
                Repositories.adminTaskRepository.createTask(taskDto)
                    .onSuccess {
                        navController.navigate(Route.TASK_TEMPLATE.path)
                        Toast.makeText(context, "Задание успешно создано", Toast.LENGTH_SHORT).show()
                    }
                    .onFailure { errorMessage = it.errorMessage() }
            }
        } catch (e: ValidationException) {
            errorMessage = e.message
        }
    }
}