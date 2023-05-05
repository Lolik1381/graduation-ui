package ru.stankin.compose.viewmodel

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ru.stankin.compose.core.util.content
import ru.stankin.compose.core.util.nonNullContent
import ru.stankin.compose.core.util.onFailure
import ru.stankin.compose.core.util.onSuccess
import ru.stankin.compose.model.TaskTemplateCheckDto
import ru.stankin.compose.model.TaskTemplateDto
import ru.stankin.compose.presentation.tasktemplate.admin.screen.immutableTaskTemplate
import ru.stankin.compose.presentation.tasktemplate.admin.screen.immutableTaskTemplateChecks
import ru.stankin.compose.presentation.tasktemplate.admin.screen.mutableTaskTemplate
import ru.stankin.compose.presentation.tasktemplate.admin.screen.mutableTaskTemplateChecks
import ru.stankin.compose.presentation.tasktemplate.admin.screen.taskTemplateActive
import ru.stankin.compose.presentation.tasktemplate.admin.screen.taskTemplateCreate
import ru.stankin.compose.presentation.tasktemplate.admin.screen.taskTemplateUpdate
import ru.stankin.compose.retrofit.repository.AdminEquipmentRepository
import ru.stankin.compose.retrofit.repository.AdminTaskTemplateRepository
import ru.stankin.compose.viewmodel.base.EventHandler
import ru.stankin.compose.viewmodel.base.ViewModelFactory
import ru.stankin.compose.viewmodel.event.TaskTemplateEvent
import ru.stankin.compose.viewmodel.state.TaskTemplateState
import ru.stankin.compose.viewmodel.viewstate.TaskTemplateViewState
import java.util.*

