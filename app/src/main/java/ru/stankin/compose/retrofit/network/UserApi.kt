package ru.stankin.compose.retrofit.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import ru.stankin.compose.model.CommonResponse
import ru.stankin.compose.model.RequestChangePasswordDto
import ru.stankin.compose.model.RequestLoginDto
import ru.stankin.compose.model.ResponseLoginDto

interface UserApi {

    @POST("/user/v1/login")
    suspend fun login(@Body loginDto: RequestLoginDto): Response<CommonResponse<ResponseLoginDto>>

    @PUT("/user/v1/changePassword")
    suspend fun changePassword(@Body changePasswordDto: RequestChangePasswordDto): Response<CommonResponse<Unit>>
}