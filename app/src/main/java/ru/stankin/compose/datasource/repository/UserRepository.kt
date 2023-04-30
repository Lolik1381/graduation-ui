package ru.stankin.compose.datasource.repository

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.stankin.compose.model.CommonResponse
import ru.stankin.compose.model.RequestLoginDto

interface UserRepository {

    @POST("/user/v1/login")
    suspend fun login(@Body loginDto: RequestLoginDto): Response<CommonResponse<String>>

    @GET("/user/v1/roles")
    suspend fun userRoles(): Response<CommonResponse<List<String>>>
}