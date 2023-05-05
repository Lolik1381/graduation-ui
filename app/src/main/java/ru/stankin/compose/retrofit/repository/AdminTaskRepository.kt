package ru.stankin.compose.retrofit.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import ru.stankin.compose.model.CommonResponse
import ru.stankin.compose.model.TaskDto
import ru.stankin.compose.retrofit.APIs
import ru.stankin.compose.retrofit.network.AdminTaskApi

class AdminTaskRepository(
    private val adminTaskApi: AdminTaskApi = APIs.adminTaskApi
) {

    fun createTask(taskDto: TaskDto): Flow<Response<CommonResponse<TaskDto>>> = flow {
        emit(adminTaskApi.createTask(taskDto))
    }.flowOn(Dispatchers.IO)
}