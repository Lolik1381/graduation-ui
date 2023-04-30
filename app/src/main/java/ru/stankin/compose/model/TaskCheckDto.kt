package ru.stankin.compose.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TaskCheckDto(

    var id: String? = null,
    var taskId: String? = null,
    var taskTemplateCheckId: String? = null,
    var comment: String? = null,
    var controlValue: String? = null,
    var status: TaskCheckStatus? = null,
    var files: List<String>? = null
) : Parcelable {

    @Parcelize
    enum class TaskCheckStatus : Parcelable {
        ACTIVE, FINISHED
    }
}