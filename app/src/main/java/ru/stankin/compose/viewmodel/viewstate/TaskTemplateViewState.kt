package ru.stankin.compose.viewmodel.viewstate

import ru.stankin.compose.model.EquipmentDto
import ru.stankin.compose.model.TaskTemplateCheckDto
import ru.stankin.compose.model.TaskTemplateDto

sealed class TaskTemplateViewState {

    object Empty: TaskTemplateViewState()

    data class Initialized(
        val id: String = "",
        val taskTemplateName: String = "",
        val taskTemplateDescription: String = "",
        val equipment: EquipmentDto = EquipmentDto(),
        val status: TaskTemplateDto.TaskTemplateDtoStatus = TaskTemplateDto.TaskTemplateDtoStatus.DRAFT,
        val taskTemplateChecks: List<TaskTemplateCheckDto> = emptyList(),

        val equipmentsSearch: Map<String, String> = emptyMap()
    ): TaskTemplateViewState()
}