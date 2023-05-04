package ru.stankin.compose.retrofit.network

import retrofit2.Response
import retrofit2.http.GET
import ru.stankin.compose.model.HealthDto

interface ActuatorApi {

    @GET("/actuator/health")
    suspend fun health(): Response<HealthDto>
}