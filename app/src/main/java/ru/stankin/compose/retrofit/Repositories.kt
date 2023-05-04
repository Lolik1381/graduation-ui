package ru.stankin.compose.retrofit

import ru.stankin.compose.retrofit.network.ActuatorApi
import ru.stankin.compose.retrofit.network.AdminTaskApi
import ru.stankin.compose.retrofit.network.AdminTaskSchedulerApi
import ru.stankin.compose.retrofit.network.AdminTaskTemplateApi
import ru.stankin.compose.retrofit.network.AdminUserApi
import ru.stankin.compose.retrofit.network.TaskApi
import ru.stankin.compose.retrofit.network.UserFileApi
import ru.stankin.compose.retrofit.network.UserApi
import ru.stankin.compose.retrofit.network.UserTaskCheckApi

object Repositories {
    const val BASE_URL = "http://192.168.1.5:8080/"

    val userApi: UserApi
        get() = RetrofitClient.getClient(BASE_URL).create(UserApi::class.java)

    val taskApi: TaskApi
        get() = RetrofitClient.getClient(BASE_URL).create(TaskApi::class.java)

    val actuatorApi: ActuatorApi
        get() = RetrofitClient.getClient(BASE_URL).create(ActuatorApi::class.java)

    val adminTaskTemplateApi: AdminTaskTemplateApi
        get() = RetrofitClient.getClient(BASE_URL).create(AdminTaskTemplateApi::class.java)

    val adminUserApi: AdminUserApi
        get() = RetrofitClient.getClient(BASE_URL).create(AdminUserApi::class.java)

    val adminTaskApi: AdminTaskApi
        get() = RetrofitClient.getClient(BASE_URL).create(AdminTaskApi::class.java)

    val adminTaskSchedulerApi: AdminTaskSchedulerApi
        get() = RetrofitClient.getClient(BASE_URL).create(AdminTaskSchedulerApi::class.java)

    val userFileApi: UserFileApi
        get() = RetrofitClient.getClient(BASE_URL).create(UserFileApi::class.java)

    val userTaskCheckApi: UserTaskCheckApi
        get() = RetrofitClient.getClient(BASE_URL).create(UserTaskCheckApi::class.java)
}