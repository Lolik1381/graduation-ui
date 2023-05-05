package ru.stankin.compose.viewmodel.viewstate

import ru.stankin.compose.model.TaskTemplateDto

sealed class TaskTemplateListViewState {

    object Empty: TaskTemplateListViewState()

    data class Initialized(
        val taskTemplatesDto: List<TaskTemplateDto> = emptyList()
    ): TaskTemplateListViewState()
}