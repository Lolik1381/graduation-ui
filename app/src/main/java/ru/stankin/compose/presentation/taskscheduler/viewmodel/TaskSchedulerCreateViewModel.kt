package ru.stankin.compose.presentation.taskscheduler.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import ru.stankin.compose.core.ext.navigate
import ru.stankin.compose.core.util.Route
import ru.stankin.compose.core.util.content
import ru.stankin.compose.core.util.errorMessage
import ru.stankin.compose.core.util.onFailure
import ru.stankin.compose.core.util.onSuccess
import ru.stankin.compose.retrofit.Repositories
import ru.stankin.compose.model.TaskSchedulerDto
import ru.stankin.compose.model.TaskTemplateDto

class TaskSchedulerCreateViewModel : ViewModel() {

    private lateinit var _navController: NavController

    private val _taskScheduler = mutableStateOf(TaskSchedulerDto())
    private val _days = mutableStateOf(1L)
    private val _minutes = mutableStateOf(0L)

    private val _isError = mutableStateOf(false)
    private val _errorMessage = mutableStateOf<String?>(null)

    private val _adminTaskRepository = Repositories.adminTaskTemplateApi
    private val _adminTaskSchedulerRepository = Repositories.adminTaskSchedulerApi

    val taskScheduler
        get() = _taskScheduler.value

    var days
        get() = _days.value
        set(value) {
            _days.value = value
        }

    var minutes
        get() = _minutes.value
        set(value) {
            _minutes.value = value
        }

    val isError
        get() = _isError.value

    val errorMessage
        get() = _errorMessage.value

    fun init(
        navController: NavController,
        taskTemplateId: String? = null
    ) {
        _navController = navController
        taskScheduler.taskTemplate = TaskTemplateDto()

        viewModelScope.launch {
            taskTemplateId?.let { taskTemplateId ->
                _adminTaskRepository.findById(taskTemplateId)
                    .onSuccess { taskScheduler.taskTemplate = it.content()}
                    .onFailure {
                        _isError.value = true
                        _errorMessage.value = it.errorMessage()
                    }
            }
        }
    }

    fun create() {
        taskScheduler.expireDelay = (days * 24 * 60 * 60 * 1000) + (minutes * 60 * 1000)

        viewModelScope.launch {
            _adminTaskSchedulerRepository.createTaskTemplate(taskSchedulerDto = taskScheduler)
                .onSuccess { _navController.navigate(Route.TASK_TEMPLATE) }
                .onFailure {
                    _isError.value = true
                    _errorMessage.value = it.errorMessage()
                }
        }
    }
}