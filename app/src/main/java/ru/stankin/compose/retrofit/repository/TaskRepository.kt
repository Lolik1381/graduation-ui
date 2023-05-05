package ru.stankin.compose.retrofit.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import ru.stankin.compose.model.CommonResponse
import ru.stankin.compose.model.TaskDto
import ru.stankin.compose.model.TaskMetadataDto
import ru.stankin.compose.retrofit.APIs
import ru.stankin.compose.retrofit.network.TaskApi

class TaskRepository(
    private val taskApi: TaskApi = APIs.taskApi
) {

    fun findTasks(
        status: TaskDto.TaskStatusDto? = null,
        searchText: String? = null,
        equipmentId: String? = null
    ): Flow<Response<CommonResponse<List<TaskMetadataDto>>>> = flow {
        emit(taskApi.findTasks(status, searchText, equipmentId))
    }.flowOn(Dispatchers.IO)
}