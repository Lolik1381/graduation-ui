package ru.stankin.compose.datasource.repository

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import ru.stankin.compose.model.CommonResponse
import ru.stankin.compose.model.FileDto

interface UserFileRepository {

    @Multipart
    @POST("/user/v1/file/multiple-create")
    suspend fun saveFiles(@Part files: List<MultipartBody.Part>): Response<CommonResponse<List<FileDto>>>
}