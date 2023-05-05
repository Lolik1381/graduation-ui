package ru.stankin.compose.retrofit.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.stankin.compose.model.CommonResponse
import ru.stankin.compose.model.TaskDto
import ru.stankin.compose.model.TaskMetadataDto

interface TaskApi {

    @GET("/user/v1/task/findall")
    suspend fun findTasks(
        @Query("status") status: TaskDto.TaskStatusDto? = null,
        @Query("searchText") searchText: String? = null,
        @Query("equipmentId") equipmentId: String? = null
    ): Response<CommonResponse<List<TaskMetadataDto>>>

    @GET("/user/v1/task/find/{taskId}")
    suspend fun findTaskById(@Path("taskId") taskId: String): Response<CommonResponse<TaskDto>>
}