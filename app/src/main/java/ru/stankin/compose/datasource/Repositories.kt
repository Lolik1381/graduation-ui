package ru.stankin.compose.datasource

import ru.stankin.compose.datasource.repository.ActuatorRepository
import ru.stankin.compose.datasource.repository.AdminTaskRepository
import ru.stankin.compose.datasource.repository.AdminTaskSchedulerRepository
import ru.stankin.compose.datasource.repository.AdminTaskTemplateRepository
import ru.stankin.compose.datasource.repository.AdminUserRepository
import ru.stankin.compose.datasource.repository.TaskRepository
import ru.stankin.compose.datasource.repository.UserFileRepository
import ru.stankin.compose.datasource.repository.UserRepository
import ru.stankin.compose.datasource.repository.UserTaskCheckRepository

object Repositories {
    const val BASE_URL = "http://192.168.1.2:8080/"

    val userRepository: UserRepository
        get() = RetrofitClient.getClient(BASE_URL).create(UserRepository::class.java)

    val taskRepository: TaskRepository
        get() = RetrofitClient.getClient(BASE_URL).create(TaskRepository::class.java)

    val actuatorRepository: ActuatorRepository
        get() = RetrofitClient.getClient(BASE_URL).create(ActuatorRepository::class.java)

    val adminTaskTemplateRepository: AdminTaskTemplateRepository
        get() = RetrofitClient.getClient(BASE_URL).create(AdminTaskTemplateRepository::class.java)

    val adminUserRepository: AdminUserRepository
        get() = RetrofitClient.getClient(BASE_URL).create(AdminUserRepository::class.java)

    val adminTaskRepository: AdminTaskRepository
        get() = RetrofitClient.getClient(BASE_URL).create(AdminTaskRepository::class.java)

    val adminTaskSchedulerRepository: AdminTaskSchedulerRepository
        get() = RetrofitClient.getClient(BASE_URL).create(AdminTaskSchedulerRepository::class.java)

    val userFileRepository: UserFileRepository
        get() = RetrofitClient.getClient(BASE_URL).create(UserFileRepository::class.java)

    val userTaskCheckRepository: UserTaskCheckRepository
        get() = RetrofitClient.getClient(BASE_URL).create(UserTaskCheckRepository::class.java)
}