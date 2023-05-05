package ru.stankin.compose.retrofit.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import ru.stankin.compose.model.CommonResponse
import ru.stankin.compose.model.EquipmentDto
import ru.stankin.compose.model.Page
import ru.stankin.compose.retrofit.APIs
import ru.stankin.compose.retrofit.network.AdminEquipmentApi

class AdminEquipmentRepository(
    private val adminEquipmentApi: AdminEquipmentApi = APIs.adminEquipmentApi
) {

    fun findAll(searchText: String? = null, page: Int = 0, size: Int = 20): Flow<Response<CommonResponse<Page<EquipmentDto>>>> = flow {
        emit(adminEquipmentApi.findAll(searchText, page, size))
    }.flowOn(Dispatchers.IO)
}