class TaskTemplateViewModel(
    private val adminTaskTemplateRepository: AdminTaskTemplateRepository = AdminTaskTemplateRepository(),
    private val adminEquipmentRepository: AdminEquipmentRepository = AdminEquipmentRepository()
) : ViewModel(), EventHandler<TaskTemplateEvent>, ViewModelFactory<List<LazyListScope.() -> Unit>> {

    private val _state = mutableStateOf<TaskTemplateState>(TaskTemplateState.Loading)
    private val _viewState = mutableStateOf<TaskTemplateViewState>(TaskTemplateViewState.Empty)
    val state by _state
    val viewState by _viewState

    override fun obtainEvent(event: TaskTemplateEvent) {
        when (event) {
            is TaskTemplateEvent.Completed -> _state.value = TaskTemplateState.Completed
            is TaskTemplateEvent.LoadingComplete -> loading(event.taskTemplateId)
            is TaskTemplateEvent.Reload -> obtainEvent(TaskTemplateEvent.LoadingComplete(event.taskTemplateId))
            is TaskTemplateEvent.Clear -> clear(viewState as TaskTemplateViewState.Initialized)

            is TaskTemplateEvent.NavigateToTaskCreate -> _state.value = TaskTemplateState.NavigateToTaskCreate(event.taskTemplateId)
            is TaskTemplateEvent.NavigateToTaskSchedulerCreate -> _state.value = TaskTemplateState.NavigateToTaskSchedulerCreate(event.taskTemplateId)

            is TaskTemplateEvent.TaskTemplateEquipmentSearch -> equipmentSearch(event.name, viewState as TaskTemplateViewState.Initialized)
            is TaskTemplateEvent.TaskTemplateEquipmentClick -> processingViewState(event, viewState as TaskTemplateViewState.Initialized)

            is TaskTemplateEvent.TaskTemplateNameChange,
            is TaskTemplateEvent.TaskTemplateDescriptionChange -> processingViewState(event, viewState as TaskTemplateViewState.Initialized)

            is TaskTemplateEvent.TaskTemplateCheckAdd,
            is TaskTemplateEvent.TaskTemplateCheckDelete,
            is TaskTemplateEvent.TaskTemplateCheckUp,
            is TaskTemplateEvent.TaskTemplateCheckDown,
            is TaskTemplateEvent.TaskTemplateCheckNameChange,
            is TaskTemplateEvent.TaskTemplateCheckDescriptionChange,
            is TaskTemplateEvent.TaskTemplateCheckRequiredControlValueChange,
            is TaskTemplateEvent.TaskTemplateCheckRequiredPhotoChange,
            is TaskTemplateEvent.TaskTemplateCheckRequiredCommentChange,
            is TaskTemplateEvent.TaskTemplateCheckPermissionControlValueTypeChange -> processingTaskTemplateCheck(event, viewState as TaskTemplateViewState.Initialized)

            is TaskTemplateEvent.SavedClick -> saved(viewState as TaskTemplateViewState.Initialized)
            is TaskTemplateEvent.UpdateClick -> update(viewState as TaskTemplateViewState.Initialized)
            is TaskTemplateEvent.ActivateClick -> activate(viewState as TaskTemplateViewState.Initialized)
        }
    }

    override fun make(): List<LazyListScope.() -> Unit> {
        val listScopes: MutableList<LazyListScope.() -> Unit> = mutableListOf()

        if (viewState !is TaskTemplateViewState.Initialized) {
            return listScopes
        }

        val initializedViewState = viewState as TaskTemplateViewState.Initialized
        when {
            initializedViewState.id.isBlank() -> {
                listScopes.add { mutableTaskTemplate(this@TaskTemplateViewModel, initializedViewState) }
                listScopes.add { mutableTaskTemplateChecks(this@TaskTemplateViewModel, initializedViewState) }
                listScopes.add {
                    taskTemplateCreate(
                        viewModel = this@TaskTemplateViewModel,
                        saveButtonContent = {
                            when (state) {
                                is TaskTemplateState.Loaded -> {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = "Сохранить",
                                        tint = Color.Black
                                    )
                                }
                                is TaskTemplateState.SavedProcess -> {
                                    CircularProgressIndicator()
                                }
                                else -> {}
                            }
                        }
                    )
                }
            }
            initializedViewState.id.isNotBlank() && initializedViewState.status == TaskTemplateDto.TaskTemplateDtoStatus.DRAFT -> {
                listScopes.add { mutableTaskTemplate(this@TaskTemplateViewModel, initializedViewState) }
                listScopes.add { mutableTaskTemplateChecks(this@TaskTemplateViewModel, initializedViewState) }
                listScopes.add {
                    taskTemplateUpdate(
                        viewModel = this@TaskTemplateViewModel,
                        saveButtonContent = {
                            when (state) {
                                is TaskTemplateState.Loaded -> {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = "Сохранить",
                                        tint = Color.Black
                                    )
                                }
                                is TaskTemplateState.SavedProcess -> {
                                    CircularProgressIndicator()
                                }
                                else -> {}
                            }
                        },
                        activateButtonContent = {
                            when (state) {
                                is TaskTemplateState.Loaded -> {
                                    Icon(
                                        imageVector = Icons.Filled.CheckCircleOutline,
                                        contentDescription = "Активировать",
                                        tint = Color.Black
                                    )
                                }
                                is TaskTemplateState.SavedProcess -> {
                                    CircularProgressIndicator()
                                }
                                else -> {}
                            }
                        }
                    )
                }
            }
            initializedViewState.id.isNotBlank() && initializedViewState.status == TaskTemplateDto.TaskTemplateDtoStatus.ACTIVE -> {
                listScopes.add { immutableTaskTemplate(viewModel = this@TaskTemplateViewModel, viewState = initializedViewState, enabledDeleted = true) }
                listScopes.add { immutableTaskTemplateChecks(viewModel = this@TaskTemplateViewModel, viewState = initializedViewState) }
                listScopes.add { taskTemplateActive(viewModel = this@TaskTemplateViewModel, viewState = initializedViewState) }
            }
            initializedViewState.id.isNotBlank() && initializedViewState.status == TaskTemplateDto.TaskTemplateDtoStatus.DELETED -> {
                listScopes.add { immutableTaskTemplate(viewModel = this@TaskTemplateViewModel, viewState = initializedViewState, enabledDeleted = false) }
                listScopes.add { immutableTaskTemplateChecks(viewModel = this@TaskTemplateViewModel, viewState = initializedViewState) }
            }
        }

        return listScopes
    }

    private fun loading(taskTemplateId: String?) {
        if (!taskTemplateId.isNullOrBlank()) {
             viewModelScope.launch {
                 adminTaskTemplateRepository.findById(taskTemplateId)
                     .onStart { _state.value = TaskTemplateState.Loading }
                     .catch { _state.value = TaskTemplateState.Error(it.message.orEmpty()) }
                     .collect { response ->
                         response
                             .onSuccess { commonResponse ->
                                 val taskTemplateDto = commonResponse.nonNullContent()

                                 _state.value = TaskTemplateState.Loaded
                                 _viewState.value = TaskTemplateViewState.Initialized(
                                     id = taskTemplateDto.id,
                                     taskTemplateName = taskTemplateDto.header,
                                     taskTemplateDescription = taskTemplateDto.description,
                                     equipment = taskTemplateDto.equipment,
                                     status = taskTemplateDto.status,
                                     taskTemplateChecks = taskTemplateDto.taskTemplateChecks.sortedBy { it.taskTemplateCheckOrder }
                                 )
                             }
                             .onFailure { _state.value = TaskTemplateState.Error(it.body()?.message.orEmpty()) }
                     }
             }
        } else {
            _state.value = TaskTemplateState.Loaded
            _viewState.value = TaskTemplateViewState.Initialized()
        }
    }

    private fun clear(currentViewState: TaskTemplateViewState.Initialized) {
        if (currentViewState.id.isNotBlank()) {
            viewModelScope.launch {
                adminTaskTemplateRepository.deleteTaskTemplate(currentViewState.id)
                    .onStart { _state.value = TaskTemplateState.SavedProcess }
                    .catch { _state.value = TaskTemplateState.Error(it.message.orEmpty()) }
                    .collect { response ->
                        response
                            .onSuccess { _state.value = TaskTemplateState.Success }
                            .onFailure { _state.value = TaskTemplateState.Error(response.body()?.message.orEmpty()) }
                    }
            }
        } else {
            _viewState.value = TaskTemplateViewState.Initialized()
        }
    }

    private fun equipmentSearch(name: String, viewState: TaskTemplateViewState.Initialized) {
        viewModelScope.launch {
            val searchText = name.takeIf { it.isNotBlank() }

            adminEquipmentRepository.findAll(searchText = searchText, page = 0, size = 5)
                .catch { _state.value = TaskTemplateState.Error(it.message.orEmpty()) }
                .collect { response ->
                    response
                        .onSuccess {
                            val equipmentsSearch = response.content()
                                ?.content
                                ?.sortedBy { it.name }
                                ?.associate { it.id to it.name }.orEmpty()

                            _viewState.value = viewState.copy(equipmentsSearch = equipmentsSearch)
                        }
                        .onFailure { _state.value = TaskTemplateState.Error(response.body()?.message.orEmpty()) }
                }
        }
    }

    private fun saved(currentViewState: TaskTemplateViewState.Initialized) {
        val taskTemplateDto = TaskTemplateDto(
            id = currentViewState.id,
            header = currentViewState.taskTemplateName,
            description = currentViewState.taskTemplateDescription,
            equipment = currentViewState.equipment,
            taskTemplateChecks = currentViewState.taskTemplateChecks.toMutableList()
        )

        viewModelScope.launch {
            adminTaskTemplateRepository.createTaskTemplate(taskTemplateDto)
                .onStart { _state.value = TaskTemplateState.SavedProcess }
                .catch { _state.value = TaskTemplateState.Error(it.message.orEmpty()) }
                .collect { response ->
                    response
                        .onSuccess { _state.value = TaskTemplateState.Success }
                        .onFailure { _state.value = TaskTemplateState.Error(response.body()?.message.orEmpty()) }
                }
        }
    }

    private fun update(currentViewState: TaskTemplateViewState.Initialized) {
        val taskTemplateDto = TaskTemplateDto(
            id = currentViewState.id,
            header = currentViewState.taskTemplateName,
            description = currentViewState.taskTemplateDescription,
            equipment = currentViewState.equipment,
            taskTemplateChecks = currentViewState.taskTemplateChecks.toMutableList()
        )

        viewModelScope.launch {
            adminTaskTemplateRepository.updateTaskTemplate(currentViewState.id, taskTemplateDto)
                .onStart { _state.value = TaskTemplateState.SavedProcess }
                .catch { _state.value = TaskTemplateState.Error(it.message.orEmpty()) }
                .collect { response ->
                    response
                        .onSuccess { _state.value = TaskTemplateState.Success }
                        .onFailure { _state.value = TaskTemplateState.Error(response.body()?.message.orEmpty()) }
                }
        }
    }

    private fun activate(currentViewState: TaskTemplateViewState.Initialized) {
        viewModelScope.launch {
            adminTaskTemplateRepository.activateTaskTemplate(currentViewState.id)
                .onStart { _state.value = TaskTemplateState.SavedProcess }
                .catch { _state.value = TaskTemplateState.Error(it.message.orEmpty()) }
                .collect { response ->
                    response
                        .onSuccess { _state.value = TaskTemplateState.Success }
                        .onFailure { _state.value = TaskTemplateState.Error(response.body()?.message.orEmpty()) }
                }
        }
    }

    private fun processingViewState(event: TaskTemplateEvent, currentViewState: TaskTemplateViewState.Initialized) {
        when (event) {
            is TaskTemplateEvent.TaskTemplateNameChange -> _viewState.value = currentViewState.copy(taskTemplateName = event.name)
            is TaskTemplateEvent.TaskTemplateDescriptionChange -> _viewState.value = currentViewState.copy(taskTemplateDescription = event.description)
            is TaskTemplateEvent.TaskTemplateEquipmentClick -> _viewState.value = currentViewState.copy(equipment = event.equipment)
            else -> {}
        }
    }

    private fun processingTaskTemplateCheck(event: TaskTemplateEvent, currentViewState: TaskTemplateViewState.Initialized) {
        when (event) {
            is TaskTemplateEvent.TaskTemplateCheckAdd -> addTaskTemplateCheck(currentViewState)
            is TaskTemplateEvent.TaskTemplateCheckDelete -> deleteTaskTemplateCheck(event, currentViewState)
            is TaskTemplateEvent.TaskTemplateCheckUp -> upTaskTemplateCheck(event, currentViewState)
            is TaskTemplateEvent.TaskTemplateCheckDown -> downTaskTemplateCheck(event, currentViewState)
            is TaskTemplateEvent.TaskTemplateCheckNameChange -> changeNameTaskTemplateCheck(event, currentViewState)
            is TaskTemplateEvent.TaskTemplateCheckDescriptionChange -> changeDescriptionTaskTemplateCheck(event, currentViewState)
            is TaskTemplateEvent.TaskTemplateCheckRequiredPhotoChange -> changeRequiredPhotoTaskTemplateCheck(event, currentViewState)
            is TaskTemplateEvent.TaskTemplateCheckRequiredCommentChange -> changeRequiredCommentTaskTemplateCheck(event, currentViewState)
            is TaskTemplateEvent.TaskTemplateCheckRequiredControlValueChange -> changeRequiredControlValueTaskTemplateCheck(event, currentViewState)
            is TaskTemplateEvent.TaskTemplateCheckPermissionControlValueTypeChange -> changePermissionControlValueTypeTaskTemplateCheck(event, currentViewState)
            else -> {}
        }
    }

    private fun addTaskTemplateCheck(currentViewState: TaskTemplateViewState.Initialized) {
        val taskTemplateCheck = TaskTemplateCheckDto(
            id = UUID.randomUUID().toString(),
            taskTemplateCheckOrder = currentViewState.taskTemplateChecks.size + 1
        )

        val taskTemplateChecks = currentViewState.taskTemplateChecks
            .toMutableList()
            .apply { add(taskTemplateCheck) }
        _viewState.value = currentViewState.copy(taskTemplateChecks = taskTemplateChecks)
    }

    private fun deleteTaskTemplateCheck(event: TaskTemplateEvent.TaskTemplateCheckDelete, currentViewState: TaskTemplateViewState.Initialized) {
        val taskTemplateChecks = currentViewState.taskTemplateChecks.toMutableList().apply { remove(event.taskTemplateCheck) }
        taskTemplateChecks.forEachIndexed { index, taskTemplateCheckDto -> taskTemplateCheckDto.taskTemplateCheckOrder = index + 1 }

        _viewState.value = currentViewState.copy(taskTemplateChecks = taskTemplateChecks.sortedBy { it.taskTemplateCheckOrder })
    }

    private fun upTaskTemplateCheck(event: TaskTemplateEvent.TaskTemplateCheckUp, currentViewState: TaskTemplateViewState.Initialized) {
        val currentOrder = event.taskTemplateCheck.taskTemplateCheckOrder

        val prevCheck = currentViewState.taskTemplateChecks.find { it.taskTemplateCheckOrder == currentOrder - 1 }?.copy(taskTemplateCheckOrder = currentOrder)
        var currentCheck = event.taskTemplateCheck
        if (prevCheck != null) {
            currentCheck = event.taskTemplateCheck.copy(taskTemplateCheckOrder = currentOrder - 1)
        }

        val taskTemplateChecks = currentViewState.taskTemplateChecks.toMutableList().apply {
            removeIf { it.id == prevCheck?.id }
            removeIf { it.id == currentCheck.id }

            prevCheck?.let { add(it) }
            add(currentCheck)
        }.sortedBy { it.taskTemplateCheckOrder }

        _viewState.value = currentViewState.copy(taskTemplateChecks = taskTemplateChecks)
    }

    private fun downTaskTemplateCheck(event: TaskTemplateEvent.TaskTemplateCheckDown, currentViewState: TaskTemplateViewState.Initialized) {
        val currentOrder = event.taskTemplateCheck.taskTemplateCheckOrder

        val prevCheck = currentViewState.taskTemplateChecks.find { it.taskTemplateCheckOrder == currentOrder + 1 }?.copy(taskTemplateCheckOrder = currentOrder)
        var currentCheck = event.taskTemplateCheck
        if (prevCheck != null) {
            currentCheck = event.taskTemplateCheck.copy(taskTemplateCheckOrder = currentOrder + 1)
        }

        val taskTemplateChecks = currentViewState.taskTemplateChecks.toMutableList().apply {
            removeIf { it.id == prevCheck?.id }
            removeIf { it.id == currentCheck.id }

            prevCheck?.let { add(it) }
            add(currentCheck)
        }.sortedBy { it.taskTemplateCheckOrder }

        _viewState.value = currentViewState.copy(taskTemplateChecks = taskTemplateChecks)
    }

    private fun changeNameTaskTemplateCheck(event: TaskTemplateEvent.TaskTemplateCheckNameChange, currentViewState: TaskTemplateViewState.Initialized) {
        val taskTemplateChecks = currentViewState.taskTemplateChecks.toMutableList()
            .apply {
                remove(event.taskTemplateCheck)
                add(event.taskTemplateCheck.copy(name = event.name))
            }.sortedBy { it.taskTemplateCheckOrder }

        _viewState.value = currentViewState.copy(taskTemplateChecks = taskTemplateChecks)
    }

    private fun changeDescriptionTaskTemplateCheck(event: TaskTemplateEvent.TaskTemplateCheckDescriptionChange, currentViewState: TaskTemplateViewState.Initialized) {
        val taskTemplateChecks = currentViewState.taskTemplateChecks.toMutableList()
            .apply {
                remove(event.taskTemplateCheck)
                add(event.taskTemplateCheck.copy(description = event.description))
            }.sortedBy { it.taskTemplateCheckOrder }

        _viewState.value = currentViewState.copy(taskTemplateChecks = taskTemplateChecks)
    }

    private fun changeRequiredPhotoTaskTemplateCheck(event: TaskTemplateEvent.TaskTemplateCheckRequiredPhotoChange, currentViewState: TaskTemplateViewState.Initialized) {
        val taskTemplateChecks = currentViewState.taskTemplateChecks.toMutableList()
            .apply {
                remove(event.taskTemplateCheck)
                add(event.taskTemplateCheck.copy(requiredPhoto = event.requiredPhoto))
            }.sortedBy { it.taskTemplateCheckOrder }

        _viewState.value = currentViewState.copy(taskTemplateChecks = taskTemplateChecks)
    }

    private fun changeRequiredControlValueTaskTemplateCheck(event: TaskTemplateEvent.TaskTemplateCheckRequiredControlValueChange, currentViewState: TaskTemplateViewState.Initialized) {
        val taskTemplateChecks = currentViewState.taskTemplateChecks.toMutableList()
            .apply {
                remove(event.taskTemplateCheck)
                add(event.taskTemplateCheck.copy(requiredControlValue = event.requiredControlValue))
            }.sortedBy { it.taskTemplateCheckOrder }

        _viewState.value = currentViewState.copy(taskTemplateChecks = taskTemplateChecks)
    }

    private fun changeRequiredCommentTaskTemplateCheck(event: TaskTemplateEvent.TaskTemplateCheckRequiredCommentChange, currentViewState: TaskTemplateViewState.Initialized) {
        val taskTemplateChecks = currentViewState.taskTemplateChecks.toMutableList()
            .apply {
                remove(event.taskTemplateCheck)
                add(event.taskTemplateCheck.copy(requiredComment = event.requiredComment))
            }.sortedBy { it.taskTemplateCheckOrder }

        _viewState.value = currentViewState.copy(taskTemplateChecks = taskTemplateChecks)
    }

    private fun changePermissionControlValueTypeTaskTemplateCheck(event: TaskTemplateEvent.TaskTemplateCheckPermissionControlValueTypeChange, currentViewState: TaskTemplateViewState.Initialized) {
        val taskTemplateChecks = currentViewState.taskTemplateChecks.toMutableList()
            .apply {
                remove(event.taskTemplateCheck)
                add(event.taskTemplateCheck.copy(controlValueType = event.permissionControlValueType))
            }.sortedBy { it.taskTemplateCheckOrder }

        _viewState.value = currentViewState.copy(taskTemplateChecks = taskTemplateChecks)
    }
}