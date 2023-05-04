package ru.stankin.compose.core.util

import retrofit2.Response
import ru.stankin.compose.core.manager.JwtTokenManager
import ru.stankin.compose.core.manager.RoleManager
import ru.stankin.compose.model.CommonResponse
import ru.stankin.compose.viewmodel.state.AuthState

fun <T> Response<CommonResponse<T>>.onSuccess(action: (value: Response<CommonResponse<T>>) -> Unit): Response<CommonResponse<T>> {
    if (this.isSuccessful && this.body()?.code in 200 .. 300) {
        action(this)
    }

    return this
}

fun <T> Response<CommonResponse<T>>.onFailure(action: (value: Response<CommonResponse<T>>) -> Unit): Response<CommonResponse<T>> {
    if (!this.isSuccessful || this.body()?.code !in 200 .. 300) {
        action(this)
    }

    return this
}

fun <T> Response<CommonResponse<T>>.isError(): Boolean {
    return this.body()?.error == true || this.code() != 200
}

fun <T> Response<CommonResponse<T>>.errorMessage(): String {
    return this.body()?.message?.takeIf { it.isBlank() }
        ?: "Ошибка при получении данных."
}

fun <T> Response<CommonResponse<T>>.content(): T? {
    return this.body()?.body
}

fun <T> Response<CommonResponse<T>>.nonNullContent(): T {
    return this.body()?.body!!
}