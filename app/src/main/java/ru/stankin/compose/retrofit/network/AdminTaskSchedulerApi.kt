package ru.stankin.compose.retrofit.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import ru.stankin.compose.model.CommonResponse
import ru.stankin.compose.model.TaskSchedulerDto

interface AdminTaskSchedulerApi {

    @Headers("Content-Type: application/json")
    @POST("/admin/v1/task/scheduler/create")
    suspend fun createTaskTemplate(@Body taskSchedulerDto: TaskSchedulerDto): Response<CommonResponse<TaskSchedulerDto>>
}