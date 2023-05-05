package ru.stankin.compose.viewmodel.event

import ru.stankin.compose.model.EquipmentDto
import ru.stankin.compose.model.TaskTemplateCheckDto

sealed class TaskTemplateEvent {

    data class LoadingComplete(val taskTemplateId: String?): TaskTemplateEvent()
    data class Reload(val taskTemplateId: String?): TaskTemplateEvent()
    object Clear: TaskTemplateEvent()

    data class TaskTemplateEquipmentSearch(val name: String): TaskTemplateEvent()
    data class TaskTemplateEquipmentClick(val equipment: EquipmentDto): TaskTemplateEvent()

    data class TaskTemplateNameChange(val name: String): TaskTemplateEvent()
    data class TaskTemplateDescriptionChange(val description: String): TaskTemplateEvent()

    object TaskTemplateCheckAdd: TaskTemplateEvent()
    data class TaskTemplateCheckDelete(val taskTemplateCheck: TaskTemplateCheckDto): TaskTemplateEvent()
    data class TaskTemplateCheckUp(val taskTemplateCheck: TaskTemplateCheckDto): TaskTemplateEvent()
    data class TaskTemplateCheckDown(val taskTemplateCheck: TaskTemplateCheckDto): TaskTemplateEvent()

    data class TaskTemplateCheckNameChange(val taskTemplateCheck: TaskTemplateCheckDto, val name: String): TaskTemplateEvent()
    data class TaskTemplateCheckDescriptionChange(val taskTemplateCheck: TaskTemplateCheckDto, val description: String): TaskTemplateEvent()
    data class TaskTemplateCheckRequiredPhotoChange(val taskTemplateCheck: TaskTemplateCheckDto, val requiredPhoto: Boolean): TaskTemplateEvent()
    data class TaskTemplateCheckRequiredCommentChange(val taskTemplateCheck: TaskTemplateCheckDto, val requiredComment: Boolean): TaskTemplateEvent()
    data class TaskTemplateCheckRequiredControlValueChange(val taskTemplateCheck: TaskTemplateCheckDto, val requiredControlValue: Boolean): TaskTemplateEvent()
    data class TaskTemplateCheckPermissionControlValueTypeChange(val taskTemplateCheck: TaskTemplateCheckDto, val permissionControlValueType: TaskTemplateCheckDto.PermissionControlValueType): TaskTemplateEvent()

    object SavedClick: TaskTemplateEvent()
    object UpdateClick: TaskTemplateEvent()
    object ActivateClick: TaskTemplateEvent()
    object Completed: TaskTemplateEvent()

    data class NavigateToTaskCreate(val taskTemplateId: String): TaskTemplateEvent()
    data class NavigateToTaskSchedulerCreate(val taskTemplateId: String): TaskTemplateEvent()
}