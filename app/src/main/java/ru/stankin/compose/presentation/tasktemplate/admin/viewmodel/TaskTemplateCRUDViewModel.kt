package ru.stankin.compose.presentation.tasktemplate.admin.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.stankin.compose.core.util.Route
import ru.stankin.compose.core.util.content
import ru.stankin.compose.core.util.errorMessage
import ru.stankin.compose.core.util.onFailure
import ru.stankin.compose.core.util.onSuccess
import ru.stankin.compose.retrofit.Repositories
import ru.stankin.compose.model.TaskTemplateCheckDto
import ru.stankin.compose.model.TaskTemplateDto

class TaskTemplateCRUDViewModel : ViewModel() {
    private val _taskTemplateId = mutableStateOf<String?>(null)
    private val _taskTemplateName = mutableStateOf("")
    private val _taskTemplateDescription = mutableStateOf("")
    private val _taskTemplateChecks = mutableStateListOf<TaskTemplateStateCheck>()
    private val _taskTemplateStatus = mutableStateOf<TaskTemplateDto.TaskTemplateDtoStatus?>(null)
    private val _errorMessage = mutableStateOf("")
    
    var taskTemplateName
        get() = _taskTemplateName.value
        set(value) {
            _taskTemplateName.value = value
        }
    
    var taskTemplateDescription
        get() = _taskTemplateDescription.value
        set(value) {
            _taskTemplateDescription.value = value
        }

    val taskTemplateId
        get() = _taskTemplateId.value

    val taskTemplateChecks
        get() = _taskTemplateChecks.toList().sortedBy { it.order }

    var taskTemplateStatus
        get() = _taskTemplateStatus.value
        set(value) {
            _taskTemplateStatus.value = value
        }

    var errorMessage
        get() = _errorMessage.value
        set(value) {
            _errorMessage.value = value
        }

    fun removeTaskTemplateCheck(taskTemplateCheck: TaskTemplateStateCheck) {
        _taskTemplateChecks.removeIf { it == taskTemplateCheck }
    }

    fun addTaskTemplateCheck() {
        _taskTemplateChecks.add(
            TaskTemplateStateCheck().apply {
                order = _taskTemplateChecks.size
            }
        )
    }

    fun createTaskTemplate(
        navController: NavController
    ) {
        val taskTemplateDto = toTaskTemplateDto()

        viewModelScope.launch {
            runBlocking {
                Repositories.adminTaskTemplateApi.createTaskTemplate(taskTemplateDto)

                navController.navigate(Route.TASK_TEMPLATE.path) {
                    navController.graph.startDestinationRoute?.let { screenRoute ->
                        popUpTo(screenRoute) {
                            saveState = true
                        }
                    }

                    launchSingleTop = true
                    restoreState = true
                }

                clear()
            }
        }
    }

    fun updateTaskTemplate(navController: NavController, context: Context) {
        val taskTemplateDto = toTaskTemplateDto()

        viewModelScope.launch {
            Repositories.adminTaskTemplateApi.updateTaskTemplate(_taskTemplateId.value!!, taskTemplateDto)
                .onSuccess {
                    Toast.makeText(context, "Шаблон задания успешно обновлен", Toast.LENGTH_SHORT).show()
                    navController.navigate(Route.TASK_TEMPLATE.path)
                }
                .onFailure { Toast.makeText(context, "Не удалось обновить шаблон задания. Попробуйте позже.", Toast.LENGTH_SHORT).show() }
        }
    }

    fun deleteTaskTemplate(navController: NavController, context: Context) {
        viewModelScope.launch {
            Repositories.adminTaskTemplateApi.deleteTaskTemplate(_taskTemplateId.value!!)
                .onSuccess {
                    Toast.makeText(context, "Шаблон задания успешно удален", Toast.LENGTH_SHORT).show()
                    navController.navigate(Route.TASK_TEMPLATE.path)
                }
                .onFailure { Toast.makeText(context, "Не удалось удалить шаблон задания. Попробуйте позже.", Toast.LENGTH_SHORT).show() }
        }
    }

