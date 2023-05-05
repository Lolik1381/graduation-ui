package ru.stankin.compose.retrofit.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import ru.stankin.compose.model.CommonResponse
import ru.stankin.compose.model.Page
import ru.stankin.compose.model.TaskTemplateDto
import ru.stankin.compose.retrofit.APIs
import ru.stankin.compose.retrofit.network.AdminTaskTemplateApi

class AdminTaskTemplateRepository(
    private val adminTaskTemplateApi: AdminTaskTemplateApi = APIs.adminTaskTemplateApi
) {

    fun createTaskTemplate(taskTemplateDto: TaskTemplateDto): Flow<Response<CommonResponse<TaskTemplateDto>>> = flow {
        emit(adminTaskTemplateApi.createTaskTemplate(taskTemplateDto))
    }.flowOn(Dispatchers.IO)

    fun findAll(searchText: String? = null, page: Int = 0, size: Int = 20): Flow<Response<CommonResponse<Page<TaskTemplateDto>>>> = flow {
        emit(adminTaskTemplateApi.findAll(searchText, page, size))
    }.flowOn(Dispatchers.IO)

    fun findById(id: String): Flow<Response<CommonResponse<TaskTemplateDto>>> = flow {
        emit(adminTaskTemplateApi.findById(id))
    }.flowOn(Dispatchers.IO)

    fun activateTaskTemplate(id: String): Flow<Response<CommonResponse<TaskTemplateDto>>> = flow {
        emit(adminTaskTemplateApi.activateTaskTemplate(id))
    }.flowOn(Dispatchers.IO)

    fun updateTaskTemplate(id: String, taskTemplateDto: TaskTemplateDto): Flow<Response<CommonResponse<TaskTemplateDto>>> = flow {
        emit(adminTaskTemplateApi.updateTaskTemplate(id, taskTemplateDto))
    }.flowOn(Dispatchers.IO)

    fun deleteTaskTemplate(id: String): Flow<Response<CommonResponse<TaskTemplateDto>>> = flow {
        emit(adminTaskTemplateApi.deleteTaskTemplate(id))
    }.flowOn(Dispatchers.IO)
}