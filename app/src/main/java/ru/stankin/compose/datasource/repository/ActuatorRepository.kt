package ru.stankin.compose.datasource.repository

import retrofit2.Response
import retrofit2.http.GET
import ru.stankin.compose.model.HealthDto

interface ActuatorRepository {

    @GET("/actuator/health")
    suspend fun health(): Response<HealthDto>
}