    fun activateTaskTemplate(navController: NavController, context: Context) {
        viewModelScope.launch {
            Repositories.adminTaskTemplateApi.activateTaskTemplate(_taskTemplateId.value!!)
                .onSuccess {
                    Toast.makeText(context, "Шаблон задания успешно активирован", Toast.LENGTH_SHORT).show()
                    navController.navigate(Route.TASK_TEMPLATE.path)
                }
                .onFailure { Toast.makeText(context, "Не удалось активировать шаблон задания. Попробуйте позже.", Toast.LENGTH_SHORT).show() }
        }
    }

    fun clear() {
        taskTemplateName = ""
        taskTemplateDescription = ""
        _taskTemplateChecks.clear()
    }

    fun init(taskTemplateId: String) {
        viewModelScope.launch {
            Repositories.adminTaskTemplateApi.findById(taskTemplateId)
                .onSuccess {
                    val taskTemplateDto = it.content()

                    _taskTemplateId.value = taskTemplateDto?.id.orEmpty()
                    taskTemplateName = taskTemplateDto?.header.orEmpty()
                    taskTemplateDescription = taskTemplateDto?.description.orEmpty()
                    taskTemplateStatus = taskTemplateDto?.status

                    _taskTemplateChecks.clear()
                    taskTemplateDto?.taskTemplateChecks?.map { check ->
                        TaskTemplateStateCheck().apply {
                            id = check.id.orEmpty()
                            name = check.name.orEmpty()
                            description = check.description.orEmpty()
                            requiredPhoto = check.requiredPhoto
                            requiredComment = check.requiredComment
                            requiredControlValue = check.requiredControlValue
                            order = check.taskTemplateCheckOrder ?: -1
                            controlValueType = check.controlValueType
                        }
                    }?.run { _taskTemplateChecks.addAll(this) }
                }
                .onFailure { errorMessage = it.errorMessage() }
        }
    }

    fun orderUp(taskTemplateCheck: TaskTemplateStateCheck) {
        val currentOrder = taskTemplateCheck.order

        _taskTemplateChecks.find { it.order == currentOrder - 1 }?.order = currentOrder
        taskTemplateCheck.order = currentOrder - 1
    }

    fun orderDown(taskTemplateCheck: TaskTemplateStateCheck) {
        val currentOrder = taskTemplateCheck.order

        _taskTemplateChecks.find { it.order == currentOrder + 1 }?.order = currentOrder
        taskTemplateCheck.order = currentOrder + 1
    }

    private fun toTaskTemplateDto(): TaskTemplateDto {
        return TaskTemplateDto(
            id = _taskTemplateId.value,
            header = taskTemplateName,
            description = taskTemplateDescription,
            taskTemplateChecks = taskTemplateChecks.map {
                TaskTemplateCheckDto(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    requiredPhoto = it.requiredPhoto,
                    requiredComment = it.requiredComment,
                    requiredControlValue = it.requiredControlValue,
                    taskTemplateCheckOrder = it.order,
                    controlValueType = it.controlValueType
                )
            }.toMutableList()
        )
    }

    class TaskTemplateStateCheck {
        private val _id: MutableState<String?> = mutableStateOf(null)
        private val _name: MutableState<String> = mutableStateOf("")
        private val _description: MutableState<String> = mutableStateOf("")
        private val _requiredPhoto: MutableState<Boolean> = mutableStateOf(false)
        private val _requiredComment: MutableState<Boolean> = mutableStateOf(false)
        private val _requiredControlValue: MutableState<Boolean> = mutableStateOf(false)
        private val _order: MutableState<Int> = mutableStateOf(-1)
        private val _controlValueType: MutableState<TaskTemplateCheckDto.PermissionControlValueType?> = mutableStateOf(null)

        var id
            get() = _id.value
            set(value) { _id.value = value }

        var name
            get() = _name.value
            set(value) { _name.value = value }

        var description
            get() = _description.value
            set(value) { _description.value = value }

        var requiredPhoto
            get() = _requiredPhoto.value
            set(value) { _requiredPhoto.value = value }

        var requiredComment
            get() = _requiredComment.value
            set(value) { _requiredComment.value = value }

        var requiredControlValue
            get() = _requiredControlValue.value
            set(value) { _requiredControlValue.value = value }

        var order
            get() = _order.value
            set(value) { _order.value = value }

        var controlValueType
            get() = _controlValueType.value
            set(value) { _controlValueType.value = value }
    }
}