package ru.stankin.compose.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.ZonedDateTime

@Parcelize
data class TaskSchedulerDto(
    var id: String? = null,
    var taskTemplate: TaskTemplateDto? = null,
    var type: TaskSchedulerTypeDto? = null,
    var cron: String? = null,
    var triggerDate: ZonedDateTime? = null,
    var status: TaskSchedulerStatusDto? = null,
    var userId: String? = null,
    var groupId: String? = null,
    var expireDelay: Long? = null
) : Parcelable {

    enum class TaskSchedulerTypeDto(val russianValue: String) {
        CRON("Переодичность"),
        DATE("Один раз")
    }

    enum class TaskSchedulerStatusDto {
        ACTIVE, DELETED
    }
}