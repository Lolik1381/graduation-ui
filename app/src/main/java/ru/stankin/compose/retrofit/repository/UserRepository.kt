package ru.stankin.compose.retrofit.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import ru.stankin.compose.model.CommonResponse
import ru.stankin.compose.model.RequestChangePasswordDto
import ru.stankin.compose.model.RequestLoginDto
import ru.stankin.compose.model.ResponseLoginDto
import ru.stankin.compose.retrofit.APIs
import ru.stankin.compose.retrofit.network.UserApi

class UserRepository(
    private val userApi: UserApi = APIs.userApi
) {

    fun login(loginDto: RequestLoginDto): Flow<Response<CommonResponse<ResponseLoginDto>>> = flow {
        emit(userApi.login(loginDto))
    }.flowOn(Dispatchers.IO)

    fun changePassword(changePasswordDto: RequestChangePasswordDto): Flow<Response<CommonResponse<Unit>>> = flow {
        emit(userApi.changePassword(changePasswordDto))
    }.flowOn(Dispatchers.IO)
}