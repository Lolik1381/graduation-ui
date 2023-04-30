package ru.stankin.compose.datasource.repository

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import ru.stankin.compose.model.CommonResponse
import ru.stankin.compose.model.TaskDto

interface AdminTaskRepository {

    @Headers("Content-Type: application/json")
    @POST("/admin/v1/task/create")
    suspend fun createTask(@Body taskDto: TaskDto): Response<CommonResponse<TaskDto>>
}