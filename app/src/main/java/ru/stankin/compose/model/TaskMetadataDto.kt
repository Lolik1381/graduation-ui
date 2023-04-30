package ru.stankin.compose.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.ZonedDateTime

@Parcelize
data class TaskMetadataDto(
    var id: String? = null,
    var header: String? = null,
    var description: String? = null,
    var status: TaskDto.TaskStatusDto? = null,
    var createAt: ZonedDateTime? = null,
    var updateAt: ZonedDateTime? = null
) : Parcelable