package ru.stankin.compose.viewmodel.event

sealed class TaskTemplateListEvent {

    object LoadingComplete: TaskTemplateListEvent()
    object Reload: TaskTemplateListEvent()
    data class TaskTemplateListCardClick(val taskTemplateId: String): TaskTemplateListEvent()
    object Completed: TaskTemplateListEvent()
}