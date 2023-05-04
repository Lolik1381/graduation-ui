package ru.stankin.compose.retrofit.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import ru.stankin.compose.core.manager.JwtTokenManager

class AuthInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        JwtTokenManager.get()?.let { requestBuilder.addHeader("Authorization", it) }
        return chain.proceed(requestBuilder.build())
    }
}