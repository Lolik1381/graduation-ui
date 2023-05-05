package ru.stankin.compose.viewmodel.viewstate

import ru.stankin.compose.model.TaskMetadataDto

sealed class UserTaskListViewState {

    object Empty: UserTaskListViewState()

    data class Initialized(
        val tasksMetadata: List<TaskMetadataDto> = emptyList(),
        val searchString: String = "",
        val equipmentId: String = ""
    ): UserTaskListViewState()
}