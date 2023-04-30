package ru.stankin.compose.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.ZonedDateTime

@Parcelize
data class TaskDto(
    var id: String? = null,
    var taskTemplate: TaskTemplateDto? = null,
    var taskChecks: List<TaskCheckDto>? = null,
    var systemUserId: String? = null,
    var groupId: String? = null,
    var taskStatus: TaskStatusDto? = null,
    var createAt: ZonedDateTime? = null,
    var updateAt: ZonedDateTime? = null,
    var expireDate: ZonedDateTime? = null
) : Parcelable {

    enum class TaskStatusDto(
        val uiDescription: String
    ) {
        CREATED("Назначен"),
        IN_PROGRESS("Выполняется"),
        COMPLETE("Выполнен"),
        EXPIRED("Истек")
    }
}