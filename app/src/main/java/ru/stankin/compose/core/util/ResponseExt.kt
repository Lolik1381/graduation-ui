package ru.stankin.compose.core.util

import retrofit2.Response
import ru.stankin.compose.model.CommonResponse

fun <T> Response<T>.onSuccess(action: (value: Response<T>) -> Unit): Response<T> {
    if (this.isSuccessful) {
        action(this)
    }

    return this
}

fun <T> Response<T>.onFailure(action: (value: Response<T>) -> Unit): Response<T> {
    if (!this.isSuccessful) {
        action(this)
    }

    return this
}

fun <T> Response<CommonResponse<T>>.isError(): Boolean {
    return this.body()?.error == true || this.code() != 200
}

fun <T> Response<CommonResponse<T>>.errorCode(): Int? {
    return this.body()?.code
}

fun <T> Response<CommonResponse<T>>.errorMessage(): String {
    return this.body()?.message?.takeIf { it.isBlank() }
        ?: "Ошибка при получении данных."
}

fun <T> Response<CommonResponse<T>>.content(): T? {
    return this.body()?.body
}