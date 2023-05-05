package ru.stankin.compose.retrofit.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.stankin.compose.model.CommonResponse
import ru.stankin.compose.model.EquipmentDto
import ru.stankin.compose.model.Page

interface AdminEquipmentApi {

    @GET("/admin/v1/equipment/findAll")
    suspend fun findAll(
        @Query("searchText") searchText: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<CommonResponse<Page<EquipmentDto>>>
}