package ru.stankin.compose.datasource.repository

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import ru.stankin.compose.model.CommonResponse
import ru.stankin.compose.model.Page
import ru.stankin.compose.model.TaskTemplateDto

interface AdminTaskTemplateRepository {

    @Headers("Content-Type: application/json")
    @POST("/admin/v1/task/template/create")
    suspend fun createTaskTemplate(@Body taskTemplateDto: TaskTemplateDto): Response<CommonResponse<TaskTemplateDto>>

    @GET("/admin/v1/task/template/findAll")
    suspend fun findAll(
        @Query("searchText") searchText: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<CommonResponse<Page<TaskTemplateDto>>>

    @GET("/admin/v1/task/template/find/{id}")
    suspend fun findById(@Path("id") id: String): Response<CommonResponse<TaskTemplateDto>>

    @PUT("/admin/v1/task/template/update/{id}")
    suspend fun updateTaskTemplate(@Path("id") id: String, @Body taskTemplateDto: TaskTemplateDto): Response<CommonResponse<TaskTemplateDto>>

    @PUT("/admin/v1/task/template/delete/{id}")
    suspend fun deleteTaskTemplate(@Path("id") id: String): Response<CommonResponse<TaskTemplateDto>>

    @PUT("/admin/v1/task/template/active/{id}")
    suspend fun activateTaskTemplate(@Path("id") id: String): Response<CommonResponse<TaskTemplateDto>>
}