package ru.stankin.compose.datasource.repository

import retrofit2.Response
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import ru.stankin.compose.model.CommonResponse
import ru.stankin.compose.model.TaskCheckDto

interface UserTaskCheckRepository {

    @PUT("/user/v1/taskCheck/{id}")
    suspend fun update(
        @Path("id") id: String,
        @Query("comment") comment: String?,
        @Query("controlValue") controlValue: String?,
        @Query("photoIds") photoIds: List<String>?
    ): Response<CommonResponse<TaskCheckDto>>
}