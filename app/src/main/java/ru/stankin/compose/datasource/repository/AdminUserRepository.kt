package ru.stankin.compose.datasource.repository

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.stankin.compose.model.CommonResponse
import ru.stankin.compose.model.GroupDto
import ru.stankin.compose.model.Page
import ru.stankin.compose.model.UserDto

interface AdminUserRepository {

    @GET("/admin/v1/user/findAll")
    suspend fun findAll(
        @Query("searchText") searchText: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<CommonResponse<Page<UserDto>>>

    @GET("/admin/v1/user/groups")
    suspend fun findAllGroups(
        @Query("searchText") searchName: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<CommonResponse<Page<GroupDto>>>
}